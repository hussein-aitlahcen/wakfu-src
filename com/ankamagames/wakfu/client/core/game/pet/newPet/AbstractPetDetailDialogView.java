package com.ankamagames.wakfu.client.core.game.pet.newPet;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.gameAction.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.text.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public abstract class AbstractPetDetailDialogView<T extends FieldProvider> extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String NAME_FIELD = "name";
    public static final String LEVEL_TEXT_SHORT_FIELD = "levelTextShort";
    public static final String CURRENT_LEVEL_PERCENTAGE_FIELD = "currentLevelPercentage";
    public static final String XP_TEXT_FIELD = "xpText";
    public static final String XP_VALUE_FIELD = "xpValue";
    public static final String SMALL_ICON_URL_FIELD = "smallIconUrl";
    public static final String BREED_NAME_FIELD = "breedName";
    public static final String LAST_MEAL_DATE_TEXT_FIELD = "lastMealDateText";
    public static final String DIET_DESCRIPTION_FIELD = "dietDescription";
    public static final String WEAK_DESCRIPTION_FIELD = "weakDescription";
    public static final String ANIMATED_ELEMENT_FIELD = "animatedElement";
    public static final String PET_ANIMATION_EQUIPMENT_FIELD = "petAnimationEquipment";
    public static final String HP_MAX_FIELD = "hpMax";
    public static final String HP_FIELD = "hp";
    public static final String HP_DESCRIPTION_FIELD = "hpDescription";
    public static final String BONUS_DESCRIPTION_FIELD = "bonusDescription";
    public static final String COLOR_FIELD = "color";
    public static final String COLOR_NAME_FIELD = "colorName";
    public static final String EQUIPMENT_FIELD = "equipment";
    public static final String HAS_PET_FIELD = "hasPet";
    public static final String IS_ACTIVE_FIELD = "isActive";
    public static final String CAN_CHANGE_EQUIPMENT = "canChangeEquipment";
    public static final String IS_MOUNT_FIELD = "isMount";
    public static final String CAN_MOUNT_FIELD = "canMount";
    public static final String[] FIELDS;
    protected PetActor m_petMobile;
    protected Pet m_pet;
    protected T m_petItem;
    
    protected void initPetMobile() {
        this.m_petMobile = new PetActor(this.m_pet);
        final PetDefinition definition = this.m_pet.getDefinition();
        final int equippedRefItemId = this.m_pet.getEquippedRefItemId();
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        this.m_petMobile.setGfx(PetHelper.getPetGfx(player, this.m_pet));
        this.m_petMobile.setDirection(Direction8.SOUTH);
        this.m_petMobile.setAnimation("AnimStatique");
        this.m_petMobile.setMovementSelector(SimpleMovementSelector.getInstance());
        this.m_petMobile.setDeltaZ(LayerOrder.MOBILE.getDeltaZ());
        this.m_petMobile.setAvailableDirections((byte)8);
        this.m_petMobile.setVisible(true);
        this.m_petMobile.forceReloadAnimation();
        PetDefinitionColor colorDef = this.m_pet.getColorDefinition();
        if (colorDef == null) {
            colorDef = definition.getColorDefinition(definition.getBaseColorItemRefId());
        }
        this.m_petMobile.setCustomColor(colorDef);
        final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(equippedRefItemId);
        if (refItem != null) {
            this.m_petMobile.applyPartsEquipment(refItem.getGfxId());
        }
        this.m_petMobile.forceReloadAnimation();
    }
    
    @Override
    public String[] getFields() {
        return AbstractPetDetailDialogView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("levelTextShort")) {
            return WakfuTranslator.getInstance().getString("levelShort.custom", this.m_pet.getLevel());
        }
        if (fieldName.equals("currentLevelPercentage")) {
            return this.m_pet.getXp() / 100;
        }
        if (fieldName.equals("xpText")) {
            final PetDefinition petDef = this.m_pet.getDefinition();
            final TextWidgetFormater twf = new TextWidgetFormater();
            twf.b();
            twf.append(WakfuTranslator.getInstance().getString("xpRatio", this.m_pet.getXp(), petDef.getRequiredXpForLevel((short)(this.m_pet.getLevel() + 1))));
            twf._b().newLine();
            twf.append(WakfuTranslator.getInstance().getString("pet.xpDesc"));
            return twf.finishAndToString();
        }
        if (fieldName.equals("xpValue")) {
            final Pet pet = this.m_pet;
            final short level = pet.getLevel();
            final short xpPerLevel = pet.getDefinition().getXpPerLevel();
            final int ceilXp = level * xpPerLevel;
            final float currentLevelXp = pet.getXp() - ceilXp;
            return currentLevelXp / xpPerLevel;
        }
        if (fieldName.equals("lastMealDateText")) {
            return this.getLastMealDate();
        }
        if (fieldName.equals("animatedElement")) {
            return this.m_petMobile;
        }
        if (fieldName.equals("petAnimationEquipment")) {
            return this.m_petMobile.getAnmInstance();
        }
        if (fieldName.equals("weakDescription")) {
            String itemNamesString = "";
            final int[] itemsRefIds = this.m_pet.getDefinition().getHealItemsRefIds();
            for (int i = 0, size = itemsRefIds.length; i < size; ++i) {
                if (i > 0) {
                    itemNamesString += ", ";
                }
                itemNamesString += ReferenceItemManager.getInstance().getReferenceItem(itemsRefIds[i]).getName();
            }
            return WakfuTranslator.getInstance().getString("pet.weakDesc", itemNamesString);
        }
        if (fieldName.equals("dietDescription")) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            this.m_pet.getDefinition().foreachVisibleMeal(new TIntProcedure() {
                @Override
                public boolean execute(final int refId) {
                    final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(refId);
                    twf2.addImage(CastableDescriptionGenerator.PLOT_URL, -1, -1, null).append(" ");
                    twf2.append(referenceItem.getName()).append(" ");
                    twf2.addImage(WakfuConfiguration.getInstance().getItemSmallIconUrl(referenceItem.getGfxId()), 16, 16, null);
                    twf2.newLine();
                    return true;
                }
            });
            final GameIntervalConst mealMaxInterval = this.m_pet.getDefinition().getMealMaxInterval();
            final GameIntervalConst mealMinInterval = this.m_pet.getDefinition().getMealMinInterval();
            final int minDays = mealMinInterval.getDays();
            final int maxDays = mealMaxInterval.getDays();
            return WakfuTranslator.getInstance().getString("pet.dietDesc", twf2.finishAndToString(), minDays, maxDays, this.getLastMealDate());
        }
        if (fieldName.equals("hpMax")) {
            final Pet pet = this.m_pet;
            return pet.getDefinition().getHealth();
        }
        if (fieldName.equals("hp")) {
            return this.getPetHealth();
        }
        if (fieldName.equals("color")) {
            final ReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_pet.getColorItemRefId());
            if (referenceItem != null) {
                return referenceItem.getFieldValue("iconUrl");
            }
            return null;
        }
        else if (fieldName.equals("colorName")) {
            final ReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_pet.getColorItemRefId());
            if (referenceItem != null) {
                return referenceItem.getName();
            }
            return null;
        }
        else {
            if (fieldName.equals("equipment")) {
                return ReferenceItemManager.getInstance().getDefaultItem(this.m_pet.getEquippedRefItemId());
            }
            if (fieldName.equals("hasPet")) {
                return true;
            }
            if (fieldName.equals("isActive")) {
                return !this.m_pet.isSleeping();
            }
            if (fieldName.equals("hpDescription")) {
                final TextWidgetFormater sb = new TextWidgetFormater();
                final Pet pet2 = this.m_pet;
                try {
                    sb.addImage(WakfuTextImageProvider._getIconUrl((byte)4), 16, 16, null).append(" ");
                }
                catch (PropertyException e) {
                    AbstractPetDetailDialogView.m_logger.warn((Object)e.getMessage());
                    sb.append(WakfuTranslator.getInstance().getString("HPShort")).append(" : ");
                }
                sb.append(this.getPetHealth()).append("/").append(pet2.getDefinition().getHealth());
                sb.newLine();
                final GameIntervalConst mealMaxInterval2 = this.m_pet.getDefinition().getMealMaxInterval();
                final GameIntervalConst mealMinInterval2 = this.m_pet.getDefinition().getMealMinInterval();
                final int minHours = mealMinInterval2.getDays() * 24 + mealMinInterval2.getHours();
                final int maxHours = mealMaxInterval2.getDays() * 24 + mealMaxInterval2.getHours();
                sb.append(WakfuTranslator.getInstance().getString("pet.hpDesc", minHours, maxHours));
                return sb.finishAndToString();
            }
            if (fieldName.equals("canChangeEquipment")) {
                return this.canChangeEquipment();
            }
            if (fieldName.equals("isMount")) {
                return this.m_pet.getDefinition().hasMount();
            }
            if (fieldName.equals("canMount")) {
                final RideOccupation ocupation = new RideOccupation(player);
                return ocupation.isAllowed();
            }
            return this.m_petItem.getFieldValue(fieldName);
        }
    }
    
    protected abstract boolean canChangeEquipment();
    
    private String getLastMealDate() {
        final GameDateConst lastMealDate = this.m_pet.getLastMealDate();
        if (lastMealDate.isNull()) {
            return null;
        }
        return WakfuTranslator.getInstance().formatDateShort(lastMealDate);
    }
    
    private int getPetHealth() {
        final Pet pet = this.m_pet;
        return pet.getHealth();
    }
    
    public String getName() {
        final String name = this.m_pet.getName();
        return (String)((name == null || name.length() == 0) ? this.m_petItem.getFieldValue("name") : name);
    }
    
    public Pet getPet() {
        return this.m_pet;
    }
    
    public abstract int getId();
    
    public abstract long getUID();
    
    public void clean() {
        if (this.m_petMobile != null) {
            this.m_petMobile.dispose();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractPetDetailDialogView.class);
        FIELDS = new String[] { "name", "levelTextShort", "currentLevelPercentage", "xpText", "xpValue", "smallIconUrl", "breedName", "lastMealDateText", "animatedElement", "petAnimationEquipment", "dietDescription", "weakDescription", "hpMax", "hp", "hpDescription", "bonusDescription", "color", "colorName", "equipment", "hasPet", "isActive", "isMount", "canMount" };
    }
}
