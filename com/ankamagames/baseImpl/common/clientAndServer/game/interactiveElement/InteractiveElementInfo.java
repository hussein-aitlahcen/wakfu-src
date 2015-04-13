package com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement;

public class InteractiveElementInfo
{
    private final long m_id;
    public final short m_type;
    public final byte[] m_data;
    
    public InteractiveElementInfo(final long id, final short type, final byte[] data) {
        super();
        this.m_id = id;
        this.m_type = type;
        this.m_data = data;
    }
    
    public long getId() {
        return this.m_id;
    }
}
