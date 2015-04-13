package com.ankamagames.wakfu.common.game.fight.spellCastValidation;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.ai.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;

final class SpellCastThroughGateComputer
{
    private BasicFightInfo m_linkedFight;
    private BasicCharacterInfo m_fighter;
    private AbstractSpellLevel m_spellLevel;
    private int m_rangeMin;
    private int m_boostedRangeMax;
    private Point3 m_targetCell;
    private final Map<BasicEffectArea, Set<CastValidity>> m_gatesCastValidity;
    private final Set<BasicEffectArea> m_inputGates;
    private final Set<BasicEffectArea> m_outputGates;
    
    SpellCastThroughGateComputer() {
        super();
        this.m_gatesCastValidity = new HashMap<BasicEffectArea, Set<CastValidity>>();
        this.m_inputGates = new HashSet<BasicEffectArea>();
        this.m_outputGates = new HashSet<BasicEffectArea>();
    }
    
    void compute() {
        this.computeInputAndOutputGates();
        this.computeOutputGatesCastValidity();
    }
    
    private void computeInputAndOutputGates() {
        this.computeInputGates();
        this.computeOutputGates();
    }
    
    private void computeOutputGates() {
        this.m_outputGates.clear();
        final Set<BasicEffectArea> inputGates = this.m_inputGates;
        if (inputGates.isEmpty()) {
            return;
        }
        this.m_outputGates.addAll(this.getTeamGates());
        if (inputGates.size() == 1) {
            this.m_outputGates.removeAll(inputGates);
        }
    }
    
    private void computeInputGates() {
        final Point3 position = this.m_fighter.getPosition();
        this.m_inputGates.clear();
        final Set<BasicEffectArea> teamGates = this.getTeamGates();
        for (final BasicEffectArea effectArea : teamGates) {
            final int distanceToArea = DistanceUtils.getIntersectionDistance(effectArea, position);
            if (distanceToArea <= this.m_boostedRangeMax) {
                if (distanceToArea < this.m_rangeMin) {
                    continue;
                }
                if (!this.checkLos(this.m_fighter.getPosition(), effectArea.getPosition())) {
                    continue;
                }
                if (!this.checkAlignement(effectArea.getPosition(), position)) {
                    continue;
                }
                this.m_inputGates.add(effectArea);
            }
        }
    }
    
    private void computeOutputGatesCastValidity() {
        this.m_gatesCastValidity.clear();
        final Set<BasicEffectArea> outputGates = this.m_outputGates;
        if (outputGates.isEmpty()) {
            return;
        }
        for (final BasicEffectArea outputGate : outputGates) {
            final Point3 position = outputGate.getPosition();
            final Set<CastValidity> castValidities = new HashSet<CastValidity>();
            final int distanceToTarget = position.getDistance(this.m_targetCell.getX(), this.m_targetCell.getY());
            if (this.m_spellLevel.getSpell().hasProperty(SpellPropertyType.ONLY_VALID_ON_OUTPUT_WHEN_USE_GATE)) {
                if (distanceToTarget > 0) {
                    castValidities.add(CastValidity.INVALID_RANGE);
                }
            }
            else {
                if (!this.checkAlignement(this.m_targetCell, position)) {
                    castValidities.add(CastValidity.CELLS_NOT_ALIGNED);
                }
                if (!isInRange(this.m_rangeMin, this.m_boostedRangeMax, distanceToTarget)) {
                    castValidities.add(CastValidity.INVALID_RANGE);
                }
                if (!this.checkLos(outputGate.getPosition(), this.m_targetCell)) {
                    castValidities.add(CastValidity.INVALID_LINE_OF_SIGHT);
                }
            }
            this.m_gatesCastValidity.put(outputGate, castValidities);
        }
    }
    
    private Set<BasicEffectArea> getTeamGates() {
        final Set<BasicEffectArea> res = new HashSet<BasicEffectArea>();
        for (final BasicEffectArea effectArea : this.m_linkedFight.getEffectAreaManager().getActiveEffectAreas()) {
            if (effectArea != null) {
                if (effectArea.getType() != EffectAreaType.GATE.getTypeId()) {
                    continue;
                }
                if (effectArea.getTeamId() != this.m_fighter.getTeamId()) {
                    continue;
                }
                final TByteHashSet obstaclesOnGate = this.m_linkedFight.getFightMap().getObstaclesIdFromPos(effectArea.getWorldCellX(), effectArea.getWorldCellY());
                if (obstaclesOnGate != null && !obstaclesOnGate.isEmpty()) {
                    continue;
                }
                res.add(effectArea);
            }
        }
        return res;
    }
    
    private static boolean isInRange(final int rangeMin, final int rangeMax, final int distanceToTarget) {
        return distanceToTarget <= rangeMax && distanceToTarget >= rangeMin;
    }
    
    private boolean checkLos(final Point3 startPoint, final Point3 endPoint) {
        final boolean testLineOfSight = this.m_spellLevel.getSpell().isTestLineOfSight(this.m_spellLevel, this.m_fighter, null, this.m_linkedFight.getContext());
        return !testLineOfSight || FightFunctions.hasLineOfSight(startPoint.getX(), startPoint.getY(), startPoint.getZ(), this.m_fighter.getHeight(), endPoint.getX(), endPoint.getY(), endPoint.getZ(), this.m_linkedFight.getFightMap());
    }
    
    private boolean checkAlignement(final Point3 targetCell, final Point3 position) {
        final boolean castOnlyInLine = this.m_spellLevel.getSpell().isCastOnlyInLine(this.m_spellLevel, this.m_fighter, targetCell, this.m_linkedFight.getContext());
        return !castOnlyInLine || isAligned(targetCell, position);
    }
    
    private static boolean isAligned(final Point3 targetCell, final Point3 position) {
        return position.getX() == targetCell.getX() || position.getY() == targetCell.getY();
    }
    
    void fillResults(final Collection<BasicEffectArea> inputGates, final Map<BasicEffectArea, Set<CastValidity>> gatesCastValidity) {
        inputGates.addAll(this.m_inputGates);
        gatesCastValidity.putAll(this.m_gatesCastValidity);
    }
    
    void setParams(final BasicFightInfo linkedFight, final BasicCharacterInfo fighter, final AbstractSpellLevel spellLevel, final int rangeMin, final int boostedRangeMax, final Point3 targetCell) {
        this.m_linkedFight = linkedFight;
        this.m_fighter = fighter;
        this.m_spellLevel = spellLevel;
        this.m_rangeMin = rangeMin;
        this.m_boostedRangeMax = boostedRangeMax;
        this.m_targetCell = targetCell;
    }
    
    void clearParams() {
        this.m_linkedFight = null;
        this.m_fighter = null;
        this.m_spellLevel = null;
        this.m_rangeMin = 0;
        this.m_boostedRangeMax = 0;
        this.m_targetCell = null;
    }
}
