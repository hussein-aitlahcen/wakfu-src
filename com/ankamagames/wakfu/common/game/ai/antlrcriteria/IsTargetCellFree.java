package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public class IsTargetCellFree extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_checkNonBlockingEffectArea;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsTargetCellFree.signatures;
    }
    
    IsTargetCellFree() {
        super();
        this.m_checkNonBlockingEffectArea = false;
    }
    
    public IsTargetCellFree(final ArrayList<ParserObject> args) {
        super();
        this.m_checkNonBlockingEffectArea = false;
        this.checkType(args);
        if (args.size() >= 1) {
            this.m_checkNonBlockingEffectArea = args.get(0).isValid(null, null, null, null);
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionTarget == null) {
            return 0;
        }
        if (criterionUser == null || !(criterionUser instanceof BasicCharacterInfo)) {
            return -1;
        }
        AbstractFight fight = null;
        if (criterionContext instanceof AbstractFight) {
            fight = (AbstractFight)criterionContext;
        }
        else if (criterionContext instanceof WakfuFightEffectContext) {
            fight = ((WakfuFightEffectContext)criterionContext).getFight();
        }
        if (fight == null) {
            return -1;
        }
        if (criterionTarget instanceof Point3) {
            final Point3 cell = (Point3)criterionTarget;
            final FightMap fightMap = fight.getFightMap();
            if (!fightMap.isInsideOrBorder(cell.getX(), cell.getY())) {
                return -1;
            }
            final Collection<BasicEffectArea> areas = fight.getEffectAreaManager().getActiveEffectAreas();
            for (final BasicEffectArea area : areas) {
                if (area.getPosition().equalsIgnoringAltitude(cell)) {
                    if (this.m_checkNonBlockingEffectArea) {
                        return -1;
                    }
                    if (area.isBlockingMovement()) {
                        return -1;
                    }
                    continue;
                }
            }
            final FightObstacle obstacle = fightMap.getObstacle(cell.getX(), cell.getY());
            if (obstacle == null) {
                return 0;
            }
            return -1;
        }
        else {
            if (!(criterionTarget instanceof EffectAreaProvider)) {
                return -2;
            }
            if (this.m_checkNonBlockingEffectArea) {
                return -1;
            }
            final BasicEffectArea effectArea = ((EffectAreaProvider)criterionTarget).getEffectArea();
            if (effectArea != null && effectArea.isBlockingMovement()) {
                return -1;
            }
            return 0;
        }
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_TARGET_CELL_FREE;
    }
    
    static {
        (IsTargetCellFree.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        IsTargetCellFree.signatures.add(CriterionConstants.ONE_BOOLEAN_SIGNATURE);
    }
}
