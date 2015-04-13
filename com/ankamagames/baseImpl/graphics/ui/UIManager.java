package com.ankamagames.baseImpl.graphics.ui;

import com.ankamagames.baseImpl.graphics.opengl.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;

public interface UIManager
{
    void setUserDefinedAdapter(UserDefinedAdapter p0);
    
    void setAppUI(GLApplicationUI p0);
    
    void reloadTextures();
    
    void addDialogLoadListener(DialogLoadListener p0);
    
    void removeDialogLoadListener(DialogLoadListener p0);
    
    void addDialogUnloadListener(DialogUnloadListener p0);
    
    void removeDialogUnloadListener(DialogUnloadListener p0);
}
