package com.ankamagames.wakfu.common.game.shortcut;

public enum ShortCutType
{
    SPELL_LEVEL((byte)0, 0), 
    USABLE_REFERENCE_ITEM((byte)1, 1), 
    ITEM((byte)2, 2), 
    EQUIPMENT_SLOT((byte)3, 3), 
    EMOTE((byte)5, 5);
    
    private byte m_id;
    private int m_gfxId;
    private ShortcutExecution m_execution;
    
    private ShortCutType(final byte id, final int gfxId) {
        this.m_id = id;
        this.m_gfxId = gfxId;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public ShortcutExecution getExecution() {
        return this.m_execution;
    }
    
    public void setExecution(final ShortcutExecution execution) {
        this.m_execution = execution;
    }
    
    public static ShortCutType getFromId(final byte id) {
        for (final ShortCutType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}
