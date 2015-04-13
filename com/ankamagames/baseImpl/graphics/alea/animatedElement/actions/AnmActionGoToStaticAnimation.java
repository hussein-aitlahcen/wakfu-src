package com.ankamagames.baseImpl.graphics.alea.animatedElement.actions;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class AnmActionGoToStaticAnimation extends AnmAction
{
    public static final AnmActionGoToStaticAnimation INSTANCE;
    
    @Override
    public final boolean run(final AnimatedObject animatedElement) {
        return animatedElement.setAnimation(animatedElement.getStaticAnimationKey());
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
        return AnmActionTypes.GO_TO_STATIC_ANIMATION;
    }
    
    @Override
    public final boolean equals(final Object o) {
        return o == AnmActionGoToStaticAnimation.INSTANCE;
    }
    
    @Override
    public final int hashCode() {
        return this.getType().getId();
    }
    
    static {
        INSTANCE = new AnmActionGoToStaticAnimation();
    }
}
