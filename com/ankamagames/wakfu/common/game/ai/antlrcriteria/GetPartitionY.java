package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetPartitionY extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private boolean m_useTarget;
    
    public GetPartitionY(final ArrayList<ParserObject> args) {
        super();
        final byte sigIdx = this.checkType(args);
        if (sigIdx == 1) {
            this.m_useTarget = args.get(0).getValue().equalsIgnoreCase("target");
        }
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetPartitionY.SIGNATURES;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            throw new CriteriaExecutionException("Pas de personnage trouv\u00e9 pour executer le critere");
        }
        return PartitionConstants.getPartitionXFromCellX(target.getWorldCellY());
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_PARTITION_Y;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        GetPartitionY.SIGNATURES.add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
