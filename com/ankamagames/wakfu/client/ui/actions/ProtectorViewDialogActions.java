package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.property.*;

@XulorActionsTag
public class ProtectorViewDialogActions
{
    public static final String PACKAGE = "wakfu.protectorView";
    
    public static void setViewMode(final Event e, final String mode) {
        final int modeValue = PrimitiveConverter.getInteger(mode, 0);
        PropertiesProvider.getInstance().setPropertyValue("protectorViewMode", modeValue);
    }
}
