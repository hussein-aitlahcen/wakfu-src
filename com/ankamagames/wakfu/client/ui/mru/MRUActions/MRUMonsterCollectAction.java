package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.wakfu.common.game.craft.collect.*;
import com.ankamagames.wakfu.common.game.protector.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.game.craft.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class MRUMonsterCollectAction extends AbstractQueueCollectAction
{
    private CollectAction m_collectAction;
    private ActionVisual m_actionVisual;
    
    @Override
    public List<NationLaw> getTriggeredLaws() {
        if (AbstractLawMRUAction.getCurrentNationAlignment() != NationAlignement.ALLIED) {
            return null;
        }
        final List<NationLaw> triggeredLaws = new ArrayList<NationLaw>();
        final Protector protector = ProtectorView.getInstance().getProtector();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CollectLawEvent collectLawEvent = new CollectLawEvent(localPlayer, (Collectible)this.m_source, this.m_collectAction, (protector == null) ? null : protector.getEcosystemHandler());
        triggeredLaws.addAll(collectLawEvent.getTriggeringLaws());
        return triggeredLaws;
    }
    
    @Override
    public List<NationLaw> getProbablyTriggeredLaws() {
        return null;
    }
    
    public MRUMonsterCollectAction(final CollectAction collectAction) {
        super();
        this.m_collectAction = collectAction;
        this.m_actionVisual = ActionVisualManager.getInstance().get(this.m_collectAction.getVisualId());
        this.m_pathListener = new MobileEndPathListener() {
            @Override
            public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                mobile.removeEndPositionListener(this);
                MRUMonsterCollectAction.this.collect((CharacterInfo)MRUMonsterCollectAction.this.m_source, true);
            }
        };
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUCollectAction();
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.COLLECT_ACTION;
    }
    
    @Override
    public void run() {
        this.run(false);
    }
    
    @Override
    public boolean isEnabled() {
        return this.getState() == CollectConstants.State.AVAILABLE;
    }
    
    @Override
    public void run(final boolean indirect) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!this.isRunnable()) {
            QueueCollectManager.getInstance().executeNextCollectAction();
            return;
        }
        if (this.m_source == null) {
            QueueCollectManager.getInstance().executeNextCollectAction();
            return;
        }
        final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_source;
        if (!localPlayer.cancelCurrentOccupation(false, true)) {
            return;
        }
        localPlayer.getActor().addEndPositionListener(this.m_pathListener);
        final ItemType itemType = null;
        int minRange;
        int maxRange;
        if (this.m_actionVisual.needItem()) {
            final Item item = this.m_actionVisual.getFirstValidItemInInventory(localPlayer);
            localPlayer.getEquipmentInventory().getFromPosition(EquipmentPosition.ACCESSORY.m_id);
            if (item == null) {
                QueueCollectManager.getInstance().executeNextCollectAction();
                return;
            }
            minRange = item.getReferenceItem().getUseRangeMin();
            maxRange = item.getReferenceItem().getUseRangeMax();
        }
        else {
            minRange = 1;
            maxRange = 1;
        }
        TopologyMapManager.setMoverCaracteristics(localPlayer.getHeight(), localPlayer.getPhysicalRadius(), localPlayer.getJumpCapacity());
        final ArrayList<Point3> targets = new ArrayList<Point3>();
        boolean mustMove = true;
        final Direction8[] directions = Direction8.getDirection4Values();
        final Point3 position = localPlayer.getPosition();
        for (int i = 0; i < directions.length; ++i) {
            final Direction8 dir = directions[i];
            final Point3 pos = new Point3(npc.getPosition());
            for (int k = minRange; k <= maxRange; ++k) {
                pos.shift(dir);
                final short z = TopologyMapManager.getNearestWalkableZ(pos.getX(), pos.getY(), pos.getZ());
                if (z != -32768) {
                    pos.setZ(z);
                    targets.add(pos);
                    mustMove &= !position.equals(pos);
                    break;
                }
            }
        }
        if (targets.isEmpty()) {
            ErrorsMessageTranslator.getInstance().pushMessage(2, 3, new Object[0]);
            QueueCollectManager.getInstance().executeNextCollectAction();
            return;
        }
        if (localPlayer.getActor().isMoving()) {
            mustMove = true;
        }
        if (!mustMove) {
            this.collect(npc, false);
            return;
        }
        localPlayer.getActor().addEndPositionListener(this.m_pathListener);
        final boolean useDiagonal = position.getDistance(npc.getPosition()) > 2;
        if (!localPlayer.moveTo(false, useDiagonal, targets)) {
            localPlayer.getActor().removeEndPositionListener(this.m_pathListener);
            ErrorsMessageTranslator.getInstance().pushMessage(2, 3, new Object[0]);
        }
    }
    
    @Override
    public CollectAction getCollectAction() {
        return this.m_collectAction;
    }
    
    @Override
    public AnimatedElementWithDirection getCollectedRessource() {
        if (!(this.m_source instanceof NonPlayerCharacter)) {
            return null;
        }
        return ((NonPlayerCharacter)this.m_source).getMobile();
    }
    
    public Point3 getTarget() {
        return null;
    }
    
    private CollectConstants.State getState() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_source;
        final CraftHandler craftHandler = localPlayer.getCraftHandler();
        if (!WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer)) {
            return CollectConstants.State.PLAYER_NOT_SUBSCRIBED;
        }
        if (localPlayer.getNationId() <= 0 && localPlayer.getTravellingNationId() > 0) {
            return CollectConstants.State.NO_NATION;
        }
        if (!npc.isCollectAvailable(this.m_collectAction.getId())) {
            return CollectConstants.State.ALREADY_COLLECTED;
        }
        if (!this.m_actionVisual.isValidFor(localPlayer)) {
            return CollectConstants.State.WRONG_ITEM_EQUIPED;
        }
        if (craftHandler.getLevel(this.m_collectAction.getCraftId()) < this.m_collectAction.getLevelMin()) {
            return CollectConstants.State.WRONG_LEVEL;
        }
        if (this.m_collectAction.getCriterion() != null && !this.m_collectAction.getCriterion().isValid(localPlayer, this.m_source, null, null)) {
            return CollectConstants.State.INVALID_CRITERION;
        }
        if (!craftHandler.contains(this.m_collectAction.getCraftId())) {
            return CollectConstants.State.UNUSABLE_CRAFT;
        }
        return CollectConstants.State.AVAILABLE;
    }
    
    @Override
    public boolean isRunnable() {
        if (!this.m_actionVisual.isEnabled()) {
            return false;
        }
        if (this.getState() == CollectConstants.State.UNUSABLE_CRAFT) {
            return false;
        }
        final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_source;
        if (npc.isOnFight()) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isActiveProperty(WorldPropertyType.MONSTER_COLLECT_DISABLED)) {
            return false;
        }
        final boolean criterionCheck = this.m_collectAction.getCriterion() == null || this.m_collectAction.getCriterion().isValid(localPlayer, this.m_source, null, null);
        final boolean fightCheck = !localPlayer.isOnFight();
        final boolean exchange = !ClientTradeHelper.INSTANCE.isTradeRunning();
        return criterionCheck && fightCheck && exchange;
    }
    
    @Override
    public String getTranslatorKey() {
        if (this.m_actionVisual == null) {
            return "undefined";
        }
        return this.m_actionVisual.getMruLabelKey();
    }
    
    public Color getTooltipColor() {
        return (this.getState() == CollectConstants.State.AVAILABLE) ? Color.GREEN : Color.RED;
    }
    
    @Override
    protected int getGFXId() {
        return this.m_actionVisual.getMruGfx();
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b();
        sb.append(WakfuTranslator.getInstance().getString("desc.mru." + this.getTranslatorKey()));
        sb._b().append("\n");
        final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_collectAction.getCollectItemId());
        sb.append((refItem != null) ? refItem.getName() : "#ERROR#").append("\n");
        final String craftName = WakfuTranslator.getInstance().getString(43, this.m_collectAction.getCraftId(), new Object[0]);
        sb.append(craftName).append(" ");
        final CollectConstants.State state = this.getState();
        final Color levelColor = (state == CollectConstants.State.WRONG_LEVEL) ? Color.RED : Color.GREEN;
        sb.openText().addColor(levelColor.getRGBtoHex());
        sb.append(WakfuTranslator.getInstance().getString("desc.mru.levelRequired", this.m_collectAction.getLevelMin()));
        sb.closeText();
        if (state != CollectConstants.State.AVAILABLE) {
            sb.openText().addColor(Color.RED.getRGBtoHex());
            switch (state) {
                case IN_FIGHT: {
                    sb.append("\n").append(WakfuTranslator.getInstance().getString("collect.error.npc.inFight"));
                    break;
                }
                case NO_NATION: {
                    sb.append("\n").append(WakfuTranslator.getInstance().getString("error.playerNoNation"));
                    break;
                }
                case PLAYER_NOT_SUBSCRIBED: {
                    sb.append("\n").append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed"));
                    break;
                }
                case WRONG_LEVEL: {
                    sb.append("\n").append(WakfuTranslator.getInstance().getString("collect.error.wrongLevel"));
                    break;
                }
                case ALREADY_COLLECTED: {
                    sb.append("\n").append(WakfuTranslator.getInstance().getString("collect.error.npc.collected"));
                    break;
                }
                case INVALID_CRITERION: {
                    final SimpleCriterion crit = this.m_collectAction.getCriterion();
                    if (crit != null) {
                        sb.newLine().append(CriterionDescriptionGenerator.getDescription(crit));
                        break;
                    }
                    sb.append("\n").append(WakfuTranslator.getInstance().getString("error.unknown"));
                    break;
                }
                case WRONG_ITEM_EQUIPED: {
                    sb.append("\n").append(WakfuTranslator.getInstance().getString("action.error.wrongItemEquiped"));
                    break;
                }
            }
            sb.closeText();
        }
        return sb.finishAndToString();
    }
    
    private void collect(final CharacterInfo characterInfo, final boolean move4Directions) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterActor actor = localPlayer.getActor();
        final int distance = DistanceUtils.getIntersectionDistance(actor, characterInfo);
        if (distance <= 1) {
            final Direction8 direction = actor.getWorldCoordinates().getDirectionTo(((CharacterInfo)this.m_source).getPosition());
            if (direction != null) {
                actor.setDirection(direction);
            }
            if (this.m_actionVisual.needItem()) {
                final Item item = this.m_actionVisual.getFirstValidItemInInventory(localPlayer);
                if (item == null) {
                    QueueCollectManager.getInstance().executeNextCollectAction();
                    return;
                }
            }
            final float dz = Math.abs(characterInfo.getWorldCellAltitude() - actor.getAltitude());
            if (dz > actor.getJumpCapacity()) {
                MRUMonsterCollectAction.m_logger.error((Object)("Plantation impossible sur la cellule " + characterInfo.getPosition() + " : hauteur maximale de saut de " + actor.getJumpCapacity()));
                ErrorsMessageTranslator.getInstance().pushMessage(2, 3, new Object[0]);
                QueueCollectManager.getInstance().executeNextCollectAction();
                return;
            }
            actor.removeEndPositionListener(this.m_pathListener);
            QueueCollectManager.getInstance().createTimeOut();
            final ActorCollectMonsterRequestMessage message = new ActorCollectMonsterRequestMessage(this.m_collectAction.getId(), characterInfo.getId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        }
        else if (move4Directions && distance <= 2) {
            actor.addEndPositionListener(this.m_pathListener);
            if (!localPlayer.moveNearTarget((Target)this.m_source, false, false)) {
                actor.removeEndPositionListener(this.m_pathListener);
                ErrorsMessageTranslator.getInstance().pushMessage(2, 3, new Object[0]);
                QueueCollectManager.getInstance().executeNextCollectAction();
            }
        }
    }
    
    protected enum CanUseError
    {
        NO_ERROR, 
        WRONG_ITEM_EQUIPED, 
        ITEM_NOT_USABLE, 
        NO_PATH;
    }
}
