package com.ankamagames.framework.kernel.core.net.netty;

import io.netty.channel.socket.*;
import com.ankamagames.framework.kernel.core.net.netty.encoder.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.events.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.net.netty.decoder.*;
import com.ankamagames.framework.kernel.core.net.netty.handler.*;
import io.netty.channel.*;

public class Initializer extends ChannelInitializer<SocketChannel>
{
    private static final LogHandler LOG_HANDLER;
    public static final MessageEncoder ENCODER;
    public static final ByteMessageEncoder BYTE_ENCODER;
    private final ProtocolAdapter m_protocol;
    private final List<WakfuMessageDecoder> m_decoders;
    private final SimpleObjectFactory<? extends FrameworkEntity> m_entityFactory;
    private final NetworkEventsHandler m_eventsHandler;
    
    public Initializer(final ProtocolAdapter protocol, final Collection<WakfuMessageDecoder> decoders, final SimpleObjectFactory<? extends FrameworkEntity> entityFactory, final NetworkEventsHandler eventsHandler) {
        super();
        this.m_decoders = new ArrayList<WakfuMessageDecoder>();
        this.m_protocol = protocol;
        this.m_entityFactory = entityFactory;
        this.m_eventsHandler = eventsHandler;
        this.m_decoders.addAll(decoders);
    }
    
    public void initChannel(final SocketChannel ch) throws Exception {
        final ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ChannelHandler[] { new MessageDecoder(this.m_protocol, this.m_decoders) });
        pipeline.addLast(new ChannelHandler[] { Initializer.BYTE_ENCODER });
        pipeline.addLast(new ChannelHandler[] { Initializer.ENCODER });
        pipeline.addLast(new ChannelHandler[] { Initializer.LOG_HANDLER });
        pipeline.addLast(new ChannelHandler[] { new MessageHandler(this.m_entityFactory, this.m_eventsHandler) });
    }
    
    public String toString() {
        return "ServerInitializer{m_decoders=" + this.m_decoders.size() + ", m_entityFactory=" + this.m_entityFactory + '}';
    }
    
    static {
        LOG_HANDLER = new LogHandler();
        ENCODER = new MessageEncoder();
        BYTE_ENCODER = new ByteMessageEncoder();
    }
}
