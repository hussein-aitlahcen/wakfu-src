package com.ankamagames.baseImpl.graphics.sound.binary;

import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import gnu.trove.*;

public interface AnimatedElementRunScriptData
{
    void play(AnimatedObject p0);
    
    void load(ExtendedDataInputStream p0);
    
    void save(OutputBitStream p0) throws IOException;
    
    int getType();
    
    void getSoundIds(TLongArrayList p0);
}
