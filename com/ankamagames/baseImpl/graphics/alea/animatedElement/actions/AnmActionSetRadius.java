package com.ankamagames.baseImpl.graphics.alea.animatedElement.actions;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class AnmActionSetRadius extends AnmAction
{
    private byte m_radius;
    
    @Override
    public final boolean run(final AnimatedObject animatedElement) {
        animatedElement.setRenderRadius(this.m_radius);
        return false;
    }
    
    @Override
    public final void load(final byte parametersCount, final ExtendedDataInputStream bitStream) throws Exception {
        this.m_radius = bitStream.readByte();
    }
    
    @Override
    public void write(final OutputBitStream ostream) throws IOException {
        super.write(ostream);
        ostream.writeByte((byte)1);
        ostream.writeByte(this.m_radius);
    }
    
    @Override
    public final AnmActionTypes getType() {
        return AnmActionTypes.SET_RADIUS;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnmActionSetRadius)) {
            return false;
        }
        final AnmActionSetRadius that = (AnmActionSetRadius)o;
        return this.m_radius == that.m_radius;
    }
    
    @Override
    public final int hashCode() {
        int result = this.getType().getId();
        result = 31 * result + this.m_radius;
        return result;
    }
}
