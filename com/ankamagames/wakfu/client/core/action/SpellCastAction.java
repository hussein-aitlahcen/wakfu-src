package com.ankamagames.wakfu.client.core.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.fight.spellCastValidation.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public class SpellCastAction extends AbstractFightCastAction
{
    protected static final Logger m_logger;
    private short m_levelOfSpell;
    private final RawSpellLevel m_rawSpellLevel;
    private SpellCastThroughGateValidator m_spellCastThroughGateValidator;
    
    public SpellCastAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final RawSpellLevel spell, final boolean criticalHit, final boolean criticalMiss, final long casterId, final int x, final int y, final short z) {
        super(uniqueId, actionType, actionId, fightId, criticalHit, criticalMiss, casterId, x, y, z);
        this.m_spellCastThroughGateValidator = new SpellCastThroughGateValidator();
        this.m_rawSpellLevel = spell;
    }
    
    @Override
    public long onRun() {
        final CharacterInfo fighter = this.getFighterById(this.getInstigatorId());
        if (fighter == null) {
            return 0L;
        }
        final CharacterActor casterActor = fighter.getActor();
        casterActor.clearActiveParticleSystem();
        if (casterActor.getCurrentAttack() == null || casterActor.getCurrentAttack().getType() != 2) {
            casterActor.applyFightAnimation(new SpellAttack(fighter));
        }
        final SpellLevel spellLevel = extractSpellLevel(fighter, this.m_rawSpellLevel);
        if (spellLevel == null) {
            return 0L;
        }
        final Spell spell = spellLevel.getSpell();
        this.m_levelOfSpell = spellLevel.getLevel();
        if (!spell.isAssociatedWithItemUse() && !casterActor.getCharacterInfo().isActiveProperty(WorldPropertyType.DO_NOT_USE_SCRIPTS_ON_SPELLS)) {
            this.setScriptFileId(spell.getScriptId());
        }
        boolean computeGates = true;
        if (this.consernLocalPlayer()) {
            final Fight fight = fighter.getCurrentFight();
            if (fight != null) {
                fight.updateAggroListForAll(spellLevel, fighter);
                fight.onSpellCast(fighter, spell);
                final CastValidity validity = fight.getSpellCastValidityWithoutGates(fighter, spellLevel, new Point3(this.getX(), this.getY(), this.getZ()), false);
                if (validity == CastValidity.OK) {
                    computeGates = false;
                }
            }
            if (this.isCriticalHit()) {
                this.criticalHitVisualEffects(fighter);
                this.criticalHitSoundEffects();
            }
        }
        this.m_spellCastThroughGateValidator.setParams(this.getFight(), fighter, spellLevel, spellLevel.getMinRange(), spellLevel.getMaxRange(), new Point3(this.getX(), this.getY(), this.getZ()));
        if (computeGates) {
            this.m_spellCastThroughGateValidator.computeGatesIfNecessary();
        }
        if (WakfuGameEntity.getInstance().getLocalPlayer() == fighter) {
            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventSpellCast(spell.getId()));
        }
        if (!fighter.isInvisibleForLocalPlayer()) {
            return super.onRun();
        }
        this.fireActionFinishedEvent();
        return -1L;
    }
    
    public static SpellLevel extractSpellLevel(final CharacterInfo caster, final RawSpellLevel rawSpellLevel) {
        final SpellInventory<? extends AbstractSpellLevel> spellInventory = caster.getSpellInventory();
        SpellLevel spellLevel;
        if (spellInventory != null && spellInventory.getContentProvider() != null) {
            spellLevel = spellInventory.getContentProvider().unSerializeContent(rawSpellLevel);
        }
        else {
            final SpellLevelProvider spellLevelProvider = new SpellLevelProvider(caster);
            spellLevel = spellLevelProvider.unSerializeContent(rawSpellLevel);
        }
        return spellLevel;
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
    
    @Override
    public short getLevel() {
        return this.m_levelOfSpell;
    }
    
    @Override
    public boolean isTargetCellInRange() {
        final CharacterInfo fighter = this.getFighterById(this.getInstigatorId());
        if (fighter == null) {
            return false;
        }
        final SpellLevel spellLevel = extractSpellLevel(fighter, this.m_rawSpellLevel);
        final int minRange = spellLevel.getMinRange();
        final int maxRange = spellLevel.getMaxRange();
        final boolean canCastOnCasterCell = spellLevel.getSpell().isCanCastOnCasterCell();
        final int x = this.getX();
        final int y = this.getY();
        final CommonCastValidator<SpellLevel, CharacterInfo> commonCastValidator = new CommonCastValidator<SpellLevel, CharacterInfo>(null);
        return commonCastValidator.isValidRange(fighter, spellLevel, minRange, maxRange, canCastOnCasterCell, x, y);
    }
    
    @Override
    public BasicEffectArea getValidInputGate() {
        final CharacterInfo fighter = this.getFighterById(this.getInstigatorId());
        if (fighter == null) {
            return null;
        }
        return this.m_spellCastThroughGateValidator.getValidInputGate();
    }
    
    @Override
    public BasicEffectArea getValidOutputGate() {
        final CharacterInfo fighter = this.getFighterById(this.getInstigatorId());
        if (fighter == null) {
            return null;
        }
        return this.m_spellCastThroughGateValidator.getValidOutputGate();
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpellCastAction.class);
    }
}
