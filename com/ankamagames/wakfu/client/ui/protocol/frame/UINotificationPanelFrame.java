package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.appearance.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.util.alignment.*;

public class UINotificationPanelFrame implements MessageFrame, Runnable
{
    public static final int DEFAULT_TYPE = 0;
    public static final int QUEST_SUCCESS_TYPE = 1;
    public static final int QUEST_FAILURE_TYPE = 2;
    public static final int CYCLE_DURATION = 5000;
    protected static final Logger m_logger;
    private static final UINotificationPanelFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private LinkedList<NotificationMessageView> m_notificationMessageViews;
    private LinkedList<NotificationMessageView> m_displayedNotificationMessageViews;
    private static final int NOTIFICATION_MAX = 9;
    private static final int MOVEMENT_DURATION = 1000;
    private static final int BASE_Y = 100;
    private boolean m_moving;
    private boolean m_runAwaiting;
    private long m_lastNotificationDate;
    public ArrayList<NotificationEventListener> m_listeners;
    private NotificationMessageView m_currentMessage;
    private boolean m_ready;
    private boolean m_overLocked;
    
    public UINotificationPanelFrame() {
        super();
        this.m_notificationMessageViews = new LinkedList<NotificationMessageView>();
        this.m_displayedNotificationMessageViews = new LinkedList<NotificationMessageView>();
        this.m_listeners = new ArrayList<NotificationEventListener>();
    }
    
    public void setSelectedMessage(final NotificationMessageView notificationMessageView) {
        this.m_currentMessage = notificationMessageView;
        PropertiesProvider.getInstance().setPropertyValue("currentNotification", this.m_currentMessage);
    }
    
    public void setOverLocked(final boolean overLocked) {
        if (!(this.m_overLocked = overLocked)) {
            this.resetTimer();
        }
    }
    
    private void resetTimer() {
        this.m_lastNotificationDate = System.currentTimeMillis();
    }
    
    public static UINotificationPanelFrame getInstance() {
        return UINotificationPanelFrame.m_instance;
    }
    
    public boolean addListener(final NotificationEventListener l) {
        return !this.m_listeners.contains(l) && this.m_listeners.add(l);
    }
    
    public void removeListener(final NotificationEventListener l) {
        this.m_listeners.remove(l);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16163: {
                synchronized (UINotificationPanelFrame.m_instance) {
                    final UINotificationMessage uiMsg = (UINotificationMessage)message;
                    final NotificationMessageView view = new NotificationMessageView(uiMsg.getNotificationMessageType(), uiMsg.getTitle(), uiMsg.getText(), uiMsg.getSoundId(), uiMsg.getValue());
                    if (this.m_ready && this.displayListFilledCount() < 9) {
                        this.display(view);
                    }
                    else {
                        this.m_notificationMessageViews.addLast(view);
                    }
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("informationMessageDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UINotificationPanelFrame.this);
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("notificationPanel", this.m_displayedNotificationMessageViews.toArray());
            final Container c = (Container)Xulor.getInstance().load("informationMessageDialog", Dialogs.getDialogPath("informationMessageDialog"), 139264L, (short)10004);
            this.m_ready = false;
            c.setEnablePositionEvents(true);
            c.addEventListener(Events.REPOSITIONED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    if (c.getX() <= 0) {
                        return false;
                    }
                    UINotificationPanelFrame.this.m_ready = true;
                    while (!UINotificationPanelFrame.this.m_notificationMessageViews.isEmpty() && UINotificationPanelFrame.this.m_displayedNotificationMessageViews.size() < 9) {
                        UINotificationPanelFrame.this.display(UINotificationPanelFrame.this.m_notificationMessageViews.removeLast());
                    }
                    c.removeEventListener(Events.REPOSITIONED, this, true);
                    c.setEnablePositionEvents(false);
                    c.setVisible(false);
                    return true;
                }
            }, true);
            Xulor.getInstance().putActionClass("wakfu.notificationPanel", NotificationPanelDialogActions.class);
            this.resetTimer();
            ProcessScheduler.getInstance().schedule(this, 1000L, -1);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.clean();
            PropertiesProvider.getInstance().removeProperty("notificationPanel");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("informationMessageDialog");
            Xulor.getInstance().removeActionClass("wakfu.notificationPanel");
            ProcessScheduler.getInstance().remove(this);
        }
    }
    
    private void clean() {
        this.m_moving = false;
        this.m_runAwaiting = false;
        this.m_displayedNotificationMessageViews.clear();
        this.m_listeners.clear();
        this.m_notificationMessageViews.clear();
        ProcessScheduler.getInstance().remove(this);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    private void display(final NotificationMessageView view) {
        if (view == null) {
            return;
        }
        this.resetTimer();
        this.m_displayedNotificationMessageViews.addFirst(view);
        this.refreshNotificationViews();
        final int soundId = view.getSoundId();
        if (soundId != -1) {
            WakfuSoundManager.getInstance().playGUISound(soundId);
        }
        if (this.m_displayedNotificationMessageViews.size() > 1) {
            return;
        }
        this.setSelectedMessage(view);
        final Container container = this.getMainContainer();
        container.removeTweensOfType(NotificationPositionTween.class);
        container.setVisible(true);
        final int y = 100;
        final AbstractTween positionTween = new NotificationPositionTween(container.getY(), 100, container, 0, 1000, TweenFunction.PROGRESSIVE, true);
        positionTween.addTweenEventListener(new TweenEventListener() {
            @Override
            public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                switch (e) {
                    case TWEEN_ENDED: {
                        synchronized (UINotificationPanelFrame.m_instance) {
                            UINotificationPanelFrame.this.m_moving = false;
                            if (UINotificationPanelFrame.this.m_runAwaiting) {
                                UINotificationPanelFrame.this.run();
                            }
                            else if (!UINotificationPanelFrame.this.m_notificationMessageViews.isEmpty() && UINotificationPanelFrame.this.displayListFilledCount() < 9) {
                                UINotificationPanelFrame.this.display(UINotificationPanelFrame.this.m_notificationMessageViews.poll());
                            }
                            positionTween.removeTweenEventListener(this);
                        }
                        break;
                    }
                }
            }
        });
        container.addTween(positionTween);
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("informationMessageDialog");
        final RenderableContainer renderableContainer = (RenderableContainer)map.getElement("renderableContainer");
        final List list = (List)map.getElement("messagesList");
        new FadeLauncher(view, renderableContainer, list).run();
        this.m_moving = true;
    }
    
    private Container getMainContainer() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("informationMessageDialog");
        if (map == null) {
            return null;
        }
        return (Container)map.getElement("mainContainer");
    }
    
    private void refreshNotificationViews() {
        PropertiesProvider.getInstance().setPropertyValue("notificationPanel", null);
        PropertiesProvider.getInstance().setPropertyValue("notificationPanel", this.m_displayedNotificationMessageViews.toArray());
    }
    
    @Override
    public void run() {
        if (this.m_overLocked) {
            return;
        }
        this.run(false, null);
    }
    
    public void run(final boolean force, final NotificationMessageView messageToDelete) {
        synchronized (UINotificationPanelFrame.m_instance) {
            if (this.m_moving) {
                this.m_runAwaiting = true;
                return;
            }
            this.m_runAwaiting = false;
            if (this.m_displayedNotificationMessageViews.isEmpty()) {
                return;
            }
            final NotificationMessageView currentView = this.m_displayedNotificationMessageViews.getFirst();
            if (currentView == null) {
                return;
            }
            if (!force) {
                final long date = System.currentTimeMillis();
                if (date - this.m_lastNotificationDate < currentView.getNotificationMessageType().getDuration()) {
                    return;
                }
            }
            if (this.m_displayedNotificationMessageViews.size() == 1) {
                final Container container = this.getMainContainer();
                this.fade(false);
                container.removeTweensOfType(NotificationPositionTween.class);
                final AbstractTween positionTween = new NotificationPositionTween(container.getY(), -200, container, 0, 1000, TweenFunction.PROGRESSIVE, false);
                positionTween.addTweenEventListener(new TweenEventListener() {
                    @Override
                    public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                        switch (e) {
                            case TWEEN_ENDED: {
                                synchronized (UINotificationPanelFrame.m_instance) {
                                    UINotificationPanelFrame.this.m_moving = false;
                                    container.setY(-200);
                                    container.setVisible(false);
                                    UINotificationPanelFrame.this.m_displayedNotificationMessageViews.removeLast();
                                    UINotificationPanelFrame.this.refreshNotificationViews();
                                    if (!UINotificationPanelFrame.this.m_notificationMessageViews.isEmpty()) {
                                        final NotificationMessageView view = UINotificationPanelFrame.this.m_notificationMessageViews.poll();
                                        UINotificationPanelFrame.this.display(view);
                                    }
                                    UINotificationPanelFrame.this.fireNotificationListChanged();
                                    if (UINotificationPanelFrame.this.displayListEmpty()) {
                                        UINotificationPanelFrame.this.fireNotificationsEmpty();
                                    }
                                    positionTween.removeTweenEventListener(this);
                                }
                                break;
                            }
                        }
                    }
                });
                container.addTween(positionTween);
                this.m_moving = true;
            }
            else {
                NotificationMessageView notificationMessageView;
                if (force && messageToDelete != null) {
                    this.m_displayedNotificationMessageViews.remove(messageToDelete);
                    notificationMessageView = messageToDelete;
                }
                else {
                    notificationMessageView = this.m_displayedNotificationMessageViews.removeLast();
                }
                if (!this.m_notificationMessageViews.isEmpty()) {
                    final NotificationMessageView view = this.m_notificationMessageViews.poll();
                    this.display(view);
                }
                else {
                    this.resetTimer();
                }
                if (notificationMessageView == this.m_currentMessage) {
                    this.setSelectedMessage(this.m_displayedNotificationMessageViews.getLast());
                }
                this.refreshNotificationViews();
            }
        }
    }
    
    private void fireNotificationsEmpty() {
        for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
            this.m_listeners.get(i).onNotificationsEmpty();
        }
    }
    
    private void fireNotificationListChanged() {
        for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
            this.m_listeners.get(i).onNotificationListChanged();
        }
    }
    
    public void fade(final boolean fadeIn) {
    }
    
    public void forceWidgetsColor() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("informationMessageDialog");
        final ArrayList<ModulationColorClient> appL = new ArrayList<ModulationColorClient>();
        final RenderableContainer renderableContainer = (RenderableContainer)map.getElement("renderableContainer");
        this.fillModulationColorClientList(renderableContainer.getInnerElementMap(), appL);
        final List l = (List)map.getElement("messagesList");
        for (final RenderableContainer rc : l.getRenderables()) {
            this.fillModulationColorClientList(rc.getInnerElementMap(), appL);
        }
        for (int i = 0, size = appL.size(); i < size; ++i) {
            this.fadeWidget(true, renderableContainer, appL, true);
        }
    }
    
    private void fadeWidget(final boolean fadeIn, final Widget widget, final ArrayList<ModulationColorClient> appL, final boolean atOnce) {
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
            widget.removeTweensOfType(ModulationColorListTween.class);
            final ModulationColorListTween tw = new ModulationColorListTween(a, b, appL, 0, atOnce ? 0 : 1000, 1, TweenFunction.PROGRESSIVE);
            widget.addTween(tw);
        }
    }
    
    private void fillModulationColorClientList(final ElementMap map, final ArrayList<ModulationColorClient> appL) {
        if (map == null) {
            return;
        }
        final ElementMapIterator it = new ElementMapIterator(map);
        while (it.hasNext()) {
            final EventDispatcher next = it.next();
            if (next instanceof Widget) {
                final Widget widget = (Widget)next;
                appL.add(widget.getAppearance());
            }
        }
    }
    
    public boolean displayListEmpty() {
        return this.displayListFilledCount() == 0;
    }
    
    private int displayListFilledCount() {
        int filledCount = 0;
        for (final NotificationMessageView notificationMessageView : this.m_displayedNotificationMessageViews) {
            if (notificationMessageView != null) {
                ++filledCount;
            }
        }
        return filledCount;
    }
    
    public void forceCycle() {
        synchronized (UINotificationPanelFrame.m_instance) {
            if (this.displayListEmpty() || this.m_moving) {
                return;
            }
            this.run(true, this.m_currentMessage);
        }
    }
    
    public boolean isDisplayed(final NotificationMessageType type) {
        for (final NotificationMessageView view : this.m_displayedNotificationMessageViews) {
            if (view != null && view.getNotificationMessageType() == type) {
                return true;
            }
        }
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UINotificationPanelFrame.class);
        m_instance = new UINotificationPanelFrame();
    }
    
    private class FadeLauncher
    {
        private boolean m_isRenderableReady;
        private boolean m_isListReady;
        private final NotificationMessageView m_view;
        private final RenderableContainer m_renderableContainer;
        private final List m_list;
        
        private FadeLauncher(final NotificationMessageView view, final RenderableContainer renderableContainer, final List list) {
            super();
            this.m_view = view;
            this.m_renderableContainer = renderableContainer;
            this.m_list = list;
        }
        
        private void run() {
            RenderableContainerManager.getInstance().addListener(new RenderableContainerManager.RenderableContainerManagerListener() {
                @Override
                public void onDoneProcessing() {
                    if (FadeLauncher.this.m_renderableContainer.getItemValue() == FadeLauncher.this.m_view) {
                        if (FadeLauncher.this.m_isListReady) {
                            UINotificationPanelFrame.this.fade(true);
                        }
                        else {
                            FadeLauncher.this.m_isRenderableReady = true;
                        }
                        RenderableContainerManager.getInstance().removeListener(this);
                    }
                }
            });
            this.m_list.addListContentListener(new EditableRenderableCollection.CollectionContentLoadedListener() {
                @Override
                public void onContentLoaded() {
                    if (FadeLauncher.this.m_isRenderableReady) {
                        UINotificationPanelFrame.this.fade(true);
                    }
                    else {
                        FadeLauncher.this.m_isListReady = true;
                    }
                    FadeLauncher.this.m_list.removeListContentLoadListener(this);
                }
            });
        }
    }
    
    private static class NotificationPositionTween extends AbstractWidgetTween
    {
        private int m_yA;
        private int m_yB;
        private boolean m_visible;
        
        NotificationPositionTween(final int ya, final int yb, final Widget w, final int delay, final int duration, final TweenFunction function, final boolean visibleOnEnd) {
            super(null, null, w, delay, duration, function);
            this.m_yA = ya;
            this.m_yB = yb;
            this.m_visible = visibleOnEnd;
        }
        
        @Override
        public boolean process(final int deltaTime) {
            if (!super.process(deltaTime)) {
                return false;
            }
            if (this.m_function != null) {
                final Widget widget = this.getWidget();
                final int x = Alignment9.SOUTH.getX(widget.getWidth(), widget.getContainer().getWidth());
                final int y = (int)this.m_function.compute(this.m_yA, this.m_yB, this.m_elapsedTime, this.m_duration);
                this.getWidget().setPosition(x, y, true);
            }
            return true;
        }
        
        @Override
        public void onEnd() {
            final Widget widget = (Widget)this.m_client;
            final int x = Alignment9.SOUTH.getX(widget.getWidth(), widget.getContainer().getWidth());
            this.getWidget().setPosition(x, this.m_yB, true);
            this.getWidget().setVisible(this.m_visible);
            super.onEnd();
        }
    }
    
    public interface NotificationEventListener
    {
        void onNotificationsEmpty();
        
        void onNotificationListChanged();
    }
}
