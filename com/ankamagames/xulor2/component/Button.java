package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.layout.*;
import java.awt.*;

public class Button extends Container implements PixmapClient, FontClient, ColorClient, Alignment9Client
{
    public static final String TAG = "Button";
    protected Image m_image;
    protected Label m_label;
    protected Alignment9 m_align;
    protected Alignment4 m_pixmapAlignment;
    protected Orientation m_textOrientation;
    protected TextRenderer m_textRenderer;
    protected boolean m_dirty;
    protected EventListener m_focusableListener;
    protected EventListener m_mouseReleasedListener;
    protected EventListener m_mousePressedListener;
    protected int m_clickSoundId;
    protected Dimension m_displayedSize;
    public static final int TEXT_HASH;
    public static final int ALIGN_HASH;
    public static final int FONT_HASH;
    public static final int MODULATION_COLOR_HASH;
    public static final int TEXT_ORIENTATION_HASH;
    public static final int TEXTURE_HASH;
    public static final int PIXMAP_ALIGN_HASH;
    public static final int CLICK_SOUND_ID_HASH;
    public static final int DISPLAY_SIZE_HASH;
    
    public Button() {
        super();
        this.m_dirty = true;
        this.m_clickSoundId = -1;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof PixmapElement) {
            this.setPixmap((PixmapElement)e);
        }
        else if (e instanceof Label) {
            this.setLabel((Label)e);
        }
        super.add(e);
    }
    
    @Override
    public void addFromXML(final EventDispatcher e) {
        if (e instanceof Image) {
            if (this.m_image != null && this.m_image != e) {
                this.m_image.destroySelfFromParent();
            }
            this.m_image = (Image)e;
            if (this.m_displayedSize != null) {
                this.m_image.setId("buttonImage");
                this.m_image.setDisplaySize(this.m_displayedSize);
            }
        }
        super.addFromXML(e);
    }
    
    @Override
    public String getTag() {
        return "Button";
    }
    
    public Alignment4 getPixmapAlign() {
        return this.m_pixmapAlignment;
    }
    
    public void setPixmapAlign(final Alignment4 pixmapAlignment) {
        this.m_pixmapAlignment = pixmapAlignment;
        this.invalidateMinSize();
    }
    
    public Dimension getDisplaySize() {
        return this.m_displayedSize;
    }
    
    public void setDisplaySize(final Dimension displayedSize) {
        this.m_displayedSize = displayedSize;
        if (this.m_image != null) {
            this.m_image.setDisplaySize(this.m_displayedSize);
        }
    }
    
    public Orientation getTextOrientation() {
        return this.m_textOrientation;
    }
    
    public void setTextOrientation(final Orientation textOrientation) {
        this.m_textOrientation = textOrientation;
        if (this.m_label != null) {
            this.m_label.setOrientation(textOrientation);
        }
    }
    
    @Override
    public void setAlign(final Alignment9 align) {
        this.m_align = align;
        this.m_dirty = true;
        this.setNeedsToPreProcess();
    }
    
    public void setLabel(final Label l) {
        if (l != this.m_label && this.m_label != null) {
            this.m_label.destroySelfFromParent();
            this.m_label = l;
        }
        else if (this.m_label == null) {
            this.m_label = l;
        }
        if (this.m_label != null) {
            this.m_label.setOrientation(this.m_textOrientation);
            this.m_label.setColor(this.getAppearance().getTextColor(), null);
            this.m_label.setFont(this.getAppearance().getFont());
            this.m_label.setAlign(Alignment9.CENTER);
        }
    }
    
    public void setText(final String text) {
        if (text == null || text.isEmpty()) {
            if (this.m_label != null) {
                this.destroy(this.m_label);
            }
            return;
        }
        if (this.m_label == null) {
            (this.m_label = new Label()).onCheckOut();
            this.add(this.m_label);
        }
        this.m_label.setText(text);
    }
    
    public String getText() {
        if (this.m_label != null) {
            return this.m_label.getText();
        }
        return "";
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        this.checkEnabled();
    }
    
    @Override
    public void setNetEnabled(final boolean netEnabled) {
        super.setNetEnabled(netEnabled);
        this.checkEnabled();
    }
    
    private void checkEnabled() {
        if (this.isEnabledFull()) {
            this.getAppearance().enabled();
        }
        else {
            this.getAppearance().disabled();
        }
    }
    
    @Override
    public Widget getWidget(final int x, final int y) {
        if (this.m_unloading) {
            return null;
        }
        if (this.m_visible && !this.m_nonBlocking && this.getAppearance().insideInsets(x, y) && !MasterRootContainer.getInstance().isMovePointMode()) {
            return this;
        }
        return null;
    }
    
    @Override
    public ButtonAppearance getAppearance() {
        return (ButtonAppearance)this.m_appearance;
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return appearance instanceof ButtonAppearance;
    }
    
    public void setTexture(final Texture texture) {
        PixmapElement p = null;
        if (texture != null) {
            p = PixmapElement.checkOut();
            p.setTexture(texture);
        }
        this.setPixmap(p);
    }
    
    @Override
    public void setModulationColor(final Color c) {
        if (this.m_image != null) {
            this.m_image.setModulationColor(c);
        }
    }
    
    @Override
    public Color getModulationColor() {
        if (this.m_image != null) {
            return this.m_image.getModulationColor();
        }
        return null;
    }
    
    @Override
    public void setPixmap(final PixmapElement p) {
        if (p != null) {
            if (this.m_image == null) {
                (this.m_image = new Image()).onCheckOut();
                if (this.m_displayedSize != null) {
                    this.m_image.setDisplaySize(this.m_displayedSize);
                }
                this.add(this.m_image);
            }
            this.m_image.setPixmap(p);
        }
        else if (this.m_image != null) {
            this.m_image.destroySelfFromParent();
            this.m_image = null;
        }
    }
    
    @Override
    public void setFocusable(final boolean focusable) {
        super.setFocusable(focusable);
        if (this.m_focusable && this.m_focusableListener == null) {
            this.m_focusableListener = new EventListener() {
                @Override
                public boolean run(final Event event) {
                    final KeyEvent e = (KeyEvent)event;
                    if (e.getKeyCode() == 10) {
                        Button.this.performClick();
                        MasterRootContainer.getInstance().setKeyEventConsumed(true);
                    }
                    return false;
                }
            };
            this.addEventListener(Events.KEY_PRESSED, this.m_focusableListener, false);
        }
        else if (!this.m_focusable && this.m_focusableListener != null) {
            this.removeEventListener(Events.KEY_PRESSED, this.m_focusableListener, false);
            this.m_focusableListener = null;
        }
    }
    
    @Override
    public void setFont(final TextRenderer renderer) {
        if (this.m_label != null) {
            this.m_label.setFont(renderer);
        }
        this.m_textRenderer = renderer;
    }
    
    @Override
    public void setColor(final Color color, final String name) {
        if (this.m_label != null && (name == null || name.equalsIgnoreCase("text"))) {
            this.m_label.setColor(color, null);
        }
    }
    
    public void setClickSoundId(final int clickSoundId) {
        this.m_clickSoundId = clickSoundId;
    }
    
    public int getClickSoundId() {
        return this.m_clickSoundId;
    }
    
    @Override
    protected void processEventForSound(final Event e, final boolean up) {
        Label_0131: {
            if (!e.isSoundConsumed()) {
                switch (e.getType()) {
                    case MOUSE_CLICKED:
                    case MOUSE_DOUBLE_CLICKED:
                    case ITEM_CLICK:
                    case ITEM_DOUBLE_CLICK: {
                        e.setSoundConsumed(true);
                        switch (this.m_clickSoundId) {
                            case -1: {
                                XulorSoundManager.getInstance().click();
                                break Label_0131;
                            }
                            case -2: {
                                XulorSoundManager.getInstance().tabClick();
                                break Label_0131;
                            }
                            default: {
                                XulorSoundManager.getInstance().playSound(this.m_clickSoundId);
                                break Label_0131;
                            }
                        }
                        break;
                    }
                    case MOUSE_ENTERED: {
                        XulorSoundManager.getInstance().rollOver();
                        e.setSoundConsumed(true);
                        break;
                    }
                }
            }
        }
    }
    
    public void performClick() {
        this.performClick(1, 1, 0);
    }
    
    public void performClick(final int button, final int clickCount, final int modifiers) {
        this.getAppearance().pressed();
        MessageScheduler.getInstance().addClock(new ButtonClick(button, clickCount, modifiers, this.getScreenX() + this.m_size.width / 2, this.getScreenY() + this.m_size.height / 2), 200L, 0, 1);
    }
    
    @Override
    public void addedToWidgetTree() {
        super.addedToWidgetTree();
    }
    
    @Override
    public boolean dispatchEvent(final Event event) {
        if (event.getType() == Events.MOUSE_ENTERED) {
            this.getAppearance().enter();
        }
        else if (event.getType() == Events.MOUSE_EXITED) {
            this.getAppearance().exit();
        }
        return super.dispatchEvent(event);
    }
    
    public void addButtonAppearanceListeners() {
        this.m_mouseReleasedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (Button.this.isEnabledFull()) {
                    Button.this.getAppearance().released();
                }
                return false;
            }
        };
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        this.m_mousePressedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (Button.this.isEnabledFull()) {
                    Button.this.getAppearance().pressed();
                }
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_image = null;
        this.m_label = null;
        this.m_displayedSize = null;
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        this.m_mousePressedListener = null;
        this.m_mouseReleasedListener = null;
        this.m_focusableListener = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final ButtonAppearance app = ButtonAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        final ButtonLayout layout = new ButtonLayout();
        layout.onCheckOut();
        this.add(layout);
        this.m_nonBlocking = false;
        this.m_align = Alignment9.CENTER;
        this.m_pixmapAlignment = Alignment4.WEST;
        this.m_textOrientation = Orientation.EAST;
        this.m_clickSoundId = -1;
        this.addButtonAppearanceListeners();
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
    public void copyElement(final BasicElement b) {
        final Button e = (Button)b;
        super.copyElement(e);
        e.m_align = this.m_align;
        e.m_pixmapAlignment = this.m_pixmapAlignment;
        e.m_textOrientation = this.m_textOrientation;
        e.m_clickSoundId = this.m_clickSoundId;
        e.setDisplaySize((this.m_displayedSize != null) ? ((Dimension)this.m_displayedSize.clone()) : null);
        e.removeEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        if (this.m_focusableListener != null) {
            e.removeEventListener(Events.KEY_PRESSED, this.m_focusableListener, false);
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Button.TEXT_HASH) {
            this.setText(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == Button.ALIGN_HASH) {
            this.setAlign(cl.convert(Alignment9.class, value));
        }
        else if (hash == Button.MODULATION_COLOR_HASH) {
            this.setModulationColor(cl.convertToColor(value));
        }
        else if (hash == Button.FONT_HASH) {
            this.setFont(cl.convertToFont(value));
        }
        else if (hash == Button.TEXT_ORIENTATION_HASH) {
            this.setTextOrientation(cl.convert(Orientation.class, value));
        }
        else if (hash == Button.TEXTURE_HASH) {
            this.setTexture(cl.convertToTexture(value));
        }
        else if (hash == Button.PIXMAP_ALIGN_HASH) {
            this.setPixmapAlign(cl.convert(Alignment4.class, value));
        }
        else if (hash == Button.CLICK_SOUND_ID_HASH) {
            this.setClickSoundId(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != Button.DISPLAY_SIZE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setDisplaySize(cl.convertToDimension(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Button.TEXT_HASH) {
            if (value == null) {
                this.setText(null);
            }
            else {
                this.setText(String.valueOf(value));
            }
        }
        else if (hash == Button.ALIGN_HASH) {
            this.setAlign((Alignment9)value);
        }
        else if (hash == Button.MODULATION_COLOR_HASH) {
            this.setModulationColor((Color)value);
        }
        else if (hash == Button.FONT_HASH) {
            this.setFont((TextRenderer)value);
        }
        else if (hash == Button.TEXT_ORIENTATION_HASH) {
            this.setTextOrientation((Orientation)value);
        }
        else if (hash == Button.TEXTURE_HASH) {
            this.setTexture((Texture)value);
        }
        else if (hash == Button.PIXMAP_ALIGN_HASH) {
            this.setPixmapAlign((Alignment4)value);
        }
        else {
            if (hash != Button.DISPLAY_SIZE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setDisplaySize((Dimension)value);
        }
        return true;
    }
    
    @Override
    public CursorFactory.CursorType getCursorType() {
        return CursorFactory.CursorType.HOVER;
    }
    
    static {
        TEXT_HASH = "text".hashCode();
        ALIGN_HASH = "align".hashCode();
        FONT_HASH = "font".hashCode();
        MODULATION_COLOR_HASH = "modulationColor".hashCode();
        TEXT_ORIENTATION_HASH = "textOrientation".hashCode();
        TEXTURE_HASH = "texture".hashCode();
        PIXMAP_ALIGN_HASH = "pixmapAlign".hashCode();
        CLICK_SOUND_ID_HASH = "clickSoundId".hashCode();
        DISPLAY_SIZE_HASH = "displaySize".hashCode();
    }
    
    private class ButtonClick implements MessageHandler
    {
        int m_button;
        int m_clickCount;
        int m_modifiers;
        int m_screenX;
        int m_screenY;
        
        public ButtonClick(final int button, final int clickCount, final int modifiers, final int screenX, final int screenY) {
            super();
            this.m_button = button;
            this.m_clickCount = clickCount;
            this.m_modifiers = modifiers;
            this.m_screenX = screenX;
            this.m_screenY = screenY;
        }
        
        @Override
        public boolean onMessage(final Message message) {
            if (Button.this.isUnloading()) {
                return false;
            }
            Button.this.getAppearance().released();
            final MouseEvent e = MouseEvent.checkOut();
            e.setButton(this.m_button);
            e.setClickCount(this.m_clickCount);
            e.setModifiers(this.m_modifiers);
            e.setScreenX(this.m_screenX);
            e.setScreenY(this.m_screenY);
            e.setTarget(Button.this);
            e.setType(Events.MOUSE_CLICKED);
            Button.this.dispatchEvent(e);
            return false;
        }
        
        @Override
        public long getId() {
            return 1L;
        }
        
        @Override
        public void setId(final long id) {
        }
    }
    
    public class ButtonLayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            final Dimension minSize = (Button.this.m_image != null) ? Button.this.m_image.getMinSize() : new Dimension();
            final Dimension minSize2 = (Button.this.m_label != null) ? Button.this.m_label.getMinSize() : new Dimension();
            if (Button.this.m_pixmapAlignment == Alignment4.WEST || Button.this.m_pixmapAlignment == Alignment4.EAST) {
                minSize.height = Math.max(minSize.height, minSize2.height);
                minSize.width += minSize2.width;
                if (Button.this.m_image != null && Button.this.m_label != null) {
                    final Dimension dimension = minSize;
                    dimension.width += Button.this.getAppearance().getGap();
                }
            }
            else {
                minSize.height += minSize2.height;
                minSize.width = Math.max(minSize.width, minSize2.width);
                if (Button.this.m_image != null && Button.this.m_label != null) {
                    final Dimension dimension2 = minSize;
                    dimension2.height += Button.this.getAppearance().getGap();
                }
            }
            return minSize;
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            final Dimension prefSize = (Button.this.m_image != null) ? Button.this.m_image.getPrefSize() : new Dimension();
            final Dimension prefSize2 = (Button.this.m_label != null) ? Button.this.m_label.getPrefSize() : new Dimension();
            if (Button.this.m_pixmapAlignment == Alignment4.WEST || Button.this.m_pixmapAlignment == Alignment4.EAST) {
                prefSize.height = Math.max(prefSize.height, prefSize2.height);
                prefSize.width += prefSize2.width;
                if (Button.this.m_image != null && Button.this.m_label != null) {
                    final Dimension dimension = prefSize;
                    dimension.width += Button.this.getAppearance().getGap();
                }
            }
            else {
                prefSize.height += prefSize2.height;
                prefSize.width = Math.max(prefSize.width, prefSize2.width);
                if (Button.this.m_image != null && Button.this.m_label != null) {
                    final Dimension dimension2 = prefSize;
                    dimension2.height += Button.this.getAppearance().getGap();
                }
            }
            return prefSize;
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            Dimension labelSize = new Dimension(0, 0);
            final Point labelPosition = new Point(0, 0);
            Dimension imageSize = new Dimension(0, 0);
            final Point imagePosition = new Point(0, 0);
            if (Button.this.m_label != null && Button.this.m_label.getVisible()) {
                labelSize = Button.this.m_label.getPrefSize();
                labelSize.clampTo(Button.this.m_appearance.getContentWidth(), Button.this.m_appearance.getContentHeight());
            }
            if (Button.this.m_image != null && Button.this.m_image.getVisible()) {
                imageSize = Button.this.m_image.getPrefSize();
                switch (Button.this.m_pixmapAlignment) {
                    case NORTH: {
                        final Point point = imagePosition;
                        point.y += labelSize.height;
                        if (Button.this.m_label != null) {
                            final Point point2 = imagePosition;
                            point2.y += Button.this.getAppearance().getGap();
                            break;
                        }
                        break;
                    }
                    case SOUTH: {
                        final Point point3 = labelPosition;
                        point3.y += imageSize.height;
                        if (Button.this.m_label != null) {
                            final Point point4 = labelPosition;
                            point4.y += Button.this.getAppearance().getGap();
                            break;
                        }
                        break;
                    }
                    case EAST: {
                        final Point point5 = imagePosition;
                        point5.x += labelSize.width;
                        if (Button.this.m_label != null) {
                            final Point point6 = imagePosition;
                            point6.x += Button.this.getAppearance().getGap();
                            break;
                        }
                        break;
                    }
                    case WEST: {
                        final Point point7 = labelPosition;
                        point7.x += imageSize.width;
                        if (Button.this.m_label != null) {
                            final Point point8 = labelPosition;
                            point8.x += Button.this.getAppearance().getGap();
                            break;
                        }
                        break;
                    }
                }
            }
            int heightNeeded = 0;
            int widthNeeded = 0;
            switch (Button.this.m_pixmapAlignment) {
                case NORTH:
                case SOUTH: {
                    if (labelSize.width > imageSize.width) {
                        final Point point9 = imagePosition;
                        point9.x += (labelSize.width - imageSize.width) / 2;
                    }
                    else {
                        final Point point10 = labelPosition;
                        point10.x += (imageSize.width - labelSize.width) / 2;
                    }
                    heightNeeded = labelSize.height + imageSize.height;
                    widthNeeded = Math.max(labelSize.width, imageSize.width);
                    if (Button.this.m_label != null && Button.this.m_image != null) {
                        heightNeeded += Button.this.getAppearance().getGap();
                        break;
                    }
                    break;
                }
                case EAST:
                case WEST: {
                    if (labelSize.height > imageSize.height) {
                        final Point point11 = imagePosition;
                        point11.y += (labelSize.height - imageSize.height) / 2;
                    }
                    else {
                        final Point point12 = labelPosition;
                        point12.y += (imageSize.height - labelSize.height) / 2;
                    }
                    heightNeeded = Math.max(labelSize.height, imageSize.height);
                    widthNeeded = labelSize.width + imageSize.width;
                    if (Button.this.m_label != null && Button.this.m_image != null) {
                        widthNeeded += Button.this.getAppearance().getGap();
                        break;
                    }
                    break;
                }
            }
            final Point point13 = labelPosition;
            point13.x += Button.this.m_align.getX(widthNeeded, Button.this.m_appearance.getContentWidth());
            final Point point14 = imagePosition;
            point14.x += Button.this.m_align.getX(widthNeeded, Button.this.m_appearance.getContentWidth());
            final Point point15 = labelPosition;
            point15.y += Button.this.m_align.getY(heightNeeded, Button.this.m_appearance.getContentHeight());
            final Point point16 = imagePosition;
            point16.y += Button.this.m_align.getY(heightNeeded, Button.this.m_appearance.getContentHeight());
            if (Button.this.m_image != null) {
                Button.this.m_image.setSize(imageSize);
                Button.this.m_image.setPosition(imagePosition);
            }
            if (Button.this.m_label != null) {
                Button.this.m_label.setSize(labelSize);
                Button.this.m_label.setPosition(labelPosition);
            }
        }
    }
}
