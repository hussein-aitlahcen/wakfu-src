package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.item.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.item.elements.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SelectItemElementsView extends ImmutableFieldProvider
{
    public static final String ITEM = "item";
    public static final String DAMAGE_NB_ELEMENTS = "damageNbElements";
    public static final String RES_NB_ELEMENTS = "resNbElements";
    public static final String CHECKED_DAMAGE_NB_ELEMENTS = "checkedDamageNbElements";
    public static final String CHECKED_RES_NB_ELEMENTS = "checkedResNbElements";
    public static final String DAMAGE_ELEMENTS = "damageElements";
    public static final String RES_ELEMENTS = "resElements";
    public static final String DAMAGE_NB_ELEMENTS_TEXT = "damageNbElementsText";
    public static final String RES_NB_ELEMENTS_TEXT = "resNbElementsText";
    public static final String CAN_VALID = "canValid";
    public static final SelectItemElementsView INSTANCE;
    public static final int DAMAGE_ACTION_ID;
    public static final int RES_ACTION_ID;
    private Item m_concernedItem;
    private TIntIntHashMap m_nbElements;
    private TIntObjectHashMap<ArrayList<ElementFilterView>> m_elements;
    
    private SelectItemElementsView() {
        super();
        this.m_nbElements = new TIntIntHashMap();
        this.m_elements = new TIntObjectHashMap<ArrayList<ElementFilterView>>();
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("item".equals(fieldName)) {
            return this.m_concernedItem;
        }
        if ("damageNbElements".equals(fieldName)) {
            return this.getNbElements(SelectItemElementsView.DAMAGE_ACTION_ID);
        }
        if ("resNbElements".equals(fieldName)) {
            return this.getNbElements(SelectItemElementsView.RES_ACTION_ID);
        }
        if ("damageNbElementsText".equals(fieldName)) {
            return WakfuTranslator.getInstance().getString("damage.bonusElementsReset", this.getNbElements(SelectItemElementsView.DAMAGE_ACTION_ID));
        }
        if ("resNbElementsText".equals(fieldName)) {
            return WakfuTranslator.getInstance().getString("resist.bonusElementsReset", this.getNbElements(SelectItemElementsView.RES_ACTION_ID));
        }
        if ("canValid".equals(fieldName)) {
            return this.getNbElements(SelectItemElementsView.DAMAGE_ACTION_ID) == this.countSelectedElements(SelectItemElementsView.DAMAGE_ACTION_ID) && this.getNbElements(SelectItemElementsView.RES_ACTION_ID) == this.countSelectedElements(SelectItemElementsView.RES_ACTION_ID);
        }
        if ("checkedDamageNbElements".equals(fieldName)) {
            return this.countSelectedElements(SelectItemElementsView.DAMAGE_ACTION_ID);
        }
        if ("checkedResNbElements".equals(fieldName)) {
            return this.countSelectedElements(SelectItemElementsView.RES_ACTION_ID);
        }
        if ("damageElements".equals(fieldName)) {
            return this.m_elements.get(SelectItemElementsView.DAMAGE_ACTION_ID);
        }
        if ("resElements".equals(fieldName)) {
            return this.m_elements.get(SelectItemElementsView.RES_ACTION_ID);
        }
        return null;
    }
    
    public boolean tooManyChoices(final int actionId) {
        return this.getNbElements(actionId) < this.countSelectedElements(actionId);
    }
    
    public int getNbElements(final int actionId) {
        return this.m_nbElements.get(actionId);
    }
    
    public int countSelectedElements(final int actionId) {
        int count = 0;
        for (final ElementFilterView elementFilterView : this.m_elements.get(actionId)) {
            if (elementFilterView.isSelected()) {
                ++count;
            }
        }
        return count;
    }
    
    public Item getConcernedItem() {
        return this.m_concernedItem;
    }
    
    public void setConcernedItem(final Item concernedItem) {
        this.init(this.m_concernedItem = concernedItem);
    }
    
    public void init(final Item concernedItem) {
        this.m_nbElements.clear();
        this.m_elements.clear();
        for (final WakfuEffect wakfuEffect : concernedItem) {
            final int actionId = wakfuEffect.getActionId();
            if (actionId == SelectItemElementsView.DAMAGE_ACTION_ID || actionId == SelectItemElementsView.RES_ACTION_ID) {
                final int nbElements = (int)wakfuEffect.getParam(1);
                this.m_nbElements.put(actionId, nbElements);
            }
        }
        final ArrayList<ElementFilterView> damageElements = new ArrayList<ElementFilterView>();
        final ArrayList<ElementFilterView> resElements = new ArrayList<ElementFilterView>();
        for (final Elements e : MultiElementsInfo.ELEMENTS_LIST) {
            damageElements.add(new ElementFilterView(ElementReference.getElementReferenceFromElements(e)));
            resElements.add(new ElementFilterView(ElementReference.getElementReferenceFromElements(e)));
        }
        this.m_elements.put(SelectItemElementsView.DAMAGE_ACTION_ID, damageElements);
        this.m_elements.put(SelectItemElementsView.RES_ACTION_ID, resElements);
    }
    
    public byte getElementMask(final int actionId) {
        final HashSet<Elements> elements = new HashSet<Elements>();
        for (final ElementFilterView elementFilterView : this.m_elements.get(actionId)) {
            if (elementFilterView.isSelected()) {
                elements.add(elementFilterView.getElementReference().getElement());
            }
        }
        return MultiElementsInfo.getElementMask(elements);
    }
    
    static {
        INSTANCE = new SelectItemElementsView();
        DAMAGE_ACTION_ID = RunningEffectConstants.VARIABLE_ELEMENTS_DAMAGE_GAIN.getId();
        RES_ACTION_ID = RunningEffectConstants.VARIABLE_ELEMENTS_RES_GAIN.getId();
    }
}
