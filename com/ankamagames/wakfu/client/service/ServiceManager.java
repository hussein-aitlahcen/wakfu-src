package com.ankamagames.wakfu.client.service;

import org.apache.log4j.*;
import java.util.*;

final class ServiceManager implements IServiceManager
{
    private final List<IService> m_services;
    private final Logger m_logger;
    
    ServiceManager(final List<IService> services) {
        super();
        this.m_logger = Logger.getLogger((Class)ServiceManager.class);
        this.m_services = services;
    }
    
    @Override
    public void startServices() {
        for (final IService service : this.m_services) {
            this.m_logger.info((Object)("Starting " + service + "..."));
            service.start();
        }
    }
    
    @Override
    public void stopServices() {
        for (final IService service : this.m_services) {
            this.m_logger.info((Object)("Stopping " + service + "..."));
            service.stop();
        }
    }
}
