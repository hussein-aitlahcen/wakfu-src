package com.ankamagames.wakfu.common.game.pet.change;

import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

class PetLastHungryDateChange implements PetChange
{
    private long m_lastHungryDate;
    
    PetLastHungryDateChange() {
        super();
    }
    
    PetLastHungryDateChange(final GameDateConst lastHungryDate) {
        super();
        this.m_lastHungryDate = lastHungryDate.toLong();
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(this.m_lastHungryDate);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_lastHungryDate = bb.getLong();
    }
    
    @Override
    public void compute(final PetController controller) {
        controller.setLastHungryDate(GameDate.fromLong(this.m_lastHungryDate));
    }
    
    @Override
    public PetChangeType getType() {
        return PetChangeType.LAST_HUNGRY_DATE;
    }
    
    @Override
    public String toString() {
        return "PetLastHungryDateChange{m_lastHungryDate=" + this.m_lastHungryDate + '}';
    }
}
