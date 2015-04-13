package com.ankamagames.wakfu.client.core.game.storageBox;

import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.storageBox.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.reflect.*;

public class StorageBoxViewImpl extends AbstractStorageBoxView
{
    protected int m_id;
    
    public StorageBoxViewImpl(final IEStorageBoxParameter storageBoxInfo, final StorageBoxInventory storageBoxInventory) {
        super();
        this.m_id = storageBoxInfo.getId();
        for (byte index = 0; index < storageBoxInfo.getCompartmentSize(); ++index) {
            final IEStorageBoxParameter.Compartment compartment = storageBoxInfo.getFromIndex(index);
            final StorageBoxCompartment inventoryCompartment = storageBoxInventory.get(compartment.getId());
            if (inventoryCompartment != null) {
                this.m_compartmentViewImpls.add(new CompartmentViewImpl(compartment, index, inventoryCompartment));
            }
            else {
                this.m_compartmentViewImpls.add(new CompartmentViewImpl(compartment, index));
            }
        }
        this.m_selectedCompartmentImpl = this.m_compartmentViewImpls.get(0);
    }
    
    @Override
    protected String getName() {
        return WakfuTranslator.getInstance().getString(104, this.m_id, new Object[0]);
    }
    
    @Override
    protected Dimension getPrefSize() {
        return new Dimension(200, 120);
    }
    
    @Override
    protected int getIdealSizeMaxColumns() {
        return 5;
    }
    
    @Override
    protected int getIdealSizeMaxRows() {
        return 3;
    }
    
    @Override
    protected boolean canManageMoney() {
        return false;
    }
    
    @Override
    protected boolean canPutMoney() {
        return false;
    }
    
    @Override
    protected boolean canTakeMoney() {
        return false;
    }
    
    @Override
    protected FieldProvider getHistory() {
        return null;
    }
    
    @Override
    public int getSize() {
        return this.m_compartmentViewImpls.size();
    }
    
    @Override
    public void depositMoney(final int amount) {
    }
    
    @Override
    public void withdrawMoney(final int amount) {
    }
}
