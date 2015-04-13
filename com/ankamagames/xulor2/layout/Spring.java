package com.ankamagames.xulor2.layout;

import com.ankamagames.xulor2.core.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class Spring extends NonGraphicalElement
{
    public static final String TAG = "Spring";
    private static Logger m_logger;
    private int m_value;
    private String m_referentId;
    private String m_edge;
    private String m_referentEdge;
    public static final int EDGE_HASH;
    public static final int REFERENT_EDGE_HASH;
    public static final int REFERENT_ID_HASH;
    public static final int VALUE_HASH;
    
    public Spring() {
        super();
        this.m_value = 0;
    }
    
    public String getEdge() {
        return this.m_edge;
    }
    
    public void setEdge(final String edge) {
        this.m_edge = edge;
    }
    
    public String getReferentEdge() {
        return this.m_referentEdge;
    }
    
    public void setReferentEdge(final String referentEdge) {
        this.m_referentEdge = referentEdge;
    }
    
    @Override
    public String getTag() {
        return "Spring";
    }
    
    public int getValue() {
        return this.m_value;
    }
    
    public void setValue(final int value) {
        this.m_value = value;
    }
    
    @Override
    public String toString() {
        return Integer.toString(this.getValue());
    }
    
    public static Spring sum(final Spring s1, final Spring s2) {
        return new SumSpring(s1, s2);
    }
    
    public static Spring difference(final Spring s1, final Spring s2) {
        return sum(s1, new NegativeSpring(s2));
    }
    
    public static Spring constant(final int value) {
        return new ConstantSpring(value);
    }
    
    public static Spring width(final Widget w) {
        return new WidthSpring(w);
    }
    
    public static Spring height(final Widget w) {
        return new HeightSpring(w);
    }
    
    public static Spring x(final Widget w) {
        return new XCoordSpring(w);
    }
    
    public static Spring y(final Widget w) {
        return new YCoordSpring(w);
    }
    
    public String getReferentId() {
        return this.m_referentId;
    }
    
    public void setReferentId(final String referentId) {
        this.m_referentId = referentId;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_edge = null;
        this.m_referentEdge = null;
        this.m_referentId = null;
        this.m_value = 0;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Spring.EDGE_HASH) {
            this.setEdge(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == Spring.REFERENT_EDGE_HASH) {
            this.setReferentEdge(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == Spring.REFERENT_ID_HASH) {
            this.setReferentId(cl.convertToString(value, this.m_elementMap));
        }
        else {
            if (hash != Spring.VALUE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setValue(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        Spring.m_logger = Logger.getLogger((Class)Spring.class);
        EDGE_HASH = "edge".hashCode();
        REFERENT_EDGE_HASH = "referentEdge".hashCode();
        REFERENT_ID_HASH = "referentId".hashCode();
        VALUE_HASH = "value".hashCode();
    }
    
    public static class HeightSpring extends Spring
    {
        private Widget m_widget;
        
        public HeightSpring(final Widget w) {
            super();
            this.m_widget = w;
        }
        
        @Override
        public int getValue() {
            if (this.m_widget != null) {
                return this.m_widget.getHeight();
            }
            return 0;
        }
        
        @Override
        public void setValue(final int value) {
            this.m_widget.setSize(this.m_widget.m_size.width, value);
        }
        
        @Override
        public void onCheckIn() {
            super.onCheckIn();
            this.m_widget = null;
        }
    }
    
    public static class WidthSpring extends Spring
    {
        private Widget m_widget;
        
        public WidthSpring(final Widget w) {
            super();
            this.m_widget = w;
        }
        
        @Override
        public int getValue() {
            if (this.m_widget != null) {
                return this.m_widget.getWidth();
            }
            return 0;
        }
        
        @Override
        public void setValue(final int value) {
            this.m_widget.setSize(value, this.m_widget.m_size.height);
        }
        
        @Override
        public void onCheckIn() {
            super.onCheckIn();
            this.m_widget = null;
        }
    }
    
    public static class XCoordSpring extends Spring
    {
        private Widget m_widget;
        
        public XCoordSpring(final Widget w) {
            super();
            this.m_widget = w;
        }
        
        @Override
        public int getValue() {
            if (this.m_widget != null) {
                return this.m_widget.getX();
            }
            return 0;
        }
        
        @Override
        public void setValue(final int value) {
            this.m_widget.setX(value);
        }
        
        @Override
        public void onCheckIn() {
            super.onCheckIn();
            this.m_widget = null;
        }
    }
    
    public static class YCoordSpring extends Spring
    {
        private Widget m_widget;
        
        public YCoordSpring(final Widget w) {
            super();
            this.m_widget = w;
        }
        
        @Override
        public int getValue() {
            if (this.m_widget != null) {
                return this.m_widget.getY();
            }
            return 0;
        }
        
        @Override
        public void setValue(final int value) {
            this.m_widget.setY(value);
        }
        
        @Override
        public void onCheckIn() {
            super.onCheckIn();
            this.m_widget = null;
        }
    }
    
    public static class NegativeSpring extends Spring
    {
        private Spring m_spring;
        
        public NegativeSpring(final Spring spring) {
            super();
            if (spring != null) {
                this.m_spring = spring;
            }
            else {
                Spring.m_logger.error((Object)"Le Spring pass\u00e9 en param\u00e8tre est null");
                this.m_spring = Spring.constant(0);
            }
        }
        
        @Override
        public int getValue() {
            return -this.m_spring.getValue();
        }
        
        @Override
        public void setValue(final int value) {
            this.m_spring.setValue(value);
        }
        
        @Override
        public void onCheckIn() {
            super.onCheckIn();
            this.m_spring = null;
        }
    }
    
    public static class ConstantSpring extends Spring
    {
        private final int m_value;
        
        public ConstantSpring(final int value) {
            super();
            this.m_value = value;
        }
        
        @Override
        public int getValue() {
            return this.m_value;
        }
        
        @Override
        public void setValue(final int value) {
        }
    }
    
    public abstract static class SpringCompound extends Spring
    {
        private Spring m_s1;
        private Spring m_s2;
        
        public SpringCompound(final Spring s1, final Spring s2) {
            super();
            if (s1 == null || s2 == null) {
                Spring.m_logger.error((Object)("Probl\u00e8me dans un " + this.getClass().getName() + " : un des Spring est null"));
            }
            if (s1 != null) {
                this.m_s1 = s1;
            }
            else {
                this.m_s1 = Spring.constant(0);
            }
            if (s2 != null) {
                this.m_s2 = s2;
            }
            else {
                this.m_s2 = Spring.constant(0);
            }
        }
        
        @Override
        public int getValue() {
            return this.op(this.m_s1.getValue(), this.m_s2.getValue());
        }
        
        @Override
        public void setValue(final int value) {
            super.setValue(value);
            this.m_s1.setValue(value);
            this.m_s2.setValue(value);
        }
        
        public abstract int op(final int p0, final int p1);
        
        @Override
        public void onCheckIn() {
            super.onCheckIn();
            this.m_s1 = null;
            this.m_s2 = null;
        }
    }
    
    public static class SumSpring extends SpringCompound
    {
        public SumSpring(final Spring s1, final Spring s2) {
            super(s1, s2);
        }
        
        @Override
        public int op(final int a, final int b) {
            return a + b;
        }
    }
}
