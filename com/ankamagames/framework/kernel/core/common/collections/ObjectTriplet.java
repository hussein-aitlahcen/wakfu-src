package com.ankamagames.framework.kernel.core.common.collections;

public class ObjectTriplet<F, S, T>
{
    private F m_first;
    private S m_second;
    private T m_third;
    
    public ObjectTriplet() {
        super();
    }
    
    public ObjectTriplet(final F first, final S second, final T third) {
        super();
        this.m_first = first;
        this.m_second = second;
        this.m_third = third;
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
    
    public T getThird() {
        return this.m_third;
    }
    
    public void setThird(final T third) {
        this.m_third = third;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ObjectTriplet that = (ObjectTriplet)o;
        Label_0062: {
            if (this.m_first != null) {
                if (this.m_first.equals(that.m_first)) {
                    break Label_0062;
                }
            }
            else if (that.m_first == null) {
                break Label_0062;
            }
            return false;
        }
        Label_0095: {
            if (this.m_second != null) {
                if (this.m_second.equals(that.m_second)) {
                    break Label_0095;
                }
            }
            else if (that.m_second == null) {
                break Label_0095;
            }
            return false;
        }
        if (this.m_third != null) {
            if (this.m_third.equals(that.m_third)) {
                return true;
            }
        }
        else if (that.m_third == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.m_first != null) ? this.m_first.hashCode() : 0;
        result = 31 * result + ((this.m_second != null) ? this.m_second.hashCode() : 0);
        result = 31 * result + ((this.m_third != null) ? this.m_third.hashCode() : 0);
        return result;
    }
}
