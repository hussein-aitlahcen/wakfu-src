package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.dialogclose.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.component.*;

public class UIInfoDialogFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIInfoDialogFrame m_instance;
    private static final int DURATION = 20000;
    private InfoView m_infoView;
    private Widget m_dialog;
    private Runnable m_closeRunnable;
    private DialogCloseRequestListener m_dialogCloseRequestListener;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIInfoDialogFrame getInstance() {
        return UIInfoDialogFrame.m_instance;
    }
    
    public void loadInfoDialog(final InfoView infoView) {
        this.m_infoView = infoView;
        if (!WakfuGameEntity.getInstance().hasFrame(this)) {
            WakfuGameEntity.getInstance().pushFrame(this);
        }
        else {
            PropertiesProvider.getInstance().setLocalPropertyValue("infoView", this.m_infoView, "infoDialog");
        }
    }
    
    public void closeDialog() {
        if (this.m_closeRunnable != null) {
            ProcessScheduler.getInstance().remove(this.m_closeRunnable);
            this.m_closeRunnable = null;
        }
        this.m_dialog.removeTweensOfType(PositionTween.class);
        this.m_dialog.addTween(new PositionTween(this.m_dialog.getX(), this.m_dialog.getY(), this.m_dialog.getX(), MasterRootContainer.getInstance().getHeight(), this.m_dialog, 0, 1000, TweenFunction.PROGRESSIVE));
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                WakfuGameEntity.getInstance().removeFrame(UIInfoDialogFrame.this);
            }
        }, 1000L, 1);
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
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("infoDialog")) {
                        UIInfoDialogFrame.this.closeDialog();
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.m_dialogCloseRequestListener = new DialogCloseRequestListener() {
                @Override
                public int onDialogCloseRequest(final String id) {
                    if (id.equals("infoDialog")) {
                        UIInfoDialogFrame.this.closeDialog();
                        return 2;
                    }
                    return 0;
                }
            };
            DialogClosesManager.getInstance().addDialogCloseRequestListener(this.m_dialogCloseRequestListener);
            (this.m_dialog = (Widget)Xulor.getInstance().load("infoDialog", Dialogs.getDialogPath("infoDialog"), 1L, (short)10000)).setVisible(false);
            this.m_dialog.setY(MasterRootContainer.getInstance().getHeight());
            PropertiesProvider.getInstance().setLocalPropertyValue("infoView", this.m_infoView, "infoDialog");
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    UIInfoDialogFrame.this.m_dialog.setX(Alignment9.CENTER.getX(UIInfoDialogFrame.this.m_dialog.getWidth(), MasterRootContainer.getInstance().getWidth()));
                    UIInfoDialogFrame.this.m_dialog.removeTweensOfType(PositionTween.class);
                    UIInfoDialogFrame.this.m_dialog.addTween(new PositionTween(UIInfoDialogFrame.this.m_dialog.getX(), UIInfoDialogFrame.this.m_dialog.getY(), UIInfoDialogFrame.this.m_dialog.getX(), MasterRootContainer.getInstance().getHeight() - UIInfoDialogFrame.this.m_dialog.getHeight() - 50, UIInfoDialogFrame.this.m_dialog, 0, 1000, TweenFunction.PROGRESSIVE));
                    UIInfoDialogFrame.this.m_dialog.setVisible(true);
                }
            }, 1000L, 1);
            this.m_closeRunnable = new Runnable() {
                @Override
                public void run() {
                    UIInfoDialogFrame.this.closeDialog();
                }
            };
            ProcessScheduler.getInstance().schedule(this.m_closeRunnable, 20000L, 1);
            Xulor.getInstance().putActionClass("wakfu.info", InfoDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            DialogClosesManager.getInstance().removeDialogCloseRequestListener(this.m_dialogCloseRequestListener);
            Xulor.getInstance().unload("infoDialog");
            Xulor.getInstance().removeActionClass("wakfu.info");
            if (this.m_closeRunnable != null) {
                ProcessScheduler.getInstance().remove(this.m_closeRunnable);
                this.m_closeRunnable = null;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIInfoDialogFrame.class);
        UIInfoDialogFrame.m_instance = new UIInfoDialogFrame();
    }
    
    public static class InfoView extends ImmutableFieldProvider
    {
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        private String m_title;
        private String m_description;
        
        public InfoView(final String title, final String description) {
            super();
            this.m_title = title;
            this.m_description = description;
        }
        
        @Override
        public String[] getFields() {
            return null;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("title")) {
                return this.m_title;
            }
            if (fieldName.equals("description")) {
                return this.m_description;
            }
            return null;
        }
    }
}
