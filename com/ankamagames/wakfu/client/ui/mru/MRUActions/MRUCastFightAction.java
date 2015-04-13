package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.graphics.image.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.group.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;

public class MRUCastFightAction extends AbstractLawMRUAction
{
    private FightModel m_fightModelType;
    private static final int MAX_DISTANCE_TO_FIGHT_TARGET = 25;
    private static final int NOT_DISABLED = 0;
    private static final int DISABLE_PROTECTOR_ATTACKED_TOO_RECENTLY = 1;
    private static final int LOCAL_PLAYER_NOT_SUBSCRIBED = 2;
    private static final int TARGET_NOT_SUBSCRIBED = 3;
    private static final int LOCAL_PLAYER_NOT_SUBSCRIPTION_RIGHT = 4;
    private static final int TARGET_NOT_SUBSCRIPTION_RIGHT = 5;
    private static final int LOCAL_PLAYER_NO_NATION = 6;
    private static final int JOIN_FIGHT_RESULT = 7;
    private final BitSet m_fightStatus;
    private JoinFightResult m_joinResult;
    private boolean m_pvpMode;
    private int m_disableReason;
    
    public MRUCastFightAction(final FightModel fightTypeModel) {
        super();
        this.m_fightStatus = new BitSet(FightCreation.values().length);
        this.m_joinResult = JoinFightResult.OK;
        this.m_fightModelType = fightTypeModel;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUCastFightAction(this.m_fightModelType);
    }
    
    @Override
    public MRUActions tag() {
        switch (this.m_fightModelType) {
            case DUEL: {
                return MRUActions.CHARACTER_CAST_DUEL_FIGHT_ACTION;
            }
            default: {
                return MRUActions.CHARACTER_CAST_FIGHT_ACTION;
            }
        }
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUCastFightAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        final CharacterActor localPlayerActor = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
        if (Xulor.getInstance().isLoaded("recycleDialog")) {
            WakfuGameEntity.getInstance().removeFrame(UIRecycleFrame.getInstance());
        }
        final CharacterActor actor = ((CharacterInfo)this.m_source).getActor();
        final int xCharac = actor.getCurrentWorldX();
        final int yCharac = actor.getCurrentWorldY();
        final short zCharac = (short)actor.getAltitude();
        if (DistanceUtils.getIntersectionDistance(localPlayerActor, actor) <= 25) {
            switch (this.m_fightModelType) {
                case DUEL: {
                    if (!ChatHelper.controlAction(new Action(((CharacterInfo)this.m_source).getName(), 1))) {
                        return;
                    }
                    final FightInvitPlayerRequestMessage msg = new FightInvitPlayerRequestMessage();
                    msg.setPlayerId(((CharacterInfo)this.m_source).getId());
                    msg.setFightPositionRequested(xCharac, yCharac, zCharac);
                    msg.setLockedInitialy(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.AUTO_LOCK_FIGHTS_KEY));
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                    final TextWidgetFormater f = new TextWidgetFormater().addColor(ChatConstants.CHAT_FIGHT_INFORMATION_COLOR);
                    f.append(WakfuTranslator.getInstance().getString("fight.invitation", ((CharacterInfo)this.m_source).getName()));
                    final ChatMessage chatMessage = new ChatMessage(f.finishAndToString());
                    chatMessage.setPipeDestination(4);
                    ChatManager.getInstance().pushMessage(chatMessage);
                    break;
                }
                case PROTECTOR_ASSAULT: {
                    final int protectorId = ((NonPlayerCharacter)this.m_source).getProtector().getId();
                    WakfuGameEntity.getInstance().getLocalPlayer().askForProtectorStake(protectorId);
                    break;
                }
                default: {
                    WakfuGameEntity.getInstance().getLocalPlayer().askForFightCreation((CharacterInfo)this.m_source);
                    break;
                }
            }
        }
    }
    
    public void enableStatus(final FightCreation... statuses) {
        for (final FightCreation status : statuses) {
            this.m_fightStatus.set(status.ordinal(), true);
        }
    }
    
    private boolean hasStatus(final FightCreation... statuses) {
        for (final FightCreation status : statuses) {
            if (this.m_fightStatus.get(status.ordinal())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasAnyError() {
        return this.hasStatus(FightCreation.values());
    }
    
    private boolean isFightModelCompatible() {
        final FightModel fightModel = FightModelChooser.chooseFightModel((BasicCharacterInfo)this.m_source, WakfuGameEntity.getInstance().getLocalPlayer());
        if (fightModel == this.m_fightModelType) {
            return true;
        }
        switch (this.m_fightModelType) {
            case DUEL: {
                return true;
            }
            case PVP: {
                return fightModel != FightModel.RANKED_NATION_PVP;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public boolean isRunnable() {
        this.m_disableReason = 0;
        if (!(this.m_source instanceof CharacterInfo)) {
            return false;
        }
        if (!this.isFightModelCompatible()) {
            return false;
        }
        if (this.hasStatus(FightCreation.ALREADY_IN_FIGHT, FightCreation.DUEL_FORBIDDEN_FOR_PLAYER, FightCreation.AGGRESSION_FORBIDDEN_FOR_PLAYER)) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final BasicCharacterInfo character = (CharacterInfo)this.m_source;
        if (character.isDead()) {
            return false;
        }
        if (localPlayer.getVisitingDimentionalBag() != null) {
            return false;
        }
        if (localPlayer.isOnFight()) {
            return false;
        }
        if (localPlayer.isWaitingForResult()) {
            return false;
        }
        if (ClientTradeHelper.INSTANCE.isTradeRunning()) {
            return false;
        }
        if (localPlayer.getPropertyValue(WorldPropertyType.CANT_ATTACK) > 0) {
            return false;
        }
        final boolean targetIsNpc = character instanceof NonPlayerCharacter;
        if (targetIsNpc && localPlayer.isActiveProperty(WorldPropertyType.MONSTER_FIGHT_DISABLED)) {
            return false;
        }
        if (character.isActiveProperty(WorldPropertyType.CANT_BE_ATTACKED)) {
            return false;
        }
        if (character.isActiveProperty(WorldPropertyType.CANT_BE_TEMPORARY_ATTACKED)) {
            this.m_disableReason = 1;
        }
        if (this.m_pvpMode && !localPlayer.hasSubscriptionRight(SubscriptionRight.PVP_AGGRESSION)) {
            this.m_disableReason = 4;
        }
        if (localPlayer.getNationId() <= 0 && localPlayer.getTravellingNationId() > 0 && targetIsNpc) {
            this.m_disableReason = 6;
        }
        final boolean localPlayerSubscribed = WakfuAccountPermissionContext.SUBSCRIBER.hasPermission(localPlayer);
        final boolean localPlayerSubscribedZone = WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
        if ((this.m_fightModelType != FightModel.DUEL || !localPlayer.hasSubscriptionRight(SubscriptionRight.DUEL)) && ((!localPlayerSubscribed && this.m_fightModelType == FightModel.PVP) || !localPlayerSubscribedZone)) {
            this.m_disableReason = 2;
        }
        if (character instanceof PlayerCharacter) {
            final PlayerCharacter playerCharacter = (PlayerCharacter)character;
            if (this.m_fightModelType != FightModel.DUEL || !playerCharacter.hasSubscriptionRight(SubscriptionRight.DUEL)) {
                if (this.m_pvpMode && !playerCharacter.hasSubscriptionRight(SubscriptionRight.PVP_AGGRESSION)) {
                    this.m_disableReason = 5;
                }
                final boolean targetSubscribed = WakfuAccountPermissionContext.SUBSCRIBER.hasPermission(playerCharacter);
                final boolean targetSubscribedZone = WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(playerCharacter);
                if (!targetSubscribedZone || (!targetSubscribed && this.m_fightModelType != FightModel.DUEL)) {
                    this.m_disableReason = 3;
                }
            }
        }
        if (this.m_pvpMode && !(character instanceof NonPlayerCharacter)) {
            this.m_joinResult = NationPvpHelper.testPlayersCanDoRankedNationPvp(WakfuGameEntity.getInstance().getLocalPlayer(), character);
            if (this.m_joinResult != JoinFightResult.OK) {
                this.m_joinResult = NationPvpHelper.testPlayersCanDoRegularPvp(WakfuGameEntity.getInstance().getLocalPlayer(), character);
            }
        }
        if (character.isActiveProperty(WorldPropertyType.DEAD)) {
            return false;
        }
        final CitizenComportment localCitizen = localPlayer.getCitizenComportment();
        final int localNationId = localCitizen.getNationId();
        final Protector characterAsProtector = targetIsNpc ? ((Protector)character.getProtector()) : null;
        if (characterAsProtector != null && characterAsProtector.getCurrentNationId() == localNationId) {
            return false;
        }
        if (this.m_fightModelType != null) {
            switch (this.m_fightModelType) {
                case DUEL: {
                    if (!(character instanceof PlayerCharacter)) {
                        return false;
                    }
                    break;
                }
            }
        }
        if (this.m_disableReason == 0 && this.m_joinResult != JoinFightResult.OK) {
            this.m_disableReason = 7;
        }
        return true;
    }
    
    @Override
    public String getTranslatorKey() {
        switch (this.m_fightModelType) {
            case DUEL: {
                return "duelStart";
            }
            default: {
                return "fightStart";
            }
        }
    }
    
    @Override
    public String getLabel() {
        if (!(this.m_source instanceof CharacterInfo)) {
            return this.getTranslatorKey();
        }
        final Citizen characterInfo = (CharacterInfo)this.m_source;
        final String name = characterInfo.getName();
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(this.getTooltipColor().getRGBtoHex());
        if (this.hasStatus(FightCreation.NO_PATH)) {
            sb.append(WakfuTranslator.getInstance().getString("fight.error.nopathtomonster", name));
        }
        else if (this.hasStatus(FightCreation.TOO_FAR)) {
            sb.append(WakfuTranslator.getInstance().getString("fight.error.monstertoofar", name));
        }
        else if (this.hasStatus(FightCreation.IN_GROUP)) {
            sb.append(WakfuTranslator.getInstance().getString("fight.error.insideGroup"));
        }
        else if (this.hasStatus(FightCreation.ALREADY_IN_FIGHT)) {
            sb.append(WakfuTranslator.getInstance().getString("fight.error.character.inFight", name));
        }
        else if (this.hasStatus(FightCreation.PVP_FORBIDDEN_IN_WORLD)) {
            sb.append(WakfuTranslator.getInstance().getString("fight.error.pvpForbidden", name));
        }
        else if (this.hasStatus(FightCreation.DUEL_FORBIDDEN_IN_WORLD)) {
            sb.append(WakfuTranslator.getInstance().getString("fight.error.duelForbidden", name));
        }
        else if (this.hasStatus(FightCreation.FIGHTING_FORBIDDEN)) {
            sb.append(WakfuTranslator.getInstance().getString("systemNotification.instanceConfig.cannotFight"));
        }
        else if (this.hasStatus(FightCreation.NOT_ENEMY_PROTECTOR)) {
            sb.append(WakfuTranslator.getInstance().getString("fight.error.not.enemy.protector"));
        }
        else if (this.hasStatus(FightCreation.TARGET_LVL_TOO_LOW)) {
            sb.append(WakfuTranslator.getInstance().getString("fight.error.target.level.too.low"));
        }
        else if (this.hasStatus(FightCreation.CANT_BE_AGGRO)) {
            sb.append(WakfuTranslator.getInstance().getString("fight.error.cant.be.aggro"));
        }
        else if (this.hasStatus(FightCreation.MEMBER_BUSY)) {
            sb.append(WakfuTranslator.getInstance().getString("fight.error.memberBusy"));
        }
        else {
            switch (this.m_fightModelType) {
                case DUEL: {
                    sb.append(WakfuTranslator.getInstance().getString("fight.duel.with", name));
                    break;
                }
                case PVP:
                case RANKED_NATION_PVP: {
                    sb.append(WakfuTranslator.getInstance().getString("fight.pvp.with", name));
                    break;
                }
                default: {
                    sb.append(WakfuTranslator.getInstance().getString("fight.with", name));
                    break;
                }
            }
        }
        sb._b();
        if (this.m_disableReason != 0) {
            switch (this.m_disableReason) {
                case 1: {
                    return sb.newLine().addColor(MRUCastFightAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("fight.error.protector.attacked.too.recenlty", name)).finishAndToString();
                }
                case 2: {
                    return sb.newLine().addColor(MRUCastFightAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed")).finishAndToString();
                }
                case 4: {
                    return sb.newLine().addColor(MRUCastFightAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNotSubscriptionRight")).finishAndToString();
                }
                case 5: {
                    return sb.newLine().addColor(MRUCastFightAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.targetNotSubscriptionRight")).finishAndToString();
                }
                case 6: {
                    return sb.newLine().addColor(MRUCastFightAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNoNation")).finishAndToString();
                }
                case 3: {
                    return sb.newLine().addColor(MRUCastFightAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.targetNotSubscribed")).finishAndToString();
                }
                case 7: {
                    return sb.newLine().addColor(MRUCastFightAction.NOK_TOOLTIP_COLOR).append(ErrorsMessageTranslator.getInstance().getMessageByErrorId(this.m_joinResult.getErrorCode(), new Object[0])).finishAndToString();
                }
            }
        }
        return sb.finishAndToString();
    }
    
    @Nullable
    @Override
    public String getComplementaryTooltip() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        if (this.isEnabled()) {
            switch (this.m_fightModelType) {
                case PVP: {
                    sb.openText().addColor(Color.RED).append(WakfuTranslator.getInstance().getString("pvp.cantGainPointsWithFight")).closeText();
                    break;
                }
                case RANKED_NATION_PVP: {
                    sb.openText().addColor(Color.GREEN).append(WakfuTranslator.getInstance().getString("pvp.canGainPointsWithFight")).closeText();
                    break;
                }
            }
        }
        final String complementaryTooltip = super.getComplementaryTooltip();
        if (complementaryTooltip != null) {
            if (!sb.toString().isEmpty()) {
                sb.newLine();
            }
            sb.append(complementaryTooltip);
        }
        final String text = sb.finishAndToString();
        return text.isEmpty() ? null : text;
    }
    
    public Color getTooltipColor() {
        if (this.hasStatus(FightCreation.NO_PATH, FightCreation.TOO_FAR, FightCreation.IN_GROUP, FightCreation.ALREADY_IN_FIGHT, FightCreation.PVP_FORBIDDEN_IN_WORLD, FightCreation.DUEL_FORBIDDEN_IN_WORLD, FightCreation.MEMBER_BUSY, FightCreation.FIGHTING_FORBIDDEN, FightCreation.NOT_ENEMY_PROTECTOR, FightCreation.CANT_BE_AGGRO, FightCreation.TARGET_LVL_TOO_LOW)) {
            return Color.RED;
        }
        return Color.WHITE;
    }
    
    @Override
    protected int getGFXId() {
        switch (this.m_fightModelType) {
            case DUEL: {
                return MRUGfxConstants.WHITE_SWORD.m_id;
            }
            default: {
                return MRUGfxConstants.SWORD.m_id;
            }
        }
    }
    
    @Override
    public boolean isEnabled() {
        return super.isEnabled() && !this.hasAnyError() && this.m_disableReason == 0;
    }
    
    @Override
    public void initFromSource(final Object source) {
        super.initFromSource(source);
        final CharacterInfo characterInfo = (CharacterInfo)this.m_source;
        if (characterInfo.isDead()) {
            this.enableStatus(FightCreation.FIGHTING_FORBIDDEN);
            return;
        }
        if (characterInfo.isOnFight()) {
            this.enableStatus(FightCreation.ALREADY_IN_FIGHT);
            return;
        }
        final WorldInfoManager.WorldInfo targetWorldInfo = WorldInfoManager.getInstance().getInfo(characterInfo.getInstanceId());
        WakfuClientInstance.getInstance();
        final LocalPlayerCharacter localPlayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
        Point3 position = localPlayer.getPosition();
        final boolean localPlayerOnFightoSterile = TopologyMapManager.isFightoSterileOrNotWalkable(position.getX(), position.getY(), position.getZ());
        position = characterInfo.getPosition();
        final boolean sourceOnFightoSterile = TopologyMapManager.isFightoSterileOrNotWalkable(position.getX(), position.getY(), position.getZ());
        boolean allNeighbourCellFightoSterile = true;
        if (sourceOnFightoSterile) {
            allNeighbourCellFightoSterile &= fightoSterileOrNotWalkable(position, position.getX() + 1, position.getY());
            allNeighbourCellFightoSterile &= fightoSterileOrNotWalkable(position, position.getX() - 1, position.getY());
            allNeighbourCellFightoSterile &= fightoSterileOrNotWalkable(position, position.getX(), position.getY() + 1);
            allNeighbourCellFightoSterile &= fightoSterileOrNotWalkable(position, position.getX(), position.getY() - 1);
        }
        final WorldInfoManager.WorldInfo lpcWorldInfo = WorldInfoManager.getInstance().getInfo(localPlayer.getInstanceId());
        if (!lpcWorldInfo.isFightAllowed() || localPlayerOnFightoSterile || (sourceOnFightoSterile && allNeighbourCellFightoSterile)) {
            this.enableStatus(FightCreation.FIGHTING_FORBIDDEN);
        }
        if (characterInfo instanceof NonPlayerCharacter) {
            if (targetWorldInfo != null && !targetWorldInfo.m_pveAllowed) {
                this.enableStatus(FightCreation.FIGHTING_FORBIDDEN);
            }
            final NonPlayerCharacter npc = (NonPlayerCharacter)characterInfo;
            final NPCGroupInformation groupInfo = NPCGroupInformationManager.getInstance().getGroupInformation(npc.getGroupId());
            if (groupInfo != null && groupInfo.hasKnownBusyMember()) {
                this.enableStatus(FightCreation.MEMBER_BUSY);
            }
            final Protector protector = npc.getProtector();
            if (protector != null && protector.getCurrentNationId() != 0 && localPlayer.getCitizenComportment().getNation().getDiplomacyManager().getAlignment(protector.getCurrentNationId()) != NationAlignement.ENEMY) {
                this.enableStatus(FightCreation.NOT_ENEMY_PROTECTOR);
            }
        }
        if (characterInfo instanceof PlayerCharacter) {
            if (this.m_fightModelType != FightModel.DUEL) {
                final WorldInfoManager.WorldInfo.AmbienceZone zone = WakfuGlobalZoneManager.getInstance().getCurrentAmbienceZone();
                if (zone != null) {
                    if (!zone.isPvpAllowed()) {
                        this.enableStatus(FightCreation.PVP_FORBIDDEN_IN_WORLD);
                    }
                }
                else if (!WorldInfoManager.getInstance().getInfo(WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId()).isPvpAllowed()) {
                    this.enableStatus(FightCreation.PVP_FORBIDDEN_IN_WORLD);
                }
                if (localPlayer.isActiveProperty(WorldPropertyType.AGRESSION_DISABLED) || characterInfo.isActiveProperty(WorldPropertyType.AGRESSION_DISABLED)) {
                    this.enableStatus(FightCreation.AGGRESSION_FORBIDDEN_FOR_PLAYER);
                }
                if (targetWorldInfo != null && this.m_pvpMode && !WakfuGlobalZoneManager.getInstance().getCurrentAmbienceZone().isPvpAllowed()) {
                    this.enableStatus(FightCreation.PVP_FORBIDDEN_IN_WORLD);
                }
                if (characterInfo.isActiveProperty(WorldPropertyType.CANT_BE_AGGRO)) {
                    this.enableStatus(FightCreation.CANT_BE_AGGRO);
                }
                else {
                    this.m_pvpMode = true;
                }
                return;
            }
            if (targetWorldInfo != null && !targetWorldInfo.m_duelAllowed) {
                this.enableStatus(FightCreation.DUEL_FORBIDDEN_IN_WORLD);
            }
            if (localPlayer.isActiveProperty(WorldPropertyType.DUEL_DISABLED) || characterInfo.isActiveProperty(WorldPropertyType.DUEL_DISABLED)) {
                this.enableStatus(FightCreation.DUEL_FORBIDDEN_FOR_PLAYER);
            }
        }
        if (!characterInfo.isActiveProperty(WorldPropertyType.DONT_NEED_PATH_TO_FIGHT)) {
            final CharacterActor localActor = localPlayer.getActor();
            final PathFindResult result = localActor.getPathResult(characterInfo.getPosition(), true, true);
            if (result == PathFinder.PATH_NOT_FOUND_RESULT) {
                this.enableStatus(FightCreation.NO_PATH);
            }
            else if (result.getPathLength() > 25) {
                this.enableStatus(FightCreation.TOO_FAR);
            }
        }
    }
    
    private static boolean fightoSterileOrNotWalkable(final Point3 position, final int x, final int y) {
        return TopologyMapManager.isFightoSterileOrNotWalkable(x, y, TopologyMapManager.getNearestWalkableZ(x, y, position.getZ()));
    }
    
    @Override
    public List<NationLaw> getTriggeredLaws() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ArrayList<BasicCharacterInfo> opponents = new ArrayList<BasicCharacterInfo>();
        final BasicCharacterInfo character = (BasicCharacterInfo)this.m_source;
        if (this.m_source instanceof NonPlayerCharacter) {
            final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_source;
            final NPCGroupInformation infos = NPCGroupInformationManager.getInstance().getGroupInformation(npc.getGroupId());
            for (final NPCGroupInformation.NPCInformation npcInfo : infos.getMembersInformations()) {
                final CharacterInfo ci = CharacterInfoManager.getInstance().getCharacter(npcInfo.getId());
                if (ci != null) {
                    opponents.add(ci);
                }
            }
        }
        else {
            opponents.add(character);
        }
        final Protector protector = ProtectorView.getInstance().getProtector();
        final NationLawEvent fightLawEvent = new FightLawEvent(localPlayer, this.m_fightModelType, localPlayer, character, (protector == null) ? null : protector.getEcosystemHandler(), opponents);
        final List<NationLaw> triggeredLaws = fightLawEvent.getTriggeringLaws();
        if (this.m_fightModelType == FightModel.DUEL) {
            final NationLawEvent proposeDuelLawEvent = new ProposeDuelLawEvent(localPlayer);
            triggeredLaws.addAll(proposeDuelLawEvent.getTriggeringLaws());
        }
        return triggeredLaws;
    }
    
    @Override
    public List<NationLaw> getProbablyTriggeredLaws() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final List<BasicCharacterInfo> opponents = new ArrayList<BasicCharacterInfo>();
        final BasicCharacterInfo character = (BasicCharacterInfo)this.m_source;
        final ProtectorWishLawEvent protectorWishLawEvent = new ProtectorWishLawEvent(localPlayer);
        if (this.m_source instanceof NonPlayerCharacter) {
            final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_source;
            if (AbstractLawMRUAction.getCurrentNationAlignment() == NationAlignement.ALLIED) {
                final NPCGroupInformation infos = NPCGroupInformationManager.getInstance().getGroupInformation(npc.getGroupId());
                for (final NPCGroupInformation.NPCInformation npcInfo : infos.getMembersInformations()) {
                    final CharacterInfo ci = CharacterInfoManager.getInstance().getCharacter(npcInfo.getId());
                    if (ci != null) {
                        opponents.add(ci);
                        if (protectorWishLawEvent.getActionType() == ProtectorWishLawEvent.ActionType.FOLLOWING) {
                            continue;
                        }
                        final WakfuEcosystemFamilyInfo wakfuEcosystemFamilyInfo = WakfuMonsterZoneManager.getInstance().getMonsterFamilyInfo(npc.getBreed().getFamilyId());
                        if (wakfuEcosystemFamilyInfo == null || !wakfuEcosystemFamilyInfo.hasProtectorInterval()) {
                            continue;
                        }
                        if (wakfuEcosystemFamilyInfo.isInProtectorInterval()) {
                            continue;
                        }
                        final int diffFromMax = wakfuEcosystemFamilyInfo.getProtectorIntervalDiffFromMax();
                        if (diffFromMax == 0) {
                            continue;
                        }
                        if (diffFromMax > 0) {
                            protectorWishLawEvent.setAction(ProtectorWishLawEvent.ActionType.FOLLOWING);
                        }
                        else {
                            protectorWishLawEvent.setAction(ProtectorWishLawEvent.ActionType.AGAINST);
                        }
                    }
                }
            }
        }
        else {
            opponents.add(character);
        }
        final NationLawEvent loseFightLawEvent = new LoseFightLawEvent(localPlayer, this.m_fightModelType, localPlayer);
        final NationLawEvent wonFightLawEvent = new WonFightLawEvent(localPlayer, this.m_fightModelType);
        final NationLawEvent killLawEvent = new KillLawEvent(localPlayer, character, (byte)0, (byte)1, this.m_fightModelType);
        final List<NationLaw> triggeredLaws = new ArrayList<NationLaw>();
        triggeredLaws.addAll(loseFightLawEvent.getTriggeringLaws());
        triggeredLaws.addAll(wonFightLawEvent.getTriggeringLaws());
        triggeredLaws.addAll(killLawEvent.getTriggeringLaws());
        triggeredLaws.addAll(protectorWishLawEvent.getTriggeringLaws());
        return triggeredLaws;
    }
    
    public enum FightCreation
    {
        TOO_FAR, 
        NO_PATH, 
        IN_GROUP, 
        ALREADY_IN_FIGHT, 
        PVP_FORBIDDEN_IN_WORLD, 
        DUEL_FORBIDDEN_IN_WORLD, 
        FIGHTING_FORBIDDEN, 
        MEMBER_BUSY, 
        NOT_ENEMY_PROTECTOR, 
        TARGET_LVL_TOO_LOW, 
        CANT_BE_AGGRO, 
        DUEL_FORBIDDEN_FOR_PLAYER, 
        AGGRESSION_FORBIDDEN_FOR_PLAYER, 
        OK;
    }
}
