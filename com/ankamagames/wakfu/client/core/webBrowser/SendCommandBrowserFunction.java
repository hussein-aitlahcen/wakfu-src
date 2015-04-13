package com.ankamagames.wakfu.client.core.webBrowser;

import org.eclipse.swt.browser.*;
import com.ankamagames.wakfu.client.ui.protocol.message.krozmoz.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class SendCommandBrowserFunction extends BrowserFunction
{
    public static final String FUNCTION_NAME = "sendCommand";
    
    public SendCommandBrowserFunction(final Browser browser) {
        super(browser, "sendCommand");
    }
    
    public Object function(final Object[] objects) {
        if (objects.length == 0) {
            return null;
        }
        final Object commandObject = objects[0];
        if (!(commandObject instanceof String)) {
            return null;
        }
        final String command = (String)commandObject;
        final Object[] params = new Object[objects.length - 1];
        System.arraycopy(objects, 1, params, 0, params.length);
        Worker.getInstance().pushMessage(new UIKrosmozCommandReceivedMessage(command, params));
        return null;
    }
}
