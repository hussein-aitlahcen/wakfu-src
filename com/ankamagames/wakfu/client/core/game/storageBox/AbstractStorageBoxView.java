package com.ankamagames.wakfu.client.core.game.storageBox;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.util.*;

public abstract class AbstractStorageBoxView extends ImmutableFieldProvider implements StorageBoxView
{
    public static final String COMPARTMENTS_FIELD = "compartments";
    public static final String SELECTED_COMPARTMENT_FIELD = "selectedCompartment";
    public static final String NAME_FIELD = "name";
    public static final String PREF_SIZE_FIELD = "prefSize";
    public static final String IDEAL_SIZE_MAX_COLUMNS_FIELD = "idealSizeMaxColumns";
    public static final String IDEAL_SIZE_MAX_ROWS_FIELD = "idealSizeMaxRows";
    public static final String MONEY_AMOUNT = "moneyAmount";
    public static final String CAN_MANAGE_MONEY = "canManageMoney";
    public static final String CAN_TAKE_MONEY = "canTakeMoney";
    public static final String CAN_PUT_MONEY = "canPutMoney";
    public static final String HISTORY = "history";
    public static final String[] FIELDS;
    protected ArrayList<CompartmentView> m_compartmentViewImpls;
    protected CompartmentView m_selectedCompartmentImpl;
    protected int m_moneyAmount;
    
    public AbstractStorageBoxView() {
        super();
        this.m_compartmentViewImpls = new ArrayList<CompartmentView>();
    }
    
    @Override
    public String[] getFields() {
        return AbstractStorageBoxView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("compartments")) {
            return this.m_compartmentViewImpls;
        }
        if (fieldName.equals("selectedCompartment")) {
            return this.m_selectedCompartmentImpl;
        }
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("prefSize")) {
            return this.getPrefSize();
        }
        if (fieldName.equals("idealSizeMaxColumns")) {
            return this.getIdealSizeMaxColumns();
        }
        if (fieldName.equals("idealSizeMaxRows")) {
            return this.getIdealSizeMaxRows();
        }
        if (fieldName.equals("canManageMoney")) {
            return this.canManageMoney();
        }
        if (fieldName.equals("canTakeMoney")) {
            return this.canTakeMoney();
        }
        if (fieldName.equals("canPutMoney")) {
            return this.canPutMoney();
        }
        if (fieldName.equals("history")) {
            return this.getHistory();
        }
        if (fieldName.equals("moneyAmount")) {
            return WakfuTranslator.getInstance().formatNumber(this.m_moneyAmount);
        }
        return null;
    }
    
    @Override
    public void setMoney(final int money) {
        this.m_moneyAmount = money;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "moneyAmount");
    }
    
    @Override
    public int getMoney() {
        return this.m_moneyAmount;
    }
    
    protected abstract String getName();
    
    protected abstract Dimension getPrefSize();
    
    protected abstract int getIdealSizeMaxColumns();
    
    protected abstract int getIdealSizeMaxRows();
    
    protected abstract boolean canManageMoney();
    
    protected abstract boolean canPutMoney();
    
    protected abstract boolean canTakeMoney();
    
    protected abstract FieldProvider getHistory();
    
    @Override
    public abstract int getSize();
    
    @Override
    public CompartmentView getSelectedCompartment() {
        return this.m_selectedCompartmentImpl;
    }
    
    @Override
    public CompartmentView getCompartmentByIndex(final byte index) {
        return this.m_compartmentViewImpls.get(index);
    }
    
    @Override
    public void setSelectedCompartment(final CompartmentView selectedCompartment) {
        this.m_selectedCompartmentImpl = selectedCompartment;
    }
    
    @Override
    public void updateFields() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, AbstractStorageBoxView.FIELDS);
    }
    
    public void setMoneyAmount(final int moneyAmount) {
        this.m_moneyAmount = moneyAmount;
    }
    
    @Override
    public void clear() {
    }
    
    static {
        FIELDS = new String[] { "compartments", "selectedCompartment", "name", "prefSize", "idealSizeMaxColumns", "idealSizeMaxRows", "canManageMoney", "canTakeMoney", "canPutMoney", "moneyAmount" };
    }
}
