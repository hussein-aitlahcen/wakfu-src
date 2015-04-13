package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public abstract class Teleport extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    protected boolean m_canBeExecuted;
    protected boolean m_byPassProperties;
    protected boolean m_checkFightMap;
    private Point3 m_startCell;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return Teleport.PARAMETERS_LIST_SET;
    }
    
    protected Teleport() {
        super();
        this.m_canBeExecuted = true;
        this.m_byPassProperties = false;
        this.m_checkFightMap = true;
        this.ADDITIONNAL_DATAS = new BinarSerialPart(11) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(Teleport.this.m_targetCell.getX());
                buffer.putInt(Teleport.this.m_targetCell.getY());
                buffer.putShort(Teleport.this.m_targetCell.getZ());
                buffer.put((byte)(Teleport.this.m_canBeExecuted ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                Teleport.this.m_targetCell.set(buffer.getInt(), buffer.getInt(), buffer.getShort());
                Teleport.this.m_canBeExecuted = (buffer.get() == 1);
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public abstract Teleport newInstance();
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (!this.m_canBeExecuted) {
            if (this.getCharacterToTeleport() != null) {
                this.m_targetCell.set(this.getCharacterToTeleport().getPosition());
            }
            return;
        }
        this.teleportCharacter(linkedRE, trigger, this.getCharacterToTeleport());
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(189);
    }
    
    protected abstract EffectUser getCharacterToTeleport();
    
    public Point3 getStartCell() {
        return this.m_startCell;
    }
    
    private void teleportCharacter(final RunningEffect linkedRE, final boolean trigger, final EffectUser characterToTeleport) {
        boolean mustBeExecuted = true;
        if (characterToTeleport == null) {
            Teleport.m_logger.error((Object)"caster null sur un running effect teleport");
            mustBeExecuted = false;
        }
        final Point3 teleportCell = this.getTeleportCell();
        if (!mustBeExecuted || teleportCell == null) {
            return;
        }
        final int startx = characterToTeleport.getWorldCellX();
        final int starty = characterToTeleport.getWorldCellY();
        final short startz = characterToTeleport.getWorldCellAltitude();
        this.m_startCell = new Point3(startx, starty, startz);
        AreaOccupationComputer areaOccupationComputer = null;
        if (this.isValueComputationEnabled() && this.m_context instanceof WakfuFightEffectContext) {
            areaOccupationComputer = new AreaOccupationComputer(((WakfuFightEffectContext)this.m_context).getFight(), characterToTeleport, teleportCell);
            areaOccupationComputer.setInitialState(characterToTeleport.getPosition());
        }
        characterToTeleport.teleport(teleportCell.getX(), teleportCell.getY(), teleportCell.getZ());
        this.notifyExecution(linkedRE, trigger);
        if (this.m_context.getEffectAreaManager() != null && this.isValueComputationEnabled() && areaOccupationComputer != null) {
            final Direction8 dir = new Vector3i(startx, starty, startz, characterToTeleport.getWorldCellX(), characterToTeleport.getWorldCellY(), characterToTeleport.getWorldCellAltitude()).toDirection4();
            characterToTeleport.setSpecialMovementDirection(dir);
            areaOccupationComputer.setMoverDestinationCell(characterToTeleport.getPosition());
            areaOccupationComputer.computeAreaModificationsOnMove();
            if (areaOccupationComputer.willTriggerSomething()) {
                areaOccupationComputer.triggerAreaEffects();
            }
            characterToTeleport.setSpecialMovementDirection(null);
        }
    }
    
    protected Point3 getTeleportCell() {
        return this.m_targetCell;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final EffectUser characterToTeleport = this.getCharacterToTeleport();
        if (!(characterToTeleport instanceof BasicCharacterInfo) || this.m_targetCell == null) {
            this.m_canBeExecuted = false;
            return;
        }
        final FightMap fightMap = this.m_context.getFightMap();
        if (fightMap == null) {
            Teleport.m_logger.warn((Object)("pas de fightmap sur le context " + this.m_context));
            this.m_canBeExecuted = false;
            return;
        }
        final CellPathData cell = fightMap.getCellPathData(this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ());
        if (cell == null) {
            this.m_canBeExecuted = false;
            return;
        }
        if (!this.m_byPassProperties && (characterToTeleport.isActiveProperty(FightPropertyType.STABILIZED) || characterToTeleport.isActiveProperty(FightPropertyType.CANT_TELEPORT))) {
            this.m_canBeExecuted = false;
            return;
        }
        if (((CarryTarget)characterToTeleport).isCarried() && !this.canTeleportCarried()) {
            this.m_canBeExecuted = false;
        }
        if (this.m_checkFightMap && !fightMap.checkPosition(characterToTeleport, this.m_targetCell)) {
            Teleport.m_logger.error((Object)("On demande un t\u00e9l\u00e9port sur une cellule invalide. Position demand\u00e9e : " + this.m_targetCell + " pour l'instance " + fightMap.getWorldId()));
            this.m_canBeExecuted = false;
        }
    }
    
    protected boolean canTeleportCarried() {
        return false;
    }
    
    @Override
    public abstract boolean useCaster();
    
    @Override
    public abstract boolean useTarget();
    
    @Override
    public abstract boolean useTargetCell();
    
    @Override
    public void onCheckIn() {
        this.m_startCell = null;
        this.m_byPassProperties = false;
        this.m_checkFightMap = true;
        super.onCheckOut();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[0]);
    }
}
