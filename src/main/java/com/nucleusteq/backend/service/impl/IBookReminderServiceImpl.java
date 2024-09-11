package com.nucleusteq.backend.service.impl;

import com.nucleusteq.backend.entity.Issuance;
import com.nucleusteq.backend.repository.IssuanceRepository;
import com.nucleusteq.backend.service.IBookReminderService;
import com.nucleusteq.backend.service.ISMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IBookReminderServiceImpl implements IBookReminderService {

    private final IssuanceRepository issuanceRepository;
    private final ISMSService ismsService;



    @Override
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Kolkata")
    public void sendReturnReminder() {
        LocalDateTime startOfTomorrow = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfTomorrow = startOfTomorrow.plusDays(1).minusSeconds(1);
        List<Issuance> dueTomorrow = issuanceRepository.findIssuancesByReturnDateBetweenAndStatus(startOfTomorrow, endOfTomorrow, "Issued");

        System.out.println("SCHEDULER CALLED" + dueTomorrow);
        for (Issuance issuance : dueTomorrow) {
            String message = String.format("\nReminder:\n" +
                            "Please return the book '%s'\n" +
                            "Author '%s'\n"+
                            "by tomorrow (%s).",
                    issuance.getBook().getTitle(), issuance.getBook().getAuthor(),
                    issuance.getReturn_date().toLocalDateTime().toLocalDate().toString());

            System.out.println(message);
//            ismsService.sendSms(issuance.getUser().getNumber(), message);
        }
    }
}
