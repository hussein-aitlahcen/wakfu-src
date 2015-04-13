package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class NationGovernmentRevokeResultMessage extends InputOnlyProxyMessage
{
    private String m_characterName;
    private NationRank m_rank;
    private int m_resultCode;
    
    public String getCharacterName() {
        return this.m_characterName;
    }
    
    public NationRank getRank() {
        return this.m_rank;
    }
    
    public int getResultCode() {
        return this.m_resultCode;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_rank = NationRank.getById(buffer.getLong());
        this.m_resultCode = buffer.getInt();
        final byte[] dName = new byte[buffer.remaining()];
        buffer.get(dName);
        this.m_characterName = StringUtils.fromUTF8(dName);
        return true;
    }
    
    @Override
    public int getId() {
        return 20026;
    }
}
