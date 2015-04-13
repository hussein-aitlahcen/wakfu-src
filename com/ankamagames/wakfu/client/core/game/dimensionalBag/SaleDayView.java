package com.ankamagames.wakfu.client.core.game.dimensionalBag;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;

public class SaleDayView implements FieldProvider
{
    public static final String DATE_FIELD = "date";
    public static final String SALES_FIELD = "sales";
    public static final String TOTAL_SALES_FIELD = "totalSales";
    private GameDateConst m_date;
    private final SortedList<SaleView> m_sales;
    public static final String[] FIELDS;
    
    public SaleDayView(final GameDateConst date) {
        super();
        this.m_sales = new SortedList<SaleView>(new Comparator<SaleView>() {
            @Override
            public int compare(final SaleView o1, final SaleView o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        this.m_date = new GameDate(date);
    }
    
    public void addSale(final SaleView saleView) {
        this.m_sales.add(saleView);
    }
    
    @Override
    public String[] getFields() {
        return SaleDayView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("date")) {
            return this.m_date.equals(WakfuGameCalendar.getInstance().getDate().dayToLong()) ? WakfuTranslator.getInstance().getString("flea.salesLog.today") : WakfuTranslator.getInstance().formatDateFull(this.m_date);
        }
        if (fieldName.equals("sales")) {
            return this.m_sales;
        }
        if (fieldName.equals("totalSales")) {
            int sales = 0;
            for (int i = this.m_sales.size() - 1; i >= 0; --i) {
                sales += (int)this.m_sales.get(i).getKamas();
            }
            return sales;
        }
        return null;
    }
    
    public int getTotalNumItems() {
        int numItems = 0;
        for (int i = this.m_sales.size() - 1; i >= 0; --i) {
            numItems += this.m_sales.get(i).getNumItems();
        }
        return numItems;
    }
    
    public List<SaleView> getSales() {
        return this.m_sales;
    }
    
    public GameDateConst getDate() {
        return this.m_date;
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
    
    static {
        FIELDS = new String[] { "date", "sales" };
    }
}
