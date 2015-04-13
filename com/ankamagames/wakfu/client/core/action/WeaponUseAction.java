package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.sound.*;

public class WeaponUseAction extends AbstractFightCastAction
{
    private final short m_level;
    
    public WeaponUseAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final ReferenceSkill skill, final boolean criticalHit, final boolean criticalMiss, final long casterId, final int x, final int y, final short z, final int itemReferenceId, final short skillLevel, final SpellLevel associatedSpellLevel) {
        super(uniqueId, actionType, actionId, fightId, criticalHit, criticalMiss, casterId, x, y, z);
        this.m_level = skillLevel;
        if (associatedSpellLevel != null && associatedSpellLevel.getSpell() != null) {
            this.setScriptFileId(associatedSpellLevel.getSpell().getScriptId());
        }
        else {
            this.setScriptFileId(skill.getScriptId());
        }
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    @Override
    public long onRun() {
        final CharacterInfo fighter = this.getFighterById(this.getInstigatorId());
        if (fighter == null || !this.consernLocalPlayer()) {
            return super.onRun();
        }
        if (fighter.getCurrentFight() != null) {
            fighter.getCurrentFight().updateAggroListForAll((short)1, fighter);
        }
        if (this.isCriticalHit()) {
            this.criticalHitVisualEffects(fighter);
            this.criticalHitSoundEffects();
        }
        this.displayChatLog(fighter);
        return super.onRun();
    }
    
    private void displayChatLog(final CharacterInfo fighter) {
        final TextWidgetFormater message = new TextWidgetFormater().append(WakfuTranslator.getInstance().getString("fight.closeCombat", new TextWidgetFormater().addColor(ChatConstants.CHAT_FIGHT_INFORMATION_COLOR).append(fighter.getControllerName()).finishAndToString()));
        if (this.isCriticalHit()) {
            message.b().addColor(ChatConstants.CHAT_FIGHT_EFFECT_COLOR).append(" (").append(WakfuTranslator.getInstance().getString("critical")).append(")")._b();
        }
        if (this.isCriticalMiss()) {
            message.b().addColor(ChatConstants.CHAT_FIGHT_EFFECT_COLOR).append(" (").append(WakfuTranslator.getInstance().getString("critical.miss")).append(")")._b();
        }
        WeaponUseAction.m_fightLogger.info(message.finishAndToString());
    }
    
    private void criticalHitVisualEffects(final CharacterInfo fighter) {
        this.playAps(fighter, 800017);
    }
    
    private void playAps(final CharacterInfo fighter, final int particleId) {
        if (WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight() == null || WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFightId() == this.getFight().getId()) {
            final FreeParticleSystem particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(particleId);
            if (particle != null) {
                if (this.getFight() != null) {
                    particle.setFightId(this.getFight().getId());
                }
                particle.setTarget(fighter.getActor());
                IsoParticleSystemManager.getInstance().addParticleSystem(particle);
            }
        }
    }
    
    private void criticalHitSoundEffects() {
        if (WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFightId() == this.getFight().getId()) {
            WakfuSoundManager.getInstance().playGUISound(600122L);
        }
    }
}
