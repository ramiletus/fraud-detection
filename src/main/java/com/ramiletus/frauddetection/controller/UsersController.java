package com.ramiletus.frauddetection.controller;

import com.ramiletus.frauddetection.service.users.UsersCommandHandler;
import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

    private final UsersCommandHandler usersCommandHandler;

    public UsersController(UsersCommandHandler usersCommandHandler) {
        this.usersCommandHandler = usersCommandHandler;
    }

    @PostMapping(value = "/users")
    public void injectUsers(@RequestBody InjectUserCommand injectUserCommand) {
        usersCommandHandler.handleInjectUser(injectUserCommand);
    }


}
