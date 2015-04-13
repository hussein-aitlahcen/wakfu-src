package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class ClientNationDiplomacyInformationResult extends InputOnlyProxyMessage
{
    private final TIntObjectHashMap<byte[]> m_diplomacy;
    private final TIntIntHashMap m_numProtectors;
    
    public ClientNationDiplomacyInformationResult() {
        super();
        this.m_diplomacy = new TIntObjectHashMap<byte[]>();
        this.m_numProtectors = new TIntIntHashMap();
    }
    
    public TIntObjectIterator<byte[]> diplomacyDataIterator() {
        return this.m_diplomacy.iterator();
    }
    
    public TIntIntIterator numProtectorIterator() {
        return this.m_numProtectors.iterator();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        while (buffer.hasRemaining()) {
            final int nationId = buffer.getInt();
            final byte[] data = new byte[buffer.getInt()];
            buffer.get(data);
            final ByteBuffer nationData = ByteBuffer.wrap(data);
            this.m_numProtectors.put(nationId, nationData.getInt());
            final byte[] dData = new byte[nationData.getInt()];
            nationData.get(dData);
            this.m_diplomacy.put(nationId, dData);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 20034;
    }
}
