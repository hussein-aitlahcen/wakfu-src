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
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public final class ChangeGemItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private long m_gemmedItemId;
    private byte m_index;
    
    ChangeGemItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            ChangeGemItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        this.changeMouseAndAddListener(item);
        return true;
    }
    
    private void changeMouseAndAddListener(final Item item) {
        CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM5, true);
        UIImproveGemFrame.getInstance().setEnableFrame(false);
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
                    UIImproveGemFrame.getInstance().setEnableFrame(true);
                }
                else if (event instanceof MouseEvent && ((MouseEvent)event).getButton() == 1) {
                    final Widget widget = MasterRootContainer.getInstance().getMouseOver();
                    final RenderableContainer rc = widget.getParentOfType(RenderableContainer.class);
                    if (rc == null) {
                        return false;
                    }
                    final Object value = rc.getItemValue();
                    if (!(value instanceof GemSlotDisplayer)) {
                        return false;
                    }
                    final GemSlotDisplayer slotDisplayer = (GemSlotDisplayer)value;
                    final Item holder = slotDisplayer.getHolder();
                    if (holder == null) {
                        return false;
                    }
                    if (slotDisplayer.getGemItem() == null) {
                        return false;
                    }
                    ChangeGemItemAction.this.m_gemmedItemId = holder.getUniqueId();
                    ChangeGemItemAction.this.m_index = slotDisplayer.getIndex();
                    if (item.getQuantity() == 1) {
                        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this, true);
                        CursorFactory.getInstance().unlock();
                        UIWorldInteractionFrame.getInstance().removeMouseReleasedListener(this);
                        UIImproveGemFrame.getInstance().setEnableFrame(true);
                    }
                    ChangeGemItemAction.this.sendGemRequest(item);
                }
                return false;
            }
        };
        UIWorldInteractionFrame.getInstance().addMouseReleasedListener(mouseListener);
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, mouseListener, true);
    }
    
    private void sendGemRequest(final Item tool) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        Item gemmedItem = character.getBags().getItemFromInventories(this.m_gemmedItemId);
        if (gemmedItem == null) {
            gemmedItem = ((ArrayInventoryWithoutCheck<Item, R>)character.getEquipmentInventory()).getWithUniqueId(this.m_gemmedItemId);
        }
        if (gemmedItem == null) {
            final long clientId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
            final CompanionModel companionEquiped = CompanionManager.INSTANCE.getCompanionHoldingItem(clientId, this.m_gemmedItemId);
            if (companionEquiped != null) {
                gemmedItem = ((ArrayInventoryWithoutCheck<Item, R>)companionEquiped.getItemEquipment()).getWithUniqueId(this.m_gemmedItemId);
            }
        }
        if (gemmedItem == null) {
            ChangeGemItemAction.m_logger.warn((Object)("[ItemAction] Tentative d'utilisation de regemmage d'un item qui n'existe pas dans l'inventaire " + this.m_gemmedItemId));
            return;
        }
        final Gems gems = gemmedItem.getGems();
        if (gems == null) {
            return;
        }
        if (this.m_index < 0 || this.m_index >= gems.getSlotCount() || gems.getGem(this.m_index) == 0) {
            return;
        }
        this.sendRequest(tool.getUniqueId());
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        super.serialize(buffer);
        buffer.putLong(this.m_gemmedItemId);
        buffer.put(this.m_index);
        return true;
    }
    
    @Override
    public int serializedSize() {
        return super.serializedSize() + 8 + 1;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.CHANGE_GEM_TYPE;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChangeGemItemAction.class);
    }
}
