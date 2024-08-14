package com.ramiletus.frauddetection.service.users;

import com.ramiletus.frauddetection.persistence.dao.UserDao;
import com.ramiletus.frauddetection.persistence.model.User;
import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UsersCommandHandlerImpl implements UsersCommandHandler {

    UserDao userDao;

    public UsersCommandHandlerImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void handleInjectUser(InjectUserCommand command) {
        User user = new User();
        user.setName(command.getName());
        user.setEmail(command.getEmail());
        user.setPhoneNumbers(new HashSet<>(PhoneNumberMapper.INSTANCE.phoneNumberDTOListToPhoneNUmberList(command.getPhoneNumbers())));
        userDao.save(user);
    }
}
