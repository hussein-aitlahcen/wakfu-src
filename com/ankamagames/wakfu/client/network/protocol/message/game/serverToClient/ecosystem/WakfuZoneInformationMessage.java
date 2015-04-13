package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.ecosystem;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class WakfuZoneInformationMessage extends InputOnlyProxyMessage
{
    private int m_ambianceZoneId;
    private byte[] m_wakfuMonsterInformations;
    private byte[] m_wakfuResourceInformations;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_ambianceZoneId = buffer.getInt();
        final int monsterInformationSize = buffer.getInt();
        buffer.get(this.m_wakfuMonsterInformations = new byte[monsterInformationSize]);
        final int resourceInformationSize = buffer.getInt();
        buffer.get(this.m_wakfuResourceInformations = new byte[resourceInformationSize]);
        return true;
    }
    
    @Override
    public int getId() {
        return 12600;
    }
    
    public byte[] getWakfuMonsterInformations() {
        return this.m_wakfuMonsterInformations;
    }
    
    public byte[] getWakfuResourceInformations() {
        return this.m_wakfuResourceInformations;
    }
    
    public int getAmbianceZoneId() {
        return this.m_ambianceZoneId;
    }
}
