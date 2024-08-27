package com.ramiletus.frauddetection.service.users;

import com.ramiletus.frauddetection.persistence.dao.OperatorDao;
import com.ramiletus.frauddetection.persistence.model.Operator;
import com.ramiletus.frauddetection.persistence.model.PhoneNumber;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {


    private final OperatorDao operatorDao;

    PhoneNumberServiceImpl(OperatorDao operatorDao) {
        this.operatorDao = operatorDao;
    }


    // This method creates a new PhoneNumber entity from a PhoneNumberDTO object, and saves it to the database.
    // The operator of the PhoneNumber is saved to the database if it does not exist.
    // If it exists, the existing operator is used.
    // The method returns the created PhoneNumber entity.
    @Override
    @Transactional
    public PhoneNumber createPhoneNumber(PhoneNumberDTO phoneNumber) {
        Operator operator = operatorDao.findByName(phoneNumber.getOperator())
                .orElseGet(() -> operatorDao.save(new Operator(phoneNumber.getOperator())));

        PhoneNumber phoneNumberEntity = PhoneNumberMapper.INSTANCE.phoneNumberDTOToPhoneNumber(phoneNumber);

        phoneNumberEntity.setOperator(operator);

        return phoneNumberEntity;
    }

}
