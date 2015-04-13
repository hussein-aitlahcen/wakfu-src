package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class NationGovernmentNominationRequestMessage extends OutputOnlyProxyMessage
{
    private long m_id;
    private String m_name;
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public void setId(final long id) {
        this.m_id = id;
    }
    
    @Override
    public byte[] encode() {
        final byte[] speech = StringUtils.toUTF8(this.m_name);
        final int speechSize = speech.length;
        final ByteBuffer bb = ByteBuffer.allocate(12 + speechSize);
        bb.putLong(this.m_id);
        bb.putInt(speechSize);
        bb.put(speech);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20021;
    }
}
