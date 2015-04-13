package com.ankamagames.framework.kernel.core.controllers;

import java.awt.event.*;

public interface FocusController
{
    boolean focusGained(FocusEvent p0);
    
    boolean focusLost(FocusEvent p0);
}
