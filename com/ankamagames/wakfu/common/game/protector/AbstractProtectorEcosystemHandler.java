package com.ankamagames.wakfu.common.game.protector;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.datas.*;

public abstract class AbstractProtectorEcosystemHandler
{
    protected static Logger m_logger;
    protected static final float MONSTER_REINTRODUCTION_LIMIT = 0.05f;
    protected static final float RESOURCE_REINTRODUCTION_LIMIT = 0.05f;
    protected final ProtectorBase m_protector;
    protected final TIntHashSet m_protectedMonsterFamilies;
    protected final TIntHashSet m_protectedResourceFamilies;
    
    protected AbstractProtectorEcosystemHandler(final ProtectorBase protector) {
        super();
        this.m_protector = protector;
        this.m_protectedMonsterFamilies = new TIntHashSet();
        this.m_protectedResourceFamilies = new TIntHashSet();
    }
    
    public boolean isProtectedMonsterFamily(final int monsterFamilyId) {
        return this.m_protectedMonsterFamilies.contains(monsterFamilyId);
    }
    
    public boolean isProtectedResourceFamily(final int resourceFamilyId) {
        return this.m_protectedResourceFamilies.contains(resourceFamilyId);
    }
    
    public boolean protectMonsterFamily(final int monsterFamilyId) {
        this.m_protectedMonsterFamilies.add(monsterFamilyId);
        return true;
    }
    
    public boolean protectResourceFamily(final int resourceFamilyId) {
        this.m_protectedResourceFamilies.add(resourceFamilyId);
        return true;
    }
    
    public boolean unprotectMonsterFamily(final int monsterFamilyId) {
        this.m_protectedMonsterFamilies.remove(monsterFamilyId);
        return true;
    }
    
    public boolean unprotectResourceFamily(final int resourceFamilyId) {
        this.m_protectedResourceFamilies.remove(resourceFamilyId);
        return true;
    }
    
    public abstract boolean canMonsterFamilyBeReintroduced(final int p0);
    
    public abstract boolean canResourceFamilyBeReintroduced(final int p0);
    
    public abstract boolean reintroduceMonsterFamily(final BasicCharacterInfo p0, final int p1);
    
    public abstract boolean reintroduceResourceFamily(final BasicCharacterInfo p0, final int p1);
    
    public TIntHashSet getProtectedMonsterFamilies() {
        return this.m_protectedMonsterFamilies;
    }
    
    public TIntHashSet getProtectedResourceFamilies() {
        return this.m_protectedResourceFamilies;
    }
    
    static {
        AbstractProtectorEcosystemHandler.m_logger = Logger.getLogger((Class)AbstractProtectorEcosystemHandler.class);
    }
}
