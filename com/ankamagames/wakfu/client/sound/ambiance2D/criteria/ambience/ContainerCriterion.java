package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public interface ContainerCriterion
{
    boolean isValid();
    
    void load(ExtendedDataInputStream p0);
    
    void save(OutputBitStream p0) throws IOException;
    
    byte getCriterionId();
    
    ContainerCriterion clone();
    
    boolean isNegated();
}
