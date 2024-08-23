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
