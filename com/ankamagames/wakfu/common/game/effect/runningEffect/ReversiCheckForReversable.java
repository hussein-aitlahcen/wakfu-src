package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ReversiCheckForReversable extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ReversiCheckForReversable.PARAMETERS_LIST_SET;
    }
    
    public ReversiCheckForReversable() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ReversiCheckForReversable newInstance() {
        ReversiCheckForReversable re;
        try {
            re = (ReversiCheckForReversable)ReversiCheckForReversable.m_staticPool.borrowObject();
            re.m_pool = ReversiCheckForReversable.m_staticPool;
        }
        catch (Exception e) {
            re = new ReversiCheckForReversable();
            re.m_pool = null;
            re.m_isStatic = false;
            ReversiCheckForReversable.m_logger.error((Object)("Erreur lors d'un checkOut sur un ReversiCheckForReversable : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        this.setNotified();
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            ReversiCheckForReversable.m_logger.error((Object)"La cible doit \u00eatre un fighter");
            return;
        }
        if (!(this.getContext() instanceof WakfuFightEffectContext)) {
            ReversiCheckForReversable.m_logger.error((Object)"Cet effet ne peut \u00eatre utilis\u00e9 que dans un combat");
            return;
        }
        final BasicCharacterInfo lastPiecePlaced = (BasicCharacterInfo)this.m_target;
        final Collection<BasicCharacterInfo> reversablePieces = new HashSet<BasicCharacterInfo>();
        final Direction8[] direction8Values = Direction8.getDirection8Values();
        for (int i = 0; i < direction8Values.length; ++i) {
            final Direction8 direction = direction8Values[i];
            reversablePieces.addAll(this.getReversablePiecesToDirection(lastPiecePlaced, direction));
        }
        for (final BasicCharacterInfo reversablePiece : reversablePieces) {
            reversablePiece.addProperty(FightPropertyType.REVERSI_REVERSABLE_PIECE);
        }
    }
    
    private Set<BasicCharacterInfo> getReversablePiecesToDirection(final BasicCharacterInfo lastPiecePlaced, final Direction8 direction) {
        final Point3 checkedPosition = new Point3(lastPiecePlaced.getPosition());
        checkedPosition.shift(direction);
        final WakfuFightEffectContext context = (WakfuFightEffectContext)this.getContext();
        final AbstractFight fight = context.getFight();
        final byte teamId = lastPiecePlaced.getTeamId();
        final Collection<BasicCharacterInfo> checkedEnemies = new HashSet<BasicCharacterInfo>();
        final Collection<BasicCharacterInfo> reversableEnemies = new HashSet<BasicCharacterInfo>();
        final Collection<BasicCharacterInfo> reversableAllies = new HashSet<BasicCharacterInfo>();
        while (fight.getCharacterInfoAtPosition(checkedPosition) != null) {
            final BasicCharacterInfo checkedCharacter = fight.getCharacterInfoAtPosition(checkedPosition);
            if (checkedCharacter.getTeamId() == teamId) {
                reversableEnemies.addAll(checkedEnemies);
                reversableAllies.add(checkedCharacter);
                checkedEnemies.clear();
            }
            else {
                checkedEnemies.add(checkedCharacter);
            }
            checkedPosition.shift(direction);
        }
        final Set<BasicCharacterInfo> res = new HashSet<BasicCharacterInfo>();
        res.addAll(reversableAllies);
        res.addAll(reversableEnemies);
        return res;
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ReversiCheckForReversable>() {
            @Override
            public ReversiCheckForReversable makeObject() {
                return new ReversiCheckForReversable();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
