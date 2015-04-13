package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class PlayerUseItemRequestMessage extends OutputOnlyProxyMessage
{
    private long m_itemUid;
    private boolean m_withPosition;
    private int m_x;
    private int m_y;
    private short m_z;
    
    public PlayerUseItemRequestMessage() {
        super();
        this.m_withPosition = false;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(9 + (this.m_withPosition ? 10 : 0));
        bb.putLong(this.m_itemUid);
        if (this.m_withPosition) {
            bb.put((byte)1);
            bb.putInt(this.m_x);
            bb.putInt(this.m_y);
            bb.putShort(this.m_z);
        }
        else {
            bb.put((byte)0);
        }
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 9105;
    }
    
    public void setItemUid(final long itemUid) {
        this.m_itemUid = itemUid;
    }
    
    public void setPosition(final int x, final int y, final short z) {
        this.m_withPosition = true;
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
}
