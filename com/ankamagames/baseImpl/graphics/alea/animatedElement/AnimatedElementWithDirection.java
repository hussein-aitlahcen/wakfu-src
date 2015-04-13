package com.ankamagames.baseImpl.graphics.alea.animatedElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;

public class AnimatedElementWithDirection extends AnimatedElement
{
    private static final Logger m_logger;
    protected Direction8 m_direction;
    protected boolean m_directionChanged;
    
    public AnimatedElementWithDirection() {
        super();
        this.m_direction = Direction8.SOUTH_EAST;
    }
    
    public AnimatedElementWithDirection(final long id) {
        super(id);
        this.m_direction = Direction8.SOUTH_EAST;
    }
    
    public AnimatedElementWithDirection(final long id, final float worldX, final float worldY) {
        super(id, worldX, worldY);
        this.m_direction = Direction8.SOUTH_EAST;
    }
    
    public AnimatedElementWithDirection(final long id, final float worldX, final float worldY, final float altitude) {
        super(id, worldX, worldY, altitude);
        this.m_direction = Direction8.SOUTH_EAST;
    }
    
    @Override
    protected final float getIncrementX() {
        return (this.m_direction == Direction8.SOUTH_EAST) ? 1.0f : 0.0f;
    }
    
    @Override
    protected final float getIncrementY() {
        return (this.m_direction == Direction8.SOUTH_WEST) ? 1.0f : 0.0f;
    }
    
    public void setDirection(final Direction8 direction) {
        if (direction == null) {
            AnimatedElementWithDirection.m_logger.error((Object)"Unable to set a Direction8 to null", (Throwable)new Exception());
            return;
        }
        if (this.m_direction != direction) {
            this.m_direction = direction;
            this.m_directionChanged = true;
        }
    }
    
    public Direction8 getDirection() {
        return this.m_direction;
    }
    
    @Override
    protected String getANMKey(final String animName) {
        return this.getFullAnimName(animName);
    }
    
    @Override
    protected boolean needDrawAgain() {
        return super.needDrawAgain() || this.m_directionChanged;
    }
    
    @Override
    protected void drawnAgain() {
        super.drawnAgain();
        this.m_directionChanged = false;
    }
    
    @Override
    public String getDisplayObjectLinkage() {
        return this.getFullAnimName(this.getAnimation());
    }
    
    @Override
    public int getAnimationDuration(final String animName) {
        if (animName == null) {
            return 0;
        }
        final String linkage = this.getFullAnimName(animName);
        return super.getAnimationDuration(linkage);
    }
    
    public boolean containsAnimation(final String anim) {
        final AnmInstance anmInstance = this.getAnmInstance();
        return anmInstance != null && anmInstance.containsAnimation(this.getFullAnimName(anim));
    }
    
    public final String getFullAnimName(final String anim) {
        return createLinkage(this.m_direction.m_index, anim, this.getAnimationSuffix());
    }
    
    public static String createLinkage(final int directionIndex, final String linkageName, final String animSuffix) {
        final StringBuilder sb = new StringBuilder(64);
        sb.append(directionIndex).append('_').append(linkageName);
        if (animSuffix != null) {
            sb.append(animSuffix);
        }
        return sb.toString();
    }
    
    @Override
    protected boolean needUpdateAnimation() {
        return super.needUpdateAnimation() || this.m_directionChanged;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnimatedElementWithDirection.class);
    }
}
