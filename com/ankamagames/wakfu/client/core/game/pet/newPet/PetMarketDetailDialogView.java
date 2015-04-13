package com.ankamagames.wakfu.client.core.game.pet.newPet;

import com.ankamagames.wakfu.client.core.game.market.*;

public class PetMarketDetailDialogView extends AbstractPetDetailDialogView<MerchantItemView>
{
    public PetMarketDetailDialogView(final MerchantItemView marketEntryView) {
        super();
        this.m_petItem = (T)marketEntryView;
        this.m_pet = marketEntryView.getItem().getPet();
        this.initPetMobile();
    }
    
    @Override
    protected boolean canChangeEquipment() {
        return false;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("smallIconUrl")) {
            return ((MerchantItemView)this.m_petItem).getFieldValue("iconUrl");
        }
        if (fieldName.equals("breedName")) {
            return ((MerchantItemView)this.m_petItem).getFieldValue("name");
        }
        if (fieldName.equals("bonusDescription")) {
            return ((MerchantItemView)this.m_petItem).getFieldValue("effectAndCaracteristic");
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public long getUID() {
        return ((MerchantItemView)this.m_petItem).getId();
    }
    
    @Override
    public int getId() {
        return ((MerchantItemView)this.m_petItem).getItem().getReferenceItem().getId();
    }
    
    public MerchantItemView getPetItem() {
        return (MerchantItemView)this.m_petItem;
    }
    
    @Override
    public String getName() {
        final String name = this.m_pet.getName();
        return (String)((name == null || name.length() == 0) ? ((MerchantItemView)this.m_petItem).getFieldValue("name") : name);
    }
}
