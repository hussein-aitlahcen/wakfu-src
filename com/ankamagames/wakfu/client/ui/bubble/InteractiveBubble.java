package com.ankamagames.wakfu.client.ui.bubble;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.*;
import java.awt.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.core.*;

public class InteractiveBubble extends WatcherContainer
{
    private static final Logger m_logger;
    public static final String TAG = "interactiveBubble";
    public static final String BUTTON_CONTAINER_TAG = "buttonContainer";
    public static final String CLICK_LABEL_TAG = "clickLabel";
    public static final String TEXT_TAG = "text";
    public static final String IMAGE_TAG = "image";
    private final ArrayList<Button> m_buttons;
    private int m_targetX;
    private int m_targetY;
    private int m_targetHeight;
    private String m_bubbleFontName;
    private int m_bubbleFontStyle;
    private int m_bubbleFontSize;
    private boolean m_forcedDisplaySpark;
    private boolean m_forcedDisplaySparkInit;
    private boolean m_displaySpark;
    private boolean m_actAsButton;
    private boolean m_closeOnClick;
    private EventListener m_clickListener;
    public static final int ACT_AS_BUTTON_HASH;
    public static final int BUBBLE_TEXT_HASH;
    public static final int TEXT_HASH;
    public static final int CLOSE_ON_CLICK_HASH;
    public static final int FORCED_DISPLAY_SPARK;
    
    public InteractiveBubble() {
        super();
        this.m_buttons = new ArrayList<Button>();
        this.m_forcedDisplaySpark = false;
        this.m_forcedDisplaySparkInit = false;
        this.m_displaySpark = true;
        this.m_actAsButton = false;
        this.m_closeOnClick = true;
        this.m_clickListener = null;
        this.m_bubbleFontName = "Arial Unicode MS";
        this.m_bubbleFontStyle = 0;
        this.m_bubbleFontSize = 12;
    }
    
    public void addButton(final String label, final EventListener listener, final boolean enabled) {
        if (!this.m_actAsButton) {
            final Button b = new Button();
            this.m_buttons.add(b);
            b.onCheckOut();
            b.setText(label);
            b.setExpandable(false);
            b.addEventListener(Events.MOUSE_CLICKED, listener, true);
            b.setEnabled(enabled);
            Widget w = this.getWidgetByThemeElementName("buttonContainer", false);
            if (w instanceof Container) {
                w.add(b);
            }
            b.onChildrenAdded();
            b.setStyle("interactiveBubble" + this.getStyle() + "$button", true);
            w = this.getWidgetByThemeElementName("clickLabel", false);
            if (w != null) {
                w.setVisible(false);
            }
        }
        else {
            this.addEventListener(Events.MOUSE_CLICKED, listener, true);
            final Widget w2 = this.getWidgetByThemeElementName("clickLabel", false);
            if (w2 != null) {
                w2.setVisible(true);
            }
        }
    }
    
    @Override
    public String getTag() {
        return "interactiveBubble";
    }
    
    @Override
    public InteractiveBubbleAppearance getAppearance() {
        return (InteractiveBubbleAppearance)this.m_appearance;
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return appearance instanceof InteractiveBubbleAppearance;
    }
    
    public void changeButtonText(final int buttonIndex, final String newText) {
        final Button button = this.m_buttons.get(buttonIndex);
        if (button != null) {
            button.setText(newText);
        }
    }
    
    public void changeButtonEvent(final int buttonIndex, final EventListener oldListener, final EventListener newListener) {
        final Widget w = this.m_actAsButton ? this : ((Button)this.m_buttons.get(buttonIndex));
        if (w != null) {
            w.removeEventListener(Events.MOUSE_CLICKED, oldListener, true);
            w.addEventListener(Events.MOUSE_CLICKED, newListener, true);
        }
    }
    
    public void setText(final String text) {
        final Widget w = this.getWidgetByThemeElementName("text", false);
        if (w != null && w instanceof TextWidget) {
            ((TextWidget)w).setText(text);
        }
        else {
            InteractiveBubble.m_logger.warn((Object)"Le champ de texte n'a pas \u00e9t\u00e9 d\u00e9fini dans le XML");
        }
    }
    
    public void setIconUrl(final String iconUrl) {
        final Widget w = this.getWidgetByThemeElementName("image", false);
        if (w instanceof Image) {
            final Texture texture = TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), Engine.getTextureName(iconUrl), iconUrl, false);
            if (texture == null) {
                return;
            }
            final PixmapElement pixmapElement = new PixmapElement();
            pixmapElement.onCheckOut();
            pixmapElement.setPixmap(new Pixmap(texture));
            w.add(pixmapElement);
        }
    }
    
    public boolean getActAsButton() {
        return this.m_actAsButton;
    }
    
    public void setActAsButton(final boolean actAsButton) {
        if (actAsButton != this.m_actAsButton) {
            this.m_actAsButton = actAsButton;
        }
    }
    
    public final void setBubbleFontName(final String bubbleFontName) {
        this.m_bubbleFontName = bubbleFontName;
        this.invalidate();
    }
    
    public final void setBubbleFontStyle(final int bubbleFontStyle) {
        this.m_bubbleFontStyle = bubbleFontStyle;
        this.invalidate();
    }
    
    public final void setBubbleFontSize(final int bubbleFontSize) {
        this.m_bubbleFontSize = bubbleFontSize;
        this.invalidate();
    }
    
    public final void setBubbleText(final String text) {
        this.setText(text);
    }
    
    public void setForcedDisplaySpark(final boolean displaySpark) {
        this.m_forcedDisplaySparkInit = true;
        this.m_forcedDisplaySpark = displaySpark;
        InteractiveBubbleAppearance app = this.getAppearance();
        if (app != null && app.getBubbleBorder() != null) {
            app.getBubbleBorder().setDisplaySpark(this.m_forcedDisplaySpark);
        }
        app = (InteractiveBubbleAppearance)this.m_xmlAppearance;
        if (app != null && app.getBubbleBorder() != null) {
            app.getBubbleBorder().setDisplaySpark(this.m_forcedDisplaySpark);
        }
    }
    
    public boolean isCloseOnClick() {
        return this.m_closeOnClick;
    }
    
    public void setCloseOnClick(final boolean closeOnClick) {
        if (this.m_closeOnClick != closeOnClick) {
            this.enableCloseOnClick(this.m_closeOnClick = closeOnClick);
        }
    }
    
    private void enableCloseOnClick(final boolean enable) {
        if (enable) {
            if (this.m_clickListener != null) {
                this.removeEventListener(Events.MOUSE_CLICKED, this.m_clickListener, false);
            }
            this.m_clickListener = new EventListener() {
                @Override
                public boolean run(final Event event) {
                    Xulor.getInstance().unload(InteractiveBubble.this.m_elementMap.getId(), false);
                    return false;
                }
            };
            this.addEventListener(Events.MOUSE_CLICKED, this.m_clickListener, false);
        }
        else {
            this.removeEventListener(Events.MOUSE_CLICKED, this.m_clickListener, false);
        }
    }
    
    public void clear() {
        this.destroySelfFromParent();
    }
    
    public void reset() {
        this.setText("");
        for (int i = this.m_buttons.size() - 1; i >= 0; --i) {
            this.m_buttons.get(i).destroySelfFromParent();
        }
        this.m_buttons.clear();
    }
    
    public final void show() {
        this.setVisible(true);
    }
    
    public final void hide() {
        this.setVisible(false);
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        super.copyElement(c);
        final InteractiveBubble b = (InteractiveBubble)c;
        if (this.m_forcedDisplaySparkInit) {
            b.setForcedDisplaySpark(this.m_forcedDisplaySpark);
        }
        this.setActAsButton(this.m_actAsButton);
        this.setCloseOnClick(this.m_closeOnClick);
    }
    
    @Override
    public void addedToWidgetTree() {
        super.addedToWidgetTree();
        this.enableCloseOnClick(this.m_closeOnClick);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_buttons.clear();
        this.m_clickListener = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final Font font = new Font(this.m_bubbleFontName, this.m_bubbleFontStyle, this.m_bubbleFontSize);
        this.m_forcedDisplaySparkInit = false;
        this.m_forcedDisplaySpark = false;
        this.m_displaySpark = true;
        this.m_actAsButton = false;
        this.m_closeOnClick = true;
        final InteractiveBubbleAppearance app = new InteractiveBubbleAppearance();
        app.onCheckOut();
        app.setWidget(this);
        this.add(app);
        this.setNeedsToPostProcess();
    }
    
    @Override
    public final boolean postProcess(final int deltaTime) {
        super.postProcess(deltaTime);
        return true;
    }
    
    @Override
    public final void invalidate() {
        super.invalidate();
        this.screenTargetMoved(this.getTarget(), this.m_targetX, this.m_targetY, this.m_targetHeight);
    }
    
    @Override
    public void screenTargetMoved(final ScreenTarget target, final int x, final int y, final int height) {
        if (!this.isUseTargetPositionning()) {
            return;
        }
        this.m_targetX = x;
        this.m_targetY = y;
        this.m_targetHeight = height;
        final XulorScene scene = Xulor.getInstance().getScene();
        final float frustumWidth = scene.getFrustumWidth();
        final float frustumHeight = scene.getFrustumHeight();
        final Dimension dimension = this.getSize();
        final float left = x + frustumWidth * 0.5f;
        final float bottom = y + frustumHeight * 0.5f + height;
        int leftOffset = 0;
        int bottomOffset = 0;
        final int screenX = this.getScreenX();
        final int screenY = this.getScreenY();
        if (screenX < 0) {
            leftOffset = -screenX;
        }
        else if (screenX + dimension.width > this.m_containerParent.getAppearance().getContentWidth()) {
            leftOffset = this.m_containerParent.getAppearance().getContentWidth() - dimension.width - screenX;
        }
        if (screenY < 0) {
            bottomOffset = -screenY;
        }
        else if (screenY + dimension.height > this.m_containerParent.getAppearance().getContentHeight()) {
            bottomOffset = this.m_containerParent.getAppearance().getContentHeight() - dimension.height - screenY;
        }
        boolean drawSpark = !this.m_forcedDisplaySparkInit || this.m_forcedDisplaySpark;
        if (!this.m_forcedDisplaySpark) {
            if (left < 0.0f || left > frustumWidth) {
                drawSpark = false;
            }
            if (bottom < 0.0f || bottom > frustumHeight) {
                drawSpark = false;
            }
        }
        if (drawSpark != this.m_displaySpark) {
            final InteractiveBubbleAppearance app = this.getAppearance();
            if (app != null && app.getBubbleBorder() != null) {
                app.getBubbleBorder().setDisplaySpark(drawSpark);
                this.m_displaySpark = drawSpark;
            }
        }
        super.screenTargetMoved(target, (int)left - this.getHalfDisplayWidth(), (int)bottom - this.getHalfDisplayHeight(), 0);
    }
    
    @Override
    public void setOffset(final int offsetX, final int offsetY) {
        super.setOffset(offsetX, offsetY);
    }
    
    @Override
    public String toString() {
        final Widget w = this.getWidgetByThemeElementName("text", false);
        String text = "";
        if (w != null && w instanceof TextWidget) {
            text = ((TextWidget)w).getText();
        }
        return "InteractiveBubble{m_text=" + text + ", m_targetX=" + this.m_targetX + ", m_targetY=" + this.m_targetY + ", m_screenX=" + this.getDisplayX() + ", m_screenY=" + this.getDisplayY() + '}';
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == InteractiveBubble.ACT_AS_BUTTON_HASH) {
            this.setActAsButton(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == InteractiveBubble.BUBBLE_TEXT_HASH) {
            this.setBubbleText(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == InteractiveBubble.TEXT_HASH) {
            this.setText(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == InteractiveBubble.CLOSE_ON_CLICK_HASH) {
            this.setCloseOnClick(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != InteractiveBubble.FORCED_DISPLAY_SPARK) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setForcedDisplaySpark(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == InteractiveBubble.ACT_AS_BUTTON_HASH) {
            this.setActAsButton(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == InteractiveBubble.BUBBLE_TEXT_HASH) {
            if (value == null) {
                this.setBubbleText(null);
            }
            else {
                this.setBubbleText(String.valueOf(value));
            }
        }
        else if (hash == InteractiveBubble.TEXT_HASH) {
            if (value == null) {
                this.setText(null);
            }
            else {
                this.setText(String.valueOf(value));
            }
        }
        else if (hash == InteractiveBubble.CLOSE_ON_CLICK_HASH) {
            this.setCloseOnClick(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != InteractiveBubble.FORCED_DISPLAY_SPARK) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setForcedDisplaySpark(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)InteractiveBubble.class);
        ACT_AS_BUTTON_HASH = "actAsButton".hashCode();
        BUBBLE_TEXT_HASH = "bubbleText".hashCode();
        TEXT_HASH = "text".hashCode();
        CLOSE_ON_CLICK_HASH = "closeOnClick".hashCode();
        FORCED_DISPLAY_SPARK = "forcedDisplaySpark".hashCode();
    }
}
