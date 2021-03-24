package song.pan.toolkit.web.rest.exception;

public class SystemException extends GeneralException {


    public SystemException() {
        super(ErrorType.SERVER_ERROR.getCode());
    }

    public SystemException(String msg) {
        super(ErrorType.SERVER_ERROR.getCode(), msg);
    }

    public SystemException(Throwable e) {
        super(ErrorType.SERVER_ERROR.getCode(), e);
    }



}
