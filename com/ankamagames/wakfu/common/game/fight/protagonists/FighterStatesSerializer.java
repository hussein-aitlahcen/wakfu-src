package com.ankamagames.wakfu.common.game.fight.protagonists;

import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;
import java.nio.*;

public final class FighterStatesSerializer
{
    public static byte[] serialize(final TLongObjectHashMap<FighterState> fighters) {
        final ByteArray ba = new ByteArray();
        final short size = (short)fighters.size();
        ba.putShort(size);
        fighters.forEachEntry(new TLongObjectProcedure<FighterState>() {
            @Override
            public boolean execute(final long a, final FighterState b) {
                ba.putLong(a);
                final byte[] data = FighterStateSerializer.serialize(b);
                ba.putInt(data.length);
                ba.put(data);
                return true;
            }
        });
        return ba.toArray();
    }
    
    public static TLongObjectHashMap<FighterState> unserialize(final byte[] data) {
        final ByteBuffer bb = ByteBuffer.wrap(data);
        final TLongObjectHashMap<FighterState> res = new TLongObjectHashMap<FighterState>();
        final short size = bb.getShort();
        for (int i = 0; i < size; ++i) {
            final long fighterId = bb.getLong();
            final byte[] stateData = new byte[bb.getInt()];
            bb.get(stateData);
            final FighterState fighterState = FighterStateSerializer.unserialize(stateData);
            res.put(fighterId, fighterState);
        }
        return res;
    }
}
