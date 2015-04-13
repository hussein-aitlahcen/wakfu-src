package com.ankamagames.framework.kernel.core.common.message;

import org.apache.log4j.*;
import java.util.concurrent.atomic.*;

public abstract class Message
{
    protected static final Logger m_logger;
    long m_workerTimeStamp;
    private final AtomicReference<MessageHandler> m_messageHandler;
    
    public Message() {
        super();
        this.m_messageHandler = new AtomicReference<MessageHandler>();
    }
    
    public MessageHandler getHandler() {
        return this.m_messageHandler.get();
    }
    
    public void setHandler(final MessageHandler handler) {
        this.m_messageHandler.set(handler);
    }
    
    public void execute() {
        if (this.m_messageHandler != null) {
            final MessageHandler handler = this.m_messageHandler.get();
            if (handler != null) {
                handler.onMessage(this);
            }
            else {
                Message.m_logger.warn((Object)("Le message de type " + this.getClass().getSimpleName() + " n'a pas de destinataire."));
            }
        }
        else {
            Message.m_logger.warn((Object)("No handler validator for message " + this));
        }
    }
    
    public abstract byte[] encode();
    
    public abstract boolean decode(final byte[] p0);
    
    public abstract int getId();
    
    public void setId(final int id) {
    }
    
    public boolean checkMessageSize(final int size, final int expectedSize, final boolean bExactSize) {
        if (bExactSize) {
            if (size != expectedSize) {
                Message.m_logger.error((Object)("****************************** Message de longueur incorrecte : re\u00e7u=" + size + " octet(s), attendu=" + expectedSize + " octet(s), type : " + this.getClass().getName()), (Throwable)new Exception("TRACE"));
                return false;
            }
        }
        else if (size < expectedSize) {
            Message.m_logger.error((Object)("****************************** Message de longueur incorrecte : re\u00e7u=" + size + " octet(s), attendu >= " + expectedSize + " octet(s), type : " + this.getClass().getName()), (Throwable)new Exception("TRACE"));
            return false;
        }
        return true;
    }
    
    void setWorkerTimeStamp(final long workerTimeStamp) {
        this.m_workerTimeStamp = workerTimeStamp;
    }
    
    public long getWorkerTimeStamp() {
        return this.m_workerTimeStamp;
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + '@' + Integer.toHexString(this.hashCode()) + ", listener : " + this.getHandler();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Message.class);
    }
}
