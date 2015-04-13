package com.ankamagames.framework.kernel.core.net.netty.encoder;

import io.netty.handler.codec.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import io.netty.channel.*;
import io.netty.buffer.*;
import com.ankama.wakfu.utils.injection.*;
import com.ankama.wakfu.utils.metrics.*;
import com.codahale.metrics.*;

@ChannelHandler.Sharable
public class MessageEncoder extends MessageToByteEncoder<Message>
{
    protected void encode(final ChannelHandlerContext ctx, final Message msg, final ByteBuf out) throws Exception {
        final byte[] encoded = msg.encode();
        final MetricsManager manager = Injection.getInstance().getInstance(MetricsManager.class);
        final Histogram histogram = manager.histogram(Metrics.NETWORK_OUTBOUND_MESSAGE_SIZE, msg.getId());
        histogram.update(encoded.length);
        out.writeBytes(encoded);
    }
}
