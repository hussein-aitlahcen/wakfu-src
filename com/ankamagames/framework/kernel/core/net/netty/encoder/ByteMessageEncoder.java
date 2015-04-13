package com.ankamagames.framework.kernel.core.net.netty.encoder;

import io.netty.handler.codec.*;
import io.netty.channel.*;
import io.netty.buffer.*;

@ChannelHandler.Sharable
public class ByteMessageEncoder extends MessageToByteEncoder<byte[]>
{
    protected void encode(final ChannelHandlerContext ctx, final byte[] msg, final ByteBuf out) throws Exception {
        out.writeBytes(msg);
    }
}
