package com.ankamagames.wakfu.client.core.game.ressource;

import com.ankamagames.wakfu.common.game.characterInfo.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class MonsterResource extends Resource implements MonsterGenerator
{
    private static ObjectPool m_monsterResourcePool;
    
    public static Resource checkOut(final int worldX, final int worldY, final short altitude, final int resourceReferenceId, final byte stepId, final boolean justGrowth, final boolean autoRespawn) {
        MonsterResource monsterResource;
        try {
            monsterResource = (MonsterResource)MonsterResource.m_monsterResourcePool.borrowObject();
        }
        catch (Exception e) {
            MonsterResource.m_logger.error((Object)"Erreur lors de l'extraction d'une monsterResource du pool", (Throwable)e);
            monsterResource = new MonsterResource();
        }
        if (!monsterResource.initialize(worldX, worldY, altitude, resourceReferenceId, stepId, justGrowth, autoRespawn)) {
            return null;
        }
        return monsterResource;
    }
    
    @Override
    public void release() {
        if (MonsterResource.m_monsterResourcePool != null) {
            try {
                MonsterResource.m_monsterResourcePool.returnObject(this);
            }
            catch (Exception e) {
                MonsterResource.m_logger.error((Object)"Erreur lors du retour d'une resource au pool", (Throwable)e);
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public TIntHashSet getGeneratedMonsterFamilyIds() {
        final TIntHashSet monsters = new TIntHashSet();
        monsters.addAll(((MonsterReferenceResource)this.m_referenceResource).getMonsterFamilies());
        return monsters;
    }
    
    static {
        MonsterResource.m_monsterResourcePool = new MonitoredPool(new ObjectFactory<Resource>() {
            @Override
            public Resource makeObject() {
                return new MonsterResource(null);
            }
        });
    }
}
