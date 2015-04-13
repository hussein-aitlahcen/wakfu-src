package com.ankamagames.wakfu.client.core.config;

import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.configuration.*;

public class SystemConfigurationView extends ImmutableFieldProvider
{
    @Override
    public String[] getFields() {
        return SystemConfigurationView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        final SystemConfigurationType type = SystemConfigurationType.getByKey(fieldName);
        if (type == null) {
            return null;
        }
        switch (type.getType()) {
            case BOOLEAN: {
                return SystemConfiguration.INSTANCE.getBooleanValue(type);
            }
            case NUMBER: {
                return SystemConfiguration.INSTANCE.getIntValue(type);
            }
            case STRING: {
                return SystemConfiguration.INSTANCE.getStringValue(type);
            }
            case NUMBERLIST: {
                return SystemConfiguration.INSTANCE.getIntArrayList(type);
            }
            case STRINGLIST: {
                return SystemConfiguration.INSTANCE.getStringArrayList(type);
            }
            default: {
                return null;
            }
        }
    }
}
