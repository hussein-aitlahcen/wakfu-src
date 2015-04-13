package com.ankamagames.wakfu.common.game.pvp.filter;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public abstract class NationPvpLadderFilterParam
{
    private int m_pageNum;
    private int m_pageSize;
    
    protected NationPvpLadderFilterParam() {
        super();
    }
    
    protected NationPvpLadderFilterParam(final int pageNum, final int pageSize) {
        super();
        this.m_pageNum = pageNum;
        this.m_pageSize = pageSize;
    }
    
    public int getPageNum() {
        return this.m_pageNum;
    }
    
    public void setPageNum(final int pageNum) {
        this.m_pageNum = pageNum;
    }
    
    public int getPageSize() {
        return this.m_pageSize;
    }
    
    public void setPageSize(final int pageSize) {
        this.m_pageSize = pageSize;
    }
    
    void serialize(final ByteArray bb) {
        bb.putInt(this.m_pageNum);
        bb.putInt(this.m_pageSize);
    }
    
    void unserialize(final ByteBuffer bb) {
        this.m_pageNum = bb.getInt();
        this.m_pageSize = bb.getInt();
    }
    
    public abstract FilterParamType getType();
}
