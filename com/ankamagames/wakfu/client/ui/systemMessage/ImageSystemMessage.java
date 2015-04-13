package com.ankamagames.wakfu.client.ui.systemMessage;

import org.apache.log4j.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class ImageSystemMessage extends AbstractSystemMessage
{
    private static final Logger m_logger;
    
    public ImageSystemMessage() {
        super(WakfuSystemMessageManager.SystemMessageType.AMBIENCE_ZONE_INFO_2, false, "messageContainer3", (short)10000);
    }
    
    @Override
    protected void applyTweens(final boolean in, final String id) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(id);
        if (map == null) {
            return;
        }
        final int oldDuration = this.m_fadeDuration;
        this.m_fadeDuration = (in ? 1000 : this.m_fadeDuration);
        this.applyTween((Widget)map.getElement("container1"), in);
        this.applyTween((Widget)map.getElement("text"), in);
        if (in) {
            this.m_fadeDuration = 1000;
        }
        this.applyTween((Widget)map.getElement("bannerImage"), in);
        this.m_fadeDuration = oldDuration;
    }
    
    @Override
    public void showMessage(final SystemMessageData data) {
        final ImageSystemMessageData ambData = (ImageSystemMessageData)data;
        super.showMessage(data);
        try {
            final String contentPath = WakfuConfiguration.getContentPath("bannerImageFilePath", ambData.getGfxId());
            PropertiesProvider.getInstance().setLocalPropertyValue("bannerImageIconUrl", contentPath, this.m_dialogId);
        }
        catch (PropertyException e) {
            ImageSystemMessage.m_logger.warn((Object)e.getMessage());
        }
    }
    
    @Override
    protected void clean(final String id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)ImageSystemMessage.class);
    }
}
