package com.ankamagames.framework.kernel.core.net.netty.handler;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.net.netty.*;
import io.netty.channel.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class MessageHandler extends SimpleChannelInboundHandler<Message>
{
    private final SimpleObjectFactory<? extends FrameworkEntity> m_entityFactory;
    private final NetworkEventsHandler m_eventsHandler;
    private FrameworkEntity m_entity;
    private ConnectionCtx m_connection;
    
    public MessageHandler(final SimpleObjectFactory<? extends FrameworkEntity> entityFactory, final NetworkEventsHandler eventsHandler) {
        super();
        this.m_entityFactory = entityFactory;
        this.m_eventsHandler = eventsHandler;
    }
    
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        this.m_entity = (FrameworkEntity)this.m_entityFactory.createNew();
        this.m_connection = new ConnectionCtx(ctx, this.m_entity);
        this.m_entity.setConnection(this.m_connection);
        this.m_entity.onConnect();
        this.m_eventsHandler.onNewConnection(this.m_connection);
        ctx.fireChannelActive();
    }
    
    public void channelRead0(final ChannelHandlerContext ctx, final Message msg) {
        if (msg.getHandler() == null) {
            msg.setHandler(this.m_entity);
        }
        Worker.getInstance().pushMessage(msg);
        ctx.fireChannelRead((Object)msg);
    }
    
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.m_entity.onDisconnect();
        this.m_eventsHandler.onConnectionClose(this.m_connection);
        ctx.fireChannelInactive();
    }
    
    public String toString() {
        return "ClientMessageHandler{m_entityFactory=" + this.m_entityFactory + ", m_eventsHandler=" + this.m_eventsHandler + ", m_entity=" + (this.m_entity != null) + ", m_connection=" + this.m_connection + '}';
    }
}
