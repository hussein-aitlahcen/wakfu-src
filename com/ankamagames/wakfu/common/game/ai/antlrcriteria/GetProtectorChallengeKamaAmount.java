package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;

public final class GetProtectorChallengeKamaAmount extends FunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return Collections.unmodifiableList((List<? extends ParserType[]>)GetProtectorChallengeKamaAmount.SIGNATURES);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetProtectorChallengeKamaAmount(final List<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionUser instanceof ChallengeKamaPoolProvider) {
            final Wallet challengeKamaPool = ((ChallengeKamaPoolProvider)criterionUser).getChallengeKamaPool();
            return challengeKamaPool.getAmountOfCash();
        }
        return -1L;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_PROTECTOR_CHALLENGE_KAMA_AMOUNT;
    }
    
    static {
        SIGNATURES = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        GetProtectorChallengeKamaAmount.SIGNATURES.add(sig);
    }
}
