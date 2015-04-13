package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;

public final class IsOnBorderCell extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_useTarget;
    
    public IsOnBorderCell(final ArrayList<ParserObject> args) {
        super();
        this.m_useTarget = false;
        this.checkType(args);
        this.m_useTarget = args.get(0).getValue().equalsIgnoreCase("target");
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsOnBorderCell.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        Point3 position = null;
        if (target != null) {
            position = target.getPosition();
        }
        else if (this.m_useTarget && criterionTarget instanceof Point3) {
            position = (Point3)criterionTarget;
        }
        if (position == null) {
            return -1;
        }
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight.getFightMap().isBorder(position.getX(), position.getY())) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ON_BORDER_CELL;
    }
    
    static {
        (IsOnBorderCell.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING });
    }
}
