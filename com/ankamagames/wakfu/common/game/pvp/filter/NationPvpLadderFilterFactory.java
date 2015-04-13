package com.ankamagames.wakfu.common.game.pvp.filter;

import java.nio.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class NationPvpLadderFilterFactory
{
    @Nullable
    public static NationPvpLadderFilterParam unserialize(final ByteBuffer bb) {
        final FilterParamType type = FilterParamType.getFromId(bb.get());
        if (type == null) {
            return null;
        }
        switch (type) {
            case BREED: {
                return new EntriesByBreed(bb);
            }
            case GUILD: {
                return new EntriesByGuild(bb);
            }
            default: {
                return new AllEntries(bb);
            }
        }
    }
    
    public static void serialize(final NationPvpLadderFilterParam param, final ByteArray ba) {
        ba.put(param.getType().getId());
        param.serialize(ba);
    }
}
