package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.protector.*;

public class GetTerritoryNationId extends FunctionValue
{
    private static final ArrayList<ParserType[]> signatures;
    private NumericalValue m_zoneId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetTerritoryNationId.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetTerritoryNationId(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        if (args.size() == 1) {
            this.m_zoneId = args.get(0);
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        int zoneId;
        if (this.m_zoneId == null) {
            final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
            if (user == null) {
                throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer d'utilisateur pour ce crit\u00e8re");
            }
            zoneId = user.getCurrentTerritoryId();
        }
        else {
            zoneId = (int)this.m_zoneId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        }
        final AbstractTerritory territory = TerritoryManager.INSTANCE.getTerritory(zoneId);
        if (territory == null) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer le territoire " + zoneId);
        }
        final ProtectorBase protector = territory.getProtector();
        if (protector == null) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer le protecteur du territoire " + zoneId);
        }
        return super.getSign() * protector.getCurrentNationId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETNATIONID;
    }
    
    static {
        signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.NUMBER };
        GetTerritoryNationId.signatures.add(sig);
        sig = new ParserType[0];
        GetTerritoryNationId.signatures.add(sig);
    }
}
