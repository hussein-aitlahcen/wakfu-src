package com.ankamagames.framework.graphics.engine.Anm2.actions;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public abstract class AnmAction
{
    private int m_frameIndex;
    
    public final int getFrameIndex() {
        return this.m_frameIndex;
    }
    
    public final void setFrameIndex(final int frameIndex) {
        this.m_frameIndex = frameIndex;
    }
    
    public abstract boolean run(final AnimatedObject p0);
    
    public abstract void load(final byte p0, final ExtendedDataInputStream p1) throws Exception;
    
    public void write(final OutputBitStream ostream) throws IOException {
        ostream.writeByte(this.getType().getId());
    }
    
    public abstract AnmActionTypes getType();
    
    @Override
    public abstract boolean equals(final Object p0);
    
    @Override
    public abstract int hashCode();
}
