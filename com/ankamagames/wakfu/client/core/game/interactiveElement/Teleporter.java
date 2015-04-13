package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.gameAction.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.basicDungeon.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.game.dungeon.*;
import com.ankamagames.wakfu.common.game.lock.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import gnu.trove.*;

public class Teleporter extends WakfuClientMapInteractiveElement implements ChaosInteractiveElement
{
    private static final Logger m_logger;
    public static final ObjectFactory<Teleporter> FACTORY;
    private IETeleporterParameter m_info;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case ACTIVATE: {
                final IETeleporterParameter.Exit exit = this.m_info.dropDestination(user, user, this, ((ActionUser)user).getAppropriateContext());
                return this.use(user, exit);
            }
            default: {
                Teleporter.m_logger.error((Object)"Action invalide sur un teleporter", (Throwable)new IllegalArgumentException(action.toString()));
                return false;
            }
        }
    }
    
    public boolean use(final InteractiveElementUser user, final IETeleporterParameter.Exit exit) {
        if (!this.hasValidExit(user)) {
            return true;
        }
        if (!this.canPay(exit)) {
            return false;
        }
        this.runScript(this.getDefaultAction());
        final CharacterInfo characterInfo = (CharacterInfo)user;
        final CharacterActor actor = characterInfo.getActor();
        if (actor.getCurrentPath() != null) {
            actor.addEndPositionListener(new MobileEndPathListener() {
                @Override
                public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                    actor.removeEndPositionListener(this);
                    if (x == Teleporter.this.getWorldCellX() && y == Teleporter.this.getWorldCellY()) {
                        Teleporter.this.teleport(actor, Teleporter.this.getDefaultAction(), exit);
                    }
                }
            });
            return true;
        }
        this.teleport(actor, this.getDefaultAction(), exit);
        return true;
    }
    
    private void teleport(final CharacterActor actor, final InteractiveElementAction action, final IETeleporterParameter.Exit exit) {
        if (exit.getApsId() > 0) {
            final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(exit.getApsId());
            if (system != null) {
                system.setPosition(this.getWorldX(), this.getWorldCellY(), this.getWorldCellAltitude());
                IsoParticleSystemManager.getInstance().addParticleSystem(system);
            }
        }
        final ActionVisual visual = ActionVisualManager.getInstance().get(exit.getVisualId());
        if (visual != null) {
            ActionVisualHelper.applyActionVisual(actor, visual);
        }
        this.sendActivateMessage(this.getAnimationDuration(actor, visual, exit), exit);
    }
    
    private boolean hasValidExit(final InteractiveElementUser user) {
        final IETeleporterParameter.Exit exit = this.m_info.dropDestination(user, user, this, ((BasicCharacterInfo)user).getAppropriateContext());
        return exit != null;
    }
    
    private int getAnimationDuration(final CharacterActor actor, final ActionVisual visual, final IETeleporterParameter.Exit exit) {
        if (visual != null && exit.getDelay() == 0) {
            final int animDuration = actor.getAnimationDuration(visual.getAnimLink());
            if (animDuration != 0 && animDuration != Integer.MAX_VALUE) {
                return animDuration;
            }
        }
        return exit.getDelay();
    }
    
    private void sendActivateMessage(final int delay, final IETeleporterParameter.Exit exit) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        player.setCanMoveAndInteract(false);
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                WakfuGameEntity.getInstance().getLocalPlayer().setCanMoveAndInteract(true);
                final TeleporterActionMessage msg = new TeleporterActionMessage();
                msg.setElementId(Teleporter.this.getId());
                msg.setExitId(exit.getId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
            }
        }, delay, 1);
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
        assert this.m_info == null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_info = null;
    }
    
    @Override
    public void initializeWithParameter() {
        try {
            this.m_info = (IETeleporterParameter)IEParametersManager.INSTANCE.getParam(IETypes.TELEPORTER, Integer.parseInt(this.m_parameter));
        }
        catch (NumberFormatException e) {
            Teleporter.m_logger.error((Object)("Erreur de param\u00e9trage d'IE " + this), (Throwable)e);
        }
        finally {
            if (this.m_info == null) {
                this.m_info = IETeleporterParameter.FAKE_PARAM;
            }
        }
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final TIntObjectHashMap<IETeleporterParameter.Exit> exits = this.m_info.getExits();
        final ArrayList<AbstractMRUAction> mruActions = new ArrayList<AbstractMRUAction>();
        final TIntObjectIterator<IETeleporterParameter.Exit> exitIterator = new TIntObjectIterator<IETeleporterParameter.Exit>(exits);
        while (exitIterator.hasNext()) {
            exitIterator.advance();
            final IETeleporterParameter.Exit currentExit = exitIterator.value();
            final boolean isDisabled = this.isTeleportDisabled(currentExit);
            if (currentExit.isInvisibleIfFalse() && isDisabled) {
                continue;
            }
            final ActionVisual visual = ActionVisualManager.getInstance().get(currentExit.getVisualId());
            final MRUTeleporterAction action = MRUActions.TELEPORTER_ACTION.getMRUAction();
            action.setGfxId(visual.getMruGfx());
            action.setTextKey("desc.mru." + visual.getMruLabelKey());
            action.setCostText(this.getCostText(currentExit));
            action.setCanPay(this.canPay(currentExit));
            action.setLocked(isDisabled);
            action.setLockText(this.getRequirements(currentExit.getCriterion()));
            action.setLinkedInstanceId(currentExit.getWorldId());
            action.setExit(currentExit);
            mruActions.add(action);
        }
        final MRUTeleporterAction[] toSort = new MRUTeleporterAction[mruActions.size()];
        mruActions.toArray(toSort);
        Arrays.sort(toSort, new MRUTeleporterActionComparator());
        return toSort;
    }
    
    private boolean isTeleportDisabled(final IETeleporterParameter.Exit currentExit) {
        return this.isLocked() || this.isCriterionInvalid(currentExit) || !this.canEnterDungeonWise() || !this.hasRequiredLevel();
    }
    
    private boolean canEnterDungeonWise() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return this.m_info.getLockId() < 0 || localPlayer.getNationId() > 0 || !LockManager.INSTANCE.isAvailableOnlyForCitizens(this.m_info.getLockId());
    }
    
    private boolean hasRequiredLevel() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final LockContext context = WakfuGameEntity.getInstance().getLocalPlayer().getLockContext();
        final LockInstance lock = context.getLock(this.m_info.getLockId());
        if (lock == null) {
            return true;
        }
        final int dungeonId = lock.getLockedItem();
        final DungeonDefinition dungeonDefinition = DungeonManager.INSTANCE.getDungeon(dungeonId);
        return dungeonDefinition == null || localPlayer.getLevel() >= dungeonDefinition.getMinLevel();
    }
    
    private boolean isLocked() {
        return this.m_info == null || (this.m_info.getLockId() >= 0 && WakfuGameEntity.getInstance().getLocalPlayer().getLockContext().isLocked(this.m_info.getLockId()));
    }
    
    private boolean isCriterionInvalid(final IETeleporterParameter.Exit currentExit) {
        if (this.m_info == null) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !currentExit.getCriterion().isValid(localPlayer, null, null, localPlayer.getAppropriateContext());
    }
    
    private String getRequirements(final SimpleCriterion criterion) {
        final String lockText = this.getLockText();
        final String criterionText = this.getCriterionText(criterion);
        final String dungeonText = this.getDungeonText();
        boolean first = true;
        final TextWidgetFormater sb = new TextWidgetFormater();
        if (lockText != null) {
            sb.append(lockText);
            first = false;
        }
        if (criterionText != null) {
            if (!first) {
                sb.newLine();
            }
            sb.append(criterionText);
            first = false;
        }
        if (dungeonText != null) {
            if (!first) {
                sb.newLine();
            }
            sb.append(dungeonText);
            first = false;
        }
        final String result = sb.toString();
        return result.isEmpty() ? null : result;
    }
    
    private String getLockText() {
        if (this.m_info == null) {
            return null;
        }
        if (this.m_info.getLockId() < 0) {
            return null;
        }
        final LockContext context = WakfuGameEntity.getInstance().getLocalPlayer().getLockContext();
        final LockInstance lock = context.getLock(this.m_info.getLockId());
        final boolean locked = context.isLocked(lock.getId());
        final String dungeonName = this.getName();
        final String lockText = LockHelper.getLockText(WakfuGameEntity.getInstance().getLocalPlayer(), this.m_info.getLockId());
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(dungeonName);
        if (!lockText.isEmpty()) {
            sb.newLine().b().addColor(Color.RED.getRGBtoHex()).append(lockText)._b();
        }
        return sb.finishAndToString();
    }
    
    private String getDungeonText() {
        return this.canEnterDungeonWise() ? null : WakfuTranslator.getInstance().getString("error.playerNoNation");
    }
    
    private String getCriterionText(final SimpleCriterion criterion) {
        if (this.m_info == null) {
            return null;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (criterion.isValid(localPlayer, null, null, localPlayer.getAppropriateContext())) {
            return null;
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.openText().addColor(Color.RED);
        sb.append(CriterionDescriptionGenerator.getDescription(criterion));
        return sb.finishAndToString();
    }
    
    private boolean canPay(final IETeleporterParameter.Exit exit) {
        return this.m_info != null && LootChest.canPay(exit.getConsumedItemRefId(), this.computeConsumedItemQuantity(exit), exit.getConsumedKamas());
    }
    
    private String getCostText(final IETeleporterParameter.Exit exit) {
        if (this.m_info == null) {
            return null;
        }
        return LootChest.getCostText(exit.getConsumedItemRefId(), this.computeConsumedItemQuantity(exit), exit.getConsumedKamas());
    }
    
    private short computeConsumedItemQuantity(final IETeleporterParameter.Exit exit) {
        final int consumedItemRefId = exit.getConsumedItemRefId();
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(consumedItemRefId);
        if (referenceItem == null) {
            return exit.getConsumedItemQty();
        }
        final boolean isQuestItem = referenceItem.hasItemProperty(ItemProperty.QUEST);
        if (isQuestItem) {
            return exit.getConsumedItemQty();
        }
        final short[] destinationInstanceIds = this.m_info.getDestinationInstanceIds();
        boolean containsDungeon = false;
        for (int i = 0; i < destinationInstanceIds.length; ++i) {
            final short destinationInstanceId = destinationInstanceIds[i];
            final WorldInfoManager.WorldInfo info = WorldInfoManager.getInstance().getInfo(destinationInstanceId);
            if (info != null) {
                containsDungeon |= info.isDungeon();
            }
        }
        if (containsDungeon) {
            final int companionsCount = this.getCompanionsCount();
            return (short)(exit.getConsumedItemQty() * (1 + companionsCount));
        }
        return exit.getConsumedItemQty();
    }
    
    private int getCompanionsCount() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final LocalAccountInformations localAccount = WakfuGameEntity.getInstance().getLocalAccount();
        final PartyModel party = localPlayer.getPartyComportment().getParty();
        if (party == null) {
            return 0;
        }
        final List<PartyMemberInterface> companions = party.getCompanions(localAccount.getAccountId());
        return companions.size();
    }
    
    @Override
    public String getName() {
        final LockContext context = WakfuGameEntity.getInstance().getLocalPlayer().getLockContext();
        final LockInstance lock = context.getLock(this.m_info.getLockId());
        if (lock != null) {
            final int dungeonId = lock.getLockedItem();
            final DungeonDefinition dungeonDefinition = DungeonManager.INSTANCE.getDungeon(dungeonId);
            if (dungeonDefinition != null) {
                final TextWidgetFormater sb = new TextWidgetFormater();
                sb.append(WakfuTranslator.getInstance().getString(137, dungeonId, new Object[0])).append(" (");
                sb.append(WakfuTranslator.getInstance().getString("required.level.custom", dungeonDefinition.getMinLevel())).append(')');
                return sb.finishAndToString();
            }
        }
        return WakfuTranslator.getInstance().getString(89, this.m_info.getId(), new Object[0]);
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public ChaosIEParameter getChaosIEParameter() {
        return this.m_info;
    }
    
    @Override
    public boolean checkSubscription() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
    }
    
    @Override
    public String toString() {
        return "Teleporter{m_info=" + this.m_info + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)Teleporter.class);
        FACTORY = new ObjectFactory<Teleporter>() {
            @Override
            public Teleporter makeObject() {
                return new Teleporter();
            }
        };
    }
    
    private static class MRUTeleporterActionComparator implements Comparator<MRUTeleporterAction>
    {
        @Override
        public int compare(final MRUTeleporterAction o1, final MRUTeleporterAction o2) {
            return (o1.getExit().getId() > o2.getExit().getId()) ? 1 : 0;
        }
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        static final MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            Teleporter travelMachine;
            try {
                travelMachine = (Teleporter)Factory.m_pool.borrowObject();
                travelMachine.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                Teleporter.m_logger.error((Object)"Erreur lors de l'extraction d'une DistributionMachine du pool", (Throwable)e);
                travelMachine = new Teleporter();
            }
            return travelMachine;
        }
        
        static {
            m_pool = new MonitoredPool(Teleporter.FACTORY);
        }
    }
}
