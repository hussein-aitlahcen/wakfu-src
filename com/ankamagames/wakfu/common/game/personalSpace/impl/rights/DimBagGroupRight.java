package com.ankamagames.wakfu.common.game.personalSpace.impl.rights;

import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.common.rawData.*;

public class DimBagGroupRight
{
    private GroupType m_groupType;
    private byte m_rights;
    
    DimBagGroupRight() {
        super();
    }
    
    public DimBagGroupRight(final GroupType groupType) {
        super();
        this.m_groupType = groupType;
    }
    
    public GroupType getGroupType() {
        return this.m_groupType;
    }
    
    public boolean hasRight(final GemType type) {
        return (this.m_rights & type.mask) == type.mask;
    }
    
    public void allow(final GemType type) {
        this.m_rights |= type.mask;
    }
    
    public void disallow(final GemType type) {
        this.m_rights &= (byte)~type.mask;
    }
    
    public void toRaw(final RawDimensionalBagPermissionGroupEntry raw) {
        raw.groupType = this.m_groupType.idx;
        raw.rights = this.m_rights;
    }
    
    public void fromRaw(final RawDimensionalBagPermissionGroupEntry raw) {
        this.m_groupType = GroupType.valueOf(raw.groupType);
        this.m_rights = raw.rights;
    }
}
