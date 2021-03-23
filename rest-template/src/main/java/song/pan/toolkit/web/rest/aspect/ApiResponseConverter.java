package song.pan.toolkit.web.rest.aspect;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import song.pan.toolkit.web.rest.domain.ApiResponse;

/**
 * Convert all the response to {@link ApiResponse}
 *
 * @author Song Pan
 * @version 1.0.0
 */
@RestControllerAdvice
public class ApiResponseConverter implements ResponseBodyAdvice<Object> {


    /**
     * Only convert the methods with annotation {@link ResponseBody}
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class)
                || returnType.hasMethodAnnotation(ResponseBody.class);
    }


    /**
     * Convert response body to {@link ApiResponse}
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        String path = request.getURI().getPath();
        if (!supports(returnType, selectedConverterType) || body instanceof ApiResponse
                || path.contains("swagger") || path.equals("/v2/api-docs")) {
            return body;
        }
        return ApiResponse.ok(body);
    }
}
