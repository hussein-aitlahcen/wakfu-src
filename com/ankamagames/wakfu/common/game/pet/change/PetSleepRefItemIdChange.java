package com.ankamagames.wakfu.common.game.pet.change;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.common.game.pet.exception.*;

class PetSleepRefItemIdChange implements PetChange
{
    private static final Logger m_logger;
    private int m_sleepRefItemId;
    
    PetSleepRefItemIdChange() {
        super();
    }
    
    PetSleepRefItemIdChange(final int sleepRefItemId) {
        super();
        this.m_sleepRefItemId = sleepRefItemId;
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_sleepRefItemId);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_sleepRefItemId = bb.getInt();
    }
    
    @Override
    public void compute(final PetController controller) {
        try {
            if (this.m_sleepRefItemId > 0) {
                controller.setSleepRefItemId(this.m_sleepRefItemId);
            }
            else {
                controller.removeSleepRefItemId();
            }
        }
        catch (PetControllerException e) {
            PetSleepRefItemIdChange.m_logger.error((Object)"Impossible de changer l'objet de sommeil du familier", (Throwable)e);
        }
    }
    
    @Override
    public PetChangeType getType() {
        return PetChangeType.SLEEP_ITEM;
    }
    
    @Override
    public String toString() {
        return "PetSleepRefItemIdChange{m_sleepRefItemId=" + this.m_sleepRefItemId + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)PetSleepRefItemIdChange.class);
    }
}
