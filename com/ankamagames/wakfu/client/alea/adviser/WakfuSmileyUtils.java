package com.ankamagames.wakfu.client.alea.adviser;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;

public class WakfuSmileyUtils
{
    public static final String getNewWakfuSmileyId() {
        return new StringBuffer("animatedSmileyDialog").append(SmileyWidget.UID++).toString();
    }
    
    public static final SmileyWidget loadSmiley(final String widgetId) {
        return (SmileyWidget)Xulor.getInstance().load(widgetId, Dialogs.getDialogPath("animatedSmileyDialog"), 73744L, (short)9999);
    }
}
