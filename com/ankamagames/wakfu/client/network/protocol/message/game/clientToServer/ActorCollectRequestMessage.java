package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class ActorCollectRequestMessage extends OutputOnlyProxyMessage
{
    protected static final Logger m_logger;
    protected static final boolean DEBUG_MODE = false;
    private int m_actionId;
    private int m_positionX;
    private int m_positionY;
    
    public ActorCollectRequestMessage(final int actionId, final int posX, final int posY) {
        super();
        this.m_actionId = actionId;
        this.m_positionX = posX;
        this.m_positionY = posY;
    }
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 12;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putInt(this.m_actionId);
        buffer.putInt(this.m_positionX);
        buffer.putInt(this.m_positionY);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 4143;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActorCollectRequestMessage.class);
    }
}
