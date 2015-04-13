package com.ankamagames.wakfu.common.game.spell;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import gnu.trove.*;

final class SpellCastHistorySerializer
{
    static byte[] serialize(final SpellLevelCastHistory castHistory) {
        final ByteArray ba = new ByteArray();
        serializeIntIntHashMap(ba, castHistory.getLastTurnSpellCast());
        serializeIntIntHashMap(ba, castHistory.getSpellCastedThisTurnCount());
        serializeLongIntHashMap(ba, castHistory.getSpellCastedThisTurnOnTargetCount());
        return ba.toArray();
    }
    
    static void unserialize(final ByteBuffer buf, final SpellLevelCastHistory castHistory) {
        unserializeLastTurnSpellCast(buf, castHistory);
        unserializeSpellCastedThisTurn(buf, castHistory);
        unserializeSpellCastedThisTurnOnTarget(buf, castHistory);
    }
    
    private static void unserializeLongIntHashMap(final ByteBuffer buf, final int size, final TLongIntHashMap spellsCastedThisTurnOnTarget) {
        spellsCastedThisTurnOnTarget.clear();
        for (int i = size; i > 0; --i) {
            spellsCastedThisTurnOnTarget.put(buf.getLong(), buf.getInt());
        }
    }
    
    private static void unserializeIntIntHashMap(final ByteBuffer buf, final int size, final TIntIntHashMap lastTurnSpellLevelCast) {
        lastTurnSpellLevelCast.clear();
        for (int i = size; i > 0; --i) {
            lastTurnSpellLevelCast.put(buf.getInt(), buf.getInt());
        }
    }
    
    private static void serializeIntIntHashMap(final ByteArray buf, final TIntIntHashMap lastTurnSpellLevelCast) {
        if (lastTurnSpellLevelCast == null) {
            buf.put((byte)0);
        }
        else {
            buf.put((byte)lastTurnSpellLevelCast.size());
            final TIntIntIterator intIterator = lastTurnSpellLevelCast.iterator();
            while (intIterator.hasNext()) {
                intIterator.advance();
                buf.putInt(intIterator.key());
                buf.putInt(intIterator.value());
            }
        }
    }
    
    private static void serializeLongIntHashMap(final ByteArray buf, final TLongIntHashMap spellsCastedThisTurnOnTarget) {
        if (spellsCastedThisTurnOnTarget == null) {
            buf.put((byte)0);
        }
        else {
            buf.put((byte)spellsCastedThisTurnOnTarget.size());
            final TLongIntIterator it = spellsCastedThisTurnOnTarget.iterator();
            while (it.hasNext()) {
                it.advance();
                buf.putLong(it.key());
                buf.putInt(it.value());
            }
        }
    }
    
    private static void unserializeLastTurnSpellCast(final ByteBuffer buf, final SpellLevelCastHistory castHistory) {
        final int size = buf.get() & 0xFF;
        if (size <= 0) {
            return;
        }
        castHistory.ensureSpellLevelCasted();
        final TIntIntHashMap lastTurnSpellLevelCast = castHistory.getLastTurnSpellCast();
        unserializeIntIntHashMap(buf, size, lastTurnSpellLevelCast);
    }
    
    private static void unserializeSpellCastedThisTurn(final ByteBuffer buf, final SpellLevelCastHistory castHistory) {
        final int size = buf.get() & 0xFF;
        if (size <= 0) {
            return;
        }
        castHistory.ensureSpellLevelCastedThisTurn();
        final TIntIntHashMap spellLevelsCastedThisTurn = castHistory.getSpellCastedThisTurnCount();
        unserializeIntIntHashMap(buf, size, spellLevelsCastedThisTurn);
    }
    
    private static void unserializeSpellCastedThisTurnOnTarget(final ByteBuffer buf, final SpellLevelCastHistory castHistory) {
        final int size = buf.get() & 0xFF;
        if (size <= 0) {
            return;
        }
        castHistory.ensureSpellsCastedThisTurnOnTarget();
        final TLongIntHashMap spellsCastedThisTurnOnTarget = castHistory.getSpellCastedThisTurnOnTargetCount();
        unserializeLongIntHashMap(buf, size, spellsCastedThisTurnOnTarget);
    }
}
