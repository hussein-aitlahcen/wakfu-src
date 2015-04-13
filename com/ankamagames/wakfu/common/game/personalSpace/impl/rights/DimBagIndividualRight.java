package com.ankamagames.wakfu.common.game.personalSpace.impl.rights;

import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.common.rawData.*;

public class DimBagIndividualRight
{
    private long m_id;
    private String m_name;
    private byte m_rights;
    
    DimBagIndividualRight() {
        super();
    }
    
    public DimBagIndividualRight(final long id, final String name) {
        super();
        this.m_id = id;
        this.m_name = name;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
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
    
    public void toRaw(final RawDimensionalBagPermissionIndividualEntry raw) {
        raw.userId = this.m_id;
        raw.userName = this.m_name;
        raw.rights = this.m_rights;
    }
    
    public void fromRaw(final RawDimensionalBagPermissionIndividualEntry raw) {
        this.m_id = raw.userId;
        this.m_name = raw.userName;
        this.m_rights = raw.rights;
    }
}
