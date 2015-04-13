package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.client.ui.component.*;
import gnu.trove.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.game.nation.impl.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.wakfu.common.game.pvp.*;

final class ClientCitizenComportementView extends ImmutableFieldProvider implements ClientCitizenComportementObserver
{
    public static final String NATION_ID = "nationId";
    public static final String HAS_VOTED_FIELD = "hasVoted";
    public static final String IS_CANDIDATE_FIELD = "isCandidate";
    public static final String CANDIDATE_INFO_FIELD = "candidateInfo";
    public static final String NATION_RANK_DESCRIPTION_FIELD = "nationRankDescription";
    public static final String NATIVE_NATION_CITIZEN_SCORE = "nativeNationCitizenScore";
    public static final String IS_PASSPORT_ACTIVE = "isPassportActive";
    public static final String PASSPORT_STAMP_ICON_URL = "passportStampIconUrl";
    public static final String NATION_COLOR = "nationColor";
    public static final String NATION_NAME = "nationName";
    public static final String CAN_APPLY_FOR_GOVERNOR_CONCERNING_CRIMES_FIELD = "canApplyForGovernorConcerningCrimes";
    public static final String CAN_VOTE_CONCERNING_CRIMES_FIELD = "canVoteConcerningCrimes";
    public static final String CANDIDATE_REGISTRATION_WARNING_INFO = "candidateRegistrationWarningInfo";
    public static final String PVP_STATE_STYLE = "pvpStateStyle";
    public static final String PVP_STATE_PERCENT = "pvpStatePercent";
    public static final String PVP_STATE_TITLE = "pvpStateTitle";
    public static final String PVP_STATE_DESCRIPTION = "pvpStateDescription";
    public static final String CAN_DO_PVP_DESCRIPTION = "canDoPvpDescription";
    public static final String CAN_TOGGLE_PVP = "canTogglePvp";
    public static final String PVP_MONEY = "pvpMoney";
    private ClientCitizenComportment m_comportement;
    private CandidateInfoFieldProvider m_candidateInfoFieldProvider;
    private final TIntObjectHashMap<CitizenScoreFieldProvider> m_citizenScoresFieldProviders;
    private final TIntObjectHashMap<Float[]> m_ranksColors;
    
    public ClientCitizenComportementView(final ClientCitizenComportment comportement) {
        super();
        this.m_citizenScoresFieldProviders = new TIntObjectHashMap<CitizenScoreFieldProvider>();
        this.m_ranksColors = new TIntObjectHashMap<Float[]>();
        this.m_comportement = comportement;
        this.initializeRanksColors();
    }
    
    private void initializeRanksColors() {
    }
    
    public void setComportement(final ClientCitizenComportment comportement) {
        this.m_comportement = comportement;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (this.m_comportement == null) {
            return null;
        }
        if (fieldName.equals("hasVoted")) {
            return this.m_comportement.hasVoted();
        }
        if (fieldName.equals("nationId")) {
            return this.m_comportement.getNationId();
        }
        if (fieldName.equals("isCandidate")) {
            return this.m_comportement.isCandidate();
        }
        if (fieldName.equals("candidateInfo")) {
            return this.m_candidateInfoFieldProvider;
        }
        if (fieldName.equals("nativeNationCitizenScore")) {
            return this.m_citizenScoresFieldProviders.get(this.m_comportement.getNationId());
        }
        if (fieldName.equals("nationRankDescription")) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.openText().addColor(this.getColor().getRGBtoHex());
            final NationRank rank = this.m_comportement.getRank();
            if (rank != null) {
                sb.append(WakfuTranslator.getInstance().getString(57, (int)rank.getId(), new Object[0]));
            }
            else {
                sb.append(WakfuTranslator.getInstance().getString("nation.citizen.noRank"));
            }
            if (this.m_comportement.getJobs().size() > 0) {
                for (final NationJob job : this.m_comportement.getJobs()) {
                    sb.newLine();
                    sb.append(WakfuTranslator.getInstance().getString(job.name()));
                }
            }
            sb.closeText();
            return sb.finishAndToString();
        }
        if (fieldName.equals("nationColor")) {
            return this.getColor();
        }
        if (fieldName.equals("isPassportActive")) {
            return this.m_comportement.isPasseportActive();
        }
        if (fieldName.equals("passportStampIconUrl")) {
            return WakfuConfiguration.getInstance().getIconUrl("passportStampIconsPath", "defaultIconPath", this.m_comportement.getNationId());
        }
        if (fieldName.equals("nationName")) {
            return WakfuTranslator.getInstance().getString(39, this.m_comportement.getNationId(), new Object[0]);
        }
        if (fieldName.equals("canApplyForGovernorConcerningCrimes")) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            return localPlayer.hasSubscriptionRight(SubscriptionRight.ACCESS_TO_ELECTION) && !this.m_comportement.isCandidate() && CitizenAuthorizationRules.getInstance().hasRightToStandForElectionConcerningCitizenRank(this.m_comportement);
        }
        if (fieldName.equals("canVoteConcerningCrimes")) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            return localPlayer.hasSubscriptionRight(SubscriptionRight.ACCESS_TO_ELECTION) && CitizenAuthorizationRules.getInstance().hasRightOfVoteConcerningCitizenRank(this.m_comportement);
        }
        if (fieldName.equals("candidateRegistrationWarningInfo")) {
            final TextWidgetFormater textWidgetFormater = new TextWidgetFormater();
            final LocalPlayerCharacter localPlayer2 = WakfuGameEntity.getInstance().getLocalPlayer();
            boolean first = true;
            if (!localPlayer2.hasSubscriptionRight(SubscriptionRight.ACCESS_TO_ELECTION)) {
                textWidgetFormater.append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed"));
            }
            else {
                if (!CitizenAuthorizationRules.getInstance().hasRightToStandForElectionConcerningCitizenRank(this.m_comportement)) {
                    if (!first) {
                        textWidgetFormater.newLine();
                    }
                    textWidgetFormater.append(WakfuTranslator.getInstance().getString("nation.rank.unauthorized.for.registration"));
                    first = false;
                }
                if (localPlayer2.getKamasCount() < NationDisplayer.getInstance().getCandidateRegistrationFee()) {
                    if (!first) {
                        textWidgetFormater.newLine();
                    }
                    textWidgetFormater.append(WakfuTranslator.getInstance().getString("nation.not.enough.kamas.for.registration", this.m_comportement.getNation().getCandidateRegistrationFee()));
                    first = false;
                }
                if (localPlayer2.getCitizenComportment().isCandidate()) {
                    if (!first) {
                        textWidgetFormater.newLine();
                    }
                    textWidgetFormater.append(WakfuTranslator.getInstance().getString("nation.vote.candidateRegistration.alreadyCandidate"));
                    first = false;
                }
            }
            return textWidgetFormater.finishAndToString();
        }
        if (fieldName.equals("canDoPvpDescription")) {
            final JoinFightResult canDoPvpResult = NationPvpHelper.testPlayerCanDoRankedNationPvp((PvpUser)this.m_comportement.getCitizen());
            if (canDoPvpResult != JoinFightResult.OK) {
                return new TextWidgetFormater().openText().addColor(Color.RED).append(WakfuTranslator.getInstance().getString("error.pvp.requirementsNotMet", (short)38)).finishAndToString();
            }
            return null;
        }
        else {
            if (fieldName.equals("canTogglePvp")) {
                return NationPvpHelper.canTogglePvp((PvpUser)this.m_comportement.getCitizen(), !this.m_comportement.getPvpState().isActive());
            }
            if (fieldName.equals("pvpStateStyle")) {
                return "pvpState" + this.m_comportement.getPvpState().getId();
            }
            if (fieldName.equals("pvpStateTitle")) {
                return WakfuTranslator.getInstance().getString("pvp.stateTitle." + this.m_comportement.getPvpState().getId());
            }
            if (fieldName.equals("pvpStateDescription")) {
                return this.getPvpStateDescription();
            }
            if (fieldName.equals("pvpStatePercent")) {
                switch (this.m_comportement.getPvpState()) {
                    case PVP_STARTING: {
                        final GameInterval gameInterval = this.m_comportement.getPvpDate().timeTo(WakfuGameCalendar.getInstance().getDate());
                        return gameInterval.toSeconds() / NationPvpConstants.PVP_START_DURATION.toSeconds();
                    }
                    case PVP_ON_LOCKED: {
                        final GameInterval leaveDuration = new GameInterval(NationPvpConstants.PVP_LEAVE_DURATION);
                        leaveDuration.substract(NationPvpConstants.PVP_START_DURATION);
                        final GameInterval timeSinceActivation = this.m_comportement.getPvpDate().timeTo(WakfuGameCalendar.getInstance().getDate());
                        timeSinceActivation.substract(NationPvpConstants.PVP_START_DURATION);
                        return leaveDuration.isPositive() ? (timeSinceActivation.toSeconds() / leaveDuration.toSeconds()) : 0.0f;
                    }
                    default: {
                        return 0.0f;
                    }
                }
            }
            else {
                if ("pvpMoney".equals(fieldName)) {
                    return this.m_comportement.getPvpMoneyAmount();
                }
                return null;
            }
        }
    }
    
    private String getPvpStateDescription() {
        switch (this.m_comportement.getPvpState()) {
            case PVP_STARTING: {
                final GameInterval remainingTime = new GameInterval(NationPvpConstants.PVP_START_DURATION);
                remainingTime.substract(this.m_comportement.getPvpDate().timeTo(WakfuGameCalendar.getInstance().getDate()));
                final String remainingTimeDesc = TimeUtils.getLongDescription(remainingTime);
                return WakfuTranslator.getInstance().getString("pvp.stateDescription." + this.m_comportement.getPvpState().getId(), remainingTimeDesc);
            }
            case PVP_ON_LOCKED: {
                final GameInterval remainingTime = new GameInterval(NationPvpConstants.PVP_LEAVE_DURATION);
                remainingTime.substract(this.m_comportement.getPvpDate().timeTo(WakfuGameCalendar.getInstance().getDate()));
                final String remainingTimeDesc = TimeUtils.getLongDescription(remainingTime);
                return WakfuTranslator.getInstance().getString("pvp.stateDescription." + this.m_comportement.getPvpState().getId(), remainingTimeDesc);
            }
            default: {
                return WakfuTranslator.getInstance().getString("pvp.stateDescription." + this.m_comportement.getPvpState().getId());
            }
        }
    }
    
    private Color getColor() {
        return WakfuClientConstants.PASSPORT_TEXT_COLOR;
    }
    
    @Override
    public void updateCandidateInfo() {
        this.m_candidateInfoFieldProvider = NationDisplayer.getInstance().getCandidateInfoFieldProvider(this.m_comportement.getCitizenId());
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "candidateInfo", "hasVoted", "isCandidate", "canVoteConcerningCrimes", "canApplyForGovernorConcerningCrimes");
    }
    
    @Override
    public void updatePvpTimer() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "pvpStatePercent", "pvpStateDescription");
    }
    
    @Override
    public void updatePvpState(final boolean full) {
        this.updateAdditionalAppearance();
        if (full && this.m_comportement.getCitizen() == WakfuGameEntity.getInstance().getLocalPlayer()) {
            final String title = WakfuTranslator.getInstance().getString("pvp.stateTitle." + this.m_comportement.getPvpState().getId());
            final String text = this.getPvpStateDescription();
            final Message uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.PVP, 600132);
            Worker.getInstance().pushMessage(uiNotificationMessage);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "pvpStatePercent", "pvpStateDescription", "pvpStateStyle", "canTogglePvp");
    }
    
    @Override
    public void updateAdditionalAppearance() {
        final PlayerCharacter player = (PlayerCharacter)this.m_comportement.getCitizen();
        player.updateAdditionalAppearance();
    }
    
    private void updateUI(final int nationId, final int oldCitizenScore, final boolean updateExternalDisplays) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final PlayerCharacter player = (PlayerCharacter)this.m_comportement.getCitizen();
        final int citizenScoreForNation = this.m_comportement.getCitizenScoreForNation(nationId);
        final CitizenRank citizenRank = CitizenRankManager.getInstance().getRankFromCitizenScore(citizenScoreForNation);
        CitizenScoreFieldProvider cs = this.m_citizenScoresFieldProviders.get(nationId);
        if (cs == null) {
            cs = new CitizenScoreFieldProvider(nationId);
            this.m_citizenScoresFieldProviders.put(nationId, cs);
        }
        cs.setCitizenScore(citizenScoreForNation);
        if (nationId == this.m_comportement.getNationId()) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "nativeNationCitizenScore");
        }
        if (player.isLocalPlayer()) {
            this.notifyChangeForView(nationId, citizenRank, oldCitizenScore, citizenScoreForNation, player);
        }
        if (updateExternalDisplays) {
            this.updateAdditionalAppearance();
        }
    }
    
    private void notifyChangeForView(final int nationId, final CitizenRank citizenRank, final int oldCitizenScore, final int citizenScoreForNation, final PlayerCharacter player) {
        final int crimePoints = citizenScoreForNation - oldCitizenScore;
        if (crimePoints == 0) {
            return;
        }
        final CitizenRank lastCitizenRank = CitizenRankManager.getInstance().getRankFromCitizenScore(oldCitizenScore);
        final boolean rankChanged = lastCitizenRank != citizenRank;
        if (rankChanged && this.m_comportement.isNationEnemy() && !lastCitizenRank.hasRule(CitizenRankRule.IS_NATION_ENEMY)) {
            final String nation = WakfuTranslator.getInstance().getString(39, nationId, new Object[0]);
            final String rankName = WakfuTranslator.getInstance().getString(citizenRank.getTranslationKey());
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("nation.playerCriminal", nation, rankName), 8);
            final String title = WakfuTranslator.getInstance().getString("notification.outlawTitle");
            final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.outlawText", WakfuTranslator.getInstance().getString(39, nationId, new Object[0])), NotificationMessageType.OUTLAW, nationId + "");
            final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.OUTLAW);
            Worker.getInstance().pushMessage(uiNotificationMessage);
        }
        UIControlCenterContainerFrame.getInstance().updateCitizenScore(citizenScoreForNation, citizenScoreForNation - oldCitizenScore);
        final String iconUrl = WakfuConfiguration.getInstance().getIconUrl("lawFlyingImagePath", "defaultIconPath", (crimePoints > 0) ? "1" : "2");
        final Texture texture = TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), Engine.getTextureName(iconUrl), iconUrl, false);
        final CharacterActor target = player.getActor();
        final FlyingImage.DefaultFlyingImageDeformer flyingImageDeformer = new FlyingImage.DefaultFlyingImageDeformer();
        final FlyingImage flyingImage = new FlyingImage(texture, 48, 48, flyingImageDeformer, 3000);
        flyingImage.setTarget(target);
        final HashSet<Adviser> advisers = AdviserManager.getInstance().getAdvisers(target);
        final FlyingTextDeformer textDeformer = new FlyingText.FadingFlyingTextDeformer(0, 20);
        final FlyingText flyingText = new FlyingText(FontFactory.createFont("WCI", 5, 10), ((crimePoints > 0) ? "+" : "") + crimePoints, textDeformer, 3000);
        flyingText.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        flyingText.setTarget(target);
        int waitingTime = 0;
        if (advisers != null) {
            waitingTime = advisers.size() * 600;
            flyingImage.setWaitingTime(waitingTime);
            flyingText.setWaitingTime(waitingTime);
        }
        AdviserManager.getInstance().addAdviser(flyingImage);
        AdviserManager.getInstance().addAdviser(flyingText);
        WakfuSoundManager.getInstance().playGUISound((crimePoints > 0) ? 600139L : 600140L, false, waitingTime);
    }
    
    public void updateRankStuff() {
        final PlayerCharacter player = (PlayerCharacter)this.m_comportement.getCitizen();
        if (player.isInvisibleForLocalPlayer()) {
            player.removeAdditionalAppearance();
            return;
        }
        final boolean showAps = !player.isDead();
        player.updateAdditionalAppearance();
        player.refreshDisplayEquipment();
    }
    
    @Override
    public void nationSet(final Nation nation) {
        if (this.m_comportement.getCitizen() == WakfuGameEntity.getInstance().getLocalPlayer()) {
            PvpLadderEntryView.getOrCreate(this.m_comportement.getCitizen(), true);
        }
        this.m_citizenScoresFieldProviders.put(nation.getNationId(), new CitizenScoreFieldProvider(nation.getNationId()));
        this.updateUI(nation.getNationId(), this.m_comportement.getCitizenScoreForNation(nation.getNationId()), false);
    }
    
    @Override
    public void hasVoted() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasVoted");
    }
    
    @Override
    public void onJailCooldownUpdate(final int oldCrimePoints) {
        this.updateUI(this.m_comportement.getCrimePurgationNationId(), oldCrimePoints, false);
    }
    
    @Override
    public void onCitizenScoreChanged(final int nationId, final int oldCrimePoints) {
        this.updateUI(nationId, oldCrimePoints, true);
    }
    
    @Override
    public void onPassportActiveChanged() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isPassportActive");
    }
}
