package com.ankamagames.xulor2.decorator;

import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.component.*;

public interface AppearanceElement
{
    void setEnabled(boolean p0);
    
    boolean isEnabled();
    
    void setDecoratorAppearance(DecoratorAppearance p0);
    
    DecoratorAppearance getDecoratorAppearance();
    
    void setWidget(Widget p0);
    
    Widget getWidget();
    
    void cleanAll();
    
    void setRemovable(boolean p0);
    
    boolean isRemovable();
}
