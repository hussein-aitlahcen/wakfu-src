package com.ankamagames.wakfu.client.core.world.dynamicElement;

import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.baseImpl.graphics.game.DynamicElement.*;

public class HavenWorldGuildDynamicElementTypeProvider extends GuildDynamicElementTypeProvider
{
    @Override
    protected long getGuildBlazon() {
        final GuildInfo guildInfo = this.getGuildInfo();
        if (guildInfo == null) {
            return 0L;
        }
        return guildInfo.getBlazon();
    }
    
    protected GuildInfo getGuildInfo() {
        if (!HavenWorldManager.INSTANCE.hasHavenWorld()) {
            HavenWorldGuildDynamicElementTypeProvider.m_logger.error((Object)"Doit \u00eatre plac\u00e9 dans un havre monde!!!");
            return null;
        }
        return HavenWorldManager.INSTANCE.getGuildInfo();
    }
}
