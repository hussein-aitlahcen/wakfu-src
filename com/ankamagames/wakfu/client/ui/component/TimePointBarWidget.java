package com.ankamagames.wakfu.client.ui.component;

import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.ui.component.appearance.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.xulor2.layout.*;
import gnu.trove.*;

public class TimePointBarWidget extends Container implements RenderableCollection
{
    public static final String TAG = "timePointBar";
    private static final String LABEL_WIDGETS = "labels";
    private static final String POINTS_WIDGETS = "points";
    private static final String AVATAR_WIDGETS = "avatar";
    public static final String TURN_LABEL = "turnLabel";
    public static final String TURN_ARROW = "turnArrow";
    public static final String POINT_ITEM_RENDERER = "pointItemRenderer";
    public static final String AVATAR_ITEM_RENDERER = "avatarItemRenderer";
    public static final String TIME_POINT_DESCRIPTION_POPUP = "timePointDescriptionPopup";
    public static final String TIME_POINT_DESCRIPTION_POPUP_RENDERABLE = "timePointDescriptionPopupRenderable";
    private Timeline m_timeline;
    private TimePointEffect m_timePointEffect;
    private PopupElement m_timePointDescriptionPopup;
    private ItemRendererManager m_itemRendererManager;
    private ItemRendererManager m_avatarItemRendererManager;
    private ArrayList<FighterFieldProvider> m_fighters;
    private ArrayList<FighterFieldProvider> m_sortedFighters;
    private ArrayList<RenderableContainer> m_pointRenderables;
    private ArrayList<RenderableContainer> m_avatarRenderables;
    private ArrayList<Label> m_turnLabels;
    private Image m_turnImage;
    private Label m_turnLabelTemplate;
    private boolean m_fighterListChanged;
    private int[] m_activeTurns;
    private int[] m_activeTurnsFirstIndexes;
    private int[] m_activeTurnsSizes;
    private int[] m_displayedTurns;
    private int[] m_previousDisplayedTurns;
    private int[] m_displayedTurnsFirstIndexes;
    private int[] m_displayedTurnsSizes;
    private int m_maxTimeScore;
    private boolean m_isBeingLayouted;
    public static final int CONTENT_HASH;
    
    public TimePointBarWidget() {
        super();
        this.m_fighterListChanged = false;
        this.m_isBeingLayouted = false;
    }
    
    public static int getAdjustedX(final RenderableContainer c, int x, final int y, final int startOffset, final ArrayList<RenderableContainer> avatarRenderables, final CharacterInfo currentFighter) {
        int count = 0;
        boolean collides;
    Label_0127:
        do {
            int i = startOffset - 1;
            while (i >= 0) {
                final RenderableContainer rc = avatarRenderables.get(i);
                final CharacterInfo ffp = (CharacterInfo)rc.getItemValue();
                if (rc.getX() == x && (rc.getY() >= y || rc.getY() + rc.getHeight() >= y) && (y >= rc.getY() || y + c.getHeight() >= rc.getY())) {
                    collides = true;
                    x -= c.getWidth();
                    if (ffp == currentFighter) {
                        x -= c.getWidth();
                        continue Label_0127;
                    }
                    continue Label_0127;
                }
                else {
                    --i;
                }
            }
            collides = false;
        } while (collides && count++ < 20);
        return x;
    }
    
    @Override
    public void addFromXML(final EventDispatcher element) {
        if (element instanceof Label && "turnLabel".equals(element.getId())) {
            this.m_turnLabelTemplate = (Label)element;
        }
        else if (element instanceof Image && "turnArrow".equals(element.getId())) {
            (this.m_turnImage = (Image)element).setVisible(false);
            super.addFromXML(element);
        }
        else if (element instanceof PopupElement && "timePointDescriptionPopup".equals(element.getId())) {
            this.m_timePointDescriptionPopup = (PopupElement)element;
        }
        else if (element instanceof ItemRenderer && "avatarItemRenderer".equals(element.getId())) {
            this.m_avatarItemRendererManager.addRenderer((ItemRenderer)element);
        }
        else if (element instanceof ItemRenderer && "pointItemRenderer".equals(element.getId())) {
            this.m_itemRendererManager.addRenderer((ItemRenderer)element);
        }
        else {
            super.addFromXML(element);
        }
    }
    
    public void addFighter(final int index, final CharacterInfo info) {
        this.m_fighters.add(index, info.getFighterFieldProvider());
        this.m_sortedFighters.add(info.getFighterFieldProvider());
        this.m_fighterListChanged = true;
        this.setNeedsToPreProcess();
        if (this.m_pointRenderables.size() < this.m_fighters.size()) {
            final RenderableContainer container = new RenderableContainer();
            container.onCheckOut();
            container.setNonBlocking(this.m_nonBlocking);
            container.setRendererManager(this.m_itemRendererManager);
            container.setEnableDND(false);
            container.setEnabled(this.m_enabled);
            container.setNetEnabled(this.m_netEnabled);
            this.m_pointRenderables.add(index, container);
            this.add(container);
            container.setContentProperty("timePointBar" + info.getId(), null);
            container.setContent(info);
            final RenderableContainer avatarContainer = new RenderableContainer();
            avatarContainer.onCheckOut();
            avatarContainer.setNonBlocking(this.m_nonBlocking);
            avatarContainer.setRendererManager(this.m_avatarItemRendererManager);
            avatarContainer.setEnableDND(false);
            avatarContainer.setEnabled(this.m_enabled);
            avatarContainer.setNetEnabled(this.m_netEnabled);
            this.m_avatarRenderables.add(index, avatarContainer);
            this.add(avatarContainer);
            avatarContainer.setContentProperty("timePointBar" + info.getId(), null);
            avatarContainer.setContent(info);
        }
    }
    
    public void removeFighter(final CharacterInfo info) {
        final int index = this.getFighterIndex(info);
        if (index == -1) {
            return;
        }
        this.m_sortedFighters.remove(this.m_fighters.remove(index));
        this.m_pointRenderables.remove(index).destroySelfFromParent();
        this.m_avatarRenderables.remove(index).destroySelfFromParent();
        this.m_fighterListChanged = true;
        this.setNeedsToPreProcess();
    }
    
    private int getFighterIndex(final CharacterInfo info) {
        for (int i = 0, size = this.m_fighters.size(); i < size; ++i) {
            if (this.m_fighters.get(i).getFighter() == info) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public String getTag() {
        return "timePointBar";
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
        if (type.equals("labels")) {
            if (index >= 0 && index < this.m_turnLabels.size()) {
                return this.m_turnLabels.get(index);
            }
            return null;
        }
        else if (type.equals("avatar")) {
            if (index >= 0 && index < this.m_avatarRenderables.size()) {
                return this.m_avatarRenderables.get(index);
            }
            return null;
        }
        else {
            if (!type.equals("points")) {
                return null;
            }
            if (index >= 0 && index < this.m_pointRenderables.size()) {
                return this.m_pointRenderables.get(index);
            }
            return null;
        }
    }
    
    private TimePointBarDecorator getDecorator() {
        final ArrayList<EventDispatcher> children = this.getAppearance().getChildren();
        for (int i = children.size() - 1; i >= 0; --i) {
            final EventDispatcher child = children.get(i);
            if (child instanceof TimePointBarDecorator) {
                return (TimePointBarDecorator)child;
            }
        }
        return null;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0, size = this.m_pointRenderables.size(); i < size; ++i) {
            this.m_pointRenderables.get(i).setEnabled(enabled);
            this.m_avatarRenderables.get(i).setEnabled(enabled);
        }
    }
    
    @Override
    public void setNetEnabled(final boolean enabled) {
        super.setNetEnabled(enabled);
        for (int i = 0, size = this.m_pointRenderables.size(); i < size; ++i) {
            this.m_pointRenderables.get(i).setNetEnabled(enabled);
            this.m_avatarRenderables.get(i).setNetEnabled(enabled);
        }
    }
    
    public void setContent(final Timeline timeline) {
        if (this.m_timeline == timeline) {
            return;
        }
        (this.m_timeline = timeline).setTimePointBarWidget(this);
        this.m_maxTimeScore = this.m_timeline.getTimeScoreGauges().getTimePointGap();
        this.m_fighterListChanged = true;
        this.setNeedsToPreProcess();
    }
    
    @Override
    public Widget getWidget(int x, int y) {
        if (this.m_unloading || !this.m_visible || !this.getAppearance().insideInsets(x, y)) {
            return null;
        }
        Widget found = super.getWidget(x, y);
        if (found == this || found == null) {
            x -= this.getAppearance().getLeftInset();
            y -= this.getAppearance().getBottomInset();
            final int decoHeight = this.getDecorator().getMesh().getDoubleBubblePixmap().getHeight();
            final int decoWidth = this.getDecorator().getMesh().getDoubleBubblePixmap().getWidth();
            final int deltaX = this.getAppearance().getContentWidth() - decoWidth;
            final int[] pixelsSeparations = this.getDecorator().getPixelSeparations();
            if (pixelsSeparations != null) {
                for (int i = 0; i < pixelsSeparations.length; ++i) {
                    final int separation = pixelsSeparations[i];
                    if (x > deltaX && x < decoWidth + deltaX && y > separation - decoHeight && y < separation) {
                        found = this.m_turnLabels.get(i);
                        break;
                    }
                }
            }
        }
        return found;
    }
    
    public void addTimeScoreToFighter(final FighterFieldProvider fighter, final int score) {
        this.m_fighterListChanged = true;
        this.setNeedsToPreProcess();
    }
    
    private void updateList() {
        if (this.m_timeline == null) {
            return;
        }
        final List<CharacterInfo> orderedFightersThisTurn = this.m_timeline.getOrderedFightersThisTurn();
        final int s = orderedFightersThisTurn.size();
        for (int i = s - 1; i >= 0; --i) {
            final CharacterInfo info = orderedFightersThisTurn.get(i);
            final int index = this.getFighterIndex(info);
            if (index == -1) {
                this.addFighter(s - i - 1, info);
            }
        }
        for (int i = 0, size = this.m_fighters.size(); i < size; ++i) {
            final CharacterInfo info2 = this.m_fighters.get(i).getFighter();
            if (!orderedFightersThisTurn.contains(info2)) {
                this.removeFighter(info2);
            }
        }
    }
    
    public void onCurrentFighterChange() {
    }
    
    public void onFighterAdded(final CharacterInfo info) {
    }
    
    public void onFighterRemoved(final CharacterInfo info) {
        this.removeFighter(info);
    }
    
    public void onStartAction() {
        this.m_turnImage.setVisible(true);
    }
    
    public void onNewTableTurn() {
        this.m_maxTimeScore = this.m_timeline.getTimePointGap();
        this.updateList();
        this.m_fighterListChanged = true;
        this.setNeedsToPreProcess();
        this.setNeedsToPostProcess();
    }
    
    public void onTurnEnded(final CharacterInfo info) {
    }
    
    public void onTurnStarted(final CharacterInfo info) {
        this.m_fighterListChanged = true;
        this.setNeedsToPreProcess();
    }
    
    private void computeActiveTurns() {
        Collections.sort(this.m_sortedFighters, CharacterComparator.COMPARATOR);
        final TIntHashSet set = new TIntHashSet();
        for (int i = this.m_fighters.size() - 1; i >= 0; --i) {
            final FighterFieldProvider fighter = this.m_fighters.get(i);
            set.add(fighter.getTimePointTurn());
        }
        final int[] activeTurns = set.toArray();
        Arrays.sort(activeTurns);
        this.m_activeTurns = activeTurns;
        int turn = -1;
        int index = -1;
        this.m_activeTurnsFirstIndexes = new int[this.m_activeTurns.length];
        this.m_activeTurnsSizes = new int[this.m_activeTurns.length];
        for (int j = 0, size = this.m_sortedFighters.size(); j < size; ++j) {
            final FighterFieldProvider fighter2 = this.m_sortedFighters.get(j);
            if (fighter2.getTimePointTurn() != turn) {
                ++index;
                turn = fighter2.getTimePointTurn();
                this.m_activeTurnsFirstIndexes[index] = j;
            }
            final int[] activeTurnsSizes = this.m_activeTurnsSizes;
            final int n = index;
            ++activeTurnsSizes[n];
        }
        this.m_previousDisplayedTurns = this.m_displayedTurns;
        if (this.m_activeTurns.length != 0) {
            int numGroups = 1;
            for (int k = 0, size2 = this.m_activeTurns.length - 1; k < size2; ++k) {
                if (this.m_activeTurns[k] + 1 != this.m_activeTurns[k + 1]) {
                    ++numGroups;
                }
            }
            this.m_displayedTurns = new int[numGroups + this.m_activeTurns.length];
            this.m_displayedTurnsFirstIndexes = new int[numGroups + this.m_activeTurns.length];
            this.m_displayedTurnsSizes = new int[numGroups + this.m_activeTurns.length];
            int displayedIndex = 0;
            for (int l = 0, size3 = this.m_activeTurns.length; l < size3; ++l) {
                this.m_displayedTurns[displayedIndex] = this.m_activeTurns[l];
                this.m_displayedTurnsFirstIndexes[displayedIndex] = this.m_activeTurnsFirstIndexes[l];
                this.m_displayedTurnsSizes[displayedIndex] = this.m_activeTurnsSizes[l];
                if (this.m_activeTurns.length == l + 1 || this.m_activeTurns[l] + 1 != this.m_activeTurns[l + 1]) {
                    ++displayedIndex;
                    this.m_displayedTurns[displayedIndex] = this.m_activeTurns[l] + 1;
                    this.m_displayedTurnsFirstIndexes[displayedIndex] = -1;
                    this.m_displayedTurnsSizes[displayedIndex] = 0;
                }
                ++displayedIndex;
            }
        }
        else {
            this.m_displayedTurns = new int[0];
            this.m_displayedTurnsFirstIndexes = new int[0];
            this.m_displayedTurnsSizes = new int[0];
        }
        final int numLabels = Math.max(this.m_displayedTurns.length, this.m_previousDisplayedTurns.length);
        while (this.m_turnLabels.size() > numLabels) {
            this.m_turnLabels.remove(this.m_turnLabels.size() - 1).destroySelfFromParent();
        }
        boolean added = false;
        while (this.m_turnLabels.size() < numLabels) {
            final Label m = (Label)this.m_turnLabelTemplate.cloneElementStructure();
            m.setNonBlocking(true);
            final TextWidgetAppearance app = (TextWidgetAppearance)m.getAppearance();
            app.setAlign(Alignment9.CENTER);
            this.add(m);
            this.m_turnLabels.add(m);
            added = true;
        }
        if (added) {
            for (int l = this.m_turnLabels.size() - 1; l >= 0; --l) {
                this.m_widgetChildren.remove(this.m_turnLabels.get(l));
            }
            for (int l = this.m_turnLabels.size() - 1; l >= 0; --l) {
                this.m_widgetChildren.add(0, this.m_turnLabels.get(l));
            }
        }
        this.m_fighterListChanged = false;
    }
    
    private int getDisplayedTurnsNumber() {
        if (this.m_activeTurns == null || this.m_activeTurns.length == 0) {
            return 0;
        }
        int numGroups = 1;
        for (int i = 0, size = this.m_activeTurns.length - 1; i < size; ++i) {
            if (this.m_activeTurns[i] + 1 != this.m_activeTurns[i + 1]) {
                ++numGroups;
            }
        }
        return this.m_activeTurns.length + numGroups;
    }
    
    private Dimension computeMinSize() {
        final TimePointBarDecorator pointBarDecorator = this.getDecorator();
        final TimePointBarDecoratorMesh mesh = pointBarDecorator.getMesh();
        int height = 0;
        int width = 0;
        width = Math.max(width, mesh.getBorderBubblePixmap().getWidth());
        height += mesh.getBorderBubblePixmap().getHeight();
        width = Math.max(width, mesh.getLinkPixmap().getWidth());
        final int numTurns = this.getDisplayedTurnsNumber();
        width = Math.max(width, mesh.getDoubleBubblePixmap().getWidth());
        height += mesh.getDoubleBubblePixmap().getHeight() * numTurns;
        width = Math.max(width, mesh.getOppositeBorderBubblePixmap().getWidth());
        height += mesh.getOppositeBorderBubblePixmap().getHeight();
        return new Dimension(width, height);
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_fighterListChanged) {
            this.computeActiveTurns();
            this.invalidateMinSize();
        }
        return ret;
    }
    
    @Override
    public void onChildrenAdded() {
        super.onChildrenAdded();
        this.setNeedsToResetMeshes();
        PropertiesProvider.getInstance().setPropertyValue("fight.describedTimePointBonus", this.m_timePointEffect);
        this.addEventListener(Events.MOUSE_ENTERED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final EventDispatcher target = event.getTarget();
                final int index = TimePointBarWidget.this.m_turnLabels.indexOf(target);
                if (index != -1) {
                    final Label label = TimePointBarWidget.this.m_turnLabels.get(index);
                    final int turnIndex = TimePointBarWidget.this.m_displayedTurns[index] - 1;
                    XulorActions.popup(TimePointBarWidget.this.m_timePointDescriptionPopup, label);
                }
                return false;
            }
        }, true);
        this.addEventListener(Events.MOUSE_EXITED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final EventDispatcher target = event.getTarget();
                final int index = TimePointBarWidget.this.m_turnLabels.indexOf(target);
                if (index != -1) {
                    XulorActions.closePopup(null);
                }
                return false;
            }
        }, true);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_itemRendererManager = null;
        this.m_pointRenderables.clear();
        this.m_pointRenderables = null;
        this.m_timePointEffect = null;
        this.m_sortedFighters.clear();
        this.m_sortedFighters = null;
        this.m_fighters.clear();
        this.m_fighters = null;
        this.m_turnLabels.clear();
        this.m_turnLabels = null;
        this.m_timeline = null;
        this.m_turnImage = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_invalidateOnMinSizeChange = true;
        this.m_itemRendererManager = new ItemRendererManager();
        this.m_avatarItemRendererManager = new ItemRendererManager();
        this.m_timePointEffect = new TimePointEffect(null, false);
        this.m_fighters = new ArrayList<FighterFieldProvider>();
        this.m_sortedFighters = new ArrayList<FighterFieldProvider>();
        this.m_avatarRenderables = new ArrayList<RenderableContainer>();
        this.m_pointRenderables = new ArrayList<RenderableContainer>();
        this.m_turnLabels = new ArrayList<Label>();
        this.m_displayedTurns = new int[0];
        this.m_previousDisplayedTurns = new int[0];
        this.setNonBlocking(this.m_isBeingLayouted = false);
        final StaticLayout sl = new StaticLayout();
        sl.onCheckOut();
        sl.setAdaptToContentSize(true);
        this.add(sl);
        final TimePointBarLayoutManager lm = new TimePointBarLayoutManager();
        lm.onCheckOut();
        this.add(lm);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == TimePointBarWidget.CONTENT_HASH) {
            this.setContent((Timeline)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        CONTENT_HASH = "content".hashCode();
    }
    
    public class PixelIntervalTween extends AbstractTween<int[]>
    {
        private float[] m_alphasA;
        private float[] m_alphasB;
        private float[] m_finalAlpha;
        private int[] m_previousDisplayedTurns;
        private int[] m_displayedTurns;
        private int[] m_finalSeparations;
        private CharacterInfo m_currentFighter;
        
        public PixelIntervalTween(final int[] a, final int[] b, final int[] finalSeparations, final float[] alphasA, final float[] alphasB, final float[] finalAlpha, final int[] displayedTurns, final int[] previousDisplayedTurns, final TimePointBarWidget w, final int delay, final int duration, final int repeat, final TweenFunction function) {
            super();
            this.setA(a);
            this.setB(b);
            this.setTweenClient(w);
            this.setDelay(delay);
            this.setDuration(duration);
            this.setRepeat(repeat);
            this.setTweenFunction(function);
            this.m_alphasA = alphasA;
            this.m_alphasB = alphasB;
            this.m_finalAlpha = finalAlpha;
            this.m_displayedTurns = displayedTurns;
            this.m_previousDisplayedTurns = previousDisplayedTurns;
            this.m_finalSeparations = finalSeparations;
            this.m_currentFighter = TimePointBarWidget.this.m_timeline.getCurrentOrFirstFighter();
            TimePointBarWidget.this.m_isBeingLayouted = true;
        }
        
        @Override
        public boolean process(final int deltaTime) {
            if (!super.process(deltaTime)) {
                return false;
            }
            if (this.m_function != null) {
                final int height = TimePointBarWidget.this.getDecorator().getMesh().getDoubleBubblePixmap().getHeight();
                final int[] oldPixelIntervals = (Object)this.m_a;
                final int[] newPixelIntervals = (Object)this.m_b;
                final int bubbleWidth = TimePointBarWidget.this.getDecorator().getMesh().getDoubleBubblePixmap().getWidth();
                final int deltaX = TimePointBarWidget.this.getAppearance().getContentWidth() - bubbleWidth;
                final float[] alphas = new float[this.m_alphasA.length];
                final int[] pixelInterval = new int[oldPixelIntervals.length];
                for (int i = 0; i < oldPixelIntervals.length; ++i) {
                    pixelInterval[i] = (int)this.m_function.compute(oldPixelIntervals[i], newPixelIntervals[i], this.m_elapsedTime, this.m_duration);
                    alphas[i] = this.m_function.compute(this.m_alphasA[i], this.m_alphasB[i], this.m_elapsedTime, this.m_duration);
                }
                for (int i = 0; i < pixelInterval.length && i < TimePointBarWidget.this.m_turnLabels.size(); ++i) {
                    final Label label = TimePointBarWidget.this.m_turnLabels.get(i);
                    final int deltaY = Alignment9.CENTER.getY(label.getHeight(), height) - height;
                    label.setPosition(Alignment9.CENTER.getX(label.getWidth(), bubbleWidth) + 6 + deltaX, pixelInterval[i] + deltaY);
                }
                final int junctionHeight = TimePointBarWidget.this.getDecorator().getMesh().getDoubleBubblePixmap().getHeight();
                for (int j = 0, size = TimePointBarWidget.this.m_pointRenderables.size(); j < size; ++j) {
                    final RenderableContainer renderableContainer = TimePointBarWidget.this.m_pointRenderables.get(j);
                    final FighterFieldProvider fighter = ((CharacterInfo)renderableContainer.getItemValue()).getFighterFieldProvider();
                    final int pointTurn = (fighter != null) ? fighter.getTimePointTurn() : 1;
                    final int score = (fighter != null) ? fighter.getTimeScore() : 0;
                    int turnIndex = 0;
                    for (final int displayedTurn : this.m_previousDisplayedTurns) {
                        if (displayedTurn == pointTurn) {
                            break;
                        }
                        ++turnIndex;
                    }
                    final int renderableAlignY = Alignment9.CENTER.getY(renderableContainer.getHeight(), junctionHeight);
                    int startY;
                    if (pixelInterval.length == 0) {
                        startY = 0;
                    }
                    else if (turnIndex == pixelInterval.length) {
                        startY = pixelInterval[pixelInterval.length - 1] - junctionHeight + renderableAlignY;
                    }
                    else {
                        startY = pixelInterval[turnIndex] - junctionHeight + renderableAlignY;
                    }
                    int deltaY2 = 0;
                    if (turnIndex + 1 < pixelInterval.length) {
                        final int turnSize = pixelInterval[turnIndex + 1] - pixelInterval[turnIndex] - (int)(junctionHeight * 0.5f);
                        deltaY2 = score / TimePointBarWidget.this.m_maxTimeScore * turnSize;
                    }
                    renderableContainer.setPosition(12 + deltaX, startY + deltaY2);
                }
                for (int j = 0, size = TimePointBarWidget.this.m_pointRenderables.size(); j < size; ++j) {
                    final RenderableContainer renderableContainer = TimePointBarWidget.this.m_pointRenderables.get(j);
                    final RenderableContainer arc = TimePointBarWidget.this.m_avatarRenderables.get(j);
                    int x = renderableContainer.getX() - renderableContainer.getWidth();
                    final int y = renderableContainer.getY();
                    x = TimePointBarWidget.getAdjustedX(arc, x, y, j, TimePointBarWidget.this.m_avatarRenderables, this.m_currentFighter);
                    arc.setPosition(x, y);
                    if (arc.getItemValue() == this.m_currentFighter) {
                        TimePointBarWidget.this.m_turnImage.setPosition(x - TimePointBarWidget.this.m_turnImage.getWidth(), y);
                    }
                }
                TimePointBarWidget.this.getDecorator().setPixelSeparations(pixelInterval, this.m_previousDisplayedTurns, alphas);
            }
            return true;
        }
        
        @Override
        public void onEnd() {
            final int[] b = this.m_finalSeparations;
            final int height = TimePointBarWidget.this.getDecorator().getMesh().getDoubleBubblePixmap().getHeight();
            final int width = TimePointBarWidget.this.getDecorator().getMesh().getDoubleBubblePixmap().getWidth();
            final int deltaX = TimePointBarWidget.this.getAppearance().getContentWidth() - width;
            TimePointBarWidget.this.getDecorator().setPixelSeparations(b, this.m_displayedTurns, this.m_finalAlpha);
            for (int i = 0; i < b.length && i < TimePointBarWidget.this.m_turnLabels.size(); ++i) {
                final Label label = TimePointBarWidget.this.m_turnLabels.get(i);
                final int deltaY = Alignment9.CENTER.getY(label.getHeight(), height) - height;
                label.setPosition(Alignment9.CENTER.getX(label.getWidth(), width) + 6 + deltaX, b[i] + deltaY);
            }
            final int junctionHeight = TimePointBarWidget.this.getDecorator().getMesh().getDoubleBubblePixmap().getHeight();
            for (int j = 0, size = TimePointBarWidget.this.m_pointRenderables.size(); j < size; ++j) {
                final RenderableContainer renderableContainer = TimePointBarWidget.this.m_pointRenderables.get(j);
                final FighterFieldProvider fighter = ((CharacterInfo)renderableContainer.getItemValue()).getFighterFieldProvider();
                final int pointTurn = (fighter != null) ? fighter.getTimePointTurn() : 1;
                final int score = (fighter != null) ? fighter.getTimeScore() : 0;
                final int renderableAlignY = Alignment9.CENTER.getY(renderableContainer.getHeight(), junctionHeight);
                int turnIndex = 0;
                for (final int displayedTurn : this.m_displayedTurns) {
                    if (displayedTurn == pointTurn) {
                        break;
                    }
                    ++turnIndex;
                }
                int startY = -junctionHeight + renderableAlignY;
                if (turnIndex < b.length) {
                    startY += b[turnIndex];
                }
                int deltaY2 = 0;
                if (turnIndex + 1 < b.length) {
                    final int turnSize = b[turnIndex + 1] - b[turnIndex] - (int)(junctionHeight * 0.5f);
                    deltaY2 = score / TimePointBarWidget.this.m_maxTimeScore * turnSize;
                }
                renderableContainer.setPosition(12 + deltaX, startY + deltaY2);
            }
            for (int j = 0, size = TimePointBarWidget.this.m_pointRenderables.size(); j < size; ++j) {
                final RenderableContainer renderableContainer = TimePointBarWidget.this.m_pointRenderables.get(j);
                final RenderableContainer arc = TimePointBarWidget.this.m_avatarRenderables.get(j);
                int x = renderableContainer.getX() - renderableContainer.getWidth();
                final int y = renderableContainer.getY();
                x = TimePointBarWidget.getAdjustedX(arc, x, y, j, TimePointBarWidget.this.m_avatarRenderables, this.m_currentFighter);
                arc.setPosition(x, y);
                if (arc.getItemValue() == this.m_currentFighter) {
                    TimePointBarWidget.this.m_turnImage.setPosition(x - TimePointBarWidget.this.m_turnImage.getWidth(), y);
                }
            }
            TimePointBarWidget.this.computeActiveTurns();
            TimePointBarWidget.this.m_isBeingLayouted = false;
            super.onEnd();
        }
    }
    
    public static class CharacterComparator implements Comparator<FighterFieldProvider>
    {
        private static CharacterComparator COMPARATOR;
        
        @Override
        public int compare(final FighterFieldProvider o1, final FighterFieldProvider o2) {
            final int turnDelta = o1.getTimePointTurn() - o2.getTimePointTurn();
            if (turnDelta != 0) {
                return turnDelta;
            }
            return o1.getTimeScore() - o2.getTimeScore();
        }
        
        static {
            CharacterComparator.COMPARATOR = new CharacterComparator();
        }
    }
    
    public static class RenderableContainerCharacterComparator implements Comparator<RenderableContainer>
    {
        private static RenderableContainerCharacterComparator COMPARATOR;
        
        @Override
        public int compare(final RenderableContainer o1, final RenderableContainer o2) {
            final CharacterInfo ffp1 = (CharacterInfo)o1.getItemValue();
            final CharacterInfo ffp2 = (CharacterInfo)o2.getItemValue();
            return ffp1.getCharacteristicValue(FighterCharacteristicType.INIT) - ffp2.getCharacteristicValue(FighterCharacteristicType.INIT);
        }
        
        static {
            RenderableContainerCharacterComparator.COMPARATOR = new RenderableContainerCharacterComparator();
        }
    }
    
    public static class FightersComparator implements Comparator<FighterFieldProvider>
    {
        private static FightersComparator COMPARATOR;
        
        @Override
        public int compare(final FighterFieldProvider o1, final FighterFieldProvider o2) {
            final CharacterInfo ffp1 = o1.getFighter();
            final CharacterInfo ffp2 = o2.getFighter();
            return ffp1.getCharacteristicValue(FighterCharacteristicType.INIT) - ffp2.getCharacteristicValue(FighterCharacteristicType.INIT);
        }
        
        static {
            FightersComparator.COMPARATOR = new FightersComparator();
        }
    }
    
    public class TimePointBarLayoutManager extends AbstractLayoutManager
    {
        @Override
        public Dimension getContentMinSize(final Container container) {
            return this.getContentPreferedSize(container);
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            final Dimension dimension = TimePointBarWidget.this.computeMinSize();
            dimension.setHeight(dimension.height + TimePointBarWidget.this.m_fighters.size() * 40);
            return dimension;
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            if (TimePointBarWidget.this.m_isBeingLayouted) {
                return;
            }
            final Dimension minSize = TimePointBarWidget.this.computeMinSize();
            final int availableHeight = parent.getAppearance().getContentHeight() - minSize.height;
            final int availableWidth = parent.getAppearance().getContentWidth();
            if (TimePointBarWidget.this.m_displayedTurns != null && TimePointBarWidget.this.m_displayedTurns.length != 0) {
                final int turnsNumber = TimePointBarWidget.this.m_displayedTurns.length;
                final int[] separations = new int[turnsNumber];
                final TimePointBarDecoratorMesh mesh = TimePointBarWidget.this.getDecorator().getMesh();
                final int heightPerFighter = (int)Math.floor(availableHeight / TimePointBarWidget.this.m_fighters.size());
                int startY = mesh.getBorderBubblePixmap().getHeight();
                for (int i = 0; i < TimePointBarWidget.this.m_displayedTurns.length; ++i) {
                    startY += mesh.getDoubleBubblePixmap().getHeight();
                    if (i != 0) {
                        startY += TimePointBarWidget.this.m_displayedTurnsSizes[i - 1] * heightPerFighter;
                    }
                    separations[i] = startY;
                }
                int[] previousSeparationsA = TimePointBarWidget.this.getDecorator().getPixelSeparations();
                int[] previousSeparationsB = new int[previousSeparationsA.length];
                final TIntArrayList displayedTurnList = new TIntArrayList();
                final TIntArrayList previousSeparationAList = new TIntArrayList();
                final TIntArrayList previousSeparationBList = new TIntArrayList();
                final TFloatArrayList alphasAList = new TFloatArrayList();
                final TFloatArrayList alphasBList = new TFloatArrayList();
                float[] alphasA = new float[previousSeparationsA.length];
                float[] alphasB = new float[previousSeparationsA.length];
                int newIndex = 0;
                int oldIndex = 0;
                int deleteOffset = 0;
                final int addOffset = TimePointBarWidget.this.getAppearance().getContentHeight() + mesh.getDoubleBubblePixmap().getHeight();
                while (oldIndex < TimePointBarWidget.this.m_previousDisplayedTurns.length || newIndex < TimePointBarWidget.this.m_displayedTurns.length) {
                    final int oldDisplayedTurn = (TimePointBarWidget.this.m_previousDisplayedTurns.length > oldIndex && oldIndex >= 0) ? TimePointBarWidget.this.m_previousDisplayedTurns[oldIndex] : 0;
                    final int newDisplayedTurn = (TimePointBarWidget.this.m_displayedTurns.length > newIndex && newIndex >= 0) ? TimePointBarWidget.this.m_displayedTurns[newIndex] : 0;
                    if (newIndex >= TimePointBarWidget.this.m_displayedTurns.length || (oldIndex < TimePointBarWidget.this.m_previousDisplayedTurns.length && oldDisplayedTurn < newDisplayedTurn)) {
                        if (previousSeparationsA.length <= oldIndex) {
                            ++oldIndex;
                        }
                        else {
                            displayedTurnList.add(oldDisplayedTurn);
                            previousSeparationAList.add(previousSeparationsA[oldIndex]);
                            previousSeparationBList.add(deleteOffset);
                            alphasAList.add(1.0f);
                            alphasBList.add(0.0f);
                            ++oldIndex;
                        }
                    }
                    else if (oldIndex >= TimePointBarWidget.this.m_previousDisplayedTurns.length || (newIndex < TimePointBarWidget.this.m_displayedTurns.length && oldDisplayedTurn > newDisplayedTurn)) {
                        if (separations.length <= newIndex) {
                            ++newIndex;
                        }
                        else {
                            deleteOffset = separations[newIndex];
                            displayedTurnList.add(newDisplayedTurn);
                            previousSeparationAList.add(separations[newIndex]);
                            previousSeparationBList.add(separations[newIndex]);
                            alphasAList.add(0.0f);
                            alphasBList.add(1.0f);
                            ++newIndex;
                        }
                    }
                    else if (previousSeparationsA.length <= oldIndex || separations.length <= newIndex) {
                        ++newIndex;
                        ++oldIndex;
                    }
                    else {
                        displayedTurnList.add(newDisplayedTurn);
                        previousSeparationAList.add(previousSeparationsA[oldIndex]);
                        previousSeparationBList.add(separations[newIndex]);
                        alphasAList.add(1.0f);
                        alphasBList.add(1.0f);
                        deleteOffset = separations[newIndex];
                        ++oldIndex;
                        ++newIndex;
                    }
                }
                previousSeparationsA = previousSeparationAList.toNativeArray();
                previousSeparationsB = previousSeparationBList.toNativeArray();
                alphasA = alphasAList.toNativeArray();
                alphasB = alphasBList.toNativeArray();
                for (int j = 0, size = TimePointBarWidget.this.m_pointRenderables.size(); j < size; ++j) {
                    final RenderableContainer renderableContainer = TimePointBarWidget.this.m_pointRenderables.get(j);
                    renderableContainer.setSizeToPrefSize();
                }
                for (int j = 0, size = TimePointBarWidget.this.m_avatarRenderables.size(); j < size; ++j) {
                    final RenderableContainer renderableContainer = TimePointBarWidget.this.m_avatarRenderables.get(j);
                    renderableContainer.setSizeToPrefSize();
                }
                for (int j = 0; j < TimePointBarWidget.this.m_displayedTurns.length; ++j) {
                    final Label l = TimePointBarWidget.this.m_turnLabels.get(j);
                    l.setText(String.valueOf(TimePointBarWidget.this.m_displayedTurns[j] + 1));
                    l.setSizeToPrefSize();
                }
                TimePointBarWidget.this.m_turnImage.setSizeToPrefSize();
                final float[] alphas = new float[separations.length];
                Arrays.fill(alphas, 1.0f);
                TimePointBarWidget.this.removeTweensOfType(PixelIntervalTween.class);
                final PixelIntervalTween tw = new PixelIntervalTween(previousSeparationsA, previousSeparationsB, separations, alphasA, alphasB, alphas, TimePointBarWidget.this.m_displayedTurns, displayedTurnList.toNativeArray(), TimePointBarWidget.this, 0, 300, 1, TweenFunction.PROGRESSIVE);
                TimePointBarWidget.this.addTween(tw);
            }
        }
    }
}
