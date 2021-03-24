package song.pan.toolkit.web.rest.security.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Getter
@Setter
public class UserDetails {

    private String userId;
    private String password;

    public UserDetails(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
