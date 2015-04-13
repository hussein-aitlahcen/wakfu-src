package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;

public class CellReportMessage extends AbstractFightMessage
{
    private Point3 m_targetCellCoords;
    private Long m_reporterId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas == null || rawDatas.length != 22) {
            CellReportMessage.m_logger.error((Object)"le message recu a une taille de donn\u00e9es incorrecte");
            return false;
        }
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buff);
        (this.m_targetCellCoords = new Point3()).setX(buff.getInt());
        this.m_targetCellCoords.setY(buff.getInt());
        this.m_targetCellCoords.setZ(buff.getShort());
        this.m_reporterId = buff.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 8156;
    }
    
    public Point3 getCellCoords() {
        return this.m_targetCellCoords;
    }
    
    public Long getReporterUniqueId() {
        return this.m_reporterId;
    }
}
