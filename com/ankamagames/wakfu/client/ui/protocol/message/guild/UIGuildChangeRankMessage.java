package com.ankamagames.wakfu.client.ui.protocol.message.guild;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UIGuildChangeRankMessage extends UIMessage
{
    private long m_characterId;
    private long m_rankId;
    
    public UIGuildChangeRankMessage(final long characterId, final long rankId) {
        super();
        this.m_characterId = characterId;
        this.m_rankId = rankId;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public long getRankId() {
        return this.m_rankId;
    }
    
    @Override
    public int getId() {
        return 18205;
    }
}
