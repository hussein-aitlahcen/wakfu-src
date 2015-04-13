package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetTotalLeadershipNeededForCurrentCreature extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private boolean target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetTotalLeadershipNeededForCurrentCreature.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetTotalLeadershipNeededForCurrentCreature(final ArrayList<ParserObject> args) {
        super();
        this.target = true;
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.target = false;
        }
        if (paramType == 1) {
            this.target = args.get(0).getValue().equalsIgnoreCase("target");
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        long value = 0L;
        if (criterionContext == null) {
            throw new CriteriaExecutionException("Pas de contexte...");
        }
        final boolean isFight = criterionContext instanceof AbstractFight;
        final boolean isFightContext = criterionContext instanceof WakfuFightEffectContext;
        if (!isFight && !isFightContext) {
            throw new CriteriaExecutionException("On essaie de compter les invocations en dehors d'un combat...");
        }
        if (!(criterionUser instanceof BasicCharacterInfo)) {
            throw new CriteriaExecutionException("On essaie de compter les invocations d'un caster qui n'est pas un perso");
        }
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1L;
        }
        value = targetCharacter.getTotalLeadershipNeededForCurrentCreature();
        return super.getSign() * value;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.TOTAL_LEADERSHIP_NEEDED_FOR_CURRENT_CREATURE;
    }
    
    static {
        GetTotalLeadershipNeededForCurrentCreature.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        GetTotalLeadershipNeededForCurrentCreature.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING };
        GetTotalLeadershipNeededForCurrentCreature.signatures.add(sig);
    }
}
