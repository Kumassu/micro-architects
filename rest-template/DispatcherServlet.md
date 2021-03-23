入口在`doService`方法中, springmvc所有请求的入口都是这一个servlet:
```
/**
 * Exposes the DispatcherServlet-specific request attributes and delegates to {@link #doDispatch}
 * for the actual dispatching.
 */
@Override
protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
    ...
    try {
        doDispatch(request, response);
    }
    ...
}
```

`doDispatch`:
主要做了6件事
- 找到 HandlerExecutionChain
- 找到 HandlerAdapter
- 使用 HandlerExecutionChain 调用 interceptor的 PreHandle 方法
- 调用 HandlerAdapter 的 handle 方法
- 使用 HandlerExecutionChain 调用 interceptor的 PostHandle 方法
- processDispatchResult, 将 ModelAndView 渲染成期望结果

HandlerExecutionChain 一看名字就是责任链模式，里面存了实际处理请求的 handler 对象，还有拦截器
获取 HandlerExecutionChain 是通过遍历所有的 HandlerMapping, 会返回第一个支持当前请求的HandlerMapping
HandlerMapping 是定义请求和 handler 之间的关系，所以叫mapping.
SpringBoot 默认会有 6 个HandlerMapping:
- PropertySourceRequestMappingHandlerMapping : 来自swagger2, 用于找寻 [/v2/api-docs] 
- RequestMappingHandlerMapping : 找到用 @RequestMapping 注解的方法, 将方法封装成 HandlerMethod, 用反射调用
- BeanNameUrlHandlerMapping : 会找容器中与 url 同名的 bean / Controller
- RouteFunctionMapping :  支持 RouterFunction 和 HandlerFunction
- SimpleUrlHandlerMapping : 根据 url 去容器中查找 bean, 可以使用正则匹配
- WelcomePageHandlerMapping : 设置首页, 即 index.html

在调用 handler 的时候, 并不是直接使用 handler 对象,
handler 对象有多种类型, 并没有统一的接口, 所以要使用一个适配器进行调用, 即 HandlerAdapter。
HandlerAdapter 也是通过遍历已知的 HandlerAdapter 列表, 返回第一个支持对应 handler 的 HandlerAdapter。
默认的 HandlerAdapter 有 4 个 :
- RequestMappingHandlerAdapter : handler 为 HandlerMethod, 反射调用
- HandlerFunctionAdapter : handler 为 HandlerFunction
- HttpRequestHandlerAdapter : handler 为 HttpRequestHandler, 这种 handler 没有返回值, 所以叫做 Plain handler
- SimpleControllerHandlerAdapter : handler 为 Controller, 这种 handler 有返回值, 返回 ModelAndView

```
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpServletRequest processedRequest = request;
    HandlerExecutionChain mappedHandler = null;
    boolean multipartRequestParsed = false;

    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

    try {
        ModelAndView mv = null;
        Exception dispatchException = null;

        try {
            processedRequest = checkMultipart(request);
            multipartRequestParsed = (processedRequest != request);

            // Determine handler for the current request.
            mappedHandler = getHandler(processedRequest);
            if (mappedHandler == null) {
                noHandlerFound(processedRequest, response);
                return;
            }

            // Determine handler adapter for the current request.
            HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

            // Process last-modified header, if supported by the handler.
            String method = request.getMethod();
            boolean isGet = "GET".equals(method);
            if (isGet || "HEAD".equals(method)) {
                long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
                    return;
                }
            }

            if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                return;
            }

            // Actually invoke the handler.
            mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

            if (asyncManager.isConcurrentHandlingStarted()) {
                return;
            }

            applyDefaultViewName(processedRequest, mv);
            mappedHandler.applyPostHandle(processedRequest, response, mv);
        }
        catch (Exception ex) {
            dispatchException = ex;
        }
        catch (Throwable err) {
            // As of 4.3, we're processing Errors thrown from handler methods as well,
            // making them available for @ExceptionHandler methods and other scenarios.
            dispatchException = new NestedServletException("Handler dispatch failed", err);
        }
        processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
    }
    catch (Exception ex) {
        triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
    }
    catch (Throwable err) {
        triggerAfterCompletion(processedRequest, response, mappedHandler,
                new NestedServletException("Handler processing failed", err));
    }
    finally {
        if (asyncManager.isConcurrentHandlingStarted()) {
            // Instead of postHandle and afterCompletion
            if (mappedHandler != null) {
                mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
            }
        }
        else {
            // Clean up any resources used by a multipart request.
            if (multipartRequestParsed) {
                cleanupMultipart(processedRequest);
            }
        }
    }
}
```
`render`方法中会调用View的render方法, 但是实际将返回值序列化为json的逻辑并不在render方法里面, 而是在 handler 方法里面.
有一个 HandlerMethodReturnValueHandler 专门用来处理 handler 返回值, 实现类是 RequestResponseBodyMethodProcessor，
具体的逻辑在 AbstractMessageConverterMethodProcessor#writeWithMessageConverters,
会调用所有的 HttpMessageConverter 方法和 ResponseBodyAdvice 的方法进行转换.
```
/**
 * Render the given ModelAndView. This is the last stage in handling a request. It may involve resolving the view by name.
 */
protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    需要注意这里有个 resolveViewName 的逻辑, 如果 handler 返回的 ModelAndView 是有 viewName 的，
    那么会从 ViewResolver 列表中遍历可以解析 ViewName 的 ViewResolver, 一般来说会造成重定向

    View view;
    String viewName = mv.getViewName();
    if (viewName != null) {
        // We need to resolve the view name.
        view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
        if (view == null) {
            throw new ServletException("Could not resolve view with name '" + mv.getViewName() +
                    "' in servlet with name '" + getServletName() + "'");
        }
    }
    else {
        // No need to lookup: the ModelAndView object contains the actual View object.
        view = mv.getView();
        if (view == null) {
            throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a " +
                    "View object in servlet with name '" + getServletName() + "'");
        }
    }


    ...
    try {
        view.render(mv.getModelInternal(), request, response);
    }
    ...
}
```