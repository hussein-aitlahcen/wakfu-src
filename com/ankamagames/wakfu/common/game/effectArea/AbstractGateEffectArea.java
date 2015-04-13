package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.targetfinder.*;
import org.apache.commons.lang3.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractGateEffectArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AbstractGateEffectArea.PARAMETER_LIST_SET;
    }
    
    protected AbstractGateEffectArea() {
        super();
    }
    
    protected AbstractGateEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargeted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargeted, canBeDestroyed, maxLevel);
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        return null;
    }
    
    @Override
    public ArrayList<Target> determineUnapplicationTarget(final Target triggerer) {
        return null;
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return false;
    }
    
    @Override
    public int getType() {
        return EffectAreaType.GATE.getTypeId();
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
    }
    
    static {
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Portail standard", new Parameter[0]) });
    }
}
