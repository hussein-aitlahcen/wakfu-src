package com.ankamagames.wakfu.client.ui.systemMessage;

import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class WakfuSystemMessageManager implements MessageHandler
{
    private static final WakfuSystemMessageManager m_instance;
    private LinkedList<SystemMessageData> m_systemMessage;
    private AbstractSystemMessage m_currentMessage;
    
    private WakfuSystemMessageManager() {
        super();
        this.m_systemMessage = new LinkedList<SystemMessageData>();
        this.m_currentMessage = null;
    }
    
    public static WakfuSystemMessageManager getInstance() {
        return WakfuSystemMessageManager.m_instance;
    }
    
    public void showMessage(final SystemMessageData data) {
        for (final SystemMessageData d : this.m_systemMessage) {
            if (!d.getType().getSystemMessage().isBlocking()) {
                this.m_systemMessage.remove(d);
            }
        }
        if (this.m_currentMessage == null) {
            (this.m_currentMessage = data.getType().getSystemMessage()).showMessage(data);
        }
        else if (this.m_currentMessage.isBlocking()) {
            final AbstractSystemMessage m = data.getType().getSystemMessage();
            if (m.isBlocking()) {
                this.m_systemMessage.addLast(data);
            }
        }
        else {
            this.m_currentMessage.hide(this.m_currentMessage.getType() != data.getType());
            (this.m_currentMessage = data.getType().getSystemMessage()).showMessage(data);
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (this.m_currentMessage != null) {
            if (!this.m_systemMessage.isEmpty()) {
                final SystemMessageData data = this.m_systemMessage.removeFirst();
                this.m_currentMessage.hide(this.m_currentMessage.getType() != data.getType());
                (this.m_currentMessage = data.getType().getSystemMessage()).showMessage(data);
            }
            else {
                this.m_currentMessage.hide(true);
                this.m_currentMessage = null;
            }
        }
        return false;
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_instance = new WakfuSystemMessageManager();
    }
    
    public enum SystemMessageType
    {
        SYS_INFO((AbstractSystemMessage)new NotificationSystemMessage()), 
        ADMIN_INFO((AbstractSystemMessage)new AdminSystemMessage()), 
        AMBIENCE_ZONE_INFO((AbstractSystemMessage)new AmbianceSystemMessage()), 
        AMBIENCE_ZONE_INFO_2((AbstractSystemMessage)new ImageSystemMessage()), 
        WORLD_INFO((AbstractSystemMessage)new WorldSystemMessage()), 
        FIGHT_INFO((AbstractSystemMessage)new FightSystemMessage()), 
        CHALLENGE((AbstractSystemMessage)new ChallengeSystemMessage());
        
        private final AbstractSystemMessage m_systemMessage;
        
        private SystemMessageType(final AbstractSystemMessage systemMessage) {
            this.m_systemMessage = systemMessage;
        }
        
        public AbstractSystemMessage getSystemMessage() {
            return this.m_systemMessage;
        }
    }
}
