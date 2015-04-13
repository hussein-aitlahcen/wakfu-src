package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.protector.*;
import java.util.*;
import gnu.trove.*;

public class GetProtectorNationId extends FunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    private NumericalValue m_protectorId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return Collections.unmodifiableList((List<? extends ParserType[]>)GetProtectorNationId.SIGNATURES);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetProtectorNationId(final List<ParserObject> args) {
        super();
        this.checkType(args);
        if (args.size() == 1) {
            this.m_protectorId = args.get(0);
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final int protectorId = (int)this.m_protectorId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final TIntObjectIterator<Nation> nationIterator = NationManager.INSTANCE.nationsIterator();
        while (nationIterator.hasNext()) {
            nationIterator.advance();
            final Nation nation = nationIterator.value();
            final NationProtectorInfoManager protectorManager = nation.getProtectorInfoManager();
            final NationProtectorInfo nationProtectorInfo = protectorManager.getProtectorInfo(protectorId);
            if (nationProtectorInfo != null) {
                return this.getSign() * nation.getNationId();
            }
        }
        throw new CriteriaExecutionException("protecteur non trouv\u00e9 dans le NationManager : protectorId=" + protectorId);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_PROTECTOR_NATION_ID;
    }
    
    @Override
    public String toString() {
        return "GetProtectorNationId{m_protectorId=" + this.m_protectorId + '}';
    }
    
    static {
        SIGNATURES = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        GetProtectorNationId.SIGNATURES.add(sig);
    }
}
