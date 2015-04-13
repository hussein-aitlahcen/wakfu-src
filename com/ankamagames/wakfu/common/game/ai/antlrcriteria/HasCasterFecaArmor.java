package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;

public final class HasCasterFecaArmor extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private String m_targetType;
    
    public HasCasterFecaArmor(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_targetType = args.get(0).getValue();
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasCasterFecaArmor.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser caster = CriteriaUtils.getTarget("caster", criterionUser, criterionTarget, criterionContext, criterionContent);
        final CriterionUser target = CriteriaUtils.getTarget(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (caster == null) {
            return -1;
        }
        if (!(target instanceof BasicCharacterInfo)) {
            return -1;
        }
        final TimedRunningEffectManager rem = ((BasicCharacterInfo)target).getRunningEffectManager();
        for (final RunningEffect re : rem) {
            if (!(re instanceof StateRunningEffect)) {
                continue;
            }
            if (re.getCaster() == null) {
                continue;
            }
            if (re.getCaster().getId() != caster.getId()) {
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
        (HasCasterFecaArmor.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING });
    }
}
