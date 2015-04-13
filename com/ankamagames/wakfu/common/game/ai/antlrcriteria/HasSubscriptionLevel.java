package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import gnu.trove.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import java.util.*;

public final class HasSubscriptionLevel extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private final boolean m_useTarget;
    private final TIntHashSet m_subscriptionLevels;
    
    public HasSubscriptionLevel(final ArrayList<ParserObject> args) {
        super();
        this.m_subscriptionLevels = new TIntHashSet();
        this.checkType(args);
        this.m_useTarget = args.get(0).getValue().equalsIgnoreCase("target");
        for (int i = 1, n = args.size(); i < n; ++i) {
            this.m_subscriptionLevels.add((int)args.get(i).getLongValue(null, null, null, null));
        }
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return HasSubscriptionLevel.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return -1;
        }
        if (!(target instanceof AccountInformationHolder)) {
            return -1;
        }
        final AccountInformationHolder player = (AccountInformationHolder)target;
        final WakfuAccountInformationHandler accountInformationHandler = (WakfuAccountInformationHandler)player.getAccountInformationHandler();
        final SubscriptionLevel subscriptionLevel = accountInformationHandler.getActiveSubscriptionLevel();
        if (subscriptionLevel == null) {
            return -1;
        }
        return this.m_subscriptionLevels.contains(subscriptionLevel.m_id) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_SUBSCRIPTION_LEVEL;
    }
    
    public TIntHashSet getSubscriptionLevels() {
        return this.m_subscriptionLevels;
    }
    
    @Override
    public String toString() {
        return "HasSubscriptionLevel{m_useTarget=" + this.m_useTarget + ", m_subscriptionLevels=" + this.m_subscriptionLevels + '}';
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING, ParserType.NUMBERLIST });
    }
}
