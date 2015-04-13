package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.targetfinder.*;
import org.apache.commons.lang3.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractSimpleEffectArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private int m_typeId;
    private Direction8 m_direction;
    
    @Override
    public EffectAreaParameterListSet getParametersListSet() {
        return AbstractSimpleEffectArea.PARAMETER_LIST_SET;
    }
    
    public AbstractSimpleEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBetargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBetargetted, canBeDestroyed, maxLevel);
        this.m_typeId = EffectAreaType.SIMPLE.getTypeId();
    }
    
    @Override
    public AbstractSimpleEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final AbstractSimpleEffectArea area = (AbstractSimpleEffectArea)super.instanceAnother(parameters);
        area.m_typeId = this.m_typeId;
        if (parameters != null && parameters.getDirection() != null) {
            area.setDirection(parameters.getDirection());
        }
        return area;
    }
    
    protected AbstractSimpleEffectArea() {
        super();
        this.m_typeId = EffectAreaType.SIMPLE.getTypeId();
    }
    
    @Override
    public int getType() {
        return this.m_typeId;
    }
    
    public void setTypeId(final int typeId) {
        this.m_typeId = typeId;
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return applicant == this.m_owner;
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        return null;
    }
    
    @Override
    public ArrayList<Target> determineUnapplicationTarget(final Target triggerer) {
        final ArrayList<Target> targets = new ArrayList<Target>();
        targets.add(triggerer);
        return targets;
    }
    
    @Override
    public byte getHeight() {
        return 6;
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        if (direction != null) {
            this.m_direction = direction;
        }
    }
    
    @Override
    public Direction8 getDirection() {
        if (this.m_direction == null) {
            return super.getDirection();
        }
        return this.m_direction;
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
