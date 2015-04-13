package com.ankamagames.xulor2.decorator.mesh;

import com.ankamagames.xulor2.util.*;
import java.awt.*;

public class ListOverMesh extends PlainBackgroundMesh
{
    public void setPositionSize(final int x, final int y, final int width, final int height, final int topI, final int bottomI, final int leftI, final int rightI) {
        final int left = x + leftI;
        final int top = y + bottomI + height;
        this.m_entity.setBounds(top, left, width, height);
    }
    
    public void setPositionSize(final Point p, final Dimension size, final Insets insets) {
        this.setPositionSize(p.x, p.y, size.width, size.height, insets.top, insets.bottom, insets.left, insets.right);
    }
}
