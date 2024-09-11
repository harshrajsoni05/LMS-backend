package com.nucleusteq.backend.service;

import org.springframework.stereotype.Service;

public interface ISMSService {

    void verifyNumber(String number);
    void sendSms(String toMobileNumber, String message);
}
