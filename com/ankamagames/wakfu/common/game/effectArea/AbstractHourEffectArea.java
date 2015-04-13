package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractHourEffectArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private boolean m_isCurrentHour;
    private byte m_hour;
    private final long[] m_target;
    
    @Override
    public EffectAreaParameterListSet getParametersListSet() {
        return AbstractHourEffectArea.PARAMETER_LIST_SET;
    }
    
    public AbstractHourEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_isCurrentHour = false;
        this.m_target = new long[1];
    }
    
    protected AbstractHourEffectArea() {
        super();
        this.m_isCurrentHour = false;
        this.m_target = new long[1];
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public int getType() {
        return EffectAreaType.HOUR.getTypeId();
    }
    
    public boolean isCurrentHour() {
        return this.m_isCurrentHour;
    }
    
    public void setAsCurrentHour(final boolean currentHour) {
        this.m_isCurrentHour = currentHour;
    }
    
    public void setHour(final byte hour) {
        this.m_hour = hour;
    }
    
    public byte getHour() {
        return this.m_hour;
    }
    
    @Override
    public boolean hasNoExecutionCount() {
        return true;
    }
    
    @Override
    public boolean hasActivationDelay() {
        return false;
    }
    
    @Override
    public void execute(final int targetX, final int targetY, final short targetZ, final RunningEffect triggeringRE) {
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        if (triggerer == this.m_owner) {
            this.m_target[0] = PositionValue.toLong(this.m_owner.getWorldCellX(), this.m_owner.getWorldCellY(), this.m_owner.getWorldCellAltitude());
            return this.m_target;
        }
        return null;
    }
    
    @Override
    public ArrayList<Target> determineUnapplicationTarget(final Target triggerer) {
        final ArrayList<Target> res = new ArrayList<Target>();
        if (this.m_owner != null) {
            res.add(this.m_owner);
        }
        return res;
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return this.m_owner == applicant;
    }
    
    @Override
    public void pushActivationEventForTargetInTimeline(final Target applicant) {
    }
    
    @Override
    public void onGoesOffPlay() {
        super.onGoesOffPlay();
        if (this.m_context == null) {
            return;
        }
        final Collection<BasicEffectArea> areasToRemove = this.getAreasToRemove();
        this.removeAreasFromManager(areasToRemove);
    }
    
    private void removeAreasFromManager(final Collection<BasicEffectArea> areasToRemove) {
        for (final BasicEffectArea area : areasToRemove) {
            this.m_context.getEffectAreaManager().removeEffectArea(area);
        }
    }
    
    private Collection<BasicEffectArea> getAreasToRemove() {
        final Collection<BasicEffectArea> areasToRemove = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea area : this.m_context.getEffectAreaManager().getActiveEffectAreas()) {
            if (area != this && area.getWorldCellX() == this.getWorldCellX() && area.getWorldCellY() == this.getWorldCellY() && area.getWorldCellAltitude() == this.getWorldCellAltitude() && area.getOwner() == this.getOwner()) {
                areasToRemove.add(area);
            }
        }
        return areasToRemove;
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        final ByteArray ba = new ByteArray();
        ba.put(this.m_hour);
        ba.put((byte)(this.m_isCurrentHour ? 1 : 0));
        return ba.toArray();
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
        this.setHour(bb.get());
        this.setAsCurrentHour(bb.get() == 1);
    }
    
    static {
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Heure standard", new Parameter[0]) });
    }
}
