package com.ankamagames.wakfu.common.game.pvp.filter;

import java.nio.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class EntriesByBreed extends NationPvpLadderFilterParam
{
    private short m_breedId;
    
    public EntriesByBreed(final ByteBuffer bb) {
        super();
        this.unserialize(bb);
    }
    
    public EntriesByBreed(final int pageNum, final int pageSize, final short breedId) {
        super(pageNum, pageSize);
        this.m_breedId = breedId;
    }
    
    public short getBreedId() {
        return this.m_breedId;
    }
    
    @Override
    void serialize(final ByteArray bb) {
        super.serialize(bb);
        bb.putShort(this.m_breedId);
    }
    
    @Override
    final void unserialize(final ByteBuffer bb) {
        super.unserialize(bb);
        this.m_breedId = bb.getShort();
    }
    
    @Override
    public FilterParamType getType() {
        return FilterParamType.BREED;
    }
    
    @Override
    public String toString() {
        return "EntriesByBreed{m_breedId=" + this.m_breedId + '}';
    }
}
