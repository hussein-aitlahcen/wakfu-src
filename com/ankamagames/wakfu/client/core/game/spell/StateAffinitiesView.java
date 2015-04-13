package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class StateAffinitiesView extends ImmutableFieldProvider
{
    public static final String HAS_SOME_BONUSES = "hasSomeBonuses";
    public static final String APPLICATION_BONUSES_FIELD = "applicationBonuses";
    public static final String RESISTANCE_BONUSES_FIELD = "resistanceBonuses";
    private IntObjectLightWeightMap<StateAffinityFieldProvider> m_applicationBonuses;
    private IntObjectLightWeightMap<StateAffinityFieldProvider> m_resistanceBonuses;
    
    public StateAffinitiesView() {
        super();
        this.m_applicationBonuses = new IntObjectLightWeightMap<StateAffinityFieldProvider>();
        this.m_resistanceBonuses = new IntObjectLightWeightMap<StateAffinityFieldProvider>();
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    public void reset() {
        this.m_applicationBonuses.clear();
        this.m_resistanceBonuses.clear();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasSomeBonuses", "applicationBonuses", "resistanceBonuses");
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("hasSomeBonuses")) {
            return this.m_applicationBonuses.size() > 0 || this.m_resistanceBonuses.size() > 0;
        }
        if (fieldName.equals("applicationBonuses")) {
            return this.m_applicationBonuses;
        }
        if (fieldName.equals("resistanceBonuses")) {
            return this.m_resistanceBonuses;
        }
        return null;
    }
    
    public void setApplicationBonus(final short stateId, final int value) {
        final StateAffinityFieldProvider stateAff = this.m_applicationBonuses.get(stateId);
        if (stateAff == null) {
            if (value != 0) {
                this.m_applicationBonuses.put(stateId, new StateAffinityFieldProvider(stateId, value));
                PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasSomeBonuses", "applicationBonuses");
            }
        }
        else if (value == 0) {
            this.m_applicationBonuses.remove(stateId);
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasSomeBonuses", "applicationBonuses");
        }
        else {
            stateAff.setValue(value);
        }
    }
    
    public void setResistanceBonus(final short stateId, final int value) {
        final StateAffinityFieldProvider stateAff = this.m_resistanceBonuses.get(stateId);
        if (stateAff == null) {
            if (value != 0) {
                this.m_resistanceBonuses.put(stateId, new StateAffinityFieldProvider(stateId, value));
                PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasSomeBonuses", "resistanceBonuses");
            }
        }
        else if (value == 0) {
            this.m_resistanceBonuses.remove(stateId);
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasSomeBonuses", "resistanceBonuses");
        }
        else {
            stateAff.setValue(value);
        }
    }
}
