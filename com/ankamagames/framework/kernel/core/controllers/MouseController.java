package com.ankamagames.framework.kernel.core.controllers;

import java.awt.event.*;

public interface MouseController
{
    boolean mouseClicked(MouseEvent p0);
    
    boolean mousePressed(MouseEvent p0);
    
    boolean mouseReleased(MouseEvent p0);
    
    boolean mouseEntered(MouseEvent p0);
    
    boolean mouseExited(MouseEvent p0);
    
    boolean mouseDragged(MouseEvent p0);
    
    boolean mouseMoved(MouseEvent p0);
    
    boolean mouseWheelMoved(MouseWheelEvent p0);
}
