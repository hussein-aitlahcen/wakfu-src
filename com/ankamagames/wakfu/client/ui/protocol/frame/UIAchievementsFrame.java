package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.appearance.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.achievement.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.achievement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.core.windowStick.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;

public class UIAchievementsFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final UIAchievementsFrame m_instance;
    private AsbtractAchievementsView m_displayedAchievementsView;
    protected AchievementView m_displayedAchievementView;
    final Deque<AchievementView> m_waitingAchievementViews;
    protected Container m_achievementDialogContainer;
    private ArrayList<ModulationColorClient> m_appL;
    protected ParticleDecorator m_particleDecorator;
    private AchievementListView m_achievementListView;
    private long m_characterId;
    protected static final int UNLOCKED_ACHIEVEMENT_DISPLAY_TIME = 10000;
    protected final ArrayList<AchievementView> m_followedViews;
    protected AchievementFilter m_currentFilter;
    
    public UIAchievementsFrame() {
        super();
        this.m_waitingAchievementViews = new LinkedList<AchievementView>();
        this.m_characterId = WakfuGameEntity.getInstance().getLocalPlayer().getId();
        this.m_followedViews = new ArrayList<AchievementView>(3);
        this.m_currentFilter = AchievementFilter.ALL;
    }
    
    public boolean isCompltetedAuthorised() {
        return this.m_currentFilter == AchievementFilter.ALL || this.m_currentFilter == AchievementFilter.COMPLETED;
    }
    
    public boolean isInProgressAuthorised() {
        return this.m_currentFilter == AchievementFilter.ALL || this.m_currentFilter == AchievementFilter.TO_DO;
    }
    
    public boolean isQuestDisplayed() {
        return this.m_displayedAchievementsView != null && this.m_displayedAchievementsView.isQuestView();
    }
    
    public static UIAchievementsFrame getInstance() {
        return UIAchievementsFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16136: {
                final UISelectAchievementContainerMessage uiSelectAchievementContainerMessage = (UISelectAchievementContainerMessage)message;
                AchievementCategoryView categoryView = uiSelectAchievementContainerMessage.getAchievementCategoryView();
                setSelectedAchievement(null);
                if (categoryView == null || categoryView instanceof AchievementsHistoryCategoryView) {
                    categoryView = this.m_displayedAchievementsView.getHistoryCategoryView();
                }
                this.m_displayedAchievementsView.setSelectedRootCategory(categoryView);
                return false;
            }
            case 16137: {
                final UISelectAchievementMessage uiSelectAchievementMessage = (UISelectAchievementMessage)message;
                final AchievementView achievementView = uiSelectAchievementMessage.getAchievementView();
                this.selectAchievement(achievementView);
                return false;
            }
            case 19459: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                this.m_characterId = msg.getLongValue();
                final AchievementCategoryView selectedRootCategory = this.m_displayedAchievementsView.getSelectedRootCategory();
                if (Xulor.getInstance().isLoaded("questDialog")) {
                    this.m_displayedAchievementsView = AchievementsViewManager.INSTANCE.getQuestsView(this.m_characterId);
                }
                else if (Xulor.getInstance().isLoaded("achievementsDialog")) {
                    this.m_displayedAchievementsView = AchievementsViewManager.INSTANCE.getAchievementsView(this.m_characterId);
                }
                this.m_displayedAchievementsView.setSelectedRootCategory(this.m_displayedAchievementsView.getCategoryFromId(selectedRootCategory.getId()));
                PropertiesProvider.getInstance().setPropertyValue("achievementsManager", this.m_displayedAchievementsView);
                final AchievementView view = (AchievementView)PropertiesProvider.getInstance().getObjectProperty("selectedAchievement");
                if (view != null) {
                    final AchievementView achievement = AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, view.getId());
                    this.selectAchievement(achievement);
                }
                return false;
            }
            case 16138: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final int achievementId = msg.getIntValue();
                final boolean ok = WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext().setFollowed(achievementId, msg.getBooleanValue());
                if (ok) {
                    FollowedQuestsManager.INSTANCE.onFollowedListChanged();
                    final Message netmsg = new AchievementFollowRequestMessage(msg.getIntValue(), msg.getBooleanValue());
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netmsg);
                }
                return false;
            }
            case 16139: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final int achievementId = msg.getIntValue();
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (!localPlayer.getPartyComportment().isInParty()) {
                    final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.notify.questSharedWithNobody"));
                    chatMessage.setPipeDestination(4);
                    ChatManager.getInstance().pushMessage(chatMessage);
                }
                else {
                    final Message netMsg = new AchievementShareRequestMessage(achievementId);
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
                }
                return false;
            }
            case 16140: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final int achievementId = msg.getIntValue();
                WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext().tryToActivate(achievementId);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean selectAchievement(AchievementView achievementView) {
        if (achievementView == null) {
            achievementView = this.m_displayedAchievementsView.getLastCompletedAchievement();
            this.m_displayedAchievementsView.setSelectedRootCategory(this.m_displayedAchievementsView.getHistoryCategoryView());
        }
        if (achievementView == null) {
            return true;
        }
        setSelectedAchievement(achievementView);
        PropertiesProvider.getInstance().setPropertyValue("overQuickAchievement", achievementView);
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_displayedAchievementsView.getSelectedRootCategory(), "achievementsQuickList");
        return false;
    }
    
    public static boolean isFollowedAchievementDialogLoaded() {
        final boolean isVerticalData = !WindowStickManager.getInstance().hasStickData("followedAchievementsDialog") || WindowStickManager.getInstance().isVerticalData("followedAchievementsDialog");
        final String id = isVerticalData ? "verticalFollowedAchievementsDialog" : "followedAchievementsDialog";
        return Xulor.getInstance().isLoaded(id);
    }
    
    public static void loadFollowedAchievements(final boolean display) {
        final boolean isVerticalData = !WindowStickManager.getInstance().hasStickData("followedAchievementsDialog") || WindowStickManager.getInstance().isVerticalData("followedAchievementsDialog");
        final String id = isVerticalData ? "verticalFollowedAchievementsDialog" : "followedAchievementsDialog";
        if (display) {
            AchievementUIHelper.displayFollowedAchievements();
            final Window window = (Window)Xulor.getInstance().load(id, Dialogs.getDialogPath(id), 40976L, (short)10000);
            WindowStickManager.getInstance().addWindow(window, false);
            window.setEnableResizeEvents(true);
            window.setHorizontalDialog("followedAchievementsDialog");
            window.setVerticalDialog("verticalFollowedAchievementsDialog");
            window.addEventListener(Events.RESIZED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    final Container parent = window.getContainer();
                    final WindowStickData stickData = window.getStickData();
                    final WindowStickManager.StickAlignment align = (stickData != null) ? stickData.getAlign() : null;
                    int x;
                    int y;
                    if (align == null || align == WindowStickManager.StickAlignment.NONE) {
                        x = MathHelper.clamp(window.getX(), 0, parent.getWidth() - window.getWidth());
                        y = MathHelper.clamp(window.getY(), 0, parent.getHeight() - window.getHeight());
                    }
                    else {
                        x = align.getBorderX(window.getX(), window);
                        y = align.getBorderY(window.getY(), window);
                    }
                    window.setPosition(x, y);
                    return false;
                }
            }, false);
            Xulor.getInstance().putActionClass("wakfu.followedAchievements", FollowedAchievementsDialogActions.class);
        }
        else {
            Xulor.getInstance().unload(id, true);
            Xulor.getInstance().removeActionClass("wakfu.followedAchievements");
        }
    }
    
    public static void highlightFollowedAchievement(final int achievementId) {
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                final boolean isVerticalData = !WindowStickManager.getInstance().hasStickData("followedAchievementsDialog") || WindowStickManager.getInstance().isVerticalData("followedAchievementsDialog");
                final String id = isVerticalData ? "verticalFollowedAchievementsDialog" : "followedAchievementsDialog";
                final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(id);
                if (map == null) {
                    return;
                }
                final StackList list = (StackList)map.getElement("achievementsList");
                final ArrayList<Object> items = list.getItems();
                int index = -1;
                for (int i = 0, size = items.size(); i < size; ++i) {
                    final AbstractQuestView view = items.get(i);
                    if (view != null && view.getId() == achievementId) {
                        index = i;
                        break;
                    }
                }
                if (index == -1) {
                    return;
                }
                final RenderableContainer renderableContainer = list.getRenderables().get(index);
                if (renderableContainer == null) {
                    return;
                }
                final Widget w = (Widget)renderableContainer.getInnerElementMap().getElement("environmentQuestContainer");
                final ParticleDecorator particleDecorator = new ParticleDecorator();
                particleDecorator.onCheckOut();
                particleDecorator.setFile("6001051.xps");
                particleDecorator.setAlignment(Alignment9.CENTER);
                w.getAppearance().add(particleDecorator);
            }
        }, 500L, 1);
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
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_displayedAchievementView = null;
            this.m_appL = null;
            this.m_achievementDialogContainer = null;
            Xulor.getInstance().unload("achievementUnlockedDialog");
            this.cleanUp();
        }
    }
    
    public void loadUnloadAchievementsDialog(final boolean quest) {
        if (quest) {
            if (Xulor.getInstance().isLoaded("questDialog")) {
                unloadAchievementDialog(true);
            }
            else {
                this.loadAchievementDialog(true, this.m_displayedAchievementView);
            }
        }
        else if (Xulor.getInstance().isLoaded("achievementsDialog")) {
            unloadAchievementDialog(false);
        }
        else {
            this.loadAchievementDialog(false, this.m_displayedAchievementView);
        }
    }
    
    private void cleanUp() {
        this.m_displayedAchievementsView = null;
        this.m_displayedAchievementView = null;
        this.m_achievementListView = null;
        this.m_waitingAchievementViews.clear();
        this.m_particleDecorator = null;
        PropertiesProvider.getInstance().removeProperty("achievementsManager");
        PropertiesProvider.getInstance().removeProperty("selectedAchievement");
        PropertiesProvider.getInstance().removeProperty("overQuickAchievement");
        Xulor.getInstance().removeActionClass("wakfu.achievements");
    }
    
    private static void unloadAchievementDialog(final boolean quest) {
        Xulor.getInstance().unload(quest ? "questDialog" : "achievementsDialog");
        WakfuSoundManager.getInstance().playGUISound(600046L);
    }
    
    public void loadAchievementDialog(final boolean quest, final AchievementView achievement) {
        this.m_characterId = WakfuGameEntity.getInstance().getLocalPlayer().getId();
        String dialogToOpenId;
        String dialogToCloseId;
        if (quest) {
            this.m_displayedAchievementsView = AchievementsViewManager.INSTANCE.getQuestsView(this.m_characterId);
            dialogToOpenId = "questDialog";
            dialogToCloseId = "achievementsDialog";
        }
        else {
            this.m_displayedAchievementsView = AchievementsViewManager.INSTANCE.getAchievementsView(this.m_characterId);
            dialogToOpenId = "achievementsDialog";
            dialogToCloseId = "questDialog";
        }
        if (achievement != null) {
            this.m_displayedAchievementsView.setSelectedRootCategory(AchievementsViewManager.INSTANCE.getCategory(this.m_characterId, achievement.getRootCategoryId()));
        }
        PropertiesProvider.getInstance().setPropertyValue("selectedAchievement", achievement);
        PropertiesProvider.getInstance().setPropertyValue("overQuickAchievement", achievement);
        PropertiesProvider.getInstance().setPropertyValue("achievementsManager", this.m_displayedAchievementsView);
        if (Xulor.getInstance().isLoaded(dialogToCloseId)) {
            Xulor.getInstance().unload(dialogToCloseId);
        }
        if (!Xulor.getInstance().isLoaded(dialogToOpenId)) {
            Xulor.getInstance().load(dialogToOpenId, Dialogs.getDialogPath(dialogToOpenId), 1L, (short)10000);
        }
        PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", achievement, dialogToOpenId);
        Xulor.getInstance().putActionClass("wakfu.achievements", AchievementsDialogActions.class);
        WakfuSoundManager.getInstance().playGUISound(600045L);
    }
    
    public void loadQuestDescription(final AbstractQuestView questView) {
        final boolean forceOpened = WakfuGameEntity.getInstance().getLocalPlayer().hasProperty(WorldPropertyType.FOLLOW_ACHIEVEMENT_UI_FORCE_OPENED);
        if (forceOpened) {
            return;
        }
        boolean setProperty = false;
        if (!Xulor.getInstance().isLoaded("questDescriptionDialog")) {
            Xulor.getInstance().load("questDescriptionDialog", Dialogs.getDialogPath("questDescriptionDialog"), 1L, (short)10000);
            setProperty = true;
        }
        final int achievementId = questView.getId();
        final AchievementList achievementList = (questView.getType() == 1) ? AchievementListManager.INSTANCE.getListByAchievementId(achievementId) : null;
        if (achievementList == null) {
            if (this.m_achievementListView != null) {
                this.m_achievementListView = null;
                setProperty = true;
            }
        }
        else {
            if (this.m_achievementListView == null) {
                this.m_achievementListView = new AchievementListView();
                setProperty = true;
            }
            this.m_achievementListView.setAchievementList(achievementList);
        }
        if (setProperty) {
            PropertiesProvider.getInstance().setLocalPropertyValue("questListBar", this.m_achievementListView, "questDescriptionDialog");
        }
        PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", questView, "questDescriptionDialog");
    }
    
    public void loadQuestListDescription(final AbstractQuestView achievementView) {
        final int achievementId = achievementView.getId();
        final AchievementList achievementList = AchievementListManager.INSTANCE.getListByAchievementId(achievementId);
        if (achievementList == null) {
            UIAchievementsFrame.m_logger.warn((Object)("On essaye de charger une description de liste de qu$ete pour une qu\u00eate qui n'est pas dans une suite, id=" + achievementId));
            return;
        }
        boolean setProperty = false;
        if (!Xulor.getInstance().isLoaded("questListBarDialog")) {
            Xulor.getInstance().load("questListBarDialog", Dialogs.getDialogPath("questListBarDialog"), 8193L, (short)10000);
            setProperty = true;
        }
        if (this.m_achievementListView == null) {
            this.m_achievementListView = new AchievementListView();
            setProperty = true;
        }
        this.m_achievementListView.setAchievementList(achievementList);
        this.m_achievementListView.setCurrentAchievement(achievementId);
        if (setProperty) {
            PropertiesProvider.getInstance().setLocalPropertyValue("questListBar", this.m_achievementListView, "questListBarDialog");
        }
        PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", achievementView, "questListBarDialog");
    }
    
    public void unloadQuestListDescription() {
        Xulor.getInstance().unload("questListBarDialog");
        this.m_achievementListView = null;
    }
    
    public void onAchievementUnlocked(final Achievement achievement) {
        final AchievementView achievementView = AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, achievement.getId());
        if (achievementView == null) {
            return;
        }
        if (this.m_displayedAchievementView != null) {
            this.m_waitingAchievementViews.addLast(achievementView);
        }
        else {
            this.displayAchievementViewUnlocked(achievementView);
        }
        final AchievementCategoryView category = AchievementsViewManager.INSTANCE.getCategory(this.m_characterId, achievement.getCategory().getId());
        if (category != null) {
            PropertiesProvider.getInstance().firePropertyValueChanged(category, category.getFields());
        }
    }
    
    public static void setSelectedAchievement(final AchievementView achievement) {
        PropertiesProvider.getInstance().setPropertyValue("selectedAchievement", achievement);
        if (achievement != null) {
            final boolean isQuest = achievement.getRootCategoryId() == 4;
            final String dialogId = isQuest ? "questDialog" : "achievementsDialog";
            if (Xulor.getInstance().isLoaded(dialogId)) {
                PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", achievement, dialogId);
            }
        }
    }
    
    protected void displayAchievementViewUnlocked(final AchievementView achievementView) {
        this.m_displayedAchievementView = achievementView;
        PropertiesProvider.getInstance().setPropertyValue("unlockedAchievement", achievementView);
        if (!Xulor.getInstance().isLoaded("achievementUnlockedDialog") || this.m_achievementDialogContainer == null) {
            this.m_achievementDialogContainer = (Container)Xulor.getInstance().load("achievementUnlockedDialog", Dialogs.getDialogPath("achievementUnlockedDialog"), 8194L, (short)10000);
        }
        if (this.m_achievementDialogContainer != null) {
            if (this.m_particleDecorator != null) {
                this.m_achievementDialogContainer.getAppearance().destroyDecorator(this.m_particleDecorator);
            }
            (this.m_particleDecorator = new ParticleDecorator()).onCheckOut();
            this.m_particleDecorator.setFile("6001037.xps");
            this.m_particleDecorator.setAlignment(Alignment9.CENTER);
            this.m_achievementDialogContainer.getAppearance().add(this.m_particleDecorator);
        }
        this.fade(true);
    }
    
    private void initializeAppList() {
        this.m_appL = new ArrayList<ModulationColorClient>();
        final ElementMap map = this.m_achievementDialogContainer.getElementMap();
        this.m_appL.add(((Widget)map.getElement("mainContainer")).getAppearance());
        this.m_appL.add(((Widget)map.getElement("splashContainer")).getAppearance());
        this.m_appL.add(((Widget)map.getElement("text")).getAppearance());
        this.m_appL.add(((Widget)map.getElement("icon")).getAppearance());
        this.m_appL.add(((Widget)map.getElement("starIcon")).getAppearance());
        this.m_appL.add(((Widget)map.getElement("nameText")).getAppearance());
    }
    
    private void setWidgetsNonBlocking(final boolean nonBlocking) {
        final ElementMap map = this.m_achievementDialogContainer.getElementMap();
        ((Widget)map.getElement("mainContainer")).setNonBlocking(nonBlocking);
    }
    
    public void fade(final boolean fadeIn) {
        if (this.m_achievementDialogContainer == null) {
            UIAchievementsFrame.m_logger.warn((Object)"on tente de fade l'interface de d\u00e9bloquage d'exploit alors qu'elle n'est pas charg\u00e9e");
            return;
        }
        if (this.m_appL == null) {
            this.initializeAppList();
        }
        this.setWidgetsNonBlocking(!fadeIn);
        final ElementMap map = this.m_achievementDialogContainer.getElementMap();
        final EventDispatcher mainContainer = map.getElement("mainContainer");
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
            mainContainer.removeTweensOfType(ModulationColorListTween.class);
            final AbstractTween tw = new ModulationColorListTween(a, b, this.m_appL, 0, 500, 1, TweenFunction.PROGRESSIVE);
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
                                        UIAchievementsFrame.this.fade(false);
                                    }
                                }, 10000L, 1);
                                break;
                            }
                            if (!UIAchievementsFrame.this.m_waitingAchievementViews.isEmpty()) {
                                ProcessScheduler.getInstance().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        UIAchievementsFrame.this.displayAchievementViewUnlocked(UIAchievementsFrame.this.m_waitingAchievementViews.poll());
                                    }
                                }, 1000L, 1);
                                break;
                            }
                            if (UIAchievementsFrame.this.m_particleDecorator != null) {
                                UIAchievementsFrame.this.m_achievementDialogContainer.getAppearance().destroyDecorator(UIAchievementsFrame.this.m_particleDecorator);
                                UIAchievementsFrame.this.m_particleDecorator = null;
                            }
                            UIAchievementsFrame.this.m_displayedAchievementView = null;
                            PropertiesProvider.getInstance().setPropertyValue("unlockedAchievement", null);
                            break;
                        }
                    }
                }
            });
            mainContainer.addTween(tw);
        }
    }
    
    public AsbtractAchievementsView getDisplayedAchievementsView() {
        return this.m_displayedAchievementsView;
    }
    
    public AchievementFilter getCurrentFilter() {
        return this.m_currentFilter;
    }
    
    public void setCurrentFilter(final AchievementFilter currentFilter) {
        this.m_currentFilter = currentFilter;
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_displayedAchievementsView.getSelectedRootCategory(), "achievements", "categories");
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIAchievementsFrame.class);
        m_instance = new UIAchievementsFrame();
    }
    
    public enum AchievementFilter
    {
        ALL("allOf") {
            @Override
            public boolean isAuthorized(final boolean active, final boolean complete) {
                return active || complete;
            }
        }, 
        TO_DO("todo") {
            @Override
            public boolean isAuthorized(final boolean active, final boolean complete) {
                return active && !complete;
            }
        }, 
        COMPLETED("terminated.pl") {
            @Override
            public boolean isAuthorized(final boolean active, final boolean complete) {
                return complete;
            }
        };
        
        private final String m_name;
        
        private AchievementFilter(final String name) {
            this.m_name = name;
        }
        
        public abstract boolean isAuthorized(final boolean p0, final boolean p1);
        
        @Override
        public String toString() {
            return WakfuTranslator.getInstance().getString(this.m_name, UIAchievementsFrame.m_instance.isQuestDisplayed() ? 2 : 1);
        }
    }
}
