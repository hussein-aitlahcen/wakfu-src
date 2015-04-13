package com.ankamagames.wakfu.client.core.action.fight;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;

public final class JoinFightAction extends AbstractFightAction
{
    private byte m_teamId;
    private byte[] m_serializedEffectUserDatas;
    private byte[] m_serializedFighterDatas;
    private long m_fighterId;
    
    public JoinFightAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
        super(uniqueId, actionType, actionId, fightId);
    }
    
    @Override
    protected void runCore() {
        final Fight fight = (Fight)this.getFight();
        final CharacterInfo fighter = CharacterInfoManager.getInstance().getCharacter(this.m_fighterId);
        if (fighter == null) {
            JoinFightAction.m_logger.error((Object)("Impossible d'ajouter le joueur " + this.m_fighterId + " au combat " + fight.getId() + " : ce fighter n'existe pas"));
            return;
        }
        if (fighter == WakfuGameEntity.getInstance().getLocalPlayer()) {
            return;
        }
        this.reloadFighterForFight(fighter, this.m_serializedFighterDatas, this.m_serializedEffectUserDatas);
        fight.addFighterFromControllerToTeam(fighter, this.m_teamId, false);
        final CharacterActor actor = fighter.getActor();
        actor.addPassiveTeamParticleSystem(fighter.getTeamId());
        UIFightFrame.getInstance();
        UIFightFrame.hideFighter(fighter);
        FightVisibilityManager.getInstance().onFighterJoinFight(fighter, fight.getId());
        if (actor.containsAnimation("AnimApparition")) {
            actor.addAnimationEndedListener(new AnimationEndedListener() {
                @Override
                public void animationEnded(final AnimatedElement element) {
                    actor.removeAnimationEndedListener(this);
                    actor.addDirectionParticleSystem(fighter.getDirection());
                    actor.addPassiveTeamParticleSystem(fighter.getTeamId());
                }
            });
            WeaponAnimHelper.prepareAnimForFight(fighter);
            actor.setAnimation("AnimApparition");
        }
        else {
            WeaponAnimHelper.prepareAnimForFight(fighter);
            actor.addDirectionParticleSystem(fighter.getDirection());
            actor.addPassiveTeamParticleSystem(fighter.getTeamId());
        }
    }
    
    private void reloadFighterForFight(final CharacterInfo fighter, final byte[] serializedFighterDatas, final byte[] serializedEffectuserDatas) {
        if (fighter == null) {
            return;
        }
        fighter.reloadCharacterForFight((Fight)this.getFight(), serializedFighterDatas, serializedEffectuserDatas);
    }
    
    @Override
    protected void onActionFinished() {
    }
    
    public void setTeamId(final byte teamId) {
        this.m_teamId = teamId;
    }
    
    public void setSerializedEffectUserDatas(final byte[] serializedEffectUserDatas) {
        this.m_serializedEffectUserDatas = serializedEffectUserDatas;
    }
    
    public void setSerializedFighterDatas(final byte[] serializedFighterDatas) {
        this.m_serializedFighterDatas = serializedFighterDatas;
    }
    
    public void setFighterId(final long fighterId) {
        this.m_fighterId = fighterId;
    }
}
