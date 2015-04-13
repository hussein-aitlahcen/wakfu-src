package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.datas.guild.agt_like.*;
import com.ankamagames.wakfu.common.datas.guild.bonus.*;
import com.ankamagames.wakfu.common.game.guild.bonus.*;
import com.ankamagames.wakfu.client.core.*;

public class GuildBonusLoader implements ContentInitializer
{
    private static final Logger m_logger;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        for (final GuildBonusDataAGT data : GuildBonusDataAGT.values()) {
            final GuildBonusDefinition guildBonus = data.get();
            if (GuildHelper.isBonusValid(guildBonus)) {
                GuildBonusManager.INSTANCE.add(guildBonus);
            }
            else {
                GuildBonusLoader.m_logger.warn((Object)("Bonus de guilde mal saisi. id=" + guildBonus.getId()), (Throwable)new Exception());
            }
        }
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.guildBonus");
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildBonusLoader.class);
    }
}
