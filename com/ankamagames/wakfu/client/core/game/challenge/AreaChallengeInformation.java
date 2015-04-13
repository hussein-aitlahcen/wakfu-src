package com.ankamagames.wakfu.client.core.game.challenge;

import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class AreaChallengeInformation
{
    private int m_currentProtectorId;
    private ChallengeData m_challengeInZone;
    private ChallengeStatus m_currentZoneStatus;
    private static final String CHALLENGE_PROPOSAL_TAG = "challengeProposal";
    private static final String CHALLENGE_VAR_UPDATED_TAG = "challengeVarUpdated";
    private static final String CHALLENGE_VAR_UPDATE_NEGATE_TAG = "challengeVarUpdateNegate";
    private static final String CHALLENGE_WIN_TAG = "challengeWin";
    private static final String CHALLENGE_FAILED_TAG = "challengeFailed";
    private static final int FLYING_IMAGE_DELAY = 600;
    private static AreaChallengeInformation m_instance;
    
    public void setCurrentProtectorId(final int currentProtectorId) {
        this.m_currentProtectorId = currentProtectorId;
    }
    
    public void setChallengeInZone(final ChallengeData challengeInZone) {
        this.m_challengeInZone = challengeInZone;
        this.updateCurrentChallengeInUI();
    }
    
    public void setCurrentZoneStatus(final ChallengeStatus currentZoneStatus) {
        this.m_currentZoneStatus = currentZoneStatus;
    }
    
    public void setCurrentZoneStatus(final ChallengeStatus currentZoneStatus, final int timeBeforeNext) {
        this.m_currentZoneStatus = currentZoneStatus;
        AreaChallengeWaiting.INSTANCE.setStartDate(timeBeforeNext, false);
    }
    
    public ChallengeStatus getCurrentZoneStatus() {
        return this.m_currentZoneStatus;
    }
    
    public int getCurrentProtectorId() {
        return this.m_currentProtectorId;
    }
    
    public ChallengeData getChallengeInZone() {
        return this.m_challengeInZone;
    }
    
    public static AreaChallengeInformation getInstance() {
        return AreaChallengeInformation.m_instance;
    }
    
    public void cleanCurrentChallenge() {
        if (this.m_challengeInZone != null) {
            ChallengeManager.getInstance().removeChallenge(this.m_challengeInZone.getId());
            this.m_challengeInZone = null;
            FollowedQuestsManager.INSTANCE.setCurrentChallengeId(-1);
        }
        this.updateProperties();
        PropertiesProvider.getInstance().setPropertyValue("waitingForInputChallenge", null);
    }
    
    private void displayFlyingImage(final String iconUrl) {
        this.displayFlyingImage(TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), Engine.getTextureName(iconUrl), iconUrl, false));
    }
    
    private void displayFlyingImage(final Texture texture) {
        final CharacterActor target = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
        final FlyingImage.DefaultFlyingImageDeformer flyingImageDeformer = new FlyingImage.DefaultFlyingImageDeformer();
        final FlyingImage flyingImage = new FlyingImage(texture, 32, 32, flyingImageDeformer, 2000);
        flyingImage.setTarget(target);
        final HashSet<Adviser> advisers = AdviserManager.getInstance().getAdvisers(target);
        flyingImage.setWaitingTime(Math.max(1, (advisers != null) ? (advisers.size() * 600) : 0));
        AdviserManager.getInstance().addAdviser(flyingImage);
    }
    
    public void setChallengeInZone(final int challengeId, final long timeBeforeNext, final GameDateConst startDate, final TIntArrayList actions, final boolean registeredToChallenge, final int protectorId) {
        if (challengeId == -1) {
            AreaChallengeWaiting.INSTANCE.setStartDate(timeBeforeNext, this.m_challengeInZone == null);
            AreaChallengeWaiting.INSTANCE.updateProperty();
        }
        else {
            AreaChallengeWaiting.INSTANCE.setStartDate(timeBeforeNext, false);
            final boolean proposal = this.m_currentZoneStatus == ChallengeStatus.PROPOSED;
            if (this.m_challengeInZone == null) {
                this.m_challengeInZone = ChallengeManager.getInstance().loadChallenge(challengeId, startDate);
                if (this.m_challengeInZone == null) {
                    return;
                }
                this.m_challengeInZone.activateActions(actions);
                if (registeredToChallenge) {
                    this.m_challengeInZone.setProposed(false);
                    this.m_challengeInZone.setActivated(true);
                    this.m_challengeInZone.setLaunched(true);
                }
                if (proposal && !registeredToChallenge) {
                    WakfuSoundManager.getInstance().playGUISound(600127L);
                }
            }
            this.m_challengeInZone.setProtectorId(protectorId);
            this.m_challengeInZone.activateClock(startDate);
            this.highlightChallengeField();
        }
        this.updateCurrentChallengeInUI();
        this.updateProperty();
    }
    
    public void updateCurrentChallengeInUI() {
        int challengeId;
        if (this.isPlayerConcernedByChallenge()) {
            challengeId = ((this.m_challengeInZone != null) ? this.m_challengeInZone.getId() : -1);
        }
        else {
            challengeId = -1;
        }
        FollowedQuestsManager.INSTANCE.setCurrentChallengeId(challengeId);
    }
    
    public void highlightChallengeField() {
        String dialogId;
        if (Xulor.getInstance().isLoaded("verticalFollowedAchievementsDialog")) {
            dialogId = "verticalFollowedAchievementsDialog";
        }
        else if (Xulor.getInstance().isLoaded("followedAchievementsDialog")) {
            dialogId = "followedAchievementsDialog";
        }
        else {
            dialogId = null;
        }
        if (dialogId == null) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogId);
        final Widget container = (Widget)map.getElement("environmentRenderable.environmentQuestContainer");
        final ParticleDecorator particleDecorator = new ParticleDecorator();
        particleDecorator.onCheckOut();
        particleDecorator.setFile("6001051.xps");
        particleDecorator.setAlignment(Alignment9.CENTER);
        container.getAppearance().add(particleDecorator);
    }
    
    public void updateProperty() {
        switch (this.m_currentZoneStatus) {
            case PROPOSED:
            case RUNNING: {
                final AbstractChallengeView view = (this.m_challengeInZone != null) ? ChallengeViewManager.INSTANCE.getChallengeView(this.m_challengeInZone.getId()) : null;
                PropertiesProvider.getInstance().setPropertyValue("displayedAchievement", view);
                break;
            }
            case NO_CHALLENGES_IN_LIST:
            case WAITING_NEXT_CHALLENGE: {
                PropertiesProvider.getInstance().setPropertyValue("displayedAchievement", null);
                break;
            }
        }
    }
    
    public void updateStatus() {
        switch (this.m_currentZoneStatus) {
            case RUNNING: {
                if (this.m_challengeInZone != null) {
                    this.m_challengeInZone.setProposed(false);
                    this.m_challengeInZone.setActivated(true);
                    break;
                }
                break;
            }
            case NO_CHALLENGES_IN_LIST:
            case WAITING_NEXT_CHALLENGE: {
                if (this.m_challengeInZone != null && !this.m_challengeInZone.isSuccess() && this.m_challengeInZone.islaunched()) {
                    this.failChallenge(this.m_challengeInZone.getId());
                }
                this.cleanCurrentChallenge();
                AreaChallengeWaiting.INSTANCE.activateClock();
                break;
            }
        }
    }
    
    public void failChallenge(final int scenarioId) {
        if (this.m_challengeInZone != null && this.m_challengeInZone.getId() == scenarioId) {
            PropertiesProvider.getInstance().setPropertyValue("challengeDetailsVisible", false);
            this.m_challengeInZone.setFailed(true);
            this.m_challengeInZone.setLaunched(false);
            this.m_challengeInZone.setActivated(false);
            this.m_challengeInZone.setTarget(null);
            final boolean display = !this.m_challengeInZone.isChaos();
            if (display && this.isPlayerConcernedByChallenge()) {
                final String stringMessage = WakfuTranslator.getInstance().getString("chat.challenge.failed", this.m_challengeInZone.getTitle());
                final int chatPipe = 4;
                ChatManager.getInstance().pushMessage(stringMessage, 4);
                this.displayFlyingImage(WakfuConfiguration.getInstance().getIconUrl("challengeFlyingImagePath", "defaultIconPath", "challengeFailed"));
            }
            this.cleanChallengeInZone();
        }
        this.updateProperties();
    }
    
    public void cleanChallengeInZone() {
        if (this.m_challengeInZone != null) {
            ChallengeManager.getInstance().removeChallenge(this.m_challengeInZone.getId());
            this.m_challengeInZone = null;
            this.updateProperty();
            FollowedQuestsManager.INSTANCE.setCurrentChallengeId(-1);
        }
    }
    
    public short getRemainingTime() {
        if (this.m_challengeInZone != null) {
            return this.m_challengeInZone.getRemainingTime();
        }
        return -1;
    }
    
    public void updateProperties() {
    }
    
    public void lastGoalCompleted() {
        if (this.isPlayerConcernedByChallenge()) {
            this.displayFlyingImage(WakfuConfiguration.getInstance().getIconUrl("challengeFlyingImagePath", "defaultIconPath", "challengeWin"));
            final CharacterActor characterActor = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
            characterActor.setDirection(Direction8.SOUTH_WEST);
            characterActor.setAnimation("AnimEmote-Victoire");
        }
    }
    
    public void varUpdated(final boolean positive) {
        if (this.isPlayerConcernedByChallenge()) {
            final String iconName = positive ? "challengeVarUpdated" : "challengeVarUpdateNegate";
            this.displayFlyingImage(WakfuConfiguration.getInstance().getIconUrl("challengeFlyingImagePath", "defaultIconPath", iconName));
        }
    }
    
    public boolean isPlayerConcernedByChallenge() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final SimpleCriterion crit = (this.m_challengeInZone != null) ? this.m_challengeInZone.getModel().getRewardEligibilityCriterion() : null;
        final boolean critIsValid = crit == null || crit.isValid(player, player, null, null);
        return critIsValid && !player.getCitizenComportment().isNationEnemy();
    }
    
    static {
        AreaChallengeInformation.m_instance = new AreaChallengeInformation();
    }
}
