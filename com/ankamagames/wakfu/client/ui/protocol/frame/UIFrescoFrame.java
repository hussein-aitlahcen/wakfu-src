package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.fileFormat.properties.*;
import java.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

public class UIFrescoFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIFrescoFrame m_instance;
    private int m_frescoId;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIFrescoFrame getInstance() {
        return UIFrescoFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        message.getId();
        return true;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setFrescoId(final int id) {
        if (this.m_frescoId == id) {
            return;
        }
        this.m_frescoId = id;
        this.updateFresco();
    }
    
    private void updateFresco() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("frescoDialog");
        if (map == null) {
            UIFrescoFrame.m_logger.warn((Object)"Impossible de trouver l'elementMap");
            return;
        }
        final MultipleImage image = (MultipleImage)map.getElement("multipleImage");
        try {
            image.setParticlePath(WakfuConfiguration.getInstance().getParticlePath(800248));
            final String imagePath = String.format(WakfuConfiguration.getInstance().getString("frescoPath"), this.m_frescoId);
            image.setImagePath(imagePath);
        }
        catch (PropertyException e) {
            UIFrescoFrame.m_logger.warn((Object)"Probl\u00e8me de Property", (Throwable)e);
        }
    }
    
    private void addFade(final int argb1, final int argb2, final int duration) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("frescoDialog");
        final ArrayList<ModulationColorClient> mcc = new ArrayList<ModulationColorClient>();
        Widget w = (Widget)map.getElement("closeButton");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        w = (Widget)map.getElement("multipleImage");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        w = (Widget)map.getElement("leftArrowContainer");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        w = (Widget)map.getElement("rightArrowContainer");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        w = (Widget)map.getElement("mainWindow");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        if (w != null) {
            final Color c1 = new Color(argb1);
            final Color c2 = new Color(argb2);
            w.addTween(new ModulationColorListTween(c1, c2, mcc, 0, duration, 1, TweenFunction.PROGRESSIVE));
        }
    }
    
    private void addFadeIn() {
        this.addFade(Color.WHITE_ALPHA.get(), Color.WHITE.get(), 1000);
        WakfuSoundManager.getInstance().fadeAmbiance(0.2f, 1000);
        WakfuSoundManager.getInstance().fadeMusic(0.4f, 1000);
    }
    
    private void addFadeOut() {
        this.addFade(Color.WHITE.get(), Color.WHITE_ALPHA.get(), 250);
        WakfuSoundManager.getInstance().fadeAmbiance(1.0f, 300);
        WakfuSoundManager.getInstance().fadeMusic(1.0f, 300);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("frescoDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIFrescoFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            final EventDispatcher eventDispatcher = Xulor.getInstance().load("frescoDialog", Dialogs.getDialogPath("frescoDialog"), 257L, (short)10000);
            final ElementMap elementMap = eventDispatcher.getElementMap();
            final MultipleImage image = (MultipleImage)elementMap.getElement("multipleImage");
            final Widget leftArrowContainer = (Widget)elementMap.getElement("leftArrowContainer");
            final Widget rightArrowContainer = (Widget)elementMap.getElement("rightArrowContainer");
            image.setImageListener(new MultipleImage.MultipleImageListener() {
                @Override
                public void onDeltaXBounded(final byte position) {
                    switch (position) {
                        case 0: {
                            leftArrowContainer.setVisible(false);
                            break;
                        }
                        case 2: {
                            rightArrowContainer.setVisible(false);
                            break;
                        }
                        case 1: {
                            leftArrowContainer.setVisible(true);
                            rightArrowContainer.setVisible(true);
                            break;
                        }
                    }
                }
                
                @Override
                public void onDeltaYBounded(final byte position) {
                }
            });
            leftArrowContainer.setVisible(false);
            this.updateFresco();
            this.addFadeIn();
            Xulor.getInstance().putActionClass("wakfu.fresco", FrescoDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            this.addFadeOut();
            Xulor.getInstance().removeActionClass("wakfu.fresco");
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    Xulor.getInstance().unload("frescoDialog");
                }
            }, 250L, 1);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIFrescoFrame.class);
        UIFrescoFrame.m_instance = new UIFrescoFrame();
    }
}
