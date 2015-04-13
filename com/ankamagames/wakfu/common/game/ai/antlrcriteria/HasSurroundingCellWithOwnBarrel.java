package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public final class HasSurroundingCellWithOwnBarrel extends FunctionCriterion
{
    private static final List<ParserType[]> SIGNATURES;
    private final boolean m_target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasSurroundingCellWithOwnBarrel.SIGNATURES;
    }
    
    public HasSurroundingCellWithOwnBarrel(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user == null) {
            return -1;
        }
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null) {
            return -1;
        }
        Point3 cell = null;
        if (this.m_target) {
            if (criterionTarget instanceof BasicCharacterInfo) {
                cell = ((BasicCharacterInfo)criterionTarget).getPositionConst();
            }
            else if (criterionTarget instanceof Point3) {
                cell = (Point3)criterionTarget;
            }
            if (cell == null) {
                return 0;
            }
        }
        else {
            cell = user.getPosition();
        }
        final Collection<BasicEffectArea> areas = fight.getEffectAreaManager().getActiveEffectAreas();
        for (final BasicEffectArea area : areas) {
            if (isNotOwnBarrel(user, area)) {
                continue;
            }
            if (isAdjacent(cell, area)) {
                return 0;
            }
        }
        return -1;
    }
    
    private static boolean isNotOwnBarrel(final BasicCharacterInfo user, final BasicEffectArea area) {
        return area.getType() != EffectAreaType.BARREL.getTypeId() || area.getOwner() != user;
    }
    
    private static boolean isAdjacent(final Point3 cell, final BasicEffectArea barrel) {
        return (barrel.getWorldCellX() == cell.getX() + 1 && barrel.getWorldCellY() == cell.getY()) || (barrel.getWorldCellX() == cell.getX() - 1 && barrel.getWorldCellY() == cell.getY()) || (barrel.getWorldCellX() == cell.getX() && barrel.getWorldCellY() == cell.getY() + 1) || (barrel.getWorldCellX() == cell.getX() && barrel.getWorldCellY() == cell.getY() - 1) || (barrel.getWorldCellX() == cell.getX() + 1 && barrel.getWorldCellY() == cell.getY() + 1) || (barrel.getWorldCellX() == cell.getX() + 1 && barrel.getWorldCellY() == cell.getY() - 1) || (barrel.getWorldCellX() == cell.getX() - 1 && barrel.getWorldCellY() == cell.getY() + 1) || (barrel.getWorldCellX() == cell.getX() - 1 && barrel.getWorldCellY() == cell.getY() - 1);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_SURROUNDING_CELL_WITH_OWN_BARREL;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
