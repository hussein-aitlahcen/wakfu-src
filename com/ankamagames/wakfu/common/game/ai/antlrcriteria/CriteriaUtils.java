package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public final class CriteriaUtils
{
    private static final List<CriterionUser> EMPTY_LIST;
    
    public static BasicCharacterInfo getTargetCharacterInfoFromParameters(final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        final CriterionUser user = getTargetCriterionUserFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user instanceof BasicCharacterInfo) {
            return (BasicCharacterInfo)user;
        }
        return null;
    }
    
    public static BasicCharacterInfo getTargetCharacterInfoFromParameters(final boolean useTarget, final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        final CriterionUser user = getTargetCriterionUserFromParameters(useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user instanceof BasicCharacterInfo) {
            return (BasicCharacterInfo)user;
        }
        return null;
    }
    
    public static BasicCharacterInfo getTargetCharacterInfoFromParameters(final TargetType targetType, final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        final CriterionUser user = getTargetCriterionUserFromParameters(targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user instanceof BasicCharacterInfo) {
            return (BasicCharacterInfo)user;
        }
        return null;
    }
    
    public static CriterionUser getTargetCriterionUserFromParameters(final boolean useTarget, final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        final Object character = useTarget ? criterionTarget : criterionUser;
        if (character instanceof CriterionUser) {
            return (CriterionUser)character;
        }
        return getExtendedUser(TargetType.NONE, criterionTarget, criterionContext, criterionContent);
    }
    
    public static CriterionUser getTargetCriterionUserFromParameters(final TargetType targetType, final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        if (targetType == TargetType.CASTER && criterionUser instanceof CriterionUser) {
            return (CriterionUser)criterionUser;
        }
        if (targetType == TargetType.TARGET && criterionTarget instanceof CriterionUser) {
            return (CriterionUser)criterionTarget;
        }
        return getExtendedUser(targetType, criterionTarget, criterionContext, criterionContent);
    }
    
    public static List<CriterionUser> getTargetListCriterionUserFromParameters(final boolean useTarget, final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        final Object character = useTarget ? criterionTarget : criterionUser;
        if (character instanceof CriterionUser) {
            return Collections.singletonList(character);
        }
        return getExtendedUsers(TargetType.NONE, criterionTarget, criterionContext, criterionContent);
    }
    
    public static List<CriterionUser> getTargetListCriterionUserFromParameters(final TargetType targetType, final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        if (targetType == TargetType.CASTER && criterionUser instanceof CriterionUser) {
            return Collections.singletonList(criterionUser);
        }
        if (targetType == TargetType.TARGET && criterionTarget instanceof CriterionUser) {
            return Collections.singletonList(criterionTarget);
        }
        return getExtendedUsers(targetType, criterionTarget, criterionContext, criterionContent);
    }
    
    public static Point3 getTargetPosition(final boolean useTarget, final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        final Object target = useTarget ? criterionTarget : criterionUser;
        if (target instanceof Point3) {
            return (Point3)target;
        }
        if (target instanceof CriterionUser) {
            return ((CriterionUser)target).getPosition();
        }
        return null;
    }
    
    public static CriterionUser getTargetCriterionUserFromParameters(final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        if (criterionUser instanceof CriterionUser) {
            return (CriterionUser)criterionUser;
        }
        if (criterionTarget instanceof CriterionUser) {
            return (CriterionUser)criterionTarget;
        }
        return getExtendedUser(TargetType.NONE, criterionTarget, criterionContext, criterionContent);
    }
    
    private static CriterionUser getExtendedUser(final TargetType targetType, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        switch (targetType) {
            case EVENT_TRIGGERER: {
                return getEventTriggerer(criterionContext, criterionContent);
            }
            case EVENT_TARGET: {
                return getEventTarget(criterionContext, criterionContent);
            }
            case POSITION: {
                return getUserAtPosition(criterionTarget, criterionContext);
            }
            default: {
                final CriterionUser eventTriggerer = getEventTriggerer(criterionContext, criterionContent);
                if (eventTriggerer != null) {
                    return eventTriggerer;
                }
                return getUserAtPosition(criterionTarget, criterionContext);
            }
        }
    }
    
    private static CriterionUser getUserAtPosition(final Object criterionTarget, final Object criterionContext) {
        if (!(criterionTarget instanceof Point3)) {
            return null;
        }
        final AbstractFight fight = getFightFromContext(criterionContext);
        if (fight == null) {
            return null;
        }
        final Point3 position = (Point3)criterionTarget;
        return fight.getCharacterInfoAtPosition(position);
    }
    
    private static CriterionUser getEventTriggerer(final Object criterionContext, final Object criterionContent) {
        if (criterionContext instanceof PlayerTriggered) {
            final PlayerTriggered playerTriggered = (PlayerTriggered)criterionContext;
            return playerTriggered.getTriggerer();
        }
        if (criterionContent instanceof PlayerTriggered) {
            final PlayerTriggered playerTriggered = (PlayerTriggered)criterionContent;
            return playerTriggered.getTriggerer();
        }
        return null;
    }
    
    private static CriterionUser getEventTarget(final Object criterionContext, final Object criterionContent) {
        if (criterionContext instanceof TargetedEvent) {
            final TargetedEvent event = (TargetedEvent)criterionContext;
            return (CriterionUser)event.getTarget();
        }
        if (criterionContent instanceof TargetedEvent) {
            final TargetedEvent event = (TargetedEvent)criterionContent;
            return (CriterionUser)event.getTarget();
        }
        return null;
    }
    
    private static List<CriterionUser> getExtendedUsers(final TargetType targetType, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        switch (targetType) {
            case EVENT_TRIGGERER: {
                final CriterionUser et = getEventTriggerer(criterionContext, criterionContent);
                return (et != null) ? Collections.singletonList(et) : CriteriaUtils.EMPTY_LIST;
            }
            case EVENT_TARGET: {
                final CriterionUser eta = getEventTarget(criterionContext, criterionContent);
                return (eta != null) ? Collections.singletonList(eta) : CriteriaUtils.EMPTY_LIST;
            }
            default: {
                final CriterionUser eventTriggerer = getEventTriggerer(criterionContext, criterionContent);
                if (eventTriggerer != null) {
                    return Collections.singletonList(eventTriggerer);
                }
                return getUsersAtPosition(criterionTarget, criterionContext);
            }
        }
    }
    
    private static List<CriterionUser> getUsersAtPosition(final Object criterionTarget, final Object criterionContext) {
        if (!(criterionTarget instanceof Point3)) {
            return Collections.emptyList();
        }
        final List<CriterionUser> res = new ArrayList<CriterionUser>();
        final AbstractFight fight = getFightFromContext(criterionContext);
        if (fight == null) {
            return Collections.emptyList();
        }
        final Point3 position = (Point3)criterionTarget;
        res.add(fight.getCharacterInfoAtPosition(position));
        final List<BasicEffectArea> targetableEffectAreaOnPosition = fight.getEffectAreaManager().getTargetableEffectAreasListOnPosition(position);
        for (int i = 0, n = targetableEffectAreaOnPosition.size(); i < n; ++i) {
            final BasicEffectArea area = targetableEffectAreaOnPosition.get(i);
            res.add((CriterionUser)area);
        }
        return res;
    }
    
    public static AbstractFight getFightFromContext(final Object objectContext) {
        if (objectContext == null) {
            return null;
        }
        if (objectContext instanceof AbstractFight) {
            return (AbstractFight)objectContext;
        }
        if (objectContext instanceof WakfuFightEffectContext) {
            return ((WakfuFightEffectContext)objectContext).getFight();
        }
        return null;
    }
    
    public static EffectUser getOwner(final Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BasicCharacterInfo) {
            return ((BasicCharacterInfo)obj).getController();
        }
        if (obj instanceof AreaOwnerProvider) {
            return getOwner(((AreaOwnerProvider)obj).getOwner());
        }
        if (obj instanceof EffectUser) {
            return (EffectUser)obj;
        }
        return null;
    }
    
    @Nullable
    public static WakfuRunningEffect getTriggeringEffect(final Object criterionContent) {
        if (criterionContent == null) {
            return null;
        }
        if (!(criterionContent instanceof WakfuRunningEffect)) {
            return null;
        }
        final WakfuRunningEffect wakfuRunningEffect = (WakfuRunningEffect)criterionContent;
        if (wakfuRunningEffect.getTriggeringEffectForCriterion() != null) {
            return (WakfuRunningEffect)wakfuRunningEffect.getTriggeringEffectForCriterion();
        }
        final WakfuEffectExecutionParameters params = (WakfuEffectExecutionParameters)((RunningEffect)criterionContent).getParams();
        if (params == null) {
            return null;
        }
        return params.getExternalTriggeringEffect();
    }
    
    @Nullable
    public static EffectUser getTriggeringEffectTarget(final Object criterionContent) {
        final WakfuRunningEffect triggeringEffect = getTriggeringEffect(criterionContent);
        if (triggeringEffect == null) {
            return null;
        }
        return triggeringEffect.getTarget();
    }
    
    @Nullable
    public static EffectUser getTriggeringEffectCaster(final Object criterionContent) {
        final WakfuRunningEffect triggeringEffect = getTriggeringEffect(criterionContent);
        if (triggeringEffect == null) {
            return null;
        }
        return triggeringEffect.getCaster();
    }
    
    public static AbstractFight<?> getFight(final Object criterionUser, final Object criterionContext) {
        if (criterionContext instanceof AbstractFight) {
            return (AbstractFight<?>)criterionContext;
        }
        if (criterionContext instanceof WakfuFightEffectContext) {
            return ((WakfuFightEffectContext)criterionContext).getFight();
        }
        if (!(criterionUser instanceof CriterionUser)) {
            throw new CriteriaExecutionException("On essaie de compter les fighters d'un caster qui n'est pas un perso");
        }
        if (criterionUser instanceof BasicCharacterInfo) {
            return (AbstractFight<?>)((BasicCharacterInfo)criterionUser).getCurrentFight();
        }
        return null;
    }
    
    public static CriterionUser getTarget(final String targetType, final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        CriterionUser targetCharacter = null;
        if (targetType == null || targetType.equalsIgnoreCase("caster")) {
            targetCharacter = getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        }
        else if (targetType.equalsIgnoreCase("target")) {
            targetCharacter = getTargetCriterionUserFromParameters(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        }
        else if (targetType.equalsIgnoreCase("triggeringCaster")) {
            final EffectUser triggeringCaster = getTriggeringEffectCaster(criterionContent);
            if (triggeringCaster instanceof CriterionUser) {
                targetCharacter = (CriterionUser)triggeringCaster;
            }
        }
        else if (targetType.equalsIgnoreCase("triggeringTarget")) {
            final EffectUser triggeringTarget = getTriggeringEffectTarget(criterionContent);
            if (triggeringTarget instanceof CriterionUser) {
                targetCharacter = (CriterionUser)triggeringTarget;
            }
        }
        else if (targetType.equalsIgnoreCase("casterController")) {
            final CriterionUser caster = getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
            final AbstractFight fight = getFightFromContext(criterionContext);
            if (fight == null) {
                return null;
            }
            targetCharacter = fight.getFighterFromId(caster.getOriginalControllerId());
        }
        return targetCharacter;
    }
    
    static {
        EMPTY_LIST = Collections.emptyList();
    }
}
