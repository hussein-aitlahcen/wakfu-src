package com.ankamagames.xulor2.decorator;

import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import java.awt.*;

public interface Decorator extends StateAppearanceElement
{
    boolean isDirty();
    
    AbstractDecoratorMesh getMesh();
    
    Entity getEntity();
    
    void updateDecorator(Dimension p0, Insets p1, Insets p2, Insets p3);
}
