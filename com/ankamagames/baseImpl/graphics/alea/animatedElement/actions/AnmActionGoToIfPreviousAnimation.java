package com.ankamagames.baseImpl.graphics.alea.animatedElement.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import java.util.*;

public class AnmActionGoToIfPreviousAnimation extends AnmAction
{
    private static final Logger m_logger;
    private String[] m_previousAnimations;
    private String[] m_nextAnimations;
    private String m_defaultAnimation;
    
    @Override
    public final boolean run(final AnimatedObject animatedElement) {
        final String previousAnimation = animatedElement.getPreviousAnimation();
        for (int i = 0; i < this.m_previousAnimations.length; ++i) {
            if (previousAnimation.endsWith(this.m_previousAnimations[i])) {
                return animatedElement.setAnimation(this.m_nextAnimations[i]);
            }
        }
        return animatedElement.setAnimation(this.m_defaultAnimation);
    }
    
    @Override
    public final void load(final byte parametersCount, final ExtendedDataInputStream bitStream) throws Exception {
        final int ifCount = (parametersCount - 1) / 2;
        this.m_previousAnimations = new String[ifCount];
        this.m_nextAnimations = new String[ifCount];
        for (int i = 0; i < ifCount; ++i) {
            this.m_previousAnimations[i] = bitStream.readString();
            this.m_nextAnimations[i] = bitStream.readString();
        }
        if (parametersCount % 2 == 1) {
            this.m_defaultAnimation = bitStream.readString();
        }
    }
    
    @Override
    public void write(final OutputBitStream ostream) throws IOException {
        super.write(ostream);
        int paramCount = this.m_previousAnimations.length + this.m_nextAnimations.length;
        if (this.m_defaultAnimation != null) {
            ++paramCount;
        }
        ostream.writeByte((byte)paramCount);
        for (int i = 0; i < this.m_previousAnimations.length; ++i) {
            ostream.writeString(this.m_previousAnimations[i]);
            ostream.writeString(this.m_nextAnimations[i]);
        }
        if (this.m_defaultAnimation != null) {
            ostream.writeString(this.m_defaultAnimation);
        }
    }
    
    @Override
    public final AnmActionTypes getType() {
        return AnmActionTypes.GO_TO_IF_PREVIOUS_ANIMATION;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnmActionGoToIfPreviousAnimation)) {
            return false;
        }
        final AnmActionGoToIfPreviousAnimation that = (AnmActionGoToIfPreviousAnimation)o;
        if (this.m_defaultAnimation != null) {
            if (this.m_defaultAnimation.equals(that.m_defaultAnimation)) {
                return Arrays.equals(this.m_nextAnimations, that.m_nextAnimations) && Arrays.equals(this.m_previousAnimations, that.m_previousAnimations);
            }
        }
        else if (that.m_defaultAnimation == null) {
            return Arrays.equals(this.m_nextAnimations, that.m_nextAnimations) && Arrays.equals(this.m_previousAnimations, that.m_previousAnimations);
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        int result = this.getType().getId();
        result = 31 * result + ((this.m_previousAnimations != null) ? Arrays.hashCode(this.m_previousAnimations) : 0);
        result = 31 * result + ((this.m_nextAnimations != null) ? Arrays.hashCode(this.m_nextAnimations) : 0);
        result = 31 * result + ((this.m_defaultAnimation != null) ? this.m_defaultAnimation.hashCode() : 0);
        return result;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmActionGoToIfPreviousAnimation.class);
    }
}
