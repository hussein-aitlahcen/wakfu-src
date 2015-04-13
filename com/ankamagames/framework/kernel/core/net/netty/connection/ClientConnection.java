package com.ankamagames.framework.kernel.core.net.netty.connection;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.net.netty.decoder.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.events.*;
import io.netty.channel.nio.*;
import io.netty.bootstrap.*;
import io.netty.channel.socket.nio.*;
import com.ankamagames.framework.kernel.core.net.netty.*;
import java.util.*;
import java.util.concurrent.*;
import io.netty.channel.*;
import org.jetbrains.annotations.*;
import io.netty.util.concurrent.*;
import org.apache.log4j.*;

public final class ClientConnection
{
    private static final int N_THREADS = 0;
    static final int RETRY_DELAY = 5;
    private final List<WakfuMessageDecoder> m_decoders;
    private final String m_host;
    private final int m_port;
    private final ProtocolAdapter m_protocol;
    private final SimpleObjectFactory<? extends FrameworkEntity> m_entityFactory;
    private final NetworkEventsHandler m_eventsHandler;
    
    public ClientConnection(final String host, final int port, final ProtocolAdapter protocol, final SimpleObjectFactory<? extends FrameworkEntity> entityFactory, final NetworkEventsHandler eventsHandler) {
        super();
        this.m_decoders = new ArrayList<WakfuMessageDecoder>();
        this.m_host = host;
        this.m_port = port;
        this.m_protocol = protocol;
        this.m_entityFactory = entityFactory;
        this.m_eventsHandler = eventsHandler;
    }
    
    public void addDecoder(final WakfuMessageDecoder decoder) {
        this.m_decoders.add(decoder);
    }
    
    public boolean start(final boolean blocking, final boolean reconnecting) throws InterruptedException {
        final EventLoopGroup group = (EventLoopGroup)new NioEventLoopGroup(0, (ThreadFactory)new GroupThreadFactory(this.m_host, this.m_port));
        final Bootstrap b = new Bootstrap();
        ((Bootstrap)((Bootstrap)b.group(group)).remoteAddress(this.m_host, this.m_port).channel((Class)NioSocketChannel.class)).handler((ChannelHandler)new Initializer(this.m_protocol, this.m_decoders, this.m_entityFactory, this.m_eventsHandler));
        final ChannelFutureListener closeListener = (ChannelFutureListener)(reconnecting ? new ReconnectListener(b) : new ShutdownListener(group));
        return blocking ? startBlocking(b, closeListener) : startNonBlocking(b, closeListener);
    }
    
    private static boolean startBlocking(final Bootstrap b, final ChannelFutureListener closeListener) throws InterruptedException {
        final ChannelFuture future = b.connect();
        future.await(5L, TimeUnit.SECONDS);
        if (!future.isSuccess()) {
            b.group().shutdownGracefully();
            return false;
        }
        future.channel().closeFuture().addListener((GenericFutureListener)closeListener);
        return true;
    }
    
    private static boolean startNonBlocking(final Bootstrap b, final ChannelFutureListener closeListener) {
        final RetryListener retryListener = new RetryListener(b, closeListener);
        retryListener.connect();
        return true;
    }
    
    @Override
    public String toString() {
        return "ClientConnection{m_decoders=" + this.m_decoders.size() + ", m_host='" + this.m_host + '\'' + ", m_port=" + this.m_port + ", m_entityFactory=" + this.m_entityFactory + ", m_eventsHandler=" + this.m_eventsHandler + '}';
    }
    
    private static class GroupThreadFactory implements ThreadFactory
    {
        private final String m_host;
        private final int m_port;
        private int m_cpt;
        
        GroupThreadFactory(final String host, final int port) {
            super();
            this.m_host = host;
            this.m_port = port;
        }
        
        @Override
        public Thread newThread(@NotNull final Runnable runnable) {
            return new Thread(runnable, "Net-Cnx-" + this.m_host + ':' + this.m_port + '>' + this.m_cpt++);
        }
        
        @Override
        public String toString() {
            return "GroupThreadFactory{m_host='" + this.m_host + '\'' + ", m_port=" + this.m_port + ", m_cpt=" + this.m_cpt + '}';
        }
    }
    
    private static class ShutdownListener implements ChannelFutureListener
    {
        private final EventLoopGroup m_group;
        
        ShutdownListener(final EventLoopGroup group) {
            super();
            this.m_group = group;
        }
        
        public void operationComplete(final ChannelFuture future) throws Exception {
            this.m_group.shutdownGracefully();
        }
        
        @Override
        public String toString() {
            return "ShutdownListener{m_group=" + this.m_group + '}';
        }
    }
    
    private static class ReconnectListener implements ChannelFutureListener
    {
        private final Bootstrap m_bootstrap;
        
        ReconnectListener(final Bootstrap bootstrap) {
            super();
            this.m_bootstrap = bootstrap;
        }
        
        public void operationComplete(final ChannelFuture future) throws Exception {
            startNonBlocking(this.m_bootstrap, (ChannelFutureListener)this);
        }
        
        @Override
        public String toString() {
            return "ShutdownListener{m_bootstrap=" + this.m_bootstrap + '}';
        }
    }
    
    private static class RetryListener implements ChannelFutureListener
    {
        private static final Logger LISTENER_LOG;
        private final Bootstrap m_bootStrap;
        private final ChannelFutureListener m_closeListener;
        private long m_start;
        
        RetryListener(final Bootstrap bootStrap, final ChannelFutureListener closeListener) {
            super();
            this.m_start = System.nanoTime();
            this.m_bootStrap = bootStrap;
            this.m_closeListener = closeListener;
        }
        
        public void operationComplete(final ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                future.channel().closeFuture().addListener((GenericFutureListener)this.m_closeListener);
                return;
            }
            final long duration = System.nanoTime() - this.m_start;
            TimeUnit.NANOSECONDS.sleep(TimeUnit.SECONDS.toNanos(5L) - duration);
            this.connect();
        }
        
        void connect() {
            this.m_start = System.nanoTime();
            this.m_bootStrap.connect().addListener((GenericFutureListener)this);
        }
        
        @Override
        public String toString() {
            return "RetryListener{m_bootStrap=" + this.m_bootStrap + ", m_start=" + this.m_start + '}';
        }
        
        static {
            LISTENER_LOG = Logger.getLogger((Class)RetryListener.class);
        }
    }
}
