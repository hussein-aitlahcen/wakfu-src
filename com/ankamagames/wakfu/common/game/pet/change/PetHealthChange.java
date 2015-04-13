package com.ankamagames.wakfu.common.game.pet.change;

import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.*;

class PetHealthChange implements PetChange
{
    private int m_health;
    
    PetHealthChange() {
        super();
    }
    
    PetHealthChange(final int health) {
        super();
        this.m_health = health;
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_health);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_health = bb.getInt();
    }
    
    @Override
    public void compute(final PetController controller) {
        controller.setHealth(this.m_health);
    }
    
    @Override
    public PetChangeType getType() {
        return PetChangeType.HEALTH;
    }
    
    @Override
    public String toString() {
        return "PetHealthChangeEvent{m_health=" + this.m_health + '}';
    }
}
