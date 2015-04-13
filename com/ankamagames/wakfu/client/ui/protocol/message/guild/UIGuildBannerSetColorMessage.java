package com.ankamagames.wakfu.client.ui.protocol.message.guild;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;

public class UIGuildBannerSetColorMessage extends UIMessage
{
    private Color m_color;
    private GuildBannerGenerator.GuildBannerColorType m_type;
    
    public void setColor(final Color c) {
        this.m_color = c;
    }
    
    public Color getColor() {
        return this.m_color;
    }
    
    public GuildBannerGenerator.GuildBannerColorType getType() {
        return this.m_type;
    }
    
    public void setType(final GuildBannerGenerator.GuildBannerColorType type) {
        this.m_type = type;
    }
}
