package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.los.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

abstract class GetFightersCountInRange extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    NumericalValue m_maxRange;
    NumericalValue m_minRange;
    boolean m_target;
    ConstantBooleanCriterion m_inLine;
    ConstantBooleanCriterion m_testLoS;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetFightersCountInRange.signatures;
    }
    
    GetFightersCountInRange() {
        super();
    }
    
    GetFightersCountInRange(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        if (args.size() == 1) {
            this.m_maxRange = args.get(0);
        }
        else if (args.size() == 2) {
            this.m_maxRange = args.get(0);
            this.m_target = args.get(1).getValue().equalsIgnoreCase("target");
        }
        else if (args.size() == 3) {
            this.m_minRange = args.get(0);
            this.m_maxRange = args.get(1);
            this.m_target = args.get(2).getValue().equalsIgnoreCase("target");
        }
        else if (args.size() == 4) {
            this.m_minRange = args.get(0);
            this.m_maxRange = args.get(1);
            this.m_target = args.get(2).getValue().equalsIgnoreCase("target");
            this.m_inLine = args.get(3);
        }
        else if (args.size() == 5) {
            this.m_minRange = args.get(0);
            this.m_maxRange = args.get(1);
            this.m_target = args.get(2).getValue().equalsIgnoreCase("target");
            this.m_inLine = args.get(3);
            this.m_testLoS = args.get(4);
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionContext == null) {
            throw new CriteriaExecutionException("Pas de contexte...");
        }
        final AbstractFight<?> fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        if (fight == null) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer le combat");
        }
        this.hook(criterionUser, criterionTarget, criterionContext, criterionContent);
        final CriterionUser user = CriteriaUtils.getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        Point3 center = null;
        CriterionUser targetCharacter = null;
        if (!this.m_target) {
            if (user != null) {
                center = user.getPosition();
            }
        }
        else if (criterionTarget instanceof Point3) {
            center = (Point3)criterionTarget;
        }
        else {
            targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(true, criterionUser, criterionTarget, criterionContext, criterionContent);
            if (targetCharacter != null) {
                center = targetCharacter.getPosition();
            }
        }
        if (center == null) {
            return 0L;
        }
        final Collection<? extends EffectUser> fighters = this.getFighters(fight, user);
        final Iterator<? extends EffectUser> it = fighters.iterator();
        while (it.hasNext()) {
            final EffectUser fighter = (EffectUser)it.next();
            if (fighter.isActiveProperty(FightPropertyType.CANNOT_BE_EFFECT_TARGET)) {
                it.remove();
            }
        }
        final long maxRange = this.m_maxRange.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (maxRange <= 0L) {
            return super.getSign() * fighters.size();
        }
        final long minRange = (this.m_minRange == null) ? 0L : this.m_minRange.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final boolean inLine = this.m_inLine != null && this.m_inLine.isValid(criterionUser, criterionTarget, criterionContent, criterionContext);
        final boolean testLoS = this.m_testLoS != null && this.m_testLoS.isValid(criterionUser, criterionTarget, criterionContent, criterionContext);
        long value = 0L;
        for (final EffectUser fighter2 : fighters) {
            int distanceBetweenUserAndFighter;
            if (targetCharacter != null) {
                distanceBetweenUserAndFighter = DistanceUtils.getIntersectionDistance(fighter2, targetCharacter);
            }
            else {
                distanceBetweenUserAndFighter = DistanceUtils.getIntersectionDistance(fighter2, center);
            }
            if (distanceBetweenUserAndFighter <= maxRange && distanceBetweenUserAndFighter >= minRange) {
                final Point3 fighterPosition = fighter2.getPosition();
                if (inLine && center.getX() != fighterPosition.getX() && center.getY() != fighterPosition.getY()) {
                    continue;
                }
                if (testLoS) {
                    final FightMap fightMap = fight.getFightMap();
                    final LineOfSightChecker losChecker = LineOfSightChecker.checkOut();
                    losChecker.setTopologyMapInstanceSet(fightMap);
                    losChecker.setStartPoint(center.getX(), center.getY(), center.getZ());
                    losChecker.setEndPoint(fighterPosition.getX(), fighterPosition.getY(), fighterPosition.getZ());
                    if (!losChecker.checkLOS()) {
                        continue;
                    }
                }
                ++value;
            }
        }
        return super.getSign() * value;
    }
    
    protected void hook(final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
    }
    
    protected abstract Collection<? extends EffectUser> getFighters(final AbstractFight<?> p0, final CriterionUser p1);
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public long getMaxRange() {
        return (this.m_maxRange == null) ? -1L : this.m_maxRange.getLongValue(null, null, null, null);
    }
    
    public long getMinRange() {
        return (this.m_minRange == null) ? -1L : this.m_minRange.getLongValue(null, null, null, null);
    }
    
    static {
        GetFightersCountInRange.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        GetFightersCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER };
        GetFightersCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.STRING };
        GetFightersCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.NUMBER, ParserType.STRING };
        GetFightersCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.NUMBER, ParserType.STRING, ParserType.BOOLEAN };
        GetFightersCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.NUMBER, ParserType.STRING, ParserType.BOOLEAN, ParserType.BOOLEAN };
        GetFightersCountInRange.signatures.add(sig);
    }
}
