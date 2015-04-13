package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class ModifiedGuildRankView extends GuildRankView
{
    private String m_name;
    
    public ModifiedGuildRankView(final GuildRank rank) {
        super(rank);
        this.m_name = rank.getName();
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "name");
    }
}
