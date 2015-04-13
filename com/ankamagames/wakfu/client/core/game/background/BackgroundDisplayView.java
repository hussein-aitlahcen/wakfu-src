package com.ankamagames.wakfu.client.core.game.background;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;

public class BackgroundDisplayView implements FieldProvider
{
    public static final String BACKGROUND_IMAGE_FIELD = "backgroundImage";
    public static final String BACKGROUND_SIZE_FIELD = "backgroundSize";
    public static final String LEFT_PAGE_FIELD = "leftPage";
    public static final String RIGHT_PAGE_FIELD = "rightPage";
    public static final String PREVIOUS_EXISTING_FIELD = "previousExisting";
    public static final String NEXT_EXISTING_FIELD = "nextExisting";
    public static final String HAS_PAGES_FIELD = "hasPages";
    public static final String[] FIELDS;
    private int m_currentLeftPageIndex;
    private final PageView[] m_pages;
    private final BackgroundDisplayType m_type;
    private final Dimension m_backgroundSize;
    
    public BackgroundDisplayView(final BackgroundDisplayType type, final PageView[] pages) {
        super();
        this.m_currentLeftPageIndex = -1;
        this.m_type = type;
        this.m_pages = pages;
        this.m_currentLeftPageIndex = 0;
        this.m_backgroundSize = (this.m_type.isDoublePage() ? new Dimension(1000, 612) : new Dimension(450, 612));
    }
    
    @Override
    public String[] getFields() {
        return BackgroundDisplayView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("backgroundImage")) {
            return (this.m_type == BackgroundDisplayType.EMPTY) ? null : WakfuConfiguration.getInstance().getDisplayBackgroundBackgroundImage(this.m_type.getId());
        }
        if (fieldName.equals("backgroundSize")) {
            return this.m_backgroundSize;
        }
        if (fieldName.equals("leftPage")) {
            return this.m_pages[this.m_currentLeftPageIndex];
        }
        if (fieldName.equals("rightPage")) {
            final int rightPageIndex = this.m_currentLeftPageIndex + 1;
            return (rightPageIndex < this.m_pages.length) ? this.m_pages[rightPageIndex] : null;
        }
        if (fieldName.equals("previousExisting")) {
            return this.isPreviousExisting();
        }
        if (fieldName.equals("nextExisting")) {
            return this.isNextExisting();
        }
        if (fieldName.equals("hasPages")) {
            return this.m_pages.length > 2;
        }
        return null;
    }
    
    private boolean isPreviousExisting() {
        return this.m_currentLeftPageIndex - 2 >= 0;
    }
    
    private boolean isNextExisting() {
        return this.m_currentLeftPageIndex + 2 < this.m_pages.length;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public void setPreviousPage() {
        if (!this.isPreviousExisting()) {
            return;
        }
        this.m_currentLeftPageIndex -= 2;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, BackgroundDisplayView.FIELDS);
    }
    
    public void setNextPage() {
        if (!this.isNextExisting()) {
            return;
        }
        this.m_currentLeftPageIndex += 2;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, BackgroundDisplayView.FIELDS);
    }
    
    public BackgroundDisplayType getType() {
        return this.m_type;
    }
    
    static {
        FIELDS = new String[] { "backgroundImage", "backgroundSize", "leftPage", "rightPage", "previousExisting", "nextExisting", "hasPages" };
    }
}
