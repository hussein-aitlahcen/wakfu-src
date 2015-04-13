package com.ankamagames.wakfu.client.core.game.group.partySearch;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;

public class PartyRoleView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String STYLE = "style";
    private final PartyRole m_role;
    private static final Map<PartyRole, PartyRoleView> VIEWS;
    
    public static PartyRoleView getView(final PartyRole role) {
        return PartyRoleView.VIEWS.get(role);
    }
    
    private PartyRoleView(final PartyRole role) {
        super();
        this.m_role = role;
    }
    
    @Override
    public String[] getFields() {
        return PartyRoleView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("style")) {
            return "partyRole" + this.m_role.getId();
        }
        return null;
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString("partySearch.roleName" + this.m_role.getId());
    }
    
    public PartyRole getRole() {
        return this.m_role;
    }
    
    @Override
    public String toString() {
        return "PartyRoleView{m_role=" + this.m_role + '}';
    }
    
    static {
        VIEWS = new EnumMap<PartyRole, PartyRoleView>(PartyRole.class);
        for (final PartyRole role : PartyRole.values()) {
            PartyRoleView.VIEWS.put(role, new PartyRoleView(role));
        }
    }
}
