package com.ankamagames.xulor2.component;

import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.appearance.*;

public class RadioButton extends ToggleButton
{
    public static final String TAG = "RadioButton";
    private String m_groupId;
    private String m_value;
    public static final int GROUP_ID_HASH;
    public static final int VALUE_HASH;
    
    public RadioButton() {
        super();
        this.m_groupId = "";
    }
    
    @Override
    public String getTag() {
        return "RadioButton";
    }
    
    @Override
    public RadioButtonAppearance getAppearance() {
        return (RadioButtonAppearance)this.m_appearance;
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return appearance instanceof RadioButtonAppearance;
    }
    
    public String getGroupId() {
        return this.m_groupId;
    }
    
    public void setGroupId(final String groupId) {
        this.m_groupId = groupId;
        this.updateSelectedStatus();
    }
    
    public String getValue() {
        return this.m_value;
    }
    
    public void setValue(final String value) {
        this.m_value = value;
        this.updateSelectedStatus();
    }
    
    @Override
    public void preApplyAttributes(final DocumentEntry entry, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
        super.preApplyAttributes(entry, parent, elementMaps, env);
    }
    
    private void updateSelectedStatus() {
        if (this.m_elementMap.radioGroupExists(this.m_groupId)) {
            this.m_elementMap.getRadioGroup(this.m_groupId).addRadioButton(this);
            if (this.m_value != null && this.m_value.equalsIgnoreCase(this.m_elementMap.getRadioGroup(this.m_groupId).getValue()) && !this.getAppearance().isChecked()) {
                this.getAppearance().toggleButton();
            }
        }
    }
    
    @Override
    public void onAdd() {
        super.onAdd();
        this.updateSelectedStatus();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final RadioButtonAppearance app = new RadioButtonAppearance();
        app.onCheckOut();
        app.setWidget(this);
        this.add(app);
    }
    
    @Override
    public void onCheckIn() {
        if (this.m_elementMap.radioGroupExists(this.m_groupId)) {
            this.m_elementMap.getRadioGroup(this.m_groupId).removeRadioButton(this);
        }
        super.onCheckIn();
        this.m_groupId = null;
        this.m_value = null;
    }
    
    @Override
    public void copyElement(final BasicElement r) {
        final RadioButton e = (RadioButton)r;
        super.copyElement(e);
        e.m_groupId = this.m_groupId;
        e.m_value = this.m_value;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == RadioButton.GROUP_ID_HASH) {
            this.setGroupId(cl.convertToString(value, this.m_elementMap));
        }
        else {
            if (hash != RadioButton.VALUE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setValue(cl.convertToString(value, this.m_elementMap));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == RadioButton.GROUP_ID_HASH) {
            this.setGroupId(String.valueOf(value));
        }
        else {
            if (hash != RadioButton.VALUE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setValue(String.valueOf(value));
        }
        return true;
    }
    
    static {
        GROUP_ID_HASH = "groupId".hashCode();
        VALUE_HASH = "value".hashCode();
    }
}
