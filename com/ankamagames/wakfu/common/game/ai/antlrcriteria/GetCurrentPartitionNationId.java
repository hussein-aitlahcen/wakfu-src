package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetCurrentPartitionNationId extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private boolean target;
    
    public GetCurrentPartitionNationId(final ArrayList<ParserObject> args) {
        super();
        this.target = (this.checkType(args) == 0 && args.get(0).getValue().equalsIgnoreCase("target"));
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetCurrentPartitionNationId.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(this.target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user == null) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        return super.getSign() * user.getCurrentTerritoryNationId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_CURRENT_PARTITION_NATION_ID;
    }
    
    static {
        GetCurrentPartitionNationId.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        GetCurrentPartitionNationId.signatures.add(sig);
        sig = new ParserType[0];
        GetCurrentPartitionNationId.signatures.add(sig);
    }
}
