package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.datas.havenWorld.agt_like.*;
import com.ankamagames.wakfu.common.game.havenWorld.auction.*;
import com.ankamagames.wakfu.client.core.*;

public final class HavenWorldAuctionDefinitionLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        for (final HavenWorldAuctionDefinitionDataAgt data : HavenWorldAuctionDefinitionDataAgt.values()) {
            final HavenWorldAuctionDefinition definition = data.getDefinition();
            HavenWorldAuctionDefinitionManager.INSTANCE.add(definition);
        }
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.havenWorldAuction");
    }
}
