package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;

public abstract class AbstractOccupationModificationMessage extends InputOnlyProxyMessage
{
    protected short m_occupationType;
    protected byte[] m_data;
    protected long m_concernedPlayerId;
    protected byte m_modificationType;
    protected static final Logger m_logger;
    
    @Override
    public abstract boolean decode(final byte[] p0);
    
    @Override
    public abstract int getId();
    
    @Override
    public byte[] encode() {
        throw new UnsupportedOperationException("On tente d'encoder un message server->client cot\u00e9 client");
    }
    
    public short getOccupationType() {
        return this.m_occupationType;
    }
    
    public long getConcernedPlayerId() {
        return this.m_concernedPlayerId;
    }
    
    public byte[] getData() {
        return this.m_data;
    }
    
    public byte getModificationType() {
        return this.m_modificationType;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractOccupationModificationMessage.class);
    }
}
