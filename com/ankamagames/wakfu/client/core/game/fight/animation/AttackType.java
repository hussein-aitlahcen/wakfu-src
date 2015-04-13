package com.ankamagames.wakfu.client.core.game.fight.animation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import java.util.*;

public abstract class AttackType
{
    private static final Logger m_logger;
    public static final int NONE = -1;
    public static final int BARE_HAND = 0;
    public static final int WEAPON = 1;
    public static final int SPELL = 2;
    protected int m_weaponGfxId;
    private List<AttackTypeListener> m_listeners;
    
    public AttackType() {
        super();
        this.m_weaponGfxId = -1;
    }
    
    public abstract int getType();
    
    public void endUsage(final CharacterActor actor) {
        actor.setMovementSelector(SimpleMovementSelector.getInstance());
    }
    
    public int getEndUsageDuration(final CharacterActor actor) {
        return this.getAnimDuration(actor, this.getEndUsageAnimation());
    }
    
    public int getStartUsageDuration(final CharacterActor actor) {
        return this.getAnimDuration(actor, this.getStartUsageAnimation());
    }
    
    private int getAnimDuration(final CharacterActor actor, final String animName) {
        final int duration = actor.getAnimationDuration(animName);
        if (duration == Integer.MAX_VALUE) {
            if (!animName.equals(actor.getStaticAnimationKey())) {
                AttackType.m_logger.warn((Object)("acteur gfxId=" + actor.getCharacterInfo().getGfxId() + " n'a pas une anim " + animName + " valide"));
            }
            return 0;
        }
        return duration;
    }
    
    protected abstract String getEndUsageAnimation();
    
    protected abstract String getStartUsageAnimation();
    
    public final void startUsageAndNotify(final CharacterActor actor) {
        try {
            this.startUsage(actor);
        }
        catch (Exception e) {
            AttackType.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        this.fireUsageStarted(actor);
    }
    
    private void fireUsageStarted(final CharacterActor actor) {
        if (this.m_listeners == null) {
            return;
        }
        final Iterable<AttackTypeListener> listeners = new ArrayList<AttackTypeListener>(this.m_listeners);
        for (final AttackTypeListener listener : listeners) {
            try {
                listener.onUsageStarted(actor, this);
            }
            catch (Exception e) {
                AttackType.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    public abstract void startUsage(final CharacterActor p0);
    
    public boolean equals(final AttackType attack) {
        return attack == this || (attack != null && attack.getType() == this.getType());
    }
    
    public String getAnimTackle() {
        return "AnimHit";
    }
    
    public String[] getAnimWaiting() {
        return null;
    }
    
    public int getWeaponGfxId() {
        return this.m_weaponGfxId;
    }
    
    public abstract void setMovementSelector(final CharacterActor p0);
    
    public void addListener(final AttackTypeListener listener) {
        if (this.m_listeners == null) {
            this.m_listeners = new ArrayList<AttackTypeListener>();
        }
        if (this.m_listeners.contains(listener)) {
            return;
        }
        this.m_listeners.add(listener);
    }
    
    public void removeListener(final AttackTypeListener listener) {
        if (this.m_listeners != null) {
            this.m_listeners.remove(listener);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AttackType.class);
    }
}
