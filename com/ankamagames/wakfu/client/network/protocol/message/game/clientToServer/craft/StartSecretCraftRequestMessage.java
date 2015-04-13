package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.craft;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import gnu.trove.*;

public class StartSecretCraftRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_craftTableId;
    private final int m_recipeId;
    private final byte m_recipeType;
    private final TIntShortHashMap m_ingredients;
    
    public StartSecretCraftRequestMessage(final long craftTableId, final int recipeId, final byte recipeType) {
        super();
        this.m_ingredients = new TIntShortHashMap();
        this.m_craftTableId = craftTableId;
        this.m_recipeId = recipeId;
        this.m_recipeType = recipeType;
    }
    
    public void addIngredient(final int refId, final short quantity) {
        this.m_ingredients.put(refId, quantity);
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(13 + this.m_ingredients.size() * 6);
        buffer.putLong(this.m_craftTableId);
        buffer.putInt(this.m_recipeId);
        buffer.put(this.m_recipeType);
        final TIntShortIterator it = this.m_ingredients.iterator();
        while (it.hasNext()) {
            it.advance();
            buffer.putInt(it.key());
            buffer.putShort(it.value());
        }
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15713;
    }
}
