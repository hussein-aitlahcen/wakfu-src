package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;

public final class GetOwnArmorCount extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private static final int NEUTRAL_ARMOR_ID = 888;
    private String m_targetType;
    
    public GetOwnArmorCount(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_targetType = args.get(0).getValue();
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetOwnArmorCount.SIGNATURES;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final CriterionUser armorOwner = CriteriaUtils.getTarget(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (armorOwner == null) {
            return 0L;
        }
        final AbstractFight<?> fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        if (fight == null) {
            return 0L;
        }
        int res = 0;
        final Collection<? extends BasicCharacterInfo> fightersInPlay = fight.getFightersInPlay();
        for (final BasicCharacterInfo fighter : fightersInPlay) {
            final TimedRunningEffectManager rem = fighter.getRunningEffectManager();
            for (final RunningEffect runningEffect : rem) {
                if (!(runningEffect instanceof StateRunningEffect)) {
                    continue;
                }
                final StateRunningEffect stateRunningEffect = (StateRunningEffect)runningEffect;
                final EffectUser caster = stateRunningEffect.getCaster();
                if (caster == null) {
                    continue;
                }
                if (caster.getId() != armorOwner.getId()) {
                    continue;
                }
                final State state = stateRunningEffect.getState();
                if (state == null) {
                    continue;
                }
                if (!state.isFecaArmor() && state.getStateBaseId() != 888) {
                    continue;
                }
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
        return WakfuCriterionIds.GET_OWN_ARMOR_COUNT;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
