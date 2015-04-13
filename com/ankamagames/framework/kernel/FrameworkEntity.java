package com.ankamagames.framework.kernel;

import com.ankamagames.framework.kernel.core.net.netty.*;
import java.util.concurrent.atomic.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.google.common.base.*;
import io.netty.channel.*;

public class FrameworkEntity extends FrameHandler
{
    protected ConnectionCtx m_connection;
    protected static final AtomicInteger ENTITY_ID;
    
    public FrameworkEntity() {
        super();
        this.setRunningFrame(false);
        this.setId(FrameworkEntity.ENTITY_ID.getAndDecrement());
    }
    
    public void onConnect() {
    }
    
    public void onReconnect() {
        FrameworkEntity.m_logger.info((Object)"FrameworkEntity::onReConnect()");
    }
    
    public void onDisconnect() {
    }
    
    public void setConnection(final ConnectionCtx connection) {
        this.m_connection = connection;
    }
    
    public ConnectionCtx getConnection() {
        return this.m_connection;
    }
    
    public synchronized void closeConnection() {
        if (this.m_connection != null) {
            this.m_connection.close();
        }
    }
    
    public synchronized boolean isConnected() {
        return this.m_connection != null && this.m_connection.isConnected();
    }
    
    public Optional<ChannelFuture> sendMessage(final Message message) {
        return this.sendMessage(message, false);
    }
    
    public synchronized Optional<ChannelFuture> sendMessage(final Message message, final boolean batch) {
        try {
            return (Optional<ChannelFuture>)Optional.of((Object)this.m_connection.pushMessage(message));
        }
        catch (RuntimeException e) {
            FrameworkEntity.m_logger.error((Object)("Exception lors de l'\u00e9criture du message " + message), (Throwable)e);
            return (Optional<ChannelFuture>)Optional.absent();
        }
    }
    
    static {
        ENTITY_ID = new AtomicInteger(-1);
    }
}
