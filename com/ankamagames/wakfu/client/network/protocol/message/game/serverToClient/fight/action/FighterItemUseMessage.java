package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class FighterItemUseMessage extends AbstractFightActionMessage
{
    private long m_userId;
    private int m_itemRefId;
    private boolean m_associatedWithSpell;
    private int m_usePositionX;
    private int m_usePositionY;
    private short m_usePositionZ;
    private boolean m_criticalHit;
    private boolean m_criticalMiss;
    private short m_level;
    private RawSpellLevel m_serializedSpellLevel;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_userId = bb.getLong();
        this.m_itemRefId = bb.getInt();
        this.m_level = bb.getShort();
        this.m_associatedWithSpell = (bb.get() == 1);
        if (this.m_associatedWithSpell) {
            this.m_serializedSpellLevel = new RawSpellLevel();
            if (!this.m_serializedSpellLevel.unserialize(bb)) {
                return false;
            }
        }
        if (!(this.m_criticalMiss = (bb.get() == 1))) {
            this.m_criticalHit = (bb.get() == 1);
            this.m_usePositionX = bb.getInt();
            this.m_usePositionY = bb.getInt();
            this.m_usePositionZ = bb.getShort();
        }
        else {
            this.m_criticalHit = false;
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 8108;
    }
    
    public boolean isCriticalMiss() {
        return this.m_criticalMiss;
    }
    
    public boolean isCriticalHit() {
        return this.m_criticalHit;
    }
    
    public int getItemReferenceId() {
        return this.m_itemRefId;
    }
    
    public int getUsePositionX() {
        return this.m_usePositionX;
    }
    
    public int getUsePositionY() {
        return this.m_usePositionY;
    }
    
    public short getUsePositionZ() {
        return this.m_usePositionZ;
    }
    
    public long getUserId() {
        return this.m_userId;
    }
    
    public boolean isAssociatedWithSpell() {
        return this.m_associatedWithSpell;
    }
    
    public RawSpellLevel getSerializedSpellLevel() {
        return this.m_serializedSpellLevel;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.ITEM_USE;
    }
}
