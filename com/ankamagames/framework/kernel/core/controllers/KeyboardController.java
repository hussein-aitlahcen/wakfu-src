package com.ankamagames.framework.kernel.core.controllers;

import java.awt.event.*;

public interface KeyboardController
{
    boolean keyTyped(KeyEvent p0);
    
    boolean keyPressed(KeyEvent p0);
    
    boolean keyReleased(KeyEvent p0);
}
