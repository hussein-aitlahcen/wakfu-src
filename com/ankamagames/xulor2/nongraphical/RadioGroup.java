package com.ankamagames.xulor2.nongraphical;

import com.ankamagames.xulor2.component.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;

public class RadioGroup extends NonGraphicalElement
{
    public static final String TAG = "RadioGroup";
    private ArrayList<RadioButton> m_radioButtonList;
    private String m_value;
    public static final int VALUE_HASH;
    
    public RadioGroup() {
        super();
        this.m_radioButtonList = new ArrayList<RadioButton>();
    }
    
    @Override
    public String getTag() {
        return "RadioGroup";
    }
    
    public String getValue() {
        return this.m_value;
    }
    
    public void setValue(final String value) {
        this.m_value = value;
        for (final RadioButton b : this.m_radioButtonList) {
            if (b.getValue() != null && b.getValue().equalsIgnoreCase(value)) {
                b.setSelected(true);
                break;
            }
        }
    }
    
    public void foreachRadioButton(final TObjectProcedure<RadioButton> procedure) {
        for (int i = 0, size = this.m_radioButtonList.size(); i < size; ++i) {
            if (!procedure.execute(this.m_radioButtonList.get(i))) {
                return;
            }
        }
    }
    
    public void addRadioButton(final RadioButton button) {
        if (!this.m_radioButtonList.contains(button)) {
            this.m_radioButtonList.add(button);
        }
    }
    
    public void removeRadioButton(final RadioButton button) {
        this.m_radioButtonList.remove(button);
    }
    
    public void setRadioButtonList(final ArrayList<RadioButton> radioButtonList) {
        this.m_radioButtonList = radioButtonList;
    }
    
    @Override
    public void setElementMap(final ElementMap map) {
        super.setElementMap(map);
        if (this.m_id != null) {
            this.m_elementMap.putRadioGroup(this.m_id, this);
        }
    }
    
    @Override
    public void setId(final String id) {
        if (this.m_elementMap != null && this.m_elementMap.radioGroupExists(this.m_id)) {
            this.m_elementMap.removeRadioGroup(this.m_id);
        }
        super.setId(id);
        if (this.m_elementMap != null) {
            this.m_elementMap.putRadioGroup(this.m_id, this);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_radioButtonList.clear();
    }
    
    @Override
    public void copyElement(final BasicElement r) {
        final RadioGroup e = (RadioGroup)r;
        super.copyElement(e);
        e.m_value = this.m_value;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == RadioGroup.VALUE_HASH) {
            this.setValue(cl.convertToString(value, this.m_elementMap));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == RadioGroup.VALUE_HASH) {
            this.setValue(String.valueOf(value));
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        VALUE_HASH = "value".hashCode();
    }
}
