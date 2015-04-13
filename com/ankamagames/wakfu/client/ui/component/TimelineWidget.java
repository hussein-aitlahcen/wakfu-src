package com.ankamagames.wakfu.client.ui.component;

import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.appearance.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;

public class TimelineWidget extends Container implements RenderableCollection, CharacteristicUpdateListener
{
    public static final String TIMELINE_ELEMENT_TYPE_FIELD = "timelineElementType";
    public static final int FIGHTER_TYPE = 1;
    public static final int TURN_TYPE = 2;
    public static final String TAG = "timeline";
    private static final int TWEEN_DURATION_MS = 250;
    private Timeline m_timeline;
    private ItemRendererManager m_itemRendererManager;
    private int m_lastFighterIndex;
    private ArrayList<FighterElement> m_fighters;
    private ArrayList<FighterElement> m_sortedFighters;
    private TurnElement m_turnElement;
    private HashMap<RenderableContainer, Integer> m_renderableReferences;
    private Image m_shutterImage;
    private boolean m_fighterListChanged;
    private boolean m_justResized;
    private boolean m_listUpdated;
    private boolean m_fightIsStarted;
    private ArrayList<AbstractWidgetTween> m_tweensInLine;
    private ArrayList<AbstractWidgetTween> m_tweensInLine2;
    private TimelineTurnFieldProvider m_turnProvider;
    private static int REBOUND_DURATION;
    private final Object m_mutex;
    public static final int CONTENT_HASH;
    
    public TimelineWidget() {
        super();
        this.m_renderableReferences = new HashMap<RenderableContainer, Integer>();
        this.m_fighterListChanged = false;
        this.m_justResized = false;
        this.m_listUpdated = true;
        this.m_fightIsStarted = false;
        this.m_turnProvider = new TimelineTurnFieldProvider();
        this.m_mutex = new Object();
    }
    
    @Override
    public void addFromXML(final EventDispatcher element) {
        if (element instanceof ItemRenderer) {
            this.m_itemRendererManager.addRenderer((ItemRenderer)element);
        }
        else if (element instanceof Image) {
            this.m_shutterImage = (Image)element;
        }
        super.addFromXML(element);
    }
    
    private void createTurnRenderable() {
        this.m_turnElement = new TurnElement();
        final RenderableContainer container = new RenderableContainer();
        container.onCheckOut();
        container.setNonBlocking(this.m_nonBlocking);
        container.setRendererManager(this.m_itemRendererManager);
        container.setEnableDND(false);
        container.setEnabled(this.m_enabled);
        container.setNetEnabled(this.m_netEnabled);
        this.m_turnElement.setRenderable(container);
        this.add(container);
        container.setContentProperty("timelineTurn", null);
        container.setContent(this.m_turnProvider);
        this.m_renderableReferences.put(container, 1);
    }
    
    public FighterElement addFighter(final CharacterInfo info) {
        final FighterElement fe = new FighterElement(info);
        info.getCharacteristic((CharacteristicType)FighterCharacteristicType.INIT).addListener(this);
        this.m_fighters.add(fe);
        this.m_fighterListChanged = true;
        this.setNeedsToPreProcess();
        final RenderableContainer container = new RenderableContainer();
        container.onCheckOut();
        container.setNonBlocking(this.m_nonBlocking);
        container.setRendererManager(this.m_itemRendererManager);
        container.setEnableDND(false);
        container.setEnabled(this.m_enabled);
        container.setNetEnabled(this.m_netEnabled);
        this.add(container);
        container.setContentProperty("timeline" + info.getId(), null);
        container.setContent(info);
        fe.setRenderable(container);
        this.m_renderableReferences.put(container, 1);
        this.setWidgetOnTop(this.m_shutterImage);
        return fe;
    }
    
    private void removeFighter(final FighterElement info) {
        if (this.m_fighters == null) {
            return;
        }
        synchronized (this.m_mutex) {
            this.m_fighters.remove(info);
            info.getFighter().getCharacteristic((CharacteristicType)FighterCharacteristicType.INIT).removeListener(this);
            final RenderableContainer renderable = info.getRenderable();
            final int numRef = this.m_renderableReferences.get(renderable);
            if (numRef == 1) {
                this.m_renderableReferences.remove(renderable);
                renderable.destroySelfFromParent();
            }
            else {
                this.m_renderableReferences.put(renderable, numRef - 1);
            }
            this.m_fighterListChanged = true;
            this.setNeedsToPreProcess();
        }
    }
    
    public void removeFighter(final CharacterInfo info) {
        if (this.m_fighters == null) {
            return;
        }
        synchronized (this.m_mutex) {
            for (int i = this.m_fighters.size() - 1; i >= 0; --i) {
                final FighterElement element = this.m_fighters.get(i);
                if (element.getFighter() == info) {
                    this.m_fighterListChanged = true;
                    this.setNeedsToPreProcess();
                    this.m_sortedFighters.remove(element);
                    this.m_fighters.remove(i);
                    info.getCharacteristic((CharacteristicType)FighterCharacteristicType.INIT).removeListener(this);
                    final RenderableContainer renderable = element.getRenderable();
                    final int numRef = this.m_renderableReferences.get(renderable);
                    if (numRef == 1) {
                        this.m_renderableReferences.remove(renderable);
                        renderable.destroySelfFromParent();
                    }
                    else {
                        this.m_renderableReferences.put(renderable, numRef - 1);
                    }
                }
            }
        }
    }
    
    @Override
    public String getTag() {
        return "timeline";
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return true;
    }
    
    @Override
    public Widget getWidget(final String type, final int index) {
        if (type == null) {
            return null;
        }
        return null;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0, size = this.m_fighters.size(); i < size; ++i) {
            this.m_fighters.get(i).getRenderable().setEnabled(enabled);
        }
    }
    
    @Override
    public void setNetEnabled(final boolean enabled) {
        super.setNetEnabled(enabled);
        for (int i = 0, size = this.m_fighters.size(); i < size; ++i) {
            this.m_fighters.get(i).getRenderable().setNetEnabled(enabled);
        }
    }
    
    public void setContent(final Timeline timeline) {
        if (this.m_timeline == timeline) {
            return;
        }
        this.m_timeline = timeline;
        if (this.m_timeline != null) {
            this.m_timeline.setTimelineWidget(this);
        }
        this.updateList(FightEvent.SET_CONTENT);
        this.m_fighterListChanged = true;
        this.setNeedsToPreProcess();
        this.setNeedsToPostProcess();
    }
    
    @Override
    public Widget getWidget(final int x, final int y) {
        if (this.m_unloading || !this.m_visible || !this.getAppearance().insideInsets(x, y)) {
            return null;
        }
        return super.getWidget(x, y);
    }
    
    private FighterElement getFighterElement(final CharacterInfo info, int n) {
        for (int i = 0, size = this.m_fighters.size(); i < size; ++i) {
            final FighterElement fighterElement = this.m_fighters.get(i);
            if (fighterElement.getFighter() == info) {
                if (n == 0) {
                    return fighterElement;
                }
                --n;
            }
        }
        return null;
    }
    
    private void updateList(final FightEvent event) {
        if (this.m_timeline == null) {
            return;
        }
        final CharacterInfo currentFighter = this.m_timeline.getCurrentOrFirstFighter();
        final List<CharacterInfo> thisTurn = this.m_timeline.getOrderedFightersThisTurn();
        final int currentFighterIndex = thisTurn.indexOf(currentFighter);
        if (currentFighterIndex == -1) {
            return;
        }
        final List<CharacterInfo> nextTurn = this.m_timeline.getOrderedFightersNextTurn();
        final List<CharacterInfo> toAdd = this.m_timeline.getOrderedFightersNextTurn();
        final ArrayList<CharacterInfo> added = new ArrayList<CharacterInfo>();
        this.m_lastFighterIndex = thisTurn.size() - currentFighterIndex - 1;
        this.m_sortedFighters = new ArrayList<FighterElement>();
        for (int i = currentFighterIndex, size = thisTurn.size(); i < size; ++i) {
            final CharacterInfo info = thisTurn.get(i);
            toAdd.remove(info);
            FighterElement fe = this.getFighterElement(info, 0);
            if (fe == null) {
                fe = this.addFighter(info);
            }
            added.add(info);
            this.m_sortedFighters.add(fe);
        }
        for (int i = 0, size = nextTurn.size(); i < size && (toAdd.size() != 0 || i < nextTurn.indexOf(currentFighter)); ++i) {
            final CharacterInfo info = nextTurn.get(i);
            toAdd.remove(info);
            FighterElement fe = this.getFighterElement(info, added.contains(info) ? 1 : 0);
            if (fe == null) {
                fe = this.addFighter(info);
            }
            added.add(info);
            this.m_sortedFighters.add(fe);
        }
        final int s = this.m_fighters.size();
        for (int j = s - 1; j >= 0; --j) {
            final FighterElement fe2 = this.m_fighters.get(j);
            if (!this.m_sortedFighters.contains(fe2)) {
                this.removeFighter(fe2);
            }
        }
        this.m_listUpdated = true;
    }
    
    public void onCurrentFighterChange() {
    }
    
    public void onFighterAdded(final CharacterInfo info) {
        this.updateList(FighterFightEvent.FIGHTER_ADDED.setFighter(info));
        this.m_fighterListChanged = true;
        this.setNeedsToPreProcess();
        this.setNeedsToPostProcess();
    }
    
    public void onFighterRemoved(final CharacterInfo info) {
        this.removeFighter(info);
        this.updateList(FighterFightEvent.FIGHTER_REMOVED.setFighter(info));
        this.m_fighterListChanged = true;
        this.setNeedsToPreProcess();
        this.setNeedsToPostProcess();
    }
    
    public void onStartAction() {
    }
    
    public void onNewTableTurn() {
        this.m_fightIsStarted = true;
        this.m_turnProvider.setTurn(this.m_timeline.getCurrentTableturn());
    }
    
    public void onTurnEnded(final CharacterInfo info) {
    }
    
    public void onTurnStarted(final CharacterInfo info) {
        this.updateList(FightEvent.TURN_STARTED);
        this.m_fighterListChanged = true;
        this.setNeedsToPreProcess();
        this.setNeedsToPostProcess();
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        final FighterCharacteristic fightCharac = (FighterCharacteristic)charac;
        if (fightCharac.getType() == FighterCharacteristicType.INIT && !this.m_fightIsStarted) {
            this.m_timeline.sortInitially();
            this.m_timeline.displayCurrentFighter();
            this.updateList(FightEvent.TURN_STARTED);
            this.m_fighterListChanged = true;
            this.setNeedsToPreProcess();
            this.setNeedsToPostProcess();
        }
    }
    
    public RenderableContainer getCellRenderable(final CharacterInfo info) {
        for (int i = 0, size = this.m_fighters.size(); i < size; ++i) {
            final FighterElement element = this.m_fighters.get(i);
            if (element.getFighter() == info) {
                return element.getRenderable();
            }
        }
        return null;
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_fighterListChanged) {
            this.invalidateMinSize();
        }
        return ret;
    }
    
    @Override
    public void onChildrenAdded() {
        super.onChildrenAdded();
        this.setNeedsToResetMeshes();
        this.createTurnRenderable();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        if (this.m_fighters != null) {
            for (int i = 0, size = this.m_fighters.size(); i < size; ++i) {
                final FighterCharacteristic init = this.m_fighters.get(i).getFighter().getCharacteristic((CharacteristicType)FighterCharacteristicType.INIT);
                init.removeListener(this);
            }
            this.m_fighters.clear();
            this.m_fighters = null;
        }
        this.m_sortedFighters = null;
        this.m_tweensInLine = null;
        this.m_tweensInLine2 = null;
        this.m_shutterImage = null;
        this.m_renderableReferences.clear();
        this.m_timeline = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_fightIsStarted = false;
        this.m_tweensInLine = new ArrayList<AbstractWidgetTween>();
        this.m_tweensInLine2 = new ArrayList<AbstractWidgetTween>();
        this.m_itemRendererManager = new ItemRendererManager();
        this.m_fighters = new ArrayList<FighterElement>();
        this.m_invalidateOnMinSizeChange = true;
        this.m_justResized = false;
        this.m_listUpdated = false;
        this.m_turnProvider.setTurn(1);
        this.setNonBlocking(true);
        final TimelineLayoutManager layout = new TimelineLayoutManager();
        layout.onCheckOut();
        this.add(layout);
        this.m_enableResizeEvents = true;
        this.addEventListener(Events.RESIZED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (event.getTarget() == TimelineWidget.this) {
                    TimelineWidget.this.m_justResized = true;
                }
                return false;
            }
        }, false);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == TimelineWidget.CONTENT_HASH) {
            this.setContent((Timeline)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        TimelineWidget.REBOUND_DURATION = 100;
        CONTENT_HASH = "content".hashCode();
    }
    
    private class TimelineFirstRenderableTweenA extends AbstractWidgetTween
    {
        private RenderableContainer m_renderableContainer;
        private int m_xA;
        private int m_yA;
        private int m_xB;
        private int m_yB;
        private int m_xC;
        private int m_yC;
        private int m_xD;
        private int m_yD;
        private int m_width;
        private int m_height;
        private RenderableContainer m_renderableContainerTE;
        private int m_xA2;
        private int m_yA2;
        private int m_xB2;
        private int m_yB2;
        private int m_xC2;
        private int m_yC2;
        private int m_xD2;
        private int m_yD2;
        private int m_width2;
        private int m_height2;
        private int m_shutterImageX;
        private boolean m_firstProcess;
        
        private TimelineFirstRenderableTweenA(final TweenFunction function) {
            super();
            this.m_firstProcess = true;
            this.setDelay(0);
            this.setDuration(250);
            this.setRepeat(1);
            this.setTweenFunction(function);
        }
        
        public void setRenderable(final RenderableContainer renderableContainer, final int xA, final int yA, final int xB, final int yB, final int xC, final int yC, final int xD, final int yD, final int width, final int height) {
            this.m_renderableContainer = renderableContainer;
            this.m_xA = xA;
            this.m_xB = xB;
            this.m_xC = xC;
            this.m_xD = xD;
            this.m_yA = yA;
            this.m_yB = yB;
            this.m_yC = yC;
            this.m_yD = yD;
            this.m_width = width;
            this.m_height = height;
            final int numRef = TimelineWidget.this.m_renderableReferences.get(this.m_renderableContainer);
            TimelineWidget.this.m_renderableReferences.put(this.m_renderableContainer, numRef + 1);
        }
        
        public void setTurnEnd(final RenderableContainer renderableContainer, final int xA, final int yA, final int xB, final int yB, final int xC, final int yC, final int xD, final int yD, final int width, final int height) {
            this.m_renderableContainerTE = renderableContainer;
            this.m_xA2 = xA;
            this.m_xB2 = xB;
            this.m_xC2 = xC;
            this.m_xD2 = xD;
            this.m_yA2 = yA;
            this.m_yB2 = yB;
            this.m_yC2 = yC;
            this.m_yD2 = yD;
            this.m_width2 = width;
            this.m_height2 = height;
            final int numRef = TimelineWidget.this.m_renderableReferences.get(this.m_renderableContainerTE);
            TimelineWidget.this.m_renderableReferences.put(this.m_renderableContainerTE, numRef + 1);
        }
        
        public boolean isInit() {
            return this.m_renderableContainer != null;
        }
        
        @Override
        public boolean process(final int deltaTime) {
            if (!super.process(deltaTime)) {
                return false;
            }
            if (this.m_function != null) {
                final int contentWidth = TimelineWidget.this.getAppearance().getContentWidth();
                if (this.m_firstProcess) {
                    this.m_shutterImageX = TimelineWidget.this.m_shutterImage.getX() - contentWidth;
                    this.m_firstProcess = false;
                }
                if (this.m_elapsedTime < this.m_duration / 2) {
                    final int x = (int)this.m_function.compute(this.m_xA, this.m_xB, this.m_elapsedTime, this.m_duration / 2);
                    final int y = (int)this.m_function.compute(this.m_yA, this.m_yB, this.m_elapsedTime, this.m_duration / 2);
                    this.m_renderableContainer.setPosition(x + contentWidth, y);
                    if (this.m_renderableContainerTE != null) {
                        final int x2 = (int)this.m_function.compute(this.m_xA2, this.m_xB2, this.m_elapsedTime, this.m_duration / 2);
                        final int y2 = (int)this.m_function.compute(this.m_yA2, this.m_yB2, this.m_elapsedTime, this.m_duration / 2);
                        this.m_renderableContainerTE.setPosition(x2 + contentWidth, y2);
                    }
                }
                else {
                    final int x = (int)this.m_function.compute(this.m_xC, this.m_xD, this.m_elapsedTime - this.m_duration / 2, this.m_duration / 2);
                    final int y = (int)this.m_function.compute(this.m_yC, this.m_yD, this.m_elapsedTime - this.m_duration / 2, this.m_duration / 2);
                    this.m_renderableContainer.setPosition(x + contentWidth, y);
                    if (this.m_renderableContainerTE != null) {
                        final int x2 = (int)this.m_function.compute(this.m_xC2, this.m_xD2, this.m_elapsedTime - this.m_duration / 2, this.m_duration / 2);
                        final int y2 = (int)this.m_function.compute(this.m_yC2, this.m_yD2, this.m_elapsedTime - this.m_duration / 2, this.m_duration / 2);
                        this.m_renderableContainerTE.setPosition(x2 + contentWidth, y2);
                    }
                }
                final int shutterX = (int)this.m_function.compute(this.m_shutterImageX, this.m_shutterImageX + 72, this.m_elapsedTime, this.m_duration);
                TimelineWidget.this.m_shutterImage.setX(shutterX + contentWidth);
            }
            return true;
        }
        
        @Override
        public void onEnd() {
            final int contentWidth = TimelineWidget.this.getAppearance().getContentWidth();
            this.m_renderableContainer.setPosition(this.m_xD + contentWidth, this.m_yD);
            this.m_renderableContainer.setSize(this.m_width, this.m_height);
            TimelineWidget.this.m_shutterImage.setX(this.m_shutterImageX + contentWidth);
            int numRef = TimelineWidget.this.m_renderableReferences.get(this.m_renderableContainer);
            if (numRef == 1) {
                TimelineWidget.this.m_renderableReferences.remove(this.m_renderableContainer);
                this.m_renderableContainer.destroySelfFromParent();
            }
            else {
                TimelineWidget.this.m_renderableReferences.put(this.m_renderableContainer, numRef - 1);
            }
            if (this.m_renderableContainerTE != null) {
                this.m_renderableContainerTE.setPosition(this.m_xD2 + contentWidth, this.m_yD2);
                this.m_renderableContainerTE.setSize(this.m_width2, this.m_height2);
                numRef = TimelineWidget.this.m_renderableReferences.get(this.m_renderableContainerTE);
                if (numRef == 1) {
                    TimelineWidget.this.m_renderableReferences.remove(this.m_renderableContainerTE);
                    this.m_renderableContainerTE.destroySelfFromParent();
                }
                else {
                    TimelineWidget.this.m_renderableReferences.put(this.m_renderableContainerTE, numRef - 1);
                }
            }
            super.onEnd();
        }
    }
    
    private class TimelineRenderableTween extends AbstractWidgetTween
    {
        private ArrayList<RenderableContainer> m_renderables;
        private int[] m_positionsA;
        private int[] m_positionsB;
        private int[] m_sizesA;
        private int[] m_sizesB;
        private boolean m_firstProcess;
        
        private TimelineRenderableTween(final ArrayList<RenderableContainer> elements, final int[] posA, final int[] posB, final int[] sizesA, final int[] sizesB, final TweenFunction function) {
            super();
            this.m_firstProcess = true;
            this.setDelay(250);
            this.setDuration(250);
            this.setRepeat(1);
            this.setTweenFunction(function);
            this.m_renderables = elements;
            this.m_positionsA = posA;
            this.m_positionsB = posB;
            this.m_sizesA = sizesA;
            this.m_sizesB = sizesB;
            for (int i = 0, size = this.m_renderables.size(); i < size; ++i) {
                final RenderableContainer rc = this.m_renderables.get(i);
                final int numRef = TimelineWidget.this.m_renderableReferences.get(rc);
                TimelineWidget.this.m_renderableReferences.put(rc, numRef + 1);
            }
        }
        
        private int getNumRenderables() {
            return this.m_renderables.size();
        }
        
        @Override
        public boolean process(final int deltaTime) {
            if (!super.process(deltaTime)) {
                return false;
            }
            if (this.m_function != null) {
                final int contentWidth = TimelineWidget.this.getAppearance().getContentWidth();
                final int travelDuration = this.m_duration - TimelineWidget.REBOUND_DURATION;
                if (this.m_elapsedTime < travelDuration) {
                    for (int i = 0, size = this.m_renderables.size(); i < size; ++i) {
                        final RenderableContainer renderable = this.m_renderables.get(i);
                        if (this.m_firstProcess) {
                            this.m_firstProcess = false;
                        }
                        final int x = (int)this.m_function.compute(this.m_positionsA[i * 2], this.m_positionsB[i * 2], this.m_elapsedTime, travelDuration);
                        final int y = (int)this.m_function.compute(this.m_positionsA[i * 2 + 1], this.m_positionsB[i * 2 + 1], this.m_elapsedTime, travelDuration);
                        final int width = (int)this.m_function.compute(this.m_sizesA[i * 2], this.m_sizesB[i * 2], this.m_elapsedTime, travelDuration);
                        final int height = (int)this.m_function.compute(this.m_sizesA[i * 2 + 1], this.m_sizesB[i * 2 + 1], this.m_elapsedTime, travelDuration);
                        renderable.setPosition(x + contentWidth, y);
                        renderable.setSize(width, height);
                    }
                }
                else {
                    final int elapsedTime = this.m_elapsedTime - travelDuration;
                    int deltaY = 0;
                    if (elapsedTime < 40) {
                        deltaY = (int)this.m_function.compute(0.0f, 10.0f, elapsedTime, 40);
                    }
                    else if (elapsedTime < 80) {
                        deltaY = (int)this.m_function.compute(10.0f, 0.0f, elapsedTime - 40, 40);
                    }
                    else if (elapsedTime < 90) {
                        deltaY = (int)this.m_function.compute(0.0f, 4.0f, elapsedTime - 80, 10);
                    }
                    else if (elapsedTime < TimelineWidget.REBOUND_DURATION) {
                        deltaY = (int)this.m_function.compute(4.0f, 0.0f, elapsedTime - 90, 10);
                    }
                    for (int j = 0, size2 = this.m_renderables.size(); j < size2; ++j) {
                        if (j >= this.m_positionsB.length * 2) {
                            break;
                        }
                        final RenderableContainer renderable2 = this.m_renderables.get(j);
                        renderable2.setY(this.m_positionsB[j * 2 + 1] + deltaY);
                    }
                }
                this.m_firstProcess = false;
            }
            return true;
        }
        
        @Override
        public void onEnd() {
            final int contentWidth = TimelineWidget.this.getAppearance().getContentWidth();
            for (int i = 0, size = this.m_renderables.size(); i < size; ++i) {
                final RenderableContainer renderable = this.m_renderables.get(i);
                if (i >= this.m_positionsB.length * 2) {
                    break;
                }
                renderable.setPosition(this.m_positionsB[i * 2] + contentWidth, this.m_positionsB[i * 2 + 1]);
                renderable.setSize(this.m_sizesB[i * 2], this.m_sizesB[i * 2 + 1]);
                final int numRef = TimelineWidget.this.m_renderableReferences.get(renderable);
                if (numRef == 1) {
                    TimelineWidget.this.m_renderableReferences.remove(renderable);
                    renderable.destroySelfFromParent();
                }
                else {
                    TimelineWidget.this.m_renderableReferences.put(renderable, numRef - 1);
                }
            }
            if (TimelineWidget.this.m_tweensInLine.size() > 0) {
                TimelineWidget.this.addTween(TimelineWidget.this.m_tweensInLine.remove(0));
            }
            if (TimelineWidget.this.m_tweensInLine2.size() > 0) {
                TimelineWidget.this.addTween(TimelineWidget.this.m_tweensInLine2.remove(0));
            }
            super.onEnd();
        }
    }
    
    public class TimelineLayoutManager extends AbstractLayoutManager
    {
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            return new Dimension();
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            return TimelineWidget.this.getPrefSize();
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            if (!TimelineWidget.this.m_justResized && !TimelineWidget.this.m_listUpdated) {
                return;
            }
            synchronized (TimelineWidget.this.m_mutex) {
                final boolean listUpdated = TimelineWidget.this.m_listUpdated;
                TimelineWidget.this.m_justResized = false;
                TimelineWidget.this.m_listUpdated = false;
                if (TimelineWidget.this.m_sortedFighters == null) {
                    return;
                }
                final int contentWidth = parent.getAppearance().getContentWidth();
                final int contentHeight = parent.getAppearance().getContentHeight();
                if (TimelineWidget.this.m_shutterImage != null) {
                    TimelineWidget.this.m_shutterImage.setSizeToPrefSize();
                    TimelineWidget.this.m_shutterImage.setPosition(contentWidth - TimelineWidget.this.m_shutterImage.getWidth() - 54, 0);
                }
                final int[] posA = new int[(TimelineWidget.this.m_sortedFighters.size() + 1) * 2];
                final int[] sizesA = new int[(TimelineWidget.this.m_sortedFighters.size() + 1) * 2];
                final int[] posB = new int[(TimelineWidget.this.m_sortedFighters.size() + 1) * 2];
                final int[] sizesB = new int[(TimelineWidget.this.m_sortedFighters.size() + 1) * 2];
                int y = 5;
                boolean useTweenOnce = false;
                TimelineFirstRenderableTweenA tweenA = null;
                for (int i = 0, size = TimelineWidget.this.m_sortedFighters.size() + 1; i < size; ++i) {
                    int index = i;
                    final boolean turnEnd = i == TimelineWidget.this.m_lastFighterIndex + 1;
                    if (i > TimelineWidget.this.m_lastFighterIndex + 1) {
                        --index;
                    }
                    int x = 0;
                    int previousX = 0;
                    int previousY = 0;
                    int previousWidth = 0;
                    int previousHeight = 0;
                    int width = 0;
                    int height = 0;
                    switch (i) {
                        case 0: {
                            x = 5;
                            previousX = 7;
                            previousY = 59;
                            previousWidth = 101;
                            previousHeight = 35;
                            width = 113;
                            height = 47;
                            break;
                        }
                        case 1: {
                            x = 7;
                            previousX = 7;
                            previousY = 94;
                            previousWidth = 96;
                            previousHeight = 31;
                            width = 101;
                            height = 35;
                            break;
                        }
                        case 2: {
                            x = 7;
                            previousX = 7;
                            previousY = 125;
                            previousWidth = 91;
                            previousHeight = 26;
                            width = 96;
                            height = 31;
                            break;
                        }
                        default: {
                            x = 7;
                            previousX = 7;
                            previousY = 125 + 26 * (i - 2);
                            width = (previousWidth = 91);
                            height = (previousHeight = 26);
                            break;
                        }
                    }
                    if ((i == size - 2 && i == TimelineWidget.this.m_lastFighterIndex) || (i == size - 1 && !turnEnd)) {
                        previousX = 5;
                        previousY = 5;
                        previousWidth = 113;
                        previousHeight = 47;
                    }
                    if (i == size - 1 && turnEnd) {
                        previousX = 7;
                        previousY = 59;
                        previousWidth = 101;
                        previousHeight = 35;
                    }
                    x -= width;
                    RenderableContainer renderable;
                    if (!turnEnd) {
                        renderable = TimelineWidget.this.m_sortedFighters.get(index).getRenderable();
                    }
                    else {
                        renderable = TimelineWidget.this.m_turnElement.getRenderable();
                    }
                    posB[i * 2] = x;
                    posB[i * 2 + 1] = y;
                    sizesB[i * 2] = width;
                    sizesB[i * 2 + 1] = height;
                    boolean useTween = false;
                    if (!listUpdated || renderable.getWidth() == 0) {
                        posA[i * 2] = posB[i * 2];
                        posA[i * 2 + 1] = posB[i * 2 + 1];
                        sizesA[i * 2] = sizesB[i * 2];
                        sizesA[i * 2 + 1] = sizesB[i * 2 + 1];
                        renderable.setPosition(contentWidth + posA[i * 2], posA[i * 2 + 1]);
                        renderable.setSize(sizesA[i * 2], sizesA[i * 2 + 1]);
                    }
                    else {
                        posA[i * 2] = previousX - previousWidth;
                        posA[i * 2 + 1] = previousY;
                        sizesA[i * 2] = previousWidth;
                        sizesA[i * 2 + 1] = previousHeight;
                        useTween = true;
                        useTweenOnce = true;
                    }
                    y += height;
                    if (i == 0) {
                        y += 7;
                    }
                    if (useTween) {
                        if ((i == size - 2 && i == TimelineWidget.this.m_lastFighterIndex) || (i == size - 1 && !turnEnd) || (i == size - 1 && turnEnd)) {
                            final int xA = -108;
                            final int yA = previousY;
                            final int xB = xA + 100;
                            final int yB = yA;
                            final int xC = xB;
                            final int yC = contentHeight - height;
                            final int xD = posB[i * 2];
                            final int yD = yC;
                            final int finalWidth = sizesB[i * 2];
                            final int finalHeight = sizesB[i * 2 + 1];
                            if (tweenA == null) {
                                tweenA = new TimelineFirstRenderableTweenA(TweenFunction.PROGRESSIVE);
                            }
                            if (turnEnd) {
                                tweenA.setTurnEnd(renderable, xA, yA, xB, yB, xC, yC, xD, yD, finalWidth, finalHeight);
                            }
                            else {
                                tweenA.setRenderable(renderable, xA, yA, xB, yB, xC, yC, xD, yD, finalWidth, finalHeight);
                            }
                            posA[i * 2] = xC;
                            posA[i * 2 + 1] = yC;
                            sizesA[i * 2] = finalWidth;
                            sizesA[i * 2 + 1] = finalHeight;
                        }
                    }
                }
                if (!useTweenOnce) {
                    return;
                }
                if (tweenA != null && tweenA.isInit()) {
                    if (TimelineWidget.this.hasTweensOfType(TimelineFirstRenderableTweenA.class)) {
                        TimelineWidget.this.m_tweensInLine.add(tweenA);
                    }
                    else {
                        TimelineWidget.this.addTween(tweenA);
                    }
                }
                final ArrayList<RenderableContainer> sorted = new ArrayList<RenderableContainer>(TimelineWidget.this.m_sortedFighters.size() + 1);
                int j;
                for (j = 0; j <= TimelineWidget.this.m_lastFighterIndex; ++j) {
                    sorted.add(TimelineWidget.this.m_sortedFighters.get(j).getRenderable());
                }
                sorted.add(TimelineWidget.this.m_turnElement.getRenderable());
                while (j < TimelineWidget.this.m_sortedFighters.size()) {
                    sorted.add(TimelineWidget.this.m_sortedFighters.get(j).getRenderable());
                    ++j;
                }
                final TimelineRenderableTween tweenB = new TimelineRenderableTween((ArrayList)sorted, posA, posB, sizesA, sizesB, TweenFunction.PROGRESSIVE);
                if (TimelineWidget.this.hasTweensOfType(TimelineRenderableTween.class)) {
                    TimelineWidget.this.m_tweensInLine2.add(tweenB);
                }
                else {
                    TimelineWidget.this.addTween(tweenB);
                }
            }
        }
    }
    
    private enum FightEventType
    {
        TURN_STARTED, 
        FIGHTER_ADDED, 
        FIGHTER_REMOVED, 
        SET_CONTENT;
    }
    
    private static class FightEvent
    {
        private static final FightEvent TURN_STARTED;
        private static final FightEvent SET_CONTENT;
        private FightEventType m_type;
        
        private FightEvent(final FightEventType type) {
            super();
            this.m_type = type;
        }
        
        public FightEventType getType() {
            return this.m_type;
        }
        
        static {
            TURN_STARTED = new FightEvent(FightEventType.TURN_STARTED);
            SET_CONTENT = new FightEvent(FightEventType.SET_CONTENT);
        }
    }
    
    private static class FighterFightEvent extends FightEvent
    {
        private static final FighterFightEvent FIGHTER_ADDED;
        private static final FighterFightEvent FIGHTER_REMOVED;
        private CharacterInfo m_fighter;
        
        private FighterFightEvent(final FightEventType type) {
            super(type);
        }
        
        private FighterFightEvent(final FightEventType type, final CharacterInfo fighter) {
            super(type);
            this.m_fighter = fighter;
        }
        
        public FighterFightEvent setFighter(final CharacterInfo fighter) {
            this.m_fighter = fighter;
            return this;
        }
        
        public CharacterInfo getFighter() {
            return this.m_fighter;
        }
        
        static {
            FIGHTER_ADDED = new FighterFightEvent(FightEventType.FIGHTER_ADDED);
            FIGHTER_REMOVED = new FighterFightEvent(FightEventType.FIGHTER_REMOVED);
        }
    }
    
    private abstract static class TimelineElement
    {
        private RenderableContainer m_renderable;
        
        public RenderableContainer getRenderable() {
            return this.m_renderable;
        }
        
        public void setRenderable(final RenderableContainer renderable) {
            this.m_renderable = renderable;
        }
        
        public abstract int getType();
    }
    
    private static class FighterElement extends TimelineElement
    {
        CharacterInfo m_fighter;
        
        private FighterElement(final CharacterInfo fighter) {
            super();
            this.m_fighter = fighter;
        }
        
        public CharacterInfo getFighter() {
            return this.m_fighter;
        }
        
        public void setFighter(final CharacterInfo fighter) {
            this.m_fighter = fighter;
        }
        
        @Override
        public int getType() {
            return 1;
        }
    }
    
    private static class TurnElement extends TimelineElement
    {
        int m_turn;
        
        public int getTurn() {
            return this.m_turn;
        }
        
        public void setTurn(final int turn) {
            this.m_turn = turn;
        }
        
        @Override
        public int getType() {
            return 2;
        }
    }
}
