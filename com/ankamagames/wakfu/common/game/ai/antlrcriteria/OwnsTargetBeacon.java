package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

public class OwnsTargetBeacon extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return OwnsTargetBeacon.signatures;
    }
    
    public OwnsTargetBeacon(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionUser == null || !(criterionUser instanceof BasicCharacterInfo)) {
            throw new CriteriaExecutionException("on test l'appartenance d'une balise \u00e0 autre chose qu'un perso");
        }
        if (!(criterionTarget instanceof AbstractBeaconEffectArea)) {
            if (criterionTarget instanceof Point3 && criterionContext instanceof AbstractFight) {
                final AbstractFight<BasicCharacterInfo> fight = (AbstractFight<BasicCharacterInfo>)criterionContext;
                final Point3 position = (Point3)criterionTarget;
                for (final EffectUser effectUser : fight.getPossibleTargetsAtPosition(position)) {
                    if (effectUser instanceof AbstractBeaconEffectArea) {
                        if (((AbstractBeaconEffectArea)effectUser).getOwner() != criterionUser) {
                            return 0;
                        }
                        return -1;
                    }
                }
            }
            throw new CriteriaExecutionException("on test l'appartenance d'autre chose qu'une balise");
        }
        if (((AbstractBeaconEffectArea)criterionTarget).getOwner() != criterionUser) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.OwnsTargetBeacon;
    }
    
    static {
        signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        OwnsTargetBeacon.signatures.add(sig);
    }
}
