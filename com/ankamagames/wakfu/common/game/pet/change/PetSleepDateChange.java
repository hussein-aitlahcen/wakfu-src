package com.ankamagames.wakfu.common.game.pet.change;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.pet.exception.*;

class PetSleepDateChange implements PetChange
{
    private static final Logger m_logger;
    private long m_sleepDate;
    
    PetSleepDateChange() {
        super();
    }
    
    PetSleepDateChange(final GameDateConst sleepDate) {
        super();
        this.m_sleepDate = sleepDate.toLong();
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(this.m_sleepDate);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_sleepDate = bb.getLong();
    }
    
    @Override
    public void compute(final PetController controller) {
        try {
            if (this.m_sleepDate > 0L) {
                controller.setSleepDate(GameDate.fromLong(this.m_sleepDate));
            }
            else {
                controller.removeSleepDate();
            }
        }
        catch (PetControllerException e) {
            PetSleepDateChange.m_logger.error((Object)"Impossible de changer la date de sommeil du familier", (Throwable)e);
        }
    }
    
    @Override
    public PetChangeType getType() {
        return PetChangeType.SLEEP_DATE;
    }
    
    @Override
    public String toString() {
        return "PetSleepDateChange{m_sleepDate=" + this.m_sleepDate + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)PetSleepDateChange.class);
    }
}
