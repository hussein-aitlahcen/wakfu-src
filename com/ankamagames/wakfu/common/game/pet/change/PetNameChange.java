package com.ankamagames.wakfu.common.game.pet.change;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.common.game.pet.exception.*;

class PetNameChange implements PetChange
{
    private static final Logger m_logger;
    private String m_name;
    
    PetNameChange() {
        super();
    }
    
    PetNameChange(final String name) {
        super();
        this.m_name = name;
    }
    
    @Override
    public byte[] serialize() {
        final byte[] utf = StringUtils.toUTF8(this.m_name);
        final ByteBuffer bb = ByteBuffer.allocate(4 + utf.length);
        bb.putInt(utf.length);
        bb.put(utf);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        final byte[] utf = new byte[bb.getInt()];
        bb.get(utf);
        this.m_name = StringUtils.fromUTF8(utf);
    }
    
    @Override
    public void compute(final PetController controller) {
        try {
            controller.setName(this.m_name);
        }
        catch (PetControllerException e) {
            PetNameChange.m_logger.error((Object)"Impossible de changer le nom du familier", (Throwable)e);
        }
    }
    
    @Override
    public PetChangeType getType() {
        return PetChangeType.NAME;
    }
    
    @Override
    public String toString() {
        return "PetNameChangeEvent{m_name='" + this.m_name + '\'' + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)PetNameChange.class);
    }
}
