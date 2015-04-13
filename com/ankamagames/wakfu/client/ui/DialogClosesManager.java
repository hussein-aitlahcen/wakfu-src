package com.ankamagames.wakfu.client.ui;

import com.ankamagames.xulor2.core.dialogclose.*;
import com.ankamagames.xulor2.*;

public class DialogClosesManager extends AbstractDialogClosesManager
{
    private static final DialogClosesManager m_instance;
    
    @Override
    public int closeWindow() {
        final boolean popupClosed = Xulor.getInstance().unloadFirstMessageBox();
        if (popupClosed) {
            return 0;
        }
        return super.closeWindow();
    }
    
    public static DialogClosesManager getInstance() {
        return DialogClosesManager.m_instance;
    }
    
    static {
        m_instance = new DialogClosesManager();
    }
}
