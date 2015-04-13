package com.ankamagames.xulor2.component;

import java.awt.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.event.*;
import java.util.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.image.*;

public class TabbedContainer extends Container implements TabItem.TabItemVisibilityListener
{
    public static final String TAG = "TabbedContainer";
    private static final String TAB_ELEMENT_TAG = "tab";
    private static final String SEPARATOR_ELEMENT_TAG = "separator";
    private static final String BACKGROUND_ELEMENT_TAG = "backgroundTabsContainer";
    private static final String INCREASE_BUTTON_ELEMENT_TAG = "increaseButton";
    private static final String DECREASE_BUTTON_ELEMENT_TAG = "decreaseButton";
    private HashMap<RadioButton, TabItem> m_content;
    private ArrayList<RadioButton> m_tabs;
    private Container m_dataContainer;
    private Container m_backgroundTabsContainer;
    private Image m_separator;
    private Container m_tabsContainer;
    private RadioButton m_selectedTab;
    private Rectangle m_tabsDisplaybleSpace;
    private double m_verticalFittings;
    private Alignment4 m_tabsPosition;
    private Alignment5 m_tabsAlignment;
    private Alignment4 m_pixmapAlignment;
    private Alignment9 m_tabsLabelAlignment;
    private Orientation m_textOrientation;
    private RadioGroup m_tabsRadioGroup;
    private RadioButton m_radioButtonHaveToBeEntierlyDisplayed;
    private boolean m_scrollButtonsPositionedBeforeTabs;
    private boolean m_scrollButtonsNearby;
    private Button m_increaseButton;
    private Button m_decreaseButton;
    private int m_firstVisibleTabIndex;
    private boolean m_scrollNeeded;
    private boolean m_dirty;
    private boolean m_static;
    private boolean m_tabsSizesEquilibrate;
    private int m_tabXOffset;
    private int m_tabYOffset;
    public static final String RADIO_GROUP_PREFIX_ID = "tabsRadiogGroup";
    public static int m_radioGroupId;
    public static final int PIXMAP_ALIGNMENT_HASH;
    public static final int SCROLL_BUTTONS_NEARBY_HASH;
    public static final int SCROLL_BUTTONS_POSITIONED_BEFORE_TABS_HASH;
    public static final int SELECTED_TAB_INDEX_HASH;
    public static final int TABS_ALIGNMENT_HASH;
    public static final int TABS_LABEL_ALIGNMENT_HASH;
    public static final int TABS_POSITION_HASH;
    public static final int TABS_SIZES_EQUILIBRATE_HASH;
    public static final int TEXT_ORIENTATION_HASH;
    public static final int TABS_X_OFFSET_HASH;
    public static final int TABS_Y_OFFSET_HASH;
    
    public TabbedContainer() {
        super();
        this.m_tabsDisplaybleSpace = new Rectangle();
        this.m_verticalFittings = 0.0;
        this.m_pixmapAlignment = Alignment4.WEST;
        this.m_tabsLabelAlignment = Alignment9.CENTER;
        this.m_textOrientation = Orientation.EAST;
        this.m_scrollButtonsPositionedBeforeTabs = false;
        this.m_scrollButtonsNearby = false;
        this.m_firstVisibleTabIndex = 0;
        this.m_scrollNeeded = false;
        this.m_dirty = true;
        this.m_static = true;
        this.m_tabsSizesEquilibrate = false;
        this.m_tabXOffset = 0;
        this.m_tabYOffset = 0;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
        if (e instanceof TabItem) {
            final TabItem item = (TabItem)e;
            final RadioButton button = new RadioButton();
            button.onCheckOut();
            button.setElementMap(this.m_elementMap);
            button.setGroupId(this.m_tabsRadioGroup.getId());
            button.setValue(Integer.toString(this.m_content.size()));
            button.setTextOrientation(this.m_textOrientation);
            button.setPixmapAlign(this.m_pixmapAlignment);
            button.setNeedsScissor(true);
            button.setChildrenAdded(true);
            button.setClickSoundId(-2);
            button.setOverrideClickSound(false);
            button.setStyle("TabbedContainer" + this.getStyle() + "$" + "tab" + this.m_tabsPosition, true);
            item.setButton(button);
            item.addTabItemVisibilityListener(this);
            button.addEventListener(Events.MOUSE_PRESSED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    if (button != TabbedContainer.this.m_selectedTab) {
                        TabbedContainer.this.setSelectedTab(button);
                    }
                    return false;
                }
            }, false);
            this.m_content.put(button, item);
            this.m_tabs.add(button);
            this.m_tabsContainer.add(button);
        }
    }
    
    @Override
    public String getTag() {
        return "TabbedContainer";
    }
    
    @Override
    public void setElementMap(final ElementMap map) {
        super.setElementMap(map);
        this.m_tabsRadioGroup.setElementMap(map);
    }
    
    public boolean isScrollButtonsNearby() {
        return this.m_scrollButtonsNearby;
    }
    
    public void setScrollButtonsNearby(final boolean scrollButtonsNearby) {
        this.m_scrollButtonsNearby = scrollButtonsNearby;
        this.m_dirty = true;
        this.setNeedsToPreProcess();
    }
    
    public boolean isScrollButtonsPositionedBeforeTabs() {
        return this.m_scrollButtonsPositionedBeforeTabs;
    }
    
    public void setScrollButtonsPositionedBeforeTabs(final boolean scrollButtonsPositionedBeforeTabs) {
        this.m_scrollButtonsPositionedBeforeTabs = scrollButtonsPositionedBeforeTabs;
        this.m_dirty = true;
        this.setNeedsToPreProcess();
    }
    
    public Alignment4 getTabsPosition() {
        return this.m_tabsPosition;
    }
    
    public void setTabsSizesEquilibrate(final boolean tabsSizesEquilibrate) {
        this.m_tabsSizesEquilibrate = tabsSizesEquilibrate;
    }
    
    public int getTabXOffset() {
        return this.m_tabXOffset;
    }
    
    public void setTabsXOffset(final int tabXOffset) {
        this.m_tabXOffset = tabXOffset;
    }
    
    public int getTabYOffset() {
        return this.m_tabYOffset;
    }
    
    public void setTabsYOffset(final int tabYOffset) {
        this.m_tabYOffset = tabYOffset;
    }
    
    public void setTabsPosition(final Alignment4 tabsPosition) {
        final RowLayout rowLayout = (RowLayout)this.m_tabsContainer.getLayoutManager();
        switch (tabsPosition) {
            case NORTH: {
                rowLayout.setHorizontal(true);
                break;
            }
            case SOUTH: {
                rowLayout.setHorizontal(true);
                break;
            }
            case WEST: {
                rowLayout.setHorizontal(false);
                break;
            }
            case EAST: {
                rowLayout.setHorizontal(false);
                break;
            }
        }
        this.m_tabsPosition = tabsPosition;
        this.m_tabsContainer.invalidateMinSize();
        this.updateStyle();
    }
    
    public Alignment5 getTabsAlignment() {
        return this.m_tabsAlignment;
    }
    
    public void setTabsAlignment(final Alignment5 tabsAlignment) {
        this.m_tabsAlignment = tabsAlignment;
        this.m_tabsContainer.invalidateMinSize();
    }
    
    public Alignment9 getTabsLabelAlignment() {
        return this.m_tabsLabelAlignment;
    }
    
    public void setTabsLabelAlignment(final Alignment9 tabsLabelAlignment) {
        this.m_tabsLabelAlignment = tabsLabelAlignment;
        for (final RadioButton button : this.m_tabs) {
            button.setAlign(this.m_tabsLabelAlignment);
        }
    }
    
    public Alignment4 getPixmapAlignment() {
        return this.m_pixmapAlignment;
    }
    
    public void setPixmapAlignment(final Alignment4 pixmapAlignment) {
        this.m_pixmapAlignment = pixmapAlignment;
        for (final RadioButton button : this.m_tabs) {
            button.setPixmapAlign(pixmapAlignment);
        }
    }
    
    public Orientation getTextOrientation() {
        return this.m_textOrientation;
    }
    
    public void setTextOrientation(final Orientation textOrientation) {
        if (this.m_textOrientation != textOrientation) {
            this.m_textOrientation = textOrientation;
            for (final RadioButton button : this.m_tabs) {
                button.setTextOrientation(textOrientation);
            }
        }
    }
    
    public Button getSelectedTab() {
        return this.m_selectedTab;
    }
    
    public int getSelectedTabIndex() {
        for (int i = 0; i < this.m_tabs.size(); ++i) {
            if (this.m_tabs.get(i) == this.m_selectedTab) {
                return i;
            }
        }
        return -1;
    }
    
    public void setSelectedTab(final RadioButton selectedTab) {
        selectedTab.getAppearance().toggleButton();
        this.m_selectedTab = selectedTab;
        this.m_radioButtonHaveToBeEntierlyDisplayed = selectedTab;
        this.m_dirty = true;
        this.setNeedsToPreProcess();
    }
    
    public void setSelectedTabIndex(final int index) {
        assert index >= 0 && index < this.m_tabs.size();
        final RadioButton tab = this.m_tabs.get(index);
        if (tab.getVisible()) {
            this.setSelectedTab(tab);
        }
        else {
            this.selectFirstVisibleTab();
        }
    }
    
    public void setDataContainer(final Container dataContainer) {
        if (dataContainer == this.m_dataContainer) {
            return;
        }
        if (this.m_dataContainer != null) {
            this.remove(this.m_dataContainer);
        }
        this.add(dataContainer);
        (this.m_dataContainer = dataContainer).invalidate();
    }
    
    @Override
    public Widget getWidget(int x, int y) {
        if (this.m_unloading || !this.m_visible || !this.getAppearance().insideInsets(x, y) || MasterRootContainer.getInstance().isMovePointMode()) {
            return null;
        }
        Widget found = this.m_nonBlocking ? null : this;
        x -= this.getAppearance().getLeftInset();
        y -= this.getAppearance().getBottomInset();
        final int xTabsContainerPosition = this.m_tabsContainer.getX();
        final int yTabsContainerPosition = this.m_tabsContainer.getY() + (int)this.m_verticalFittings;
        if (x > xTabsContainerPosition && x < xTabsContainerPosition + this.m_tabsDisplaybleSpace.getWidth() && y > yTabsContainerPosition && y < yTabsContainerPosition + this.m_tabsDisplaybleSpace.getHeight()) {
            found = this.m_tabsContainer.getWidget(x - this.m_tabsContainer.m_position.x, y - this.m_tabsContainer.m_position.y);
        }
        if (found != null) {
            return found;
        }
        for (int i = 0; i < this.m_widgetChildren.size(); ++i) {
            Widget w = this.m_widgetChildren.get(i);
            if (w != this.m_tabsContainer && !w.isUnloading()) {
                w = w.getWidget(x - w.m_position.x, y - w.m_position.y);
                if (w != null) {
                    found = w;
                }
            }
        }
        return found;
    }
    
    private void setButtonsMinHeight(final int height) {
        for (final RadioButton radioButton : this.m_tabs) {
            radioButton.setPrefSize(new Dimension(0, height));
        }
    }
    
    @Override
    public void setStyle(final String style, final boolean force) {
        super.setStyle(style, force);
        this.updateStyle();
    }
    
    private void setButtonsMinWidth(final int width) {
        for (final RadioButton radioButton : this.m_tabs) {
            radioButton.setPrefSize(new Dimension(width, 0));
        }
    }
    
    private int findGreatestMinSize(final boolean horizontal) {
        int size = 0;
        size = (int)Math.max(horizontal ? this.m_increaseButton.getMinSize().getWidth() : this.m_increaseButton.getMinSize().getHeight(), size);
        size = (int)Math.max(horizontal ? this.m_decreaseButton.getMinSize().getWidth() : this.m_decreaseButton.getMinSize().getHeight(), size);
        for (final RadioButton radioButton : this.m_tabs) {
            size = (int)Math.max(horizontal ? radioButton.getMinSize().getWidth() : radioButton.getMinSize().getHeight(), size);
        }
        return size;
    }
    
    @Override
    public void onChildrenAdded() {
        this.m_tabsRadioGroup.onChildrenAdded();
        this.m_tabsContainer.onChildrenAdded();
        this.m_backgroundTabsContainer.onChildrenAdded();
        this.m_separator.onChildrenAdded();
        this.m_increaseButton.onChildrenAdded();
        this.m_decreaseButton.onChildrenAdded();
        super.onChildrenAdded();
    }
    
    @Override
    public void onCheckIn() {
        if (this.m_dataContainer != null) {
            this.m_children.remove(this.m_dataContainer);
        }
        super.onCheckIn();
        this.m_content.clear();
        this.m_tabsDisplaybleSpace = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_content = new HashMap<RadioButton, TabItem>();
        this.m_tabs = new ArrayList<RadioButton>();
        final TabContainerLayout tcl = new TabContainerLayout();
        tcl.onCheckOut();
        this.add(tcl);
        (this.m_tabsRadioGroup = new RadioGroup()).onCheckOut();
        this.m_tabsRadioGroup.setId("tabsRadiogGroup" + TabbedContainer.m_radioGroupId++);
        this.m_tabsContainer = Container.checkOut();
        final TabRenderStates renderStates = new TabRenderStates();
        this.m_tabsContainer.getEntity().setPreRenderStates(renderStates);
        this.m_tabsContainer.getEntity().setPostRenderStates(renderStates);
        (this.m_backgroundTabsContainer = new Container()).onCheckOut();
        (this.m_separator = new Image()).onCheckOut();
        (this.m_increaseButton = new Button()).onCheckOut();
        this.m_increaseButton.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (TabbedContainer.this.isEnabledFull()) {
                    final int previousIndex = TabbedContainer.this.m_firstVisibleTabIndex;
                    TabbedContainer.this.m_firstVisibleTabIndex = Math.min(TabbedContainer.this.m_tabs.size() - 1, TabbedContainer.this.m_firstVisibleTabIndex + 1);
                    if (TabbedContainer.this.m_firstVisibleTabIndex != previousIndex) {
                        TabbedContainer.this.m_dirty = true;
                        TabbedContainer.this.setNeedsToPreProcess();
                    }
                }
                return false;
            }
        }, false);
        (this.m_decreaseButton = new Button()).onCheckOut();
        this.m_decreaseButton.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final int previousIndex = TabbedContainer.this.m_firstVisibleTabIndex;
                TabbedContainer.this.m_firstVisibleTabIndex = Math.max(0, TabbedContainer.this.m_firstVisibleTabIndex - 1);
                if (TabbedContainer.this.m_firstVisibleTabIndex != previousIndex) {
                    TabbedContainer.this.m_dirty = true;
                    TabbedContainer.this.setNeedsToPreProcess();
                }
                return false;
            }
        }, false);
        this.m_tabsContainer.setNeedsScissor(true);
        this.add(this.m_backgroundTabsContainer);
        this.add(this.m_tabsContainer);
        this.add(this.m_separator);
        this.add(this.m_increaseButton);
        this.add(this.m_decreaseButton);
        this.setTabsPosition(Alignment4.NORTH);
        this.setTabsAlignment(Alignment5.WEST);
        this.m_needsScissor = true;
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_dirty) {
            this.invalidate();
            this.m_dirty = false;
        }
        return ret;
    }
    
    @Override
    public void validate() {
        if (this.m_selectedTab != null) {
            this.setDataContainer(this.m_content.get(this.m_selectedTab).getData());
        }
        else if (this.m_tabs.isEmpty()) {
            this.m_selectedTab = null;
            this.m_dataContainer = null;
        }
        else {
            for (final RadioButton button : this.m_tabs) {
                if (button.getValue() != null && button.getValue().equalsIgnoreCase(this.m_tabsRadioGroup.getValue())) {
                    this.setSelectedTab(button);
                }
            }
            if (this.m_selectedTab == null) {
                this.selectFirstVisibleTab();
            }
            if (this.m_selectedTab != null) {
                this.setDataContainer(this.m_content.get(this.m_selectedTab).getData());
            }
        }
        super.validate();
    }
    
    @Override
    public void onAttributesInitialized() {
        super.onAttributesInitialized();
        this.updateStyle();
    }
    
    private void updateStyle() {
        if (this.m_content != null) {
            for (final TabItem tab : this.m_content.values()) {
                final RadioButton button = tab.getButton();
                button.m_appearance.destroyAllRemovableDecorators();
                button.setStyle("TabbedContainer" + this.getStyle() + "$" + "tab" + this.m_tabsPosition, true);
                tab.resetButtonPixmap();
            }
        }
        if (this.m_separator != null) {
            this.m_separator.m_appearance.destroyAllRemovableDecorators();
            this.m_separator.setStyle("TabbedContainer" + this.getStyle() + "$" + "separator" + this.m_tabsPosition, true);
        }
        if (this.m_backgroundTabsContainer != null) {
            this.m_backgroundTabsContainer.m_appearance.destroyAllRemovableDecorators();
            this.m_backgroundTabsContainer.setStyle("TabbedContainer" + this.getStyle() + "$" + "backgroundTabsContainer" + this.m_tabsPosition, true);
        }
        if (this.m_increaseButton != null) {
            this.m_increaseButton.m_appearance.destroyAllRemovableDecorators();
            this.m_increaseButton.setStyle("TabbedContainer" + this.getStyle() + "$" + "increaseButton" + this.m_tabsPosition, true);
        }
        if (this.m_decreaseButton != null) {
            this.m_decreaseButton.m_appearance.destroyAllRemovableDecorators();
            this.m_decreaseButton.setStyle("TabbedContainer" + this.getStyle() + "$" + "decreaseButton" + this.m_tabsPosition, true);
        }
    }
    
    @Override
    public void invalidateMinSize() {
        super.invalidateMinSize();
        switch (this.m_tabsPosition) {
            case NORTH:
            case SOUTH: {
                this.m_tabsContainer.setPrefSize(new Dimension(0, this.findGreatestMinSize(false)));
                if (this.m_tabsSizesEquilibrate) {
                    this.setButtonsMinWidth(this.findGreatestMinSize(true));
                    break;
                }
                break;
            }
            case WEST:
            case EAST: {
                this.m_tabsContainer.setPrefSize(new Dimension(this.findGreatestMinSize(true), 0));
                if (this.m_tabsSizesEquilibrate) {
                    this.setButtonsMinHeight(this.findGreatestMinSize(false));
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void copyElement(final BasicElement t) {
        final TabbedContainer e = (TabbedContainer)t;
        super.copyElement(e);
        e.m_tabsPosition = this.m_tabsPosition;
        e.m_tabsAlignment = this.m_tabsAlignment;
        e.m_pixmapAlignment = this.m_pixmapAlignment;
        e.m_tabsLabelAlignment = this.m_tabsLabelAlignment;
        e.m_textOrientation = this.m_textOrientation;
        e.m_scrollButtonsPositionedBeforeTabs = this.m_scrollButtonsPositionedBeforeTabs;
        e.m_scrollButtonsNearby = this.m_scrollButtonsNearby;
        e.m_tabXOffset = this.m_tabXOffset;
        e.m_tabYOffset = this.m_tabYOffset;
    }
    
    @Override
    public void onVisibilityChange(final boolean visible) {
        this.m_layout.layoutContainer(this.getContainer());
        if (this.m_selectedTab != null) {
            this.selectFirstVisibleTab();
        }
    }
    
    public void selectFirstVisibleTab() {
        for (int i = 0; i < this.m_tabs.size(); ++i) {
            final RadioButton radioButton = this.m_tabs.get(i);
            if (radioButton != null && this.m_content.get(radioButton).isVisible()) {
                this.setSelectedTab(radioButton);
                return;
            }
        }
    }
    
    private ArrayList<RadioButton> getVisibleTabs() {
        final ArrayList<RadioButton> tabs = new ArrayList<RadioButton>();
        for (final RadioButton radioButton : this.m_tabs) {
            if (this.m_content.get(radioButton).isVisible()) {
                tabs.add(radioButton);
            }
        }
        return tabs;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TabbedContainer.PIXMAP_ALIGNMENT_HASH) {
            this.setPixmapAlignment(Alignment4.value(value));
        }
        else if (hash == TabbedContainer.SCROLL_BUTTONS_NEARBY_HASH) {
            this.setScrollButtonsNearby(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TabbedContainer.SELECTED_TAB_INDEX_HASH) {
            this.setSelectedTabIndex(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TabbedContainer.SCROLL_BUTTONS_POSITIONED_BEFORE_TABS_HASH) {
            this.setScrollButtonsPositionedBeforeTabs(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TabbedContainer.TABS_ALIGNMENT_HASH) {
            this.setTabsAlignment(Alignment5.value(value));
        }
        else if (hash == TabbedContainer.TABS_LABEL_ALIGNMENT_HASH) {
            this.setTabsLabelAlignment(Alignment9.value(value));
        }
        else if (hash == TabbedContainer.TABS_POSITION_HASH) {
            this.setTabsPosition(Alignment4.value(value));
        }
        else if (hash == TabbedContainer.TEXT_ORIENTATION_HASH) {
            this.setTextOrientation(Orientation.value(value));
        }
        else if (hash == TabbedContainer.TABS_X_OFFSET_HASH) {
            this.setTabsXOffset(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TabbedContainer.TABS_Y_OFFSET_HASH) {
            this.setTabsYOffset(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != TabbedContainer.TABS_SIZES_EQUILIBRATE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setTabsSizesEquilibrate(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == TabbedContainer.PIXMAP_ALIGNMENT_HASH) {
            this.setPixmapAlignment((Alignment4)value);
        }
        else if (hash == TabbedContainer.SELECTED_TAB_INDEX_HASH) {
            this.setSelectedTabIndex(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TabbedContainer.SCROLL_BUTTONS_NEARBY_HASH) {
            this.setScrollButtonsNearby(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TabbedContainer.SCROLL_BUTTONS_POSITIONED_BEFORE_TABS_HASH) {
            this.setScrollButtonsPositionedBeforeTabs(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TabbedContainer.TABS_ALIGNMENT_HASH) {
            this.setTabsAlignment((Alignment5)value);
        }
        else if (hash == TabbedContainer.TABS_LABEL_ALIGNMENT_HASH) {
            this.setTabsLabelAlignment((Alignment9)value);
        }
        else if (hash == TabbedContainer.TABS_POSITION_HASH) {
            this.setTabsPosition((Alignment4)value);
        }
        else if (hash == TabbedContainer.TEXT_ORIENTATION_HASH) {
            this.setTextOrientation((Orientation)value);
        }
        else if (hash == TabbedContainer.TABS_X_OFFSET_HASH) {
            this.setTabsXOffset(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TabbedContainer.TABS_Y_OFFSET_HASH) {
            this.setTabsYOffset(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != TabbedContainer.TABS_SIZES_EQUILIBRATE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setTabsSizesEquilibrate(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        TabbedContainer.m_radioGroupId = 0;
        PIXMAP_ALIGNMENT_HASH = "pixmapAlignment".hashCode();
        SCROLL_BUTTONS_NEARBY_HASH = "scrollButtonsNearby".hashCode();
        SCROLL_BUTTONS_POSITIONED_BEFORE_TABS_HASH = "scrollButtonsPositionedBeforeTabs".hashCode();
        SELECTED_TAB_INDEX_HASH = "selectedTabIndex".hashCode();
        TABS_ALIGNMENT_HASH = "tabsAlignment".hashCode();
        TABS_LABEL_ALIGNMENT_HASH = "tabsLabelAlignment".hashCode();
        TABS_POSITION_HASH = "tabsPosition".hashCode();
        TABS_SIZES_EQUILIBRATE_HASH = "tabsSizesEquilibrate".hashCode();
        TEXT_ORIENTATION_HASH = "textOrientation".hashCode();
        TABS_X_OFFSET_HASH = "tabsXOffset".hashCode();
        TABS_Y_OFFSET_HASH = "tabsYOffset".hashCode();
    }
    
    private class TabContainerLayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        private int findBiggestButton(final boolean horizontal, final boolean minSize) {
            int size = 0;
            if (minSize) {
                for (final RadioButton button : TabbedContainer.this.m_tabs) {
                    size = Math.max(horizontal ? button.getMinSize().width : button.getMinSize().height, size);
                }
            }
            else {
                for (final RadioButton button : TabbedContainer.this.m_tabs) {
                    size = Math.max(horizontal ? button.getPrefSize().width : button.getPrefSize().height, size);
                }
            }
            return size;
        }
        
        private int totalSize(final ArrayList<RadioButton> radioButtons, final boolean horizontal, final boolean minSize) {
            int size = 0;
            if (minSize) {
                for (final RadioButton button : radioButtons) {
                    size += (horizontal ? button.getMinSize().width : button.getMinSize().height);
                }
            }
            else {
                for (final RadioButton button : radioButtons) {
                    size += (horizontal ? button.getPrefSize().width : button.getPrefSize().height);
                }
            }
            return size;
        }
        
        private boolean computeTabsVisibility(final ArrayList<RadioButton> radioButtons, final int firstTabVisibleIndex, final int displaySize, final boolean horizontal) {
            TabbedContainer.this.m_tabsContainer.invalidateMinSize();
            RadioButton lastDisplayedTab = null;
            int index = 0;
            int currentDisplayPosition = 0;
            for (final RadioButton button : radioButtons) {
                final int buttonSize = horizontal ? button.getPrefSize().width : button.getPrefSize().height;
                final TabItem tabItem = TabbedContainer.this.m_content.get(button);
                if (index < firstTabVisibleIndex || currentDisplayPosition > displaySize) {
                    button.setVisible(false);
                }
                else {
                    button.setVisible(tabItem.isVisible());
                    currentDisplayPosition += buttonSize;
                    if (currentDisplayPosition > displaySize) {
                        lastDisplayedTab = button;
                    }
                }
                ++index;
            }
            if (TabbedContainer.this.m_radioButtonHaveToBeEntierlyDisplayed != null) {
                if (lastDisplayedTab != null && lastDisplayedTab == TabbedContainer.this.m_radioButtonHaveToBeEntierlyDisplayed) {
                    TabbedContainer.this.m_firstVisibleTabIndex = Math.min(TabbedContainer.this.m_tabs.size() - 1, TabbedContainer.this.m_firstVisibleTabIndex + 1);
                    return this.computeTabsVisibility(radioButtons, TabbedContainer.this.m_firstVisibleTabIndex, displaySize, horizontal);
                }
                TabbedContainer.this.m_radioButtonHaveToBeEntierlyDisplayed = null;
            }
            return currentDisplayPosition <= displaySize;
        }
        
        @Override
        public Dimension getContentMinSize(final Container scrollContainer) {
            final Dimension m_minSize = new Dimension();
            final ArrayList<RadioButton> radioButtons = TabbedContainer.this.m_tabs;
            switch (TabbedContainer.this.m_tabsPosition) {
                case NORTH:
                case SOUTH: {
                    if (radioButtons.size() > 0) {
                        m_minSize.setHeight(radioButtons.get(0).getMinSize().height);
                    }
                    m_minSize.setWidth(Math.min(this.findBiggestButton(true, true), this.totalSize(radioButtons, true, true)));
                    break;
                }
                case WEST:
                case EAST: {
                    if (radioButtons.size() > 0) {
                        m_minSize.setWidth(radioButtons.get(0).getMinSize().width);
                    }
                    m_minSize.setHeight(Math.min(this.findBiggestButton(false, true), this.totalSize(radioButtons, false, true)));
                    break;
                }
            }
            return m_minSize;
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            final Dimension m_prefSize = new Dimension();
            final ArrayList<RadioButton> radioButtons = TabbedContainer.this.m_tabs;
            switch (TabbedContainer.this.m_tabsPosition) {
                case NORTH:
                case SOUTH: {
                    if (radioButtons.size() > 0) {
                        m_prefSize.setHeight(radioButtons.get(0).getPrefSize().height);
                    }
                    m_prefSize.setWidth(Math.min(this.findBiggestButton(true, false), this.totalSize(radioButtons, true, false)));
                    break;
                }
                case WEST:
                case EAST: {
                    if (radioButtons.size() > 0) {
                        m_prefSize.setWidth(radioButtons.get(0).getPrefSize().width);
                    }
                    m_prefSize.setHeight(Math.min(this.findBiggestButton(false, false), this.totalSize(radioButtons, false, false)));
                    break;
                }
            }
            return m_prefSize;
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            TabbedContainer.this.m_verticalFittings = 0.0;
            final int displaySpaceWidth = TabbedContainer.this.m_appearance.getContentWidth();
            final int displaySpaceHeight = TabbedContainer.this.m_appearance.getContentHeight();
            final ArrayList<RadioButton> radioButtons = TabbedContainer.this.getVisibleTabs();
            TabbedContainer.this.m_increaseButton.setSize(TabbedContainer.this.m_increaseButton.getPrefSize());
            TabbedContainer.this.m_decreaseButton.setSize(TabbedContainer.this.m_decreaseButton.getPrefSize());
            switch (TabbedContainer.this.m_tabsPosition) {
                case NORTH:
                case SOUTH: {
                    TabbedContainer.this.m_scrollNeeded = (displaySpaceWidth < this.totalSize(radioButtons, true, false));
                    break;
                }
                case WEST:
                case EAST: {
                    TabbedContainer.this.m_scrollNeeded = (displaySpaceHeight < this.totalSize(radioButtons, false, false));
                    break;
                }
            }
            if (TabbedContainer.this.m_scrollNeeded) {
                TabbedContainer.this.m_increaseButton.setVisible(true);
                TabbedContainer.this.m_decreaseButton.setVisible(true);
            }
            else {
                TabbedContainer.this.m_increaseButton.setVisible(false);
                TabbedContainer.this.m_decreaseButton.setVisible(false);
            }
            boolean lastVisible = true;
            switch (TabbedContainer.this.m_tabsPosition) {
                case NORTH:
                case SOUTH: {
                    if (TabbedContainer.this.m_scrollNeeded) {
                        lastVisible = this.computeTabsVisibility(TabbedContainer.this.getVisibleTabs(), TabbedContainer.this.m_firstVisibleTabIndex, displaySpaceWidth - TabbedContainer.this.m_increaseButton.getWidth() - TabbedContainer.this.m_decreaseButton.getHeight(), true);
                    }
                    else {
                        for (final RadioButton tab : TabbedContainer.this.m_tabs) {
                            final TabItem tabItem = TabbedContainer.this.m_content.get(tab);
                            tab.setVisible(tabItem.isVisible());
                        }
                    }
                    TabbedContainer.this.m_tabsContainer.setSize((int)TabbedContainer.this.m_tabsContainer.getPrefSize().getWidth(), (int)TabbedContainer.this.m_tabsContainer.getPrefSize().getHeight());
                    TabbedContainer.this.m_tabsDisplaybleSpace.setSize(displaySpaceWidth - (TabbedContainer.this.m_scrollNeeded ? (TabbedContainer.this.m_increaseButton.getWidth() + TabbedContainer.this.m_decreaseButton.getHeight()) : 0), (int)TabbedContainer.this.m_tabsContainer.getPrefSize().getHeight());
                    TabbedContainer.this.m_backgroundTabsContainer.setSize(displaySpaceWidth, TabbedContainer.this.m_tabsContainer.getHeight());
                    TabbedContainer.this.m_separator.setSize(displaySpaceWidth, (int)TabbedContainer.this.m_separator.getPrefSize().getHeight());
                    if (TabbedContainer.this.m_dataContainer != null) {
                        TabbedContainer.this.m_dataContainer.setSize(displaySpaceWidth, displaySpaceHeight - TabbedContainer.this.m_tabsContainer.getHeight() - TabbedContainer.this.m_separator.getHeight());
                        break;
                    }
                    break;
                }
                case WEST:
                case EAST: {
                    if (TabbedContainer.this.m_scrollNeeded) {
                        lastVisible = this.computeTabsVisibility(TabbedContainer.this.m_tabs, TabbedContainer.this.m_firstVisibleTabIndex, displaySpaceHeight - TabbedContainer.this.m_increaseButton.getHeight() - TabbedContainer.this.m_decreaseButton.getHeight(), false);
                    }
                    else {
                        for (final RadioButton tab : TabbedContainer.this.m_tabs) {
                            final TabItem tabItem = TabbedContainer.this.m_content.get(tab);
                            tab.setVisible(tabItem.isVisible());
                        }
                    }
                    TabbedContainer.this.m_tabsContainer.setSize((int)TabbedContainer.this.m_tabsContainer.getPrefSize().getWidth(), (int)TabbedContainer.this.m_tabsContainer.getPrefSize().getHeight());
                    TabbedContainer.this.m_tabsDisplaybleSpace.setSize((int)TabbedContainer.this.m_tabsContainer.getPrefSize().getWidth(), displaySpaceHeight - (TabbedContainer.this.m_scrollNeeded ? (TabbedContainer.this.m_increaseButton.getHeight() + TabbedContainer.this.m_decreaseButton.getHeight()) : 0));
                    TabbedContainer.this.m_backgroundTabsContainer.setSize(TabbedContainer.this.m_tabsContainer.getWidth(), displaySpaceHeight);
                    TabbedContainer.this.m_separator.setSize((int)TabbedContainer.this.m_separator.getPrefSize().getWidth(), displaySpaceHeight);
                    if (TabbedContainer.this.m_dataContainer != null) {
                        TabbedContainer.this.m_dataContainer.setSize(displaySpaceWidth - TabbedContainer.this.m_tabsContainer.getWidth() - TabbedContainer.this.m_separator.getWidth(), displaySpaceHeight);
                        break;
                    }
                    break;
                }
            }
            TabbedContainer.this.m_increaseButton.setEnabled(!lastVisible);
            TabbedContainer.this.m_decreaseButton.setEnabled(TabbedContainer.this.m_firstVisibleTabIndex != 0);
            int xTabPosition = 0;
            int yTabPosition = 0;
            int xBackgroundTabsPosition = 0;
            int yBackgroundTabsPosition = 0;
            int xSeparatorPosition = 0;
            int ySeparatorPosition = 0;
            int xDataPosition = 0;
            int yDataPosition = 0;
            int xIncreaseButtonPosition = 0;
            int yIncreaseButtonPosition = 0;
            int xDecreaseButtonPosition = 0;
            int yDecreaseButtonPosition = 0;
            switch (TabbedContainer.this.m_tabsPosition) {
                case NORTH: {
                    xBackgroundTabsPosition = (xSeparatorPosition = (xTabPosition = TabbedContainer.this.m_tabXOffset));
                    yDecreaseButtonPosition = (yIncreaseButtonPosition = (yBackgroundTabsPosition = (yTabPosition = TabbedContainer.this.m_appearance.getContentHeight() - TabbedContainer.this.m_tabsContainer.getHeight() + TabbedContainer.this.m_tabYOffset)));
                    ySeparatorPosition = yTabPosition - TabbedContainer.this.m_separator.getHeight();
                    if (TabbedContainer.this.m_scrollNeeded) {
                        if (TabbedContainer.this.m_scrollButtonsNearby) {
                            if (TabbedContainer.this.m_scrollButtonsPositionedBeforeTabs) {
                                xDecreaseButtonPosition = xTabPosition;
                                xIncreaseButtonPosition = xDecreaseButtonPosition + TabbedContainer.this.m_decreaseButton.getWidth();
                                xTabPosition += TabbedContainer.this.m_decreaseButton.getWidth() + TabbedContainer.this.m_increaseButton.getWidth();
                            }
                            else {
                                xIncreaseButtonPosition = TabbedContainer.this.m_appearance.getContentWidth() - TabbedContainer.this.m_increaseButton.getWidth();
                                xDecreaseButtonPosition = xIncreaseButtonPosition - TabbedContainer.this.m_decreaseButton.getWidth();
                            }
                        }
                        else {
                            xDecreaseButtonPosition = xTabPosition;
                            xTabPosition += TabbedContainer.this.m_decreaseButton.getWidth();
                            xIncreaseButtonPosition = TabbedContainer.this.m_appearance.getContentWidth() - TabbedContainer.this.m_increaseButton.getWidth();
                        }
                    }
                    else if (TabbedContainer.this.m_tabsAlignment.equals(Alignment5.CENTER)) {
                        xTabPosition += (displaySpaceWidth - TabbedContainer.this.m_tabsContainer.getWidth()) / 2;
                    }
                    else if (!TabbedContainer.this.m_tabsAlignment.equals(Alignment5.WEST)) {
                        xTabPosition += displaySpaceWidth - TabbedContainer.this.m_tabsContainer.getWidth();
                    }
                    xDataPosition = 0;
                    yDataPosition = 0;
                    break;
                }
                case SOUTH: {
                    xBackgroundTabsPosition = (xSeparatorPosition = (xTabPosition = TabbedContainer.this.m_tabXOffset));
                    yDecreaseButtonPosition = (yIncreaseButtonPosition = (yBackgroundTabsPosition = (yTabPosition = TabbedContainer.this.m_tabYOffset)));
                    ySeparatorPosition = yTabPosition + TabbedContainer.this.m_tabsContainer.getHeight();
                    if (TabbedContainer.this.m_scrollNeeded) {
                        if (TabbedContainer.this.m_scrollButtonsNearby) {
                            if (TabbedContainer.this.m_scrollButtonsPositionedBeforeTabs) {
                                xDecreaseButtonPosition = xTabPosition;
                                xIncreaseButtonPosition = xDecreaseButtonPosition + TabbedContainer.this.m_decreaseButton.getWidth();
                                xTabPosition += TabbedContainer.this.m_decreaseButton.getWidth() + TabbedContainer.this.m_increaseButton.getWidth();
                            }
                            else {
                                xIncreaseButtonPosition = TabbedContainer.this.m_appearance.getContentWidth() - TabbedContainer.this.m_increaseButton.getWidth();
                                xDecreaseButtonPosition = xIncreaseButtonPosition - TabbedContainer.this.m_decreaseButton.getWidth();
                            }
                        }
                        else {
                            xDecreaseButtonPosition = xTabPosition;
                            xTabPosition += TabbedContainer.this.m_decreaseButton.getWidth();
                            xIncreaseButtonPosition = TabbedContainer.this.m_appearance.getContentWidth() - TabbedContainer.this.m_increaseButton.getWidth();
                        }
                    }
                    else if (TabbedContainer.this.m_tabsAlignment.equals(Alignment5.CENTER)) {
                        xTabPosition += (displaySpaceWidth - TabbedContainer.this.m_tabsContainer.getWidth()) / 2;
                    }
                    else if (!TabbedContainer.this.m_tabsAlignment.equals(Alignment5.WEST)) {
                        xTabPosition += displaySpaceWidth - TabbedContainer.this.m_tabsContainer.getWidth();
                    }
                    xDataPosition = 0;
                    yDataPosition = ySeparatorPosition + TabbedContainer.this.m_separator.getHeight();
                    break;
                }
                case EAST: {
                    xDecreaseButtonPosition = (xIncreaseButtonPosition = (xBackgroundTabsPosition = (xTabPosition = TabbedContainer.this.m_appearance.getContentWidth() - TabbedContainer.this.m_tabsContainer.getWidth() + TabbedContainer.this.m_tabXOffset)));
                    yBackgroundTabsPosition = (ySeparatorPosition = (yTabPosition = TabbedContainer.this.m_tabYOffset));
                    xSeparatorPosition = xTabPosition - TabbedContainer.this.m_separator.getWidth();
                    if (TabbedContainer.this.m_scrollNeeded) {
                        if (TabbedContainer.this.m_scrollButtonsNearby) {
                            if (TabbedContainer.this.m_scrollButtonsPositionedBeforeTabs) {
                                yDecreaseButtonPosition = TabbedContainer.this.m_appearance.getContentHeight() - TabbedContainer.this.m_decreaseButton.getHeight();
                                yIncreaseButtonPosition = yDecreaseButtonPosition - TabbedContainer.this.m_increaseButton.getHeight();
                            }
                            else {
                                yIncreaseButtonPosition = yTabPosition;
                                yDecreaseButtonPosition = TabbedContainer.this.m_increaseButton.getWidth();
                                yTabPosition += TabbedContainer.this.m_decreaseButton.getHeight() + TabbedContainer.this.m_increaseButton.getHeight();
                            }
                        }
                        else {
                            yIncreaseButtonPosition = yTabPosition;
                            yTabPosition += TabbedContainer.this.m_increaseButton.getHeight();
                            yDecreaseButtonPosition = TabbedContainer.this.m_appearance.getContentHeight() - TabbedContainer.this.m_decreaseButton.getWidth();
                        }
                        TabbedContainer.this.m_tabsDisplaybleSpace.setLocation(xTabPosition, yTabPosition);
                        TabbedContainer.this.m_verticalFittings = TabbedContainer.this.m_tabsContainer.getHeight() - TabbedContainer.this.m_tabsDisplaybleSpace.getHeight();
                        yTabPosition -= (int)TabbedContainer.this.m_verticalFittings;
                    }
                    else if (TabbedContainer.this.m_tabsAlignment.equals(Alignment5.CENTER)) {
                        yTabPosition += (displaySpaceHeight - TabbedContainer.this.m_tabsContainer.getHeight()) / 2;
                    }
                    else if (!TabbedContainer.this.m_tabsAlignment.equals(Alignment5.SOUTH)) {
                        yTabPosition += displaySpaceHeight - TabbedContainer.this.m_tabsContainer.getHeight();
                    }
                    xDataPosition = 0;
                    yDataPosition = 0;
                    break;
                }
                case WEST: {
                    xDecreaseButtonPosition = (xIncreaseButtonPosition = (xBackgroundTabsPosition = (xTabPosition = TabbedContainer.this.m_tabXOffset)));
                    yBackgroundTabsPosition = (ySeparatorPosition = (yTabPosition = TabbedContainer.this.m_tabYOffset));
                    xSeparatorPosition = xTabPosition + TabbedContainer.this.m_tabsContainer.getWidth();
                    if (TabbedContainer.this.m_scrollNeeded) {
                        if (TabbedContainer.this.m_scrollButtonsNearby) {
                            if (TabbedContainer.this.m_scrollButtonsPositionedBeforeTabs) {
                                yDecreaseButtonPosition = TabbedContainer.this.m_appearance.getContentHeight() - TabbedContainer.this.m_decreaseButton.getHeight();
                                yIncreaseButtonPosition = yDecreaseButtonPosition - TabbedContainer.this.m_increaseButton.getHeight();
                            }
                            else {
                                yIncreaseButtonPosition = yTabPosition;
                                yDecreaseButtonPosition = TabbedContainer.this.m_increaseButton.getWidth();
                                yTabPosition += TabbedContainer.this.m_decreaseButton.getHeight() + TabbedContainer.this.m_increaseButton.getHeight();
                            }
                        }
                        else {
                            yIncreaseButtonPosition = yTabPosition;
                            yTabPosition += TabbedContainer.this.m_increaseButton.getHeight();
                            yDecreaseButtonPosition = TabbedContainer.this.m_appearance.getContentHeight() - TabbedContainer.this.m_decreaseButton.getWidth();
                        }
                        TabbedContainer.this.m_tabsDisplaybleSpace.setLocation(xTabPosition, yTabPosition);
                        TabbedContainer.this.m_verticalFittings = TabbedContainer.this.m_tabsContainer.getHeight() - TabbedContainer.this.m_tabsDisplaybleSpace.getHeight();
                        yTabPosition -= (int)TabbedContainer.this.m_verticalFittings;
                    }
                    else if (TabbedContainer.this.m_tabsAlignment.equals(Alignment5.CENTER)) {
                        yTabPosition += (displaySpaceHeight - TabbedContainer.this.m_tabsContainer.getHeight()) / 2;
                    }
                    else if (!TabbedContainer.this.m_tabsAlignment.equals(Alignment5.SOUTH)) {
                        yTabPosition += displaySpaceHeight - TabbedContainer.this.m_tabsContainer.getHeight();
                    }
                    xDataPosition = xSeparatorPosition + TabbedContainer.this.m_separator.getWidth();
                    yDataPosition = 0;
                    break;
                }
            }
            TabbedContainer.this.m_tabsContainer.setPosition(xTabPosition, yTabPosition);
            TabbedContainer.this.m_backgroundTabsContainer.setPosition(xBackgroundTabsPosition, yBackgroundTabsPosition);
            TabbedContainer.this.m_separator.setPosition(xSeparatorPosition, ySeparatorPosition);
            TabbedContainer.this.m_increaseButton.setPosition(xIncreaseButtonPosition, yIncreaseButtonPosition);
            TabbedContainer.this.m_decreaseButton.setPosition(xDecreaseButtonPosition, yDecreaseButtonPosition);
            if (TabbedContainer.this.m_dataContainer != null) {
                TabbedContainer.this.m_dataContainer.setPosition(xDataPosition, yDataPosition);
            }
        }
    }
    
    private class TabRenderStates extends RenderStates
    {
        private boolean m_preRender;
        
        private TabRenderStates() {
            super();
            this.m_preRender = true;
        }
        
        @Override
        public void apply(final Renderer renderer) {
            if (this.m_preRender) {
                TabbedContainer.this.m_tabsContainer.setScreenPosition(TabbedContainer.this.m_tabsContainer.getScreenX(), TabbedContainer.this.m_tabsContainer.getScreenY());
                final PooledRectangle contRect = TabbedContainer.this.m_tabsContainer.getScissor(null);
                contRect.setSize(TabbedContainer.this.m_tabsDisplaybleSpace.getSize());
                contRect.setLocation(contRect.getX(), (int)(contRect.getY() + TabbedContainer.this.m_verticalFittings));
                Xulor.getInstance().getScene().scale(contRect);
                Graphics.getInstance().pushScissor(contRect);
                final PooledRectangle r = Graphics.getInstance().getScissor();
                RenderStateManager.getInstance().enableScissor(true);
                RenderStateManager.getInstance().setScissorRect(r.getX(), r.getY(), r.getWidth() + 1, r.getHeight() + 1);
            }
            else {
                TabbedContainer.this.m_tabsContainer.setScreenPosition(-1, -1);
                Graphics.getInstance().popScissor();
                final PooledRectangle r2 = Graphics.getInstance().getScissor();
                if (r2 != null) {
                    RenderStateManager.getInstance().enableScissor(true);
                    RenderStateManager.getInstance().setScissorRect(r2.getX(), r2.getY(), r2.getWidth() + 1, r2.getHeight() + 1);
                }
            }
            this.m_preRender = !this.m_preRender;
        }
    }
}
