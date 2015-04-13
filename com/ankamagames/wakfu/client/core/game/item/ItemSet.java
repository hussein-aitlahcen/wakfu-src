package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characteristics.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import gnu.trove.*;

public class ItemSet extends AbstractItemSet<ReferenceItem> implements FieldProvider
{
    private static final Logger m_logger;
    public static final String NAME_FIELD = "name";
    public static final String NAME_UNDERLINE_FIELD = "nameUnderline";
    public static final String ID_FIELD = "id";
    public static final String ITEMS_FIELD = "items";
    public static final String NAME_AND_NUMBER_FIELD = "nameAndNumber";
    public static final String ITEM_NUMBER = "itemNumber";
    public static final String BONUS_THRESHOLD = "bonusThreshold";
    public static final String CONDITION_FIELD = "condition";
    public static final String BONUS_BY_ITEM_FIELD = "bonusByItem";
    public static final String ALL_BONUSES_FIELD = "allBonuses";
    public static final String REQUIREMENT_FIELD = "requirement";
    public static final String IS_EQUIPABLE = "isEquipable";
    public static final String IS_PREVIEWABLE = "isPreviewable";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String[] FIELDS;
    private ArrayList<String> m_maxCrits;
    private Object[] m_allItems;
    
    public ItemSet(final short id, final int linkedRefItemId, final List<ReferenceItem> referenceItems) {
        super(id, linkedRefItemId, referenceItems);
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(20, this.getId(), new Object[0]);
    }
    
    public String getDescription() {
        return WakfuTranslator.getInstance().getString(21, this.getId(), new Object[0]);
    }
    
    public void initializeItemSetCriterions() {
        final ArrayList<SimpleCriterion> crits = new ArrayList<SimpleCriterion>();
        final List<ReferenceItem> referenceItems = this.getReferenceItems();
        for (int i = 0; i < referenceItems.size(); ++i) {
            final ReferenceItem item = referenceItems.get(i);
            crits.add(item.getCriterion(ActionsOnItem.EQUIP));
        }
        this.m_maxCrits = CriterionDescriptionGenerator.displayMaxCrits(crits);
    }
    
    private byte getItemEquipedCount() {
        byte nbItemEquiped = 0;
        final ItemEquipment equipment = WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory();
        for (final ReferenceItem setItem : this) {
            if (equipment.containsReferenceId(setItem.getId())) {
                ++nbItemEquiped;
            }
        }
        return nbItemEquiped;
    }
    
    public String getItemBonuses(final int equipedNumber) {
        final ArrayList<WakfuEffect> effects = this.getItemEffects(equipedNumber);
        if (effects == null) {
            return "";
        }
        return getEffectsDescription(effects);
    }
    
    public Object[] getItems() {
        if (this.m_allItems != null) {
            return this.m_allItems;
        }
        final List<ReferenceItem> referenceItems = this.getReferenceItems();
        final TIntArrayList metaIds = new TIntArrayList();
        final TIntObjectHashMap items = new TIntObjectHashMap();
        for (int i = 0; i < referenceItems.size(); ++i) {
            final ReferenceItem refItem = referenceItems.get(i);
            final int referenceId = refItem.getId();
            Item found = null;
            int lockedMetaId = -1;
            int metaId = -1;
            if (refItem instanceof SubMetaItem) {
                final SubMetaItem subMetaItem = (SubMetaItem)refItem;
                metaId = subMetaItem.getMetaParent().getId();
                lockedMetaId = (metaIds.contains(metaId) ? metaId : -1);
            }
            if (found == null) {
                final TLongObjectIterator<AbstractBag> it = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getBagsIterator();
                while (it.hasNext()) {
                    it.advance();
                    final AbstractBag c = it.value();
                    for (final Item item : c) {
                        if (item.getReferenceId() == referenceId) {
                            found = item;
                            break;
                        }
                    }
                    if (found != null) {
                        break;
                    }
                }
            }
            if (found != null) {
                items.put((metaId == -1) ? referenceId : metaId, found);
            }
            else if (lockedMetaId == -1) {
                items.put((metaId == -1) ? referenceId : metaId, refItem);
            }
            if (metaId != -1) {
                metaIds.add(metaId);
            }
        }
        items.getValues(this.m_allItems = new Object[items.size()]);
        return this.m_allItems;
    }
    
    public static String getEffectsDescription(final ArrayList<WakfuEffect> effects) {
        Collections.sort(effects, EffectDescription.WakfuEffectTypeComparator.COMPARATOR);
        final ItemSetWriter writer = new ItemSetWriter(effects);
        final ArrayList<String> strings = writer.writeContainer();
        return strings.isEmpty() ? "" : strings.get(0);
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName == null) {
            return null;
        }
        if (fieldName.equalsIgnoreCase("name")) {
            return this.getName();
        }
        if (fieldName.equalsIgnoreCase("nameUnderline")) {
            return "<u>" + this.getName() + "</u>";
        }
        if (fieldName.equals("id")) {
            return this.getId();
        }
        if (fieldName.equals("nameAndNumber")) {
            final byte itemEquipedCount = this.getItemEquipedCount();
            final TextWidgetFormater textWidgetFormater = new TextWidgetFormater();
            final int piecesNumber = this.getPiecesNumber();
            if (itemEquipedCount == piecesNumber) {
                textWidgetFormater.openText().addColor(new Color(0.0f, 0.7f, 0.0f, 1.0f).getRGBtoHex());
            }
            textWidgetFormater.append(this.getName()).append(" (").append(itemEquipedCount).append("/").append(piecesNumber).append(")");
            return textWidgetFormater.finishAndToString();
        }
        if (fieldName.equals("isEquipable")) {
            final TLongObjectIterator<AbstractBag> it = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getBagsIterator();
            while (it.hasNext()) {
                it.advance();
                final AbstractBag bag = it.value();
                for (final ReferenceItem setItem : this) {
                    if (bag.containsReferenceId(setItem.getId())) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (fieldName.equals("isPreviewable")) {
            return true;
        }
        if (fieldName.equals("itemNumber")) {
            final byte itemEquipedCount = this.getItemEquipedCount();
            return "(" + itemEquipedCount + "/" + this.getPiecesNumber() + ")";
        }
        if (fieldName.equals("bonusThreshold")) {
            if (!this.m_effects.isEmpty()) {
                final Integer[] effectsThreshold = new Integer[this.m_effects.size()];
                int i = 0;
                for (final int j : this.m_effects.keys()) {
                    effectsThreshold[i++] = j;
                }
                Arrays.sort(effectsThreshold);
                return effectsThreshold;
            }
            return null;
        }
        else {
            if (fieldName.equals("items")) {
                return this.getItems();
            }
            if (fieldName.equals("bonusByItem")) {
                return this.getBonusByItem();
            }
            if (fieldName.equals("allBonuses")) {
                return this.getItemBonuses(this.getPiecesNumber());
            }
            if (fieldName.equals("description")) {
                return this.getDescription();
            }
            if (fieldName.equalsIgnoreCase("requirement")) {
                String temp = "";
                if (this.m_maxCrits != null) {
                    for (final String crit : this.m_maxCrits) {
                        temp = temp + crit + "\n";
                    }
                }
                return temp;
            }
            return null;
        }
    }
    
    @Override
    public int getPiecesNumber() {
        return this.getItems().length;
    }
    
    private String getBonusByItem() {
        final TextWidgetFormater textWidgetFormater = new TextWidgetFormater();
        int j = 0;
        for (int i = 0; i < this.getItems().length; ++i) {
            final int equipedNumber = i + 1;
            final String bonuses = this.getItemBonuses(equipedNumber);
            if (bonuses.length() != 0) {
                if (j > 0) {
                    textWidgetFormater.newLine().newLine();
                }
                textWidgetFormater.b();
                final boolean valid = this.getItemEquipedCount() >= equipedNumber;
                if (valid) {
                    textWidgetFormater.addColor("00b400");
                }
                textWidgetFormater.append(WakfuTranslator.getInstance().getString("object", equipedNumber)).append(" :");
                textWidgetFormater._b().newLine();
                textWidgetFormater.append(bonuses);
                ++j;
            }
        }
        return textWidgetFormater.finishAndToString();
    }
    
    @Override
    public String[] getFields() {
        return ItemSet.FIELDS;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    public void resetCache() {
        this.m_allItems = null;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isEquipable");
        PropertiesProvider.getInstance().firePropertyValueChanged(this, ItemSet.FIELDS);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemSet.class);
        FIELDS = new String[] { "name", "nameUnderline", "nameAndNumber", "items", "itemNumber", "bonusThreshold", "condition", "requirement", "isEquipable", "isPreviewable", "allBonuses", "bonusByItem", "description", "id" };
    }
}
