package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.fight.handler.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.dungeon.loader.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.dungeon.arcade.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.dungeon.ranks.*;

public class UIArcadeDungeonFrame implements MessageFrame, FightListener
{
    protected static final Logger m_logger;
    private static final UIArcadeDungeonFrame INSTANCE;
    private ParticleDecorator m_particleDecorator;
    private Container m_splashMessageDialogContainer;
    private ArrayList<ModulationColorClient> m_appL;
    private final LinkedList<String> m_waitingSplashTexts;
    private static final int SPLASH_TEXT_DISPLAY_TIME = 2500;
    private ArcadeDungeonView m_arcadeDungeonView;
    private boolean m_monsterScoreRollEnded;
    private boolean m_eventScoreRollEnded;
    private boolean m_challengeScoreRollEnded;
    private DisplayResultStep m_currentStep;
    private Runnable m_rewardRunnable;
    private int m_remainingTurns;
    private TotalScoreUpdate m_totalScoreUpdate;
    private AudioSource m_rollSound;
    private boolean m_bossSpawned;
    private int m_localPlayerLevelAtStart;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UIArcadeDungeonFrame() {
        super();
        this.m_waitingSplashTexts = new LinkedList<String>();
        this.m_remainingTurns = -1;
        this.m_localPlayerLevelAtStart = -1;
    }
    
    public static UIArcadeDungeonFrame getInstance() {
        return UIArcadeDungeonFrame.INSTANCE;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        return true;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            if (this.m_arcadeDungeonView == null) {
                UIArcadeDungeonFrame.m_logger.error((Object)"Impossible de charger l'affichage du donjon arcade, il manque les donn\u00e9es !");
                return;
            }
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final Fight currentFight = localPlayer.getCurrentFight();
            if (currentFight == null) {
                localPlayer.setActivateUIArcadeDungeonFrame(true);
                return;
            }
            this.activate(currentFight);
        }
    }
    
    public void activate(final Fight currentFight) {
        WakfuGameEntity.getInstance().getLocalPlayer().setActivateUIArcadeDungeonFrame(false);
        currentFight.registerLocalFightHandler(this);
        this.m_dialogUnloadListener = new DialogUnloadListener() {
            @Override
            public void dialogUnloaded(final String id) {
                if (id.equals("arcadeDungeonResultDialog")) {
                    WakfuGameEntity.getInstance().removeFrame(UIArcadeDungeonFrame.getInstance());
                    WakfuSoundManager.getInstance().playGUISound(600024L);
                }
            }
        };
        Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
        PropertiesProvider.getInstance().setPropertyValue("arcadeDungeon", this.m_arcadeDungeonView);
        Xulor.getInstance().load("arcadeDungeonDialog", Dialogs.getDialogPath("arcadeDungeonDialog"), 8192L, (short)10000);
        Xulor.getInstance().putActionClass("wakfu.arcadeDungeon", ArcadeDungeonDialogActions.class);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_localPlayerLevelAtStart = -1;
            this.m_currentStep = null;
            this.m_monsterScoreRollEnded = false;
            this.m_eventScoreRollEnded = false;
            this.m_challengeScoreRollEnded = false;
            if (this.m_rollSound != null) {
                this.m_rollSound.setStopOnNullGain(true);
                this.m_rollSound.fade(0.0f, 100.0f);
                this.m_rollSound = null;
            }
            ProcessScheduler.getInstance().remove(this.m_rewardRunnable);
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("arcadeDungeon");
            Xulor.getInstance().unload("arcadeDungeonDialog");
            Xulor.getInstance().removeActionClass("wakfu.arcadeDungeon");
        }
    }
    
    public void loadDungeon(final int dungeonId) {
        this.m_localPlayerLevelAtStart = WakfuGameEntity.getInstance().getLocalPlayer().getLevel();
        (this.m_arcadeDungeonView = new ArcadeDungeonView(ArcadeDungeonLoader.INSTANCE.getDungeon(dungeonId))).startChrono();
        this.m_totalScoreUpdate = new TotalScoreUpdate();
        WakfuGameEntity.getInstance().pushFrame(UIArcadeDungeonFrame.INSTANCE);
    }
    
    public void displaySplashText(final String text) {
        if (PropertiesProvider.getInstance().getObjectProperty("splashText") != null) {
            this.m_waitingSplashTexts.addLast(text);
        }
        else {
            PropertiesProvider.getInstance().setPropertyValue("splashText", text);
            if (!Xulor.getInstance().isLoaded("splashMessageDialog") || this.m_splashMessageDialogContainer == null) {
                this.m_splashMessageDialogContainer = (Container)Xulor.getInstance().load("splashMessageDialog", Dialogs.getDialogPath("splashMessageDialog"), 8210L, (short)10000);
            }
            if (this.m_splashMessageDialogContainer != null) {
                if (this.m_particleDecorator != null) {
                    this.m_splashMessageDialogContainer.getAppearance().destroyDecorator(this.m_particleDecorator);
                }
                (this.m_particleDecorator = new ParticleDecorator()).onCheckOut();
                this.m_particleDecorator.setFile("6001037.xps");
                this.m_particleDecorator.setAlignment(Alignment9.CENTER);
                this.m_splashMessageDialogContainer.getAppearance().add(this.m_particleDecorator);
            }
            this.fade(true);
        }
    }
    
    private void initializeAppList() {
        this.m_appL = new ArrayList<ModulationColorClient>();
        final ElementMap map = this.m_splashMessageDialogContainer.getElementMap();
        this.m_appL.add(((Widget)map.getElement("mainContainer")).getAppearance());
        this.m_appL.add(((Widget)map.getElement("text")).getAppearance());
    }
    
    private void setWidgetsNonBlocking(final boolean nonBlocking) {
        final ElementMap map = this.m_splashMessageDialogContainer.getElementMap();
        ((Widget)map.getElement("mainContainer")).setNonBlocking(nonBlocking);
    }
    
    public void fade(final boolean fadeIn) {
        if (this.m_splashMessageDialogContainer == null) {
            UIArcadeDungeonFrame.m_logger.warn((Object)"on tente de fade un splashScreen alors qu'il n'est pas charg\u00e9");
            return;
        }
        if (this.m_appL == null) {
            this.initializeAppList();
        }
        this.setWidgetsNonBlocking(!fadeIn);
        final ElementMap map = this.m_splashMessageDialogContainer.getElementMap();
        final EventDispatcher mainContainer = map.getElement("mainContainer");
        final ModulationColorClient app = this.m_appL.get(0);
        int aValue;
        int bValue;
        if (fadeIn) {
            aValue = Color.WHITE_ALPHA.get();
            bValue = Color.WHITE.get();
        }
        else {
            aValue = Color.WHITE.get();
            bValue = Color.WHITE_ALPHA.get();
        }
        if (aValue != bValue) {
            if (fadeIn) {
                WakfuSoundManager.getInstance().windowFadeIn();
            }
            else {
                WakfuSoundManager.getInstance().windowFadeOut();
            }
            final Color a = new Color(aValue);
            final Color b = new Color(bValue);
            final String text = (String)PropertiesProvider.getInstance().getObjectProperty("splashText");
            mainContainer.removeTweensOfType(ModulationColorListTween.class);
            final ModulationColorListTween tw = new ModulationColorListTween(a, b, this.m_appL, 0, 500, 1, TweenFunction.PROGRESSIVE);
            tw.addTweenEventListener(new TweenEventListener() {
                @Override
                public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                    switch (e) {
                        case TWEEN_ENDED: {
                            tw.removeTweenEventListener(this);
                            if (fadeIn) {
                                ProcessScheduler.getInstance().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        UIArcadeDungeonFrame.this.fade(false);
                                    }
                                }, 2500 + text.length() * 25, 1);
                                break;
                            }
                            if (!UIArcadeDungeonFrame.this.m_waitingSplashTexts.isEmpty()) {
                                ProcessScheduler.getInstance().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        PropertiesProvider.getInstance().setPropertyValue("splashText", null);
                                        UIArcadeDungeonFrame.this.displaySplashText(UIArcadeDungeonFrame.this.m_waitingSplashTexts.poll());
                                    }
                                }, 2000L, 1);
                                break;
                            }
                            if (UIArcadeDungeonFrame.this.m_particleDecorator != null) {
                                UIArcadeDungeonFrame.this.m_splashMessageDialogContainer.getAppearance().destroyDecorator(UIArcadeDungeonFrame.this.m_particleDecorator);
                                UIArcadeDungeonFrame.this.m_particleDecorator = null;
                            }
                            UIArcadeDungeonFrame.this.m_splashMessageDialogContainer = null;
                            UIArcadeDungeonFrame.this.m_appL = null;
                            PropertiesProvider.getInstance().setPropertyValue("splashText", null);
                            break;
                        }
                    }
                }
            });
            mainContainer.addTween(tw);
        }
    }
    
    public ArcadeDungeonView getArcadeDungeonView() {
        return this.m_arcadeDungeonView;
    }
    
    public void loadResultDialog() {
        if (this.m_totalScoreUpdate != null) {
            this.m_totalScoreUpdate.forceEnd();
        }
        if (Xulor.getInstance().isLoaded("arcadeDungeonResultDialog")) {
            return;
        }
        final Window window = (Window)Xulor.getInstance().load("arcadeDungeonResultDialog", Dialogs.getDialogPath("arcadeDungeonResultDialog"), 257L, (short)10000);
        window.addWindowPostProcessedListener(new WindowPostProcessedListener() {
            @Override
            public void windowPostProcessed() {
                UIArcadeDungeonFrame.this.startFirstScoreStep();
                window.removeWindowPostProcessedListener(this);
            }
        });
        WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight().unRegisterLocalFightHandler(this);
        WakfuSoundManager.getInstance().playGUISound(600137L);
    }
    
    public void startFirstScoreStep() {
        if (this.m_currentStep == DisplayResultStep.FIRST_SCORE_STEP) {
            return;
        }
        this.m_currentStep = DisplayResultStep.FIRST_SCORE_STEP;
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("arcadeDungeonResultDialog");
        if (map == null) {
            return;
        }
        final TextView monsterCount = (TextView)map.getElement("monsterCount");
        final int mc = this.m_arcadeDungeonView.getMonsterCompletedCount();
        ProcessScheduler.getInstance().schedule(new Runnable() {
            int count;
            
            @Override
            public void run() {
                monsterCount.setText(String.valueOf(this.count));
                ++this.count;
            }
        }, 50L, mc + 1);
        final TextView eventCount = (TextView)map.getElement("eventCount");
        final int ec = this.m_arcadeDungeonView.getEventCompletedCount();
        ProcessScheduler.getInstance().schedule(new Runnable() {
            int count;
            
            @Override
            public void run() {
                eventCount.setText(String.valueOf(this.count));
                ++this.count;
            }
        }, 50L, ec + 1);
        final TextView challengeCount = (TextView)map.getElement("challengeCount");
        final int cc = this.m_arcadeDungeonView.getChallengeCompletedCount();
        ProcessScheduler.getInstance().schedule(new Runnable() {
            int count;
            
            @Override
            public void run() {
                challengeCount.setText(String.valueOf(this.count));
                ++this.count;
            }
        }, 50L, cc + 1);
        final RollingText monsterScore = (RollingText)map.getElement("monsterScore");
        monsterScore.setNumber(this.m_arcadeDungeonView.getMonsterTotalScore());
        monsterScore.addRollEndListener(new RollingText.RollEndListener() {
            @Override
            public void onRollEnd() {
                UIArcadeDungeonFrame.this.m_monsterScoreRollEnded = true;
                if (UIArcadeDungeonFrame.this.firstScoreStepEnded()) {
                    UIArcadeDungeonFrame.this.startSecondScoreStep();
                }
                monsterScore.removeRollEndListener(this);
            }
        });
        final RollingText eventScore = (RollingText)map.getElement("eventScore");
        eventScore.setNumber(this.m_arcadeDungeonView.getEventTotalScore());
        eventScore.addRollEndListener(new RollingText.RollEndListener() {
            @Override
            public void onRollEnd() {
                UIArcadeDungeonFrame.this.m_eventScoreRollEnded = true;
                if (UIArcadeDungeonFrame.this.firstScoreStepEnded()) {
                    UIArcadeDungeonFrame.this.startSecondScoreStep();
                }
                eventScore.removeRollEndListener(this);
            }
        });
        final RollingText challengeScore = (RollingText)map.getElement("challengeScore");
        challengeScore.setNumber(this.m_arcadeDungeonView.getChallengeTotalScore());
        challengeScore.addRollEndListener(new RollingText.RollEndListener() {
            @Override
            public void onRollEnd() {
                UIArcadeDungeonFrame.this.m_challengeScoreRollEnded = true;
                if (UIArcadeDungeonFrame.this.firstScoreStepEnded()) {
                    UIArcadeDungeonFrame.this.startSecondScoreStep();
                }
                challengeScore.removeRollEndListener(this);
            }
        });
        this.m_rollSound = WakfuSoundManager.getInstance().playGUISound(600146L, true);
        if (this.m_rollSound != null) {
            this.m_rollSound.setGain(0.0f);
            this.m_rollSound.fade(1.0f, 100.0f);
        }
    }
    
    private void startSecondScoreStep() {
        if (this.m_currentStep == DisplayResultStep.SECOND_SCORE_STEP) {
            return;
        }
        this.m_currentStep = DisplayResultStep.SECOND_SCORE_STEP;
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("arcadeDungeonResultDialog");
        if (map == null) {
            return;
        }
        final RollingText finalScore = (RollingText)map.getElement("finalScore");
        final List rewardsList = (List)map.getElement("rewardsList");
        finalScore.setNumber(this.m_arcadeDungeonView.getCurrentScore());
        finalScore.addRollEndListener(new RollingText.RollEndListener() {
            @Override
            public void onRollEnd() {
                UIArcadeDungeonFrame.this.displayStamp();
                finalScore.removeRollEndListener(this);
                if (UIArcadeDungeonFrame.this.m_rollSound != null) {
                    UIArcadeDungeonFrame.this.m_rollSound.setStopOnNullGain(true);
                    UIArcadeDungeonFrame.this.m_rollSound.fade(0.0f, 100.0f);
                    UIArcadeDungeonFrame.this.m_rollSound = null;
                }
            }
        });
        final int nbRewards = this.m_arcadeDungeonView.getRewardsSize();
        if (nbRewards > 0) {
            this.m_rewardRunnable = new Runnable() {
                int cpt;
                
                @Override
                public void run() {
                    if (rewardsList.isUnloading()) {
                        return;
                    }
                    final RenderableContainer r = rewardsList.getRenderableByOffset(this.cpt);
                    final EventDispatcher icon = r.getInnerElementMap().getElement("rewardIcon");
                    Image i = (Image)icon;
                    if (!i.getVisible()) {
                        i = (Image)r.getInnerElementMap().getElement("rewardIcon2");
                    }
                    i.setModulationColor(Color.WHITE);
                    ++this.cpt;
                }
            };
            ProcessScheduler.getInstance().schedule(this.m_rewardRunnable, finalScore.getTotalDuration() / nbRewards, nbRewards);
        }
    }
    
    private void fadeReward(final Image i) {
        final Color c = Color.WHITE;
        final Color c2 = Color.WHITE_ALPHA;
        final AbstractTween t = new ModulationColorTween(c, c2, i, 0, 500, 4, TweenFunction.PROGRESSIVE);
        t.addTweenEventListener(new TweenEventListener() {
            @Override
            public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                if (e == TweenEvent.TWEEN_ENDED) {
                    i.setModulationColor(Color.WHITE);
                    t.removeTweenEventListener(this);
                }
            }
        });
        i.addTween(t);
    }
    
    private void displayStamp() {
        if (this.m_currentStep == DisplayResultStep.STAMP_STEP) {
            return;
        }
        this.m_currentStep = DisplayResultStep.STAMP_STEP;
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("arcadeDungeonResultDialog");
        if (map == null) {
            return;
        }
        final Widget stampCont = (Widget)map.getElement("containerStamp");
        int soundID = 600131;
        String file = null;
        switch (this.m_arcadeDungeonView.getRank()) {
            case A: {
                file = "6001046.xps";
                soundID = 600130;
                break;
            }
            case B: {
                file = "6001047.xps";
                soundID = 600130;
                break;
            }
            case C: {
                file = "6001048.xps";
                soundID = 600131;
                break;
            }
            case D: {
                file = "6001049.xps";
                soundID = 600131;
                break;
            }
        }
        if (file == null) {
            return;
        }
        WakfuSoundManager.getInstance().playGUISound(soundID);
        final ParticleDecorator decorator = new ParticleDecorator();
        decorator.onCheckOut();
        decorator.setFile(file);
        decorator.setAlignment(Alignment9.CENTER);
        stampCont.getAppearance().add(decorator);
    }
    
    public void onRoundBegin() {
        this.m_remainingTurns = -1;
        this.m_bossSpawned = false;
        this.m_arcadeDungeonView.onRoundBegin();
        if (this.m_arcadeDungeonView.getRoundCount() == 1) {
            WakfuSoundManager.getInstance().playGUISound(600136L);
        }
        else {
            WakfuSoundManager.getInstance().playGUISound(600147L);
        }
        UIArcadeDungeonFrame.INSTANCE.displaySplashText(WakfuTranslator.getInstance().getString("arcadeDungeon.round", this.m_arcadeDungeonView.getRoundCount()));
    }
    
    public void onNewWave() {
        this.m_arcadeDungeonView.onNewWave();
        final String title = WakfuTranslator.getInstance().getString("notification.arcadeDungeonWaveTitle");
        final TextWidgetFormater twf = new TextWidgetFormater();
        twf.append(WakfuTranslator.getInstance().getString("notification.arcadeDungeonWaveText", this.m_arcadeDungeonView.getWaveCount()));
        if (this.m_remainingTurns == -1 && this.m_bossSpawned) {
            this.m_remainingTurns = 6;
        }
        if (this.m_remainingTurns != -1) {
            --this.m_remainingTurns;
            twf.newLine();
            final String remainingText = WakfuTranslator.getInstance().getString("notification.arcadeDungeonRemainingTurnText", this.m_remainingTurns);
            twf.openText().b().addColor(Color.RED.getRGBAtoHex()).append(remainingText)._b().closeText();
        }
        Worker.getInstance().pushMessage(new UINotificationMessage(title, twf.finishAndToString(), NotificationMessageType.DUNGEON_LADDER));
    }
    
    private boolean firstScoreStepEnded() {
        return this.m_monsterScoreRollEnded && this.m_eventScoreRollEnded && this.m_challengeScoreRollEnded;
    }
    
    public void onEventSucceeded(final int eventId) {
        final int totalScore = this.m_arcadeDungeonView.getCurrentRoundTotalScore();
        final int score = Math.round(totalScore * 0.2f);
        this.m_arcadeDungeonView.addEventScore(score);
        this.updateScore(score);
        final String title = WakfuTranslator.getInstance().getString("notification.arcadeDungeonEventSucceededTitle");
        final String text = WakfuTranslator.getInstance().getString("notification.arcadeDungeonEventSucceededText", ArcadeDungeonLoader.INSTANCE.getEventName(eventId), score);
        Worker.getInstance().pushMessage(new UINotificationMessage(title, text, NotificationMessageType.DUNGEON_LADDER));
        this.addFlyingScoreTextOnTarget(WakfuGameEntity.getInstance().getLocalPlayer(), score);
    }
    
    public void onChallengeFailed(final int challengeId) {
        final DungeonChallengeView challenge = this.m_arcadeDungeonView.getChallenge(challengeId);
        challenge.setSucceeded(false);
        challenge.setFinished(true);
        final String title = WakfuTranslator.getInstance().getString("notification.arcadeDungeonChallengeFailedTitle");
        final String text = WakfuTranslator.getInstance().getString("notification.arcadeDungeonChallengeFailedText", challenge.getName());
        Worker.getInstance().pushMessage(new UINotificationMessage(title, text, NotificationMessageType.DUNGEON_LADDER));
        final ChatMessage chatMessage = new ChatMessage(text);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    public void onChallengeSucceeded(final int challengeId) {
        final DungeonChallengeView challenge = this.m_arcadeDungeonView.getChallenge(challengeId);
        challenge.setSucceeded(true);
        challenge.setFinished(true);
        final int challengeScore = challenge.getScore();
        this.m_arcadeDungeonView.addChallengeScore(challengeScore);
        this.updateScore(challengeScore);
        final String title = WakfuTranslator.getInstance().getString("notification.arcadeDungeonChallengeSucceededTitle");
        final String text = WakfuTranslator.getInstance().getString("notification.arcadeDungeonChallengeSucceededText", challengeScore, challenge.getName(), challenge.getDescription());
        Worker.getInstance().pushMessage(new UINotificationMessage(title, text, NotificationMessageType.DUNGEON_LADDER));
        final ChatMessage chatMessage = new ChatMessage(text);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
        this.addFlyingScoreTextOnTarget(WakfuGameEntity.getInstance().getLocalPlayer(), challengeScore);
    }
    
    public void onChallengePending(final int challengeId) {
        final DungeonChallengeView challenge = this.m_arcadeDungeonView.getChallenge(challengeId);
        challenge.setFinished(false);
    }
    
    @Override
    public void onPlacementStart() {
        WakfuSoundManager.getInstance().playGUISound(600134L);
    }
    
    @Override
    public void onPlacementEnd() {
    }
    
    @Override
    public void onFightStart() {
    }
    
    @Override
    public void onFightEnd() {
    }
    
    @Override
    public void onTableTurnStart() {
    }
    
    @Override
    public void onTableTurnEnd() {
    }
    
    @Override
    public void onFighterStartTurn(final BasicCharacterInfo fighter) {
    }
    
    @Override
    public void onFighterEndTurn(final BasicCharacterInfo fighter) {
    }
    
    @Override
    public void onFighterJoinFight(final BasicCharacterInfo fighter) {
        if (fighter instanceof NonPlayerCharacter) {
            final NonPlayerCharacter nonPlayerCharacter = (NonPlayerCharacter)fighter;
            if (nonPlayerCharacter.hasProperty(WorldPropertyType.BOSS)) {
                UIArcadeDungeonFrame.INSTANCE.displaySplashText(WakfuTranslator.getInstance().getString("arcadeDungeon.boss", nonPlayerCharacter.getName()));
                WakfuSoundManager.getInstance().playGUISound(600136L);
                this.m_bossSpawned = true;
            }
        }
    }
    
    @Override
    public void onFighterOutOfPlay(final BasicCharacterInfo fighter) {
        if (fighter instanceof NonPlayerCharacter) {
            final Fight currentFight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
            if (currentFight == null || currentFight.isEnding()) {
                return;
            }
            final NonPlayerCharacter nonPlayerCharacter = (NonPlayerCharacter)fighter;
            if (!nonPlayerCharacter.isActiveProperty(WorldPropertyType.IS_ARCADE_WAVE_NPC)) {
                return;
            }
            final int score = nonPlayerCharacter.getArcadeScore();
            this.m_arcadeDungeonView.addMonsterScore(score);
            this.updateScore(score);
            this.addFlyingScoreTextOnTarget(fighter, score);
        }
    }
    
    private void addFlyingScoreTextOnTarget(final BasicCharacterInfo fighter, final int score) {
        final FlyingTextDeformer textDeformer = new FlyingText.FadingFlyingTextDeformer(0, 20);
        final FlyingText flyingText = new FlyingText(FontFactory.createFont("WCI", 5, 20), "+" + score, textDeformer, 3000);
        flyingText.setColor(Color.GOLD.getRed(), Color.GOLD.getGreen(), Color.GOLD.getBlue(), Color.GOLD.getAlpha());
        flyingText.setTarget(fighter);
        final HashSet<Adviser> advisers = AdviserManager.getInstance().getAdvisers(fighter);
        if (advisers != null) {
            flyingText.setWaitingTime(advisers.size() * 600);
        }
        AdviserManager.getInstance().addAdviser(flyingText);
    }
    
    @Override
    public void onFighterWinFight(final BasicCharacterInfo fighter) {
    }
    
    @Override
    public void onFighterLoseFight(final BasicCharacterInfo fighter) {
    }
    
    @Override
    public void onFighterCastSpell(final BasicCharacterInfo caster, final AbstractSpell spell) {
    }
    
    @Override
    public void onEffectAreaGoesOffPlay(final AbstractEffectArea area) {
    }
    
    @Override
    public void onFighterRemovedFromFight(final BasicCharacterInfo fighter) {
        if (fighter instanceof LocalPlayerCharacter) {
            this.loadResultDialog();
        }
    }
    
    @Override
    public void onFightEnded() {
        this.loadResultDialog();
    }
    
    private void updateScore(final int scoreToAdd) {
        if (scoreToAdd < 1) {
            return;
        }
        final int totalScore = this.m_arcadeDungeonView.getTotalScore();
        if (this.m_totalScoreUpdate == null) {
            this.m_totalScoreUpdate = new TotalScoreUpdate();
        }
        if (this.m_totalScoreUpdate.isFinished()) {
            this.m_totalScoreUpdate.init(totalScore, scoreToAdd);
        }
        else {
            this.m_totalScoreUpdate.addToEnd(scoreToAdd);
        }
        ProcessScheduler.getInstance().schedule(this.m_totalScoreUpdate, Math.max(25, 75 - Math.max(1, this.m_totalScoreUpdate.getNbOfDigits()) * 25));
    }
    
    public int getLocalPlayerLevelAtStart() {
        return this.m_localPlayerLevelAtStart;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIArcadeDungeonFrame.class);
        INSTANCE = new UIArcadeDungeonFrame();
    }
    
    private enum DisplayResultStep
    {
        FIRST_SCORE_STEP, 
        SECOND_SCORE_STEP, 
        STAMP_STEP;
    }
    
    private class TotalScoreUpdate implements Runnable
    {
        private int m_start;
        private int m_end;
        private int m_current;
        private byte m_nbOfDigits;
        
        public void init(final int start, final int toAdd) {
            this.m_start = start;
            this.m_end = start + toAdd;
            this.m_nbOfDigits = this.getNbOfDigits(toAdd);
            this.m_current = this.m_start;
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("arcadeDungeonDialog");
            final TextView tv = (TextView)map.getElement("score");
            tv.setStyle("StyledBold16Dark");
        }
        
        private byte getNbOfDigits(final int toAdd) {
            return (byte)Math.max(0, String.valueOf(toAdd).length() - 2);
        }
        
        public boolean isFinished() {
            return this.m_current >= this.m_end;
        }
        
        @Override
        public void run() {
            final int delta = (int)(Math.pow(10.0, this.m_nbOfDigits) * 10.0 / 9.0);
            this.m_current += ((this.m_current + delta > this.m_end) ? (this.m_end - this.m_current) : delta);
            UIArcadeDungeonFrame.this.m_arcadeDungeonView.setTotalScore(this.m_current);
            if (this.isFinished()) {
                this.finish();
            }
        }
        
        private void finish() {
            this.m_nbOfDigits = 1;
            ProcessScheduler.getInstance().remove(this);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("arcadeDungeonDialog");
            final TextView tv = (TextView)map.getElement("score");
            tv.setStyle("DarkTitle");
        }
        
        public void addToEnd(final int toAdd) {
            this.m_end += toAdd;
            final byte b = this.getNbOfDigits(this.m_end - this.m_start);
            if (this.m_nbOfDigits < b) {
                this.m_nbOfDigits = b;
            }
        }
        
        public void forceEnd() {
            UIArcadeDungeonFrame.this.m_arcadeDungeonView.setTotalScore(this.m_end);
            this.finish();
        }
        
        public byte getNbOfDigits() {
            return this.m_nbOfDigits;
        }
    }
}
