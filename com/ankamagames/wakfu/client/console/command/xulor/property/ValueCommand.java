package com.ankamagames.wakfu.client.console.command.xulor.property;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.property.*;

public class ValueCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final String propertyName = args.get(2);
        final StringBuilder builder = new StringBuilder(propertyName);
        builder.append(" = ");
        final Property property = Xulor.getInstance().getEnvironment().getPropertiesProvider().getProperty(propertyName);
        if (property != null) {
            final Object value = property.getValue();
            if (value instanceof FieldProvider) {
                this.appendFieldsValue((FieldProvider)value, builder);
            }
            else {
                this.appendValue(value, builder);
            }
        }
        manager.trace(builder.toString());
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    private void appendValue(final Object value, final StringBuilder builder) {
        if (value instanceof Object[]) {
            for (final Object subValue : (Object[])value) {
                if (subValue instanceof FieldProvider) {
                    this.appendFieldsValue((FieldProvider)subValue, builder);
                }
                else {
                    builder.append(subValue);
                }
                builder.append(',').append('\n');
            }
        }
        else {
            builder.append(value);
        }
    }
    
    private void appendFieldsValue(final FieldProvider fieldProvider, final StringBuilder builder) {
        final String[] arr$;
        final String[] fields = arr$ = fieldProvider.getFields();
        for (final String field : arr$) {
            builder.append('\n').append(field).append(" = ");
            this.appendValue(fieldProvider.getFieldValue(field), builder);
        }
    }
}
