package com.ankamagames.wakfu.client.core.game.group.partySearch;

import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public abstract class PartyOccupationView<P extends PartyOccupation> extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String LEVEL = "level";
    public static final String SELECTED = "selected";
    public static final String ENABLED = "enabled";
    public static final String LEVEL_VALUE = "levelValue";
    private final PvePartyOccupationGroup m_group;
    private boolean m_registration;
    protected final P m_occupation;
    private boolean m_selected;
    protected boolean m_enabled;
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PartyOccupationView that = (PartyOccupationView)o;
        return this.m_registration == that.m_registration && this.m_occupation.equals(that.m_occupation);
    }
    
    @Override
    public int hashCode() {
        int result = this.m_registration ? 1 : 0;
        result = 31 * result + this.m_occupation.hashCode();
        return result;
    }
    
    protected PartyOccupationView(final PvePartyOccupationGroup group, final P occupation, final boolean enabled, final boolean registration) {
        super();
        this.m_group = group;
        this.m_occupation = occupation;
        this.m_enabled = enabled;
        this.m_registration = registration;
    }
    
    @Override
    public String[] getFields() {
        return PartyOccupationView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("selected")) {
            return this.m_selected;
        }
        if (fieldName.equals("enabled")) {
            return this.m_enabled;
        }
        if (fieldName.equals("level")) {
            return this.getLevelDescription();
        }
        if (fieldName.equals("levelValue")) {
            return this.getLevel();
        }
        return null;
    }
    
    public abstract String getName();
    
    public PvePartyOccupationGroup getGroup() {
        return this.m_group;
    }
    
    public static String name(final PartyOccupation occupation) {
        if (occupation.getOccupationType() == PartyOccupationType.MONSTER) {
            return MonsterPartyOccupationView.name(occupation.getReferenceId());
        }
        if (occupation.getOccupationType() == PartyOccupationType.DUNGEON) {
            return DungeonPartyOccupationView.name(occupation.getReferenceId());
        }
        return "";
    }
    
    public abstract String getLevelDescription();
    
    public abstract short getLevel();
    
    public boolean isSelected() {
        return this.m_selected;
    }
    
    public void setSelected(final boolean selected) {
        this.setSelected(selected, true);
    }
    
    public void setSelected(final boolean selected, final boolean update) {
        if (this.m_selected == selected) {
            return;
        }
        this.m_selected = selected;
        if (update) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "selected");
        }
    }
    
    public void fireSelection() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selected");
    }
    
    public P getOccupation() {
        return this.m_occupation;
    }
    
    @Override
    public String toString() {
        return "PartyOccupationView{m_occupation=" + this.m_occupation + ", m_selected=" + this.m_selected + '}';
    }
}
