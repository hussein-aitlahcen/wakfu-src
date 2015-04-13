package com.ankamagames.wakfu.common.game.skill;

import org.apache.log4j.*;
import gnu.trove.*;

public class ActionVisualManager
{
    protected static final Logger m_logger;
    private static final ActionVisualManager m_uniqueInstance;
    private final TIntObjectHashMap<ActionVisual> m_skills;
    
    public ActionVisualManager() {
        super();
        this.m_skills = new TIntObjectHashMap<ActionVisual>();
    }
    
    public static ActionVisualManager getInstance() {
        return ActionVisualManager.m_uniqueInstance;
    }
    
    public void add(final ActionVisual actionVisual) {
        this.m_skills.put(actionVisual.getVisualId(), actionVisual);
    }
    
    public ActionVisual get(final int visualId) {
        return this.m_skills.get(visualId);
    }
    
    public boolean isEmpty() {
        return this.m_skills.isEmpty();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActionVisualManager.class);
        m_uniqueInstance = new ActionVisualManager();
    }
}
