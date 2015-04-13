package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;

abstract class HasSurroundingCell extends FunctionCriterion
{
    protected boolean m_target;
    protected NeighbourhoodType m_neighbourhoodType;
    
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
        final boolean cellIsInNeighbourhood = this.cellIsInNeighbourhood(user, fight, cell);
        if (cellIsInNeighbourhood) {
            return 0;
        }
        return -1;
    }
    
    protected abstract boolean cellIsInNeighbourhood(final BasicCharacterInfo p0, final AbstractFight p1, final Point3 p2);
    
    protected void extractNeighbourhoodType(final String neighbourhoodName) {
        if (neighbourhoodName.equalsIgnoreCase(NeighbourhoodType.MOORE.toString())) {
            this.m_neighbourhoodType = NeighbourhoodType.MOORE;
        }
        else if (neighbourhoodName.equalsIgnoreCase(NeighbourhoodType.VON_NEUMANN.toString())) {
            this.m_neighbourhoodType = NeighbourhoodType.VON_NEUMANN;
        }
    }
    
    protected enum NeighbourhoodType
    {
        MOORE, 
        VON_NEUMANN;
    }
}
