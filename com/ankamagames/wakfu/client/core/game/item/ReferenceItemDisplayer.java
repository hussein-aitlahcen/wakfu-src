package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.bind.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.wakfu.common.game.craft.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.xulor2.util.text.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import gnu.trove.*;

public class ReferenceItemDisplayer implements FieldProvider
{
    private static final Logger m_logger;
    public static final String NAME_FIELD = "name";
    public static final String NAME_WITH_RARITY_FIELD = "nameWithRarity";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String TYPE_FIELD = "type";
    public static final String ITEM_TYPE_COLOR_FIELD = "itemTypeColor";
    public static final String IS_REF_ITEM = "isRefItem";
    public static final String SET_FIELD = "set";
    public static final String IS_EQUIPED = "isEquiped";
    public static final String IS_EQUIPABLE_TYPE = "isEquipableType";
    public static final String IS_EQUIPABLE = "isEquipable";
    public static final String IS_PREVIEWABLE = "isPreviewable";
    public static final String IS_IN_INVENTORY = "isInInventory";
    public static final String LEVEL_FIELD = "level";
    public static final String LEVEL_DESCRIPTION_FIELD = "levelDescription";
    public static final String EFFECT_FIELD = "effect";
    public static final String EFFECT_AND_CARACTERISTIC_FIELD = "effectAndCaracteristic";
    public static final String EFFECT_FIELD_DETAILS = "effectDetails";
    public static final String CRITICAL_EFFECT_FIELD = "criticalEffectDetails";
    public static final String SHORT_EFFECT_FIELD_DETAILS = "shortEffectDetails";
    public static final String HAS_CARACTERISTIQUE_FIELD = "hasCaracteristic";
    public static final String CARACTERISTIQUE_FIELD = "caracteristic";
    public static final String REQUIREMENT_FIELD = "requirement";
    public static final String USE_REQUIREMENT_FIELD = "useRequirement";
    public static final String CRAFT_REQUIREMENT_FIELD = "craftRequirement";
    public static final String CONDITION_DESCRIPTION_FIELD = "conditionDescription";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String ICON_BIG_URL_FIELD = "iconBigUrl";
    public static final String AP_FIELD = "ap";
    public static final String AP_DESC_FIELD = "apDesc";
    public static final String RANGE_FIELD = "range";
    public static final String AREA_OF_EFFECT_ICON_URL_FIELD = "areaOfEffectIconURL";
    public static final String IS_ACTION_LINKED = "isActionLinked";
    public static final String INGREDIENTS_FIELD = "ingredients";
    public static final String STORING_CAPACITY_FIELD = "storingCapacity";
    public static final String CRAFT_MOVABLE_FIELD = "craftMovable";
    public static final String IS_PERFECT_FIELD = "isPerfect";
    public static final String IS_RELIC_FIELD = "isRelic";
    public static final String IS_RELIC2_FIELD = "isRelic2";
    public static final String IS_CRAFTABLE_FIELD = "isCraftable";
    public static final String IS_USED_IN_CRAFT = "isUsedInCraft";
    public static final String CRAFTS_USED_INTO_DESCRIPTION = "craftsUsedIntoDescription";
    public static final String HAS_CRAFTS_USING_ITEM = "hasCraftsUsingItem";
    public static final String HAS_XP = "hasXp";
    public static final String GEMS = "gems";
    public static final String ITEM_USE_STYLE = "itemUseStyle";
    public static final String ITEM_USE_DESCRIPTION = "itemUseDescription";
    public static final String HAS_HEAL_EFFECT = "hasHealEffect";
    public static final String CAN_BE_EQUIPED_FIELD = "canBeEquiped";
    public static final String BOUND_DESCRIPTION_FIELD = "boundDescription";
    public static final String DROP_QUANTITY_FIELD = "dropQuantity";
    public static final String[] FIELDS;
    private AbstractReferenceItem<ItemType> m_referenceItem;
    private ArrayList<String> m_effectsString;
    private ArrayList<String> m_criticalEffectsCache;
    private String m_areaOfEffectString;
    private ArrayList<String> m_characteristicsString;
    private ArrayList<String> m_allCharacteristicsString;
    private GemsDisplayer m_gemsDisplayer;
    
    @Override
    public String[] getFields() {
        return ReferenceItemDisplayer.FIELDS;
    }
    
    public ReferenceItemDisplayer(final AbstractReferenceItem<ItemType> referenceItem) {
        super();
        this.m_effectsString = null;
        this.m_criticalEffectsCache = null;
        this.m_areaOfEffectString = null;
        this.m_characteristicsString = null;
        this.m_allCharacteristicsString = null;
        this.m_referenceItem = referenceItem;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isRefItem")) {
            return true;
        }
        if (fieldName.equals("name")) {
            return this.m_referenceItem.getName();
        }
        if (fieldName.equals("nameWithRarity")) {
            return new TextWidgetFormater().openText().addColor(this.m_referenceItem.getRarity().getColor().getRGBtoHex()).append(this.m_referenceItem.getName()).closeText().finishAndToString();
        }
        if (fieldName.equals("itemTypeColor")) {
            return this.m_referenceItem.getType().getColor().toString();
        }
        if (fieldName.equals("description")) {
            final String s = this.m_referenceItem.getDescription();
            if (s == null || s.length() == 0) {
                return null;
            }
            return "\"" + s + "\"";
        }
        else {
            if (fieldName.equals("type")) {
                final String type = ItemTypeManager.getInstance().getItemType(this.m_referenceItem.getItemType().getId()).getName();
                final boolean isTwoHands = false;
                final String retour = type;
                return (retour.length() == 0) ? null : ("[" + retour + "]");
            }
            if (fieldName.equals("level")) {
                return this.m_referenceItem.getLevel();
            }
            if (fieldName.equals("levelDescription")) {
                final LocalPlayerCharacter lpc = UIEquipmentFrame.getCharacter();
                if (lpc == null) {
                    return WakfuTranslator.getInstance().getString("levelShort.custom", this.m_referenceItem.getLevel());
                }
                final EquipmentInventoryChecker checker = (EquipmentInventoryChecker)lpc.getEquipmentInventory().getContentChecker();
                if (checker == null) {
                    return WakfuTranslator.getInstance().getString("levelShort.custom", this.m_referenceItem.getLevel());
                }
                final TextWidgetFormater sb = new TextWidgetFormater();
                boolean valid = true;
                if (this.m_referenceItem.getItemType().getEquipmentPositions().length == 0) {
                    if (this.m_referenceItem.getItemAction() instanceof SeedItemAction) {
                        final int levelMin = ((SeedItemAction)this.m_referenceItem.getItemAction()).getLevelMin();
                        valid = (lpc.getCraftHandler().getLevel(((SeedItemAction)this.m_referenceItem.getItemAction()).getCraftId()) >= levelMin);
                    }
                }
                else {
                    valid = EquipmentInventoryChecker.checkItemLevelValidity(this.m_referenceItem, lpc);
                }
                if (!valid) {
                    sb.openText().addColor(Color.RED.getRGBtoHex());
                }
                sb.append(WakfuTranslator.getInstance().getString("levelShort.custom", this.m_referenceItem.getLevel()));
                if (!valid) {
                    sb.closeText();
                }
                return sb.finishAndToString();
            }
            else {
                if (fieldName.equals("isEquipableType")) {
                    return this.isEquipableType();
                }
                if (fieldName.equals("isEquipable")) {
                    return this.isEquipable();
                }
                if (fieldName.equals("isPreviewable")) {
                    return this.m_referenceItem.getItemType().getEquipmentPositions().length > 0;
                }
                if (fieldName.equals("isEquiped")) {
                    final LocalPlayerCharacter lpc = UIEquipmentFrame.getCharacter();
                    final ItemEquipment itemList = lpc.getEquipmentInventory();
                    int metaId = -1;
                    if (this.m_referenceItem instanceof SubMetaItem) {
                        metaId = ((SubMetaItem)this.m_referenceItem).getMetaParent().getId();
                    }
                    for (final Item item : itemList) {
                        if (item.getReferenceItem().getId() == this.m_referenceItem.getId() || (item.getReferenceItem() instanceof SubMetaItem && metaId == ((SubMetaItem)item.getReferenceItem()).getMetaParent().getId())) {
                            return true;
                        }
                    }
                    return false;
                }
                if (fieldName.equals("isInInventory")) {
                    final LocalPlayerCharacter lpc = UIEquipmentFrame.getCharacter();
                    final TLongObjectIterator<AbstractBag> it = lpc.getBags().getBagsIterator();
                    while (it.hasNext()) {
                        it.advance();
                        final AbstractBag bag = it.value();
                        if (bag.containsReferenceId(this.m_referenceItem.getId())) {
                            return true;
                        }
                    }
                    return false;
                }
                if (fieldName.equals("set")) {
                    final short setId = this.m_referenceItem.getSetId();
                    if (setId != 0) {
                        return ItemSetManager.getInstance().getItemSet(setId);
                    }
                    return null;
                }
                else if (fieldName.equals("apDesc")) {
                    final int ap = this.m_referenceItem.getActionPoints();
                    if (ap > 0) {
                        return "<b color=\"00afea\">" + this.m_referenceItem.getActionPoints() + " " + WakfuTranslator.getInstance().getString("APShort") + "</b>";
                    }
                    return null;
                }
                else {
                    if (fieldName.equals("ap")) {
                        return this.m_referenceItem.getActionPoints();
                    }
                    if (fieldName.equals("range")) {
                        final int minRange = this.m_referenceItem.getUseRangeMin();
                        final int maxRange = this.m_referenceItem.getUseRangeMax();
                        if (minRange == 0 && maxRange == 0) {
                            return null;
                        }
                        if (minRange == maxRange) {
                            return minRange;
                        }
                        return minRange + "-" + maxRange;
                    }
                    else {
                        if (fieldName.equalsIgnoreCase("areaOfEffectIconURL")) {
                            return this.getAreaOfEffectString();
                        }
                        if (fieldName.equals("hasCaracteristic")) {
                            return !this.getCharacteristicsString().isEmpty();
                        }
                        if (fieldName.equals("caracteristic")) {
                            final ArrayList<String> result = new ArrayList<String>();
                            final ArrayList<String> characs = this.getCharacteristicsString();
                            if (!characs.isEmpty()) {
                                result.add(WakfuTranslator.getInstance().getString("effectOnEquip"));
                                for (final String s2 : characs) {
                                    result.add(s2);
                                }
                            }
                            return result.isEmpty() ? null : result;
                        }
                        if (fieldName.equals("effect")) {
                            final ArrayList<String> effectsString = this.getEffectsString();
                            return effectsString.isEmpty() ? null : effectsString;
                        }
                        if (fieldName.equals("effectAndCaracteristic")) {
                            return this.getEffectAndCaracteristicString(null);
                        }
                        if (fieldName.equals("effectDetails")) {
                            return getEffectsString(CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY, this.m_referenceItem, null);
                        }
                        if (fieldName.equals("shortEffectDetails")) {
                            return getEffectsString(CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY, this.m_referenceItem, null);
                        }
                        if (fieldName.equals("criticalEffectDetails")) {
                            if (this.m_criticalEffectsCache == null) {
                                this.m_criticalEffectsCache = getEffectsString(CastableDescriptionGenerator.DescriptionMode.CRITICALS_ONLY, this.m_referenceItem, null);
                            }
                            return this.m_criticalEffectsCache.isEmpty() ? null : this.m_criticalEffectsCache;
                        }
                        if (fieldName.equals("requirement")) {
                            final StringBuilder criterion = new StringBuilder();
                            final SimpleCriterion criterions = this.m_referenceItem.getCriterion(ActionsOnItem.EQUIP);
                            final LocalPlayerCharacter lpc2 = UIEquipmentFrame.getCharacter();
                            if (criterions != null) {
                                final String criterionString = CriterionDescriptionGenerator.getDescription(criterions);
                                if (criterionString.length() > 0) {
                                    if (criterions.isValid(lpc2, this, null, lpc2.getEffectContext())) {
                                        criterion.append(criterionString);
                                    }
                                    else {
                                        criterion.append("<text color=\"cc4444\">").append(criterionString).append("</text>");
                                    }
                                }
                            }
                            return criterion.toString();
                        }
                        if (fieldName.equals("useRequirement")) {
                            boolean first = true;
                            final LocalPlayerCharacter lpc2 = UIEquipmentFrame.getCharacter();
                            final TextWidgetFormater sb2 = new TextWidgetFormater();
                            SimpleCriterion crit;
                            if (lpc2.getCurrentFight() == null) {
                                crit = this.m_referenceItem.getCriterion(ActionsOnItem.USE);
                            }
                            else {
                                crit = this.m_referenceItem.getCriterion(ActionsOnItem.USE_IN_FIGHT);
                            }
                            if (crit != null) {
                                final String criterionString2 = CriterionDescriptionGenerator.getDescription(crit);
                                if (crit.isValid(lpc2, this, null, null)) {
                                    sb2.append(criterionString2);
                                }
                                else {
                                    sb2.openText().addColor("cc4444").append(criterionString2).closeText();
                                }
                                first = false;
                            }
                            crit = null;
                            final AbstractItemAction action = this.m_referenceItem.getItemAction();
                            if (action != null) {
                                crit = action.getCriterion();
                            }
                            if (crit != null) {
                                if (!first) {
                                    sb2.newLine();
                                }
                                final String criterionString3 = CriterionDescriptionGenerator.getDescription(crit);
                                if (crit.isValid(lpc2, this, null, null)) {
                                    sb2.append(criterionString3);
                                }
                                else {
                                    sb2.openText().addColor("cc4444").append(criterionString3).closeText();
                                }
                                first = false;
                            }
                            final AbstractClientItemAction itemAction = (AbstractClientItemAction)this.m_referenceItem.getItemAction();
                            if (itemAction != null && itemAction instanceof SeedItemAction) {
                                if (!first) {
                                    sb2.newLine();
                                }
                                final int levelMin2 = ((SeedItemAction)itemAction).getLevelMin();
                                final int craftId = ((SeedItemAction)itemAction).getCraftId();
                                final String craftName = WakfuTranslator.getInstance().getString(43, craftId, new Object[0]);
                                final String value = WakfuTranslator.getInstance().getString("craft.neededAtLevel", craftName, levelMin2);
                                sb2.append(value);
                                first = false;
                            }
                            if (itemAction != null) {
                                final String additionalRequirementDescription = itemAction.getAdditionalRequirementDescription();
                                if (additionalRequirementDescription != null) {
                                    sb2.openText().addColor("cc4444").append(additionalRequirementDescription).closeText();
                                }
                            }
                            return sb2.finishAndToString();
                        }
                        if (fieldName.equals("conditionDescription")) {
                            final LocalPlayerCharacter lpc = UIEquipmentFrame.getCharacter();
                            final ArrayList<String> conditions = new ArrayList<String>();
                            SimpleCriterion criterions2 = this.m_referenceItem.getCriterion(ActionsOnItem.EQUIP);
                            if (criterions2 != null) {
                                this.addCriterion(criterions2, conditions, lpc.getEffectContext());
                            }
                            if (lpc.getCurrentFight() == null) {
                                criterions2 = this.m_referenceItem.getCriterion(ActionsOnItem.USE);
                            }
                            else {
                                criterions2 = this.m_referenceItem.getCriterion(ActionsOnItem.USE_IN_FIGHT);
                            }
                            if (criterions2 != null) {
                                this.addCriterion(criterions2, conditions, null);
                            }
                            criterions2 = null;
                            final AbstractItemAction action2 = this.m_referenceItem.getItemAction();
                            if (action2 != null) {
                                criterions2 = action2.getCriterion();
                            }
                            if (criterions2 != null) {
                                this.addCriterion(criterions2, conditions, null);
                            }
                            final AbstractClientItemAction itemAction2 = (AbstractClientItemAction)this.m_referenceItem.getItemAction();
                            if (itemAction2 != null && itemAction2 instanceof SeedItemAction) {
                                final int levelMin3 = ((SeedItemAction)itemAction2).getLevelMin();
                                final int craftId2 = ((SeedItemAction)itemAction2).getCraftId();
                                final boolean valid2 = lpc.getCraftHandler().contains(craftId2) && lpc.getCraftHandler().getLevel(craftId2) >= levelMin3;
                                final String craftName = WakfuTranslator.getInstance().getString(43, craftId2, new Object[0]);
                                String value = WakfuTranslator.getInstance().getString("craft.neededAtLevel", craftName, levelMin3);
                                if (!valid2) {
                                    value = "<text color=\"cc4444\">" + value + "</text>";
                                }
                                conditions.add(value);
                            }
                            if (itemAction2 != null) {
                                final String additionalRequirementDescription2 = itemAction2.getAdditionalRequirementDescription();
                                if (additionalRequirementDescription2 != null) {
                                    conditions.add("<text color=\"cc4444\">" + additionalRequirementDescription2 + "</text>");
                                }
                            }
                            return conditions;
                        }
                        if (fieldName.equals("iconUrl")) {
                            final LocalPlayerCharacter lpc = UIEquipmentFrame.getCharacter();
                            return WakfuConfiguration.getInstance().getItemSmallIconUrl((lpc.getSex() == 1) ? this.m_referenceItem.getFemaleGfxId() : this.m_referenceItem.getGfxId());
                        }
                        if (fieldName.equals("iconBigUrl")) {
                            final LocalPlayerCharacter lpc = UIEquipmentFrame.getCharacter();
                            return WakfuConfiguration.getInstance().getItemBigIconUrl((lpc.getSex() == 1) ? this.m_referenceItem.getFemaleGfxId() : this.m_referenceItem.getGfxId());
                        }
                        if (fieldName.equals("isActionLinked")) {
                            return this.m_referenceItem.getItemAction() != null;
                        }
                        if (fieldName.equals("storingCapacity")) {
                            return BagStoringManager.INSTANCE.getCapacity(this.m_referenceItem.getId());
                        }
                        if (fieldName.equals("craftRequirement")) {
                            final TextWidgetFormater sb3 = new TextWidgetFormater();
                            final SimpleCriterion criterions = this.m_referenceItem.getCriterion(ActionsOnItem.CRAFT);
                            if (criterions != null) {
                                final LocalPlayerCharacter lpc2 = UIEquipmentFrame.getCharacter();
                                final String criterionString = CriterionDescriptionGenerator.getDescription(criterions);
                                if (criterions.isValid(lpc2, this, null, lpc2.getEffectContext())) {
                                    sb3.append(criterionString);
                                }
                                else {
                                    sb3.openText().addColor("cc4444").append(criterionString).closeText();
                                }
                            }
                            return sb3.finishAndToString();
                        }
                        if (!fieldName.equals("ingredients")) {
                            if (fieldName.equals("craftMovable")) {
                                return this.m_referenceItem.isCraftEnabled();
                            }
                            if (fieldName.equals("isPerfect")) {
                                return this.isPerfect();
                            }
                            if (fieldName.equals("isRelic")) {
                                return this.m_referenceItem.getRarity() == ItemRarity.RELIC;
                            }
                            if (fieldName.equals("isRelic2")) {
                                return this.m_referenceItem.getRarity() == ItemRarity.RELIC2;
                            }
                            if (fieldName.equals("isCraftable")) {
                                return this.isCraftable();
                            }
                            if (fieldName.equals("isUsedInCraft")) {
                                return IngredientManager.INSTANCE.getCraftsUsing(this.m_referenceItem.getId()) != null;
                            }
                            if (fieldName.equals("hasCraftsUsingItem")) {
                                final TIntHashSet set = IngredientManager.INSTANCE.getCraftsUsing(this.m_referenceItem.getId());
                                if (set == null) {
                                    return null;
                                }
                                final int[] array = set.toArray();
                                Arrays.sort(array);
                                final LocalPlayerCharacter lpc2 = UIEquipmentFrame.getCharacter();
                                final CraftHandler craftHandler = lpc2.getCraftHandler();
                                for (int i = 0; i < array.length; ++i) {
                                    if (craftHandler.contains(array[i])) {
                                        return true;
                                    }
                                }
                                return false;
                            }
                            else if (fieldName.equals("craftsUsedIntoDescription")) {
                                final TIntHashSet set = IngredientManager.INSTANCE.getCraftsUsing(this.m_referenceItem.getId());
                                if (set == null) {
                                    return null;
                                }
                                final int[] array = set.toArray();
                                Arrays.sort(array);
                                final LocalPlayerCharacter lpc2 = UIEquipmentFrame.getCharacter();
                                final CraftHandler craftHandler = lpc2.getCraftHandler();
                                final TextWidgetFormater sb4 = new TextWidgetFormater();
                                sb4.append(WakfuTranslator.getInstance().getString("crafts.item.isUsedIn")).newLine();
                                for (int j = 0, size = array.length; j < size; ++j) {
                                    if (j != 0) {
                                        sb4.append(", ");
                                    }
                                    final boolean craftKnown = craftHandler.contains(array[j]);
                                    if (!craftKnown) {
                                        sb4.openText().addColor(Color.RED.getRGBtoHex());
                                    }
                                    sb4.append(WakfuTranslator.getInstance().getString(43, array[j], new Object[0]));
                                    if (!craftKnown) {
                                        sb4.closeText();
                                    }
                                }
                                return sb4.toString();
                            }
                            else {
                                if (fieldName.equals("hasXp")) {
                                    return false;
                                }
                                if (fieldName.equals("gems")) {
                                    return this.getGemDisplayer();
                                }
                                if (fieldName.equals("itemUseStyle")) {
                                    final ReferenceItem refItem = (ReferenceItem)this.m_referenceItem;
                                    return refItem.getActionVisual().getStyle();
                                }
                                if (fieldName.equals("itemUseDescription")) {
                                    final ReferenceItem refItem = (ReferenceItem)this.m_referenceItem;
                                    return WakfuTranslator.getInstance().getString(refItem.getActionVisual().getTranslationKey());
                                }
                                if (fieldName.equals("hasHealEffect")) {
                                    return this.m_referenceItem.hasHealEffect();
                                }
                                if (fieldName.equals("boundDescription")) {
                                    if (this.m_referenceItem.getBindType() == ItemBindType.NOT_BOUND) {
                                        return null;
                                    }
                                    final TextWidgetFormater sb3 = new TextWidgetFormater().openText().addColor(Color.RED).append(WakfuTranslator.getInstance().getString(this.m_referenceItem.getBindType().getTranslationKey()));
                                    return sb3.finishAndToString();
                                }
                                else if (fieldName.equals("canBeEquiped")) {
                                    return this.m_referenceItem.canBeEquiped();
                                }
                            }
                        }
                        return null;
                    }
                }
            }
        }
    }
    
    private void addCriterion(final SimpleCriterion criterions, final ArrayList<String> conditions, final Object criterionContext) {
        final String criterionString = CriterionDescriptionGenerator.getDescription(criterions);
        if (criterionString.length() <= 0) {
            return;
        }
        final String[] strings = StringUtils.split(criterionString, '\n');
        final LocalPlayerCharacter lpc = UIEquipmentFrame.getCharacter();
        for (String s2 : strings) {
            if (!criterions.isValid(lpc, this, null, criterionContext)) {
                s2 = "<text color=\"cc4444\">" + s2 + "</text>";
            }
            conditions.add(s2);
        }
    }
    
    private GemsDisplayer getGemDisplayer() {
        if (this.m_gemsDisplayer == null) {
            this.m_gemsDisplayer = new GemsDisplayer(null, this.m_referenceItem.isGemmable() ? new Gems(this.m_referenceItem) : Gems.EMPTY);
        }
        return this.m_gemsDisplayer;
    }
    
    public ArrayList<String> getEffectAndCaracteristicString(final Item realItem) {
        final ArrayList<String> result = new ArrayList<String>();
        final ArrayList<String> effectsString = getEffectsString(CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY, this.m_referenceItem, realItem);
        if (!effectsString.isEmpty()) {
            result.add(WakfuTranslator.getInstance().getString("effectOnUse"));
            for (final String s : effectsString) {
                result.add(s);
            }
        }
        final ArrayList<String> caracList = getAllCharacteristicsString(this.m_referenceItem, realItem);
        if (!caracList.isEmpty()) {
            result.add(WakfuTranslator.getInstance().getString("effectOnEquip"));
            for (final String s2 : caracList) {
                result.add(s2);
            }
        }
        return result.isEmpty() ? null : result;
    }
    
    private boolean isCraftable() {
        return ((ItemManagerImpl)ReferenceItemManager.getInstance()).isCraftableItem(this.m_referenceItem.getId());
    }
    
    public boolean isPerfect() {
        if (this.m_referenceItem.getMetaType() != ItemMetaType.SUB_META_ITEM) {
            return false;
        }
        final SubMetaItem subMetaItem = (SubMetaItem)this.m_referenceItem;
        return subMetaItem.isPerfectItem();
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
    
    public boolean isEquipable() {
        if (this.m_referenceItem.getItemType().getEquipmentPositions().length > 0) {
            final TLongObjectIterator<AbstractBag> it = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getBagsIterator();
            while (it.hasNext()) {
                it.advance();
                final AbstractBag bag = it.value();
                if (bag.containsReferenceId(this.m_referenceItem.getId())) {
                    final SimpleCriterion criterions = this.m_referenceItem.getCriterion(ActionsOnItem.EQUIP);
                    if (criterions == null || criterions.isValid(WakfuGameEntity.getInstance().getLocalPlayer(), this, null, WakfuGameEntity.getInstance().getLocalPlayer().getEffectContext())) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }
    
    public boolean isEquipableType() {
        return this.m_referenceItem.getItemType().getEquipmentPositions().length > 0;
    }
    
    public String getAreaOfEffectString() {
        if (this.m_areaOfEffectString == null) {
            final Iterator<WakfuEffect> effectList = this.m_referenceItem.getEffectsIterator();
            final ArrayList<AreaOfEffect> aoeList = new ArrayList<AreaOfEffect>();
            this.m_areaOfEffectString = "";
            while (effectList.hasNext()) {
                final WakfuEffect wakfuEffect = effectList.next();
                if (wakfuEffect.isAnUsableEffect()) {
                    aoeList.add(wakfuEffect.getAreaOfEffect());
                }
            }
            if (!aoeList.isEmpty()) {
                final AreaOfEffect aoe = AreaOfEffectEnum.getBiggestAreaOfEffect(aoeList);
                String areaOfEffect = aoe.getShape().toString();
                if (areaOfEffect.equals("SPECIAL")) {
                    return "";
                }
                try {
                    areaOfEffect = String.format(WakfuConfiguration.getInstance().getString("areasIconsPath"), areaOfEffect);
                    this.m_areaOfEffectString = TextUtils.getImageTag(areaOfEffect, -1, -1, null);
                }
                catch (PropertyException e) {
                    ReferenceItemDisplayer.m_logger.error((Object)e);
                }
            }
        }
        return this.m_areaOfEffectString;
    }
    
    public ArrayList<String> getEffectsString() {
        if (this.m_effectsString == null) {
            this.m_effectsString = getEffectsString(CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY, this.m_referenceItem, null);
        }
        return this.m_effectsString;
    }
    
    public ArrayList<String> getCriticalEffectsString() {
        if (this.m_criticalEffectsCache == null) {
            this.m_criticalEffectsCache = getEffectsString(CastableDescriptionGenerator.DescriptionMode.CRITICALS_ONLY, this.m_referenceItem, null);
        }
        return this.m_criticalEffectsCache;
    }
    
    public static ArrayList<String> getEffectsString(final CastableDescriptionGenerator.DescriptionMode mode, final AbstractReferenceItem item, @Nullable final Item realItem) {
        switch (mode) {
            case CRITICALS_ONLY: {
                return describeEffects(ItemEffectType.CRITICALS, item, realItem);
            }
            default: {
                return describeEffects(ItemEffectType.EFFECTS, item, realItem);
            }
        }
    }
    
    private static ArrayList<String> describeEffects(final ItemEffectType effectType, final AbstractReferenceItem item, final Item realItem) {
        final LocalPlayerCharacter localPlayer = UIEquipmentFrame.getCharacter();
        final ContainerWriter itemWriter = new ItemWriter(item, realItem, localPlayer.getCharacteristics(), localPlayer.getEquipmentInventory(), effectType);
        return itemWriter.writeContainer();
    }
    
    public ArrayList<String> getCharacteristicsString() {
        if (this.m_characteristicsString == null) {
            this.m_characteristicsString = getCharacteristicsString(this.m_referenceItem, null);
        }
        return this.m_characteristicsString;
    }
    
    public ArrayList<String> getAllCharacteristicsString() {
        if (this.m_allCharacteristicsString == null) {
            this.m_allCharacteristicsString = getAllCharacteristicsString(this.m_referenceItem, null);
        }
        return this.m_allCharacteristicsString;
    }
    
    public static ArrayList<String> getAllCharacteristicsString(final AbstractReferenceItem item, @Nullable final Item realItem) {
        final LocalPlayerCharacter localPlayer = UIEquipmentFrame.getCharacter();
        final ContainerWriter itemWriter = new ItemWriter(item, realItem, localPlayer.getCharacteristics(), localPlayer.getEquipmentInventory(), ItemEffectType.ALL_CHARACTERISTICS);
        return itemWriter.writeContainer();
    }
    
    public static ArrayList<String> getCharacteristicsString(final AbstractReferenceItem item, @Nullable final Item realItem) {
        final ContainerWriter itemWriter = new ItemWriter(item, realItem, null, null, ItemEffectType.BASE_CHARACTERISTICS);
        return itemWriter.writeContainer();
    }
    
    public void resetCache() {
        this.m_allCharacteristicsString = null;
        this.m_characteristicsString = null;
        this.m_effectsString = null;
        this.m_criticalEffectsCache = null;
        if (this.m_referenceItem.getSetId() != 0) {
            final ItemSet itemSet = ItemSetManager.getInstance().getItemSet(this.m_referenceItem.getSetId());
            if (itemSet != null) {
                itemSet.resetCache();
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReferenceItemDisplayer.class);
        FIELDS = new String[] { "name", "description", "type", "level", "effect", "effectAndCaracteristic", "criticalEffectDetails", "shortEffectDetails", "caracteristic", "requirement", "iconUrl", "iconBigUrl", "ap", "range", "areaOfEffectIconURL", "set", "isEquiped", "isInInventory", "isEquipable", "isPreviewable", "ingredients", "storingCapacity", "conditionDescription", "craftMovable", "isPerfect", "isRelic", "isRelic2", "isCraftable", "gems", "itemUseStyle", "canBeEquiped", "itemUseDescription", "dropQuantity" };
    }
}
