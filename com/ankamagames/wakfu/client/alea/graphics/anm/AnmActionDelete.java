package com.ankamagames.wakfu.client.alea.graphics.anm;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class AnmActionDelete extends AnmAction
{
    public static final AnmActionDelete INSTANCE;
    
    @Override
    public final boolean run(final AnimatedObject animatedElement) {
        return false;
    }
    
    @Override
    public final void load(final byte parametersCount, final ExtendedDataInputStream bitStream) throws Exception {
    }
    
    @Override
    public void write(final OutputBitStream ostream) throws IOException {
        super.write(ostream);
        ostream.writeByte((byte)0);
    }
    
    @Override
    public final AnmActionTypes getType() {
        return AnmActionTypes.DELETE;
    }
    
    @Override
    public final boolean equals(final Object o) {
        return o == AnmActionDelete.INSTANCE;
    }
    
    @Override
    public final int hashCode() {
        return this.getType().getId();
    }
    
    static {
        INSTANCE = new AnmActionDelete();
    }
}
