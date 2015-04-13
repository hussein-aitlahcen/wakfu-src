package com.ankamagames.framework.kernel.core.common.collections;

public class ObjectPair<F, S>
{
    private F m_first;
    private S m_second;
    
    public ObjectPair() {
        super();
    }
    
    public ObjectPair(final F first, final S second) {
        super();
        this.m_first = first;
        this.m_second = second;
    }
    
    public F getFirst() {
        return this.m_first;
    }
    
    public void setFirst(final F first) {
        this.m_first = first;
    }
    
    public S getSecond() {
        return this.m_second;
    }
    
    public void setSecond(final S second) {
        this.m_second = second;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ObjectPair)) {
            return false;
        }
        final ObjectPair that = (ObjectPair)o;
        Label_0054: {
            if (this.m_first != null) {
                if (this.m_first.equals(that.m_first)) {
                    break Label_0054;
                }
            }
            else if (that.m_first == null) {
                break Label_0054;
            }
            return false;
        }
        if (this.m_second != null) {
            if (this.m_second.equals(that.m_second)) {
                return true;
            }
        }
        else if (that.m_second == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.m_first != null) ? this.m_first.hashCode() : 0;
        result = 31 * result + ((this.m_second != null) ? this.m_second.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "ObjectPair{m_first=" + this.m_first + ", m_second=" + this.m_second + '}';
    }
}
