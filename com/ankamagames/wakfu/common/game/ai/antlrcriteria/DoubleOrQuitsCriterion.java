package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class DoubleOrQuitsCriterion extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_useTarget;
    
    public DoubleOrQuitsCriterion(final ArrayList<ParserObject> args) {
        super();
        this.m_useTarget = false;
        this.checkType(args);
        this.m_useTarget = false;
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return DoubleOrQuitsCriterion.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo target = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        final RunningEffect doubleOrQuitsEffect = target.getEffectWithActionId(RunningEffectConstants.DOUBLE_OR_QUITS.getId());
        if (doubleOrQuitsEffect == null) {
            return -1;
        }
        final EffectUser doubleOrQuitsTarget = doubleOrQuitsEffect.getTarget();
        if (doubleOrQuitsTarget == null) {
            return -1;
        }
        if (doubleOrQuitsTarget instanceof AbstractFakeFighterEffectArea && ((AbstractFakeFighterEffectArea)doubleOrQuitsTarget).getUserDefinedId() == 3) {
            return -1;
        }
        final boolean isUndead = doubleOrQuitsTarget.isActiveProperty(FightPropertyType.UNDEAD);
        if (isUndead) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.DOUBLE_OR_QUITS_CRITERION;
    }
    
    static {
        (DoubleOrQuitsCriterion.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
