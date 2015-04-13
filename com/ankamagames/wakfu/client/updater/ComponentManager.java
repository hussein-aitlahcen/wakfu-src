package com.ankamagames.wakfu.client.updater;

import com.ankamagames.wakfu.client.core.*;
import org.apache.log4j.*;
import com.google.common.collect.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.preloading.*;
import java.util.*;

class ComponentManager implements IComponentManager
{
    private final WakfuConfiguration m_configuration;
    private State m_state;
    private ProgressInformation m_progressInformation;
    private final Map<String, ComponentInformation> m_components;
    private final Logger m_logger;
    
    ComponentManager(final WakfuConfiguration configuration) {
        super();
        this.m_state = State.UNKNOWN;
        this.m_components = (Map<String, ComponentInformation>)Maps.newHashMap();
        this.m_logger = Logger.getLogger((Class)ComponentManager.class);
        this.m_configuration = configuration;
    }
    
    @Override
    public synchronized void updateComponentInformation(final String label, final int priority, final boolean completed) {
        final ComponentInformation componentInformation = this.createComponentInformation(label, priority, completed);
        if (componentInformation.equals(this.m_components.get(this.key(label)))) {
            return;
        }
        this.m_components.put(this.key(label), componentInformation);
        Worker.getInstance().pushMessage(new UIPreloadingComponentMessage((short)18052, Component.byName(label), completed));
        this.m_logger.info((Object)("Component " + label + ": " + (completed ? "" : "not ") + "completed."));
    }
    
    @Override
    public synchronized void changeState(final State newState) {
        if (this.m_state == newState) {
            return;
        }
        this.m_state = newState;
        Worker.getInstance().pushMessage(new UIPreloadingStateMessage((short)18050, this.m_state));
        this.m_logger.info((Object)("Update state change to: " + this.m_state));
    }
    
    @Override
    public synchronized void updateProgessInformation(final double progress, final int estimatedTime) {
        final ProgressInformation progressInformation = new ProgressInformation((int)(progress * 100.0) / 100.0, estimatedTime / 1000);
        if (progressInformation.equals(this.m_progressInformation)) {
            return;
        }
        this.m_progressInformation = progressInformation;
        Worker.getInstance().pushMessage(new UIPreloadingProgressMessage((short)18051, progress, estimatedTime));
        this.m_logger.info((Object)("Update Progress: " + progressInformation.getProgress() + ", ETA: " + progressInformation.getEstimatedTime() + "s."));
    }
    
    @Override
    public synchronized boolean hasComponentsCompleted(final Component expectedComponent) {
        if (!this.updaterIsConfigured()) {
            return true;
        }
        if (this.m_configuration.initialUpdateStateIsFullyInstalled()) {
            return true;
        }
        if (this.m_state == State.UP_TO_DATE) {
            return true;
        }
        if (!this.componentIsCompleted(expectedComponent)) {
            return false;
        }
        for (final Component neededComponent : expectedComponent.getNeededComponents()) {
            if (!this.componentIsCompleted(neededComponent)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean componentIsCompleted(final Component expectedComponent) {
        final ComponentInformation component = this.m_components.get(this.key(expectedComponent.name()));
        return component != null && component.isCompleted();
    }
    
    private ComponentInformation createComponentInformation(final String label, final int priority, final boolean completed) {
        return new ComponentInformation(label, priority, completed);
    }
    
    private String key(final String label) {
        return label.toLowerCase();
    }
    
    private boolean updaterIsConfigured() {
        return this.m_configuration.getInteger("UPDATER_COMMUNICATION_PORT", 0) != 0;
    }
    
    synchronized State getState() {
        return this.m_state;
    }
    
    synchronized ProgressInformation getProgressInformation() {
        return this.m_progressInformation;
    }
    
    class ProgressInformation
    {
        private final double m_progress;
        private final int m_estimatedTime;
        
        ProgressInformation(final double progress, final int estimatedTime) {
            super();
            this.m_progress = progress;
            this.m_estimatedTime = estimatedTime;
        }
        
        double getProgress() {
            return this.m_progress;
        }
        
        int getEstimatedTime() {
            return this.m_estimatedTime;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final ProgressInformation that = (ProgressInformation)o;
            return this.m_estimatedTime == that.m_estimatedTime && Double.compare(that.m_progress, this.m_progress) == 0;
        }
        
        @Override
        public int hashCode() {
            final long temp = Double.doubleToLongBits(this.m_progress);
            int result = (int)(temp ^ temp >>> 32);
            result = 31 * result + this.m_estimatedTime;
            return result;
        }
    }
    
    class ComponentInformation
    {
        private final String m_name;
        private final int m_priority;
        private final boolean m_completed;
        
        ComponentInformation(final String name, final int priority, final boolean completed) {
            super();
            this.m_name = name;
            this.m_priority = priority;
            this.m_completed = completed;
        }
        
        String getName() {
            return this.m_name;
        }
        
        int getPriority() {
            return this.m_priority;
        }
        
        boolean isCompleted() {
            return this.m_completed;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final ComponentInformation that = (ComponentInformation)o;
            return this.m_completed == that.m_completed && this.m_priority == that.m_priority && this.m_name.equals(that.m_name);
        }
        
        @Override
        public int hashCode() {
            int result = this.m_name.hashCode();
            result = 31 * result + this.m_priority;
            result = 31 * result + (this.m_completed ? 1 : 0);
            return result;
        }
    }
}
