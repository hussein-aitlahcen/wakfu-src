package com.ankamagames.wakfu.client.core.game.travel;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;

public class ZaapIslandFieldProvider extends ImmutableFieldProvider
{
    public static final String ISLAND_FIELD = "island";
    public static final String LINK_LIST_FIELD = "links";
    private final int m_instanceId;
    private final ArrayList<ZaapInfoFieldProvider> m_zaapList;
    
    public ZaapIslandFieldProvider(final int instanceId) {
        super();
        this.m_zaapList = new ArrayList<ZaapInfoFieldProvider>();
        this.m_instanceId = instanceId;
    }
    
    public void addZaap(final ZaapInfoFieldProvider zaapInfo) {
        this.m_zaapList.add(zaapInfo);
        Collections.sort(this.m_zaapList, ZaapNameComparator.COMPARATOR);
    }
    
    public int getInstanceId() {
        return this.m_instanceId;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("island")) {
            return WakfuTranslator.getInstance().getString(77, this.m_instanceId, new Object[0]);
        }
        if (fieldName.equals("links")) {
            return this.m_zaapList;
        }
        return null;
    }
    
    private static final class ZaapNameComparator implements Comparator<ZaapInfoFieldProvider>
    {
        static final Comparator<ZaapInfoFieldProvider> COMPARATOR;
        
        @Override
        public int compare(final ZaapInfoFieldProvider o1, final ZaapInfoFieldProvider o2) {
            return o1.getName().compareTo(o2.getName());
        }
        
        static {
            COMPARATOR = new ZaapNameComparator();
        }
    }
}
