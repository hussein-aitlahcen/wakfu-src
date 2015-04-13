package com.ankamagames.wakfu.client.core.game.fight.animation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class NoneAttack extends AttackType
{
    private static final Logger m_logger;
    private static final NoneAttack m_instance;
    
    public static NoneAttack getInstance() {
        return NoneAttack.m_instance;
    }
    
    @Override
    public int getType() {
        return -1;
    }
    
    @Override
    protected String getEndUsageAnimation() {
        return null;
    }
    
    @Override
    protected String getStartUsageAnimation() {
        return null;
    }
    
    @Override
    public void startUsage(final CharacterActor actor) {
        if (actor.getHmiHelper().hasAnimStaticChanges()) {
            return;
        }
        actor.setStaticAnimationKey("AnimStatique");
        actor.setAnimation(actor.getStaticAnimationKey());
    }
    
    @Override
    public void setMovementSelector(final CharacterActor actor) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NoneAttack.class);
        m_instance = new NoneAttack();
    }
}
