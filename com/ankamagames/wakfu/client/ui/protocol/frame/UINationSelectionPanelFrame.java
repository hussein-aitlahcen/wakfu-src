package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.nation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation.*;
import java.util.*;

public class UINationSelectionPanelFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UINationSelectionPanelFrame m_instance;
    private ArrayList<NationSelectionView> m_nationSelectionViews;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UINationSelectionPanelFrame getInstance() {
        return UINationSelectionPanelFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19007: {
                final UIMessage uiMessage = (UIMessage)message;
                final int nationId = uiMessage.getIntValue();
                final String nationName = WakfuTranslator.getInstance().getString(39, nationId, new Object[0]);
                final String msgText = WakfuTranslator.getInstance().getString("question.nationSelectionConfirm", nationName);
                final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 24L);
                final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final SelectNationRequestMessage selectNationRequestMessage = new SelectNationRequestMessage();
                            selectNationRequestMessage.setNationId(nationId);
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(selectNationRequestMessage);
                            WakfuGameEntity.getInstance().removeFrame(UINationSelectionPanelFrame.this);
                        }
                    }
                });
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
            if (this.m_nationSelectionViews == null) {
                UINationSelectionPanelFrame.m_logger.error((Object)"Nations null on ne peut pas afficher le panneau !");
                return;
            }
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            PropertiesProvider.getInstance().setPropertyValue("nationSelectionList", this.m_nationSelectionViews);
            TextWidgetFormater sb = null;
            if (localPlayer.getLevel() < 15) {
                sb = new TextWidgetFormater().addColor(Color.RED);
                sb.append(WakfuTranslator.getInstance().getString("levelSup", 15));
            }
            else if (localPlayer.getNationId() != 0) {
                sb = new TextWidgetFormater().addColor(Color.RED);
                sb.append(WakfuTranslator.getInstance().getString("nationSelection.alreadySet"));
            }
            PropertiesProvider.getInstance().setPropertyValue("nationSelectionDisabledMessage", (sb == null) ? sb : sb.finishAndToString());
            PropertiesProvider.getInstance().setPropertyValue("selectedNation", null);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("nationSelectionPanelDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UINationSelectionPanelFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("nationSelectionPanelDialog", Dialogs.getDialogPath("nationSelectionPanelDialog"), 256L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.nationSelectionPanel", NationSelectionPanelDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_nationSelectionViews = null;
            PropertiesProvider.getInstance().removeProperty("nationSelectionList");
            PropertiesProvider.getInstance().removeProperty("selectedNation");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("nationSelectionPanelDialog");
            Xulor.getInstance().removeActionClass("wakfu.nationSelectionPanel");
        }
    }
    
    public void setNationInfos(final ArrayList<NationSelectionInfoResult.NationSelectionInfo> nationSelectionInfos) {
        this.m_nationSelectionViews = new ArrayList<NationSelectionView>();
        for (final NationSelectionInfoResult.NationSelectionInfo nationSelectionInfo : nationSelectionInfos) {
            this.m_nationSelectionViews.add(new NationSelectionView(nationSelectionInfo.getNationId(), nationSelectionInfo.getTotalCash(), nationSelectionInfo.getPopulationPercent(), nationSelectionInfo.getGovernorName(), nationSelectionInfo.getProtectorsSize(), nationSelectionInfo.getAlignments()));
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UINationSelectionPanelFrame.class);
        UINationSelectionPanelFrame.m_instance = new UINationSelectionPanelFrame();
    }
}
