package com.ankamagames.wakfu.client.core.game.tutorial;

import org.jetbrains.annotations.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.*;

public class TutorialPage
{
    private final int m_id;
    private final int[] m_eventIds;
    private static int m_staticOrder;
    private int m_order;
    
    public TutorialPage(final int id, @NotNull final int... eventIds) {
        super();
        this.m_id = id;
        this.m_eventIds = eventIds;
        this.m_order = TutorialPage.m_staticOrder;
        ++TutorialPage.m_staticOrder;
    }
    
    public int getOrder() {
        return this.m_order;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean forEachEvent(final TIntProcedure procedure) {
        for (final int eventId : this.m_eventIds) {
            procedure.execute(eventId);
        }
        return true;
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(139, this.m_id, new Object[0]);
    }
}
