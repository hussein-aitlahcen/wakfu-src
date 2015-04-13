package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.world.*;
import com.ankamagames.wakfu.common.game.protector.*;

public class IsZoneInChaos extends FunctionCriterion
{
    private static final Logger m_logger;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsZoneInChaos.signatures;
    }
    
    public IsZoneInChaos(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    public IsZoneInChaos() {
        super();
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        int territoryId;
        if (criterionUser instanceof ProtectorBase) {
            territoryId = ((ProtectorBase)criterionUser).getTerritory().getId();
        }
        else {
            if (!(criterionUser instanceof BasicCharacterInfo)) {
                throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer le territoire");
            }
            territoryId = ((BasicCharacterInfo)criterionUser).getCurrentTerritoryId();
        }
        if (TerritoryConstants.IGNORED_TERRITORIES.contains(territoryId)) {
            return -1;
        }
        final AbstractTerritory territory = TerritoryManager.INSTANCE.getTerritory(territoryId);
        if (territory == null) {
            return -1;
        }
        final TerritoryChaosHandler chaosHandler = territory.getChaosHandler();
        if (chaosHandler == null) {
            return -1;
        }
        return chaosHandler.isInChaos() ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ZONE_IN_CHAOS;
    }
    
    static {
        m_logger = Logger.getLogger((Class)IsZoneInChaos.class);
        IsZoneInChaos.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        IsZoneInChaos.signatures.add(sig);
    }
}
