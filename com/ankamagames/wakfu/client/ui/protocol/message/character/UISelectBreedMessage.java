package com.ankamagames.wakfu.client.ui.protocol.message.character;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class UISelectBreedMessage extends UIPlayerInfoMessage
{
    private static final Logger m_logger;
    private CharacterCreationDefinition m_definition;
    
    public CharacterCreationDefinition getDefinition() {
        return this.m_definition;
    }
    
    public void setDefinition(final CharacterCreationDefinition definition) {
        this.m_definition = definition;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UISelectBreedMessage.class);
    }
}
