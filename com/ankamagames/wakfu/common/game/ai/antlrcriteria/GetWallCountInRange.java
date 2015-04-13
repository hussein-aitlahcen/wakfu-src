package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.ai.*;
import java.util.*;

public class GetWallCountInRange extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_maxRange;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetWallCountInRange.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetWallCountInRange(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_maxRange = args.get(0);
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        long value = 0L;
        if (criterionContext != null && criterionContext instanceof AbstractFight) {
            if (!(criterionUser instanceof BasicFighter)) {
                throw new CriteriaExecutionException("On essaie de compter les walls \u00e0 port\u00e9e d'autre chose qu'un BasicFighter");
            }
            final AbstractFight<?> fight = (AbstractFight<?>)criterionContext;
            final BasicFighter user = (BasicFighter)criterionUser;
            for (final BasicEffectArea effectArea : fight.getEffectAreaManager().getActiveEffectAreas()) {
                if (effectArea != user && effectArea != null) {
                    if (effectArea.getType() != EffectAreaType.WALL.getTypeId()) {
                        continue;
                    }
                    final int distanceBetweenUserAndEffectUser = DistanceUtils.getIntersectionDistance(effectArea, user);
                    if (distanceBetweenUserAndEffectUser > this.m_maxRange.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext)) {
                        continue;
                    }
                    ++value;
                }
            }
        }
        return super.getSign() * value;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_WALL_COUNT_IN_RANGE;
    }
    
    static {
        GetWallCountInRange.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        GetWallCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER };
        GetWallCountInRange.signatures.add(sig);
    }
}
