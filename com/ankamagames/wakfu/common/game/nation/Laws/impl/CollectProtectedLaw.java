package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;
import gnu.trove.*;

public class CollectProtectedLaw extends NationLaw<CollectLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<CollectProtectedLaw> MODEL;
    
    private CollectProtectedLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.COLLECT_ECO_PROTECTED;
    }
    
    @Override
    public boolean isTriggering(final CollectLawEvent event) {
        if (!event.getCollectAction().isDestructive()) {
            return false;
        }
        final Collectible collectible = event.getCollectible();
        final int familyId = collectible.getFamilyId();
        final AbstractProtectorEcosystemHandler ecosystemHandler = event.getProtectorEcosystemHandler();
        if (ecosystemHandler == null) {
            return false;
        }
        if (collectible instanceof MonsterGenerator) {
            final MonsterGenerator generator = (MonsterGenerator)collectible;
            final TIntHashSet monsterFamilyIds = generator.getGeneratedMonsterFamilyIds();
            final TIntIterator it = monsterFamilyIds.iterator();
            while (it.hasNext()) {
                if (ecosystemHandler.isProtectedMonsterFamily(it.next())) {
                    return true;
                }
            }
        }
        return ecosystemHandler.isProtectedResourceFamily(familyId);
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<CollectProtectedLaw>() {
            @Override
            public CollectProtectedLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new CollectProtectedLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return CollectProtectedLaw.PARAMETERS;
            }
        };
    }
}
