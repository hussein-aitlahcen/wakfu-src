package com.ankamagames.wakfu.client.ui.protocol.message.dimensionalBag;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;

public class UIDimensionalBagChangeRoomPermissionMessage extends UIMessage
{
    private RoomView m_roomView;
    
    public RoomView getRoomView() {
        return this.m_roomView;
    }
    
    public void setRoomView(final RoomView roomView) {
        this.m_roomView = roomView;
    }
}
