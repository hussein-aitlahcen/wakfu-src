package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;

public class ActorLevelUpAptitudeRequestMessage extends OutputOnlyProxyMessage
{
    private TShortShortHashMap m_aptitudeModifications;
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putShort((short)this.m_aptitudeModifications.size());
        final TShortShortIterator it = this.m_aptitudeModifications.iterator();
        while (it.hasNext()) {
            it.advance();
            ba.putShort(it.key());
            ba.putShort(it.value());
        }
        return this.addClientHeader((byte)3, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 8407;
    }
    
    public void setAptitudeModifications(final TShortShortHashMap aptitudeModifications) {
        this.m_aptitudeModifications = aptitudeModifications;
    }
}
