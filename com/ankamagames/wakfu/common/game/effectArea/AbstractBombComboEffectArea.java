package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.apache.commons.lang3.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractBombComboEffectArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private final long[] m_target;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AbstractBombComboEffectArea.PARAMETER_LIST_SET;
    }
    
    protected AbstractBombComboEffectArea() {
        super();
        this.m_target = new long[1];
    }
    
    protected AbstractBombComboEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargeted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargeted, canBeDestroyed, maxLevel);
        this.m_target = new long[1];
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        if (triggerer == null) {
            return null;
        }
        this.m_target[0] = PositionValue.toLong(triggerer.getWorldCellX(), triggerer.getWorldCellY(), triggerer.getWorldCellAltitude());
        return this.m_target;
    }
    
    @Override
    public ArrayList<Target> determineUnapplicationTarget(final Target triggerer) {
        if (triggerer == null) {
            return null;
        }
        final ArrayList<Target> targets = new ArrayList<Target>();
        targets.add(triggerer);
        return targets;
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return true;
    }
    
    @Override
    public int getType() {
        return EffectAreaType.BOMB_COMBO.getTypeId();
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
    }
    
    static {
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Bomb combo standard", new Parameter[0]) });
    }
}
