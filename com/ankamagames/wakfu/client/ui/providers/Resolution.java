package com.ankamagames.wakfu.client.ui.providers;

public class Resolution implements Comparable<Resolution>
{
    private final int m_width;
    private final int m_height;
    
    public Resolution(final int width, final int height) {
        super();
        this.m_width = width;
        this.m_height = height;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Resolution that = (Resolution)o;
        return this.m_height == that.m_height && this.m_width == that.m_width;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_width;
        result = 31 * result + this.m_height;
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        return sb.append(this.m_width).append("x").append(this.m_height).toString();
    }
    
    @Override
    public int compareTo(final Resolution resolution) {
        return this.m_width * this.m_height - resolution.m_width * resolution.m_height;
    }
}
