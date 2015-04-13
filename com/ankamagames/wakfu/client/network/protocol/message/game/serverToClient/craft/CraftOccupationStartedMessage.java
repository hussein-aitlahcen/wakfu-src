package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.craft;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CraftOccupationStartedMessage extends InputOnlyProxyMessage
{
    private long m_tableId;
    private int m_recipeId;
    private byte m_recipeType;
    private long m_duration;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_tableId = bb.getLong();
        this.m_recipeId = bb.getInt();
        this.m_recipeType = bb.get();
        this.m_duration = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 15716;
    }
    
    public long getTableId() {
        return this.m_tableId;
    }
    
    public int getRecipeId() {
        return this.m_recipeId;
    }
    
    public byte getRecipeType() {
        return this.m_recipeType;
    }
    
    public long getDuration() {
        return this.m_duration;
    }
}
