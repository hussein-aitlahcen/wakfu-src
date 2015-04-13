package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public class HasFreeSurroundingCell extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> signatures;
    private final boolean m_withBorder;
    private final boolean m_useTarget;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasFreeSurroundingCell.signatures;
    }
    
    public HasFreeSurroundingCell(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        if (args.isEmpty()) {
            this.m_withBorder = false;
            this.m_useTarget = true;
        }
        else if (args.size() == 1) {
            this.m_withBorder = args.get(0).isValid(null, null, null, null);
            this.m_useTarget = true;
        }
        else {
            this.m_withBorder = args.get(0).isValid(null, null, null, null);
            this.m_useTarget = args.get(1).isValid(null, null, null, null);
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return -1;
        }
        if (!(criterionUser instanceof BasicCharacterInfo)) {
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
        Point3 cell = null;
        if (target instanceof BasicCharacterInfo) {
            cell = ((BasicCharacterInfo)target).getPositionConst();
        }
        else if (target instanceof Point3) {
            cell = (Point3)target;
        }
        if (cell == null) {
            return -2;
        }
        final FightMap fightMap = fight.getFightMap();
        if (this.isCellFree(fightMap, cell.getX() + 1, cell.getY(), this.m_withBorder)) {
            return 0;
        }
        if (this.isCellFree(fightMap, cell.getX() - 1, cell.getY(), this.m_withBorder)) {
            return 0;
        }
        if (this.isCellFree(fightMap, cell.getX(), cell.getY() + 1, this.m_withBorder)) {
            return 0;
        }
        if (this.isCellFree(fightMap, cell.getX(), cell.getY() - 1, this.m_withBorder)) {
            return 0;
        }
        return -1;
    }
    
    private boolean isCellFree(final FightMap fightMap, final int x, final int y, final boolean withBorder) {
        if (fightMap.getObstacle(x, y) != null) {
            return false;
        }
        if (withBorder) {
            return fightMap.isInsideOrBorder(x, y);
        }
        return fightMap.isInside(x, y);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_FREE_SURROUNDING_CELL;
    }
    
    static {
        (signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        HasFreeSurroundingCell.signatures.add(CriterionConstants.ONE_BOOLEAN_SIGNATURE);
        HasFreeSurroundingCell.signatures.add(new ParserType[] { ParserType.BOOLEAN, ParserType.BOOLEAN });
    }
}
