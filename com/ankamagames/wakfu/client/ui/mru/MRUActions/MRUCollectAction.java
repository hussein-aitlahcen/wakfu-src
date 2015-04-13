package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.craft.util.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.craft.*;
import com.ankamagames.wakfu.common.game.craft.collect.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.resource.collect.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public class MRUCollectAction extends AbstractQueueCollectAction
{
    private Resource m_resource;
    private CollectAction m_collectAction;
    private ActionVisual m_actionVisual;
    
    public MRUCollectAction() {
        super();
    }
    
    public MRUCollectAction(final Resource resource, final CollectAction collectAction) {
        super();
        this.m_resource = resource;
        this.m_collectAction = collectAction;
        this.m_actionVisual = ActionVisualManager.getInstance().get(this.m_collectAction.getVisualId());
        this.m_pathListener = new MobileEndPathListener() {
            @Override
            public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                mobile.removeEndPositionListener(MRUCollectAction.this.m_pathListener);
                MRUCollectAction.this.collect(MRUCollectAction.this.m_resource);
            }
        };
    }
    
    @Override
    public CollectAction getCollectAction() {
        return this.m_collectAction;
    }
    
    @Override
    public AnimatedElementWithDirection getCollectedRessource() {
        return this.m_resource;
    }
    
    public Point3 getTarget() {
        return this.m_resource.getWorldCoordinates();
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
    public List<NationLaw> getTriggeredLaws() {
        if (AbstractLawMRUAction.getCurrentNationAlignment() == NationAlignement.ENEMY) {
            return null;
        }
        final List<NationLaw> triggeredLaws = new ArrayList<NationLaw>();
        final ReferenceResource referenceResource = this.m_resource.getReferenceResource();
        final Protector protector = ProtectorView.getInstance().getProtector();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (this.m_collectAction.isDestructive()) {
            final ArrayList<WakfuEcosystemFamilyInfo> families = new ArrayList<WakfuEcosystemFamilyInfo>();
            if (referenceResource instanceof MonsterReferenceResource) {
                for (final int familyId : ((MonsterReferenceResource)referenceResource).getMonsterFamilies()) {
                    families.add(WakfuMonsterZoneManager.getInstance().getMonsterFamilyInfo(familyId));
                }
            }
            else {
                families.add(WakfuResourceZoneManager.getInstance().getResourceFamilyInfo(referenceResource.getResourceType()));
            }
            for (final WakfuEcosystemFamilyInfo wakfuEcosystemFamilyInfo : families) {
                if (wakfuEcosystemFamilyInfo != null && wakfuEcosystemFamilyInfo.hasProtectorInterval() && !wakfuEcosystemFamilyInfo.isInProtectorInterval()) {
                    final ProtectorWishLawEvent wishLawEvent = new ProtectorWishLawEvent(localPlayer);
                    final int diffFromMax = wakfuEcosystemFamilyInfo.getProtectorIntervalDiffFromMax();
                    if (diffFromMax == 0) {
                        continue;
                    }
                    ProtectorWishLawEvent.ActionType type;
                    if (diffFromMax > 0) {
                        type = ProtectorWishLawEvent.ActionType.FOLLOWING;
                    }
                    else {
                        type = ProtectorWishLawEvent.ActionType.AGAINST;
                    }
                    wishLawEvent.setAction(type);
                    triggeredLaws.addAll(wishLawEvent.getTriggeringLaws());
                }
            }
        }
        if (!this.m_resource.getReferenceResource().hasProperty(ResourcesProperty.NO_NATION_LAWS)) {
            final CollectLawEvent collectLawEvent = new CollectLawEvent(localPlayer, this.m_resource, this.m_collectAction, (protector == null) ? null : protector.getEcosystemHandler());
            triggeredLaws.addAll(collectLawEvent.getTriggeringLaws());
        }
        return triggeredLaws;
    }
    
    @Override
    public List<NationLaw> getProbablyTriggeredLaws() {
        return null;
    }
    
    @Override
    public boolean isRunnable() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isActiveProperty(WorldPropertyType.RESOURCE_COLLECT_DISABLED)) {
            return false;
        }
        final boolean runnable = this.m_actionVisual.isEnabled() && this.getState() != CollectConstants.State.UNUSABLE_CRAFT && !localPlayer.isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning();
        if (runnable) {
            this.setEnabled(true);
            final DimensionalBagView visitingDimensionalBag = localPlayer.getVisitingDimentionalBag();
            if (visitingDimensionalBag != null && !visitingDimensionalBag.canPlayerInteractWithContentInRoom(localPlayer, this.m_resource.getWorldCellX(), this.m_resource.getWorldCellY())) {
                this.setEnabled(false);
            }
        }
        return runnable;
    }
    
    @Override
    public String getTranslatorKey() {
        if (this.m_actionVisual == null) {
            return "undefined";
        }
        return this.m_actionVisual.getMruLabelKey();
    }
    
    public Color getTooltipColor() {
        if (this.m_collectAction.getCraftId() == 0) {
            return Color.GREEN;
        }
        final ReferenceCraft craft = CraftManager.INSTANCE.getCraft(this.m_collectAction.getCraftId());
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final short playerLevel = localPlayer.getCraftHandler().getLevel(this.m_collectAction.getCraftId());
        final int targetLevel = this.m_collectAction.getLevelMin();
        final boolean obsolete = CraftXPUtil.getXPGain(playerLevel, (short)targetLevel, craft, localPlayer) == 0L;
        return (this.getState() != CollectConstants.State.WRONG_LEVEL) ? (obsolete ? Color.GRAY : Color.GREEN) : Color.RED;
    }
    
    @Override
    public boolean isEnabled() {
        return this.getState() == CollectConstants.State.AVAILABLE;
    }
    
    @Override
    protected int getGFXId() {
        return this.m_actionVisual.getMruGfx();
    }
    
    private CollectConstants.State getState() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CraftHandler craftHandler = localPlayer.getCraftHandler();
        final ConsumableInfo consumableInfo = this.m_collectAction.getConsumableInfo();
        final boolean localPlayerSubscribedZone = WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
        if (!localPlayerSubscribedZone) {
            return CollectConstants.State.PLAYER_NOT_SUBSCRIBED;
        }
        if (localPlayer.getNationId() <= 0 && localPlayer.getTravellingNationId() > 0) {
            return CollectConstants.State.NO_NATION;
        }
        if (this.m_collectAction.getCraftId() != 0 && (!craftHandler.contains(this.m_collectAction.getCraftId()) || QueueCollectManager.getInstance().hasActionOnResource(this.m_resource))) {
            return CollectConstants.State.UNUSABLE_CRAFT;
        }
        if (craftHandler.getLevel(this.m_collectAction.getCraftId()) < this.m_collectAction.getLevelMin()) {
            return CollectConstants.State.WRONG_LEVEL;
        }
        if (!this.m_actionVisual.isValidFor(localPlayer)) {
            return CollectConstants.State.UNUSABLE_CRAFT;
        }
        if (this.m_collectAction.getCriterion() != null && !this.m_collectAction.getCriterion().isValid(localPlayer, this.m_source, null, null)) {
            return CollectConstants.State.INVALID_CRITERION;
        }
        if (consumableInfo.hasConsumable() && localPlayer.getBags().getFirstItemFromInventory(consumableInfo.getConsumableId()) == null && ((QuestInventory)localPlayer.getInventory(InventoryType.QUEST)).getItem(consumableInfo.getConsumableId()) == null) {
            return CollectConstants.State.NO_CONSUMABLE;
        }
        final boolean timerEnabled = SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.TIMER_FOR_FIRST_COLLECT);
        if (!this.isChallengeResource() && timerEnabled) {
            final WakfuAccountInformationHandler accountInfo = localPlayer.getAccountInformationHandler();
            final GameDate now = WakfuGameCalendar.getInstance().getNewDate();
            final long sessionStartTime = accountInfo.getSessionStartTime();
            if (now.toLong() - sessionStartTime < 60000L) {
                return CollectConstants.State.TIMER_NOT_FINISHED;
            }
        }
        if (this.m_resource == localPlayer.getCurrentInteractiveElement()) {
            return CollectConstants.State.ALREADY_COLLECTING;
        }
        return CollectConstants.State.AVAILABLE;
    }
    
    private boolean isChallengeResource() {
        final ReferenceResource referenceResource = this.m_resource.getReferenceResource();
        return referenceResource != null && referenceResource.hasProperty(ResourcesProperty.CHALLENGE_RESOURCE);
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.b().append(WakfuTranslator.getInstance().getString("desc.mru." + this.getTranslatorKey()))._b();
        if (this.m_collectAction.getCollectItemId() != 0) {
            sb.newLine();
            final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_collectAction.getCollectItemId());
            if (refItem != null) {
                sb.append(refItem.getName());
            }
            else {
                sb.append("#ERROR#");
            }
        }
        final int craftId = this.m_collectAction.getCraftId();
        if (craftId != 0) {
            sb.newLine();
            final String craftName = WakfuTranslator.getInstance().getString(43, craftId, new Object[0]);
            sb.append(craftName).append(" : ").openText().addColor(this.getTooltipColor().getRGBtoHex()).append(WakfuTranslator.getInstance().getString("desc.mru.levelRequired", this.m_collectAction.getLevelMin())).closeText();
        }
        final CollectConstants.State state = this.getState();
        final ConsumableInfo consumableInfo = this.m_collectAction.getConsumableInfo();
        if (consumableInfo.hasConsumable()) {
            sb.newLine();
            final int id = consumableInfo.getConsumableId();
            final AbstractReferenceItem ref = ReferenceItemManager.getInstance().getReferenceItem(id);
            final String itemName = ref.getName();
            sb.append(WakfuTranslator.getInstance().getString("itemNeeded"));
            sb.openText().addColor((state == CollectConstants.State.NO_CONSUMABLE) ? MRUCollectAction.NOK_TOOLTIP_COLOR : MRUCollectAction.OK_TOOLTIP_COLOR).append(itemName).closeText();
        }
        if (state != CollectConstants.State.AVAILABLE) {
            switch (state) {
                case PLAYER_NOT_SUBSCRIBED: {
                    sb.newLine().openText().addColor(MRUCollectAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed")).closeText();
                    break;
                }
                case NO_NATION: {
                    sb.newLine().openText().addColor(MRUCollectAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNoNation")).closeText();
                    break;
                }
                case TIMER_NOT_FINISHED: {
                    sb.newLine().openText().addColor(MRUCollectAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.timerNotFinished")).closeText();
                    break;
                }
                case WRONG_LEVEL: {
                    sb.newLine().openText().addColor(MRUCollectAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("collect.error.wrongLevel")).closeText();
                    break;
                }
                case INVALID_CRITERION: {
                    final SimpleCriterion crit = this.m_collectAction.getCriterion();
                    if (crit != null) {
                        sb.newLine().openText().addColor(MRUCollectAction.NOK_TOOLTIP_COLOR).append(CriterionDescriptionGenerator.getDescription(crit)).closeText();
                        break;
                    }
                    sb.newLine().openText().addColor(MRUCollectAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.unknown")).closeText();
                    break;
                }
                case ALREADY_COLLECTED: {
                    sb.newLine().openText().addColor(MRUCollectAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("collect.error.tooEarly")).closeText();
                    break;
                }
                case ALREADY_COLLECTING: {
                    sb.newLine().openText().addColor(MRUCollectAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("collect.resourceAlreadyUseByPlayer")).closeText();
                    break;
                }
                case WRONG_ITEM_EQUIPED: {
                    sb.newLine().openText().addColor(MRUCollectAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("action.error.wrongItemEquiped")).closeText();
                    break;
                }
                case NO_CONSUMABLE: {
                    break;
                }
                default: {
                    sb.newLine().openText().addColor(MRUCollectAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("unknown")).closeText();
                    break;
                }
            }
        }
        final int requiredPlayer = this.m_collectAction.getNbRequiredPlayer();
        if (requiredPlayer > 1) {
            sb.newLine().append(WakfuTranslator.getInstance().getString("collect.nbPlayers", requiredPlayer));
        }
        return sb.finishAndToString();
    }
    
    private void collect(final Resource resource) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterActor actor = localPlayer.getActor();
        actor.setDirection(localPlayer.getPosition().getDirectionTo(this.m_resource.getWorldCoordinates()));
        final int minRange = 1;
        final int maxRange = CollectPositionChecker.getMaxCollectDistance(this.m_collectAction.getCraftId());
        final int dist = localPlayer.getPosition().getDistance(this.m_resource.getWorldCoordinates());
        if (dist < 1 || dist > maxRange) {
            QueueCollectManager.getInstance().executeNextCollectAction();
            return;
        }
        QueueCollectManager.getInstance().createTimeOut();
        final ActorCollectRequestMessage message = new ActorCollectRequestMessage(this.m_collectAction.getId(), resource.getWorldCellX(), resource.getWorldCellY());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
    }
    
    @Override
    public void run() {
        this.run(false);
    }
    
    @Override
    public void run(final boolean indirect) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!this.isRunnable()) {
            QueueCollectManager.getInstance().executeNextCollectAction();
            return;
        }
        if (this.m_resource == null || !this.m_resource.canInteractWith()) {
            QueueCollectManager.getInstance().executeNextCollectAction();
            return;
        }
        final AnimatedInteractiveElement iElt = localPlayer.getCurrentInteractiveElement();
        if (iElt != null && iElt == this.m_resource) {
            QueueCollectManager.getInstance().executeNextCollectAction();
            return;
        }
        if (!localPlayer.cancelCurrentOccupation(false, true)) {
            return;
        }
        final int minRange = 1;
        final int maxRange = CollectPositionChecker.getMaxCollectDistance(this.m_collectAction.getCraftId());
        TopologyMapManager.setMoverCaracteristics(localPlayer.getHeight(), localPlayer.getPhysicalRadius(), localPlayer.getJumpCapacity());
        final List<Point3> targets = new ArrayList<Point3>();
        boolean mustMove = true;
        final Direction8[] directions = Direction8.getDirection4Values();
        final Point3 position = localPlayer.getPosition();
        for (int i = 0; i < directions.length; ++i) {
            final Direction8 dir = directions[i];
            final Point3 pos = new Point3(this.m_resource.getWorldCoordinates());
            for (int k = 1; k <= maxRange; ++k) {
                pos.shift(dir);
                final short z = TopologyMapManager.getNearestWalkableZ(pos.getX(), pos.getY(), pos.getZ());
                if (z != -32768 && CollectPositionChecker.isAltitudeValid(localPlayer.getHeight(), z, this.m_resource.getPosition().getZ(), this.m_collectAction.getCraftId())) {
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
        if (mustMove) {
            localPlayer.getActor().addEndPositionListener(this.m_pathListener);
            final boolean useDiagonal = position.getDistance(this.m_resource.getWorldCoordinates()) > 2;
            if (!localPlayer.moveTo(false, useDiagonal, targets)) {
                localPlayer.getActor().removeEndPositionListener(this.m_pathListener);
                ErrorsMessageTranslator.getInstance().pushMessage(2, 3, new Object[0]);
                QueueCollectManager.getInstance().executeNextCollectAction();
            }
        }
        else {
            this.collect(this.m_resource);
        }
    }
}
