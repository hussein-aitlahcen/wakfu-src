package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class SpellCastExecutionMessage extends AbstractFightActionMessage
{
    private long m_casterId;
    private RawSpellLevel m_serializedSpellLevel;
    private int m_castPositionX;
    private int m_castPositionY;
    private short m_castPositionZ;
    private boolean m_criticalHit;
    private boolean m_criticalMiss;
    private int m_refItemId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 35, false)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_casterId = bb.getLong();
        this.m_serializedSpellLevel = new RawSpellLevel();
        if (!this.m_serializedSpellLevel.unserialize(bb)) {
            return false;
        }
        this.m_refItemId = bb.getInt();
        if (!(this.m_criticalMiss = (bb.get() == 1))) {
            this.m_criticalHit = (bb.get() == 1);
        }
        else {
            this.m_criticalHit = false;
        }
        this.m_castPositionX = bb.getInt();
        this.m_castPositionY = bb.getInt();
        this.m_castPositionZ = bb.getShort();
        return true;
    }
    
    @Override
    public int getId() {
        return 8110;
    }
    
    public long getCasterId() {
        return this.m_casterId;
    }
    
    public RawSpellLevel getSerializedSpellLevel() {
        return this.m_serializedSpellLevel;
    }
    
    public int getCastPositionX() {
        return this.m_castPositionX;
    }
    
    public int getCastPositionY() {
        return this.m_castPositionY;
    }
    
    public short getCastPositionZ() {
        return this.m_castPositionZ;
    }
    
    public boolean isCriticalHit() {
        return this.m_criticalHit;
    }
    
    public boolean isCriticalMiss() {
        return this.m_criticalMiss;
    }
    
    public int getRefItemId() {
        return this.m_refItemId;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.SPELL_CAST;
    }
}
