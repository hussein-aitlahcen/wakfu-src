package com.ankamagames.wakfu.common.game.effect.runningEffect.manager;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.effect.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;

final class RunningEffectManagerSerializer
{
    private static final Logger m_logger;
    private final TimedRunningEffectManager m_manager;
    
    RunningEffectManagerSerializer(final TimedRunningEffectManager manager) {
        super();
        this.m_manager = manager;
    }
    
    public byte[] serializeInFight(final boolean forIa) {
        final Map<RunningEffect, byte[]> serializedRunningEffect = new HashMap<RunningEffect, byte[]>();
        final Map<RunningEffect, byte[]> serializedStateRunningEffect = new HashMap<RunningEffect, byte[]>();
        final TLongHashSet stateNotSerialized = new TLongHashSet();
        this.fillSerializedStates(forIa, serializedStateRunningEffect, stateNotSerialized);
        this.fillSerializedEffects(forIa, serializedRunningEffect, stateNotSerialized);
        return this.buildData(forIa, serializedRunningEffect, serializedStateRunningEffect);
    }
    
    private byte[] buildData(final boolean forIa, final Map<RunningEffect, byte[]> serializedRunningEffect, final Map<RunningEffect, byte[]> serializedStateRunningEffect) {
        final ByteArray ba = new ByteArray();
        final int effectsSize = serializedStateRunningEffect.size() + serializedRunningEffect.size();
        ba.putShort((short)effectsSize);
        this.putSerializedEffect(serializedStateRunningEffect, ba);
        this.putSerializedEffect(serializedRunningEffect, ba);
        return this.checkSizeAndReturnData(forIa, serializedRunningEffect, serializedStateRunningEffect, ba);
    }
    
    private void fillSerializedEffects(final boolean forIa, final Map<RunningEffect, byte[]> serializedRunningEffect, final TLongHashSet stateNotSerialized) {
        for (final Object effect : this.m_manager) {
            final WakfuRunningEffect re = (WakfuRunningEffect)effect;
            final boolean isStateEffect = re.getId() == RunningEffectConstants.RUNNING_STATE.getId();
            if (isStateEffect) {
                continue;
            }
            if (!this.shouldSerializeEffect(forIa, re)) {
                continue;
            }
            final WakfuEffectContainer effectContainer = ((RunningEffect<FX, WakfuEffectContainer>)re).getEffectContainer();
            if (containerIsNotSerializedState(stateNotSerialized, effectContainer)) {
                continue;
            }
            final byte[] datas = re.serialize();
            serializedRunningEffect.put(re, datas);
        }
    }
    
    private void fillSerializedStates(final boolean forIa, final Map<RunningEffect, byte[]> serializedStateRunningEffect, final TLongHashSet stateNotSerialized) {
        for (final Object effect : this.m_manager) {
            final WakfuRunningEffect re = (WakfuRunningEffect)effect;
            final boolean isStateEffect = re.getId() == RunningEffectConstants.RUNNING_STATE.getId();
            if (!isStateEffect) {
                continue;
            }
            if (!this.shouldSerializeEffect(forIa, re)) {
                stateNotSerialized.add(((StateRunningEffect)re).getState().getStateBaseId());
            }
            else {
                final byte[] datas = re.serialize();
                serializedStateRunningEffect.put(re, datas);
            }
        }
    }
    
    private byte[] checkSizeAndReturnData(final boolean forIa, final Map<RunningEffect, byte[]> serializedRunningEffect, final Map<RunningEffect, byte[]> serializedStateRunningEffect, final ByteArray ba) {
        final int size = ba.size();
        final boolean sizeOk = size >= 0 && size <= 32767;
        if (!sizeOk) {
            this.logTooBigData(forIa, serializedRunningEffect, serializedStateRunningEffect, size);
            return emptyManagerData();
        }
        return ba.toArray();
    }
    
    private static byte[] emptyManagerData() {
        final ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort((short)0);
        return buffer.array();
    }
    
    private void logTooBigData(final boolean forIa, final Map<RunningEffect, byte[]> serializedRunningEffect, final Map<RunningEffect, byte[]> serializedStateRunningEffect, final int size) {
        RunningEffectManagerSerializer.m_logger.error((Object)("Taille du REM serialise trop grande pour \u00eatre envoy\u00e9e : " + size + " nombre d'effets dans le REM : " + this.m_manager.size() + " serialise pour l'ia : " + forIa));
        logSerializedEffects(serializedStateRunningEffect, serializedRunningEffect);
    }
    
    private static void logSerializedEffects(final Map<RunningEffect, byte[]> states, final Map<RunningEffect, byte[]> effects) {
        logSerializedEffects(states);
        logSerializedEffects(effects);
    }
    
    private static void logSerializedEffects(final Map<RunningEffect, byte[]> states) {
        for (final Map.Entry<RunningEffect, byte[]> set : states.entrySet()) {
            final RunningEffect re = set.getKey();
            final byte[] data = set.getValue();
            RunningEffectManagerSerializer.m_logger.error((Object)("Effet serialise : " + re.actionAndGenericEffectIdString() + " Taille de l'effet " + data.length));
        }
    }
    
    private void putSerializedEffect(final Map<RunningEffect, byte[]> serializedRunningEffect, final ByteArray buffer) {
        for (final Map.Entry<RunningEffect, byte[]> set : serializedRunningEffect.entrySet()) {
            final RunningEffect effect = set.getKey();
            buffer.putInt(effect.getId());
            buffer.putShort((short)set.getValue().length);
            buffer.put(set.getValue());
            if (this.m_manager.hasEndTime(effect.getUniqueId())) {
                buffer.putInt((int)Math.max(0L, this.m_manager.getEndTime(effect.getUniqueId()) - System.currentTimeMillis()));
            }
            else {
                buffer.putInt(0);
            }
        }
    }
    
    private boolean shouldSerializeEffect(final boolean forIa, final WakfuRunningEffect re) {
        if (forIa) {
            return !(re instanceof NullEffect) && !re.mustBeTriggered();
        }
        if (this.m_manager.ownerIsCompanion()) {
            return re.isItemEquip() || isStateEffect(re);
        }
        if (re instanceof StateRunningEffect) {
            final StateRunningEffect stateRunningEffect = (StateRunningEffect)re;
            final State state = stateRunningEffect.getState();
            if (state.isTransmigrable()) {
                return true;
            }
        }
        return !re.isItemEquip() && !re.isAptitudeEffect() && !re.isProtectorBuff() && !re.comeFromTransmigrableState() && !re.isPassiveSpellEffect() && !re.isGuildEffect() && !re.isHavenWorldEffect() && !re.isAntiAddictionEffect();
    }
    
    private static boolean isStateEffect(final WakfuRunningEffect re) {
        if (re instanceof StateRunningEffect) {
            return true;
        }
        final WakfuEffectContainer effectContainer = ((RunningEffect<FX, WakfuEffectContainer>)re).getEffectContainer();
        return effectContainer != null && effectContainer instanceof State;
    }
    
    private static boolean containerIsNotSerializedState(final TLongHash stateNotSerialized, final EffectContainer effectContainer) {
        return effectContainer != null && effectContainer.getContainerType() == 1 && stateNotSerialized.contains(((State)effectContainer).getStateBaseId());
    }
    
    public byte[] serializeFull() {
        final Map<RunningEffect, byte[]> serializedRunningEffect = new HashMap<RunningEffect, byte[]>();
        final Map<RunningEffect, byte[]> serializedStateRunningEffect = new HashMap<RunningEffect, byte[]>();
        this.fillSerializedFull(serializedRunningEffect, serializedStateRunningEffect);
        return this.buildData(false, serializedRunningEffect, serializedStateRunningEffect);
    }
    
    private void fillSerializedFull(final Map<RunningEffect, byte[]> serializedRunningEffect, final Map<RunningEffect, byte[]> serializedStateRunningEffect) {
        for (final Object effect : this.m_manager) {
            final WakfuRunningEffect re = (WakfuRunningEffect)effect;
            final boolean isStateEffect = re.getId() == RunningEffectConstants.RUNNING_STATE.getId();
            final byte[] data = re.serialize();
            if (isStateEffect) {
                serializedStateRunningEffect.put(re, data);
            }
            else {
                serializedRunningEffect.put(re, data);
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)RunningEffectManagerSerializer.class);
    }
}
