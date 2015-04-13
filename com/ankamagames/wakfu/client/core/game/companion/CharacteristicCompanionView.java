package com.ankamagames.wakfu.client.core.game.companion;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.common.game.fight.protagonists.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.companion.freeCompanion.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.time.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class CharacteristicCompanionView extends CharacterView implements CharacteristicUpdateListener
{
    protected static final Logger m_logger;
    public static final String CHARACTER_SHEET_ILLUSTRATION_URL_FIELD = "characterSheetillustrationUrl";
    public static final String SPELLS_ILLUSTRATION_URL_FIELD = "spellsIllustrationUrl";
    public static final String LIST_ILLUSTRATION_URL_FIELD = "listIllustrationUrl";
    public static final String CURRENT_LEVEL_PERCENTAGE_FIELD = "currentLevelPercentage";
    public static final String XP_RATIO_FIELD = "xpRatio";
    public static final String EQUIPMENT_BONUSES = "equipmentBonus";
    public static final String HEAD_EQUIPMENT_FIELD = "headEquipment";
    public static final String HAIR_EQUIPMENT_FIELD = "hairEquipment";
    public static final String FACE_EQUIPMENT_FIELD = "faceEquipment";
    public static final String SHOULDERS_EQUIPMENT_FIELD = "shoulderEquipment";
    public static final String NECK_EQUIPMENT_FIELD = "neckEquipment";
    public static final String CHEST_EQUIPMENT_FIELD = "chestEquipment";
    public static final String ARMS_EQUIPMENT_FIELD = "armsEquipment";
    public static final String LEFT_HAND_EQUIPMENT_FIELD = "leftHandEquipment";
    public static final String RIGHT_HAND_EQUIPMENT_FIELD = "rightHandEquipment";
    public static final String BELT_EQUIPMENT_FIELD = "beltEquipment";
    public static final String SKIRT_EQUIPMENT_FIELD = "skirtEquipment";
    public static final String TROUSERS_EQUIPMENT_FIELD = "trousersEquipment";
    public static final String LEGS_EQUIPMENT_FIELD = "legsEquipment";
    public static final String PET_EQUIPMENT_FIELD = "petEquipment";
    public static final String MOUNT_EQUIPMENT_FIELD = "mountEquipment";
    public static final String COSTUME_EQUIPMENT_FIELD = "costumeEquipment";
    public static final String BACK_EQUIPMENT_FIELD = "backEquipment";
    public static final String WING_EQUIPMENT_FIELD = "wingEquipment";
    public static final String FIRST_WEAPON_EQUIPMENT_FIELD = "firstWeaponEquipment";
    public static final String SECOND_WEAPON_EQUIPMENT_FIELD = "secondWeaponEquipment";
    public static final String ACCESSORY_EQUIPMENT_FIELD = "accessoryEquipment";
    public static final String BACKGROUND_TEXT_FIELD = "backgroundText";
    public static final String SHOP_COST_FIELD = "shopCost";
    public static final String CAN_BUY_FIELD = "canBuy";
    public static final String IS_OWNED_FIELD = "isOwned";
    public static final String IS_ACTIVATED_FIELD = "isActivated";
    public static final String IS_FREE_FIELD = "isFree";
    public static final String BREED_INFO_FIELD = "breedInfo";
    public static final String ACTOR_STANDARD_SCALE = "actorStandardScale";
    public static final String REMOVE_DISABLED_TEXT = "removeDisabledText";
    public static final String[] UPDATE_EQUIPMENT_PROPERTIES;
    private final CompanionModel m_companion;
    private Article m_shopArticle;
    private TLongObjectHashMap<CompanionSpellAnimation> m_spellsAnimations;
    
    public CharacteristicCompanionView(final CharacterInfo characterInfo, final ShortCharacterView shortCharacterView) {
        this(characterInfo, shortCharacterView, CompanionManager.INSTANCE.getCompanion(((CompanionViewShort)shortCharacterView).getCompanionId()));
    }
    
    public CharacteristicCompanionView(final CharacterInfo characterInfo, final ShortCharacterView shortCharacterView, final CompanionModel companionModel) {
        super(characterInfo, shortCharacterView);
        this.m_spellsAnimations = new TLongObjectHashMap<CompanionSpellAnimation>();
        this.m_companion = companionModel;
        ((LazyFighterCharacteristicManager)this.getCharacterInfo().getCharacteristics()).addListener(this);
        this.loadSpellsAnimations();
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("nameAndLevel")) {
            return this.m_characterInfo.getNameAndLevel(this.getName());
        }
        if (fieldName.equals("breedInfo")) {
            return this.m_characterInfo;
        }
        if (fieldName.equals("companionRealLevel")) {
            return WakfuTranslator.getInstance().getString("levelShort.custom", this.m_companion.getLevel());
        }
        if (fieldName.equals("characterSheetillustrationUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("companionCharacterSheetIllustrationPath"), this.m_characterInfo.getBreedId());
            }
            catch (PropertyException e) {
                CharacteristicCompanionView.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        if (fieldName.equals("spellsIllustrationUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("companionSpellInventoryIllustrationsPath"), this.m_characterInfo.getBreedId());
            }
            catch (PropertyException e) {
                CharacteristicCompanionView.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        if (fieldName.equals("listIllustrationUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("companionListIllustrationsPath"), this.m_characterInfo.getBreedId());
            }
            catch (PropertyException e) {
                CharacteristicCompanionView.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        if (fieldName.equals("currentLevelPercentage")) {
            return this.getCurrentLevelPercentage();
        }
        if (fieldName.equals("xpRatio")) {
            if (this.getLevel() >= XpConstants.getPlayerCharacterLevelCap()) {
                return WakfuTranslator.getInstance().getString("maxLevel");
            }
            final StringBuilder sb = new StringBuilder();
            final long levelExtent = this.getXpTable().getLevelExtent(this.getLevel());
            final long xpInLevel = Math.min(this.getXpTable().getXpInLevel(this.getCurrentXp()), levelExtent);
            sb.append(WakfuTranslator.getInstance().getString("xpRatio", WakfuTranslator.getInstance().formatNumber(xpInLevel), WakfuTranslator.getInstance().formatNumber(levelExtent)));
            return sb.toString();
        }
        else {
            if (fieldName.equals("headEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.HEAD.m_id);
            }
            if (fieldName.equals("hairEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.HAIR.m_id);
            }
            if (fieldName.equals("faceEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.FACE.m_id);
            }
            if (fieldName.equals("shoulderEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.SHOULDERS.m_id);
            }
            if (fieldName.equals("neckEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.NECK.m_id);
            }
            if (fieldName.equals("chestEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.CHEST.m_id);
            }
            if (fieldName.equals("armsEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.ARMS.m_id);
            }
            if (fieldName.equals("leftHandEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.LEFT_HAND.m_id);
            }
            if (fieldName.equals("rightHandEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.RIGHT_HAND.m_id);
            }
            if (fieldName.equals("beltEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.BELT.m_id);
            }
            if (fieldName.equals("skirtEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.SKIRT.m_id);
            }
            if (fieldName.equals("trousersEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.TROUSERS.m_id);
            }
            if (fieldName.equals("legsEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.LEGS.m_id);
            }
            if (fieldName.equals("backEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.BACK.m_id);
            }
            if (fieldName.equals("wingEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.WING.m_id);
            }
            if (fieldName.equals("firstWeaponEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.FIRST_WEAPON.m_id);
            }
            if (fieldName.equals("secondWeaponEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.SECOND_WEAPON.m_id);
            }
            if (fieldName.equals("accessoryEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.ACCESSORY.m_id);
            }
            if (fieldName.equals("petEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.PET.m_id);
            }
            if (fieldName.equals("mountEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.MOUNT.m_id);
            }
            if (fieldName.equals("costumeEquipment")) {
                return ((ArrayInventoryWithoutCheck<Object, R>)this.m_companion.getItemEquipment()).getFromPosition(EquipmentPosition.COSTUME.m_id);
            }
            if (fieldName.equals("equipmentBonus")) {
                final ArrayList<Item> items = new ArrayList<Item>();
                for (final Item it : this.m_companion.getItemEquipment()) {
                    items.add(it);
                }
                return PlayerCharacter.getEquipmentBonus(this.m_characterInfo, items);
            }
            if (fieldName.equals("backgroundText")) {
                return WakfuTranslator.getInstance().getString(String.format("companionBackgroundText.%d", this.getCharacterInfo().getBreedId()));
            }
            if (fieldName.equals("shopCost")) {
                if (this.isOwned()) {
                    return null;
                }
                return (this.m_shopArticle == null) ? null : this.m_shopArticle.getPrice();
            }
            else {
                if (fieldName.equals("canBuy")) {
                    return this.m_shopArticle != null;
                }
                if (fieldName.equals("isOwned")) {
                    return this.isOwned();
                }
                if (fieldName.equals("isFree")) {
                    return this.isFree();
                }
                if (fieldName.equals("isActivated")) {
                    return this.isActivated();
                }
                if (fieldName.equals("actorStandardScale")) {
                    final CharacterActor characterActor = this.m_characterInfo.getActor();
                    final short height = characterActor.getHeight();
                    final float anmScale = characterActor.getAnmInstance().getScale();
                    return 6.0f / Math.max(height, 6.0f) * 2.0f * anmScale;
                }
                if (fieldName.equals("removeDisabledText")) {
                    boolean disabled = false;
                    final TextWidgetFormater twf = new TextWidgetFormater().b().addColor(Color.RED);
                    twf.append(WakfuTranslator.getInstance().getString("desc.removeCompanionDisabled"))._b();
                    final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                    final PartyComportment partyComportment = localPlayer.getPartyComportment();
                    if (partyComportment.isInParty()) {
                        final PartyModel party = partyComportment.getParty();
                        if (party.contains(-this.getCompanionId())) {
                            twf.newLine().append(WakfuTranslator.getInstance().getString("companion.stillInParty"));
                            disabled = true;
                        }
                    }
                    if (!this.m_companion.getItemEquipment().isEmpty()) {
                        disabled = true;
                        twf.newLine().append(WakfuTranslator.getInstance().getString("companion.hasEquipment"));
                    }
                    if (localPlayer.getBags().getNbFreePlaces() == 0) {
                        disabled = true;
                        twf.newLine().append(WakfuTranslator.getInstance().getString("bagsFull"));
                    }
                    return disabled ? twf.finishAndToString() : null;
                }
                if (fieldName.equals("name")) {
                    final String name = this.m_companion.getName();
                    return (name == null || name.isEmpty()) ? this.m_shortCharacterView.getName() : name;
                }
                if (fieldName.equals("rerollXpBonus")) {
                    return this.getXpRerollBonus();
                }
                return super.getFieldValue(fieldName);
            }
        }
    }
    
    private String getXpRerollBonus() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final float rerollXpFactor = localPlayer.getXpRerollBonus() * CompanionConstants.COMPANION_XP_FACTOR;
        return WakfuTranslator.getInstance().getString("rerollXp.factor", rerollXpFactor);
    }
    
    @Override
    protected boolean isInParty() {
        for (final ShortCharacterView shortCharacterView : WakfuGameEntity.getInstance().getLocalPlayer().getCompanionViews()) {
            if (shortCharacterView != null && shortCharacterView.getBreedId() == this.getCharacterInfo().getBreedId()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected boolean addRemovePartyEnabled() {
        return super.addRemovePartyEnabled() && (this.isFree() || this.isOwned());
    }
    
    @Override
    public CharacterInfo getCharacterInfo() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return super.getCharacterInfo();
        }
        if (!localPlayer.isOnFight()) {
            return super.getCharacterInfo();
        }
        final Collection<CharacterInfo> fighters = localPlayer.getCurrentFight().getFighters(ProtagonistFilter.ofType((byte)5), ProtagonistFilter.controlledBy(localPlayer));
        for (final CharacterInfo ci : fighters) {
            if (ci.getBreedId() == this.m_characterInfo.getBreedId()) {
                return ci;
            }
        }
        return super.getCharacterInfo();
    }
    
    public boolean isOwned() {
        return this.m_companion.isUnlocked();
    }
    
    public boolean isActivated() {
        return this.m_companion.getId() != 0L;
    }
    
    public boolean isFree() {
        return FreeCompanionManager.INSTANCE.getFreeCompanionBreedId() == this.m_characterInfo.getBreedId();
    }
    
    @Override
    public boolean isCompanion() {
        return true;
    }
    
    @Override
    public ItemEquipment getItemEquipment() {
        return this.m_companion.getItemEquipment();
    }
    
    public float getCurrentLevelPercentage() {
        return this.getXpTable().getPercentageInLevel(this.getLevel(), this.getCurrentXp());
    }
    
    public short getLevel() {
        return this.m_characterInfo.getLevel();
    }
    
    public XpTable getXpTable() {
        return CharacterXpTable.getInstance();
    }
    
    public long getCurrentXp() {
        return this.m_companion.getXp();
    }
    
    public long getCompanionId() {
        return this.m_companion.getId();
    }
    
    public String getName() {
        return this.m_shortCharacterView.getName();
    }
    
    public void setShopArticle(final Article shopArticle) {
        this.m_shopArticle = shopArticle;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "shopCost", "canBuy");
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CharacteristicCompanionView && this.m_characterInfo.getBreedId() == ((CharacteristicCompanionView)obj).getCharacterInfo().getBreedId();
    }
    
    public Article getShopArticle() {
        return this.m_shopArticle;
    }
    
    public CompanionSpellAnimation getSpellAnimations(final long spellId) {
        return this.m_spellsAnimations.get(spellId);
    }
    
    public void loadSpellsAnimations() {
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "characteristics");
    }
    
    public void onLeaveFight() {
        ((LazyFighterCharacteristicManager)this.getCharacterInfo().getCharacteristics()).removeListener(this);
        TimeManager.INSTANCE.removeListener(this.getCharacterInfo().getRunningEffectFieldProvider());
    }
    
    public void onJoinFight() {
        ((LazyFighterCharacteristicManager)this.getCharacterInfo().getCharacteristics()).addListener(this);
        TimeManager.INSTANCE.addListener(this.getCharacterInfo().getRunningEffectFieldProvider());
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacteristicCompanionView.class);
        UPDATE_EQUIPMENT_PROPERTIES = new String[] { "headEquipment", "hairEquipment", "faceEquipment", "shoulderEquipment", "neckEquipment", "chestEquipment", "armsEquipment", "leftHandEquipment", "rightHandEquipment", "beltEquipment", "skirtEquipment", "trousersEquipment", "legsEquipment", "petEquipment", "mountEquipment", "backEquipment", "wingEquipment", "firstWeaponEquipment", "secondWeaponEquipment", "accessoryEquipment", "costumeEquipment", "actorEquipment" };
    }
}
