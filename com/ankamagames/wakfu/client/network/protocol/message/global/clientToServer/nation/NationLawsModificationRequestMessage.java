package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import gnu.trove.*;

public class NationLawsModificationRequestMessage extends OutputOnlyProxyMessage
{
    private TLongHashSet m_activatedLaws;
    
    public void setActivatedLaws(final TLongHashSet activatedLaws) {
        this.m_activatedLaws = activatedLaws;
    }
    
    @Override
    public byte[] encode() {
        final int size = this.m_activatedLaws.size();
        final ByteBuffer bb = ByteBuffer.allocate(2 + 8 * size);
        bb.putShort((short)size);
        final TLongIterator it = this.m_activatedLaws.iterator();
        while (it.hasNext()) {
            bb.putLong(it.next());
        }
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20032;
    }
}
