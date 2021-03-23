package song.pan.toolkit.web.rest.exception;


public class IllegalParameterException extends GeneralException {

    public IllegalParameterException() {
        super(ErrorType.NOT_ACCEPTABLE.getCode());
    }

    public IllegalParameterException(String msg) {
        super(ErrorType.NOT_ACCEPTABLE.getCode(), msg);
    }

    public IllegalParameterException(Throwable e) {
        super(ErrorType.NOT_ACCEPTABLE.getCode(), e);
    }

}
