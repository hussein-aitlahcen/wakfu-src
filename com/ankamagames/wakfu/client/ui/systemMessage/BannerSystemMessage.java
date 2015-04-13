package com.ankamagames.wakfu.client.ui.systemMessage;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;

public abstract class BannerSystemMessage extends AbstractSystemMessage
{
    private static final Logger m_logger;
    
    public BannerSystemMessage(final WakfuSystemMessageManager.SystemMessageType type, final String dialog) {
        super(type, false, dialog, (short)10000);
    }
    
    protected abstract String getBannerPath() throws PropertyException;
    
    @Override
    protected void applyTweens(final boolean in, final String id) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(id);
        if (map == null) {
            return;
        }
        final int oldDuration = this.m_fadeDuration;
        this.m_fadeDuration = (in ? 150 : this.m_fadeDuration);
        this.applyTween((Widget)map.getElement("container1"), in);
        this.applyTween((Widget)map.getElement("image1"), in);
        this.applyTween((Widget)map.getElement("image2"), in);
        this.applyTween((Widget)map.getElement("image3"), in);
        this.applyTween((Widget)map.getElement("text"), in);
        if (in) {
            this.m_fadeDuration = 10;
        }
        this.applyTween((Widget)map.getElement("animatedElementLeft"), in);
        this.applyTween((Widget)map.getElement("animatedElementRight"), in);
        this.m_fadeDuration = oldDuration;
    }
    
    @Override
    public void showMessage(final SystemMessageData data) {
        final BannerSystemMessageData ambData = (BannerSystemMessageData)data;
        super.showMessage(data);
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(this.m_dialogId);
        if (map == null) {
            return;
        }
        final AnimatedElementViewer animatedElementViewerLeft = (AnimatedElementViewer)map.getElement("animatedElementLeft");
        final AnimatedElementViewer animatedElementViewerRight = (AnimatedElementViewer)map.getElement("animatedElementRight");
        String bannerPath;
        try {
            bannerPath = this.getBannerPath();
        }
        catch (PropertyException e) {
            return;
        }
        animatedElementViewerLeft.setFilePath(bannerPath);
        animatedElementViewerLeft.setAnimName(ambData.getAnimationName() + "-G");
        animatedElementViewerLeft.setDirection(0);
        animatedElementViewerLeft.setScale(1.0f);
        animatedElementViewerRight.setFilePath(bannerPath);
        animatedElementViewerRight.setAnimName(ambData.getAnimationName() + "-D");
        animatedElementViewerRight.setDirection(0);
        animatedElementViewerRight.setScale(1.0f);
        AbstractSystemMessage.reloadAnm(animatedElementViewerLeft);
        AbstractSystemMessage.reloadAnm(animatedElementViewerRight);
    }
    
    @Override
    protected void clean(final String id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)BannerSystemMessage.class);
    }
}
