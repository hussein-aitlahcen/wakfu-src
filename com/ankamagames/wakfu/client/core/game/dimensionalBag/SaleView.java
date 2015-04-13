package com.ankamagames.wakfu.client.core.game.dimensionalBag;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.fight.history.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;

public class SaleView extends ImmutableFieldProvider
{
    public static final String TIME_FIELD = "time";
    public static final String ITEMS_FIELD = "items";
    public static final String PLAYER_NAME_FIELD = "playerName";
    public static final String KAMAS_FIELD = "kamas";
    private GameDateConst m_date;
    private String m_playerName;
    private List<ReferenceItemFieldProvider> m_items;
    private long m_kamas;
    public final String[] FIELDS;
    
    public SaleView(final GameDateConst date, final String playerName, final long kamas) {
        super();
        this.m_items = new ArrayList<ReferenceItemFieldProvider>();
        this.FIELDS = new String[] { "time", "items", "playerName", "kamas" };
        this.m_date = date;
        this.m_playerName = playerName;
        this.m_kamas = kamas;
    }
    
    public void addItem(final int refId, final short quantity) {
        this.m_items.add(new ReferenceItemFieldProvider(refId, quantity));
    }
    
    public int getNumItems() {
        return this.m_items.size();
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("time")) {
            return WakfuTranslator.getInstance().formatTimeShort(this.m_date);
        }
        if (fieldName.equals("items")) {
            return this.m_items;
        }
        if (fieldName.equals("playerName")) {
            return this.m_playerName;
        }
        if (fieldName.equals("kamas")) {
            return this.m_kamas;
        }
        return null;
    }
    
    public GameDateConst getDate() {
        return this.m_date;
    }
    
    public long getKamas() {
        return this.m_kamas;
    }
}
