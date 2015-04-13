package com.ankamagames.wakfu.common.game.pet.change;

import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.*;

class PetXpChange implements PetChange
{
    private int m_xp;
    
    PetXpChange() {
        super();
    }
    
    PetXpChange(final int xp) {
        super();
        this.m_xp = xp;
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_xp);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_xp = bb.getInt();
    }
    
    @Override
    public void compute(final PetController controller) {
        controller.setXp(this.m_xp);
    }
    
    @Override
    public PetChangeType getType() {
        return PetChangeType.XP;
    }
    
    @Override
    public String toString() {
        return "PetXpChange{m_xp=" + this.m_xp + '}';
    }
}
