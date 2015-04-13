package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.challenge.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class IsChallengeUser extends FunctionCriterion
{
    private TIntArrayList m_challengeId;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsChallengeUser.signatures;
    }
    
    public TIntArrayList getChallengeId() {
        return this.m_challengeId;
    }
    
    public IsChallengeUser(final ArrayList<ParserObject> args) {
        super();
        this.m_challengeId = new TIntArrayList();
        this.checkType(args);
        for (final ParserObject arg : args) {
            final int challenge = (int)((NumericalValue)arg).getLongValue(null, null, null, null);
            this.m_challengeId.add(challenge);
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionUser instanceof ChallengeUser)) {
            throw new CriteriaExecutionException("On essaie de r\u00e9cup\u00e9rer la pr\u00e9sence d'un challenge sur une cible invalide");
        }
        final ChallengeUser challengeUser = (ChallengeUser)criterionUser;
        for (final int challengeId : this.m_challengeId.toNativeArray()) {
            if (!challengeUser.hasChallenge(challengeId)) {
                return -1;
            }
        }
        return 0;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.ISCHALLENGEUSER;
    }
    
    static {
        IsChallengeUser.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBERLIST };
        IsChallengeUser.signatures.add(sig);
    }
}
