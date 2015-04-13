package com.ankamagames.wakfu.common.game.pet.change;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.common.game.pet.exception.*;

class PetColorChange implements PetChange
{
    private static final Logger m_logger;
    private int m_colorItemRefId;
    
    PetColorChange() {
        super();
    }
    
    PetColorChange(final int colorItemRefId) {
        super();
        this.m_colorItemRefId = colorItemRefId;
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_colorItemRefId);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_colorItemRefId = bb.getInt();
    }
    
    @Override
    public void compute(final PetController controller) {
        try {
            controller.setColorItem(this.m_colorItemRefId);
        }
        catch (PetControllerException e) {
            PetColorChange.m_logger.error((Object)"Impossible de changer la couleur du familier", (Throwable)e);
        }
    }
    
    @Override
    public PetChangeType getType() {
        return PetChangeType.COLOR;
    }
    
    @Override
    public String toString() {
        return "PetColorChange{m_colorItemRefId='" + this.m_colorItemRefId + '\'' + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)PetColorChange.class);
    }
}
