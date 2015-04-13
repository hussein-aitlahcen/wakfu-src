package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.datas.*;

public class IsAccountSubscribed extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsAccountSubscribed.signatures;
    }
    
    public IsAccountSubscribed(final ArrayList<ParserObject> args) {
        super();
        final byte type = this.checkType(args);
        switch (type) {
            case 0: {}
            default: {
                IsAccountSubscribed.m_logger.error((Object)("Mauvais param\u00e9trage du crit\u00e8re : " + Arrays.toString(args.toArray())));
            }
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo character = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
        if (character == null) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        if (!(character instanceof AccountInformationHolder)) {
            throw new CriteriaExecutionException("La cible " + character + " du crit\u00e8re est invalide");
        }
        final WakfuAccountInformationHandler handler = (WakfuAccountInformationHandler)((AccountInformationHolder)character).getAccountInformationHandler();
        return handler.hasSubscriptionLevel(SubscriptionLevel.EU_SUBSCRIBER) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ACCOUNT_SUBSCRIBED;
    }
    
    static {
        (IsAccountSubscribed.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
