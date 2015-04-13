package com.ankamagames.baseImpl.graphics.alea.animatedElement.actions;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class AnmActionGoToAnimation extends AnmAction
{
    private String m_animationName;
    private int m_percent;
    
    public AnmActionGoToAnimation() {
        super();
        this.m_percent = 100;
    }
    
    @Override
    public final boolean run(final AnimatedObject animatedElement) {
        if (MathHelper.random(100) > this.m_percent) {
            return false;
        }
        animatedElement.setAnimation(this.m_animationName);
        return true;
    }
    
    @Override
    public final void load(final byte parametersCount, final ExtendedDataInputStream bitStream) throws Exception {
        this.m_animationName = bitStream.readString();
        if (parametersCount == 2) {
            this.m_percent = bitStream.readByte();
        }
    }
    
    @Override
    public void write(final OutputBitStream ostream) throws IOException {
        super.write(ostream);
        final int paramCount = (this.m_percent == 0) ? 1 : 2;
        ostream.writeByte((byte)paramCount);
        ostream.writeString(this.m_animationName);
        if (paramCount == 2) {
            ostream.writeByte((byte)this.m_percent);
        }
    }
    
    @Override
    public final AnmActionTypes getType() {
        return AnmActionTypes.GO_TO_ANIMATION;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnmActionGoToAnimation)) {
            return false;
        }
        final AnmActionGoToAnimation that = (AnmActionGoToAnimation)o;
        if (this.m_percent != that.m_percent) {
            return false;
        }
        if (this.m_animationName != null) {
            if (this.m_animationName.equals(that.m_animationName)) {
                return true;
            }
        }
        else if (that.m_animationName == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        int result = this.getType().getId();
        result = 31 * result + this.m_percent;
        result = 31 * result + ((this.m_animationName != null) ? this.m_animationName.hashCode() : 0);
        return result;
    }
}
