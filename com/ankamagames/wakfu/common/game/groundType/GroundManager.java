package com.ankamagames.wakfu.common.game.groundType;

import gnu.trove.*;

public class GroundManager
{
    private static final GroundManager m_instance;
    private final TIntObjectHashMap<GroundType> m_grounds;
    
    public static GroundManager getInstance() {
        return GroundManager.m_instance;
    }
    
    private GroundManager() {
        super();
        this.m_grounds = new TIntObjectHashMap<GroundType>();
    }
    
    public void addGroundType(final GroundType gt) {
        this.m_grounds.put(gt.getId(), gt);
    }
    
    public GroundType getGroundType(final int id) {
        return this.m_grounds.get(id);
    }
    
    static {
        m_instance = new GroundManager();
    }
}
