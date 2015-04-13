package com.ankamagames.baseImpl.common.clientAndServer.world;

import org.jetbrains.annotations.*;
import java.util.*;

public abstract class AbstractPartition<PartitionType extends AbstractPartition>
{
    public static final int LAYOUT_COUNT = 9;
    public static final int LAYOUT_TOP_LEFT = 0;
    public static final int LAYOUT_TOP = 1;
    public static final int LAYOUT_TOP_RIGHT = 2;
    public static final int LAYOUT_LEFT = 3;
    public static final int LAYOUT_CENTER = 4;
    public static final int LAYOUT_RIGHT = 5;
    public static final int LAYOUT_BOTTOM_LEFT = 6;
    public static final int LAYOUT_BOTTOM = 7;
    public static final int LAYOUT_BOTTOM_RIGHT = 8;
    protected int m_x;
    protected int m_y;
    protected int m_width;
    protected int m_height;
    private PartitionType[] m_layout;
    private List<PartitionType> m_layoutList;
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    public List<PartitionType> getLayoutList() {
        return this.m_layoutList;
    }
    
    public PartitionType[] getLayout() {
        return (PartitionType[])this.m_layout;
    }
    
    public void setLayoutArray(@NotNull final PartitionType[] partitions) {
        assert partitions.length == 9;
        this.m_layout = partitions;
        this.m_layoutList = Arrays.asList(partitions);
    }
    
    public PartitionType getPartition(final int layoutEntry) {
        if (layoutEntry >= 0 && layoutEntry <= 8) {
            return (PartitionType)this.m_layout[layoutEntry];
        }
        return null;
    }
    
    public void setPartition(final int layoutEntry, final PartitionType partition) {
        if (layoutEntry >= 0 && layoutEntry <= 8) {
            this.m_layout[layoutEntry] = partition;
        }
    }
    
    public boolean isUnitsWithinBounds(final int x, final int y) {
        final int cy = this.m_y * this.m_height;
        if (y >= cy && y < cy + this.m_height) {
            final int cx = this.m_x * this.m_width;
            if (x >= cx && x < cx + this.m_width) {
                return true;
            }
        }
        return false;
    }
    
    public void setBounds(final int x, final int y, final int width, final int height) {
        this.m_x = x;
        this.m_y = y;
        this.m_width = width;
        this.m_height = height;
    }
}
