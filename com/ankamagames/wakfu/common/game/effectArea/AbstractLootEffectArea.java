package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.nio.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractLootEffectArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private Direction8 m_direction;
    private final long[] m_target;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AbstractLootEffectArea.PARAMETER_LIST_SET;
    }
    
    protected AbstractLootEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargeted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargeted, canBeDestroyed, maxLevel);
        this.m_target = new long[1];
    }
    
    protected AbstractLootEffectArea() {
        super();
        this.m_target = new long[1];
    }
    
    @Override
    public AbstractEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final AbstractEffectArea area = super.instanceAnother(parameters);
        if (parameters != null && parameters.getDirection() != null) {
            area.setDirection(parameters.getDirection());
        }
        else {
            area.setDirection(Direction8.SOUTH_WEST);
        }
        return area;
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public int getType() {
        return EffectAreaType.LOOT_AREA.getTypeId();
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        this.m_target[0] = PositionValue.toLong(triggerer.getWorldCellX(), triggerer.getWorldCellY(), triggerer.getWorldCellAltitude());
        return this.m_target;
    }
    
    @Override
    public List<Target> determineUnapplicationTarget(final Target triggerer) {
        return null;
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return true;
    }
    
    @Override
    public Direction8 getDirection() {
        return this.m_direction;
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        this.m_direction = direction;
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
    }
    
    static {
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("LootArea standard", new Parameter[0]) });
    }
}
