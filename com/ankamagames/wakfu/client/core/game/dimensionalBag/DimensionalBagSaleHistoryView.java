package com.ankamagames.wakfu.client.core.game.dimensionalBag;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;

public class DimensionalBagSaleHistoryView extends ImmutableFieldProvider
{
    public static final String SALES_FIELD = "sales";
    public static final String HAS_SALES_FIELD = "hasSales";
    public static final String HAS_PREVIOUS_PAGES_FIELD = "hasPrevious";
    public static final String HAS_NEXT_PAGES_FIELD = "hasNext";
    public static final String PAGE_NUMBER_FIELD = "pageNumberDesc";
    private List<SaleDayView> m_salesPerDay;
    private int m_currentPage;
    private int m_numPages;
    
    public DimensionalBagSaleHistoryView(final List<SaleDayView> view) {
        super();
        this.m_salesPerDay = view;
        this.m_numPages = this.m_salesPerDay.size();
    }
    
    public void nextPage() {
        if (this.m_currentPage + 1 >= this.m_numPages) {
            return;
        }
        ++this.m_currentPage;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasNext", "hasPrevious", "pageNumberDesc", "sales");
    }
    
    public void previousPage() {
        if (this.m_currentPage - 1 < 0) {
            return;
        }
        --this.m_currentPage;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasNext", "hasPrevious", "pageNumberDesc", "sales");
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("hasSales")) {
            return this.m_numPages > 0;
        }
        if (fieldName.equals("hasNext")) {
            return this.m_currentPage + 1 < this.m_numPages;
        }
        if (fieldName.equals("hasPrevious")) {
            return this.m_currentPage > 0;
        }
        if (fieldName.equals("pageNumberDesc")) {
            return WakfuTranslator.getInstance().getString("pageNumber", this.m_currentPage + 1, this.m_numPages);
        }
        if (fieldName.equals("sales")) {
            return (this.m_salesPerDay != null && this.m_salesPerDay.size() > this.m_currentPage) ? this.m_salesPerDay.get(this.m_currentPage) : null;
        }
        return null;
    }
}
