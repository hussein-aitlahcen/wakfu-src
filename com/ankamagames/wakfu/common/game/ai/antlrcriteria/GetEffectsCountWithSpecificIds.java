package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public final class GetEffectsCountWithSpecificIds extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private final boolean m_useTarget;
    private final TIntArrayList m_ids;
    
    public GetEffectsCountWithSpecificIds(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_useTarget = args.get(0).getValue().equalsIgnoreCase("target");
        this.m_ids = new TIntArrayList();
        for (int i = 1, n = args.size(); i < n; ++i) {
            this.m_ids.add((int)args.get(i).getLongValue(null, null, null, null));
        }
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetEffectsCountWithSpecificIds.SIGNATURES;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return -1L;
        }
        final RunningEffectManager rem = target.getRunningEffectManager();
        if (rem == null) {
            return -1L;
        }
        long res = 0L;
        for (int i = 0, n = this.m_ids.size(); i < n; ++i) {
            final int effectId = this.m_ids.get(i);
            if (rem.containsWithSpecificId(effectId)) {
                ++res;
            }
        }
        return res;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_EFFECTS_COUNT_WITH_SPECIFIC_IDS;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING, ParserType.NUMBERLIST });
    }
}
