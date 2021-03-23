package song.pan.toolkit.web.rest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    //~ client error

    /**
     * {@link HttpStatus#UNAUTHORIZED}
     */
    UNAUTHORIZED(401),

    /**
     * {@link HttpStatus#FORBIDDEN}
     */
    FORBIDDEN(403),

    /**
     * {@link HttpStatus#NOT_FOUND}
     */
    NOT_FOUND(404),


    /**
     * {@link HttpStatus#NOT_ACCEPTABLE}
     */
     NOT_ACCEPTABLE(406),


    //~ server error

    /**
     * {@link HttpStatus#INTERNAL_SERVER_ERROR}
     */
    SERVER_ERROR(500),
    ;


    private int code;

    ErrorType(int code) {
        this.code = code;
    }
}
