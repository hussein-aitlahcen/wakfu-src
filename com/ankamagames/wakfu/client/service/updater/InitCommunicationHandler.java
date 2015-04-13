package com.ankamagames.wakfu.client.service.updater;

import io.netty.channel.*;
import com.google.gson.*;
import com.ankamagames.wakfu.client.service.updater.message.*;

final class InitCommunicationHandler extends ChannelInboundHandlerAdapter
{
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush((Object)new Gson().toJson((Object)HelloMessage.get()));
        super.channelActive(ctx);
    }
}
