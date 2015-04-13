package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.framework.reflect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.temporary.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ClientTemporaryInventoryController extends TemporaryInventoryController implements FieldProvider, TemporaryInventoryListener
{
    public static final String TEMPORARY_BAG_INVENTORY_FIELD = "temporaryBagInventory";
    public static final String TEMPORARY_BAG_SIZE = "temporaryBagSize";
    private ArrayList<Item> m_items;
    
    public ClientTemporaryInventoryController(final TemporaryInventory inventory) {
        super(inventory);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("temporaryBagInventory")) {
            if (this.m_items == null) {
                this.createItemList();
            }
            return this.m_items;
        }
        if (fieldName.equals("temporaryBagSize")) {
            return 2;
        }
        return null;
    }
    
    @Override
    protected boolean checkEquipmentCriterion(final Item itemToAdd) {
        final LocalPlayerCharacter localPlayer = UIEquipmentFrame.getCharacter();
        return this.getEquipment() != null && ((ArrayInventoryWithoutCheck<Item, R>)this.getEquipment()).getContentChecker().checkCriterion(itemToAdd, localPlayer, localPlayer.getAppropriateContext());
    }
    
    @Override
    protected ItemEquipment getEquipment() {
        return UIEquipmentFrame.getCharacter().getEquipmentInventory();
    }
    
    @Override
    protected AbstractBag getBag(final long uniqueId) {
        return HeroUtils.getHeroWithBagUid(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), uniqueId).getBags().get(uniqueId);
    }
    
    private void fireUpdate() {
        this.m_items = null;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "temporaryBagInventory", "temporaryBagSize");
    }
    
    @Override
    public void onItemAdded(final Item item) {
        if (!WakfuGameEntity.getInstance().hasFrame(UITemporaryInventoryFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UITemporaryInventoryFrame.getInstance());
        }
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("temporaryInventory.itemAdded", item.getQuantity(), item.getName()));
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
        this.fireUpdate();
    }
    
    @Override
    public void onItemRemoved(final Item item) {
        final TemporaryInventory inventory = this.getInventory();
        if (inventory.isEmpty()) {
            if (WakfuGameEntity.getInstance().hasFrame(UITemporaryInventoryFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(UITemporaryInventoryFrame.getInstance());
            }
        }
        else {
            this.fireUpdate();
        }
    }
    
    @Override
    public void onItemQuantityChanged(final Item item) {
        this.fireUpdate();
    }
    
    @Override
    public void onClear() {
        this.fireUpdate();
    }
    
    private void createItemList() {
        this.m_items = new ArrayList<Item>();
        final TemporaryInventory inventory = this.getInventory();
        inventory.forEach(new TObjectProcedure<Item>() {
            @Override
            public boolean execute(final Item object) {
                ClientTemporaryInventoryController.this.m_items.add(object);
                return true;
            }
        });
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
}
