package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class FighterInvisibleDetectedMessage extends AbstractFightActionMessage
{
    private long m_detectedFighterId;
    private long[] m_detectedByFightersId;
    private Point3 m_detectedPosition;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 29, false)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_detectedFighterId = bb.getLong();
        final long detectedPosition = bb.getLong();
        this.m_detectedPosition = PositionValue.fromLong(detectedPosition);
        final int count = bb.get() & 0xFF;
        if (count <= 0) {
            FighterInvisibleDetectedMessage.m_logger.warn((Object)"Aucun fighter le d\u00e9tecte l'invisibilit\u00e9 ?");
        }
        if (!this.checkMessageSize(bb.remaining(), 8 * count, true)) {
            return false;
        }
        this.m_detectedByFightersId = new long[count];
        for (byte i = 0; i < count; ++i) {
            this.m_detectedByFightersId[i] = bb.getLong();
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 4508;
    }
    
    public long getDetectedFighterId() {
        return this.m_detectedFighterId;
    }
    
    public long[] getDetectedByFightersId() {
        return this.m_detectedByFightersId;
    }
    
    public Point3 getDetectedPosition() {
        return this.m_detectedPosition;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.INVISIBLE_DETECTED;
    }
}
