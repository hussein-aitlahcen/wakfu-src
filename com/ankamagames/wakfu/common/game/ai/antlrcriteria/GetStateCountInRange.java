package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.game.spell.*;

public class GetStateCountInRange extends GetFightersCountInRange
{
    private static ArrayList<ParserType[]> signatures;
    private final long m_stateId;
    private final long m_stateLevel;
    private final boolean m_onlyInPlayFighters;
    protected boolean m_ownTeam;
    
    public GetStateCountInRange(final List<ParserObject> args) {
        super();
        final byte sigIdx = this.checkType(args);
        this.m_maxRange = args.get(0);
        this.m_target = args.get(1).getValue().equalsIgnoreCase("target");
        this.m_stateId = args.get(2).getLongValue(null, null, null, null);
        this.m_ownTeam = false;
        if (sigIdx == 1) {
            this.m_stateLevel = args.get(3).getLongValue(null, null, null, null);
        }
        else {
            this.m_stateLevel = -1L;
        }
        if (sigIdx == 2) {
            this.m_onlyInPlayFighters = args.get(3).isValid(null, null, null, null);
        }
        else {
            this.m_onlyInPlayFighters = true;
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetStateCountInRange.signatures;
    }
    
    @Override
    protected Collection<? extends EffectUser> getFighters(final AbstractFight<?> fight, final CriterionUser user) {
        if (fight == null) {
            return (Collection<? extends EffectUser>)Collections.emptyList();
        }
        final Collection<? extends BasicCharacterInfo> fighters = this.m_onlyInPlayFighters ? fight.getInPlayFighters() : fight.getAllFighters();
        final Collection<EffectUser> res = new ArrayList<EffectUser>();
        for (final BasicCharacterInfo fighter : fighters) {
            if (this.hasState(user, fighter.getRunningEffectManager())) {
                res.add(fighter);
            }
        }
        final Collection<BasicEffectArea> activeEffectAreas = fight.getEffectAreaManager().getActiveEffectAreas();
        for (final BasicEffectArea area : activeEffectAreas) {
            final TimedRunningEffectManager rem = (TimedRunningEffectManager)area.getRunningEffectManager();
            if (rem == null) {
                continue;
            }
            if (!this.hasState(user, rem)) {
                continue;
            }
            res.add(area);
        }
        return res;
    }
    
    private boolean hasState(final CriterionUser user, final TimedRunningEffectManager rem) {
        boolean hasState = false;
        for (final RunningEffect runningEffect : rem) {
            if (runningEffect.getId() != RunningEffectConstants.RUNNING_STATE.getId()) {
                continue;
            }
            final State state = ((StateRunningEffect)runningEffect).getState();
            final short stateBaseId = state.getStateBaseId();
            if (this.m_ownTeam) {
                final EffectUser caster = runningEffect.getCaster();
                if (!(caster instanceof FightEffectUser)) {
                    continue;
                }
                if (user == null) {
                    continue;
                }
                if (((FightEffectUser)caster).getTeamId() != user.getTeamId()) {
                    continue;
                }
            }
            if (stateBaseId != this.m_stateId) {
                continue;
            }
            if (this.m_stateLevel < 0L || state.getLevel() == this.m_stateLevel) {
                hasState = true;
                break;
            }
        }
        return hasState;
    }
    
    public long getStateId() {
        return this.m_stateId;
    }
    
    public long getStateLevel() {
        return this.m_stateLevel;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_STATE_COUNT_IN_RANGE;
    }
    
    static {
        (GetStateCountInRange.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.NUMBER, ParserType.STRING, ParserType.NUMBER });
        GetStateCountInRange.signatures.add(new ParserType[] { ParserType.NUMBER, ParserType.STRING, ParserType.NUMBER, ParserType.NUMBER });
        GetStateCountInRange.signatures.add(new ParserType[] { ParserType.NUMBER, ParserType.STRING, ParserType.NUMBER, ParserType.BOOLEAN });
    }
}
