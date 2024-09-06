package com.ramiletus.frauddetection.service.users;

import com.ramiletus.frauddetection.persistence.model.User;
import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;

public interface UsersCommandHandler {
    User handle(InjectUserCommand command);
}
