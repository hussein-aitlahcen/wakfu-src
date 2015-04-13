package com.ankamagames.framework.kernel.core.net.netty.handler;

import org.apache.log4j.*;
import io.netty.channel.*;

@ChannelHandler.Sharable
public class LogHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger LOG;
    
    private static String format(final ChannelHandlerContext ctx, final String message) {
        final String chStr = ctx.channel().toString();
        return chStr + ' ' + message;
    }
    
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        LogHandler.LOG.info((Object)format(ctx, "Channel active"));
        ctx.fireChannelActive();
    }
    
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        LogHandler.LOG.info((Object)format(ctx, "Channel inActive"));
        ctx.fireChannelInactive();
    }
    
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        LogHandler.LOG.error((Object)format(ctx, "Channel exception caught"), cause);
        ctx.fireExceptionCaught(cause);
    }
    
    static {
        LOG = Logger.getLogger((Class)LogHandler.class);
    }
}
