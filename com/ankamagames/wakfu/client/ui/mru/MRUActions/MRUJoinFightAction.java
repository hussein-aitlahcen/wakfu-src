package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.core.game.fight.join.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.text.*;
import com.google.common.collect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.graphics.image.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUJoinFightAction extends AbstractLawMRUAction
{
    private MRUCastFightAction.FightCreation m_fightStatus;
    private FightInfo m_fight;
    private CharacterInfo m_target;
    private static final int NOT_DISABLED = 0;
    private static final int LOCAL_PLAYER_NOT_SUBSCRIBED = 1;
    private static final int TARGET_NOT_SUBSCRIBED = 2;
    private static final int LOCAL_PLAYER_NO_NATION = 3;
    private static final int JOIN_FIGHT_RESULT = 4;
    private int m_disableReason;
    private JoinFightResult m_joinResult;
    private static final EnumMap<JoinFightResult, MRUCastFightAction.FightCreation> m_runnableStates;
    
    public MRUJoinFightAction() {
        super();
        this.m_fightStatus = null;
        this.m_joinResult = JoinFightResult.OK;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUJoinFightAction();
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.CHARACTER_JOIN_FIGHT_ACTION;
    }
    
    @Override
    public void run() {
        if (NationPvpHelper.playerNeedsPvpTagToJoin((BasicFightInfo<BasicCharacterInfo>)this.m_fight, this.m_fight.getTeamId(this.m_target.getId())) && WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getPvpState() == NationPvpState.PVP_OFF) {
            final String msgText = WakfuTranslator.getInstance().getString("pvp.activationWarning");
            final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        MRUJoinFightAction.this.doRun();
                    }
                }
            });
        }
        else {
            this.doRun();
        }
    }
    
    private void doRun() {
        final JoinFightProcedure joinFightProcedure = JoinFight.joinAlly(this.m_fight, this.m_target);
        final JoinFightResult joinResult = joinFightProcedure.tryJoinFight();
        if (joinFightProcedure.isJoinProtectorAttack()) {
            this.sendStakeRequest(this.m_fight);
        }
        if (joinResult != JoinFightResult.OK) {
            ErrorsMessageTranslator.getInstance().pushMessage(joinResult.getErrorCode(), 3, new Object[0]);
        }
    }
    
    private void sendStakeRequest(final FightInfo fight) {
        final Collection<CharacterInfo> fighters = fight.getFighters();
        for (final CharacterInfo info : fighters) {
            if (info.getType() == 1) {
                if (!(info instanceof NonPlayerCharacter)) {
                    continue;
                }
                final NonPlayerCharacter npc = (NonPlayerCharacter)info;
                final Protector protector = npc.getProtector();
                if (protector == null) {
                    continue;
                }
                final Message getStakeMessage = new GetProtectorFightStakeRequestMessage(protector.getId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(getStakeMessage);
            }
        }
    }
    
    @Override
    public String getLabel() {
        if (!(this.m_source instanceof CharacterInfo)) {
            return this.getTranslatorKey();
        }
        switch (this.m_fightStatus) {
            case NO_PATH: {
                final String color = MRUJoinFightAction.NOK_TOOLTIP_COLOR;
            }
            case TOO_FAR: {
                final String color = MRUJoinFightAction.NOK_TOOLTIP_COLOR;
                break;
            }
        }
        final String color = MRUJoinFightAction.DEFAULT_TOOLTIP_COLOR;
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(color);
        final String name = ((BasicCharacterInfo)this.m_source).getName();
        switch (this.m_fightStatus) {
            case NO_PATH: {
                sb.append(WakfuTranslator.getInstance().getString("fight.error.nopathtomonster", name));
            }
            case TOO_FAR: {
                sb.append(WakfuTranslator.getInstance().getString("fight.error.monstertoofar", name));
                break;
            }
        }
        sb.append(WakfuTranslator.getInstance().getString("fight.join", name));
        if (this.m_fight.getModel().isPvp()) {
            sb.append(WakfuTranslator.getInstance().getString("mru.joinFight.pvp"));
        }
        sb._b();
        if (this.m_disableReason != 0) {
            switch (this.m_disableReason) {
                case 1: {
                    return sb.newLine().addColor(MRUJoinFightAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed")).finishAndToString();
                }
                case 2: {
                    return sb.newLine().addColor(MRUJoinFightAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.targetNotSubscribed")).finishAndToString();
                }
                case 3: {
                    return sb.newLine().addColor(MRUJoinFightAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNoNation")).finishAndToString();
                }
                case 4: {
                    if (ImmutableList.of((Object)JoinFightResult.PVP_MUST_BE_GUARD, (Object)JoinFightResult.PVP_MUST_HAVE_MIN_LEVEL, (Object)JoinFightResult.PVP_NOT_SAME_NATION_AS_GUILD).contains((Object)this.m_joinResult)) {
                        return sb.newLine().addColor(MRUJoinFightAction.NOK_TOOLTIP_COLOR).append(ErrorsMessageTranslator.getInstance().getMessageByErrorId(this.m_joinResult.getErrorCode(), new Object[0])).finishAndToString();
                    }
                    return sb.newLine().addColor(MRUJoinFightAction.NOK_TOOLTIP_COLOR).append(ErrorsMessageTranslator.getInstance().getMessageByErrorId(this.m_joinResult.getErrorCode(), new Object[0])).finishAndToString();
                }
            }
        }
        return sb.finishAndToString();
    }
    
    @Nullable
    @Override
    public String getComplementaryTooltip() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        if ((this.isEnabled() && this.m_fight.getModel() == FightModel.RANKED_NATION_PVP) || this.m_fight.getModel() == FightModel.PVP) {
            final byte teamId = this.m_fight.getTeamId(this.m_target.getId());
            final byte initiatingTeamId = this.m_fight.getInitiatingTeamId();
            final boolean attacking = initiatingTeamId == teamId;
            final boolean pvpInactive = !WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getPvpState().isActive();
            if (pvpInactive || (attacking && this.m_fight.getModel() == FightModel.PVP)) {
                sb.openText().addColor(Color.RED).append(WakfuTranslator.getInstance().getString("pvp.cantGainPointsWithFight")).closeText();
            }
            else {
                sb.openText().addColor(Color.GREEN).append(WakfuTranslator.getInstance().getString("pvp.canGainPointsWithFight")).closeText();
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
    
    @Override
    public boolean isEnabled() {
        return this.m_disableReason == 0 && super.isEnabled() && this.m_fightStatus == MRUCastFightAction.FightCreation.OK;
    }
    
    @Override
    public boolean isRunnable() {
        if (this.m_fightStatus == null || this.m_fightStatus == MRUCastFightAction.FightCreation.ALREADY_IN_FIGHT) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterInfo character = (CharacterInfo)this.m_source;
        final boolean localPlayerSubscribed = WakfuAccountPermissionContext.SUBSCRIBER.hasPermission(localPlayer);
        final boolean localPlayerSubscribedZone = WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
        if ((!localPlayerSubscribed && this.m_fight.getModel().isPvp()) || !localPlayerSubscribedZone) {
            this.m_disableReason = 1;
        }
        if (localPlayer.isActiveProperty(WorldPropertyType.JOIN_IN_FIGHT_MRU_DISABLED)) {
            return false;
        }
        if (character instanceof PlayerCharacter) {
            final PlayerCharacter playerCharacter = (PlayerCharacter)character;
            final ExternalFightInfo fightInfo = playerCharacter.getCurrentExternalFightInfo();
            if (fightInfo != null && localPlayer.getNationId() <= 0 && localPlayer.getTravellingNationId() > 0 && fightInfo.getModel() == FightModel.PVE) {
                this.m_disableReason = 3;
            }
            final boolean targetSubscribedZone = WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(playerCharacter);
            if (!targetSubscribedZone) {
                this.m_disableReason = 2;
            }
            this.m_joinResult = NationPvpHelper.testPlayerCanJoinPvpFightOf((BasicFightInfo<LocalPlayerCharacter>)fightInfo, localPlayer, character.getTeamId());
            if (character.isActiveProperty(WorldPropertyType.JOIN_IN_FIGHT_MRU_DISABLED)) {
                return false;
            }
        }
        if (this.m_disableReason == 0 && this.m_joinResult != JoinFightResult.OK) {
            this.m_disableReason = 4;
        }
        return true;
    }
    
    @Override
    public String getTranslatorKey() {
        return "fightJoin";
    }
    
    @Override
    public void initFromSource(final Object source) {
        super.initFromSource(source);
        if (!(this.m_source instanceof CharacterInfo)) {
            return;
        }
        this.m_target = (CharacterInfo)this.m_source;
        this.m_fight = this.m_target.getCurrentExternalFightInfo();
        if (this.m_fight == null) {
            return;
        }
        final JoinFightResult joinResult = JoinFight.joinAlly(this.m_fight, this.m_target).canJoinFight();
        this.m_fightStatus = MRUJoinFightAction.m_runnableStates.get(joinResult);
    }
    
    @Override
    public List<NationLaw> getTriggeredLaws() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ArrayList<BasicCharacterInfo> opponents = new ArrayList<BasicCharacterInfo>();
        final ArrayList<BasicCharacterInfo> teamMates = new ArrayList<BasicCharacterInfo>();
        final CharacterInfo character = (CharacterInfo)this.m_source;
        final ArrayList<NationLaw> triggeredLaws = new ArrayList<NationLaw>();
        final FightInfo fightInfo = FightManager.getInstance().getFightById(character.getCurrentFightId());
        final FightModel fightModel = fightInfo.getModel();
        for (final CharacterInfo ci : fightInfo.getFighters()) {
            if (ci.getTeamId() == character.getTeamId()) {
                teamMates.add(ci);
            }
            else {
                opponents.add(ci);
            }
        }
        final Protector protector = ProtectorView.getInstance().getProtector();
        final FightLawEvent fightLawEvent = new FightLawEvent(localPlayer, fightModel, localPlayer, opponents.get(0), (protector == null) ? null : protector.getEcosystemHandler(), opponents);
        final JoinFightLawEvent joinFightLawEvent = new JoinFightLawEvent(character, fightModel, ((ExternalFightInfo)fightInfo).getAttackerCreator(), ((ExternalFightInfo)fightInfo).getDefenderCreator(), teamMates);
        triggeredLaws.addAll(fightLawEvent.getTriggeringLaws());
        triggeredLaws.addAll(joinFightLawEvent.getTriggeringLaws());
        return triggeredLaws;
    }
    
    @Override
    public List<NationLaw> getProbablyTriggeredLaws() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterInfo character = (CharacterInfo)this.m_source;
        final ProtectorWishLawEvent protectorWishLawEvent = new ProtectorWishLawEvent(localPlayer);
        final ArrayList<NationLaw> triggeredLaws = new ArrayList<NationLaw>();
        final FightInfo fightInfo = FightManager.getInstance().getFightById(character.getCurrentFightId());
        final FightModel fightModel = fightInfo.getModel();
        if (AbstractLawMRUAction.getCurrentNationAlignment() == NationAlignement.ALLIED) {
            for (final CharacterInfo ci : fightInfo.getFighters()) {
                final KillLawEvent killLawEvent = new KillLawEvent(localPlayer, ci, (byte)0, (byte)((ci.getTeamId() != character.getTeamId()) ? 1 : 0), fightModel);
                triggeredLaws.addAll(killLawEvent.getTriggeringLaws());
                if (!(ci instanceof NonPlayerCharacter)) {
                    continue;
                }
                final NonPlayerCharacter npc = (NonPlayerCharacter)ci;
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
                protectorWishLawEvent.setAction((diffFromMax > 0) ? ProtectorWishLawEvent.ActionType.FOLLOWING : ProtectorWishLawEvent.ActionType.AGAINST);
            }
        }
        final LoseFightLawEvent loseFightLawEvent = new LoseFightLawEvent(localPlayer, fightModel, ((ExternalFightInfo)fightInfo).getAttackerCreator());
        final WonFightLawEvent wonFightLawEvent = new WonFightLawEvent(localPlayer, fightModel);
        triggeredLaws.addAll(loseFightLawEvent.getTriggeringLaws());
        triggeredLaws.addAll(wonFightLawEvent.getTriggeringLaws());
        triggeredLaws.addAll(protectorWishLawEvent.getTriggeringLaws());
        return triggeredLaws;
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.JOIN_FIGHT.m_id;
    }
    
    static {
        (m_runnableStates = new EnumMap<JoinFightResult, MRUCastFightAction.FightCreation>(JoinFightResult.class)).put(JoinFightResult.PATHFINDER_ERROR, MRUCastFightAction.FightCreation.TOO_FAR);
        MRUJoinFightAction.m_runnableStates.put(JoinFightResult.JOINER_ALREADY_IN_FIGHT, MRUCastFightAction.FightCreation.ALREADY_IN_FIGHT);
        MRUJoinFightAction.m_runnableStates.put(JoinFightResult.FIGHT_NOT_IN_PLACEMENT_PHASE, MRUCastFightAction.FightCreation.ALREADY_IN_FIGHT);
        MRUJoinFightAction.m_runnableStates.put(JoinFightResult.OK, MRUCastFightAction.FightCreation.OK);
    }
}
