package com.ankamagames.framework.graphics.image;

import com.ankamagames.framework.kernel.core.common.*;
import java.util.*;

public class Palette extends ReferenceCounter
{
    private ArrayList<Color> m_colors;
    
    public Palette(final int size) {
        super();
        this.m_colors = new ArrayList<Color>(size);
    }
    
    public Palette(final Palette palette) {
        super();
        (this.m_colors = new ArrayList<Color>(palette.m_colors.size())).addAll(palette.m_colors);
    }
    
    public final int getNumColors() {
        return this.m_colors.size();
    }
    
    public final Color getColor(final int colorIndex) {
        return this.m_colors.get(colorIndex);
    }
    
    public final void setColor(final int colorIndex, final Color color) {
        try {
            this.m_colors.set(colorIndex, color);
        }
        catch (IndexOutOfBoundsException OutOfBound) {
            this.m_colors.add(colorIndex, color);
        }
    }
    
    public final void addColor(final Color color) {
        this.m_colors.add(color);
    }
    
    @Override
    protected void delete() {
        this.m_colors.clear();
        this.m_colors = null;
    }
}
