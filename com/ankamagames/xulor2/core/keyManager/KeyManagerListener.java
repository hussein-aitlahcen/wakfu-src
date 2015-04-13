package com.ankamagames.xulor2.core.keyManager;

import java.awt.event.*;

public abstract class KeyManagerListener
{
    public abstract boolean keyPressed(final KeyEvent p0);
    
    public abstract boolean keyReleased(final KeyEvent p0);
}
