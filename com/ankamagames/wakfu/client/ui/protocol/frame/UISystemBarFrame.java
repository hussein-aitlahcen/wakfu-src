package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.ui.protocol.message.preloading.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.updater.*;
import com.ankama.wakfu.utils.injection.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.*;

public class UISystemBarFrame implements MessageFrame
{
    private static UISystemBarFrame m_instance;
    private static final Logger m_logger;
    
    public static UISystemBarFrame getInstance() {
        return UISystemBarFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16400: {
                WakfuGameEntity.getInstance().pushFrame(UIMenuFrame.getInstance());
                return false;
            }
            case 16427: {
                if (!WakfuGameEntity.getInstance().hasFrame(UIDebugInformationGenerator.getInstance())) {
                    WakfuGameEntity.getInstance().pushFrame(UIDebugInformationGenerator.getInstance());
                }
                UIDebugInformationGenerator.takeScreenShot();
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.generateDebugInformation"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            UIMessage.send((short)16440);
                        }
                    }
                });
                return false;
            }
            case 17721: {
                if (!WakfuGameEntity.getInstance().hasFrame(UIAdminMonitorFrame.getInstance())) {
                    WakfuGameEntity.getInstance().pushFrame(UIAdminMonitorFrame.getInstance());
                }
                return false;
            }
            case 17724: {
                if (!WakfuGameEntity.getInstance().hasFrame(UIAdminCharacterEditorFrame.getInstance())) {
                    WakfuGameEntity.getInstance().pushFrame(UIAdminCharacterEditorFrame.getInstance());
                }
                return false;
            }
            case 18050: {
                final UIPreloadingStateMessage msg = (UIPreloadingStateMessage)message;
                switch (msg.getState()) {
                    case UP_TO_DATE: {
                        IsPreloading.m_isPreloading = false;
                        PropertiesProvider.getInstance().setPropertyValue("preloading.isFailing", false);
                        PropertiesProvider.getInstance().setPropertyValue("preloading.isLoading", false);
                        break;
                    }
                    case FAILURE: {
                        PropertiesProvider.getInstance().setPropertyValue("preloading.isFailing", true);
                        break;
                    }
                    case UPDATING: {
                        PropertiesProvider.getInstance().setPropertyValue("preloading.isFailing", false);
                        break;
                    }
                }
                final TextWidgetFormater progressInformation = new TextWidgetFormater();
                progressInformation.append(WakfuTranslator.getInstance().getString("preloading.progressDescription." + msg.getState()));
                PropertiesProvider.getInstance().setPropertyValue("preloading.progressDescription", progressInformation.finishAndToString());
                return false;
            }
            case 18052: {
                final UIPreloadingComponentMessage msg2 = (UIPreloadingComponentMessage)message;
                this.setComponentDescription();
                if (msg2.getComponent() == Component.FULL && msg2.isCompleted()) {
                    AnmManager.getInstance().reloadFailedAnms();
                    IsPreloading.m_isPreloading = false;
                    PropertiesProvider.getInstance().setPropertyValue("preloading.isLoading", false);
                }
                return false;
            }
            case 18051: {
                final UIPreloadingProgressMessage msg3 = (UIPreloadingProgressMessage)message;
                final int estimatedTime = msg3.getEstimatedTime();
                final double percent = msg3.getPercent();
                final int displayedPercent = MathHelper.clamp((int)(percent * 100.0), 0, 100);
                PropertiesProvider.getInstance().setPropertyValue("preloading.percent", displayedPercent);
                PropertiesProvider.getInstance().setPropertyValue("preloading.percentText", displayedPercent + "%");
                final TextWidgetFormater sb = new TextWidgetFormater();
                sb.append(WakfuTranslator.getInstance().getString("preloading.progressDescription." + State.UPDATING, displayedPercent));
                sb.newLine();
                sb.append(WakfuTranslator.getInstance().getString("remainingTime")).append(WakfuTranslator.getInstance().getString("colon"));
                if (estimatedTime > 0) {
                    final GameInterval duration = GameInterval.fromSeconds(estimatedTime / 1000);
                    sb.append(TimeUtils.getShortDescription(duration));
                }
                else {
                    sb.append(" - ");
                }
                PropertiesProvider.getInstance().setPropertyValue("preloading.progressDescription", sb.finishAndToString());
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
    
    private void setComponentDescription() {
        Component component = null;
        for (final Component c : Component.values()) {
            if (Injection.getInstance().getInstance(IComponentManager.class).hasComponentsCompleted(c)) {
                component = c;
            }
        }
        if (component != null) {
            final String desc = WakfuTranslator.getInstance().getString("preloading.componentDescription." + component.name());
            PropertiesProvider.getInstance().setPropertyValue("preloading.componentDescription", desc);
        }
        else {
            PropertiesProvider.getInstance().setPropertyValue("preloading.componentDescription", null);
        }
    }
    
    private void initPreloading() {
        final boolean loading = IsPreloading.m_isPreloading = !Injection.getInstance().getInstance(IComponentManager.class).hasComponentsCompleted(Component.FULL);
        PropertiesProvider.getInstance().setPropertyValue("preloading.isLoading", loading);
        PropertiesProvider.getInstance().setPropertyValue("preloading.isFailing", false);
        this.setComponentDescription();
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            Xulor.getInstance().load("systemBarDialog", Dialogs.getDialogPath("systemBarDialog"), 12288L, (short)10000);
            Xulor.getInstance().load("menuBarDialog", Dialogs.getDialogPath("menuBarDialog"), 12288L, (short)10000);
            Xulor.getInstance().load("reportBugDialog", Dialogs.getDialogPath("reportBugDialog"), 12296L, (short)10000);
            this.initPreloading();
            PropertiesProvider.getInstance().setPropertyValue("reportBug", WakfuConfiguration.getInstance().getBoolean("bugReport.enable", false));
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().unload("systemBarDialog");
            Xulor.getInstance().unload("reportBugDialog");
            Xulor.getInstance().unload("menuBarDialog");
        }
    }
    
    public void reloadMenuBarDialog() {
        Xulor.getInstance().load("menuBarDialog", Dialogs.getDialogPath("menuBarDialog"), 12304L, (short)10000);
    }
    
    static {
        UISystemBarFrame.m_instance = new UISystemBarFrame();
        m_logger = Logger.getLogger((Class)UISystemBarFrame.class);
    }
}
