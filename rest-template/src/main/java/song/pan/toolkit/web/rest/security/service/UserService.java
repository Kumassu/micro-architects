package song.pan.toolkit.web.rest.security.service;

import song.pan.toolkit.web.rest.security.service.dto.UserDetails;

/**
 * @author Song Pan
 * @version 1.0.0
 */
public interface UserService {


    UserDetails getUserById(String userId);


    UserDetails getUserByBearerToken(String token);

}
