package com.ankamagames.framework.kernel.core.net.netty;

import com.ankamagames.framework.kernel.*;
import io.netty.channel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankama.wakfu.utils.injection.*;
import com.ankama.wakfu.utils.metrics.*;
import com.codahale.metrics.*;
import java.net.*;

public class ConnectionCtx
{
    private final ChannelHandlerContext m_ctx;
    private final FrameworkEntity m_user;
    
    public ConnectionCtx(final ChannelHandlerContext ctx, final FrameworkEntity user) {
        super();
        this.m_ctx = ctx;
        this.m_user = user;
    }
    
    public FrameworkEntity getUser() {
        return this.m_user;
    }
    
    public ChannelFuture close() {
        return this.m_ctx.close();
    }
    
    public boolean isConnected() {
        return this.m_ctx.channel().isActive();
    }
    
    public ChannelFuture pushMessage(final Message message) {
        final byte[] encoded = message.encode();
        final ChannelFuture channelFuture = this.m_ctx.writeAndFlush((Object)encoded);
        final MetricsManager manager = Injection.getInstance().getInstance(MetricsManager.class);
        final Histogram histogram = manager.histogram(Metrics.NETWORK_OUTBOUND_MESSAGE_SIZE, message.getId());
        histogram.update(encoded.length);
        return channelFuture;
    }
    
    public InetAddress getInetAddress() {
        final InetSocketAddress address = (InetSocketAddress)this.m_ctx.channel().remoteAddress();
        return address.getAddress();
    }
    
    @Override
    public String toString() {
        return "NettyConnection{m_ctx=" + this.m_ctx + '}';
    }
}
