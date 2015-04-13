package com.ankamagames.wakfu.client.core.game.item;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.xp.*;
import com.ankamagames.wakfu.common.game.item.mergeSet.*;
import com.ankamagames.wakfu.common.game.item.companion.*;
import com.ankamagames.wakfu.common.game.item.rent.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.craft.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.xulor2.util.text.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ItemDisplayerImpl implements ItemDisplayer
{
    private LocalPlayerCharacter m_localPlayer;
    public static final String ENABLED_FIELD = "isEnabled";
    public static final String IS_ACTIVE_FIELD = "isActive";
    public static final String QUANTITY_FIELD = "quantity";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String LEVEL_DESCRIPTION_FIELD = "levelDescription";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String ICON_BIG_URL_FIELD = "iconBigUrl";
    public static final String MOVABLE_FIELD = "movable";
    public static final String USABLE_IN_FIGHT_FIELD = "usableInFight";
    public static final String BACKGROUND_STYLE_FIELD = "backgroundStyle";
    public static final String IS_EQUIPED_FIELD = "isEquiped";
    public static final String IS_IN_INVENTORY_FIELD = "isInInventory";
    public static final String USED_IN_CURRENT_RECIPE_FIELD = "usedInCurrentRecipe";
    public static final String DELETABLE_FIELD = "deletable";
    public static final String USABLE_FIELD = "usable";
    public static final String USABLE_NOW_FIELD = "usableNow";
    public static final String CLICK_DESCRIPTION_FIELD = "clickDescription";
    public static final String IS_EMPTY_SLOT = "isEmptySlot";
    public static final String HAS_PET_FIELD = "hasPet";
    public static final String HAS_XP = "hasXp";
    public static final String CURRENT_XP_PERCENTAGE = "currentXpPercentage";
    public static final String CURRENT_XP_DESCRIPTION = "currentXpDescription";
    public static final String CURRENT_XP_LEVEL = "currentXpLevel";
    public static final String IS_REF_ITEM_FIELD = "isRefItem";
    public static final String IS_SORTABLE = "isSortable";
    public static final String IS_ITEM_SET_MERGEABLE = "isItemSetMergeable";
    public static final String CAN_MERGE_ITEM_SET = "canMergeItemSet";
    public static final String HAS_INVALID_ACTIONS = "hasInvalidActions";
    public static final String INVALID_ACTIONS_DESCRIPTION = "invalidActionsDescription";
    public static final String IS_RENT = "isRent";
    public static final String RENT_INFO_DESCRIPTION = "rentInfoDescription";
    public static final String COMPANION_LEVEL_FIELD = "companionLevel";
    public static final String SET_ITEMS_FIELD = "setItems";
    public static final String[] FIELDS;
    protected static final Logger m_logger;
    private final String[] ITEM_PROPERTIES;
    private final TLongObjectHashMap<GemsDisplayer> m_gemDisplayers;
    private final TLongObjectHashMap<ArrayList<String>> m_allCharacteristics;
    private final TLongObjectHashMap<ArrayList<String>> m_characteristics;
    private final TLongObjectHashMap<ArrayList<String>> m_effects;
    private final TLongObjectHashMap<ArrayList<String>> m_criticalEffects;
    
    public ItemDisplayerImpl() {
        super();
        this.m_localPlayer = null;
        this.ITEM_PROPERTIES = new String[] { "itemPopupDetail" };
        this.m_gemDisplayers = new TLongObjectHashMap<GemsDisplayer>();
        this.m_allCharacteristics = new TLongObjectHashMap<ArrayList<String>>();
        this.m_characteristics = new TLongObjectHashMap<ArrayList<String>>();
        this.m_effects = new TLongObjectHashMap<ArrayList<String>>();
        this.m_criticalEffects = new TLongObjectHashMap<ArrayList<String>>();
    }
    
    @Override
    public String[] getFields() {
        return ItemDisplayerImpl.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final Item item, final String fieldName) {
        if (WakfuGameEntity.getInstance().hasFrame(UIEquipmentFrame.getInstance())) {
            this.m_localPlayer = UIEquipmentFrame.getCharacter();
        }
        if (this.m_localPlayer == null) {
            this.m_localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        }
        if (fieldName.equals("isRefItem")) {
            return false;
        }
        if (fieldName.equals("quantity")) {
            return item.getQuantity();
        }
        if (fieldName.equals("description")) {
            final String s = item.getReferenceItem().getDescription();
            if (s == null || s.isEmpty()) {
                return null;
            }
            return "\"" + s + "\"";
        }
        else {
            if (fieldName.equals("level")) {
                return item.getLevel();
            }
            if (fieldName.equals("levelDescription")) {
                final boolean valid = isLevelValid(item);
                final TextWidgetFormater sb = new TextWidgetFormater();
                if (!valid) {
                    sb.openText().addColor("ff4a4a");
                }
                sb.append(WakfuTranslator.getInstance().getString("levelShort.custom", item.getLevel()));
                if (!valid) {
                    sb.closeText();
                }
                return sb.finishAndToString();
            }
            if (fieldName.equals("isEnabled")) {
                return true;
            }
            if (fieldName.equals("isActive")) {
                return item.isActive();
            }
            if (fieldName.equals("backgroundStyle")) {
                return getBackgroundStyle(item);
            }
            if (fieldName.equals("iconUrl")) {
                return this.getIconUrl(item);
            }
            if (fieldName.equals("iconBigUrl")) {
                return WakfuConfiguration.getInstance().getItemBigIconUrl((this.m_localPlayer.getSex() == 1) ? item.getFemaleGfxId() : item.getGfxId());
            }
            if (fieldName.equals("movable")) {
                return !isItemUsedInSeedSpreader(item) && !isItemUsedInHavenWorldResourcesCollector(item) && !((ReferenceItem)item.getReferenceItem()).isItemUsedInCraft();
            }
            if (fieldName.equals("usableInFight")) {
                return item.isUsableInFight();
            }
            if (fieldName.equals("isInInventory")) {
                return this.m_localPlayer.getBags().getItemFromInventories(item.getUniqueId()) != null;
            }
            if (fieldName.equals("isEquiped")) {
                return ((ArrayInventoryWithoutCheck<Item, R>)this.m_localPlayer.getEquipmentInventory()).contains(item) || this.m_localPlayer.getBags().contains(item.getUniqueId());
            }
            if (fieldName.equals("usedInCurrentRecipe")) {
                return UIRecycleFrame.getInstance().containsItem(item.getUniqueId()) || ((ReferenceItem)item.getReferenceItem()).isItemUsedInCraft();
            }
            if (fieldName.equals("deletable")) {
                final SimpleCriterion simpleCriterion = item.getReferenceItem().getCriterion(ActionsOnItem.DELETE);
                return !isItemUsedInSeedSpreader(item) && !isItemUsedInHavenWorldResourcesCollector(item) && !((ReferenceItem)item.getReferenceItem()).isItemUsedInCraft() && (simpleCriterion == null || simpleCriterion.isValid(this.m_localPlayer, this.m_localPlayer.getPosition(), item, this.m_localPlayer.getEffectContext()));
            }
            if (fieldName.equals("usable")) {
                return item.getReferenceItem().getItemAction() != null || item.isUsableInWorld();
            }
            if (fieldName.equals("usableNow")) {
                return isItemUsable(item);
            }
            if (fieldName.equals("boundDescription")) {
                if (!item.hasBind()) {
                    return null;
                }
                if (item.isBound()) {
                    return WakfuTranslator.getInstance().getString("item.bound");
                }
                final TextWidgetFormater sb2 = new TextWidgetFormater().openText().addColor(Color.RED).append(WakfuTranslator.getInstance().getString(item.getBind().getType().getTranslationKey()));
                return sb2.finishAndToString();
            }
            else {
                if (fieldName.equals("isEmptySlot")) {
                    return false;
                }
                if (fieldName.equals("clickDescription")) {
                    final TextWidgetFormater sb2 = new TextWidgetFormater();
                    if (WakfuGameEntity.getInstance().hasFrame(UIManageFleaFrame.getInstance())) {
                        return WakfuTranslator.getInstance().getString("clickToSell");
                    }
                    final Boolean usable = (Boolean)this.getFieldValue(item, "usable");
                    if (usable) {
                        final boolean valid2 = isItemUsable(item);
                        if (!valid2) {
                            sb2.openText().addColor("ff0000");
                        }
                        sb2.append(WakfuTranslator.getInstance().getString("leftClickToUse"));
                        if (!valid2) {
                            sb2.closeText();
                        }
                    }
                    if (item.getReferenceItem().isEquipment()) {
                        if (usable) {
                            sb2.newLine();
                        }
                        final boolean valid2 = isEquipmentEquipable(item);
                        if (!valid2) {
                            sb2.openText().addColor("ff0000");
                        }
                        sb2.append(WakfuTranslator.getInstance().getString("leftClickToEquip"));
                        if (!valid2) {
                            sb2.closeText();
                        }
                    }
                    if (item.hasPet()) {
                        sb2.newLine();
                        sb2.append(WakfuTranslator.getInstance().getString("rightClickToManagePet"));
                        if (item.getPet().isSleeping()) {
                            sb2.newLine();
                            sb2.openText().addColor("ff0000");
                            sb2.append(WakfuTranslator.getInstance().getString("pet.sleeping"));
                            sb2.closeText();
                        }
                    }
                    final String s2 = sb2.finishAndToString();
                    return s2.isEmpty() ? null : s2;
                }
                else {
                    if (fieldName.equals("effect") || fieldName.equals("shortEffectDetails")) {
                        return this.getEffectsString(item);
                    }
                    if (fieldName.equals("criticalEffectDetails")) {
                        return this.getCriticalEffectsString(item);
                    }
                    if (fieldName.equals("effectAndCaracteristic")) {
                        final ArrayList<String> result = new ArrayList<String>();
                        final ArrayList<String> effectsString = this.getEffectsString(item);
                        if (effectsString != null && !effectsString.isEmpty()) {
                            result.add(WakfuTranslator.getInstance().getString("effectOnUse"));
                            for (final String s3 : effectsString) {
                                result.add(s3);
                            }
                        }
                        final ArrayList<String> caracList = this.getAllCharacteristicsString(item);
                        if (!caracList.isEmpty()) {
                            result.add(WakfuTranslator.getInstance().getString("effectOnEquip"));
                            for (final String s4 : caracList) {
                                result.add(s4);
                            }
                        }
                        return result.isEmpty() ? null : result;
                    }
                    if (fieldName.equals("hasCaracteristic")) {
                        final ArrayList<String> string = this.getCharacteristicsString(item);
                        return string != null && !string.isEmpty();
                    }
                    if (fieldName.equals("caracteristic")) {
                        final ArrayList<String> result = new ArrayList<String>();
                        final ArrayList<String> characs = this.getCharacteristicsString(item);
                        if (!characs.isEmpty()) {
                            result.add(WakfuTranslator.getInstance().getString("effectOnEquip"));
                            for (final String s3 : characs) {
                                result.add(s3);
                            }
                        }
                        return result.isEmpty() ? null : result;
                    }
                    if (fieldName.equals("hasXp")) {
                        return item.hasXp();
                    }
                    if (fieldName.equals("currentXpPercentage")) {
                        if (!item.hasXp()) {
                            return 0.0f;
                        }
                        return item.getXp().getCurrentPercentage();
                    }
                    else if (fieldName.equals("currentXpLevel")) {
                        if (!item.hasXp()) {
                            return 0;
                        }
                        return item.getXp().getLevel();
                    }
                    else if (fieldName.equals("currentXpDescription")) {
                        if (!item.hasXp()) {
                            return null;
                        }
                        final ItemXp xp = item.getXp();
                        return WakfuTranslator.getInstance().getString("xpRatio", xp.getXpTable().getXpInLevel(xp.getXp()), xp.getXpTable().getLevelExtent(xp.getLevel()));
                    }
                    else {
                        if (fieldName.equals("hasPet")) {
                            return item.hasPet();
                        }
                        if (fieldName.equals("isRefItem")) {
                            return false;
                        }
                        if (fieldName.equals("isSortable")) {
                            return this.m_localPlayer.getBags().getBagFromUid(item.getUniqueId()) != null;
                        }
                        if (fieldName.equals("isItemSetMergeable")) {
                            final short setId = item.getReferenceItem().getSetId();
                            final ItemSet itemSet = ItemSetManager.getInstance().getItemSet(setId);
                            return itemSet != null && ItemSetMergeHelper.isItemSetMergeable(itemSet);
                        }
                        if (fieldName.equals("canMergeItemSet")) {
                            if (item.isRent()) {
                                return false;
                            }
                            final short setId = item.getReferenceItem().getSetId();
                            final ItemSet itemSet = ItemSetManager.getInstance().getItemSet(setId);
                            if (itemSet == null) {
                                return false;
                            }
                            if (!ItemSetMergeHelper.isItemSetMergeable(itemSet)) {
                                return false;
                            }
                            return ItemSetMergeHelper.checkInventoryForMerge(this.m_localPlayer, itemSet);
                        }
                        else {
                            if (fieldName.equals("gems")) {
                                return this.getGemDisplayer(item);
                            }
                            if (fieldName.equals("hasInvalidActions")) {
                                return this.hasInvalidActions(item);
                            }
                            if (fieldName.equals("invalidActionsDescription")) {
                                return this.getInvalidActionsDescription(item);
                            }
                            if (fieldName.equals("rentInfoDescription")) {
                                return getRentInfoDescription(item);
                            }
                            if (fieldName.equals("isRent")) {
                                return isRent(item);
                            }
                            if (fieldName.equals("setItems")) {
                                final MergedSetInfo mergedSetItems = item.getMergedSetItems();
                                if (mergedSetItems == null) {
                                    return null;
                                }
                                return mergedSetItems.getItems();
                            }
                            else {
                                if (fieldName.equals("companionLevel")) {
                                    final CompanionItemInfo companionInfo = item.getCompanionInfo();
                                    return (companionInfo == null) ? null : WakfuTranslator.getInstance().getString("companionLevel", CharacterXpTable.getInstance().getLevelByXp(companionInfo.getXp()));
                                }
                                return ((RefItemFieldProvider)item.getReferenceItem()).getFieldValue(fieldName);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static String getRentInfoDescription(final Item item) {
        final RentInfo rentInfo = item.getRentInfo();
        if (rentInfo == null) {
            return "";
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        final int type = rentInfo.getType();
        boolean needNewLine = false;
        switch (type) {
            case 1: {
                final FightLimitedRentInfo fightLimitedRentInfo = (FightLimitedRentInfo)rentInfo;
                final long remainingFights = fightLimitedRentInfo.getRemainingFights();
                sb.append(WakfuTranslator.getInstance().getString("rentInfo.description." + type, remainingFights));
                needNewLine = true;
                break;
            }
            case 2: {
                final TimeLimitedRentInfo timeLimitedRentInfo = (TimeLimitedRentInfo)rentInfo;
                final GameInterval remainingTime = timeLimitedRentInfo.getRemainingTime();
                if (remainingTime.isPositive()) {
                    final String timeDescription = TimeUtils.getShortDescription(remainingTime);
                    sb.append(WakfuTranslator.getInstance().getString("rentInfo.description." + type, timeDescription));
                    needNewLine = true;
                    break;
                }
                break;
            }
        }
        if (rentInfo.isExpired()) {
            if (needNewLine) {
                sb.newLine();
            }
            sb.append(WakfuTranslator.getInstance().getString("rentInfo.description.finished"));
        }
        return sb.finishAndToString();
    }
    
    private static boolean isRent(final Item item) {
        return item.isRent();
    }
    
    private boolean hasInvalidActions(final Item item) {
        final LocalPlayerCharacter localPlayer = this.m_localPlayer;
        final ActionsOnItem[] actionsOnItems = { ActionsOnItem.DELETE, ActionsOnItem.DROP, ActionsOnItem.EXCHANGE };
        if (item.isRent()) {
            return true;
        }
        for (final ActionsOnItem action : actionsOnItems) {
            final SimpleCriterion criterion = item.getReferenceItem().getCriterion(action);
            if (criterion != null && !criterion.isValid(localPlayer, null, null, localPlayer.getOwnContext())) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isItemUsedInHavenWorldResourcesCollector(final Item item) {
        if (!WakfuGameEntity.getInstance().hasFrame(UIHavenWorldResourcesCollectorFrame.getInstance())) {
            return false;
        }
        final AbstractReferenceItem ref = item.getReferenceItem();
        final LocalPlayerCharacter localPlayer = HeroUtils.getHeroWithItemUidFromBagsOrEquipment(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), item.getUniqueId());
        if (localPlayer == null) {
            return false;
        }
        if (ref.getCriterion(ActionsOnItem.DROP) != null && !ref.getCriterion(ActionsOnItem.DROP).isValid(localPlayer, -1, ref, localPlayer.getEffectContext())) {
            return true;
        }
        boolean isGoodType = false;
        for (final short parentTypeId : HavenWorldConstants.RESOURCES_TYPE_IDS) {
            if (item.getType().isChildOf((AbstractItemType<AbstractItemType<AbstractItemType>>)ItemTypeManager.getInstance().getItemType(parentTypeId))) {
                isGoodType = true;
                break;
            }
        }
        return !isGoodType || UIHavenWorldResourcesCollectorFrame.getInstance().isItemUsed(item.getUniqueId());
    }
    
    public static boolean isItemUsedInSeedSpreader(final Item item) {
        return WakfuGameEntity.getInstance().hasFrame(UISeedSpreaderFrame.getInstance()) && (!UISeedSpreaderFrame.getInstance().isMonsterSeedItem(item) || UISeedSpreaderFrame.getInstance().isItemUsed(item.getUniqueId()));
    }
    
    private String getInvalidActionsDescription(final Item item) {
        final LocalPlayerCharacter localPlayer = this.m_localPlayer;
        final ActionsOnItem[] actionsOnItems = { ActionsOnItem.DELETE, ActionsOnItem.DROP, ActionsOnItem.EXCHANGE };
        final TextWidgetFormater sb = new TextWidgetFormater();
        boolean first = true;
        for (final ActionsOnItem action : actionsOnItems) {
            final SimpleCriterion criterion = item.getReferenceItem().getCriterion(action);
            if (criterion != null && !criterion.isValid(localPlayer, null, null, localPlayer.getOwnContext())) {
                if (first) {
                    first = false;
                }
                else {
                    sb.newLine();
                }
                sb.append(WakfuTranslator.getInstance().getString("actionsOnItem.invalid." + action.name()));
            }
            else if ((item.isBound() || item.isRent()) && (action == ActionsOnItem.DROP || action == ActionsOnItem.EXCHANGE)) {
                if (first) {
                    first = false;
                }
                else {
                    sb.newLine();
                }
                sb.append(WakfuTranslator.getInstance().getString("actionsOnItem.invalid." + action.name()));
            }
        }
        return sb.finishAndToString();
    }
    
    private GemsDisplayer getGemDisplayer(final Item item) {
        final GemsDisplayer gemsDisplayer = this.m_gemDisplayers.get(item.getUniqueId());
        if (gemsDisplayer == null) {
            final GemsDisplayer newGemsDisplayer = new GemsDisplayer(item, item.hasGems() ? item.getGems() : Gems.EMPTY);
            if (item.getUniqueId() != 0L) {
                this.m_gemDisplayers.put(item.getUniqueId(), newGemsDisplayer);
            }
            return newGemsDisplayer;
        }
        return gemsDisplayer;
    }
    
    private static ArrayList<String> getEffectsString(final Item item, final TLongObjectHashMap<ArrayList<String>> effects, final CastableDescriptionGenerator.DescriptionMode descriptionMode) {
        ArrayList<String> effectList = effects.get(item.getUniqueId());
        if (effectList == null) {
            effectList = ReferenceItemDisplayer.getEffectsString(descriptionMode, item.getReferenceItem(), item);
            effects.put(item.getUniqueId(), effectList);
        }
        return effectList.isEmpty() ? null : effectList;
    }
    
    private ArrayList<String> getEffectsString(final Item item) {
        return getEffectsString(item, this.m_effects, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY);
    }
    
    private ArrayList<String> getCriticalEffectsString(final Item item) {
        return getEffectsString(item, this.m_criticalEffects, CastableDescriptionGenerator.DescriptionMode.CRITICALS_ONLY);
    }
    
    private ArrayList<String> getCharacteristicsString(final Item item) {
        ArrayList<String> effects = this.m_characteristics.get(item.getUniqueId());
        if (effects == null) {
            effects = ReferenceItemDisplayer.getCharacteristicsString(item.getReferenceItem(), item);
            this.m_characteristics.put(item.getUniqueId(), effects);
        }
        return effects;
    }
    
    private ArrayList<String> getAllCharacteristicsString(final Item item) {
        ArrayList<String> effects = this.m_allCharacteristics.get(item.getUniqueId());
        if (effects == null) {
            effects = ReferenceItemDisplayer.getAllCharacteristicsString(item.getReferenceItem(), item);
            this.m_allCharacteristics.put(item.getUniqueId(), effects);
        }
        return effects;
    }
    
    @Override
    public String getIconUrl(final Item item) {
        final LocalPlayerCharacter localPlayer = (this.m_localPlayer != null) ? this.m_localPlayer : WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuConfiguration.getInstance().getItemSmallIconUrl((localPlayer.getSex() == 1) ? item.getFemaleGfxId() : item.getGfxId());
    }
    
    public static String getBackgroundStyle(final Item item) {
        return getBackgroundStyle(item, "equipmentDialog");
    }
    
    public static String getBackgroundStyle(final Item item, final String elementMapId) {
        final Item itemDetail = (Item)PropertiesProvider.getInstance().getObjectProperty("itemDetail", elementMapId);
        if (itemDetail != null && item.getUniqueId() == 0L && itemDetail.getUniqueId() == 0L) {
            if (item.getReferenceId() == itemDetail.getReferenceId()) {
                return BackgroundStyle.SELECTED.getStyle();
            }
        }
        else if (itemDetail != null && item.getUniqueId() == itemDetail.getUniqueId()) {
            return BackgroundStyle.SELECTED.getStyle();
        }
        if (item.hasPet() && item.getPet().isSleeping()) {
            return BackgroundStyle.FROZEN.getStyle();
        }
        if (!isValid(item)) {
            return BackgroundStyle.DISABLED.getStyle();
        }
        return BackgroundStyle.DEFAULT.getStyle();
    }
    
    public static boolean isValid(final Item item) {
        if (item.isRent() && item.getRentInfo().isExpired()) {
            return false;
        }
        if (item.getReferenceItem().getItemType().getEquipmentPositions().length > 0) {
            return isEquipmentEquipable(item);
        }
        return !item.isUsable() || isItemUsable(item);
    }
    
    public static boolean isItemUsable(final Item item) {
        final LocalPlayerCharacter lpc = HeroUtils.getHeroWithItemUidInBags(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), item.getUniqueId());
        if (lpc == null) {
            return false;
        }
        if (lpc.isDead()) {
            return false;
        }
        final AbstractReferenceItem refItem = item.getReferenceItem();
        final AbstractClientItemAction itemAction = (AbstractClientItemAction)refItem.getItemAction();
        if (item.isRent() && item.getRentInfo().isExpired()) {
            return false;
        }
        if (itemAction != null) {
            if (!itemAction.isRunnable(item)) {
                return false;
            }
            if (itemAction.getType() != null && itemAction.getType() == ItemActionConstants.TELEPORT && lpc.isInPrisonInstance()) {
                return false;
            }
        }
        final AbstractOccupation occupation = lpc.getCurrentOccupation();
        if (occupation != null && occupation.getOccupationTypeId() != 14 && (itemAction == null || !itemAction.canUseDuringOccupation())) {
            return false;
        }
        if (item.getType().getId() == 218 || item.getType().getId() == 399) {
            return true;
        }
        if (lpc.getBags().contains(item) == null) {
            return false;
        }
        final Fight fight = lpc.getCurrentFight();
        if (fight != null) {
            if (!isUsableInFight(item.getReferenceItem(), lpc, fight, item.getUniqueId())) {
                return false;
            }
        }
        else {
            final SimpleCriterion crit = refItem.getCriterion(ActionsOnItem.USE);
            if (!item.isUsableInWorld() || (crit != null && !crit.isValid(lpc, lpc.getPosition(), item, lpc.getEffectContext()))) {
                return false;
            }
        }
        if (itemAction instanceof SplitItemSetItemAction) {
            return true;
        }
        if (itemAction instanceof SeedItemAction) {
            final int levelMin = ((SeedItemAction)itemAction).getLevelMin();
            final CraftHandler craftHandler = lpc.getCraftHandler();
            final int craftId = ((SeedItemAction)itemAction).getCraftId();
            return craftHandler.contains(craftId) && craftHandler.getLevel(craftId) >= levelMin;
        }
        return !isNotAdminXpItem(item) || item.getLevel() <= lpc.getLevel();
    }
    
    public static boolean isUsableInFight(final AbstractReferenceItem item, final CharacterInfo player, final Fight fight, final long uid) {
        final TurnBasedTimeline timeline = fight.getContext().getTimeline();
        final SimpleCriterion crit = item.getCriterion(ActionsOnItem.USE_IN_FIGHT);
        return (!timeline.hasCurrentFighter() || timeline.getCurrentFighterId() == player.getId()) && (!item.isEquipment() || ((ArrayInventoryWithoutCheck<Item, R>)player.getEquipmentInventory()).getWithUniqueId(uid) != null) && (item.isUsableInFight() || fight.getStatus() == AbstractFight.FightStatus.PLACEMENT) && (crit == null || crit.isValid(player, player.getPosition(), item, player.getEffectContext()));
    }
    
    private static boolean isNotAdminXpItem(final Item item) {
        final AbstractReferenceItem referenceItem = item.getReferenceItem();
        return referenceItem == null || !referenceItem.hasItemProperty(ItemProperty.ADMIN_XP);
    }
    
    private static boolean isEquipmentEquipable(final Item equipment) {
        assert equipment.getReferenceItem().isEquipment() : "On appelle isEquipmentEquipable avec un item qui n'est pas un equipment : " + equipment.getName();
        final BasicCharacterInfo equipmentOwner = getEquipmentOwner(equipment);
        return equipmentOwner != null && EquipmentInventoryChecker.getInstance().checkCriterion(equipment, (EffectUser)equipmentOwner, (EffectContext)equipmentOwner.getAppropriateContext());
    }
    
    private static BasicCharacterInfo getEquipmentOwner(final Item equipment) {
        final LocalPlayerCharacter lpc = WakfuGameEntity.getInstance().getLocalPlayer();
        if (lpc.getFromEquipmentOrInventory(equipment.getUniqueId()) != null) {
            return lpc;
        }
        return getEquipmentOwnerFromCompanions(equipment);
    }
    
    private static BasicCharacterInfo getEquipmentOwnerFromCompanions(final Item equipment) {
        final long accountId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
        final List<CompanionModel> companions = CompanionManager.INSTANCE.getCompanions(accountId);
        BasicCharacterInfo equipmentOwner = null;
        for (final CompanionModel companion : companions) {
            if (!((ArrayInventoryWithoutCheck<Item, R>)companion.getItemEquipment()).contains(equipment)) {
                continue;
            }
            final CharacterView characterSheetView = UICompanionsEmbeddedFrame.getCharacterSheetView(companion.getBreedId());
            if (characterSheetView == null) {
                equipmentOwner = UICompanionsEmbeddedFrame.createNonPlayerCharacterCompanion(companion);
                break;
            }
            equipmentOwner = ((characterSheetView.getCharacterInfo() != null) ? characterSheetView.getCharacterInfo() : UICompanionsEmbeddedFrame.createNonPlayerCharacterCompanion(companion));
            break;
        }
        return equipmentOwner;
    }
    
    private static boolean isLevelValid(final Item item) {
        final LocalPlayerCharacter lpc = UIEquipmentFrame.getCharacter();
        if (lpc == null) {
            return true;
        }
        final AbstractReferenceItem referenceItem = item.getReferenceItem();
        if (referenceItem.getItemType().getEquipmentPositions().length > 0) {
            final EquipmentInventoryChecker checker = (EquipmentInventoryChecker)lpc.getEquipmentInventory().getContentChecker();
            if (checker != null) {
                return EquipmentInventoryChecker.checkItemLevelValidity(item, lpc);
            }
        }
        else {
            if (referenceItem.getItemAction() instanceof SeedItemAction) {
                final int levelMin = ((SeedItemAction)referenceItem.getItemAction()).getLevelMin();
                return lpc.getCraftHandler().getLevel(((SeedItemAction)referenceItem.getItemAction()).getCraftId()) >= levelMin;
            }
            if (referenceItem.getLevel() > lpc.getLevel()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String getName(final Item item) {
        final AbstractReferenceItem referenceItem = item.getReferenceItem();
        return (referenceItem != null) ? referenceItem.getName() : "<undefined name>";
    }
    
    @Override
    public String getDescription(final Item item) {
        final AbstractReferenceItem referenceItem = item.getReferenceItem();
        return (referenceItem != null) ? referenceItem.getDescription() : "<undefined desc>";
    }
    
    @Override
    public void cleanUp(final Item item) {
        final ArrayList<Property> toRemove = new ArrayList<Property>();
        for (final String propertyName : this.ITEM_PROPERTIES) {
            final Property property = PropertiesProvider.getInstance().getProperty(propertyName);
            if (property != null) {
                if (property.getValue() == item && !property.hasParent()) {
                    toRemove.add(property);
                }
            }
        }
        for (int i = 0, size = toRemove.size(); i < size; ++i) {
            PropertiesProvider.getInstance().removeProperty(toRemove.get(i));
        }
    }
    
    @Override
    public void resetCache(final Item item) {
        this.m_gemDisplayers.remove(item.getUniqueId());
        this.m_characteristics.remove(item.getUniqueId());
        this.m_allCharacteristics.remove(item.getUniqueId());
        this.m_effects.remove(item.getUniqueId());
        this.m_criticalEffects.remove(item.getUniqueId());
        final RefItemFieldProvider referenceItem = (RefItemFieldProvider)item.getReferenceItem();
        if (referenceItem != null) {
            referenceItem.getReferenceItemDisplayer().resetCache();
        }
        try {
            PropertiesProvider.getInstance().firePropertyValueChanged(item, "isEquipable", "requirement", "effect", "caracteristic", "isEquiped", "isInInventory", "effectAndCaracteristic", "gems");
        }
        catch (Exception e) {
            ItemDisplayerImpl.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    public static String getEffectsString(final Iterator<WakfuEffect> effects, final short level, final boolean isFromStateDescription) {
        final StringBuilder caracteristics = new StringBuilder();
        while (effects.hasNext()) {
            final WakfuEffect effect = effects.next();
            final String caracText = WakfuTranslator.getInstance().getStringWithoutFormat(10, effect.getActionId());
            if (effect.getActionId() == RunningEffectConstants.NULL_EFFECT.getId()) {
                continue;
            }
            final int effectParamsCount = effect.getParamsCount();
            final Object[] params = new Object[effectParamsCount];
            for (int i = 0; i < effectParamsCount; ++i) {
                params[i] = effect.getParam(i, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            }
            StateClient state = null;
            SpellLevel spellLevel = null;
            if (effect.getActionId() == RunningEffectConstants.STATE_APPLY.getId() || effect.getActionId() == RunningEffectConstants.STATE_FORCE_UNAPPLY.getId()) {
                state = (StateClient)StateManager.getInstance().getState((int)params[0]);
                short stateLevel = 0;
                if (params.length > 1) {
                    stateLevel = (short)params[1];
                }
                params[0] = CastableDescriptionGenerator.getStateNameLink(state, stateLevel, (short)effect.getContainerMaxLevel(), true);
            }
            else if (effect.getActionId() != RunningEffectConstants.VARIABLE_ELEMENTS_DAMAGE_GAIN.getId()) {
                if (effect.getActionId() != RunningEffectConstants.VARIABLE_ELEMENTS_RES_GAIN.getId()) {
                    if (effect.getActionId() == RunningEffectConstants.SPELL_BOOST_LEVEL.getId()) {
                        final int spellId = (int)params[0];
                        spellLevel = WakfuGameEntity.getInstance().getLocalPlayer().getSpellInventory().getFirstWithReferenceId(spellId);
                        if (spellLevel == null) {
                            spellLevel = new SpellLevel(SpellManager.getInstance().getSpell(spellId), (short)0, 0L);
                        }
                    }
                }
            }
            final String effectString = StringFormatter.format(caracText, params);
            if (state != null && !isFromStateDescription) {
                String stateEffectsDescription = getEffectsString(state.iterator(), state.getLevel(), true);
                stateEffectsDescription += ",";
                caracteristics.append(StringFormatter.format(caracText, params)).append(" (").append(stateEffectsDescription).append(")");
            }
            else if (spellLevel != null) {
                final short level2 = spellLevel.getLevel();
                spellLevel.setLevel(level, false);
                caracteristics.append(spellLevel.getSpellLevelDescription(CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY));
                spellLevel.setLevel(level2, false);
            }
            else {
                caracteristics.append(effectString);
            }
            if (effect.getEffectType() == 2) {
                final WakfuFightEffect feffect = (WakfuFightEffect)effect;
                final RelativeFightTimeInterval effectDuration = feffect.getDuration(level);
                if (effectDuration.getTableTurnsFromNow() < 0) {
                    caracteristics.append(' ').append(WakfuTranslator.getInstance().getString("cast.infiniteDuration"));
                }
                else if (effectDuration.getTableTurnsFromNow() > 0) {
                    caracteristics.append(' ').append(WakfuTranslator.getInstance().getString("remaining.duration.turn", effectDuration.getTableTurnsFromNow()));
                }
            }
            else if (effect.getEffectType() == 1) {
                final WakfuWorldEffectImpl wakfuWorldEffect = (WakfuWorldEffectImpl)effect;
                if (state != null) {
                    int inMs = wakfuWorldEffect.getDurationInMs(state.getLevel());
                    if (inMs <= 0) {
                        inMs = state.getMsDuration();
                    }
                    if (inMs > 0) {
                        caracteristics.append(' ').append(TimeUtils.getVeryShortDescription(GameInterval.fromSeconds(inMs / 1000)));
                    }
                }
            }
            final Elements element = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId()).getElement();
            if (element != null) {
                try {
                    final String elementUrl = String.format(WakfuConfiguration.getInstance().getString("elementsSmallIconsPath"), element.name());
                    caracteristics.append(" ").append(TextUtils.getImageTag(elementUrl, -1, -1, null));
                }
                catch (PropertyException e) {
                    ItemDisplayerImpl.m_logger.error((Object)e.toString());
                }
            }
            if (!effects.hasNext()) {
                continue;
            }
            caracteristics.append("\r\n");
        }
        return caracteristics.toString();
    }
    
    static {
        FIELDS = new String[] { "isActive", "quantity", "levelDescription", "movable", "usableInFight", "backgroundStyle", "isEquiped", "usedInCurrentRecipe", "deletable", "usable", "usableNow", "clickDescription", "isEmptySlot", "hasXp", "currentXpPercentage", "currentXpDescription", "currentXpLevel", "hasPet", "isRefItem", "canMergeItemSet", "companionLevel", "setItems" };
        m_logger = Logger.getLogger((Class)ItemDisplayerImpl.class);
    }
    
    public enum BackgroundStyle
    {
        FROZEN("itemPetFrozenBackground"), 
        SELECTED("itemSelectedBackground"), 
        DISABLED("itemVirtualBackground"), 
        DEFAULT("itemBackground");
        
        private final String m_style;
        
        private BackgroundStyle(final String style) {
            this.m_style = style;
        }
        
        public String getStyle() {
            return this.m_style;
        }
    }
    
    public static class DisabledItem extends ImmutableFieldProvider
    {
        private final FieldProvider m_item;
        
        public DisabledItem(final FieldProvider item) {
            super();
            this.m_item = item;
        }
        
        @Override
        public String[] getFields() {
            return null;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("isEnabled")) {
                return false;
            }
            return this.m_item.getFieldValue(fieldName);
        }
        
        public FieldProvider getItem() {
            return this.m_item;
        }
    }
    
    public static class ItemPlaceHolder extends ImmutableFieldProvider
    {
        @Override
        public String[] getFields() {
            return new String[0];
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("isEmptySlot")) {
                return true;
            }
            if (fieldName.equals("isEnabled")) {
                return true;
            }
            return null;
        }
    }
}
