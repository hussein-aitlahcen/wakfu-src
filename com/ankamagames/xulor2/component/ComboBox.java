package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.layout.*;

public class ComboBox extends Container implements ContentClient
{
    public static final String TAG = "ComboBoxPlus";
    public static final String ALTERNATE_TAG = "ComboBox";
    public static final String THEME_RENDERABLE = "renderable";
    public static final String THEME_LIST = "list";
    public static final String THEME_BUTTON = "button";
    private boolean m_listIsBeingDisplayed;
    private boolean m_listJustGotDisplayed;
    private EventListener m_mousePressedListener;
    private EventListener m_mouseReleasedListener;
    private EventListener m_comboBoxListener;
    private List m_list;
    private RenderableContainer m_renderable;
    private Button m_button;
    private int m_maxRows;
    private ComboBoxBehaviour m_behaviour;
    private String m_contentProperty;
    private ElementMap m_contentPropertyElementMap;
    private Alignment9 m_align;
    private Alignment9 m_hotSpotPosition;
    public static final int CONTENT_HASH;
    public static final int MAX_ROWS_HASH;
    public static final int SELECTED_VALUE_HASH;
    public static final int ALIGN_HASH;
    public static final int HOT_SPOT_POSITION_HASH;
    public static final int BEHAVIOUR_HASH;
    
    public ComboBox() {
        super();
        this.m_listIsBeingDisplayed = false;
        this.m_listJustGotDisplayed = true;
        this.m_list = null;
        this.m_renderable = null;
        this.m_button = null;
        this.m_maxRows = -1;
        this.m_behaviour = ComboBoxBehaviour.COMBOBOX;
        this.m_contentProperty = null;
        this.m_contentPropertyElementMap = null;
        this.m_align = Alignment9.SOUTH;
        this.m_hotSpotPosition = Alignment9.NORTH;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        boolean addWidget = true;
        if (e instanceof List) {
            if (this.m_list != null) {
                this.m_list.release();
            }
            addWidget = false;
            (this.m_list = (List)e).setModalLevel(ModalManager.POP_UP_MODAL_LEVEL);
            this.addListlistener(this.m_list);
        }
        else if (e instanceof RenderableContainer) {
            if (this.m_renderable != null) {
                this.m_renderable.release();
            }
            this.m_renderable = (RenderableContainer)e;
        }
        else if (e instanceof Button) {
            if (this.m_button != null) {
                this.m_button.destroySelfFromParent();
            }
            this.m_button = (Button)e;
        }
        if (addWidget) {
            super.add(e);
        }
    }
    
    @Override
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        if ("renderable".equalsIgnoreCase(themeElementName)) {
            if (this.m_renderable != null) {
                return this.m_renderable;
            }
        }
        else if ("list".equalsIgnoreCase(themeElementName)) {
            if (this.m_list != null) {
                return this.m_list;
            }
        }
        else if ("button".equalsIgnoreCase(themeElementName)) {
            return this.m_button;
        }
        return null;
    }
    
    @Override
    public String getTag() {
        return "ComboBoxPlus";
    }
    
    public Button getButton() {
        return this.m_button;
    }
    
    public List getList() {
        return this.m_list;
    }
    
    public int getMaxRows() {
        return this.m_maxRows;
    }
    
    public void setMaxRows(final int maxRows) {
        this.m_maxRows = maxRows;
    }
    
    @Override
    public void setElementMap(final ElementMap map) {
        super.setElementMap(map);
        if (this.m_list != null) {
            this.m_list.setElementMap(map);
        }
    }
    
    public Object getSelectedValue() {
        if (this.m_list != null) {
            return this.m_list.getSelectedValue();
        }
        return null;
    }
    
    public void setSelectedValue(final Object value) {
        if (value == null) {
            return;
        }
        if (this.m_list != null) {
            this.m_list.setSelectedValue(value);
            final Object selectedValue = this.m_list.getSelectedValue();
            if (value != selectedValue && (selectedValue == null || !selectedValue.equals(value))) {
                ComboBox.m_logger.error((Object)("Impossible de retrouver la valeur s\u00e9lectionn\u00e9e dans la liste - il faut appliquer l'attribut content AVANT selectedValue - " + value + " - " + selectedValue));
            }
            this.setRenderableContent(selectedValue, -1);
        }
    }
    
    public RenderableContainer getRenderable() {
        return this.m_renderable;
    }
    
    @Override
    public void setContentProperty(final String propertyName, final ElementMap map) {
        this.m_contentProperty = propertyName;
        this.m_contentPropertyElementMap = map;
    }
    
    private void setRenderableContent(final Object value, final int index) {
        if (this.m_renderable != null) {
            int selectedIndex = 0;
            Object selectedValue;
            if (this.m_list != null) {
                selectedValue = this.m_list.getSelectedValue();
                selectedIndex = this.m_list.getSelectedOffset();
            }
            else {
                selectedValue = value;
                if (index != -1) {
                    selectedIndex = index;
                }
            }
            if (selectedValue != null) {
                this.m_renderable.setContentProperty(this.m_contentProperty + "#" + selectedIndex, this.m_contentPropertyElementMap);
            }
            this.m_renderable.setContent(selectedValue);
        }
    }
    
    public void setContent(final Iterable content) {
        if (content != null) {
            boolean isEmpty = true;
            Object firstValue = null;
            if (this.m_list != null) {
                this.m_list.setContentProperty(this.m_contentProperty, this.m_contentPropertyElementMap);
                this.m_list.setContent(content);
                if (this.m_list.size() != 0 && this.m_list.getSelectedValue() == null) {
                    isEmpty = false;
                    firstValue = this.m_list.getItems().get(0);
                    this.m_list.setSelectedOffset(0);
                }
            }
            Object renderableContent = null;
            int index = -1;
            if (!isEmpty) {
                renderableContent = firstValue;
                index = 0;
            }
            this.setRenderableContent(renderableContent, index);
        }
    }
    
    public void setContent(final Object[] content) {
        if (content != null) {
            boolean isEmpty = true;
            Object firstValue = null;
            if (this.m_list != null) {
                this.m_list.setContentProperty(this.m_contentProperty, this.m_contentPropertyElementMap);
                this.m_list.setContent(content);
                if (this.m_list.size() != 0 && this.m_list.getSelectedValue() == null) {
                    isEmpty = false;
                    firstValue = this.m_list.getItems().get(0);
                    this.m_list.setSelectedOffset(0);
                }
            }
            Object renderableContent = null;
            int index = -1;
            if (!isEmpty) {
                renderableContent = firstValue;
                index = 0;
            }
            this.setRenderableContent(renderableContent, index);
        }
    }
    
    public void setHotSpotPosition(final Alignment9 hotSpotPosition) {
        if (hotSpotPosition != null) {
            this.m_hotSpotPosition = hotSpotPosition;
        }
    }
    
    public void setAlign(final Alignment9 align) {
        if (align != null) {
            this.m_align = align;
        }
    }
    
    @Override
    public void addedToWidgetTree() {
        super.addedToWidgetTree();
        this.setFocusable(true);
    }
    
    public void setBehaviour(final ComboBoxBehaviour behaviour) {
        this.m_behaviour = behaviour;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        if (this.m_button != null) {
            this.m_button.setEnabled(enabled);
        }
    }
    
    @Override
    public void setNetEnabled(final boolean netEnabled) {
        super.setNetEnabled(netEnabled);
        if (this.m_button != null) {
            this.m_button.setNetEnabled(netEnabled);
        }
    }
    
    @Override
    public void onChildrenAdded() {
        super.onChildrenAdded();
        if (this.m_button != null) {
            this.m_button.setVisible(this.m_behaviour.isUseButton());
        }
    }
    
    public void addComboBoxListeners() {
        this.m_comboBoxListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (ComboBox.this.m_behaviour.isOpenOnClickOnContainer() && ComboBox.this.isEnabledFull()) {
                    ComboBox.this.switchPopup();
                }
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_CLICKED, this.m_comboBoxListener, false);
    }
    
    public void addRootContainerlistener(final MasterRootContainer masterRootContainer) {
        this.m_mousePressedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent e = (MouseEvent)event;
                if (ComboBox.this.m_listIsBeingDisplayed) {
                    if (ComboBox.this.m_appearance == null) {
                        return true;
                    }
                    if (ComboBox.this.m_appearance.insideInsets(e.getX(ComboBox.this), e.getY(ComboBox.this))) {
                        return true;
                    }
                    final DecoratorAppearance app = ComboBox.this.m_list.getAppearance();
                    if (app == null) {
                        return true;
                    }
                    if (!app.insideInsets(e.getX(ComboBox.this.m_list), e.getY(ComboBox.this.m_list))) {
                        ComboBox.this.closePopup();
                        return true;
                    }
                }
                return false;
            }
        };
        masterRootContainer.addEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        this.m_mouseReleasedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent e = (MouseEvent)event;
                if (!ComboBox.this.m_listJustGotDisplayed && ComboBox.this.m_listIsBeingDisplayed) {
                    final ScrollBar scrollBar = ComboBox.this.m_list.getScrollBar();
                    final DecoratorAppearance app = scrollBar.getAppearance();
                    if (app == null) {
                        return true;
                    }
                    if (!app.insideInsets(e.getX(scrollBar), e.getY(scrollBar))) {
                        ComboBox.this.closePopup();
                        return true;
                    }
                }
                if (ComboBox.this.m_listJustGotDisplayed) {
                    ComboBox.this.m_listJustGotDisplayed = false;
                }
                return false;
            }
        };
        masterRootContainer.addEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
    }
    
    public void addListlistener(final List list) {
        list.addEventListener(Events.LIST_SELECTION_CHANGED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (!ComboBox.this.m_behaviour.isSelectElementOnClick()) {
                    return false;
                }
                final ListSelectionChangedEvent e = (ListSelectionChangedEvent)event;
                if (e.getSelected()) {
                    ComboBox.this.setSelectedValue(e.getValue());
                    ComboBox.this.closePopup();
                }
                final ListSelectionChangedEvent e2 = new ListSelectionChangedEvent(ComboBox.this, e.getRenderableContainer(), e.getValue(), e.getSelected());
                ComboBox.this.dispatchEvent(e2);
                return false;
            }
        }, false);
    }
    
    public void switchPopup() {
        if (this.m_listIsBeingDisplayed) {
            this.closePopup();
        }
        else {
            this.openPopup();
        }
    }
    
    private void closePopup() {
        if (this.m_listIsBeingDisplayed) {
            this.m_list.removeSelfFromParent();
            this.m_listIsBeingDisplayed = false;
            XulorSoundManager.getInstance().comboBoxClose();
        }
    }
    
    private void openPopup() {
        if (!this.m_listIsBeingDisplayed) {
            final Dimension idealSize = this.m_list.getIdealSize(this.m_maxRows, -1);
            int height = idealSize.height;
            final int displayY = this.getDisplayY();
            final MasterRootContainer rootContainer = MasterRootContainer.getInstance();
            Alignment9 align = this.m_align;
            Alignment9 hotSpot = this.m_hotSpotPosition;
            int y = this.getDisplayY() + align.getY(this.getHeight()) - hotSpot.getY(height);
            if (y < 0 || y > rootContainer.getAppearance().getContentHeight() - height) {
                align = align.getYOpposite();
                hotSpot = hotSpot.getYOpposite();
            }
            y = this.getDisplayY() + align.getY(this.getHeight()) - hotSpot.getY(height);
            y = Math.max(0, Math.min(y, rootContainer.getAppearance().getContentHeight() - height));
            if (displayY - height < 0) {
                if (displayY + this.getHeight() + height > rootContainer.getHeight()) {
                    height = displayY;
                    y = 0;
                }
            }
            this.m_list.setSizeToPrefSize();
            final int width = Math.max(this.m_list.getWidth(), this.getWidth());
            this.m_list.setSize(width, height);
            this.m_list.setPosition(this.getDisplayX(), y);
            this.m_list.setNonBlocking(false);
            rootContainer.getLayeredContainer().addWidgetToLayer(this.m_list, 30000);
            this.m_listIsBeingDisplayed = true;
            this.m_listJustGotDisplayed = true;
            XulorSoundManager.getInstance().comboBoxClick();
        }
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return appearance instanceof ComboBoxAppearance;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        this.m_mouseReleasedListener = null;
        this.m_mousePressedListener = null;
        this.m_comboBoxListener = null;
        this.m_align = null;
        this.m_hotSpotPosition = null;
        this.m_list.destroySelfFromParent();
        this.m_button = null;
        this.m_list = null;
        this.m_renderable = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final ComboBoxLayout comboBoxLayout = new ComboBoxLayout();
        comboBoxLayout.onCheckOut();
        this.add(comboBoxLayout);
        final ComboBoxAppearance comboBoxAppearance = new ComboBoxAppearance();
        comboBoxAppearance.onCheckOut();
        comboBoxAppearance.setWidget(this);
        this.add(comboBoxAppearance);
        this.m_behaviour = ComboBoxBehaviour.COMBOBOX;
        final Button button = new Button();
        button.onCheckOut();
        this.add(button);
        (this.m_list = new List()).onCheckOut();
        (this.m_renderable = new RenderableContainer()).onCheckOut();
        this.m_nonBlocking = false;
        this.addComboBoxListeners();
        this.addRootContainerlistener(MasterRootContainer.getInstance());
    }
    
    @Override
    public void copyElement(final BasicElement s) {
        final ComboBox e = (ComboBox)s;
        super.copyElement(e);
        e.m_behaviour = this.m_behaviour;
        e.m_maxRows = this.m_maxRows;
        final Widget list = (Widget)this.m_list.cloneElementStructure();
        list.m_styleIsDirty = false;
        list.removeAllEventListeners();
        e.add(list);
        e.removeEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        e.removeEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        e.removeEventListener(Events.MOUSE_CLICKED, this.m_comboBoxListener, false);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ComboBox.MAX_ROWS_HASH) {
            this.setMaxRows(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ComboBox.ALIGN_HASH) {
            this.setAlign(Alignment9.value(value));
        }
        else if (hash == ComboBox.BEHAVIOUR_HASH) {
            this.setBehaviour(cl.convert(ComboBoxBehaviour.class, value));
        }
        else {
            if (hash != ComboBox.HOT_SPOT_POSITION_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setHotSpotPosition(Alignment9.value(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ComboBox.MAX_ROWS_HASH) {
            this.setMaxRows(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ComboBox.ALIGN_HASH) {
            this.setAlign((Alignment9)value);
        }
        else if (hash == ComboBox.HOT_SPOT_POSITION_HASH) {
            this.setHotSpotPosition((Alignment9)value);
        }
        else if (hash == ComboBox.CONTENT_HASH) {
            if (value == null || value.getClass().isArray()) {
                this.setContent((Object[])value);
            }
            else {
                if (!(value instanceof Iterable)) {
                    return false;
                }
                this.setContent((Iterable)value);
            }
        }
        else {
            if (hash != ComboBox.SELECTED_VALUE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setSelectedValue(value);
        }
        return true;
    }
    
    static {
        CONTENT_HASH = "content".hashCode();
        MAX_ROWS_HASH = "maxRows".hashCode();
        SELECTED_VALUE_HASH = "selectedValue".hashCode();
        ALIGN_HASH = "align".hashCode();
        HOT_SPOT_POSITION_HASH = "hotSpotPosition".hashCode();
        BEHAVIOUR_HASH = "behaviour".hashCode();
    }
    
    public enum ComboBoxBehaviour
    {
        COMBOBOX(true, true, true), 
        DRAWER(false, false, false);
        
        private final boolean m_openOnClickOnContainer;
        private final boolean m_selectElementOnClick;
        private final boolean m_useButton;
        
        private ComboBoxBehaviour(final boolean openOnClickOnContainer, final boolean selectElementOnClick, final boolean useButton) {
            this.m_openOnClickOnContainer = openOnClickOnContainer;
            this.m_selectElementOnClick = selectElementOnClick;
            this.m_useButton = useButton;
        }
        
        public boolean isOpenOnClickOnContainer() {
            return this.m_openOnClickOnContainer;
        }
        
        public boolean isSelectElementOnClick() {
            return this.m_selectElementOnClick;
        }
        
        public boolean isUseButton() {
            return this.m_useButton;
        }
    }
    
    private class ComboBoxLayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            return ComboBox.this.getContentPrefSize();
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            final Dimension prefSize = (ComboBox.this.m_renderable != null) ? ComboBox.this.m_renderable.getPrefSize() : new Dimension();
            final Dimension prefSize2 = (ComboBox.this.m_button != null) ? ComboBox.this.m_button.getPrefSize() : new Dimension();
            prefSize.height = Math.max(prefSize.height, prefSize2.height);
            prefSize.width += prefSize2.width;
            return prefSize;
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            int buttonWidth = 0;
            if (ComboBox.this.m_button != null && ComboBox.this.m_button.getVisible()) {
                ComboBox.this.m_button.setSizeToPrefSize();
                buttonWidth = ComboBox.this.m_button.getSize().width;
                ComboBox.this.m_button.setPosition(ComboBox.this.m_appearance.getContentWidth() - buttonWidth, (ComboBox.this.m_appearance.getContentHeight() - ComboBox.this.m_button.getHeight()) / 2);
            }
            if (ComboBox.this.m_renderable != null && ComboBox.this.m_renderable.getVisible()) {
                ComboBox.this.m_renderable.setPosition(0, 0);
                ComboBox.this.m_renderable.setSize(new Dimension(ComboBox.this.m_appearance.getContentWidth() - buttonWidth, ComboBox.this.m_appearance.getContentHeight()));
            }
        }
    }
}
