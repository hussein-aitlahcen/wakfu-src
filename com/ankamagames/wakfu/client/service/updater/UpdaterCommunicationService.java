package com.ankamagames.wakfu.client.service.updater;

import com.ankamagames.wakfu.client.service.*;
import java.util.concurrent.atomic.*;
import com.ankamagames.wakfu.client.core.*;
import javax.inject.*;
import io.netty.bootstrap.*;
import org.apache.log4j.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.nio.*;
import io.netty.channel.socket.*;
import java.util.*;
import io.netty.util.concurrent.*;
import io.netty.channel.*;
import java.util.concurrent.*;

public final class UpdaterCommunicationService implements IService
{
    private static final String LOCALHOST = "127.0.0.1";
    public static final int DELAY_BEFORE_RECONNECTION = 20;
    private final AtomicBoolean m_stutdown;
    private final ScheduledExecutorService m_scheduledExecutorService;
    private final WakfuConfiguration m_configuration;
    private final Provider<List<ChannelHandler>> m_handlersProvider;
    private Bootstrap m_bootstrap;
    private final Logger m_logger;
    
    public UpdaterCommunicationService(final WakfuConfiguration configuration, final Provider<List<ChannelHandler>> handlersProvider) {
        super();
        this.m_stutdown = new AtomicBoolean();
        this.m_logger = Logger.getLogger((Class)UpdaterCommunicationService.class);
        this.m_handlersProvider = handlersProvider;
        this.m_scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        this.m_configuration = configuration;
    }
    
    private Bootstrap configureBootstrap() {
        return (Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)new NioEventLoopGroup())).channel((Class)NioSocketChannel.class)).option(ChannelOption.TCP_NODELAY, (Object)true)).option(ChannelOption.SO_KEEPALIVE, (Object)true)).handler((ChannelHandler)new ChannelInitializer<SocketChannel>() {
            public void initChannel(final SocketChannel ch) throws Exception {
                final ChannelPipeline pipeline = ch.pipeline();
                for (final ChannelHandler handler : (List)UpdaterCommunicationService.this.m_handlersProvider.get()) {
                    pipeline.addLast(new ChannelHandler[] { handler });
                }
                pipeline.addLast(new ChannelHandler[] { UpdaterCommunicationService.this.reconnectOnDisconnectionHandler() });
            }
        });
    }
    
    @Override
    public void start() {
        if (!this.checkUpdaterCommunicationIsActive()) {
            return;
        }
        try {
            this.m_bootstrap = this.configureBootstrap();
        }
        catch (Exception e) {
            this.m_logger.error((Object)"Unable to configure bootstrap to communicate with updater: ", (Throwable)e);
            return;
        }
        this.tryConnect();
    }
    
    private void tryConnect() {
        if (!this.checkUpdaterCommunicationIsActive()) {
            return;
        }
        final int port = this.m_configuration.getInteger("UPDATER_COMMUNICATION_PORT", 0);
        this.m_logger.info((Object)("Trying to establish connection to updater communication service on port " + port));
        final ChannelFuture connectFuture = this.m_bootstrap.connect("127.0.0.1", port);
        connectFuture.addListener((GenericFutureListener)new ChannelFutureListener() {
            public void operationComplete(final ChannelFuture future) throws Exception {
                if (future.cause() != null) {
                    UpdaterCommunicationService.this.m_logger.error((Object)"Error caught on channel during connect: ", future.cause());
                }
            }
        });
    }
    
    private boolean checkUpdaterCommunicationIsActive() {
        if (this.m_stutdown.get()) {
            return false;
        }
        if (this.m_configuration.getInteger("UPDATER_COMMUNICATION_PORT", 0) == 0) {
            this.m_logger.warn((Object)"Updater communication service is not currently configured: Service is unactivated.");
            return false;
        }
        return true;
    }
    
    @Override
    public void stop() {
        this.m_stutdown.set(true);
        if (this.m_bootstrap == null) {
            return;
        }
        final Future<?> future = (Future<?>)this.m_bootstrap.group().shutdownGracefully();
        try {
            future.sync();
        }
        catch (InterruptedException e) {
            this.m_logger.error((Object)"Exception during shutting down gracefully", (Throwable)e);
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    private ChannelHandler reconnectOnDisconnectionHandler() {
        return (ChannelHandler)new ChannelInboundHandlerAdapter() {
            public void channelActive(final ChannelHandlerContext ctx) throws Exception {
                UpdaterCommunicationService.this.m_logger.info((Object)"Connection to updater communication service established.");
                super.channelActive(ctx);
            }
            
            public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
                UpdaterCommunicationService.this.m_logger.info((Object)"Connection lost with updater communication service, scheduling reconnection within 20 seconds");
                UpdaterCommunicationService.this.m_scheduledExecutorService.schedule(new Runnable() {
                    @Override
                    public void run() {
                        UpdaterCommunicationService.this.tryConnect();
                    }
                }, 20L, TimeUnit.SECONDS);
                super.channelUnregistered(ctx);
            }
            
            public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
                UpdaterCommunicationService.this.m_logger.error((Object)"Error caught on channel: ", cause);
                if (ctx.channel().isOpen()) {
                    ctx.channel().close().sync();
                }
            }
        };
    }
}
