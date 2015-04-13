package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class NationSpeechModificationRequestMessage extends OutputOnlyProxyMessage
{
    private String m_newSpeech;
    
    public void setNewSpeech(final String newSpeech) {
        this.m_newSpeech = newSpeech;
    }
    
    @Override
    public byte[] encode() {
        final byte[] speech = StringUtils.toUTF8(this.m_newSpeech);
        final int speechSize = speech.length;
        final ByteBuffer bb = ByteBuffer.allocate(4 + speechSize);
        bb.putInt(speechSize);
        bb.put(speech);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20015;
    }
}
