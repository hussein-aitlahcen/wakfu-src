package com.ankamagames.wakfu.client.core.game.characterInfo.guild;

import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.xulor2.property.*;

public class SelfGuildInformationHelper
{
    public static final SelfGuildInformationHelper INSTANCE;
    private boolean m_isLoading;
    
    private SelfGuildInformationHelper() {
        super();
        this.m_isLoading = false;
    }
    
    public void sendUpdateRequestMessage() {
        this.setLoading(true);
    }
    
    public boolean needsUpdate() {
        return !this.m_isLoading && !WakfuGuildView.getInstance().isLoaded();
    }
    
    public void setLoading(final boolean loading) {
        this.m_isLoading = loading;
        PropertiesProvider.getInstance().setPropertyValue("guild.isLoading", loading);
    }
    
    public void cleanUp() {
        this.m_isLoading = false;
        PropertiesProvider.getInstance().setPropertyValue("guild.isLoading", false);
    }
    
    static {
        INSTANCE = new SelfGuildInformationHelper();
    }
}
