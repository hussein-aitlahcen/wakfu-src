package com.ankamagames.wakfu.client.core.game.background;

public class PageData
{
    private final int m_id;
    private final short m_index;
    private final short m_templateLayoutId;
    private final int m_gfxId;
    
    public PageData(final int id, final short index, final short templateLayoutId, final int gfxId) {
        super();
        this.m_id = id;
        this.m_index = index;
        this.m_templateLayoutId = templateLayoutId;
        this.m_gfxId = gfxId;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public short getIndex() {
        return this.m_index;
    }
    
    public short getTemplateLayoutId() {
        return this.m_templateLayoutId;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
}
