package com.ankamagames.wakfu.client.network.protocol.message.craft.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.craftNew.*;

public class CraftTasksSlotsMessage extends InputOnlyProxyMessage
{
    private ArrayList<CraftContract> m_craftContracts;
    
    public CraftTasksSlotsMessage() {
        super();
        this.m_craftContracts = new ArrayList<CraftContract>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        while (buffer.hasRemaining()) {
            this.m_craftContracts.add(CraftSerializer.unSerializeCraftContract(buffer));
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 32008;
    }
    
    public ArrayList<CraftContract> getCraftContracts() {
        return this.m_craftContracts;
    }
}
