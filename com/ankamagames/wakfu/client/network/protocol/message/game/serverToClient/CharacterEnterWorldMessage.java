package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.protector.*;
import java.nio.*;

public class CharacterEnterWorldMessage extends InputOnlyProxyMessage
{
    private byte[] m_serializedCharacterInfo;
    private final ArrayList<byte[]> m_serializedProtectors;
    private final ArrayList<NationProtectorInfo> m_nationProtectorInfos;
    
    public CharacterEnterWorldMessage() {
        super();
        this.m_serializedProtectors = new ArrayList<byte[]>();
        this.m_nationProtectorInfos = new ArrayList<NationProtectorInfo>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        buffer.get(this.m_serializedCharacterInfo = new byte[buffer.getShort() & 0xFFFF]);
        final byte[] serializedProtectors = new byte[buffer.getShort() & 0xFFFF];
        buffer.get(serializedProtectors);
        final ByteBuffer b = ByteBuffer.wrap(serializedProtectors);
        final short nbProtectors = b.getShort();
        for (int i = 0; i < nbProtectors; ++i) {
            final byte[] serializedProtector = new byte[b.getShort() & 0xFFFF];
            b.get(serializedProtector);
            this.m_serializedProtectors.add(serializedProtector);
        }
        final byte[] serializedProtectorInfos = new byte[buffer.getShort() & 0xFFFF];
        buffer.get(serializedProtectorInfos);
        final ByteBuffer b2 = ByteBuffer.wrap(serializedProtectorInfos);
        final short nbProtectorsInfos = b2.getShort();
        for (int j = 0; j < nbProtectorsInfos; ++j) {
            this.m_nationProtectorInfos.add(NationProtectorInfo.fromBuild(b2));
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 4100;
    }
    
    public byte[] getSerializedCharacterInfo() {
        return this.m_serializedCharacterInfo;
    }
    
    public ArrayList<byte[]> getSerializedProtectors() {
        return this.m_serializedProtectors;
    }
    
    public ArrayList<NationProtectorInfo> getNationProtectorInfos() {
        return this.m_nationProtectorInfos;
    }
}
