package com.ankamagames.wakfu.common.game.pet.change;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.common.game.pet.exception.*;

class PetEquipmentChange implements PetChange
{
    private static final Logger m_logger;
    private int m_equipmentRefItemId;
    
    PetEquipmentChange() {
        super();
    }
    
    PetEquipmentChange(final int equipmentRefItemId) {
        super();
        this.m_equipmentRefItemId = equipmentRefItemId;
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_equipmentRefItemId);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_equipmentRefItemId = bb.getInt();
    }
    
    @Override
    public void compute(final PetController controller) {
        try {
            if (this.m_equipmentRefItemId > 0) {
                controller.setEquipment(this.m_equipmentRefItemId);
            }
            else {
                controller.removeEquipment();
            }
        }
        catch (PetControllerException e) {
            PetEquipmentChange.m_logger.error((Object)"Impossible de changer le dernier repas du familier", (Throwable)e);
        }
    }
    
    @Override
    public PetChangeType getType() {
        return PetChangeType.EQUIPMENT;
    }
    
    @Override
    public String toString() {
        return "PetEquipmentChange{m_equipmentRefItemId=" + this.m_equipmentRefItemId + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)PetEquipmentChange.class);
    }
}
