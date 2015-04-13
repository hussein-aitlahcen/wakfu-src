package com.ankamagames.wakfu.common.datas.Breed;

import java.util.*;

public class CharacBoundByLevelTable
{
    public static final CharacBoundByLevelTable m_instance;
    HashMap<Byte, CharacBoundByLevel> m_characBoundsByLevel;
    
    public static CharacBoundByLevelTable getInstance() {
        return CharacBoundByLevelTable.m_instance;
    }
    
    public final void addCharacBoundByLevel(final CharacBoundByLevel characBoundByLevel) {
        this.m_characBoundsByLevel.put(characBoundByLevel.getCharacId(), characBoundByLevel);
    }
    
    public Iterable<CharacBoundByLevel> getCharacBoundsByLevel() {
        return this.m_characBoundsByLevel.values();
    }
    
    public CharacBoundByLevelTable() {
        super();
        this.m_characBoundsByLevel = new HashMap<Byte, CharacBoundByLevel>();
    }
    
    static {
        m_instance = new CharacBoundByLevelTable();
    }
}
