package song.pan.toolkit.web.rest.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Getter
@Setter
public class TracedMethod {


    private Boolean succ;
    private Exception error;
    private long timeCostMs;

    private String method;
    private List<Argument> args;
    private Object returns;



    @Getter
    @Setter
    public static class Argument {
        private String name;
        private Object value;
        private String cls;
    }



}
