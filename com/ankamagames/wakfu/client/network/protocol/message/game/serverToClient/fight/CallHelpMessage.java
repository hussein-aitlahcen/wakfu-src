package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public class CallHelpMessage extends AbstractFightMessage
{
    private long m_fighterId;
    private boolean m_callingHelp;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buff);
        this.m_fighterId = buff.getLong();
        this.m_callingHelp = (buff.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 8154;
    }
    
    public boolean isCallingHelp() {
        return this.m_callingHelp;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
}
