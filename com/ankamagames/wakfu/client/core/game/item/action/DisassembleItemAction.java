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
import com.ankamagames.wakfu.common.game.item.validator.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public final class DisassembleItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private long m_itemToDisassembleUid;
    
    DisassembleItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            DisassembleItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
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
                    final Item itemToRecycle = (Item)value;
                    final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
                    final Collection<Item> recyclableItem = character.getBags().getAllWithValidator(new RecyclableItemValidator(character));
                    if (!recyclableItem.contains(itemToRecycle)) {
                        return false;
                    }
                    DisassembleItemAction.this.m_itemToDisassembleUid = itemToRecycle.getUniqueId();
                    if (item.getQuantity() == 1) {
                        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this, true);
                        CursorFactory.getInstance().unlock();
                        UIWorldInteractionFrame.getInstance().removeMouseReleasedListener(this);
                    }
                    DisassembleItemAction.this.sendRecycleRequest(item, character);
                }
                return false;
            }
        };
        UIWorldInteractionFrame.getInstance().addMouseReleasedListener(mouseListener);
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, mouseListener, true);
    }
    
    private void sendRecycleRequest(final Item item, final LocalPlayerCharacter character) {
        Item itemToRecycle = character.getBags().getItemFromInventories(this.m_itemToDisassembleUid);
        if (itemToRecycle == null) {
            itemToRecycle = ((ArrayInventoryWithoutCheck<Item, R>)character.getEquipmentInventory()).getWithUniqueId(this.m_itemToDisassembleUid);
        }
        if (itemToRecycle == null) {
            DisassembleItemAction.m_logger.warn((Object)("[ItemAction] Tentative d'utilisation de d\u00e9gemmage d'un item qui n'existe pas dans l'inventaire " + this.m_itemToDisassembleUid));
            return;
        }
        final Collection<Item> recyclableItem = character.getBags().getAllWithValidator(new RecyclableItemValidator(character));
        if (!recyclableItem.contains(itemToRecycle)) {
            return;
        }
        this.sendRequest(item.getUniqueId());
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        super.serialize(buffer);
        buffer.putLong(this.m_itemToDisassembleUid);
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
        return ItemActionConstants.DISASSEMBLE_ITEM;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DisassembleItemAction.class);
    }
}
