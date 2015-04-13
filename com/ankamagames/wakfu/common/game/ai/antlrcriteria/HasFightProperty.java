package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;

public class HasFightProperty extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_fightPropertyId;
    private boolean m_target;
    
    public HasFightProperty(final ArrayList<ParserObject> args) {
        super();
        this.m_target = false;
        this.checkType(args);
        if (args.size() == 2) {
            final ParserObject targetOrCaster = args.remove(0);
            final String breed = ((StringObject)targetOrCaster).getValue();
            if (breed.equalsIgnoreCase("target")) {
                this.m_target = true;
            }
            else if (breed.equalsIgnoreCase("caster")) {
                this.m_target = false;
            }
        }
        this.m_fightPropertyId = args.get(0);
    }
    
    public int getFightPropertyId() {
        if (this.m_fightPropertyId.isConstant() && this.m_fightPropertyId.isInteger()) {
            return (int)this.m_fightPropertyId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasFightProperty.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        if (!(targetCharacter instanceof EffectUser)) {
            return -1;
        }
        final EffectUser character = (EffectUser)targetCharacter;
        final byte fightPropertyId = (byte)this.getFightPropertyId();
        final FightPropertyType property = FightPropertyType.getPropertyFromId(fightPropertyId);
        if (property == null) {
            throw new CriteriaExecutionException("Le param\u00e8tre du crit\u00e8re HasFightProperty doit \u00eatre un id de FightPropertyType valide (id=" + fightPropertyId + " n'existe pas)");
        }
        return character.isActiveProperty(property) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HASFIGHTPROPERTY;
    }
    
    static {
        HasFightProperty.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.NUMBER };
        HasFightProperty.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING, ParserType.NUMBER };
        HasFightProperty.signatures.add(sig);
    }
}
