package com.ankamagames.wakfu.client.service.updater;

import io.netty.channel.*;
import java.util.concurrent.atomic.*;
import com.google.common.base.*;

final class MessageBufferHandler extends SimpleChannelInboundHandler<String>
{
    private final StringBuffer m_buffer;
    
    MessageBufferHandler() {
        super(true);
        this.m_buffer = new StringBuffer();
    }
    
    protected void channelRead0(final ChannelHandlerContext ctx, final String msg) throws Exception {
        this.m_buffer.append(msg);
        this.checkForCompleteMessage(ctx);
    }
    
    private void checkForCompleteMessage(final ChannelHandlerContext ctx) {
        final String data = this.m_buffer.toString();
        final AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < data.length(); ++i) {
            final char c = data.charAt(i);
            if (c == '{') {
                counter.incrementAndGet();
            }
            else if (c == '}' && counter.decrementAndGet() == 0) {
                this.m_buffer.delete(0, i + 1);
                final String completeMessage = data.substring(0, i + 1).trim();
                ctx.fireChannelRead((Object)completeMessage);
                this.checkForCompleteMessage(ctx);
                return;
            }
        }
        Preconditions.checkState(counter.get() >= 0, (Object)"Parsing error : more closing bracket than opening bracket");
    }
}
