package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.game.market.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.market.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.market.*;

public final class NetMarketFrame extends MessageRunnerFrame
{
    private static final Logger m_logger;
    private static final NetMarketFrame m_instance;
    
    private NetMarketFrame() {
        super(new MessageRunner[] { new MarketConsultResultRunner(), new MarketConsultSellerResultRunner() });
    }
    
    public static NetMarketFrame getInstance() {
        return NetMarketFrame.m_instance;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetMarketFrame.class);
        m_instance = new NetMarketFrame();
    }
    
    private static class MarketConsultResultRunner implements MessageRunner<MarketConsultResultMessage>
    {
        @Override
        public boolean run(final MarketConsultResultMessage msg) {
            NetMarketFrame.m_logger.info((Object)"R\u00e9ception d'un message de contenu de MARKET");
            final byte[] raw = msg.getRaw();
            final int totalEntries = msg.getTotalCount();
            final MarketConstant.ConsultReturnSerialType serialType = msg.getSerialType();
            switch (serialType) {
                case MARKET_ENTRY: {
                    final ArrayList<MarketEntry> entries = MarketEntryBuilder.unSerializeList(ByteBuffer.wrap(raw));
                    MarketView.INSTANCE.setMarketEntries(entries.iterator());
                    break;
                }
                case MARKET_HISTORY_ENTRY: {
                    final ArrayList<MarketHistoryEntry> saleList = new ArrayList<MarketHistoryEntry>();
                    final ArrayList<MarketEntry> outdatedList = new ArrayList<MarketEntry>();
                    final ByteBuffer bb = ByteBuffer.wrap(raw);
                    for (int i = 0, size = bb.getInt(); i < size; ++i) {
                        saleList.add(MarketHistoryEntry.fromRaw(bb));
                    }
                    for (int i = 0, size = bb.getInt(); i < size; ++i) {
                        outdatedList.add(MarketEntryBuilder.unSerialize(bb));
                    }
                    MarketView.INSTANCE.setHistoryEntries(saleList, outdatedList);
                    break;
                }
            }
            MarketView.INSTANCE.setTotalResultSize(totalEntries);
            MarketView.INSTANCE.updateResults();
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 20100;
        }
    }
    
    private static class MarketConsultSellerResultRunner implements MessageRunner<MarketConsultSellerResultMessage>
    {
        @Override
        public boolean run(final MarketConsultSellerResultMessage msg) {
            NetMarketFrame.m_logger.info((Object)"R\u00e9ception d'un message de contenu de vente de MARKET");
            final byte[] raw = msg.getRaw();
            final int totalEntries = msg.getTotalCount();
            final int outdatedCount = msg.getOutdatedCount();
            final ArrayList<MarketEntry> entries = MarketEntryBuilder.unSerializeList(ByteBuffer.wrap(raw));
            MarketView.INSTANCE.setMarketEntries(entries.iterator());
            MarketView.INSTANCE.setTotalResultSize(totalEntries);
            MarketView.INSTANCE.setOutdatedResultSize(outdatedCount);
            MarketView.INSTANCE.updateResults();
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 20102;
        }
    }
}
