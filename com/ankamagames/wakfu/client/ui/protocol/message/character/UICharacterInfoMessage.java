package com.ankamagames.wakfu.client.ui.protocol.message.character;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class UICharacterInfoMessage extends UIMessage
{
    private CharacterInfo m_characterInfo;
    
    public CharacterInfo getCharacterInfo() {
        return this.m_characterInfo;
    }
    
    public void setCharacterInfo(final CharacterInfo characterInfo) {
        this.m_characterInfo = characterInfo;
    }
}
