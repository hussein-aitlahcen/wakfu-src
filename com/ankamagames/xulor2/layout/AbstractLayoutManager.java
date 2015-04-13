package com.ankamagames.xulor2.layout;

import com.ankamagames.xulor2.core.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.*;

public abstract class AbstractLayoutManager extends DataElement implements LayoutManager
{
    private static Logger m_logger;
    protected boolean m_isStandAlone;
    
    public AbstractLayoutManager() {
        super();
        this.m_isStandAlone = false;
    }
    
    public boolean canComputeContentSize() {
        return true;
    }
    
    @Override
    public boolean isStandAlone() {
        return this.m_isStandAlone;
    }
    
    @Override
    public Dimension getContentGreedySize(final Container container, final Widget greedy, final Dimension contentSize) {
        return this.getContentPreferedSize(container);
    }
    
    @Override
    public void layoutWidget(final Container parent, final Widget child) {
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_isStandAlone = false;
    }
    
    public AbstractLayoutManager clone() {
        return null;
    }
    
    static {
        AbstractLayoutManager.m_logger = Logger.getLogger((Class)AbstractLayoutManager.class);
    }
}
