package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.form.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.*;

@XulorActionsTag
public class ConsoleDialogActions
{
    public static final String PACKAGE = "console";
    
    public static void processInputKeyEvent(final KeyEvent keyPressedEvent, final Form form) {
        final Property fieldedProperty = form.getProperty("debug.console");
        switch (keyPressedEvent.getKeyCode()) {
            case 10: {
                form.synchronizeProperties();
                final String input = fieldedProperty.getFieldStringValue("input");
                ConsoleManager.getInstance().parseInput(input);
                fieldedProperty.setFieldValue("input", "");
                break;
            }
            case 38: {
                fieldedProperty.setFieldValue("input", ConsoleManager.getInstance().getHistoryUp());
                break;
            }
            case 40: {
                fieldedProperty.setFieldValue("input", ConsoleManager.getInstance().getHistoryDown());
                break;
            }
        }
    }
    
    public static void clear(final Event event) {
        Xulor.getInstance().getEnvironment().getPropertiesProvider().setPropertyValue("debug.console", "logs", "");
    }
}
