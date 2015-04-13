package com.ankamagames.wakfu.client.ui.systemMessage;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class ChallengeSystemMessage extends BannerSystemMessage
{
    public ChallengeSystemMessage() {
        super(WakfuSystemMessageManager.SystemMessageType.CHALLENGE, "messageContainerChallenge");
    }
    
    @Override
    protected String getBannerPath() throws PropertyException {
        return WakfuConfiguration.getContentPath("fightBannerAnmFilePath");
    }
}
