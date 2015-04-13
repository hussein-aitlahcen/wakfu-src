package com.ankamagames.wakfu.client.console.command.xulor.property;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class ListCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final StringBuilder builder = new StringBuilder("# Liste des propri\u00e9t\u00e9s #");
        PropertiesProvider.getInstance().foreachProperty(new TObjectProcedure<Property>() {
            @Override
            public boolean execute(final Property property) {
                builder.append('\n').append(" - ").append(property.getName());
                if (property.getValue() instanceof FieldProvider) {
                    builder.append(" (F)");
                }
                return true;
            }
        });
        manager.trace(builder.toString());
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
