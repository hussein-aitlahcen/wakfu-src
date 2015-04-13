package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.treasure.*;

public class TreasureLoader implements ContentInitializer
{
    private static final Logger m_logger;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new TreasureBinaryData(), new LoadProcedure<TreasureBinaryData>() {
            @Override
            public void load(final TreasureBinaryData data) {
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.treasure");
    }
    
    public static Treasure loadTreasure(final int id) {
        final Treasure treasure = new Treasure(id);
        try {
            BinaryDocumentManager.getInstance().forId(id, new TreasureBinaryData(), new LoadProcedure<TreasureBinaryData>() {
                @Override
                public void load(final TreasureBinaryData data) throws Exception {
                    treasure.setQuantity(data.getQuantity());
                    treasure.setRewardItem(data.getRewardItem());
                    treasure.setRewardKama(data.getRewardKama());
                    treasure.setUsedItem(data.getUsedItem());
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return treasure;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TreasureLoader.class);
    }
}
