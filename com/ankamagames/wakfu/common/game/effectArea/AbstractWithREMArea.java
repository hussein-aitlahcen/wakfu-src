package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.nio.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractWithREMArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private RunningEffectManager m_runningEffectManager;
    
    @Override
    public EffectAreaParameterListSet getParametersListSet() {
        return AbstractWithREMArea.PARAMETER_LIST_SET;
    }
    
    public AbstractWithREMArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBetargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBetargetted, canBeDestroyed, maxLevel);
        this.m_runningEffectManager = null;
    }
    
    protected AbstractWithREMArea() {
        super();
        this.m_runningEffectManager = null;
    }
    
    @Override
    public void initialize() {
        this.m_runningEffectManager = new TimedRunningEffectManager(this);
    }
    
    @Override
    public RunningEffectManager getRunningEffectManager() {
        return this.m_runningEffectManager;
    }
    
    @Override
    public int getType() {
        return EffectAreaType.SIMPLE_WITH_REM.getTypeId();
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        return null;
    }
    
    @Override
    public List<Target> determineUnapplicationTarget(final Target triggerer) {
        return Collections.emptyList();
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return false;
    }
    
    @Override
    public boolean isBlockingMovement() {
        return true;
    }
    
    @Override
    public boolean canBlockMovementOrSight() {
        return true;
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
    }
    
    static {
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Case simple", new Parameter[0]) });
    }
}
