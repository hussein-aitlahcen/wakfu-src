package com.ankamagames.wakfu.client.core.action.fight;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public final class MoverHitInvisibleFighterAction extends AbstractFightAction
{
    private long m_moverId;
    private long m_hitedInvisibleFighterId;
    
    public MoverHitInvisibleFighterAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
        super(uniqueId, actionType, actionId, fightId);
    }
    
    public void setMoverId(final long moverId) {
        this.m_moverId = moverId;
    }
    
    public void setHitedInvisibleFighterId(final long hitedInvisibleFighterId) {
        this.m_hitedInvisibleFighterId = hitedInvisibleFighterId;
    }
    
    @Override
    protected void runCore() {
        final CharacterInfo mover = CharacterInfoManager.getInstance().getCharacter(this.m_moverId);
        final CharacterInfo hitedInvisibleFighter = CharacterInfoManager.getInstance().getCharacter(this.m_hitedInvisibleFighterId);
        mover.setDirection(new Vector3i(mover.getPosition(), hitedInvisibleFighter.getPosition()).toDirection4Prefering(mover.getDirection()));
        mover.getActor().setAnimation("AnimHit");
        hitedInvisibleFighter.getActor().setAnimation("AnimHit");
    }
    
    @Override
    protected void onActionFinished() {
    }
}
