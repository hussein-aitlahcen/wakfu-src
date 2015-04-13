package com.ankamagames.wakfu.common.game.spell;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class StateBuilder<S extends State>
{
    private static final Logger m_logger;
    protected final S m_state;
    
    public StateBuilder(final S state) {
        super();
        this.m_state = state;
    }
    
    public void setMaxlevel(final short maxlevel) {
        this.m_state.m_maxlevel = maxlevel;
    }
    
    public void setStateBaseId(final short stateBaseId) {
        this.m_state.m_stateBaseId = stateBaseId;
    }
    
    public void setDurationTurnTable(final short durationTurnTable) {
        this.m_state.m_durationTurnTable = durationTurnTable;
    }
    
    public void setDurationTurnTableIncrement(final float durationTurnTableIncrement) {
        this.m_state.m_durationTurnTableIncrement = durationTurnTableIncrement;
    }
    
    public void setDurationInFullTurns(final boolean durationInFullTurns) {
        this.m_state.m_isDurationInFullTurns = durationInFullTurns;
    }
    
    public void setEndsAtEndOfTurn(final boolean endsAtEndOfTurn) {
        this.m_state.m_endsAtEndOfTurn = endsAtEndOfTurn;
    }
    
    public void setDurationMs(final int durationMs) {
        this.m_state.m_durationMs = durationMs;
    }
    
    public void setDurationMsIncrement(final int durationMsIncrement) {
        this.m_state.m_durationMsIncrement = durationMsIncrement;
    }
    
    public void setTransmigrable(final boolean transmigrable) {
        this.m_state.m_isTransmigrable = transmigrable;
    }
    
    public void setInTurnInFight(final boolean inTurnInFight) {
        this.m_state.m_inTurnInFight = inTurnInFight;
    }
    
    public void setReplacable(final boolean replacable) {
        this.m_state.m_replacable = replacable;
    }
    
    public void setCumulable(final boolean cumulable) {
        this.m_state.m_isCumulable = cumulable;
    }
    
    public void setDurationIsInCasterTurn(final boolean durationIsInCasterTurn) {
        this.m_state.m_durationIsInCasterTurn = durationIsInCasterTurn;
    }
    
    public void setStateImmunities(final int[] stateImmunities) {
        if (stateImmunities == null) {
            this.m_state.m_stateImmunities = new TIntHashSet();
        }
        else {
            this.m_state.m_stateImmunities = new TIntHashSet(stateImmunities);
        }
    }
    
    public void setApplyCriterions(final String applyCriterions) {
        try {
            this.m_state.m_applyCriterions = CriteriaCompiler.compileBoolean(applyCriterions);
        }
        catch (Exception e) {
            StateBuilder.m_logger.error((Object)("Erreur lors de la compilation des crit\u00e8res de l'\u00e9tat " + this.m_state.getStateBaseId()), (Throwable)e);
        }
    }
    
    public void setDecursable(final boolean decursable) {
        this.m_state.m_decursable = decursable;
    }
    
    public void setStateType(final byte stateType) {
        this.m_state.m_stateType = stateType;
    }
    
    public void setStateShouldBeSaved(final boolean stateShouldBeSaved) {
        this.m_state.m_stateShouldBeSaved = stateShouldBeSaved;
    }
    
    public void setLevel(final short level) {
        this.m_state.m_level = level;
    }
    
    public void setUniqueId(final int uniqueId) {
        this.m_state.m_uniqueId = uniqueId;
    }
    
    public void setStatePowerType(final byte statePowerType) {
        this.m_state.m_statePowerType = StatePowerType.getFromId(statePowerType);
    }
    
    public void setReapplyEvenAtMaxLevel(final boolean reapply) {
        this.m_state.m_reapplyEvenAtMaxLevel = reapply;
    }
    
    public void setDurationInRealTime(final boolean durationInRealTime) {
        this.m_state.m_durationInRealTime = durationInRealTime;
    }
    
    public void setEndTriggers(final int[] endTrigger) {
        final BitSet bitset = new BitSet();
        for (final int state_end_trigger : endTrigger) {
            bitset.set(state_end_trigger);
        }
        this.m_state.addEndTriggers(bitset);
    }
    
    public void setEffects(final GrowingArray<WakfuEffect> effects) {
        this.m_state.m_effects = effects;
    }
    
    public void setHMIActions(final String hmiActions) {
        this.m_state.m_HMIActions = new ArrayList<HMIAction>();
        if (hmiActions.trim().length() == 0) {
            return;
        }
        final String[] arr$;
        final String[] hmis = arr$ = StringUtils.split(hmiActions, '~');
        for (final String hmi : arr$) {
            if (!addHMI(hmi, this.m_state.m_HMIActions)) {
                StateBuilder.m_logger.error((Object)("Impossible d'ajouter \u00e0 l'\u00e9tat " + this.m_state.getStateBaseId() + " l'HMIAction :" + hmi));
            }
        }
    }
    
    private static boolean addHMI(final String hmi, final Collection<HMIAction> hmiActions) {
        final String[] params = hmi.split("\\|", -1);
        if (params.length % 2 != 0) {
            StateBuilder.m_logger.error((Object)("HMI error : Nombre de param\u00e8tres d\u00e9cod\u00e9s: " + params.length + " Attendu: pair "));
            return false;
        }
        Byte hmiType = 0;
        String hmiData = "";
        boolean hmiBroadcast = false;
        for (int i = 0; i < params.length; i += 2) {
            final String paramName = params[i];
            final String paramValue = params[i + 1];
            if (paramName.equals("type")) {
                hmiType = Byte.parseByte(paramValue);
            }
            else if (paramName.equals("data")) {
                hmiData = paramValue;
            }
            else if (paramName.equals("broadcast")) {
                hmiBroadcast = Boolean.parseBoolean(paramValue);
            }
        }
        final HMIAction action = HMIActionManager.getInstance().registerNewAction(hmiType, hmiData, hmiBroadcast);
        if (action != null) {
            hmiActions.add(action);
            return true;
        }
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)StateBuilder.class);
    }
}
