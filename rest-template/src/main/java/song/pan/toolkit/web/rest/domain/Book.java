package song.pan.toolkit.web.rest.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Getter
@Setter
public class Book {

    private String name;
    private Author author;
    private Date publishDate;

}
