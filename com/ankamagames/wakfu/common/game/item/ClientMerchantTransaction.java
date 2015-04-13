package com.ankamagames.wakfu.common.game.item;

public class ClientMerchantTransaction<ItemType extends Item>
{
    private byte m_error;
    private AbstractBag m_targetContainer;
    private ItemType m_obtainedItem;
    private int m_price;
    private int m_quantity;
    public static final byte NO_ERROR = 0;
    public static final byte FAILED = 1;
    public static final byte INVENTORY_FULL = 2;
    public static final byte INVENTORY_LOCKED = 3;
    public static final byte NOT_SUBSCRIBER = 4;
    
    public ClientMerchantTransaction() {
        super();
        this.m_error = 1;
    }
    
    public byte getError() {
        return this.m_error;
    }
    
    public void setError(final byte error) {
        this.m_error = error;
    }
    
    public AbstractBag getTargetContainer() {
        return this.m_targetContainer;
    }
    
    public void setTargetContainer(final AbstractBag targetContainer) {
        this.m_targetContainer = targetContainer;
    }
    
    public ItemType getObtainedItem() {
        return this.m_obtainedItem;
    }
    
    public void setObtainedItem(final ItemType obtainedItem) {
        this.m_obtainedItem = obtainedItem;
    }
    
    public int getPrice() {
        return this.m_price;
    }
    
    public void setPrice(final int price) {
        this.m_price = price;
    }
    
    public int getQuantity() {
        return this.m_quantity;
    }
    
    public void setQuantity(final int quantity) {
        this.m_quantity = quantity;
    }
    
    public boolean isSuccess() {
        return this.m_error == 0;
    }
}
