package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

@XulorActionsTag
public class BuildingPanelDialogActions extends HavenWorldDialogsActions
{
    private static final Logger m_logger;
    public static final String PACKAGE = "wakfu.buildingPanel";
    
    public static void evolveBuilding(final Event event) {
        HavenWorldDialogsActions.evolveBuilding(event, (HavenWorldElementView)PropertiesProvider.getInstance().getObjectProperty("selectedBuilding"));
    }
    
    public static void deleteBuilding(final Event event) {
        final MessageBoxControler controler = createDeleteMessageBox();
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                final Building selectedBuilding = UIBuildingPanelFrame.getInstance().getSelectedBuilding();
                if (type == 8) {
                    BuildingPanelDialogActions.deleteBuilding(selectedBuilding.getUid());
                    WakfuGameEntity.getInstance().removeFrame(UIBuildingPanelFrame.getInstance());
                }
            }
        });
    }
    
    public static MessageBoxControler createDeleteMessageBox() {
        final String msgText = WakfuTranslator.getInstance().getString("question.havenWorldDeleteBuilding");
        final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
        return Xulor.getInstance().msgBox(data);
    }
    
    public static void deleteBuilding(final long buildingUid) {
        final BuildingDelete delete = new BuildingDelete(buildingUid);
        final HavenWorldManageActionRequest havenWorldManageActionRequest = new HavenWorldManageActionRequest();
        havenWorldManageActionRequest.addAction(delete);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(havenWorldManageActionRequest);
    }
    
    public static void dropFrontManual(final DropEvent dropEvent) {
        if (!(dropEvent.getValue() instanceof Item)) {
            return;
        }
        UIBuildingPanelFrame.getInstance().resetSlot();
        final Item item = (Item)dropEvent.getValue();
        final UIItemMessage uiItemMessage = new UIItemMessage();
        uiItemMessage.setItem(item);
        uiItemMessage.setId(19366);
        Worker.getInstance().pushMessage(uiItemMessage);
    }
    
    public static void removeFrontManual(final DragEvent dragEvent) {
        UIMessage.send((short)19367);
    }
    
    static {
        m_logger = Logger.getLogger((Class)BuildingPanelDialogActions.class);
    }
}
