package com.ankamagames.wakfu.common.game.pet.change;

import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

class PetLastMealDateChange implements PetChange
{
    private long m_lastMealDate;
    
    PetLastMealDateChange() {
        super();
    }
    
    PetLastMealDateChange(final GameDateConst lastMealDate) {
        super();
        this.m_lastMealDate = lastMealDate.toLong();
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(this.m_lastMealDate);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_lastMealDate = bb.getLong();
    }
    
    @Override
    public void compute(final PetController controller) {
        controller.setLastMealDate(GameDate.fromLong(this.m_lastMealDate));
    }
    
    @Override
    public PetChangeType getType() {
        return PetChangeType.LAST_MEAL_DATE;
    }
    
    @Override
    public String toString() {
        return "PetLastMealDateChange{m_lastMealDate=" + this.m_lastMealDate + '}';
    }
}
