package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public final class IsFreeCell extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private boolean m_fromTarget;
    private int m_x;
    private int m_y;
    
    public IsFreeCell(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_fromTarget = args.get(0).getValue().equalsIgnoreCase("target");
        this.m_x = (int)args.get(1).getLongValue(null, null, null, null);
        this.m_y = (int)args.get(2).getLongValue(null, null, null, null);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsFreeCell.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final Point3 fromPosition = CriteriaUtils.getTargetPosition(this.m_fromTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (fromPosition == null) {
            return -1;
        }
        final Point3 toPosition = CriteriaUtils.getTargetPosition(!this.m_fromTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (toPosition == null) {
            return -1;
        }
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null) {
            return -1;
        }
        final Direction8 direction = fromPosition.getDirection4To(toPosition);
        if (direction == null) {
            return -1;
        }
        final int diffX = direction.m_x * this.m_x - direction.m_y * this.m_y;
        final int diffY = direction.m_y * this.m_x + direction.m_x * this.m_y;
        final Point3 cellToCheck = new Point3(fromPosition);
        cellToCheck.add(diffX, diffY);
        if (!fight.getFightMap().isInsideOrBorder(cellToCheck.getX(), cellToCheck.getY())) {
            return -1;
        }
        final Collection<BasicEffectArea> areas = fight.getEffectAreaManager().getActiveEffectAreas();
        for (final BasicEffectArea area : areas) {
            if (area.getPosition().equalsIgnoringAltitude(cellToCheck) && area.isBlockingMovement()) {
                return -1;
            }
        }
        final FightObstacle obstacle = fight.getFightMap().getObstacle(cellToCheck.getX(), cellToCheck.getY());
        if (obstacle != null) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_FREE_CELL;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING, ParserType.NUMBER, ParserType.NUMBER });
    }
}
