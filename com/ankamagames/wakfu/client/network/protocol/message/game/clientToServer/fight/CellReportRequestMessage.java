package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;

public class CellReportRequestMessage extends OutputOnlyProxyMessage
{
    private Point3 m_targetCellCoords;
    
    public void setTargetCoords(final Point3 targetCoords) {
        this.m_targetCellCoords = targetCoords;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putInt(this.m_targetCellCoords.getX());
        buffer.putInt(this.m_targetCellCoords.getY());
        buffer.putShort(this.m_targetCellCoords.getZ());
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 8155;
    }
}
