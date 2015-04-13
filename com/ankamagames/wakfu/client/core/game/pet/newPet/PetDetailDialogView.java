package com.ankamagames.wakfu.client.core.game.pet.newPet;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.pet.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.gameAction.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class PetDetailDialogView extends AbstractPetDetailDialogView<Item> implements PetModelListener
{
    private static final Logger m_logger;
    
    public PetDetailDialogView(final Item petItem) {
        super();
        this.m_petItem = (T)petItem;
        (this.m_pet = petItem.getPet()).addListener(this);
        this.initPetMobile();
    }
    
    @Override
    protected boolean canChangeEquipment() {
        return ((ArrayInventoryWithoutCheck<Item, R>)WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory()).contains((Item)this.m_petItem) || WakfuGameEntity.getInstance().getLocalPlayer().getBags().contains((InventoryContent)this.m_petItem) != null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("smallIconUrl")) {
            return ((Item)this.m_petItem).getFieldValue("iconUrl");
        }
        if (fieldName.equals("breedName")) {
            return ((Item)this.m_petItem).getFieldValue("name");
        }
        if (!fieldName.equals("bonusDescription")) {
            return super.getFieldValue(fieldName);
        }
        final boolean valid = !this.m_pet.isSleeping() && this.m_pet.getHealth() > 0;
        final ArrayList<String> descs = new ArrayList<String>();
        final Object value = ((Item)this.m_petItem).getFieldValue("effectAndCaracteristic");
        if (value == null) {
            return descs;
        }
        for (final String desc : (ArrayList)value) {
            final TextWidgetFormater twf = new TextWidgetFormater();
            if (!valid) {
                twf.openText();
                twf.addColor(Color.GRAY.getRGBtoHex());
            }
            twf.append(desc);
            if (!valid) {
                twf.closeText();
            }
            descs.add(twf.finishAndToString());
        }
        return descs;
    }
    
    @Override
    public void nameChanged(final String name) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "name");
    }
    
    @Override
    public void colorItemChanged(final int colorItemRefId) {
        final PetDefinitionColor colorDef = this.m_pet.getDefinition().getColorDefinition(colorItemRefId);
        if (colorDef == null) {
            return;
        }
        this.m_petMobile.setCustomColor(colorDef);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "animatedElement", "color");
    }
    
    @Override
    public void equippedItemChanged(final int equippedItem) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        PetHelper.applyEquipment(player, this.m_pet, this.m_petMobile, equippedItem);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "petAnimationEquipment", "animatedElement", "equipment");
    }
    
    @Override
    public void healthChanged(final int health) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hp", "hpDescription");
    }
    
    @Override
    public void xpChanged(final int xp) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "levelTextShort", "xpValue", "xpText", "bonusDescription");
    }
    
    @Override
    public void lastMealDateChanged(final GameDateConst mealDate) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "lastMealDateText", "dietDescription");
    }
    
    @Override
    public void lastHungryDateChanged(final GameDateConst hungryDate) {
        if (this.m_pet.getHealth() == 0) {
            return;
        }
        this.m_petMobile.setAnimation("AnimEmote-Effrayee");
        this.m_petMobile.forceReloadAnimation();
    }
    
    @Override
    public void sleepDateChanged(final GameDateConst sleepDate) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isActive");
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_petItem, "backgroundStyle");
    }
    
    @Override
    public void sleepItemChanged(final int sleepRefItemId) {
    }
    
    public Item getPetItem() {
        return (Item)this.m_petItem;
    }
    
    @Override
    public String getName() {
        final String name = this.m_pet.getName();
        return (name == null || name.length() == 0) ? ((Item)this.m_petItem).getName() : name;
    }
    
    @Override
    public void clean() {
        super.clean();
        this.m_pet.removeListener(this);
    }
    
    public void updateFields() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, PetDetailDialogView.FIELDS);
    }
    
    public Actor getPetMobile() {
        return this.m_petMobile;
    }
    
    @Override
    public long getUID() {
        return ((Item)this.m_petItem).getUniqueId();
    }
    
    @Override
    public int getId() {
        return ((Item)this.m_petItem).getReferenceId();
    }
    
    static {
        m_logger = Logger.getLogger((Class)PetDetailDialogView.class);
    }
}
