package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class NbAreasWithBaseId extends NbAreas
{
    private static ArrayList<ParserType[]> signatures;
    private long m_areaBaseId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return NbAreasWithBaseId.signatures;
    }
    
    public NbAreasWithBaseId(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        this.m_isForAll = true;
        this.m_targetType = "caster";
        if (paramType == 0) {
            this.m_areaBaseId = args.get(0).getLongValue(null, null, null, null);
        }
        else if (paramType == 1) {
            this.m_isForAll = false;
            this.m_areaBaseId = args.get(0).getLongValue(null, null, null, null);
            this.m_targetType = args.get(1).getValue();
        }
    }
    
    @Override
    protected int getAreaType() {
        return -1;
    }
    
    @Override
    protected boolean isConcernedArea(final BasicEffectArea area) {
        return area.getBaseId() == this.m_areaBaseId;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.NB_AREAS_WITH_BASE_ID;
    }
    
    static {
        (NbAreasWithBaseId.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_NUMBER_SIGNATURE);
        NbAreasWithBaseId.signatures.add(new ParserType[] { ParserType.NUMBER, ParserType.STRING });
    }
}
