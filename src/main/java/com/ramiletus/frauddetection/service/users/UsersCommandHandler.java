package com.ramiletus.frauddetection.service.users;

import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;

public interface UsersCommandHandler {
    void handleInjectUser(InjectUserCommand command);
}
