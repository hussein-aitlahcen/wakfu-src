package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class MRUCastTrainingFightAction extends AbstractMRUAction
{
    private final Point3 m_destPos;
    private boolean m_teleportFighters;
    private int m_fightType;
    private boolean m_withBorders;
    private byte m_battleGroundType;
    private Point3 m_battlegroundCenter;
    private Point3 m_attackerPos;
    private Point3 m_defenderPos;
    private int[] m_bgParams;
    
    public MRUCastTrainingFightAction() {
        super();
        this.m_teleportFighters = false;
        this.m_fightType = 2;
        this.m_withBorders = true;
        this.m_battleGroundType = BattlegroundTypes.RANDOM_IN_CUSTOM_PVP.getId();
        this.m_battlegroundCenter = null;
        this.m_attackerPos = null;
        this.m_defenderPos = null;
        this.m_bgParams = null;
        this.m_destPos = new Point3();
    }
    
    public MRUCastTrainingFightAction(final Point3 pos, final boolean teleport, final int type, final boolean borders, final byte bgType, final Point3 center, final int[] bgParams, final Point3 attackerPos, final Point3 defenderPos) {
        super();
        this.m_teleportFighters = false;
        this.m_fightType = 2;
        this.m_withBorders = true;
        this.m_battleGroundType = BattlegroundTypes.RANDOM_IN_CUSTOM_PVP.getId();
        this.m_battlegroundCenter = null;
        this.m_attackerPos = null;
        this.m_defenderPos = null;
        this.m_bgParams = null;
        this.m_destPos = pos;
        this.m_teleportFighters = teleport;
        this.m_fightType = type;
        this.m_withBorders = borders;
        this.m_battleGroundType = bgType;
        this.m_battlegroundCenter = center;
        this.m_bgParams = bgParams;
        this.m_attackerPos = attackerPos;
        this.m_defenderPos = defenderPos;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUCastTrainingFightAction(this.m_destPos, this.m_teleportFighters, this.m_fightType, this.m_withBorders, this.m_battleGroundType, this.m_battlegroundCenter, this.m_bgParams, this.m_attackerPos, this.m_defenderPos);
    }
    
    public void setBattlegroundCenter(final Point3 battlegroundCenter) {
        this.m_battlegroundCenter = battlegroundCenter;
    }
    
    public void setBattleGroundType(final byte battleGroundType) {
        this.m_battleGroundType = battleGroundType;
    }
    
    public void setBgParams(final int[] bgParams) {
        this.m_bgParams = bgParams;
    }
    
    public void setFightType(final int fightType) {
        this.m_fightType = fightType;
    }
    
    public void setTeleportFighters(final boolean teleportFighters) {
        this.m_teleportFighters = teleportFighters;
    }
    
    public void setWithBorders(final boolean withBorders) {
        this.m_withBorders = withBorders;
    }
    
    public void setAttackerPos(final Point3 attackerPos) {
        this.m_attackerPos = attackerPos;
    }
    
    public void setDefenderPos(final Point3 defenderPos) {
        this.m_defenderPos = defenderPos;
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUCastTrainingFightAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        final CharacterActor localPlayerActor = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
        final CharacterActor actor = ((CharacterInfo)this.m_source).getActor();
        final int xCharac = actor.getCurrentWorldX();
        final int yCharac = actor.getCurrentWorldY();
        final short zCharac = (short)actor.getAltitude();
        final int xPos = localPlayerActor.getWorldCellX();
        final int yPos = localPlayerActor.getWorldCellY();
        if (Math.abs(xCharac - xPos) <= 10 && Math.abs(yCharac - yPos) <= 10) {
            final ParameterizedFightCreationRequestMessage netMessage = new ParameterizedFightCreationRequestMessage();
            netMessage.setTargetId(((CharacterInfo)this.m_source).getId());
            netMessage.setTargetPosition(xCharac, yCharac, zCharac);
            netMessage.setBattlegroundCenter(this.m_battlegroundCenter);
            netMessage.setBattleGroundType(this.m_battleGroundType);
            netMessage.setBgParams(this.m_bgParams);
            netMessage.setFightType(this.m_fightType);
            netMessage.setTeleportFighters(this.m_teleportFighters);
            netMessage.setWithBorders(this.m_withBorders);
            netMessage.setAttackerPos(this.m_attackerPos);
            netMessage.setDefenderPos(this.m_defenderPos);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
        }
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof CharacterInfo)) {
            return false;
        }
        if (((CharacterInfo)this.m_source).isOnFight()) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !localPlayer.isOnFight() && localPlayer.getVisitingDimentionalBag() == null && !localPlayer.isWaitingForResult() && !ClientTradeHelper.INSTANCE.isTradeRunning() && localPlayer.getPropertyValue(WorldPropertyType.CANT_ATTACK) <= 0 && !((CharacterInfo)this.m_source).isActiveProperty(WorldPropertyType.CANT_BE_ATTACKED);
    }
    
    @Override
    public String getTranslatorKey() {
        return "trainingFightStart";
    }
    
    @Override
    protected int getGFXId() {
        return MRUActions.CHARACTER_CAST_FIGHT_ACTION.getActionId();
    }
}
