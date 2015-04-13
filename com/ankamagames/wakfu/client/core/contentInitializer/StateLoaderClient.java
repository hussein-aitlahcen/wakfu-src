package com.ankamagames.wakfu.client.core.contentInitializer;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.spell.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.effect.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public class StateLoaderClient implements StateLoader
{
    private static final Logger m_logger;
    private final BinaryLoader<StateBinaryData> m_dataLoader;
    
    public StateLoaderClient() {
        this(new BinaryLoaderFromFile<StateBinaryData>(new StateBinaryData()));
    }
    
    public StateLoaderClient(final BinaryLoader<StateBinaryData> dataLoader) {
        super();
        this.m_dataLoader = dataLoader;
    }
    
    @Nullable
    @Override
    public State loadState(final int stateId) {
        if (stateId <= 0) {
            return null;
        }
        final StateClient state = this.createState();
        final StateBinaryData fromId = this.m_dataLoader.createFromId(stateId);
        if (fromId == null) {
            StateLoaderClient.m_logger.error((Object)("Impossible de charger l'\u00e9tat " + stateId));
            return null;
        }
        fromBinary(state, fromId);
        return state;
    }
    
    protected StateClient createState() {
        return new StateClient();
    }
    
    private static void fromBinary(final StateClient state, final StateBinaryData data) {
        final StateClientBuilder builder = new StateClientBuilder(state);
        builder.setGfxId(data.getGfxId());
        builder.setDisplayCasterName(data.isDisplayCasterName());
        builder.setShowInTimeline(data.isTimelineVisible());
        builder.setStateBaseId((short)data.getId());
        builder.setMaxlevel(data.getMaxLevel());
        builder.setDurationTurnTable(data.getDurationTurnTable());
        builder.setDurationTurnTableIncrement(data.getDurationTurnTableInc());
        builder.setDurationInFullTurns(data.isDurationInFullTurns());
        builder.setEndsAtEndOfTurn(data.isEndsAtEndOfTurn());
        builder.setDurationMs(data.getDurationMs());
        builder.setDurationMsIncrement(data.getDurationMsInc());
        builder.setTransmigrable(data.isTransmigrable());
        builder.setInTurnInFight(data.isInTurnInFight());
        builder.setReplacable(data.isReplacable());
        builder.setCumulable(data.isCumulable());
        builder.setStateImmunities(data.getStateImmunities());
        builder.setApplyCriterions(data.getApplyCriterion());
        builder.setEndTriggers(data.getEndTrigger());
        builder.setHMIActions(data.getHmiActions());
        builder.setDurationIsInCasterTurn(data.isDurationInCasterTurn());
        builder.setStateShouldBeSaved(data.isStateShouldBeSaved());
        builder.setDecursable(data.isDecursable());
        builder.setStateType(data.getStateType());
        builder.setStatePowerType(data.getStatePowerType());
        builder.setReapplyEvenAtMaxLevel(data.isReapplyEvenAtMaxLevel());
        builder.setDurationInRealTime(data.isDurationInRealTime());
        builder.setHMIActions(data.getHmiActions());
        loadAndAddEffects(state, data);
    }
    
    private static void loadAndAddEffects(final StateClient state, final StateBinaryData data) {
        final int[] effectIds = data.getEffectIds();
        if (effectIds == null || effectIds.length == 0) {
            return;
        }
        for (final int effectId : effectIds) {
            final WakfuEffect effect = EffectManager.getInstance().loadAndAddEffect(effectId);
            if (effect != null) {
                state.addEffect(effect);
            }
            else {
                StateLoaderClient.m_logger.error((Object)("Probl\u00e8me de chargmeent de State " + data.getId()));
            }
        }
    }
    
    public static TIntObjectHashMap<State> getFullList() {
        final TIntObjectHashMap<State> map = new TIntObjectHashMap<State>();
        try {
            BinaryDocumentManager.getInstance().foreach(new StateBinaryData(), new LoadProcedure<StateBinaryData>() {
                @Override
                public void load(final StateBinaryData data) {
                    final StateClient state = new StateClient();
                    fromBinary(state, data);
                    map.put(state.getStateBaseId(), state);
                }
            });
        }
        catch (Exception e) {
            StateLoaderClient.m_logger.error((Object)"", (Throwable)e);
        }
        return map;
    }
    
    static {
        m_logger = Logger.getLogger((Class)StateLoaderClient.class);
    }
}
