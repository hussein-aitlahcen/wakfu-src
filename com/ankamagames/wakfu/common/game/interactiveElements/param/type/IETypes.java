package com.ankamagames.wakfu.common.game.interactiveElements.param.type;

public enum IETypes
{
    CRAFT_TABLE((byte)0), 
    DECORATION((byte)1), 
    DIMENSIONAL_BAG_BACKGROUND_DISPLAY((byte)2), 
    STREET_LIGHT((byte)3), 
    BOARD((byte)4), 
    BACKGROUND_DISPLAY((byte)5), 
    DESTRUCTIBLE((byte)6), 
    LOOT_CHEST((byte)7), 
    COLLECT_MACHINE((byte)8), 
    TELEPORTER((byte)9), 
    STOOL((byte)10), 
    AUDIO_MARKER((byte)11), 
    MARKET_BOARD((byte)12), 
    STORAGE_BOX((byte)13), 
    GENERIC((byte)14), 
    GENERIC_ACTIVABLE((byte)15), 
    EXCHANGE_MACHINE((byte)16), 
    RECYCLE_MACHINE((byte)17), 
    DIALOG_MACHINE((byte)18), 
    DUNGEON_DIPLAYER((byte)19), 
    REWARD_DISPLAYER((byte)20), 
    HAVEN_WORLD_BOARD((byte)21), 
    EQUIPABLE_DUMMY((byte)22), 
    BOOKCASE((byte)23), 
    HAVEN_WORLD_BUILDING_BOARD((byte)24), 
    KROSMOZ_GAME_BOARD((byte)25), 
    KROSMOZ_GAME_COLLECTION((byte)26), 
    HAVEN_WORLD_ENTRY((byte)27), 
    DOOR((byte)28);
    
    private final byte m_id;
    
    private IETypes(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static IETypes getFromId(final byte id) {
        for (final IETypes type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
}
