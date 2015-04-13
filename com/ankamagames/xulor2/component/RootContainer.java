package com.ankamagames.xulor2.component;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;

public class RootContainer extends Container
{
    private static Logger m_logger;
    public static final String TAG = "RootContainer";
    protected Container m_content;
    protected Container m_topContent;
    protected LayeredContainer m_layeredContainer;
    protected final WindowManager m_windowManager;
    
    public RootContainer() {
        super();
        this.m_windowManager = new WindowManager(this);
    }
    
    @Override
    public void addFromXML(final EventDispatcher e) {
        this.m_content.addFromXML(e);
    }
    
    @Override
    public void addFromXML(final DataElement e) {
        this.m_content.addFromXML(e);
    }
    
    public void add(final Widget w, final int index, final boolean addToContent) {
        if (addToContent) {
            this.m_content.add(w, index);
        }
        else {
            super.add(w, index);
        }
    }
    
    @Override
    public void add(final Widget widget, final int index) {
        this.add(widget, index, true);
    }
    
    @Override
    public String getTag() {
        return "RootContainer";
    }
    
    public Container getContentContainer() {
        return this.m_content;
    }
    
    public LayeredContainer getLayeredContainer() {
        return this.m_layeredContainer;
    }
    
    public WindowManager getWindowManager() {
        return this.m_windowManager;
    }
    
    public void createContainers() {
        (this.m_layeredContainer = new LayeredContainer()).onCheckOut();
        this.m_content = Container.checkOut();
        final StaticLayoutData sld = new StaticLayoutData();
        sld.onCheckOut();
        sld.setSize(new Dimension(100.0f, 100.0f));
        this.m_content.add(sld);
        this.m_layeredContainer.addWidgetToLayer(this.m_content, -30000);
        super.add(this.m_layeredContainer);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final RootContainerLayout l = new RootContainerLayout();
        l.onCheckOut();
        this.add(l);
        this.createContainers();
        this.m_rootFocusContainer = true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_windowManager.clean();
        FocusManager.getInstance().removeRootContainer(this);
    }
    
    static {
        RootContainer.m_logger = Logger.getLogger((Class)RootContainer.class);
    }
    
    public class RootContainerLayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            return new Dimension(RootContainer.this.m_appearance.getContentWidth(), RootContainer.this.m_appearance.getContentHeight());
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            return new Dimension(RootContainer.this.m_appearance.getContentWidth(), RootContainer.this.m_appearance.getContentHeight());
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            RootContainer.this.m_layeredContainer.setSize(RootContainer.this.getAppearance().getContentWidth(), RootContainer.this.getAppearance().getContentHeight());
            RootContainer.this.m_layeredContainer.setPosition(0, 0);
            RootContainer.this.m_content.setSize(RootContainer.this.getAppearance().getContentWidth(), RootContainer.this.getAppearance().getContentHeight());
            RootContainer.this.m_content.setPosition(0, 0);
        }
    }
}
