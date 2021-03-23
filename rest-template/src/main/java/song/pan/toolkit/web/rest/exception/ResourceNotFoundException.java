package song.pan.toolkit.web.rest.exception;


public class ResourceNotFoundException extends GeneralException {

    public ResourceNotFoundException() {
        super(ErrorType.NOT_FOUND.getCode());
    }

    public ResourceNotFoundException(String msg) {
        super(ErrorType.NOT_FOUND.getCode(), msg);
    }

    public ResourceNotFoundException(Throwable e) {
        super(ErrorType.NOT_FOUND.getCode(), e);
    }

}
