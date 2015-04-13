package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import java.util.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.tween.*;

public class UniversalRadialMenu extends Container
{
    public static final String TAG = "MRU";
    public static final String BUTTON_TAG = "Button";
    public static final String INCREASE_BUTTON_TAG = "IncreaseButton";
    public static final String DECREASE_BUTTON_TAG = "DecreaseButton";
    public static final int MIN_RADIUS = 40;
    public static final float ANGLE = 0.7853982f;
    private Button m_button;
    private PopupElement m_popupElement;
    private TextView m_popupText;
    private EventListener m_clickListener;
    private boolean m_displayed;
    private int m_showX;
    private int m_showY;
    private int m_radius;
    private final ArrayList<MRUGroup> m_groups;
    private byte m_currentGroup;
    private Button m_increaseButton;
    private Button m_decreaseButton;
    public static final int RADIUS_HASH;
    
    public UniversalRadialMenu() {
        super();
        this.m_radius = 40;
        this.m_groups = new ArrayList<MRUGroup>(3);
    }
    
    public static float computeAngle(final int numElements) {
        switch (numElements) {
            case 2: {
                return 1.5707964f;
            }
            case 4:
            case 6: {
                return 1.0471976f;
            }
            default: {
                return 0.7853982f;
            }
        }
    }
    
    @Override
    public void addFromXML(final EventDispatcher e) {
        if (e instanceof PopupElement) {
            this.m_popupElement = (PopupElement)e;
        }
        else if (e instanceof Button) {
            this.m_button = (Button)e;
        }
        else if (!(e instanceof Widget)) {
            super.addFromXML(e);
        }
    }
    
    public void addButton(final String label, final String tooltip, final String complementaryTooltip, final Pixmap pixmap, final String style, final Iterable<ParticleDecorator> particleDecorators, final MouseClickedListener listener, final boolean enabled) {
        if (this.m_groups.isEmpty()) {
            return;
        }
        final Button b = (Button)this.m_button.cloneElementStructure();
        if (label != null) {
            b.setText(label);
        }
        if (tooltip != null && this.m_popupText != null) {
            b.addEventListener(Events.POPUP_DISPLAY, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    UniversalRadialMenu.this.m_popupText.setText(tooltip);
                    UniversalRadialMenu.this.m_popupElement.show(b);
                    if (complementaryTooltip != null) {
                        ProcessScheduler.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                if (!UniversalRadialMenu.this.isVisible()) {
                                    return;
                                }
                                final String text = tooltip + "\n" + complementaryTooltip;
                                UniversalRadialMenu.this.m_popupText.setText(text);
                                UniversalRadialMenu.this.m_popupElement.show(b);
                            }
                        }, 1L, 1);
                    }
                    return false;
                }
            }, true);
            b.addEventListener(Events.POPUP_HIDE, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    MasterRootContainer.getInstance().getPopupContainer().hide();
                    return false;
                }
            }, true);
        }
        if (pixmap != null) {
            final PixmapElement pe = PixmapElement.checkOut();
            pe.setPixmap(pixmap);
            b.setPixmap(pe);
        }
        if (style != null) {
            b.addStyle(style);
        }
        b.setStyle("MRU" + this.getStyle() + "$buttonNorth", true);
        b.setOnClick(listener);
        b.setEnabled(enabled);
        b.setVisible(false);
        for (final ParticleDecorator particleDecorator : particleDecorators) {
            particleDecorator.setRemovable(false);
            b.getAppearance().add(particleDecorator);
        }
        final ArrayList<Button> buttons = this.m_groups.get(this.m_groups.size() - 1).m_buttons;
        buttons.add(b);
        this.add(b);
    }
    
    @Override
    public String getTag() {
        return "MRU";
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
        else {
            if ("DecreaseButton".equalsIgnoreCase(themeElementName)) {
                return this.m_decreaseButton;
            }
            if ("IncreaseButton".equalsIgnoreCase(themeElementName)) {
                return this.m_increaseButton;
            }
            return null;
        }
    }
    
    public int getRadius() {
        return this.m_radius;
    }
    
    public void setRadius(final int radius) {
        this.m_radius = radius;
    }
    
    public void newGroup() {
        this.m_groups.add(new MRUGroup());
    }
    
    public int getGroupSize() {
        return this.m_groups.size();
    }
    
    public void show(final int x, final int y) {
        this.setVisible(true);
        this.m_showX = x;
        this.m_showY = y;
    }
    
    public void show() {
        this.show(MouseManager.getInstance().getX(), MouseManager.getInstance().getY());
    }
    
    public void hide() {
        if (this.m_visible) {
            Xulor.getInstance().unload(this.m_elementMap.getId());
        }
    }
    
    public void addListeners() {
        this.m_clickListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final WidgetRemovalRequestedEvent e = new WidgetRemovalRequestedEvent(UniversalRadialMenu.this);
                e.onCheckOut();
                UniversalRadialMenu.this.dispatchEvent(e);
                Xulor.getInstance().unload(UniversalRadialMenu.this.m_elementMap.getId());
                return false;
            }
        };
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_CLICKED, this.m_clickListener, false);
        this.m_increaseButton.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                ArrayList<Button> buttons = UniversalRadialMenu.this.m_groups.get(UniversalRadialMenu.this.m_currentGroup).m_buttons;
                for (int i = 0; i < buttons.size(); ++i) {
                    buttons.get(i).setVisible(false);
                }
                UniversalRadialMenu.this.m_currentGroup = (byte)((UniversalRadialMenu.this.m_currentGroup == UniversalRadialMenu.this.m_groups.size() - 1) ? 0 : (UniversalRadialMenu.this.m_currentGroup + 1));
                final UniversalRadialMenuGroupChanged mruEvent = new UniversalRadialMenuGroupChanged(UniversalRadialMenu.this, UniversalRadialMenu.this.m_currentGroup);
                mruEvent.onCheckOut();
                UniversalRadialMenu.this.dispatchEvent(mruEvent);
                buttons = UniversalRadialMenu.this.m_groups.get(UniversalRadialMenu.this.m_currentGroup).m_buttons;
                for (int j = 0; j < buttons.size(); ++j) {
                    buttons.get(j).setVisible(true);
                }
                UniversalRadialMenu.this.invalidateMinSize();
                return true;
            }
        }, false);
        this.m_decreaseButton.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                ArrayList<Button> buttons = UniversalRadialMenu.this.m_groups.get(UniversalRadialMenu.this.m_currentGroup).m_buttons;
                for (int i = 0; i < buttons.size(); ++i) {
                    buttons.get(i).setVisible(false);
                }
                UniversalRadialMenu.this.m_currentGroup = (byte)((UniversalRadialMenu.this.m_currentGroup == 0) ? (UniversalRadialMenu.this.m_groups.size() - 1) : (UniversalRadialMenu.this.m_currentGroup - 1));
                final UniversalRadialMenuGroupChanged mruEvent = new UniversalRadialMenuGroupChanged(UniversalRadialMenu.this, UniversalRadialMenu.this.m_currentGroup);
                mruEvent.onCheckOut();
                UniversalRadialMenu.this.dispatchEvent(mruEvent);
                buttons = UniversalRadialMenu.this.m_groups.get(UniversalRadialMenu.this.m_currentGroup).m_buttons;
                for (int j = 0; j < buttons.size(); ++j) {
                    buttons.get(j).setVisible(true);
                }
                UniversalRadialMenu.this.invalidateMinSize();
                return true;
            }
        }, false);
    }
    
    @Override
    public void invalidateMinSize() {
        this.resetContentSizeCache();
        this.invalidate();
    }
    
    @Override
    public void validate() {
        this.setSizeToPrefSize();
        final int width = this.getWidth();
        final int height = this.getHeight();
        int x = this.m_showX - Alignment5.CENTER.getX(width);
        int y = this.m_showY - Alignment5.CENTER.getY(height);
        final DecoratorAppearance appearance = this.m_containerParent.getAppearance();
        x = Math.min(Math.max(0, x), appearance.getContentWidth() - width);
        y = Math.min(Math.max(0, y), appearance.getContentHeight() - height);
        this.setPosition(x, y);
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
        this.m_popupText = (TextView)this.m_elementMap.getElement("popupText");
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final MRULayout layout = new MRULayout();
        layout.onCheckOut();
        this.add(layout);
        (this.m_increaseButton = new Button()).onCheckOut();
        this.add(this.m_increaseButton);
        this.m_increaseButton.setVisible(false);
        (this.m_decreaseButton = new Button()).onCheckOut();
        this.add(this.m_decreaseButton);
        this.m_decreaseButton.setVisible(false);
        this.setNeedsToPostProcess();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_CLICKED, this.m_clickListener, false);
        this.m_groups.clear();
        if (this.m_popupElement != null) {
            this.m_popupElement.destroySelfFromParent();
            this.m_popupElement = null;
        }
        if (this.m_button != null) {
            this.m_button.destroySelfFromParent();
            this.m_button = null;
        }
        if (this.m_decreaseButton != null) {
            this.m_decreaseButton.destroySelfFromParent();
            this.m_decreaseButton = null;
        }
        if (this.m_increaseButton != null) {
            this.m_increaseButton.destroySelfFromParent();
            this.m_increaseButton = null;
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == UniversalRadialMenu.RADIUS_HASH) {
            this.setRadius(PrimitiveConverter.getInteger(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == UniversalRadialMenu.RADIUS_HASH) {
            this.setRadius(PrimitiveConverter.getInteger(value));
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        RADIUS_HASH = "radius".hashCode();
    }
    
    private static class MRUGroup
    {
        int m_radius;
        ArrayList<Button> m_buttons;
        
        private MRUGroup() {
            super();
            this.m_radius = 40;
            this.m_buttons = new ArrayList<Button>();
        }
    }
    
    private class MRULayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        private PooledRectangle isRadiusValid(final int radius, final boolean useMinSize, final ArrayList<Button> buttons) {
            final int size = buttons.size();
            if (size == 1) {
                final Dimension prefSize = buttons.get(0).getPrefSize();
                return PooledRectangle.checkout(0, 0, prefSize.width, prefSize.height);
            }
            assert size > 1 : "Le nombre de boutons est inf\u00e9rieur \u00e0 2";
            final float alpha = UniversalRadialMenu.computeAngle(size);
            float angle;
            if (size == 8) {
                angle = 4.712389f;
            }
            else {
                angle = 1.5707964f + (size - 1) * alpha / 2.0f;
            }
            final PooledRectangle firstRect = PooledRectangle.checkout();
            final PooledRectangle currentRect = PooledRectangle.checkout();
            final PooledRectangle totalRect = PooledRectangle.checkout();
            PooledRectangle previousRect = null;
            for (int i = 0; i < size; ++i) {
                final Widget w = buttons.get(i);
                final Dimension dim = useMinSize ? w.getMinSize() : w.getPrefSize();
                currentRect.setWidth(dim.width);
                currentRect.setHeight(dim.height);
                currentRect.setX((int)(radius * MathHelper.cosf(angle)) + radius);
                currentRect.setY((int)(radius * MathHelper.sinf(angle)) + radius);
                totalRect.storeUnion(currentRect);
                if (previousRect != null) {
                    if (currentRect.intersects(previousRect)) {
                        totalRect.release();
                        currentRect.release();
                        firstRect.release();
                        previousRect.release();
                        return null;
                    }
                }
                else {
                    previousRect = PooledRectangle.checkout();
                }
                previousRect.setBounds(currentRect);
                if (i == 0) {
                    firstRect.setBounds(currentRect);
                }
                angle -= alpha;
            }
            if (currentRect.intersects(firstRect)) {
                totalRect.release();
                currentRect.release();
                firstRect.release();
                if (previousRect != null) {
                    previousRect.release();
                }
                return null;
            }
            currentRect.release();
            firstRect.release();
            if (previousRect != null) {
                previousRect.release();
            }
            return totalRect;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            final UniversalRadialMenu mru = (UniversalRadialMenu)container;
            Dimension m_minSize;
            if (mru.m_groups.size() == 0) {
                m_minSize = new Dimension(0, 0);
            }
            else {
                final ArrayList<Button> buttons = mru.m_groups.get(mru.m_currentGroup).m_buttons;
                int radius = 40;
                for (PooledRectangle rect = this.isRadiusValid(radius, true, buttons); rect == null; rect = this.isRadiusValid(radius, true, buttons)) {
                    radius += 5;
                }
                UniversalRadialMenu.this.m_radius = radius;
                m_minSize = new Dimension(UniversalRadialMenu.this.m_radius * 2, UniversalRadialMenu.this.m_radius * 2);
                if (UniversalRadialMenu.this.m_groups.size() > 1) {
                    if (buttons.size() > 3) {
                        final Dimension dimension = m_minSize;
                        dimension.width += UniversalRadialMenu.this.m_increaseButton.getMinSize().width + UniversalRadialMenu.this.m_decreaseButton.getMinSize().width;
                        final Dimension dimension2 = m_minSize;
                        dimension2.height += UniversalRadialMenu.this.m_increaseButton.getMinSize().height + UniversalRadialMenu.this.m_decreaseButton.getMinSize().height;
                    }
                    m_minSize.width = Math.max(UniversalRadialMenu.this.m_increaseButton.getMinSize().width + UniversalRadialMenu.this.m_decreaseButton.getMinSize().width, m_minSize.width);
                    m_minSize.height = Math.max(UniversalRadialMenu.this.m_increaseButton.getMinSize().height + UniversalRadialMenu.this.m_decreaseButton.getMinSize().height, m_minSize.height);
                }
            }
            return m_minSize;
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            final UniversalRadialMenu mru = (UniversalRadialMenu)container;
            Dimension m_prefSize;
            if (mru.m_groups.size() == 0) {
                m_prefSize = new Dimension(0, 0);
            }
            else {
                final Dimension buttonPrefSize = mru.m_groups.get(0).m_buttons.get(0).getPrefSize();
                final int width = buttonPrefSize.width;
                final int height = buttonPrefSize.height;
                UniversalRadialMenu.this.m_radius = 40;
                for (int i = 0, size = mru.m_groups.size(); i < size; ++i) {
                    final MRUGroup group = mru.m_groups.get(i);
                    final ArrayList<Button> buttons = group.m_buttons;
                    group.m_radius = 40;
                    PooledRectangle rect;
                    for (rect = this.isRadiusValid(group.m_radius, false, buttons); rect == null; rect = this.isRadiusValid(group.m_radius, false, buttons)) {
                        final MRUGroup mruGroup = group;
                        mruGroup.m_radius += 5;
                    }
                    group.m_radius = Math.max(group.m_radius, Math.max(rect.getHeight(), rect.getWidth()) / 2);
                    rect.release();
                    UniversalRadialMenu.this.m_radius = Math.max(UniversalRadialMenu.this.m_radius, group.m_radius);
                }
                m_prefSize = new Dimension(width + UniversalRadialMenu.this.m_radius * 2, height + UniversalRadialMenu.this.m_radius * 2);
            }
            return m_prefSize;
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            if (UniversalRadialMenu.this.m_groups.size() == 0) {
                return;
            }
            final MRUGroup group = UniversalRadialMenu.this.m_groups.get(UniversalRadialMenu.this.m_currentGroup);
            final ArrayList<Button> buttons = group.m_buttons;
            final int size = buttons.size();
            final float alpha = UniversalRadialMenu.computeAngle(size);
            float angle;
            if (size == 8) {
                angle = 4.712389f;
            }
            else {
                angle = 1.5707964f + (size - 1) * alpha / 2.0f;
            }
            for (int i = 0; i < size; ++i) {
                buttons.get(i).setSizeToPrefSize();
            }
            final int deltaXY = UniversalRadialMenu.this.m_radius - group.m_radius;
            if (UniversalRadialMenu.this.m_groups.size() > 1) {
                UniversalRadialMenu.this.m_decreaseButton.setVisible(true);
                UniversalRadialMenu.this.m_decreaseButton.setSizeToPrefSize();
                int x = Alignment5.CENTER.getX(UniversalRadialMenu.this.m_decreaseButton.getWidth(), parent.getAppearance().getContentWidth()) - UniversalRadialMenu.this.m_decreaseButton.getWidth();
                int y = Alignment5.CENTER.getY(UniversalRadialMenu.this.m_decreaseButton.getHeight(), parent.getAppearance().getContentHeight());
                UniversalRadialMenu.this.m_decreaseButton.setPosition(UniversalRadialMenu.this.m_radius, UniversalRadialMenu.this.m_radius);
                UniversalRadialMenu.this.m_decreaseButton.setUsePositionTween(true);
                UniversalRadialMenu.this.m_decreaseButton.setPosition(x, y);
                UniversalRadialMenu.this.m_decreaseButton.setUsePositionTween(false);
                UniversalRadialMenu.this.m_increaseButton.setVisible(true);
                UniversalRadialMenu.this.m_increaseButton.setSizeToPrefSize();
                x = Alignment5.CENTER.getX(UniversalRadialMenu.this.m_increaseButton.getWidth(), parent.getAppearance().getContentWidth()) + UniversalRadialMenu.this.m_increaseButton.getWidth();
                y = Alignment5.CENTER.getY(UniversalRadialMenu.this.m_increaseButton.getHeight(), parent.getAppearance().getContentHeight());
                UniversalRadialMenu.this.m_increaseButton.setPosition(UniversalRadialMenu.this.m_radius, UniversalRadialMenu.this.m_radius);
                UniversalRadialMenu.this.m_increaseButton.setUsePositionTween(true);
                UniversalRadialMenu.this.m_increaseButton.setPosition(x, y);
                UniversalRadialMenu.this.m_increaseButton.setUsePositionTween(false);
            }
            else {
                UniversalRadialMenu.this.m_decreaseButton.setVisible(false);
                UniversalRadialMenu.this.m_increaseButton.setVisible(false);
            }
            for (int j = 0; j < size; ++j) {
                final Widget w = buttons.get(j);
                w.setVisible(true);
                final int degreeAngle = (int)Math.round(Math.toDegrees(angle)) % 360;
                switch (degreeAngle) {
                    case 0: {
                        w.setStyle("MRU" + UniversalRadialMenu.this.getStyle() + "$buttonEast", true);
                        break;
                    }
                    case -315:
                    case -300:
                    case 45:
                    case 60: {
                        w.setStyle("MRU" + UniversalRadialMenu.this.getStyle() + "$buttonNorthEast", true);
                        break;
                    }
                    case -270:
                    case 90: {
                        w.setStyle("MRU" + UniversalRadialMenu.this.getStyle() + "$buttonNorth", true);
                        break;
                    }
                    case -240:
                    case -225:
                    case 120:
                    case 135: {
                        w.setStyle("MRU" + UniversalRadialMenu.this.getStyle() + "$buttonNorthWest", true);
                        break;
                    }
                    case -180:
                    case 180: {
                        w.setStyle("MRU" + UniversalRadialMenu.this.getStyle() + "$buttonWest", true);
                        break;
                    }
                    case -135:
                    case -120:
                    case 225:
                    case 240: {
                        w.setStyle("MRU" + UniversalRadialMenu.this.getStyle() + "$buttonSouthWest", true);
                        break;
                    }
                    case -90:
                    case 270: {
                        w.setStyle("MRU" + UniversalRadialMenu.this.getStyle() + "$buttonSouth", true);
                        break;
                    }
                    case -60:
                    case -45:
                    case 300:
                    case 315: {
                        w.setStyle("MRU" + UniversalRadialMenu.this.getStyle() + "$buttonSouthEast", true);
                        break;
                    }
                }
                if (!w.hasTweensOfType(UniversalRadialMenuPositionTween2.class)) {
                    w.setPosition(group.m_radius + deltaXY, group.m_radius + deltaXY);
                    final UniversalRadialMenuPositionTween2 tween = new UniversalRadialMenuPositionTween2(1.5707964f, angle, 0, group.m_radius, group.m_radius + deltaXY, group.m_radius + deltaXY, w, 0, 300, TweenFunction.PROGRESSIVE);
                    w.addTween(tween);
                }
                angle -= alpha;
            }
        }
    }
}
