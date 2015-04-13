package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.nio.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractDepositEffectArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private final long[] m_target;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AbstractDepositEffectArea.PARAMETER_LIST_SET;
    }
    
    protected AbstractDepositEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargeted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargeted, canBeDestroyed, maxLevel);
        this.m_target = new long[1];
    }
    
    protected AbstractDepositEffectArea() {
        super();
        this.m_target = new long[1];
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public int getType() {
        return EffectAreaType.ENUTROF_DEPOSIT.getTypeId();
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        this.m_target[0] = PositionValue.toLong(this.m_position);
        return this.m_target;
    }
    
    @Override
    public List<Target> determineUnapplicationTarget(final Target triggerer) {
        final ArrayList<Target> res = new ArrayList<Target>();
        res.add(this.m_owner);
        return res;
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return applicant == this.getOwner();
    }
    
    public int getDepositLevel() {
        return (int)this.getParams(0);
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
    }
    
    static {
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Gisement standard", new Parameter[] { new Parameter("Niveau du gisement") }) });
    }
}
