package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public class IsEnemy extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsEnemy.signatures;
    }
    
    public IsEnemy(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionTarget == null) {
            return 0;
        }
        if (criterionUser == null || !(criterionUser instanceof CriterionUser)) {
            return -1;
        }
        final CriterionUser f = (CriterionUser)criterionUser;
        if (criterionTarget instanceof CriterionUser) {
            if (f.getTeamId() == ((CriterionUser)criterionTarget).getTeamId()) {
                return -1;
            }
            return 0;
        }
        else {
            if (criterionTarget instanceof Point3 && criterionContext instanceof AbstractFight) {
                final AbstractFight<BasicCharacterInfo> fight = (AbstractFight<BasicCharacterInfo>)criterionContext;
                final Point3 cell = (Point3)criterionTarget;
                final Collection<BasicCharacterInfo> ennemies = fight.getFightersInPlayOrOffPlayNotInTeam(f.getTeamId());
                for (final BasicCharacterInfo ennemy : ennemies) {
                    if (cell.equals(ennemy.getPosition())) {
                        return 0;
                    }
                }
                return -1;
            }
            return -2;
        }
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.ISENNEMY;
    }
    
    static {
        IsEnemy.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        IsEnemy.signatures.add(sig);
    }
}
