package com.smsa.backend.controller.scheduler;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EmailSchedular {
    private static final Logger logger = LoggerFactory.getLogger(EmailSchedular.class);

    @Scheduled(cron = "0 30 0 ? * *") //after every 30 min
    public void markExpired(){
        logger.info("hello murtaza is my name"+new Date());
    }


}
