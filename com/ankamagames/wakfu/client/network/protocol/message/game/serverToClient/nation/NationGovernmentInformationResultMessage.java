package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.framework.kernel.utils.*;

public class NationGovernmentInformationResultMessage extends InputOnlyProxyMessage
{
    private final ArrayList<GovernmentInfo> m_government;
    
    public NationGovernmentInformationResultMessage() {
        super();
        this.m_government = new ArrayList<GovernmentInfo>();
    }
    
    public Iterator<GovernmentInfo> governmentIterator() {
        return this.m_government.iterator();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        while (buffer.hasRemaining()) {
            final NationRank rank = NationRank.getById(buffer.getLong());
            final long characterId = buffer.getLong();
            final byte[] dName = new byte[buffer.getShort()];
            buffer.get(dName);
            final String name = StringUtils.fromUTF8(dName);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 20020;
    }
}
