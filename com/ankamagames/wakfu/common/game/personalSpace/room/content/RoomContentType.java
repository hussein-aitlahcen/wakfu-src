package com.ankamagames.wakfu.common.game.personalSpace.room.content;

public enum RoomContentType
{
    DECORATION(127), 
    BACKGROUND_DISPLAY(127), 
    MERCHANT_DISPLAY(4), 
    CRAFT_TABLE(127);
    
    public final byte maxContentPerGem;
    
    private RoomContentType(final int maxContentPerGem) {
        this.maxContentPerGem = (byte)maxContentPerGem;
    }
}
