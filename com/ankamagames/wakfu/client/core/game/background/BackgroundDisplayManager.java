package com.ankamagames.wakfu.client.core.game.background;

import java.util.*;
import gnu.trove.*;

public class BackgroundDisplayManager
{
    public static final BackgroundDisplayManager INSTANCE;
    private final TIntObjectHashMap<BackgroundDisplayData> m_backgroundDisplayData;
    
    public BackgroundDisplayManager() {
        super();
        this.m_backgroundDisplayData = new TIntObjectHashMap<BackgroundDisplayData>();
    }
    
    public void addBackgroundDisplay(final BackgroundDisplayData backgroundDisplayData) {
        this.m_backgroundDisplayData.put(backgroundDisplayData.getId(), backgroundDisplayData);
    }
    
    public BackgroundDisplayData getBackgroundDisplayData(final int id) {
        return this.m_backgroundDisplayData.get(id);
    }
    
    public ArrayList<BackgroundDisplayData> getBackgroundDisplayDataOfType(final BackgroundDisplayType type) {
        final ArrayList<BackgroundDisplayData> list = new ArrayList<BackgroundDisplayData>();
        final TIntObjectIterator<BackgroundDisplayData> it = this.m_backgroundDisplayData.iterator();
        while (it.hasNext()) {
            it.advance();
            final BackgroundDisplayData data = it.value();
            if (data.getType() == type) {
                list.add(data);
            }
        }
        return list;
    }
    
    static {
        INSTANCE = new BackgroundDisplayManager();
    }
}
