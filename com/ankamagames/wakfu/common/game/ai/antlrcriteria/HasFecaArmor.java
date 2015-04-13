package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;

public final class HasFecaArmor extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_useTarget;
    
    public HasFecaArmor(final ArrayList<ParserObject> args) {
        super();
        this.m_useTarget = false;
        this.checkType(args);
        this.m_useTarget = args.get(0).getValue().equalsIgnoreCase("target");
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasFecaArmor.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo target = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return -1;
        }
        final TimedRunningEffectManager rem = target.getRunningEffectManager();
        for (final RunningEffect re : rem) {
            if (!(re instanceof StateRunningEffect)) {
                continue;
            }
            final State state = ((StateRunningEffect)re).getState();
            if (state == null) {
                continue;
            }
            if (state.isFecaArmor()) {
                return 0;
            }
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_FECA_ARMOR;
    }
    
    static {
        (HasFecaArmor.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING });
    }
}
