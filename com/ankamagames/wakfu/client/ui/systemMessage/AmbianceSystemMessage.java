package com.ankamagames.wakfu.client.ui.systemMessage;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class AmbianceSystemMessage extends BannerSystemMessage
{
    public AmbianceSystemMessage() {
        super(WakfuSystemMessageManager.SystemMessageType.AMBIENCE_ZONE_INFO, "messageContainer2");
    }
    
    @Override
    protected String getBannerPath() throws PropertyException {
        return WakfuConfiguration.getContentPath("bannerAnmFilePath");
    }
}
