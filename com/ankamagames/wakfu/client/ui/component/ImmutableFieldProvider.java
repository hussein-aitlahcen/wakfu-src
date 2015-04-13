package com.ankamagames.wakfu.client.ui.component;

import com.ankamagames.framework.reflect.*;

public abstract class ImmutableFieldProvider implements FieldProvider
{
    @Override
    public final void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public final void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public final void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public final boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
}
