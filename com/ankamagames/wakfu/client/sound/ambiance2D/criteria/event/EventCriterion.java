package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public interface EventCriterion
{
    boolean isValid(SoundEvent p0);
    
    void load(ExtendedDataInputStream p0);
    
    void save(OutputBitStream p0) throws IOException;
    
    byte getCriterionId();
    
    EventCriterion clone();
}
