package com.ramiletus.frauddetection.service.users;

import com.ramiletus.frauddetection.persistence.dao.UserDao;
import com.ramiletus.frauddetection.persistence.model.User;
import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UsersCommandHandlerImpl implements UsersCommandHandler {

    private final PhoneNumberService phoneNumberService;
    private final UserDao userDao;

    public UsersCommandHandlerImpl(UserDao userDao, PhoneNumberService phoneNumberService) {
        this.userDao = userDao;
        this.phoneNumberService = phoneNumberService;
    }

    @Override
    public User handle(InjectUserCommand command) {
        User user = new User();
        user.setName(command.getName());
        user.setEmail(command.getEmail());
        user.setPhoneNumbers(new HashSet<>());

        List<PhoneNumberDTO> phoneNumberDTOs = command.getPhoneNumbers();
        phoneNumberDTOs.forEach(phoneNumberDTO ->
            user.getPhoneNumbers().add(phoneNumberService.createPhoneNumber(phoneNumberDTO))
        );

        return userDao.save(user);
    }
}
