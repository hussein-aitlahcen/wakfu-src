package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class SetEffectArea extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    static final ObjectPool m_staticPool;
    protected AbstractEffectArea m_area;
    protected long m_newTargetId;
    protected short m_zoneLevel;
    protected boolean m_shouldBeInfinite;
    private boolean m_useCaster;
    private boolean m_useTarget;
    private boolean m_useTargetCell;
    protected Direction8 m_areaDirection;
    static final int EFFECT_AREA_ADDITIONALDATA_SIZE = 15;
    public BinarSerialPart ADDITIONALDATAS;
    
    public SetEffectArea() {
        super();
        this.m_shouldBeInfinite = false;
        this.m_useCaster = false;
        this.m_useTarget = false;
        this.m_useTargetCell = true;
        this.ADDITIONALDATAS = new BinarSerialPart(15) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(SetEffectArea.this.m_newTargetId);
                buffer.putShort(SetEffectArea.this.m_zoneLevel);
                buffer.put((byte)(SetEffectArea.this.m_shouldBeInfinite ? 1 : 0));
                if (SetEffectArea.this.m_areaDirection != null) {
                    buffer.putInt(SetEffectArea.this.m_areaDirection.getIndex());
                }
                else {
                    buffer.putInt(-1);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SetEffectArea.this.m_newTargetId = buffer.getLong();
                SetEffectArea.this.m_zoneLevel = buffer.getShort();
                SetEffectArea.this.m_shouldBeInfinite = (buffer.get() == 1);
                final int directionIdx = buffer.getInt();
                if (directionIdx == -1) {
                    SetEffectArea.this.m_areaDirection = null;
                }
                else {
                    SetEffectArea.this.m_areaDirection = Direction8.getDirectionFromIndex(directionIdx);
                }
            }
        };
        this.setTriggersToExecute();
    }
    
    public SetEffectArea(final boolean useCaster, final boolean useTarget, final boolean useTargetCell) {
        super();
        this.m_shouldBeInfinite = false;
        this.m_useCaster = false;
        this.m_useTarget = false;
        this.m_useTargetCell = true;
        this.ADDITIONALDATAS = new BinarSerialPart(15) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(SetEffectArea.this.m_newTargetId);
                buffer.putShort(SetEffectArea.this.m_zoneLevel);
                buffer.put((byte)(SetEffectArea.this.m_shouldBeInfinite ? 1 : 0));
                if (SetEffectArea.this.m_areaDirection != null) {
                    buffer.putInt(SetEffectArea.this.m_areaDirection.getIndex());
                }
                else {
                    buffer.putInt(-1);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SetEffectArea.this.m_newTargetId = buffer.getLong();
                SetEffectArea.this.m_zoneLevel = buffer.getShort();
                SetEffectArea.this.m_shouldBeInfinite = (buffer.get() == 1);
                final int directionIdx = buffer.getInt();
                if (directionIdx == -1) {
                    SetEffectArea.this.m_areaDirection = null;
                }
                else {
                    SetEffectArea.this.m_areaDirection = Direction8.getDirectionFromIndex(directionIdx);
                }
            }
        };
        this.m_useCaster = useCaster;
        this.m_useTarget = useTarget;
        this.m_useTargetCell = useTargetCell;
    }
    
    public static SetEffectArea checkOut(final EffectContext<WakfuEffect> context, final Point3 targetCell, final int effectAreaBaseId) {
        SetEffectArea re;
        try {
            re = (SetEffectArea)SetEffectArea.m_staticPool.borrowObject();
            re.m_pool = SetEffectArea.m_staticPool;
        }
        catch (Exception e) {
            re = new SetEffectArea();
            re.m_isStatic = false;
            re.m_pool = null;
            SetEffectArea.m_logger.error((Object)("Erreur lors d'un checkOut sur un SetEffectArea : " + e.getMessage()));
        }
        re.m_id = RunningEffectConstants.SET_EFFECT_AREA.getId();
        re.m_status = RunningEffectConstants.SET_EFFECT_AREA.getObject().getRunningEffectStatus();
        re.setTriggersToExecute();
        re.m_targetCell.set(targetCell);
        re.m_value = effectAreaBaseId;
        re.m_newTargetId = context.getEffectUserInformationProvider().getNextFreeEffectUserId((byte)2);
        re.m_maxExecutionCount = -1;
        re.m_zoneLevel = -1;
        re.m_context = (EffectContext<FX>)context;
        return re;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_shouldBeInfinite = false;
        this.m_area = null;
        this.m_areaDirection = null;
    }
    
    @Override
    public SetEffectArea newInstance() {
        SetEffectArea re;
        try {
            re = (SetEffectArea)this.getPool().borrowObject();
            re.m_pool = this.getPool();
        }
        catch (Exception e) {
            re = new SetEffectArea();
            re.m_pool = null;
            re.m_isStatic = false;
            SetEffectArea.m_logger.error((Object)("Erreur lors d'un checkOut sur un ArenaRunningEffect : " + e.getMessage()));
        }
        re.m_newTargetId = this.m_newTargetId;
        re.m_area = this.m_area;
        re.m_shouldBeInfinite = this.m_shouldBeInfinite;
        re.m_useCaster = this.m_useCaster;
        re.m_useTarget = this.m_useTarget;
        re.m_useTargetCell = this.m_useTargetCell;
        re.m_zoneLevel = this.m_zoneLevel;
        return re;
    }
    
    protected ObjectPool getPool() {
        return SetEffectArea.m_staticPool;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final AbstractEffectArea area = StaticEffectAreaManager.getInstance().getAreaFromId(this.m_value);
        if (area != null && this.m_targetCell != null) {
            this.createArea(area);
            if (this.m_context == null || this.m_context.getEffectAreaManager() == null) {
                SetEffectArea.m_logger.warn((Object)"Impossible d'ajouter une zone d'effet au combat le contexte est null ou l'effectAreaManager est null");
                return;
            }
            this.notifyExecution(linkedRE, trigger);
            this.m_area.computeZone();
            this.m_context.getEffectAreaManager().addEffectArea(this.m_area);
        }
        else {
            SetEffectArea.m_logger.error((Object)("Impossible d'ajouter une zone inconnue " + this.m_value));
            this.setNotified(true);
        }
    }
    
    protected void createArea(final AbstractEffectArea area) {
        Direction8 direction;
        if (this.m_areaDirection != null) {
            direction = this.m_areaDirection;
        }
        else {
            direction = ((this.m_caster != null) ? this.m_caster.getDirection() : Direction8.NORTH_EAST);
        }
        (this.m_area = area.instanceAnother(new EffectAreaParameters(this.m_newTargetId, this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), this.m_context, this.getOwner(), this.m_zoneLevel, direction))).setDisappearWithOwner(!this.m_shouldBeInfinite);
        if (this.hasDuration() && !this.m_shouldBeInfinite) {
            this.m_area.setLinkedEffect(this);
        }
    }
    
    protected EffectUser getOwner() {
        return this.m_caster;
    }
    
    public void setUseCaster(final boolean useCaster) {
        this.m_useCaster = useCaster;
    }
    
    @Override
    public boolean useCaster() {
        return this.m_useCaster;
    }
    
    @Override
    public boolean useTarget() {
        return this.m_useTarget;
    }
    
    @Override
    public boolean useTargetCell() {
        return this.m_useTargetCell;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        this.m_newTargetId = this.m_context.getEffectUserInformationProvider().getNextFreeEffectUserId((byte)2);
        this.extractZoneLevel(level);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            this.m_shouldBeInfinite = (((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
    }
    
    private void extractZoneLevel(final short level) {
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 4) {
            if (this.m_caster == null || !(this.m_caster instanceof BasicCharacterInfo)) {
                SetEffectArea.m_logger.error((Object)"Impossible de poser la zone, le caster n'est pas valide");
                return;
            }
            final int spellId = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            final SpellInventory<? extends AbstractSpellLevel> spellInventory = ((BasicCharacterInfo)this.m_caster).getSpellInventory();
            if (spellInventory == null) {
                SetEffectArea.m_logger.error((Object)"Impossible de poser la zone, le caster n'a pas d'inventaire de sort");
                return;
            }
            final AbstractSpellLevel spell = spellInventory.getFirstWithReferenceId(spellId);
            if (spell == null) {
                SetEffectArea.m_logger.error((Object)("Impossible de poser la zone, l'inventaire de sort ne contient pas le sort " + spellId + ", breed caster : " + ((BasicCharacterInfo)this.m_caster).getBreedId()));
                return;
            }
            this.m_zoneLevel = spell.getLevel();
            final float factor = ((WakfuEffect)this.m_genericEffect).getParam(3, level);
            this.m_zoneLevel *= (short)factor;
        }
        else if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            this.m_zoneLevel = (short)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        else {
            this.m_zoneLevel = this.getContainerLevel();
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_area != null && !this.m_shouldBeInfinite && this.hasDuration()) {
            this.m_context.getEffectAreaManager().removeEffectArea(this.m_area);
        }
        super.unapplyOverride();
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetEffectArea.PARAMETERS_LIST_SET;
    }
    
    public void setShouldBeInfinite(final boolean shouldBeInfinite) {
        this.m_shouldBeInfinite = shouldBeInfinite;
    }
    
    public void setZoneLevel(final short zoneLevel) {
        this.m_zoneLevel = zoneLevel;
    }
    
    public AbstractEffectArea getArea() {
        return this.m_area;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONALDATAS;
    }
    
    public static BasicEffectArea<WakfuEffect, EffectAreaParameters> getBasicEffectAreaFromEffect(final WakfuEffect effect, final WakfuEffectContainer container) {
        final short level = container.getLevel();
        final int areaId = effect.getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        return StaticEffectAreaManager.getInstance().getAreaFromId(areaId);
    }
    
    public void setAreaDirection(final Direction8 areaDirection) {
        this.m_areaDirection = areaDirection;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Param standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id de la zone", WakfuRunningEffectParameterType.ID) }), new WakfuRunningEffectParameterList("Param avec niveau de la zone", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id de la zone", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("Niveau de la zone", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Param avec niveau de la zone + dur\u00e9e apres la fin de l'effet", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id de la zone", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("Niveau de la zone", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("La zone reste apres la fin de cet effet (1 = Oui, Non pas defaut)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Niveau de la zone fct du niveau d'un sort du caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id de la zone", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("Id du sort a prendre en compte pour le niveau", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("La zone reste apres la fin de cet effet (1 = Oui, Non pas defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Ratio du niveau du sort", WakfuRunningEffectParameterType.CONFIG) }) });
        m_staticPool = new MonitoredPool(new ObjectFactory<SetEffectArea>() {
            @Override
            public SetEffectArea makeObject() {
                return new SetEffectArea();
            }
        });
    }
}
