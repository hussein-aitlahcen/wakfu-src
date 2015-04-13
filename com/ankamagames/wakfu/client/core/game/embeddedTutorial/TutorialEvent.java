package com.ankamagames.wakfu.client.core.game.embeddedTutorial;

public enum TutorialEvent
{
    DIMENSIONNAL_ENTER((byte)1), 
    OTHER_FLEA_OPEN((byte)2), 
    INVENTORY_OPENED((byte)3), 
    DIMENSIONAL_FLEA_CLOSE((byte)4), 
    DIMENSIONNAL_FLEA_MANAGE((byte)5), 
    GROUP_OPEN((byte)6), 
    SPELLS_OPEN((byte)7), 
    REPAIR_BROKEN_ITEM((byte)8), 
    REPAIR_OPEN((byte)9), 
    CRAFT_JOBS((byte)10), 
    OPTIONS_GAME((byte)11), 
    OPTIONS_SOUND((byte)12), 
    OPTIONS_VIDEO((byte)13), 
    OPTIONS_DISPLAY((byte)14), 
    OPTIONS_COMMANDS((byte)15), 
    CRAFT_DIALOG((byte)16), 
    CRAFT_OPEN_RECIPE((byte)17), 
    SYMBIOT_OPEN((byte)18), 
    ENU_BAG_OPEN((byte)19), 
    PUPPET_OPEN((byte)20), 
    RIGHT_CLICK((byte)21);
    
    private final byte m_eventId;
    
    private TutorialEvent(final byte eventId) {
        this.m_eventId = eventId;
    }
    
    public byte getId() {
        return this.m_eventId;
    }
}
