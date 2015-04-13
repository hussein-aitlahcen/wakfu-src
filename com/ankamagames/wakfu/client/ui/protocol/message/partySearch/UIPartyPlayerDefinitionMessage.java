package com.ankamagames.wakfu.client.ui.protocol.message.partySearch;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;

public class UIPartyPlayerDefinitionMessage extends UIMessage
{
    private final PartyPlayerDefinition m_definition;
    
    public UIPartyPlayerDefinitionMessage(final PartyPlayerDefinition definition) {
        super();
        this.m_definition = definition;
    }
    
    public PartyPlayerDefinition getDefinition() {
        return this.m_definition;
    }
    
    @Override
    public int getId() {
        return 19458;
    }
}
