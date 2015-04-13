package com.ankamagames.wakfu.client.core.game.fight.animation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.alea.animation.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;

public class SpellAttack extends AttackType
{
    private static final Logger m_logger;
    private CharacterInfo m_caster;
    private boolean m_started;
    
    public SpellAttack(final CharacterInfo caster) {
        super();
        this.m_started = false;
        this.m_caster = caster;
    }
    
    @Override
    public int getType() {
        return 2;
    }
    
    @Override
    public void endUsage(final CharacterActor actor) {
        super.endUsage(actor);
        this.m_started = false;
        actor.setAnimation(this.getEndUsageAnimation());
    }
    
    private static String getAnim(final CharacterActor actor, final String anim, final String defaultAnim) {
        if (actor.containsAnimation(anim)) {
            return anim;
        }
        return defaultAnim;
    }
    
    @Override
    public void startUsage(final CharacterActor actor) {
        if (this.m_started) {
            return;
        }
        this.m_started = true;
        assert actor != null;
        this.setMovementSelector(actor);
        actor.setStaticAnimationKey(getAnim(actor, "AnimStatique02", "AnimStatique"));
        actor.setHitAnimationKey(getAnim(actor, "AnimHit02", "AnimHit"));
        actor.setAnimation(this.getStartUsageAnimation());
    }
    
    @Override
    public void setMovementSelector(final CharacterActor actor) {
        final boolean customWalk = actor.containsAnimation("AnimMarche02");
        final boolean customRun = actor.containsAnimation("AnimCourse02");
        if (customWalk || customRun) {
            final PathMovementStyle walk = customWalk ? WalkWithSpellMovementStyle.getInstance() : WalkMovementStyle.getInstance();
            final PathMovementStyle run = customRun ? RunInFightMovementStyle.getInstance() : RunMovementStyle.getInstance();
            actor.setMovementSelector(new CustomMovementSelector(SimpleMovementSelector.getInstance(), walk, run));
        }
        else {
            actor.setMovementSelector(SimpleMovementSelector.getInstance());
        }
    }
    
    @Override
    public boolean equals(final AttackType attack) {
        return super.equals(attack) && this.m_caster.getBreedId() == ((SpellAttack)attack).m_caster.getBreedId();
    }
    
    @Override
    protected String getEndUsageAnimation() {
        return getAnim(this.m_caster.getActor(), "AnimStatique02-Fin", "AnimStatique");
    }
    
    @Override
    protected String getStartUsageAnimation() {
        final CharacterActor actor = this.m_caster.getActor();
        return getAnim(actor, "AnimStatique02-Debut", actor.getStaticAnimationKey());
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpellAttack.class);
    }
}
