package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.apache.commons.lang3.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractAuraEffectArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private final long[] m_targets;
    
    @Override
    public EffectAreaParameterListSet getParametersListSet() {
        return AbstractAuraEffectArea.PARAMETER_LIST_SET;
    }
    
    protected AbstractAuraEffectArea() {
        super();
        this.m_targets = new long[1];
    }
    
    public AbstractAuraEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_targets = new long[1];
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public int getType() {
        return EffectAreaType.AURA.getTypeId();
    }
    
    @Override
    public int getWorldCellX() {
        return this.m_owner.getWorldCellX();
    }
    
    @Override
    public int getWorldCellY() {
        return this.m_owner.getWorldCellY();
    }
    
    @Override
    public short getWorldCellAltitude() {
        return this.m_owner.getWorldCellAltitude();
    }
    
    @Override
    public void setPosition(final int x, final int y, final short alt) {
    }
    
    @Override
    public boolean checkTriggers(final BitSet triggers, final Target applicant) {
        return (applicant == this.m_owner && triggers.get(10004)) || super.checkTriggers(triggers, applicant);
    }
    
    @Override
    public void triggers(final BitSet triggers, @Nullable final RunningEffect triggeringRE, final Target applicant) {
        if (applicant == this.m_owner && (triggers.get(10001) || triggers.get(10002))) {
            return;
        }
        super.triggers(triggers, triggeringRE, applicant);
    }
    
    @Override
    public boolean isPositionStatic() {
        return false;
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        if (triggerer != null) {
            this.m_targets[0] = PositionValue.toLong(triggerer.getWorldCellX(), triggerer.getWorldCellY(), triggerer.getWorldCellAltitude());
            return this.m_targets;
        }
        return null;
    }
    
    @Override
    public ArrayList<Target> determineUnapplicationTarget(final Target triggerer) {
        final ArrayList<Target> targets = new ArrayList<Target>();
        if (triggerer != null) {
            targets.add(triggerer);
        }
        return targets;
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return true;
    }
    
    @Override
    public void computeZone() {
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
    }
    
    static {
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Aura standard", new Parameter[0]) });
    }
}
