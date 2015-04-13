package com.ankamagames.xulor2.component.text.builder;

public class AbstractLineSubBlock
{
    private LineBlock m_lineBlock;
    private int m_x;
    private int m_width;
    
    public AbstractLineSubBlock() {
        super();
        this.m_x = 0;
        this.m_width = 0;
    }
    
    public LineBlock getLineBlock() {
        return this.m_lineBlock;
    }
    
    public void setLineBlock(final LineBlock lineBlock) {
        this.m_lineBlock = lineBlock;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public int getY() {
        if (this.m_lineBlock != null) {
            return this.m_lineBlock.getY();
        }
        return 0;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public void setWidth(final int width) {
        this.m_width = width;
    }
    
    public int getHeight() {
        if (this.m_lineBlock != null) {
            return this.m_lineBlock.getHeight();
        }
        return 0;
    }
}
