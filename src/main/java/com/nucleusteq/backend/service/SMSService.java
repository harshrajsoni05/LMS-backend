package com.nucleusteq.backend.service;
import org.springframework.stereotype.Service;


@Service
public class SMSService {

    // Method to send an SMS
    public void sendSMS(String toPhoneNumber, String messageBody) {
        // Simulate SMS sending by printing to the console
        System.out.println("Sending SMS...");
        System.out.println("To: " + toPhoneNumber);
        System.out.println("Message: " + messageBody);
        System.out.println("SMS sent successfully!");
    }
}


