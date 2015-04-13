package com.ankamagames.xulor2.decorator.mesh;

import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public abstract class AbstractDecoratorMesh
{
    public abstract void updateVertex(final Dimension p0, final Insets p1, final Insets p2, final Insets p3);
    
    public abstract Entity getEntity();
    
    public abstract void onCheckIn();
    
    public abstract void onCheckOut();
}
