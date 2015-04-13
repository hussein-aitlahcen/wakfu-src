package com.ankamagames.xulor2.core.dialogclose;

public interface DialogCloseRequestListener
{
    public static final int CLOSE_DIALOG = 0;
    public static final int OPEN_CLOSE_MENU = 1;
    public static final int DO_NOTHING = 2;
    public static final int CONTINUE = 3;
    
    int onDialogCloseRequest(String p0);
}
