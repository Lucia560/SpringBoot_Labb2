package org.example.Application.ApplicationListener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


import java.util.logging.Logger;
@Component
public class StartupApplicationListener {

    private static final Logger LOG
            = Logger.getLogger(StartupApplicationListener.class.getName());

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOG.info("Application started!");
    }
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.info("Application context has been refreshed.");
    }
}