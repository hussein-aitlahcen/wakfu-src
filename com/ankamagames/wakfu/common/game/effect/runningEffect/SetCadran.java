package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SetCadran extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private List<BasicEffectArea> m_hours;
    private long m_baseUniqueId;
    private byte m_systemHour;
    private int m_currentHourAreaId;
    public BinarSerialPart TARGET;
    
    public SetCadran() {
        super();
        this.m_hours = new ArrayList<BasicEffectArea>();
        this.TARGET = new BinarSerialPart(13) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(SetCadran.this.m_baseUniqueId);
                buffer.putInt(SetCadran.this.m_currentHourAreaId);
                buffer.put(SetCadran.this.m_systemHour);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SetCadran.this.m_baseUniqueId = buffer.getLong();
                SetCadran.this.m_currentHourAreaId = buffer.getInt();
                SetCadran.this.m_systemHour = buffer.get();
                SetCadran.this.m_target = null;
            }
        };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetCadran.PARAMETERS_LIST_SET;
    }
    
    @Override
    public SetCadran newInstance() {
        SetCadran re;
        try {
            re = (SetCadran)SetCadran.m_staticPool.borrowObject();
            re.m_pool = SetCadran.m_staticPool;
        }
        catch (Exception e) {
            re = new SetCadran();
            re.m_pool = null;
            re.m_isStatic = false;
            SetCadran.m_logger.error((Object)("Erreur lors d'un checkOut sur un ArenaRunningEffect : " + e.getMessage()));
        }
        re.m_baseUniqueId = this.m_baseUniqueId;
        re.m_hours.clear();
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final AbstractHourEffectArea hourModel = StaticEffectAreaManager.getInstance().getHourAreas(this.m_value);
        if (hourModel == null) {
            this.setNotified();
            return;
        }
        AbstractHourEffectArea currentHourModel = StaticEffectAreaManager.getInstance().getHourAreas(this.m_currentHourAreaId);
        if (currentHourModel == null) {
            SetCadran.m_logger.error((Object)("Unable to find area " + this.m_currentHourAreaId + ". Using " + this.m_value + " as the area even for the 'current' hour"));
            currentHourModel = hourModel;
        }
        this.notifyExecution(linkedRE, trigger);
        final int[][] patternList = this.buildCadranPattern();
        this.spawnHourAreas(hourModel, currentHourModel, patternList);
    }
    
    private int[][] buildCadranPattern() {
        final int[][] patternList = { { 1, -2 }, { 2, -1 }, { 3, 0 }, { 2, 1 }, { 1, 2 }, { 0, 3 }, { -1, 2 }, { -2, 1 }, { -3, 0 }, { -2, -1 }, { -1, -2 }, { 0, -3 } };
        if (this.m_caster == null) {
            SetCadran.m_logger.error((Object)"pas de caster ");
            return patternList;
        }
        final Direction8 dir = this.m_caster.getDirection();
        switch (dir) {
            case NORTH_WEST: {
                for (final int[] pair : patternList) {
                    final int x = pair[0];
                    final int y = pair[1];
                    pair[0] = y;
                    pair[1] = -x;
                }
                break;
            }
            case SOUTH_WEST: {
                for (final int[] pair : patternList) {
                    pair[0] = -pair[0];
                    pair[1] = -pair[1];
                }
                break;
            }
            case SOUTH_EAST: {
                for (final int[] pair : patternList) {
                    final int x = pair[0];
                    final int y = pair[1];
                    pair[0] = -y;
                    pair[1] = x;
                }
                break;
            }
        }
        return patternList;
    }
    
    private void spawnHourAreas(final AbstractHourEffectArea hourModel, final AbstractHourEffectArea currentHourModel, final int[][] patternList) {
        byte hourcount = 0;
        if (this.m_context == null || this.m_context.getFightMap() == null) {
            if (this.m_context == null) {
                SetCadran.m_logger.warn((Object)"Unable to spawn hours : context is null");
            }
            else {
                SetCadran.m_logger.warn((Object)"Unable to spawn hours : context has no fightmap");
            }
            return;
        }
        for (final int[] pair : patternList) {
            ++hourcount;
            final int x = this.m_targetCell.getX() + pair[0];
            final int y = this.m_targetCell.getY() + pair[1];
            if (this.m_context.getFightMap().isInside(x, y)) {
                final boolean isCurrentHour = hourcount % 12 == this.m_systemHour % 12;
                final short cellHeight = this.m_context.getFightMap().getCellHeight(x, y);
                final long uniqueHourId = this.m_baseUniqueId + hourcount;
                if (cellHeight != -32768) {
                    final AbstractHourEffectArea model = isCurrentHour ? currentHourModel : hourModel;
                    final AbstractHourEffectArea hour = (AbstractHourEffectArea)model.instanceAnother(new EffectAreaParameters(uniqueHourId, x, y, cellHeight, this.m_context, this.m_caster, this.getContainerLevel(), this.m_caster.getDirection()));
                    hour.setHour(hourcount);
                    hour.setAsCurrentHour(isCurrentHour);
                    this.m_context.getEffectAreaManager().addEffectArea(hour);
                    this.m_hours.add(hour);
                }
                else {
                    SetCadran.m_logger.warn((Object)("La cellule [" + this.m_targetCell.getX() + pair[0] + ":" + this.m_targetCell.getY() + pair[1] + "] n'existe pas"));
                }
            }
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
            this.m_currentHourAreaId = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
        }
        final long nextFreeId = this.m_context.getEffectUserInformationProvider().getNextFreeEffectUserId((byte)3);
        this.m_baseUniqueId = nextFreeId << 8;
        if (nextFreeId > 2147483647L) {
            SetCadran.m_logger.error((Object)"probl\u00e8me possible entre les Ids des effects Users");
        }
        this.m_systemHour = (byte)WakfuGameCalendar.getInstance().getDate().getHours();
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_hours != null) {
            for (final BasicEffectArea area : this.m_hours) {
                this.m_context.getEffectAreaManager().removeEffectArea(area);
            }
        }
        super.unapplyOverride();
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    @Override
    public BinarSerialPart getTargetBinarSerialPart() {
        return this.TARGET;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetCadran>() {
            @Override
            public SetCadran makeObject() {
                return new SetCadran();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Param standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id des zones d'heure", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("id de la zone de l'heure courante", WakfuRunningEffectParameterType.ID) }) });
    }
}
