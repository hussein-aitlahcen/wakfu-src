package com.ankamagames.wakfu.common.game.nation.actionRequest;

import org.apache.log4j.*;
import java.nio.*;
import org.jetbrains.annotations.*;

public abstract class NationActionRequestFactory
{
    private static final Logger m_logger;
    
    public abstract NationActionRequest createNew();
    
    public static byte[] serializeRequest(final NationActionRequest request) {
        final ByteBuffer buffer = ByteBuffer.allocate(5 + request.serializedSize());
        buffer.put((byte)request.getType().ordinal());
        buffer.putInt(request.getNationId());
        request.serialize(buffer);
        return buffer.array();
    }
    
    @Nullable
    public static NationActionRequest unserializeRequest(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final byte requestType = buffer.get();
        final NationActionRequest request = NationActionRequestType.createRequestFromOrdinal(requestType);
        if (request == null) {
            NationActionRequestFactory.m_logger.error((Object)("Impossible de d\u00e9coder une NactionActionRequest : type inconnu : " + requestType));
            return null;
        }
        request.setNationId(buffer.getInt());
        if (request.unserialize(buffer)) {
            return request;
        }
        NationActionRequestFactory.m_logger.error((Object)("Erreur \u00e0 la d\u00e9s\u00e9rialisation d'une requ\u00eate de type " + requestType));
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationActionRequestFactory.class);
    }
}
