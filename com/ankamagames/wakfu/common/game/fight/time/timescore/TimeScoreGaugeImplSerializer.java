package com.ankamagames.wakfu.common.game.fight.time.timescore;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.effect.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fight.time.buff.*;
import com.ankamagames.framework.kernel.utils.*;

final class TimeScoreGaugeImplSerializer
{
    public static byte[] serialize(final TimeScoreGaugesImpl timeScoreGauges, final int currentTurn) {
        final ByteArray ba = new ByteArray();
        serializeTimePointGap(timeScoreGauges, ba);
        serializeScoreGauges(timeScoreGauges, ba);
        serializeEffectsList(timeScoreGauges, ba);
        serializeAvailableEffects(timeScoreGauges, ba);
        return ba.toArray();
    }
    
    public static TimeScoreGaugesImpl unserialize(final byte[] data, final TimeScoreGaugesImpl res) {
        final ByteBuffer bb = ByteBuffer.wrap(data);
        unserializeTimePointGap(bb, res);
        unserializeTimeScoreGauges(bb, res);
        unserializeEffectsList(bb, res);
        unserializeAvailableEffects(bb, res);
        return res;
    }
    
    private static void serializeTimePointGap(final TimeScoreGauges timeScoreGauges, final ByteArray ba) {
        ba.putInt(timeScoreGauges.getTimePointGap());
    }
    
    private static void unserializeTimePointGap(final ByteBuffer bb, final TimeScoreGaugesImpl res) {
        res.setTimePointGap(bb.getInt());
    }
    
    private static void serializeScoreGauges(final TimeScoreGaugesImpl timeScoreGauges, final ByteArray ba) {
        ba.put((byte)timeScoreGauges.size());
        timeScoreGauges.getTimeScoreGauges().forEachEntry(new TLongIntProcedure() {
            @Override
            public boolean execute(final long a, final int b) {
                ba.putLong(a);
                ba.putInt(b);
                return true;
            }
        });
    }
    
    private static void unserializeTimeScoreGauges(final ByteBuffer bb, final TimeScoreGaugesImpl res) {
        final byte scoreGaugesSize = bb.get();
        for (int i = 0; i < scoreGaugesSize; ++i) {
            res.getTimeScoreGauges().put(bb.getLong(), bb.getInt());
        }
    }
    
    private static void serializeEffectsList(final TimeScoreGauges timeScoreGauges, final ByteArray ba) {
        final TIntObjectHashMap<TimelineBuffList<WakfuEffect>> effects = timeScoreGauges.getBuffListsByTeamId();
        ba.put((byte)effects.size());
        effects.forEachEntry(new TIntObjectProcedure<TimelineBuffList<WakfuEffect>>() {
            @Override
            public boolean execute(final int a, final TimelineBuffList<WakfuEffect> b) {
                final List<WakfuEffect> teamEffects = b.getEffects();
                ba.putInt(a);
                ba.put((byte)teamEffects.size());
                for (final WakfuEffect effect : teamEffects) {
                    ba.putInt(effect.getEffectId());
                }
                return true;
            }
        });
    }
    
    private static void unserializeEffectsList(final ByteBuffer bb, final TimeScoreGaugesImpl res) {
        final byte teamSize = bb.get();
        for (int i = 0; i < teamSize; ++i) {
            final int teamId = bb.getInt();
            final byte effectsListSize = bb.get();
            final List<WakfuEffect> effects = new ArrayList<WakfuEffect>();
            for (int j = 0; j < effectsListSize; ++j) {
                final WakfuEffect effectById = TimelineBuffListManager.INSTANCE.getEffectById(bb.getInt());
                if (effectById != null) {
                    effects.add(effectById);
                }
            }
            res.setTeamEffects((byte)teamId, effects);
        }
    }
    
    private static void serializeAvailableEffects(final TimeScoreGaugesImpl timeScoreGauges, final ByteArray ba) {
        final ByteArray availableEffects = new ByteArray();
        final Accumulator size = new Accumulator();
        timeScoreGauges.getAvailableEffectsPerTeam().forEachEntry(new TIntObjectProcedure<List<WakfuEffect>>() {
            @Override
            public boolean execute(final int a, final List<WakfuEffect> b) {
                size.inc();
                availableEffects.putInt(a);
                availableEffects.put((byte)b.size());
                for (final WakfuEffect wakfuEffect : b) {
                    availableEffects.putInt(wakfuEffect.getEffectId());
                }
                return true;
            }
        });
        ba.put((byte)size.getValue());
        ba.put(availableEffects);
    }
    
    private static void unserializeAvailableEffects(final ByteBuffer bb, final TimeScoreGaugesImpl res) {
        final byte availableEffectsSize = bb.get();
        for (int i = 0; i < availableEffectsSize; ++i) {
            final int key = bb.getInt();
            final byte effectSize = bb.get();
            final List<WakfuEffect> availableEffects = new ArrayList<WakfuEffect>();
            for (int j = 0; j < effectSize; ++j) {
                final int effectId = bb.getInt();
                final WakfuEffect effectById = TimelineBuffListManager.INSTANCE.getEffectById(effectId);
                if (effectById != null) {
                    availableEffects.add(effectById);
                }
            }
            res.getAvailableEffectsPerTeam().put(key, availableEffects);
        }
    }
}
