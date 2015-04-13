package com.ankamagames.wakfu.client;

import org.apache.log4j.*;
import java.awt.*;
import com.ankamagames.baseImpl.graphics.opengl.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import java.net.*;

class WakfuApplicationUI extends SkinnedGLApplicationUI
{
    private static final Logger m_logger;
    private final Dimension m_minDimensions;
    
    WakfuApplicationUI() {
        super();
        this.m_minDimensions = new Dimension(800, 600);
        try {
            final String skinPath = WakfuConfiguration.getInstance().getString("appSkinPath");
            final ApplicationSkin skinNormal = new ApplicationSkin(skinPath);
            final ApplicationSkin skinOff = new ApplicationSkin(skinPath, "Off", skinNormal);
            this.setTitle(WakfuTranslator.getInstance().getString("desc.wakfu"));
            this.setSkins(skinNormal, skinOff);
            final int minWidth = WakfuConfiguration.getInstance().getInteger("resolution.min.width", 800);
            final int minHeight = WakfuConfiguration.getInstance().getInteger("resolution.min.height", 600);
            this.m_minDimensions.setSize(minWidth, minHeight);
        }
        catch (PropertyException e) {
            WakfuApplicationUI.m_logger.error((Object)"Unable to create skinPath", (Throwable)e);
        }
    }
    
    @Override
    public void onCloseEvent() {
        if (SWFWrapper.INSTANCE.isOpened()) {
            SWFWrapper.INSTANCE.unload();
            return;
        }
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.quit"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    WakfuApplicationUI.this.onCloseEvent();
                }
            }
        });
    }
    
    @Override
    protected URL getIconURL() {
        return WakfuClientInstance.class.getResource("icon.png");
    }
    
    @Override
    public Dimension getMinimumSize() {
        return this.m_minDimensions;
    }
    
    @Override
    public String toString() {
        return "WakfuApplicationUI{m_minDimensions=" + this.m_minDimensions + "} ";
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuApplicationUI.class);
    }
}
