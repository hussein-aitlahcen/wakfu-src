package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.characteristics.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.framework.ai.dataProvider.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractEffectArea extends BasicEffectArea<WakfuEffect, EffectAreaParameters> implements WakfuEffectContainer, Parameterized, CriterionUser
{
    public static final short DEFAULT_AGGRO_WEIGHT = 1;
    public static final short DEFAULT_ALLY_EFFICACITY = 1;
    public static final short DEFAULT_FOE_EFFICACITY = 1;
    protected BitSet m_destructionTriggers;
    protected Set<PropertyType> m_properties;
    protected short m_level;
    protected SetEffectArea m_linkedEffect;
    private boolean m_shouldStopMover;
    private byte m_teamId;
    protected BasicCharacterInfo m_forcedTarget;
    
    protected AbstractEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargeted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargeted, canBeDestroyed, maxLevel);
        this.m_shouldStopMover = true;
        this.m_teamId = -1;
        this.validateParamsLength(params);
    }
    
    private void validateParamsLength(final float[] params) {
        if (!this.getParametersListSet().mapValueCount((params == null) ? 0 : params.length)) {
            AbstractEffectArea.m_logger.error((Object)"effectArea poss\u00e9dant un mauvais nombre de param\u00e8tres");
        }
    }
    
    protected AbstractEffectArea() {
        super();
        this.m_shouldStopMover = true;
        this.m_teamId = -1;
    }
    
    @Override
    public AbstractEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final AbstractEffectArea area = (AbstractEffectArea)super.instanceAnother(parameters);
        area.m_destructionTriggers = this.m_destructionTriggers;
        area.m_shouldStopMover = this.m_shouldStopMover;
        if (this.m_properties != null) {
            area.m_properties = new HashSet<PropertyType>(this.m_properties);
        }
        if (parameters != null) {
            area.m_level = parameters.getLevel();
        }
        else {
            area.m_level = this.m_level;
        }
        return area;
    }
    
    @Override
    protected void setOwner(final EffectUser owner) {
        super.setOwner(owner);
        if (owner != null && owner.getEffectUserType() == 20) {
            this.m_teamId = ((BasicCharacterInfo)owner).getTeamId();
        }
    }
    
    public abstract void initialize();
    
    @Override
    public boolean triggers(@Nullable final RunningEffect triggeringRE, final Target triggerer) {
        return (!(triggerer instanceof FightEffectUser) || !this.isActiveProperty(EffectAreaPropertyType.NOT_ACTIVATED_BY_ALLIES) || this.m_teamId != ((FightEffectUser)triggerer).getTeamId()) && super.triggers(triggeringRE, triggerer);
    }
    
    @Override
    public byte getEffectUserType() {
        return 2;
    }
    
    public AbstractCharacteristic getDisplayedCharacteristic() {
        return null;
    }
    
    @Override
    public AbstractCharacteristic getCharacteristic(final CharacteristicType charac) {
        return null;
    }
    
    public FillableCharacteristicManager getCharacteristics() {
        return null;
    }
    
    @Override
    public boolean hasCharacteristic(final CharacteristicType charac) {
        return false;
    }
    
    protected void initializeDmgCharac() {
        if (this.m_owner == null) {
            return;
        }
        this.initDmgInPercent();
        this.initElementalDamage();
        this.initSecondaryDamage();
    }
    
    private void initSecondaryDamage() {
        this.initCharac(FighterCharacteristicType.AOE_DMG, this.m_owner);
        this.initCharac(FighterCharacteristicType.SINGLE_TARGET_DMG, this.m_owner);
        this.initCharac(FighterCharacteristicType.MELEE_DMG, this.m_owner);
        this.initCharac(FighterCharacteristicType.RANGED_DMG, this.m_owner);
    }
    
    private void initElementalDamage() {
        for (final Elements element : Elements.values()) {
            final FighterCharacteristicType dmgBonusCharac = element.getDamageBonusCharacteristic();
            if (this.m_owner.hasCharacteristic(dmgBonusCharac) && this.hasCharacteristic(dmgBonusCharac)) {
                final AbstractCharacteristic characteristic = this.getCharacteristic(dmgBonusCharac);
                characteristic.makeDefault();
                characteristic.add(this.m_owner.getCharacteristicValue(dmgBonusCharac));
            }
        }
    }
    
    private void initDmgInPercent() {
        if (!this.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
            return;
        }
        final AbstractCharacteristic dmgCharac = this.getCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT);
        dmgCharac.makeDefault();
        if (this.m_owner.hasCharacteristic(FighterCharacteristicType.SUMMONING_MASTERY)) {
            dmgCharac.add(this.m_owner.getCharacteristicValue(FighterCharacteristicType.SUMMONING_MASTERY));
        }
        if (this.m_owner.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
            dmgCharac.add(this.m_owner.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT));
        }
    }
    
    @Override
    public void goOffPlay(final EffectUser killer) {
        this.triggers(10014, null, killer);
        super.goOffPlay(killer);
    }
    
    @Override
    public boolean hasActivationDelay() {
        return this.m_deactivationDelay != null && this.m_deactivationDelay.length == 4 && (this.m_deactivationDelay[0] + this.getLevel() * this.m_deactivationDelay[1] > 0.0f || this.m_deactivationDelay[2] + this.getLevel() * this.m_deactivationDelay[3] > 0.0f);
    }
    
    @Override
    public boolean isActiveProperty(final PropertyType property) {
        return this.m_properties != null && this.m_properties.contains(property);
    }
    
    @Override
    public byte getPropertyValue(final PropertyType property) {
        if (this.m_properties == null) {
            return 0;
        }
        return (byte)(this.m_properties.contains(property) ? 1 : 0);
    }
    
    @Override
    public void addProperty(final PropertyType property) {
        if (property == null) {
            return;
        }
        if (this.m_properties == null) {
            this.m_properties = new HashSet<PropertyType>();
        }
        this.m_properties.add(property);
    }
    
    public void addProperties(final int... properties) {
        if (properties == null) {
            return;
        }
        for (int i = 0; i < properties.length; ++i) {
            final int id = properties[i];
            final EffectAreaPropertyType property = EffectAreaPropertyType.getPropertyFromId(id);
            this.addProperty(property);
        }
    }
    
    @Override
    public void substractProperty(final PropertyType property) {
        this.removeProperty(property);
    }
    
    @Override
    public void removeProperty(final PropertyType property) {
        if (this.m_properties == null) {
            return;
        }
        this.m_properties.remove(property);
    }
    
    @Override
    public void setPropertyValue(final PropertyType property, final byte value) {
        if (property.getPropertyTypeId() != 4) {
            return;
        }
        if (value > 0) {
            this.addProperty(property);
        }
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    @Override
    public boolean hasNoExecutionCount() {
        return this.m_maxExecutionCount < 0;
    }
    
    @Override
    public void pushActivationEventForTargetInTimeline(final Target applicant) {
        if (this.m_context != null && this.m_context.getTimeline() != null) {
            final TurnBasedTimeline timeline = this.m_context.getTimeline();
            final int tableturn = (int)(this.m_deactivationDelay[0] + this.getLevel() * this.m_deactivationDelay[1]);
            final int turn = (int)(this.m_deactivationDelay[2] + this.getLevel() * this.m_deactivationDelay[3]);
            final RelativeFightTimeInterval timeToActivation = RelativeFightTimeInterval.turnsFromNow((short)tableturn).atEndOfTurn(turn > 0);
            timeline.addTimeEvent(new EffectAreaActivationEvent(this, applicant.getId()), timeToActivation.withPriority((short)1));
        }
    }
    
    @Override
    public void untriggers(final Target triggerer) {
        final List<Target> targets = this.determineUnapplicationTarget(triggerer);
        if (targets != null && !targets.isEmpty()) {
            for (final Target target : targets) {
                if (target != null && target instanceof EffectUser && ((EffectUser)target).getRunningEffectManager() != null) {
                    ((EffectUser)target).getRunningEffectManager().removeLinkedToContainer(this, true, true);
                }
                this.onUnapplication(target);
            }
        }
        else {
            if (this.m_context == null) {
                return;
            }
            final TargetInformationProvider targetInformationProvider = this.m_context.getTargetInformationProvider();
            if (targetInformationProvider == null) {
                return;
            }
            final Iterator targetsIterator = targetInformationProvider.getAllPossibleTargets();
            while (targetsIterator.hasNext()) {
                final Object target2 = targetsIterator.next();
                if (!(target2 instanceof EffectUser)) {
                    continue;
                }
                final EffectUser effectUser = (EffectUser)target2;
                if (effectUser.getRunningEffectManager() == null) {
                    continue;
                }
                effectUser.getRunningEffectManager().removeLinkedToContainer(this, true);
            }
        }
    }
    
    @Override
    public boolean checkTriggers(final BitSet triggers, final Target applicant) {
        return triggers != null && (this.m_applicationTriggers.intersects(triggers) || this.m_unapplicationTriggers.intersects(triggers) || this.m_destructionTriggers.intersects(triggers));
    }
    
    @Override
    public void triggers(final BitSet triggers, @Nullable final RunningEffect triggeringRE, final Target applicant) {
        super.triggers(triggers, triggeringRE, applicant);
        if (this.m_destructionTriggers.intersects(triggers)) {
            this.selfDestruction();
        }
    }
    
    private void selfDestruction() {
        final RunningEffectManager manager = this.m_linkedEffect.getManagerWhereIamStored();
        if (manager != null) {
            manager.removeEffect(this.m_linkedEffect);
        }
        else {
            this.m_context.getEffectAreaManager().removeEffectArea(this);
        }
    }
    
    @Override
    public void onEffectAreaRemovedFromManager() {
        super.onEffectAreaRemovedFromManager();
        if (this.m_linkedEffect == null) {
            return;
        }
        final RunningEffectManager rem = this.m_linkedEffect.getManagerWhereIamStored();
        if (rem != null) {
            rem.removeEffect(this.m_linkedEffect);
        }
    }
    
    @Override
    public void onGoesOffPlay() {
        if (this.m_context instanceof WakfuFightEffectContextInterface) {
            ((WakfuFightEffectContextInterface)this.m_context).onEffectAreaGoesOffPlay(this);
        }
        super.onGoesOffPlay();
    }
    
    @Override
    public short getAggroWeight() {
        return 1;
    }
    
    @Override
    public short getAllyEfficacity() {
        return 1;
    }
    
    @Override
    public short getFoeEfficacity() {
        return 1;
    }
    
    public void setDestructionTriggers(final BitSet destructionTriggers) {
        this.m_destructionTriggers = destructionTriggers;
    }
    
    public void setLinkedEffect(final SetEffectArea linkedEffect) {
        this.m_linkedEffect = linkedEffect;
    }
    
    @Override
    public boolean canBeTargeted() {
        return true;
    }
    
    public void setShouldStopMover(final boolean shouldStopMover) {
        this.m_shouldStopMover = shouldStopMover;
    }
    
    @Override
    public boolean shouldStopMover() {
        return this.m_shouldStopMover;
    }
    
    public void onSelectionChanged(final boolean selected) {
    }
    
    @Override
    public byte getTeamId() {
        return this.m_teamId;
    }
    
    @Override
    public void setTeamId(final byte teamId) {
        this.m_teamId = teamId;
    }
    
    @Override
    public Breed getBreed() {
        return AvatarBreed.NONE;
    }
    
    @Override
    public long getOriginalControllerId() {
        if (this.m_owner != null) {
            return this.m_owner.getId();
        }
        return 0L;
    }
    
    @Override
    public boolean isSummoned() {
        return false;
    }
    
    @Override
    public int getCharacteristicMax(final CharacteristicType charac) {
        if (this.hasCharacteristic(charac)) {
            return this.getCharacteristic(charac).max();
        }
        return 0;
    }
    
    @Override
    public boolean hasProperty(final PropertyType property) {
        return false;
    }
    
    @Override
    public boolean hasState(final long stateId) {
        final State state = this.getState(stateId);
        return state != null;
    }
    
    @Override
    public int getStateLevel(final long stateId) {
        if (this.getRunningEffectManager() == null) {
            return -1;
        }
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getId() == RunningEffectConstants.RUNNING_STATE.getId() && ((StateRunningEffect)runningEffect).getState().getStateBaseId() == stateId) {
                return ((StateRunningEffect)runningEffect).getState().getLevel();
            }
        }
        return -1;
    }
    
    @Override
    public boolean hasState(final long stateId, final long stateLevel) {
        final State state = this.getState(stateId);
        return state != null && stateLevel == state.getLevel();
    }
    
    private State getState(final long stateId) {
        if (this.getRunningEffectManager() == null) {
            return null;
        }
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
                final State state = ((StateRunningEffect)runningEffect).getState();
                if (state.getStateBaseId() == stateId) {
                    return state;
                }
                continue;
            }
        }
        return null;
    }
    
    @Override
    public boolean hasStateFromUser(final long stateId, final long stateLevel, final CriterionUser casterUser) {
        if (this.getRunningEffectManager() == null) {
            return false;
        }
        if (casterUser == null) {
            return false;
        }
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
                final State state = ((StateRunningEffect)runningEffect).getState();
                if (state.getStateBaseId() != stateId) {
                    continue;
                }
                return state.getLevel() == stateLevel && runningEffect.getCaster() != null && runningEffect.getCaster().getId() == casterUser.getId();
            }
        }
        return false;
    }
    
    @Override
    public boolean hasStateFromUser(final long stateId, final CriterionUser casterUser) {
        if (this.getRunningEffectManager() == null) {
            return false;
        }
        if (casterUser == null) {
            return false;
        }
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
                final State state = ((StateRunningEffect)runningEffect).getState();
                if (state.getStateBaseId() != stateId) {
                    continue;
                }
                return runningEffect.getCaster() != null && runningEffect.getCaster().getId() == casterUser.getId();
            }
        }
        return false;
    }
    
    @Override
    public boolean hasStateFromLevel(final long stateId, final long stateLevel) {
        final State state = this.getState(stateId);
        return state != null && stateLevel <= state.getLevel();
    }
    
    @Override
    public boolean is(final CriterionUserType type) {
        return CriterionUserType.EFFECT_AREA.is(type);
    }
    
    public void initCharac(final CharacteristicType charac, final EffectUser owner) {
        if (this.hasCharacteristic(charac) && owner.hasCharacteristic(charac)) {
            final AbstractCharacteristic characteristic = this.getCharacteristic(charac);
            characteristic.makeDefault();
            characteristic.add(owner.getCharacteristicValue(charac));
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_teamId = -1;
        this.m_linkedEffect = null;
        this.m_properties = null;
        this.m_forcedTarget = null;
    }
    
    public String getName() {
        return this.toString();
    }
    
    @Override
    public boolean isOnFight() {
        return true;
    }
    
    @Override
    public int getSummoningsCount() {
        return 0;
    }
    
    @Override
    public int getSummoningsCount(final int summonBreedId) {
        return 0;
    }
    
    @Override
    public boolean isSummonedFromSymbiot() {
        return false;
    }
    
    protected EffectUser getLauncher() {
        if (this.isActiveProperty(EffectAreaPropertyType.OWNER_IS_CASTER) && this.m_owner != null) {
            return this.m_owner;
        }
        return this;
    }
    
    public void setForcedTarget(final BasicCharacterInfo forcedTarget) {
        this.m_forcedTarget = forcedTarget;
    }
    
    public abstract byte[] serializeSpecificInfoForReconnection();
    
    public abstract void unserializeSpecificInfoForReconnection(final ByteBuffer p0);
}
