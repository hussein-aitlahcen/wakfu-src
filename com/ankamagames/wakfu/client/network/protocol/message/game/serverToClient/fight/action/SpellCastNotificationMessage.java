package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public final class SpellCastNotificationMessage extends AbstractFightActionMessage
{
    private long m_casterId;
    private int m_spellRefId;
    private boolean m_criticalHit;
    private boolean m_criticalMiss;
    private RawSpellLevel m_rawSpell;
    private int m_x;
    private int m_y;
    private short m_z;
    
    public SpellCastNotificationMessage() {
        super();
        this.m_casterId = -1L;
        this.m_spellRefId = -1;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_casterId = bb.getLong();
        this.m_spellRefId = bb.getInt();
        this.m_criticalHit = (bb.get() == 1);
        this.m_criticalMiss = (bb.get() == 1);
        this.m_rawSpell = new RawSpellLevel();
        if (!this.m_rawSpell.unserialize(bb)) {
            return false;
        }
        this.m_x = bb.getInt();
        this.m_y = bb.getInt();
        this.m_z = bb.getShort();
        return true;
    }
    
    public long getCasterId() {
        return this.m_casterId;
    }
    
    public int getSpellRefId() {
        return this.m_spellRefId;
    }
    
    public boolean isCriticalHit() {
        return this.m_criticalHit;
    }
    
    public boolean isCriticalMiss() {
        return this.m_criticalMiss;
    }
    
    public RawSpellLevel getRawSpell() {
        return this.m_rawSpell;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public short getZ() {
        return this.m_z;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.SPELL_CAST_NOTIFICATION;
    }
    
    @Override
    public int getId() {
        return 8116;
    }
}
