package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;

public class GetNationAlignment extends FunctionValue
{
    private static final int PLAYER_NATION = -1;
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_nation1;
    private NumericalValue m_nation2;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetNationAlignment.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetNationAlignment(final ArrayList<ParserObject> args) {
        super();
        final byte type = this.checkType(args);
        if (type == 0) {
            this.m_nation1 = null;
            this.m_nation2 = args.get(0);
        }
        else if (type == 1) {
            this.m_nation1 = args.get(0);
            this.m_nation2 = args.get(1);
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        int nation1;
        if (this.m_nation1 == null) {
            final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
            if (user == null) {
                throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
            }
            nation1 = user.getCitizenComportment().getNationId();
        }
        else {
            nation1 = (int)this.m_nation1.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        }
        final int nation2 = (int)this.m_nation2.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final NationAlignement alignment = NationManager.INSTANCE.getNationById(nation1).getDiplomacyManager().getAlignment(nation2);
        return super.getSign() * alignment.getId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETNATIONALIGNMENT;
    }
    
    public NumericalValue getNation1() {
        return this.m_nation1;
    }
    
    public NumericalValue getNation2() {
        return this.m_nation2;
    }
    
    static {
        GetNationAlignment.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.NUMBER };
        GetNationAlignment.signatures.add(sig);
        sig = new ParserType[2];
        sig[0] = (sig[1] = ParserType.NUMBER);
        GetNationAlignment.signatures.add(sig);
    }
}
