package com.ankamagames.framework.kernel.core.net.netty.decoder;

import io.netty.handler.codec.*;
import java.util.*;
import io.netty.channel.*;
import io.netty.buffer.*;
import com.google.common.collect.*;
import com.ankama.wakfu.utils.injection.*;
import com.ankama.wakfu.utils.metrics.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.codahale.metrics.*;

public class MessageDecoder extends ReplayingDecoder<Void>
{
    private final ProtocolAdapter m_adapter;
    private final List<WakfuMessageDecoder> m_decoders;
    
    public MessageDecoder(final ProtocolAdapter adapter, final Collection<WakfuMessageDecoder> decoders) {
        super();
        this.m_decoders = new ArrayList<WakfuMessageDecoder>();
        this.m_adapter = adapter;
        this.m_decoders.addAll(decoders);
    }
    
    public boolean add(final WakfuMessageDecoder handler) {
        return this.m_decoders.add(handler);
    }
    
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        final ByteBuffer bb = this.m_adapter.adapt(in);
        final ArrayList<WakfuMessageDecoder> decoders = (ArrayList<WakfuMessageDecoder>)Lists.newArrayList((Iterable)this.m_decoders);
        Message msg = null;
        WakfuMessageDecoder decoder;
        for (int i = 0; i < decoders.size() && msg == null; msg = decoder.decode(bb), ++i) {
            decoder = decoders.get(i);
            bb.rewind();
        }
        if (msg == null) {
            throw new UnsupportedOperationException("Unable to decode message");
        }
        final MetricsManager manager = Injection.getInstance().getInstance(MetricsManager.class);
        final Histogram histogram = manager.histogram(Metrics.NETWORK_INBOUND_MESSAGE_SIZE, msg.getId());
        histogram.update(bb.position());
        out.add(msg);
    }
    
    public String toString() {
        return "ServerMessageDecoder{m_decoders=" + this.m_decoders.size() + '}';
    }
}
