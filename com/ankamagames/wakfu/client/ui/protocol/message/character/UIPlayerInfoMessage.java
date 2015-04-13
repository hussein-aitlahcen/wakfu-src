package com.ankamagames.wakfu.client.ui.protocol.message.character;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class UIPlayerInfoMessage extends UIMessage
{
    private PlayerCharacter m_playerCharacter;
    
    public PlayerCharacter getPlayerInfo() {
        return this.m_playerCharacter;
    }
    
    public void setPlayerInfo(final PlayerCharacter playerCharacter) {
        this.m_playerCharacter = playerCharacter;
    }
}
