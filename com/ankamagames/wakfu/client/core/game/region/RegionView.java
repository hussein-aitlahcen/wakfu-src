package com.ankamagames.wakfu.client.core.game.region;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;

public class RegionView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    private final Region m_region;
    
    public RegionView(final Region region) {
        super();
        this.m_region = region;
    }
    
    public Region getRegion() {
        return this.m_region;
    }
    
    @Override
    public String[] getFields() {
        return RegionView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("name".equals(fieldName)) {
            return WakfuTranslator.getInstance().getString("region." + this.m_region.name());
        }
        return null;
    }
}
