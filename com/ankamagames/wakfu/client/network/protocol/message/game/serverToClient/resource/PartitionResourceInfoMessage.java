package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.resource;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.resource.*;

public class PartitionResourceInfoMessage extends InputOnlyProxyMessage
{
    private final ResourceDecoder m_decoder;
    
    public PartitionResourceInfoMessage() {
        super();
        this.m_decoder = new ResourceDecoder();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        this.m_decoder.decode(rawDatas);
        return true;
    }
    
    public short getPartitionX() {
        return this.m_decoder.getPartitionX();
    }
    
    public short getPartitionY() {
        return this.m_decoder.getPartitionY();
    }
    
    public ArrayList<ResourceInfo> getResources() {
        return this.m_decoder.getResources();
    }
    
    @Override
    public int getId() {
        return 4202;
    }
}
