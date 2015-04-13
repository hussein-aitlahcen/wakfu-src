package com.ankamagames.xulor2.layout;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;

public class SpringProxy extends Spring
{
    private static final Logger m_logger;
    private SpringLayout m_layout;
    private Widget m_widget;
    private String m_edge;
    private String m_path;
    
    public SpringProxy(final SpringLayout layout, final Widget widget, final String edge) {
        super();
        this.m_layout = layout;
        this.m_widget = widget;
        this.m_edge = edge;
    }
    
    public SpringProxy(final SpringLayout layout, final String widget, final String edge) {
        super();
        this.m_layout = layout;
        this.m_path = widget;
        this.m_edge = edge;
    }
    
    public Spring getConstraint() {
        if (this.m_widget == null) {
            Environment e = Xulor.getInstance().getEnvironment();
            if (e == null) {
                e = Xulor.getInstance().getEnvironment();
            }
            this.m_widget = (Widget)e.getElement(this.m_path);
            if (this.m_widget == null) {
                SpringProxy.m_logger.warn((Object)("Impossible de trouver le widget " + this.m_path));
            }
        }
        if (this.m_layout.getConstraint(this.m_widget) == null) {
            return null;
        }
        return this.m_layout.getConstraint(this.m_widget).getConstraint(this.m_edge);
    }
    
    @Override
    public int getValue() {
        final Spring spring = this.getConstraint();
        return (spring != null) ? spring.getValue() : 0;
    }
    
    @Override
    public void setValue(final int value) {
        this.getConstraint().setValue(value);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpringProxy.class);
    }
}
