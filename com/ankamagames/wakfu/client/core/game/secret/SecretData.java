package com.ankamagames.wakfu.client.core.game.secret;

public class SecretData
{
    private final int m_id;
    private final short m_level;
    private final short m_itemId;
    
    public SecretData(final int id, final short level, final short itemId) {
        super();
        this.m_id = id;
        this.m_level = level;
        this.m_itemId = itemId;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public short getItemId() {
        return this.m_itemId;
    }
}
