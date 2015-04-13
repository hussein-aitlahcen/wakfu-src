package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;

public class FoldingContainer extends Container
{
    public static final String TAG = "FoldingContainer";
    private static final String THEME_TITLE_BAR = "titleBar";
    private static final String THEME_CONTENT = "content";
    private Alignment4 m_titleBarPosition;
    private Container m_titleBar;
    private Container m_content;
    private boolean m_folded;
    private boolean m_unfoldedExpandable;
    public static final int TITLE_BAR_POSITION_HASH;
    public static final int FOLDED_HASH;
    
    public FoldingContainer() {
        super();
        this.m_titleBarPosition = Alignment4.NORTH;
    }
    
    @Override
    public void addFromXML(final EventDispatcher element) {
        if (element instanceof Container && ((Widget)element).getThemeElementName().equals("titleBar")) {
            if (this.m_titleBar != null) {
                this.m_titleBar.destroySelfFromParent();
            }
            this.add(this.m_titleBar = (Container)element);
        }
        else if (element instanceof Container && ((Widget)element).getThemeElementName().equals("content")) {
            if (this.m_content != null) {
                this.m_content.destroySelfFromParent();
            }
            (this.m_content = (Container)element).setVisible(!this.m_folded);
            this.add(this.m_content);
        }
        else if (!(element instanceof Widget)) {
            super.addFromXML(element);
        }
    }
    
    @Override
    public String getTag() {
        return "FoldingContainer";
    }
    
    public Alignment4 getTitleBarPosition() {
        return this.m_titleBarPosition;
    }
    
    public void setTitleBarPosition(final Alignment4 titleBarPosition) {
        this.m_titleBarPosition = titleBarPosition;
        this.invalidate();
    }
    
    public void setFolded(final boolean folded) {
        if (folded) {
            this.fold();
        }
        else {
            this.unfold();
        }
    }
    
    public boolean isFolded() {
        return this.m_folded;
    }
    
    public void fold() {
        this.m_folded = true;
        this.m_unfoldedExpandable = this.m_expandable;
        this.setExpandable(false);
        if (this.m_content != null) {
            this.m_content.setVisible(false);
        }
        this.invalidateMinSize();
    }
    
    public void unfold() {
        this.m_folded = false;
        this.setExpandable(this.m_unfoldedExpandable);
        this.m_content.setVisible(true);
        this.invalidateMinSize();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_titleBarPosition = null;
        this.m_content = null;
        this.m_titleBar = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final FoldingContainerLayout fcl = new FoldingContainerLayout();
        fcl.onCheckOut();
        this.add(fcl);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == FoldingContainer.TITLE_BAR_POSITION_HASH) {
            this.setTitleBarPosition(cl.convert(Alignment4.class, value));
        }
        else {
            if (hash != FoldingContainer.FOLDED_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setFolded(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == FoldingContainer.TITLE_BAR_POSITION_HASH) {
            this.setTitleBarPosition((Alignment4)value);
        }
        else {
            if (hash != FoldingContainer.FOLDED_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setFolded(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        TITLE_BAR_POSITION_HASH = "titleBarPosition".hashCode();
        FOLDED_HASH = "folded".hashCode();
    }
    
    public class FoldingContainerLayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            Dimension m_minSize;
            if (FoldingContainer.this.m_content.getVisible()) {
                m_minSize = FoldingContainer.this.m_content.getMinSize();
            }
            else {
                m_minSize = new Dimension(0, 0);
            }
            switch (FoldingContainer.this.m_titleBarPosition) {
                case NORTH:
                case SOUTH: {
                    final Dimension dimension = m_minSize;
                    dimension.height += FoldingContainer.this.m_titleBar.getMinSize().height;
                    m_minSize.width = Math.max(m_minSize.width, FoldingContainer.this.m_titleBar.getMinSize().width);
                    break;
                }
                case EAST:
                case WEST: {
                    final Dimension dimension2 = m_minSize;
                    dimension2.width += FoldingContainer.this.m_titleBar.getMinSize().width;
                    m_minSize.height = Math.max(m_minSize.height, FoldingContainer.this.m_titleBar.getMinSize().height);
                    break;
                }
            }
            return m_minSize;
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            Dimension m_prefSize;
            if (FoldingContainer.this.m_content.getVisible()) {
                m_prefSize = FoldingContainer.this.m_content.getPrefSize();
            }
            else {
                m_prefSize = new Dimension(0, 0);
            }
            switch (FoldingContainer.this.m_titleBarPosition) {
                case NORTH:
                case SOUTH: {
                    final Dimension dimension = m_prefSize;
                    dimension.height += FoldingContainer.this.m_titleBar.getPrefSize().height;
                    m_prefSize.width = Math.max(m_prefSize.width, FoldingContainer.this.m_titleBar.getPrefSize().width);
                    break;
                }
                case EAST:
                case WEST: {
                    final Dimension dimension2 = m_prefSize;
                    dimension2.width += FoldingContainer.this.m_titleBar.getPrefSize().width;
                    m_prefSize.height = Math.max(m_prefSize.height, FoldingContainer.this.m_titleBar.getPrefSize().height);
                    break;
                }
            }
            return m_prefSize;
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            final int availableHeight = parent.m_appearance.getContentHeight();
            final int availableWidth = parent.m_appearance.getContentWidth();
            switch (FoldingContainer.this.m_titleBarPosition) {
                case NORTH: {
                    FoldingContainer.this.m_titleBar.setSize(availableWidth, FoldingContainer.this.m_titleBar.getPrefSize().height);
                    FoldingContainer.this.m_titleBar.setPosition(0, availableHeight - FoldingContainer.this.m_titleBar.getHeight());
                    if (FoldingContainer.this.m_content.getVisible()) {
                        FoldingContainer.this.m_content.setSize(availableWidth, availableHeight - FoldingContainer.this.m_titleBar.getHeight());
                        FoldingContainer.this.m_content.setPosition(0, 0);
                        break;
                    }
                    break;
                }
                case SOUTH: {
                    FoldingContainer.this.m_titleBar.setSize(availableWidth, FoldingContainer.this.m_titleBar.getPrefSize().height);
                    FoldingContainer.this.m_titleBar.setPosition(0, 0);
                    if (FoldingContainer.this.m_content.getVisible()) {
                        FoldingContainer.this.m_content.setSize(availableWidth, availableHeight - FoldingContainer.this.m_titleBar.getHeight());
                        FoldingContainer.this.m_content.setPosition(0, FoldingContainer.this.m_titleBar.getHeight());
                        break;
                    }
                    break;
                }
                case WEST: {
                    FoldingContainer.this.m_titleBar.setSize(FoldingContainer.this.m_titleBar.getPrefSize().width, availableHeight);
                    FoldingContainer.this.m_titleBar.setPosition(0, 0);
                    if (FoldingContainer.this.m_content.getVisible()) {
                        FoldingContainer.this.m_content.setSize(availableWidth - FoldingContainer.this.m_titleBar.getWidth(), availableHeight);
                        FoldingContainer.this.m_content.setPosition(FoldingContainer.this.m_titleBar.getWidth(), 0);
                        break;
                    }
                    break;
                }
                case EAST: {
                    FoldingContainer.this.m_titleBar.setSize(FoldingContainer.this.m_titleBar.getPrefSize().width, availableHeight);
                    FoldingContainer.this.m_titleBar.setPosition(availableWidth - FoldingContainer.this.m_titleBar.getWidth(), 0);
                    if (FoldingContainer.this.m_content.getVisible()) {
                        FoldingContainer.this.m_content.setSize(availableWidth - FoldingContainer.this.m_titleBar.getWidth(), availableHeight);
                        FoldingContainer.this.m_content.setPosition(0, 0);
                        break;
                    }
                    break;
                }
            }
        }
    }
}
