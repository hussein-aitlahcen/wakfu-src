package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.core.*;
import java.awt.*;
import java.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.wakfu.client.core.game.background.*;

public class UIBackgroundDisplayFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final UIBackgroundDisplayFrame m_instance;
    private BackgroundDisplayView m_backgroundDisplayView;
    private boolean m_modal;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIBackgroundDisplayFrame getInstance() {
        return UIBackgroundDisplayFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16141: {
                this.m_backgroundDisplayView.setPreviousPage();
                this.fadeOut(true);
                return false;
            }
            case 16142: {
                this.m_backgroundDisplayView.setNextPage();
                this.fadeOut(true);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            if (this.m_backgroundDisplayView == null) {
                UIBackgroundDisplayFrame.m_logger.error((Object)"Impossible de charger l'affichage, il manque les donn\u00e9es !");
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("backgroundDisplayDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIBackgroundDisplayFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("backgroundDisplay", this.m_backgroundDisplayView);
            Xulor.getInstance().load("backgroundDisplayDialog", Dialogs.getDialogPath("backgroundDisplayDialog"), 256L, (short)30000);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("backgroundDisplayDialog");
            final BackgroundDisplayType type = this.m_backgroundDisplayView.getType();
            final Margin margin1 = (Margin)map.getElement("margin1");
            this.setMargin(margin1, type.getOuterMargin(), type.getInnerMargin());
            final Margin margin2 = (Margin)map.getElement("margin2");
            this.setMargin(margin2, type.getInnerMargin(), type.getOuterMargin());
            this.fadeOut(false);
            Xulor.getInstance().putActionClass("wakfu.background", BackgroundDisplayDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(type.getOpenSound());
        }
    }
    
    private void setMargin(final Margin margin, final int leftMargin, final int rightMargin) {
        if (margin != null && this.m_backgroundDisplayView != null) {
            final Insets insets = new Insets(50, leftMargin, 50, rightMargin);
            final Container container = margin.getParentOfType(Container.class);
            container.getAppearance().setMargin(insets);
            container.getContainer().getLayoutManager().layoutContainer(container);
        }
    }
    
    private void fadeOut(final boolean contentOnly) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("backgroundDisplayDialog");
        final ArrayList<ModulationColorClient> mcc = new ArrayList<ModulationColorClient>();
        Widget w = null;
        if (!contentOnly) {
            w = (Widget)map.getElement("background");
            if (w != null) {
                mcc.add(w.getAppearance());
            }
            w = (Widget)map.getElement("closeButton");
            if (w != null) {
                mcc.add(w.getAppearance());
            }
            w = (Widget)map.getElement("previousButton");
            if (w != null) {
                mcc.add(w.getAppearance());
            }
            w = (Widget)map.getElement("nextButton");
            if (w != null) {
                mcc.add(w.getAppearance());
            }
        }
        for (int i = 1; i <= 10; ++i) {
            w = (Widget)map.getElement("text" + i);
            if (w != null) {
                mcc.add(w.getAppearance());
            }
        }
        for (int i = 1; i <= 6; ++i) {
            w = (Widget)map.getElement("image" + i);
            if (w != null) {
                mcc.add(w.getAppearance());
            }
        }
        if (w != null) {
            final Color c1 = new Color(Color.WHITE_ALPHA.get());
            final Color c2 = new Color(Color.WHITE.get());
            w.addTween(new ModulationColorListTween(c1, c2, mcc, 0, 500, 1, TweenFunction.PROGRESSIVE));
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final int soundId = (this.m_backgroundDisplayView != null) ? this.m_backgroundDisplayView.getType().getCloseSound() : 600149;
            this.m_backgroundDisplayView = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("backgroundDisplay");
            Xulor.getInstance().unload("backgroundDisplayDialog");
            Xulor.getInstance().removeActionClass("wakfu.background");
            WakfuSoundManager.getInstance().playGUISound(soundId);
        }
    }
    
    public void loadBackgroundDisplay(final int id) {
        this.loadBackgroundDisplay(id, true);
    }
    
    public void loadBackgroundDisplay(final int id, final boolean modal) {
        this.m_modal = modal;
        final BackgroundDisplayData data = BackgroundDisplayManager.INSTANCE.getBackgroundDisplayData(id);
        if (data == null) {
            return;
        }
        final PageData[] datas = data.getPageData();
        final PageView[] pageViews = new PageView[datas.length];
        for (final PageData pageData : datas) {
            pageViews[pageData.getIndex()] = new PageView(pageData.getId(), pageData.getIndex(), pageData.getGfxId(), pageData.getTemplateLayoutId());
        }
        this.m_backgroundDisplayView = new BackgroundDisplayView(data.getType(), pageViews);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIBackgroundDisplayFrame.class);
        m_instance = new UIBackgroundDisplayFrame();
    }
}
