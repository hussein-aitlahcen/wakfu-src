package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.component.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

final class RemoveLastGemItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private long m_itemToUngemUid;
    
    RemoveLastGemItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            RemoveLastGemItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        this.changeMouseAndAddListener(item);
        return true;
    }
    
    private void sendUngemRequest(final Item item, final LocalPlayerCharacter character) {
        Item itemToUngem = character.getBags().getItemFromInventories(this.m_itemToUngemUid);
        if (itemToUngem == null) {
            itemToUngem = ((ArrayInventoryWithoutCheck<Item, R>)character.getEquipmentInventory()).getWithUniqueId(this.m_itemToUngemUid);
        }
        if (itemToUngem == null) {
            RemoveLastGemItemAction.m_logger.warn((Object)("[ItemAction] Tentative d'utilisation de d\u00e9gemmage d'un item qui n'existe pas dans l'inventaire " + this.m_itemToUngemUid));
            return;
        }
        this.sendRequest(item.getUniqueId());
    }
    
    private void changeMouseAndAddListener(final Item item) {
        CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM5, true);
        final MouseReleasedListener mouseListener = new MouseReleasedListener() {
            @Override
            public boolean run(final Event event) {
                if (event instanceof MouseEvent && ((MouseEvent)event).getButton() == 3) {
                    MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this, true);
                    CursorFactory.getInstance().unlock();
                    UIWorldInteractionFrame.getInstance().removeMouseReleasedListener(this);
                }
                else if (event != null && event instanceof MouseEvent && ((MouseEvent)event).getButton() == 1) {
                    final Widget widget = MasterRootContainer.getInstance().getMouseOver();
                    final RenderableContainer rc = widget.getParentOfType(RenderableContainer.class);
                    if (rc == null) {
                        return false;
                    }
                    final Object value = rc.getItemValue();
                    if (!(value instanceof Item)) {
                        return false;
                    }
                    final Item itemToUngem = (Item)value;
                    if (!itemToUngem.hasGemsSlotted()) {
                        return false;
                    }
                    RemoveLastGemItemAction.this.m_itemToUngemUid = itemToUngem.getUniqueId();
                    final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
                    RemoveLastGemItemAction.this.sendUngemRequest(item, character);
                    MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this, true);
                    CursorFactory.getInstance().unlock();
                    UIWorldInteractionFrame.getInstance().removeMouseReleasedListener(this);
                }
                return false;
            }
        };
        UIWorldInteractionFrame.getInstance().addMouseReleasedListener(mouseListener);
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, mouseListener, true);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        super.serialize(buffer);
        buffer.putLong(this.m_itemToUngemUid);
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
        return ItemActionConstants.REMOVE_LAST_GEM;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RemoveLastGemItemAction.class);
    }
}
