package com.ankamagames.wakfu.common.game.pet.change;

import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.*;

public interface PetChange
{
    byte[] serialize();
    
    void unSerialize(ByteBuffer p0);
    
    void compute(PetController p0);
    
    PetChangeType getType();
}
