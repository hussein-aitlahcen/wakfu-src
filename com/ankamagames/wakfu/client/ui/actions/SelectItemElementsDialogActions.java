package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.core.messagebox.*;

@XulorActionsTag
public class SelectItemElementsDialogActions
{
    public static final String PACKAGE = "wakfu.selectItemElements";
    
    public static void selectDamageElement(final Event e, final ElementFilterView elementFilterView) {
        final ToggleButton button = e.getCurrentTarget();
        elementFilterView.setSelected(button.getSelected());
        final SelectItemElementsView elementsView = SelectItemElementsView.INSTANCE;
        if (elementsView.tooManyChoices(SelectItemElementsView.DAMAGE_ACTION_ID)) {
            elementFilterView.setSelected(false);
            button.setSelected(false);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(elementsView, "checkedDamageNbElements");
    }
    
    public static void selectResElement(final Event e, final ElementFilterView elementFilterView) {
        final ToggleButton button = e.getCurrentTarget();
        elementFilterView.setSelected(button.getSelected());
        final SelectItemElementsView elementsView = SelectItemElementsView.INSTANCE;
        if (elementsView.tooManyChoices(SelectItemElementsView.RES_ACTION_ID)) {
            elementFilterView.setSelected(false);
            button.setSelected(false);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(elementsView, "checkedResNbElements");
    }
    
    public static void valid(final Event e) {
        final MessageBoxControler msgBox = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("setItemElements.question"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        msgBox.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    final SelectItemElementsView elementsView = SelectItemElementsView.INSTANCE;
                    final Item item = elementsView.getConcernedItem();
                    final SetItemElementsRequestMessage netMsg = new SetItemElementsRequestMessage();
                    netMsg.setItemUid(item.getUniqueId());
                    netMsg.setDamageElementMask(elementsView.getElementMask(SelectItemElementsView.DAMAGE_ACTION_ID));
                    netMsg.setResElementMask(elementsView.getElementMask(SelectItemElementsView.RES_ACTION_ID));
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
                    WakfuGameEntity.getInstance().removeFrame(UISelectItemElementsFrame.getInstance());
                }
            }
        });
    }
}
