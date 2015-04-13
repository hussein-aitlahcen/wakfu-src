package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.common.game.companion.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class RerollElementsItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private long m_itemId;
    protected int m_maxLevel;
    
    RerollElementsItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
        final int paramCount = params.length;
        this.m_maxLevel = ((paramCount > 0) ? Integer.parseInt(params[0]) : 0);
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            RerollElementsItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        if (ClientTradeHelper.INSTANCE.isTradeRunning()) {
            return false;
        }
        this.changeMouseAndAddListener(item);
        return true;
    }
    
    private void changeMouseAndAddListener(final Item item) {
        CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM5, true);
        final MouseReleasedListener mouseListener = new MouseReleasedListener() {
            @Override
            public boolean run(final Event event) {
                if (event == null) {
                    return false;
                }
                if (event instanceof MouseEvent && ((MouseEvent)event).getButton() == 3) {
                    MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this, true);
                    CursorFactory.getInstance().unlock();
                    UIWorldInteractionFrame.getInstance().removeMouseReleasedListener(this);
                }
                else if (event instanceof MouseEvent && ((MouseEvent)event).getButton() == 1) {
                    final Widget widget = MasterRootContainer.getInstance().getMouseOver();
                    final RenderableContainer rc = widget.getParentOfType(RenderableContainer.class);
                    if (rc == null) {
                        return false;
                    }
                    final Object value = rc.getItemValue();
                    if (!(value instanceof Item)) {
                        return false;
                    }
                    final Item itemToRoll = (Item)value;
                    if (itemToRoll.getUniqueId() == item.getUniqueId()) {
                        return false;
                    }
                    if (itemToRoll.getQuantity() > 1) {
                        ChatHelper.pushErrorMessage("error.rollElements.itemStack", new Object[0]);
                        return false;
                    }
                    if (!itemToRoll.getReferenceItem().hasRandomElementEffect() || itemToRoll.needRollRandomElementsEffect()) {
                        ChatHelper.pushErrorMessage("error.rollElements.cantReroll", new Object[0]);
                        return false;
                    }
                    if (itemToRoll.getLevel() > RerollElementsItemAction.this.m_maxLevel && RerollElementsItemAction.this.m_maxLevel != 0) {
                        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.item.cantReroll"), 3);
                        return false;
                    }
                    if (item.getQuantity() == 1 && RerollElementsItemAction.this.isMustConsumeItem()) {
                        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this, true);
                        CursorFactory.getInstance().unlock();
                        UIWorldInteractionFrame.getInstance().removeMouseReleasedListener(this);
                    }
                    RerollElementsItemAction.this.m_itemId = itemToRoll.getUniqueId();
                    final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("reroll.itemElementsConfirmation"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
                    controler.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                RerollElementsItemAction.this.sendRequest(item);
                                EquipmentDialogActions.addRollElementsParticleToContainer(rc);
                            }
                        }
                    });
                }
                return false;
            }
        };
        UIWorldInteractionFrame.getInstance().addMouseReleasedListener(mouseListener);
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, mouseListener, true);
    }
    
    private void sendRequest(final Item tool) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        Item itemToRoll = character.getBags().getItemFromInventories(this.m_itemId);
        if (itemToRoll == null) {
            itemToRoll = ((ArrayInventoryWithoutCheck<Item, R>)character.getEquipmentInventory()).getWithUniqueId(this.m_itemId);
        }
        if (itemToRoll == null) {
            final long clientId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
            final CompanionModel companionEquiped = CompanionManager.INSTANCE.getCompanionHoldingItem(clientId, this.m_itemId);
            if (companionEquiped != null) {
                itemToRoll = ((ArrayInventoryWithoutCheck<Item, R>)companionEquiped.getItemEquipment()).getWithUniqueId(this.m_itemId);
            }
        }
        if (itemToRoll == null) {
            RerollElementsItemAction.m_logger.warn((Object)("[ItemAction] Tentative d'utilisation de reroll d'\u00e9l\u00e9ments d'un item qui n'existe pas dans l'inventaire " + this.m_itemId));
            return;
        }
        this.sendRequest(tool.getUniqueId());
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        super.serialize(buffer);
        buffer.putLong(this.m_itemId);
        return true;
    }
    
    @Override
    public int serializedSize() {
        return super.serializedSize() + 8;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.REROLL_ELEMENTS;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RerollElementsItemAction.class);
    }
}
