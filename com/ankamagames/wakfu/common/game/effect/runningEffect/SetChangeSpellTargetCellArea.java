package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class SetChangeSpellTargetCellArea extends SetEffectArea
{
    private static final ObjectPool m_staticPool;
    
    @Override
    protected ObjectPool getPool() {
        return SetChangeSpellTargetCellArea.m_staticPool;
    }
    
    public SetChangeSpellTargetCellArea() {
        super();
        this.setTriggersToExecute();
        this.m_triggers.set(2124);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final AbstractEffectArea area = StaticEffectAreaManager.getInstance().getAreaFromId(this.m_value);
        if (area != null && this.m_targetCell != null && this.m_caster != null) {
            this.createArea(area);
            if (this.m_context == null || this.m_context.getEffectAreaManager() == null) {
                SetChangeSpellTargetCellArea.m_logger.warn((Object)"Impossible d'ajouter une zone d'effet au combat le contexte est null ou l'effectAreaManager est null");
                return;
            }
            this.notifyExecution(linkedRE, trigger);
        }
        else {
            SetChangeSpellTargetCellArea.m_logger.error((Object)("Impossible d'ajouter une zone inconnue " + this.m_value));
            this.setNotified(true);
        }
        BasicEffectArea tunnelExit = null;
        final Collection<BasicEffectArea> effectAreaList = this.m_context.getEffectAreaManager().getActiveEffectAreas();
        for (final BasicEffectArea basicEffectArea : effectAreaList) {
            if (basicEffectArea.getOwner() != this.m_caster) {
                continue;
            }
            if (basicEffectArea.getType() == EffectAreaType.SPELL_TUNNEL_MARKER.getTypeId()) {
                tunnelExit = basicEffectArea;
                break;
            }
        }
        if (tunnelExit == null) {
            this.m_area = null;
            return;
        }
        final int exitX = tunnelExit.getWorldCellX();
        final int exitY = tunnelExit.getWorldCellY();
        final short exitZ = tunnelExit.getWorldCellAltitude();
        ((AbstractChangeSpellTargetCellArea)this.m_area).setFirstPoint(this.m_targetCell);
        ((AbstractChangeSpellTargetCellArea)this.m_area).setSecondPoint(new Point3(exitX, exitY, exitZ));
        this.m_area.computeZone();
        this.m_context.getEffectAreaManager().addEffectArea(this.m_area);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetChangeSpellTargetCellArea>() {
            @Override
            public SetChangeSpellTargetCellArea makeObject() {
                return new SetChangeSpellTargetCellArea();
            }
        });
    }
}
