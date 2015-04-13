package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.core.*;

public class PopupMenu extends Container
{
    public static final String TAG = "PopupMenu";
    public static final String BUTTON_TAG = "Button";
    public static final String LABEL_TAG = "Label";
    public static final String SEPARATOR_TAG = "Separator";
    private Alignment9 m_hotSpotPosition;
    private int m_showX;
    private int m_showY;
    private EventListener m_clickListener;
    private EventListener m_pressListener;
    private Button m_button;
    private Label m_label;
    private Separator m_separator;
    private boolean m_displayed;
    public static final int HOT_SPOT_POSITION_HASH;
    
    public PopupMenu() {
        super();
        this.m_hotSpotPosition = Alignment9.NORTH_WEST;
        this.m_showX = -1;
        this.m_showY = -1;
        this.m_displayed = false;
    }
    
    @Override
    public void addFromXML(final EventDispatcher e) {
        if (e instanceof Button) {
            this.m_button = (Button)e;
        }
        else if (e instanceof Label) {
            this.m_label = (Label)e;
        }
        else if (e instanceof Separator) {
            this.m_separator = (Separator)e;
        }
        else if (!(e instanceof Widget)) {
            super.addFromXML(e);
        }
    }
    
    public void addLabel(final String label, final Pixmap pixmap) {
        final Label l = (Label)this.m_label.cloneElementStructure();
        l.setText(label);
        this.add(l);
    }
    
    public void addButton(final String label, final Pixmap pixmap, final MouseClickedListener listener, final boolean enabled) {
        final Button b = (Button)this.m_button.cloneElementStructure();
        this.add(b);
        b.setText(label);
        b.setOnClick(listener);
        b.setEnabled(enabled);
    }
    
    public void addSeparator() {
        final Separator s = (Separator)this.m_separator.cloneElementStructure();
        this.add(s);
    }
    
    @Override
    public String getTag() {
        return "PopupMenu";
    }
    
    @Override
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        if ("Button".equalsIgnoreCase(themeElementName)) {
            if (this.m_button != null) {
                return this.m_button;
            }
            final Button button = new Button();
            button.onCheckOut();
            return button;
        }
        else if ("Label".equalsIgnoreCase(themeElementName)) {
            if (this.m_label != null) {
                return this.m_label;
            }
            final Label label = new Label();
            label.onCheckOut();
            return label;
        }
        else {
            if (!"Separator".equalsIgnoreCase(themeElementName)) {
                return null;
            }
            if (this.m_separator != null) {
                return this.m_separator;
            }
            final Separator separator = new Separator();
            separator.onCheckOut();
            return separator;
        }
    }
    
    public void setHotSpotPosition(final Alignment9 align) {
        this.m_hotSpotPosition = align;
    }
    
    public void show(final int x, final int y) {
        this.setVisible(true);
        this.m_showX = x;
        this.m_showY = y;
    }
    
    public void show() {
        this.show(MouseManager.getInstance().getX(), MouseManager.getInstance().getY());
    }
    
    public void addListeners() {
        this.m_clickListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (PopupMenu.this.m_visible) {
                    Xulor.getInstance().unload(PopupMenu.this.m_elementMap.getId());
                }
                return false;
            }
        };
        this.m_pressListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (PopupMenu.this.m_visible && event.getTarget().getElementMap() != PopupMenu.this.m_elementMap) {
                    Xulor.getInstance().unload(PopupMenu.this.m_elementMap.getId());
                }
                return false;
            }
        };
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_CLICKED, this.m_clickListener, false);
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_PRESSED, this.m_pressListener, false);
    }
    
    @Override
    public void validate() {
        this.setSizeToPrefSize();
        this.m_showX -= this.m_hotSpotPosition.getX(this.getWidth());
        this.m_showY -= this.m_hotSpotPosition.getY(this.getHeight());
        final RootContainer rootContainer = this.getWidgetParentOfType(RootContainer.class);
        this.m_showX = Math.min(rootContainer.getWidth() - this.getWidth(), this.m_showX);
        if (this.m_showY < 0) {
            this.m_showY = 0;
        }
        this.m_showY = Math.min(rootContainer.getHeight() - this.getHeight(), this.m_showY);
        this.setPosition(this.m_showX, this.m_showY);
        super.validate();
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        if (!this.m_displayed) {
            this.addListeners();
            this.m_displayed = true;
        }
        return ret;
    }
    
    @Override
    public void onChildrenAdded() {
        super.onChildrenAdded();
        this.setStyle(this.m_style[0], true);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final RowLayout rl = RowLayout.checkOut();
        rl.setHorizontal(false);
        this.add(rl);
        this.setNeedsToPostProcess();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_CLICKED, this.m_clickListener, false);
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_PRESSED, this.m_pressListener, false);
        if (this.m_button != null) {
            this.m_button.destroySelfFromParent();
            this.m_button = null;
        }
        if (this.m_label != null) {
            this.m_label.destroySelfFromParent();
            this.m_label = null;
        }
        if (this.m_separator != null) {
            this.m_separator.destroySelfFromParent();
            this.m_separator = null;
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == PopupMenu.HOT_SPOT_POSITION_HASH) {
            this.setHotSpotPosition(Alignment9.value(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == PopupMenu.HOT_SPOT_POSITION_HASH) {
            this.setHotSpotPosition((Alignment9)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        HOT_SPOT_POSITION_HASH = "hotSpotPosition".hashCode();
    }
}
