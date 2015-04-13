package com.ankamagames.wakfu.client.alea.highlightingCells;

import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.wakfu.common.game.effect.targeting.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

final class EffectsExecutionZoneComputer
{
    private final ElementSelection m_zoneEffect;
    private final ElementSelection m_emptyCellsNeeded;
    private final EffectTargetsComputer m_effectTargetComputer;
    private final RunningEffectGroupExecutionTargetDisplayer m_runningEffectGroupExecutionTargetDisplayer;
    private final EmbeddedGroupExecutionTargetDisplayer m_embeddedGroupExecutionTargetDisplayer;
    private final EffectExecutionTargetDisplayer m_defaultExecutionTargetDisplayer;
    
    EffectsExecutionZoneComputer(final ElementSelection zoneEffect, final ElementSelection emptyCellsNeeded) {
        super();
        this.m_effectTargetComputer = new EffectTargetsComputer();
        this.m_runningEffectGroupExecutionTargetDisplayer = new RunningEffectGroupExecutionTargetDisplayer();
        this.m_embeddedGroupExecutionTargetDisplayer = new EmbeddedGroupExecutionTargetDisplayer();
        this.m_defaultExecutionTargetDisplayer = new EffectExecutionTargetDisplayer();
        this.m_zoneEffect = zoneEffect;
        this.m_emptyCellsNeeded = emptyCellsNeeded;
    }
    
    public void clearAndSelectZoneEffect(final EffectContainer<WakfuEffect> container, final CharacterInfo fighter, final Point3 target) {
        if (!(container instanceof AbstractEffectArea) || ((AbstractEffectArea)container).getType() != EffectAreaType.FECA_GLYPH.getTypeId()) {
            this.clear();
        }
        final short rootContainerLevel = ((WakfuEffectContainer)container).getLevel();
        this.selectZoneEffect(container, fighter, target, rootContainerLevel);
    }
    
    public void clearAndSelectTargetCell(final Point3 target) {
        this.clear();
        if (this.m_zoneEffect == null) {
            return;
        }
        this.m_zoneEffect.add(target.getX(), target.getY(), target.getZ());
    }
    
    private void selectZoneEffect(final EffectContainer<WakfuEffect> container, final CharacterInfo fighter, final Point3 target, final short rootContainerLevel) {
        if (container == null) {
            throw new IllegalArgumentException("container == null");
        }
        if (this.m_zoneEffect == null && this.m_emptyCellsNeeded == null) {
            return;
        }
        if (fighter == null || !fighter.getActor().isVisible()) {
            return;
        }
        final Fight fight = fighter.getCurrentFight();
        if (fight == null) {
            return;
        }
        final FightMap fightMap = fight.getFightMap();
        for (final WakfuEffect effect : container) {
            if (effect.getEffectType() != 2) {
                continue;
            }
            if (effect.checkFlags(1L)) {
                continue;
            }
            if (effect.hasProperty(RunningEffectPropertyType.DONT_DISPLAY_ZONE)) {
                continue;
            }
            if (rootContainerLevel > effect.getContainerMaxLevel()) {
                continue;
            }
            if (rootContainerLevel < effect.getContainerMinLevel()) {
                continue;
            }
            this.selectEmptyCellsNeeded(fighter, target, fightMap, effect);
            this.selectZoneEffect((WakfuEffectContainer)container, fighter, target, fight, fightMap, effect, rootContainerLevel);
        }
    }
    
    private void selectEmptyCellsNeeded(final CharacterInfo fighter, final Point3 target, final FightMap fightMap, final WakfuEffect effect) {
        if (this.m_emptyCellsNeeded == null) {
            return;
        }
        final AreaOfEffect emptyAoe = effect.getEmptyCellNeededAreaOfEffect();
        if (emptyAoe == null) {
            return;
        }
        final Iterable<int[]> cells = emptyAoe.getCells(target.getX(), target.getY(), target.getZ(), fighter.getWorldCellX(), fighter.getWorldCellY(), fighter.getWorldCellAltitude(), fighter.getDirection());
        for (final int[] areaCell : cells) {
            final int x = areaCell[0];
            final int y = areaCell[1];
            if (fightMap.isInsideOrBorder(x, y)) {
                final short z = fightMap.getCellHeight(x, y);
                this.m_emptyCellsNeeded.add(x, y, z);
            }
        }
    }
    
    private void selectZoneEffect(final WakfuEffectContainer container, final CharacterInfo fighter, final Point3 target, final Fight fight, final FightMap fightMap, final WakfuEffect effect, final short rootContainerLevel) {
        if (this.m_zoneEffect == null) {
            return;
        }
        if (!this.isEffectExecutionValid(fighter, target, fight, fightMap, effect)) {
            return;
        }
        if (this.isRunningEffectGroup(effect)) {
            this.selectZoneForEffectGroup(fighter, target, effect, rootContainerLevel);
            return;
        }
        final AreaOfEffect aoe = effect.getAreaOfEffect();
        if (aoe.getType() == AreaOfEffectEnum.EMPTY) {
            this.selectZoneForEmptyArea(fighter, target, fight, effect);
        }
        else {
            this.selectZoneForNonEmptyArea(container, fighter, target, fightMap, effect, aoe, rootContainerLevel);
        }
    }
    
    private boolean isEffectExecutionValid(final CharacterInfo fighter, final Point3 target, final Fight fight, final FightMap fightMap, final WakfuEffect effect) {
        final AreaOfEffect emptyAoe = effect.getEmptyCellNeededAreaOfEffect();
        if (emptyAoe != null) {
            final Iterable<int[]> emptyCells = emptyAoe.getCells(target.getX(), target.getY(), target.getZ(), fighter.getWorldCellX(), fighter.getWorldCellY(), fighter.getWorldCellAltitude(), fighter.getDirection());
            for (final int[] emptyCell : emptyCells) {
                if (fightMap.getObstacle(emptyCell[0], emptyCell[1]) != null) {
                    return false;
                }
            }
        }
        return effect.getConditions() == null || effect.getConditions().isValid(fighter, target, effect, fight);
    }
    
    private void selectZoneForDefaultEffect(final CharacterInfo fighter, final Point3 target, final WakfuEffect effect, final WakfuEffectContainer container, final short rootContainerLevel) {
        this.m_defaultExecutionTargetDisplayer.selectZones(fighter, target, effect, rootContainerLevel, container);
    }
    
    private void selectZoneForEmbeddedEffect(final CharacterInfo fighter, final Point3 target, final WakfuEffect effect, final short rootContainerLevel, final WakfuEffectContainer container) {
        final BasicEffectArea<WakfuEffect, EffectAreaParameters> effectArea = SetEffectArea.getBasicEffectAreaFromEffect(effect, container);
        if (effectArea != null) {
            this.m_embeddedGroupExecutionTargetDisplayer.selectZones(fighter, target, effect, rootContainerLevel, (WakfuEffectContainer)effectArea);
        }
    }
    
    private void selectZoneForEffectGroup(final CharacterInfo fighter, final Point3 target, final WakfuEffect effect, final short rootContainerLevel) {
        final int effectId = effect.getEffectId();
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(effectId);
        if (effectGroup == null) {
            return;
        }
        this.m_runningEffectGroupExecutionTargetDisplayer.selectZones(fighter, target, effect, rootContainerLevel, effectGroup);
    }
    
    private boolean isRunningEffectGroup(final WakfuEffect effect) {
        final int actionId = effect.getActionId();
        return RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.contains(actionId);
    }
    
    private boolean isEffectArea(final WakfuEffect effect) {
        final int actionId = effect.getActionId();
        return actionId == RunningEffectConstants.SET_GLYPH.getId() || actionId == RunningEffectConstants.SET_TRAP.getId() || actionId == RunningEffectConstants.SET_EFFECT_AREA.getId() || actionId == RunningEffectConstants.SET_FECA_GLYPH.getId() || this.isSetBombEffect(effect) || this.isSetBeaconEffect(effect);
    }
    
    private boolean isSetBeaconEffect(final WakfuEffect effect) {
        return effect.getActionId() == RunningEffectConstants.SET_BEACON.getId();
    }
    
    private boolean isSetBombEffect(final WakfuEffect effect) {
        return effect.getActionId() == RunningEffectConstants.SET_BOMB.getId();
    }
    
    private void selectZoneForNonEmptyArea(final WakfuEffectContainer container, final CharacterInfo fighter, final Point3 target, final FightMap fightMap, final WakfuEffect effect, final AreaOfEffect aoe, final short rootContainerLevel) {
        if (this.isSetBeaconEffect(effect)) {
            this.selectZoneForEmbeddedEffect(fighter, target, effect, rootContainerLevel, container);
        }
        else if (this.isSetBombEffect(effect)) {
            final BasicEffectArea<WakfuEffect, EffectAreaParameters> effectArea = SetEffectArea.getBasicEffectAreaFromEffect(effect, container);
            if (effectArea != null) {
                this.selectZoneEffect(effectArea, fighter, target, rootContainerLevel);
            }
        }
        else {
            this.selectZoneForDefaultEffect(fighter, target, effect, container, rootContainerLevel);
        }
    }
    
    private void selectZoneForEmptyArea(final CharacterInfo fighter, final Point3 target, final Fight fight, final WakfuEffect effect) {
        final Iterator<EffectUser> it = fight.getAllPossibleTargets();
        while (it.hasNext()) {
            final EffectUser eu = it.next();
            final ObjectPair<TargetValidity, ArrayList<EffectUser>> result = effect.getTargetValidator().getTargetValidity(eu, fighter);
            switch (result.getFirst()) {
                case VALID:
                case VALID_IF_IN_AOE:
                case SUBTARGET_VALID_ONLY: {
                    this.m_zoneEffect.add(eu.getWorldCellX(), eu.getWorldCellY(), eu.getWorldCellAltitude());
                    continue;
                }
            }
        }
        this.m_zoneEffect.add(target.getX(), target.getY(), target.getZ());
        if (this.m_emptyCellsNeeded != null) {
            this.m_emptyCellsNeeded.remove(target.getX(), target.getY(), target.getZ());
        }
    }
    
    void setTexture(final String textureFileName, final HighLightTextureApplication iso) {
        if (this.m_zoneEffect != null) {
            this.m_zoneEffect.setTexture(textureFileName, iso);
        }
    }
    
    public void clear() {
        if (this.m_zoneEffect != null) {
            this.m_zoneEffect.clear();
        }
        if (this.m_emptyCellsNeeded != null) {
            this.m_emptyCellsNeeded.clear();
        }
    }
    
    private class RunningEffectGroupExecutionTargetDisplayer extends EmbeddedGroupExecutionTargetDisplayer<RunningEffectGroup, AbstractEffectGroup>
    {
        @Override
        protected boolean canModifyTarget() {
            return true;
        }
        
        @Override
        protected RunningEffectGroup createRE(final WakfuEffect effect, final EffectContext context, final EffectUser caster, final EffectUser target, final Point3 targetCell) {
            final RunningEffectGroup re = super.createRE(effect, context, caster, target, targetCell);
            re.effectiveComputeValue(null);
            return re;
        }
        
        @Override
        protected boolean isSetCasterFromTarget(final RunningEffectGroup runningEffectGroup) {
            return runningEffectGroup.isSetCasterFromTarget();
        }
        
        @Override
        protected boolean isTransmitOriginalTarget(final RunningEffectGroup runningEffectGroup) {
            return runningEffectGroup.isTransmitOriginalTarget();
        }
    }
    
    private class EmbeddedGroupExecutionTargetDisplayer<RE extends RunningEffect<WakfuEffect, WakfuEffectContainer>, EC extends WakfuEffectContainer> extends EffectExecutionTargetDisplayer<RE, EC>
    {
        @Override
        protected void doSelectZone(final CharacterInfo caster, final Point3 target, final WakfuEffect effect, final short level, final EffectContainer<WakfuEffect> effectContainer) {
            final Direction8 directionToTargetCell = new Vector3i(caster.getPosition(), target).toDirection4();
            final Iterable<int[]> cells = this.getAreaOfEffect(effect, (WakfuEffectContainer)effectContainer).getCells(target.getX(), target.getY(), target.getZ(), caster.getWorldCellX(), caster.getWorldCellY(), caster.getWorldCellAltitude(), directionToTargetCell);
            final Point3 newTarget = new Point3();
            final FightMap fightMap = caster.getCurrentFight().getFightMap();
            for (final int[] areaCell : cells) {
                final int x = areaCell[0];
                final int y = areaCell[1];
                newTarget.set(x, y, target.getZ());
                if (fightMap.isInsideOrBorder(x, y)) {
                    EffectsExecutionZoneComputer.this.selectZoneEffect(effectContainer, caster, newTarget, level);
                }
            }
        }
    }
    
    private class EffectExecutionTargetDisplayer<RE extends RunningEffect<WakfuEffect, WakfuEffectContainer>, EC extends WakfuEffectContainer>
    {
        protected boolean canModifyTarget() {
            return false;
        }
        
        protected RE createRE(final WakfuEffect effect, final EffectContext context, final EffectUser caster, final EffectUser target, final Point3 targetCell) {
            final WakfuRunningEffect modelRE = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId());
            final RE re = (RE)modelRE.newInstance();
            re.setParameters(effect, null, context, caster, target, targetCell.getX(), targetCell.getY(), targetCell.getZ(), null);
            return re;
        }
        
        protected boolean isSetCasterFromTarget(final RE re) {
            return false;
        }
        
        protected boolean isTransmitOriginalTarget(final RE re) {
            return true;
        }
        
        protected AreaOfEffect getAreaOfEffect(final WakfuEffect effect, final WakfuEffectContainer container) {
            if (EffectsExecutionZoneComputer.this.isEffectArea(effect)) {
                final BasicEffectArea<WakfuEffect, EffectAreaParameters> effectArea = SetEffectArea.getBasicEffectAreaFromEffect(effect, container);
                if (effectArea != null) {
                    return effectArea.getArea();
                }
            }
            return effect.getAreaOfEffect();
        }
        
        public void selectZones(CharacterInfo fighter, final Point3 target, final WakfuEffect effect, final short rootContainerLevel, final WakfuEffectContainer container) {
            if (effect.recomputeAreaOfEffectDisplay()) {
                CharacterInfo targetUser = null;
                final Fight fight = fighter.getCurrentFight();
                targetUser = (CharacterInfo)fight.getCharacterInfoAtPosition(target.getX(), target.getY());
                final RE re = this.createRE(effect, fighter.getEffectContext(), fighter, targetUser, target);
                if (this.isSetCasterFromTarget(re) && targetUser != null) {
                    fighter = targetUser;
                }
                if (re.useTarget()) {
                    final int previousX = target.getX();
                    final int previousY = target.getY();
                    final short previousZ = target.getZ();
                    if (!this.isTransmitOriginalTarget(re)) {
                        targetUser = null;
                    }
                    if (targetUser == null || effect.isShouldRecomputeTarget()) {
                        if (this.canModifyTarget()) {
                            final List<List<EffectUser>> targets = EffectsExecutionZoneComputer.this.m_effectTargetComputer.determineTargets(effect, fighter, fight.getContext(), target.getX(), target.getY(), target.getZ());
                            for (int i = 0, size = targets.size(); i < size; ++i) {
                                final List<EffectUser> effectUsers = targets.get(i);
                                for (int j = 0, jSize = effectUsers.size(); j < jSize; ++j) {
                                    final EffectUser effectUser = effectUsers.get(j);
                                    target.set(effectUser.getWorldCellX(), effectUser.getWorldCellY(), effectUser.getWorldCellAltitude());
                                    this.doSelectZone(fighter, target, effect, rootContainerLevel, container);
                                }
                            }
                        }
                        else {
                            this.doSelectZone(fighter, target, effect, rootContainerLevel, container);
                        }
                    }
                    else {
                        target.set(targetUser.getWorldCellX(), targetUser.getWorldCellY(), targetUser.getWorldCellAltitude());
                        this.doSelectZone(fighter, target, effect, rootContainerLevel, container);
                    }
                    target.set(previousX, previousY, previousZ);
                }
                else {
                    this.doSelectZone(fighter, target, effect, rootContainerLevel, container);
                }
                re.release();
            }
            else {
                this.doSelectZone(fighter, target, effect, rootContainerLevel, container);
            }
        }
        
        protected void doSelectZone(final CharacterInfo caster, final Point3 target, final WakfuEffect effect, final short level, final EffectContainer<WakfuEffect> effectContainer) {
            Direction8 directionToTargetCell = new Vector3i(caster.getPosition(), target).toDirection4();
            if (directionToTargetCell == Direction8.NONE) {
                directionToTargetCell = caster.getDirection();
            }
            final Iterable<int[]> cells = this.getAreaOfEffect(effect, (WakfuEffectContainer)effectContainer).getCells(target.getX(), target.getY(), target.getZ(), caster.getWorldCellX(), caster.getWorldCellY(), caster.getWorldCellAltitude(), directionToTargetCell);
            final FightMap fightMap = caster.getCurrentFight().getFightMap();
            for (final int[] areaCell : cells) {
                final int x = areaCell[0];
                final int y = areaCell[1];
                if (fightMap.isInsideOrBorder(x, y)) {
                    EffectsExecutionZoneComputer.this.m_zoneEffect.add(x, y, fightMap.getCellHeight(x, y));
                    if (EffectsExecutionZoneComputer.this.m_emptyCellsNeeded == null) {
                        continue;
                    }
                    EffectsExecutionZoneComputer.this.m_emptyCellsNeeded.remove(x, y, fightMap.getCellHeight(x, y));
                }
            }
        }
    }
}
