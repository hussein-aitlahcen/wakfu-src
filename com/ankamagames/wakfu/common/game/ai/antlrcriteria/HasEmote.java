package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.emote.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class HasEmote extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_emoteId;
    
    public HasEmote() {
        super();
    }
    
    public HasEmote(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_emoteId = args.get(0);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasEmote.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_EMOTE;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (user == null || !(user instanceof EmoteUser)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        final int emoteId = (int)this.m_emoteId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        return ((EmoteUser)user).getEmoteHandler().knowEmote(emoteId) ? 0 : -1;
    }
    
    static {
        HasEmote.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        HasEmote.signatures.add(sig);
    }
}
