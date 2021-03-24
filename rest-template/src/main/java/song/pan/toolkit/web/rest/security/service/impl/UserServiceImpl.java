package song.pan.toolkit.web.rest.security.service.impl;

import org.springframework.stereotype.Service;
import song.pan.toolkit.web.rest.annotation.TimeLog;
import song.pan.toolkit.web.rest.annotation.Trace;
import song.pan.toolkit.web.rest.security.service.UserService;
import song.pan.toolkit.web.rest.security.service.dto.UserDetails;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    @Trace
    @TimeLog
    public UserDetails getUserById(String userId) {
        return new UserDetails(userId, "123456");
    }

    @Override
    @Trace
    @TimeLog
    public UserDetails getUserByBearerToken(String token) {
        return new UserDetails("Song Pan", "123456");
    }

}
