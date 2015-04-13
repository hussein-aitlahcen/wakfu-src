package com.ankamagames.wakfu.client.core.game.tutorial;

import gnu.trove.*;

public class TutorialManager
{
    public static final TutorialManager INSTANCE;
    private final TIntObjectHashMap<TutorialPage> m_eventsByTutorial;
    
    private TutorialManager() {
        super();
        this.m_eventsByTutorial = new TIntObjectHashMap<TutorialPage>();
    }
    
    public void addTutorialPage(final int id, final int[] events) {
        this.m_eventsByTutorial.put(id, new TutorialPage(id, events));
    }
    
    public boolean forEachPage(final TIntObjectProcedure<TutorialPage> procedure) {
        return this.m_eventsByTutorial.forEachEntry(procedure);
    }
    
    static {
        INSTANCE = new TutorialManager();
    }
}
