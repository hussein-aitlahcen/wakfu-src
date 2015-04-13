package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.collector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;
import gnu.trove.*;

public class CollectorInventoryLimitedModificationRequestMessage extends OutputOnlyProxyMessage
{
    private static final Logger m_logger;
    private final TIntIntProcedure m_copyProcedure;
    private final TIntIntHashMap m_addedItems;
    private int m_cash;
    
    public CollectorInventoryLimitedModificationRequestMessage() {
        super();
        this.m_copyProcedure = new TIntIntProcedure() {
            @Override
            public boolean execute(final int a, final int b) {
                if (CollectorInventoryLimitedModificationRequestMessage.this.m_addedItems.put(a, b) != 0) {
                    throw new UnsupportedOperationException("Impossible d'ajouter plusieurs fois un m\u00eame item");
                }
                return true;
            }
        };
        this.m_addedItems = new TIntIntHashMap();
        this.m_cash = 0;
    }
    
    public void addedItem(final TIntIntHashMap items) throws UnsupportedOperationException {
        items.forEachEntry(this.m_copyProcedure);
    }
    
    public void setCash(final int cash) {
        this.m_cash = cash;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(4 + this.m_addedItems.size() * 8);
        buffer.putInt(this.m_cash);
        final TIntIntIterator it = this.m_addedItems.iterator();
        while (it.hasNext()) {
            it.advance();
            buffer.putInt(it.key());
            buffer.putInt(it.value());
        }
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15731;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CollectorInventoryLimitedModificationRequestMessage.class);
    }
}
