package com.ankamagames.wakfu.client.core.game.dimensionalBag;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.rights.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public class RoomView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String GUILD_PERMISSION_FIELD = "guildPerms";
    public static final String ANONYMOUS_PERMISSION_FIELD = "anonymousPerms";
    public static final String INDIVIDUAL_PERMISSION_FIELD = "individualPerms";
    public static final String ICON_URL = "iconUrl";
    private static final String[] FIELDS;
    private final int m_typeId;
    private int m_gfxId;
    private final DimBagRights m_dimensionalBagPermissions;
    private final ArrayList<IndividualPermissionView> m_individualPermissionsView;
    
    public RoomView(final int typeId, final DimBagRights dimensionalBagPermissions) {
        super();
        this.m_individualPermissionsView = new ArrayList<IndividualPermissionView>();
        this.m_typeId = typeId;
        final AbstractReferenceItem item = ReferenceItemManager.getInstance().getReferenceItem(this.m_typeId);
        if (item != null) {
            this.m_gfxId = item.getGfxId();
        }
        this.m_dimensionalBagPermissions = dimensionalBagPermissions;
        this.computeIndividualViews();
    }
    
    public boolean getGroupPermission(final GroupType group) {
        final DimBagGroupRight groupRight = this.m_dimensionalBagPermissions.getGroupRight(group);
        return groupRight != null && groupRight.hasRight(GemType.getFromItemReferenceId(this.m_typeId));
    }
    
    public int getTypeId() {
        return this.m_typeId;
    }
    
    @Override
    public String[] getFields() {
        return RoomView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("guildPerms")) {
            return this.getGroupPermission(GroupType.GUILD);
        }
        if (fieldName.equals("anonymousPerms")) {
            return this.getGroupPermission(GroupType.ALL);
        }
        if (fieldName.equals("individualPerms")) {
            return this.m_individualPermissionsView;
        }
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getIconUrl("itemsIconsPath", "defaultIconPath", this.m_gfxId);
        }
        return null;
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(String.format("%s.%s", "roomType", this.m_typeId));
    }
    
    public void computeIndividualViews() {
        this.m_individualPermissionsView.clear();
        final SortedList<DimBagIndividualRight> individuals = this.m_dimensionalBagPermissions.getIndividualsList();
        for (int i = 0, size = individuals.size(); i < size; ++i) {
            this.m_individualPermissionsView.add(new IndividualPermissionView(individuals.get(i)));
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "individualPerms");
    }
    
    public void updateIndividualPerms() {
        for (int i = this.m_individualPermissionsView.size() - 1; i >= 0; --i) {
            this.m_individualPermissionsView.get(i).fireGrantedChanged();
        }
    }
    
    @Override
    public String toString() {
        return "RoomView{m_typeId=" + this.m_typeId + ", m_gfxId=" + this.m_gfxId + ", m_dimensionalBagPermissions=" + this.m_dimensionalBagPermissions + ", m_individualPermissionsView=" + this.m_individualPermissionsView + '}';
    }
    
    static {
        FIELDS = new String[] { "name", "guildPerms", "anonymousPerms", "individualPerms", "iconUrl" };
    }
    
    private class IndividualPermissionView extends ImmutableFieldProvider
    {
        public static final String ID_FIELD = "id";
        public static final String NAME_FIELD = "name";
        public static final String GRANTED = "granted";
        private final long m_id;
        private final String m_name;
        private final String[] FIELDS;
        
        IndividualPermissionView(final DimBagIndividualRight individual) {
            super();
            this.FIELDS = new String[] { "id", "name", "granted" };
            this.m_name = individual.getName();
            this.m_id = individual.getId();
        }
        
        @Override
        public String[] getFields() {
            return this.FIELDS;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("name")) {
                return this.m_name;
            }
            if (fieldName.equals("id")) {
                return this.m_id;
            }
            if (fieldName.equals("granted")) {
                return RoomView.this.m_dimensionalBagPermissions.getIndividualRight(this.m_id).hasRight(GemType.getFromItemReferenceId(RoomView.this.m_typeId));
            }
            return null;
        }
        
        public void fireGrantedChanged() {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "granted");
        }
        
        @Override
        public String toString() {
            return "IndividualPermissionView{m_id=" + this.m_id + ", m_name='" + this.m_name + '\'' + ", FIELDS=" + Arrays.toString(this.FIELDS) + '}';
        }
    }
}
