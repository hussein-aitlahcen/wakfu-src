package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.craft;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CraftLearnedRecipeMessage extends InputOnlyProxyMessage
{
    private int m_refCraftId;
    private int m_recipeId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_refCraftId = bb.getInt();
        this.m_recipeId = bb.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 15712;
    }
    
    public int getRefCraftId() {
        return this.m_refCraftId;
    }
    
    public int getRecipeId() {
        return this.m_recipeId;
    }
}
