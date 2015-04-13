package com.ankamagames.wakfu.common.game.fight;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.spell.*;

public class CommonCastValidator<T extends WakfuEffectContainer, E extends EffectUser>
{
    private static Logger m_logger;
    final AbstractFight m_linkedFight;
    
    public CommonCastValidator(final AbstractFight linkedFight) {
        super();
        this.m_linkedFight = linkedFight;
    }
    
    public CastValidity getCastValidity(final E fighter, final T container, final Point3 targetCell, final boolean rangeBoostable, final int rangemin, final int rangemax, final boolean testLOS, final boolean cellMustBeFree, final boolean cellCantBeFightBorder, final boolean canCastOnCasterCell, final SimpleCriterion castCriterions) {
        if (container == null) {
            CommonCastValidator.m_logger.error((Object)this.m_linkedFight.withFightId("cast d'un conteneur null"));
            return CastValidity.INVALID_CONTAINER;
        }
        if (!this.m_linkedFight.getTimeline().hasCurrentFighter() || this.m_linkedFight.getTimeline().getCurrentFighterId() != fighter.getId()) {
            return CastValidity.CANT_CAST_BETWEEN_TURN;
        }
        final int boostedRangeMax = this.computeRangeMax(fighter, rangeBoostable, rangemin, rangemax);
        if (rangemin > boostedRangeMax) {
            return CastValidity.INVALID_RANGE;
        }
        final FightMap fightMap = this.m_linkedFight.getFightMap();
        if (targetCell != null && fightMap != null) {
            final int targetX = targetCell.getX();
            final int targetY = targetCell.getY();
            final short targetZ = targetCell.getZ();
            final EffectUser target = this.m_linkedFight.getCharacterInfoAtPosition(targetCell.getX(), targetCell.getY());
            if (!this.isValidRange(fighter, container, rangemin, boostedRangeMax, canCastOnCasterCell, targetX, targetY)) {
                return CastValidity.INVALID_RANGE;
            }
            if (!fightMap.isInMap(targetX, targetY)) {
                return CastValidity.INVALID_TARGET_CELL;
            }
            if (cellCantBeFightBorder && fightMap.isBorder(targetX, targetY)) {
                return CastValidity.INVALID_TARGET_CELL;
            }
            if (!fightMap.isInsideOrBorder(targetX, targetY)) {
                return CastValidity.INVALID_TARGET_CELL;
            }
            if (testLOS) {
                if (!this.isValidLos(fighter, container, targetX, targetY, targetZ, rangemin, boostedRangeMax, canCastOnCasterCell)) {
                    return CastValidity.INVALID_LINE_OF_SIGHT;
                }
            }
            else if (!fightMap.isWalkable(targetX, targetY, targetZ)) {
                return CastValidity.INVALID_LINE_OF_SIGHT;
            }
            if (castCriterions != null && !castCriterions.isValid(fighter, targetCell, container, this.m_linkedFight)) {
                return CastValidity.CAST_CRITERIONS_NOT_VALID;
            }
            final List targetsAtPosition = this.m_linkedFight.getPossibleTargetsAtPosition(targetCell);
            if (castCriterions != null && !targetsAtPosition.isEmpty()) {
                boolean oneIsValid = false;
                for (int i = 0, n = targetsAtPosition.size(); i < n; ++i) {
                    final Object targetAtPos = targetsAtPosition.get(i);
                    if (castCriterions.isValid(fighter, targetAtPos, container, this.m_linkedFight)) {
                        oneIsValid = true;
                        break;
                    }
                }
                if (!oneIsValid) {
                    return CastValidity.CAST_CRITERIONS_NOT_VALID;
                }
            }
            for (final WakfuEffect eff : container) {
                if (eff.getEmptyCellNeededAreaOfEffect() != null && eff.getTargetValidator() != null) {
                    final AreaOfEffect emptyCellNeededAreaOfEffect = eff.getEmptyCellNeededAreaOfEffect();
                    if (!fightMap.isAreaOfEffectCellsAllInsideOrBorderAndFree(emptyCellNeededAreaOfEffect, targetCell.getX(), targetCell.getY(), targetCell.getZ(), fighter.getWorldCellX(), fighter.getWorldCellY(), fighter.getWorldCellAltitude(), fighter.getDirection())) {
                        return CastValidity.NEED_EMPTY_CELLS;
                    }
                    continue;
                }
            }
            if (cellMustBeFree && fightMap.isMovementBlocked(targetX, targetY, targetZ)) {
                return CastValidity.CELL_NOT_FREE;
            }
            if (target != null) {
                if (target.isActiveProperty(FightPropertyType.UNTARGETTABLE_BY_OTHER) && target != fighter) {
                    return CastValidity.CAST_CRITERIONS_NOT_VALID;
                }
                if (fighter instanceof BasicCharacterInfo && target instanceof BasicCharacterInfo && fighter.isActiveProperty(FightPropertyType.COWARD)) {
                    final BasicCharacterInfo bciFighter = (BasicCharacterInfo)fighter;
                    final BasicCharacterInfo bciTarget = (BasicCharacterInfo)target;
                    if (bciFighter.getLevel() < bciTarget.getLevel()) {
                        return CastValidity.CAST_CRITERIONS_NOT_VALID;
                    }
                }
                boolean validTarget = false;
                for (final Effect eff2 : container) {
                    if (eff2.getTargetValidator() == null) {
                        validTarget = true;
                        break;
                    }
                    final ObjectPair<TargetValidity, ArrayList<EffectUser>> result = eff2.getTargetValidator().getTargetValidity(target, fighter);
                    switch (result.getFirst()) {
                        case VALID:
                        case VALID_IF_IN_AOE:
                        case SUBTARGET_VALID_ONLY: {
                            validTarget = true;
                            continue;
                        }
                    }
                }
                if (!validTarget) {
                    return CastValidity.OK_BUT_NO_EFFECT_ON_TARGET;
                }
            }
        }
        else if (castCriterions != null && !castCriterions.isValid(fighter, targetCell, container, this.m_linkedFight)) {
            return CastValidity.CAST_CRITERIONS_NOT_VALID;
        }
        return CastValidity.OK;
    }
    
    int computeRangeMax(final E fighter, final boolean rangeBoostable, final int rangemin, int rangemax) {
        int poBoost = fighter.getCharacteristicValue(FighterCharacteristicType.RANGE);
        if (fighter instanceof CarryTarget && ((CarryTarget)fighter).isCarried()) {
            ++poBoost;
        }
        if (rangemax >= 1 && rangeBoostable) {
            rangemax = Math.max(rangemax + poBoost, rangemin);
        }
        if (fighter.isActiveProperty(FightPropertyType.RANGE_REDUCED_TO_CAC) && rangemax > 1) {
            rangemax = 1;
        }
        return rangemax;
    }
    
    protected boolean isValidLos(final E fighter, final T container, final int targetX, final int targetY, final short targetZ, final int rangeMin, final int rangeMax, final boolean canCastOnCasterCell) {
        return FightFunctions.hasLineOfSight(fighter, this.m_linkedFight, targetX, targetY, targetZ);
    }
    
    public boolean isValidRange(final E fighter, final T container, final int rangeMin, final int rangeMax, final boolean canCastOnCasterCell, final int targetX, final int targetY) {
        final int distance = Math.abs(targetX - fighter.getWorldCellX()) + Math.abs(targetY - fighter.getWorldCellY());
        if (distance < rangeMin || distance > rangeMax) {
            if (distance != 0) {
                return false;
            }
            if (!canCastOnCasterCell) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean checkCellAlignement(final BasicCharacterInfo fighter, final Point3 targetCell, final AbstractSpellLevel spellLevel, final BasicCharacterInfo target, final AbstractFight fight) {
        final AbstractSpell spell = spellLevel.getSpell();
        final FightMap fightMap = fight.getFightMap();
        final boolean castOnlyInLine = spell.isCastOnlyInLine(spellLevel, fighter, targetCell, fight.getContext());
        if (!spell.isCastOnlyInDiag() && !castOnlyInLine) {
            return true;
        }
        boolean castOnlyInlineValid;
        if (castOnlyInlineValid = castOnlyInLine) {
            if (targetCell.getX() != fighter.getPosition().getX() && targetCell.getY() != fighter.getPosition().getY()) {
                castOnlyInlineValid = false;
            }
            if (castOnlyInlineValid && spell.hasToTestDirectPath()) {
                final PathFinder pathfinder = PathFinder.checkOut();
                pathfinder.setMoverCaracteristics(fighter.getHeight(), fighter.getPhysicalRadius(), fighter.getJumpCapacity());
                pathfinder.setTopologyMapInstanceSet(fightMap);
                if (target != null) {
                    fightMap.addIgnoredMovementObstacle(target);
                }
                final boolean straightMovePossible = pathfinder.isStraightMovePossible(fighter.getPositionConst(), targetCell);
                fightMap.clearIgnoredMovementObstacles();
                pathfinder.release();
                if (!straightMovePossible) {
                    castOnlyInlineValid = false;
                }
            }
        }
        if (!spell.isCastOnlyInDiag() || castOnlyInlineValid) {
            return castOnlyInlineValid;
        }
        return Math.abs(targetCell.getX() - fighter.getPosition().getX()) == Math.abs(targetCell.getY() - fighter.getPosition().getY());
    }
    
    static {
        CommonCastValidator.m_logger = Logger.getLogger((Class)CommonCastValidator.class);
    }
}
