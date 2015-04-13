package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.group;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.datas.group.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class CreateGuildRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_blazon;
    private final String m_name;
    private final boolean m_useKamas;
    
    public CreateGuildRequestMessage(final String name, final GuildBlazon blazon, final boolean useKamas) {
        super();
        this.m_name = name;
        this.m_blazon = blazon.getId();
        this.m_useKamas = useKamas;
    }
    
    @Override
    public byte[] encode() {
        final String bestRankName = WakfuTranslator.getInstance().getString("bestRankName");
        final String worstRankName = WakfuTranslator.getInstance().getString("worstRankName");
        final byte[] utfName = StringUtils.toUTF8(this.m_name);
        final byte[] utfBestRankName = StringUtils.toUTF8(bestRankName);
        final byte[] utfWorstRankName = StringUtils.toUTF8(worstRankName);
        final ByteBuffer bb = ByteBuffer.allocate(14 + utfName.length + 6 + utfBestRankName.length + 6 + utfWorstRankName.length + 1);
        bb.putLong(this.m_blazon);
        bb.putInt(utfName.length);
        bb.put(utfName);
        bb.putInt(utfBestRankName.length);
        bb.put(utfBestRankName);
        bb.putInt(utfWorstRankName.length);
        bb.put(utfWorstRankName);
        bb.put((byte)(this.m_useKamas ? 1 : 0));
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 15641;
    }
    
    @Override
    public String toString() {
        return "CreateGuildRequestMessage{m_blazon=" + this.m_blazon + ", m_name='" + this.m_name + '\'' + '}';
    }
}
