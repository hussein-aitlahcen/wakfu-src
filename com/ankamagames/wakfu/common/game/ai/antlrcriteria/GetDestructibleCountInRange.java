package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.*;
import java.util.*;

public class GetDestructibleCountInRange extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_maxRange;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetDestructibleCountInRange.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetDestructibleCountInRange(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_maxRange = args.get(0);
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        long value = 0L;
        if (criterionContext != null && criterionContext instanceof AbstractFight) {
            if (!(criterionUser instanceof BasicFighter)) {
                throw new CriteriaExecutionException("On essaie de compter les \u00e9l\u00e9ments interactifs \u00e0 port\u00e9e d'autre chose qu'un BasicFighter");
            }
            final AbstractFight<?> fight = (AbstractFight<?>)criterionContext;
            final BasicFighter user = (BasicFighter)criterionUser;
            final Iterator<EffectUser> it = fight.getAdditionalTargets();
            while (it.hasNext()) {
                final EffectUser effectUser = it.next();
                if (effectUser != user && effectUser != null) {
                    if (effectUser.getEffectUserType() != 10) {
                        continue;
                    }
                    final int distanceBetweenUserAndEffectUser = DistanceUtils.getIntersectionDistance(effectUser, user);
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
        return WakfuCriterionIds.GETDESTRUCTIBLECOUNTINRANGE;
    }
    
    static {
        GetDestructibleCountInRange.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        GetDestructibleCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER };
        GetDestructibleCountInRange.signatures.add(sig);
    }
}
