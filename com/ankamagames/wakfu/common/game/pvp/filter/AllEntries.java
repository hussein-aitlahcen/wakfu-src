package com.ankamagames.wakfu.common.game.pvp.filter;

import java.nio.*;

public class AllEntries extends NationPvpLadderFilterParam
{
    public AllEntries(final ByteBuffer bb) {
        super();
        this.unserialize(bb);
    }
    
    public AllEntries(final int pageNum, final int pageSize) {
        super(pageNum, pageSize);
    }
    
    @Override
    public FilterParamType getType() {
        return FilterParamType.ALL;
    }
}
