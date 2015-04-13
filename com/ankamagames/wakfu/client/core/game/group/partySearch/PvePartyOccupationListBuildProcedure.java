package com.ankamagames.wakfu.client.core.game.group.partySearch;

import gnu.trove.*;
import com.google.common.collect.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;

class PvePartyOccupationListBuildProcedure implements TObjectProcedure<PartyOccupation>
{
    private final Multimap<Integer, PvePartyOccupation> m_pveOccupationMultimap;
    
    private PvePartyOccupationListBuildProcedure() {
        super();
        this.m_pveOccupationMultimap = (Multimap<Integer, PvePartyOccupation>)ArrayListMultimap.create();
    }
    
    @Override
    public boolean execute(final PartyOccupation object) {
        if (object.getOccupationType() == PartyOccupationType.MONSTER || object.getOccupationType() == PartyOccupationType.DUNGEON) {
            final PvePartyOccupation occupation = (PvePartyOccupation)object;
            this.m_pveOccupationMultimap.put((Object)occupation.getMonsterType(), (Object)occupation);
        }
        return true;
    }
    
    public static ImmutableMultimap<Integer, PvePartyOccupation> getMap() {
        final PvePartyOccupationListBuildProcedure procedure = new PvePartyOccupationListBuildProcedure();
        PartyOccupationManager.INSTANCE.forEachOccupation(procedure);
        return (ImmutableMultimap<Integer, PvePartyOccupation>)ImmutableMultimap.copyOf((Multimap)procedure.m_pveOccupationMultimap);
    }
}
