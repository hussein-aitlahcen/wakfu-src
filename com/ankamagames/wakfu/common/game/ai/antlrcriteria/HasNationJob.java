package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.datas.*;

public final class HasNationJob extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_nationJobId;
    
    public HasNationJob() {
        super();
    }
    
    public HasNationJob(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_nationJobId = args.get(0);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasNationJob.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_NATION_JOB;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (user == null || !(user instanceof Citizen)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        final byte nationJobId = (byte)this.m_nationJobId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final NationJob job = NationJob.fromId(nationJobId);
        return user.getCitizenComportment().hasJob(job) ? 0 : -1;
    }
    
    static {
        HasNationJob.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        HasNationJob.signatures.add(sig);
    }
}
