package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.common.datas.*;

public final class HasPvpRank extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_pvpRankId;
    
    public HasPvpRank() {
        super();
    }
    
    public HasPvpRank(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_pvpRankId = args.get(0);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasPvpRank.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HASPVPRANK;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (user == null || !(user instanceof Citizen)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        final byte pvpRankId = (byte)this.m_pvpRankId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final NationPvpRanks rank = NationPvpRanks.getById(pvpRankId);
        return user.getCitizenComportment().getPvpRank().isLessThan(rank) ? -1 : 0;
    }
    
    public byte getPvpRankId() {
        return (byte)this.m_pvpRankId.getLongValue(null, null, null, null);
    }
    
    static {
        HasPvpRank.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        HasPvpRank.signatures.add(sig);
    }
}
