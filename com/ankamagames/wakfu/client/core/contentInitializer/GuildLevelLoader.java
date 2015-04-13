package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.datas.guild.agt_like.*;
import com.ankamagames.wakfu.common.datas.guild.level.*;
import com.ankamagames.wakfu.client.core.*;

public class GuildLevelLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        for (final GuildLevelDataAGT data : GuildLevelDataAGT.values()) {
            final GuildLevel guildLevel = data.get();
            GuildLevelManager.INSTANCE.add(guildLevel);
        }
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.guildLevel");
    }
}
