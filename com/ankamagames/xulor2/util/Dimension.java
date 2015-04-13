package com.ankamagames.xulor2.util;

import java.awt.*;
import org.apache.log4j.*;

public class Dimension extends java.awt.Dimension implements Cloneable
{
    private static Logger m_logger;
    private float m_widthPercentage;
    private float m_heightPercentage;
    
    public Dimension() {
        super();
        this.m_widthPercentage = -1.0f;
        this.m_heightPercentage = -1.0f;
    }
    
    public Dimension(final int w, final int h) {
        super();
        this.m_widthPercentage = -1.0f;
        this.m_heightPercentage = -1.0f;
        this.width = w;
        this.height = h;
    }
    
    public Dimension(final Dimension d) {
        super();
        this.m_widthPercentage = -1.0f;
        this.m_heightPercentage = -1.0f;
        this.width = d.width;
        this.height = d.height;
        this.m_widthPercentage = d.m_widthPercentage;
        this.m_heightPercentage = d.m_heightPercentage;
    }
    
    public Dimension(final float widthPercentage, final float heightPercentage) {
        super();
        this.m_widthPercentage = -1.0f;
        this.m_heightPercentage = -1.0f;
        this.m_widthPercentage = widthPercentage;
        this.m_heightPercentage = heightPercentage;
    }
    
    public Dimension(final int width1, final float height) {
        super();
        this.m_widthPercentage = -1.0f;
        this.m_heightPercentage = -1.0f;
        this.width = width1;
        this.m_heightPercentage = height;
    }
    
    public Dimension(final float w, final int h) {
        super();
        this.m_widthPercentage = -1.0f;
        this.m_heightPercentage = -1.0f;
        this.m_widthPercentage = w;
        this.height = h;
    }
    
    public float getWidthPercentage() {
        return this.m_widthPercentage;
    }
    
    public float getHeightPercentage() {
        return this.m_heightPercentage;
    }
    
    public void clampTo(final Dimension dimension) {
        this.width = Math.min(this.width, dimension.width);
        this.height = Math.min(this.height, dimension.height);
    }
    
    public void clampTo(final int w, final int h) {
        this.width = Math.min(this.width, w);
        this.height = Math.min(this.height, h);
    }
    
    public void setValue(final int w, final int h) {
        this.width = w;
        this.height = h;
    }
    
    public void setPercentage(final float width, final float height) {
        this.m_heightPercentage = height;
        this.m_widthPercentage = width;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public void setHeightPercentage(final float heightPercentage) {
        this.m_heightPercentage = heightPercentage;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public void setWidthPercentage(final float widthPercentage) {
        this.m_widthPercentage = widthPercentage;
    }
    
    public boolean hasPercentage() {
        return this.m_widthPercentage != -1.0f || this.m_heightPercentage != -1.0f;
    }
    
    public boolean equals(final Dimension d) {
        return d != null && d.height == this.height && d.width == this.width && d.m_heightPercentage == this.m_heightPercentage && d.m_widthPercentage == this.m_widthPercentage;
    }
    
    public Dimension cloneDimension() {
        final Dimension clone = new Dimension(this.width, this.height);
        clone.setHeightPercentage(this.m_heightPercentage);
        clone.setWidthPercentage(this.m_widthPercentage);
        return clone;
    }
    
    static {
        Dimension.m_logger = Logger.getLogger((Class)Dimension.class);
    }
}
