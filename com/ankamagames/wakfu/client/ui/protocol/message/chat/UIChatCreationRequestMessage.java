package com.ankamagames.wakfu.client.ui.protocol.message.chat;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.chat.*;

public class UIChatCreationRequestMessage extends UIMessage
{
    private String m_characterName;
    private ChatViewEventListener m_listener;
    
    public UIChatCreationRequestMessage(final short id) {
        super(id);
    }
    
    public UIChatCreationRequestMessage(final short id, final String characterName) {
        super(id);
        this.m_characterName = characterName;
    }
    
    public UIChatCreationRequestMessage(final short id, final String characterName, final ChatViewEventListener listener) {
        super(id);
        this.m_characterName = characterName;
        this.m_listener = listener;
    }
    
    public String getCharacterName() {
        return this.m_characterName;
    }
    
    public void setCharacterName(final String characterName) {
        this.m_characterName = characterName;
    }
    
    public ChatViewEventListener getListener() {
        return this.m_listener;
    }
    
    public void setListener(final ChatViewEventListener listener) {
        this.m_listener = listener;
    }
}
