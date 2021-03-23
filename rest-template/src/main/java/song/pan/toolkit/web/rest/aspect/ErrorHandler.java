package song.pan.toolkit.web.rest.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import song.pan.toolkit.web.rest.domain.ApiResponse;
import song.pan.toolkit.web.rest.exception.ErrorType;
import song.pan.toolkit.web.rest.exception.GeneralException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {



    @ExceptionHandler
    public Object handle(Throwable e, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSucc(Boolean.FALSE);

        // may wrapped
        while (e.getCause() != null) {
            e = e.getCause();
            if (e instanceof GeneralException) {
                break;
            }
        }

        // set error code
        if (e instanceof GeneralException) {
            apiResponse.setCode(((GeneralException) e).getCode());
        } else {
            apiResponse.setCode(ErrorType.SERVER_ERROR.getCode());
        }
        httpResponse.setStatus(apiResponse.getCode());

        // localize error message
        apiResponse.setMsg(e.getClass().getSimpleName() + (e.getMessage() == null ? "" : " : " + e.getMessage()));

        // log stacktrace
        boolean logStacktrace = apiResponse.getCode() >= ErrorType.SERVER_ERROR.getCode();
        if (logStacktrace) {
            log.error(e.getClass().getName(), e);
        } else {
            log.info("{} : {}", e.getClass().getName(), e.getMessage());
        }

        return apiResponse;
    }


}
