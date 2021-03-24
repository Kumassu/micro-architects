package song.pan.toolkit.web.rest.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiResponse<T> {
    private String id;
    private Boolean succ;
    private Integer code;
    private String msg;
    private T data;

    public ApiResponse() { }

    public ApiResponse(Boolean succ, Integer code, String msg, T data) {
        this.succ = succ;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse(Boolean.TRUE, HttpStatus.OK.value(), null, null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse(Boolean.TRUE, HttpStatus.OK.value(), null, data);
    }

    public static <T> ApiResponse<T> fail(int code, String msg) {
        return new ApiResponse(Boolean.FALSE, code, msg, null);
    }

}