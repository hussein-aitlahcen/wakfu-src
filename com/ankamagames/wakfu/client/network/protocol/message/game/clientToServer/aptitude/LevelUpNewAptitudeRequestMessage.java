package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.aptitude;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;

public final class LevelUpNewAptitudeRequestMessage extends OutputOnlyProxyMessage
{
    private TIntShortHashMap m_aptitudeModifications;
    
    @Override
    public byte[] encode() {
        final ByteArray bb = new ByteArray();
        bb.putShort((short)this.m_aptitudeModifications.size());
        final TIntShortIterator it = this.m_aptitudeModifications.iterator();
        while (it.hasNext()) {
            it.advance();
            bb.putInt(it.key());
            bb.putShort(it.value());
        }
        return this.addClientHeader((byte)3, bb.toArray());
    }
    
    public void setAptitudeModifications(final TIntShortHashMap aptitudeModifications) {
        this.m_aptitudeModifications = aptitudeModifications;
    }
    
    @Override
    public int getId() {
        return 8416;
    }
    
    @Override
    public String toString() {
        return "LevelUpNewAptitudeRequestMessage{m_aptitudeModifications=" + this.m_aptitudeModifications + '}';
    }
}
