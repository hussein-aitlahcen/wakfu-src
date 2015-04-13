package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.wakfu.client.core.*;

public class PlayListLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        PlayListManager.getInstance().setFile(WakfuConfiguration.getContentPath("playListBankFile"));
        PlayListManager.getInstance().load();
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.playList");
    }
}
