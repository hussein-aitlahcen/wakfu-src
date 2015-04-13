package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class OccupationModificationInformationMessage extends OutputOnlyProxyMessage
{
    protected static final Logger m_logger;
    private byte m_modificationType;
    private short m_occupationType;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 3;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.put(this.m_modificationType);
        buffer.putShort(this.m_occupationType);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 4171;
    }
    
    public void setModificationType(final byte modificationType) {
        this.m_modificationType = modificationType;
    }
    
    public void setOccupationType(final short occupationType) {
        this.m_occupationType = occupationType;
    }
    
    static {
        m_logger = Logger.getLogger((Class)OccupationModificationInformationMessage.class);
    }
}
