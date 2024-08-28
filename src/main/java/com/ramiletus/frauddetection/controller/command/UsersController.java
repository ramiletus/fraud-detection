package com.ramiletus.frauddetection.controller.command;

import com.ramiletus.frauddetection.service.users.UsersCommandHandler;
import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersCommandHandler usersCommandHandler;

    public UsersController(UsersCommandHandler usersCommandHandler) {
        this.usersCommandHandler = usersCommandHandler;
    }

    @PostMapping(value = "/inject")
    public void injectUsers(@Valid @RequestBody InjectUserCommand injectUserCommand) {
        usersCommandHandler.handleInjectUser(injectUserCommand);
    }

}
