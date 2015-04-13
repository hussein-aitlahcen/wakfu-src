package com.ankamagames.xulor2.appearance;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.component.*;
import java.awt.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DecoratorAppearance extends SpacingAppearance implements SwitchClient, ColorClient, ModulationColorClient
{
    protected static Logger m_logger;
    public static final String TAG = "Appearance";
    public static final String SCROLL_CONTAINER_APPEARANCE_TAG = "ScrollContainerAppearance";
    public static final String SLIDER_APPEARANCE_TAG = "SliderAppearance";
    public static final String SCROLLBAR_APPEARANCE_TAG = "ScrollBarAppearance";
    public static final String TEXT_EDITOR_APPEARANCE_TAG = "TextEditorAppearance";
    public static final String WINDOW_APPEARANCE_TAG = "WindowAppearance";
    public static final String POPUP_MENU_APPEARANCE_TAG = "PopupMenuAppearance";
    @NonNls
    public static final String DEFAULT_STATE = "DEFAULT";
    public static final String MODULATION_COLOR = "modulation";
    protected Color m_modulationColor;
    protected final ArrayList<Decorator> m_decorators;
    protected final ArrayList<Switch> m_switches;
    protected final ArrayList<Trigger> m_triggers;
    private String m_currentLoadingState;
    @NonNls
    protected String m_currentState;
    protected boolean m_needsToResetMeshes;
    private static final ObjectPool m_pool;
    public static final int MODULATION_COLOR_HASH;
    public static final int STATE_HASH;
    
    public DecoratorAppearance() {
        super();
        this.m_modulationColor = null;
        this.m_decorators = new ArrayList<Decorator>();
        this.m_switches = new ArrayList<Switch>();
        this.m_triggers = new ArrayList<Trigger>();
        this.m_currentLoadingState = "default";
        this.m_currentState = "default";
        this.m_needsToResetMeshes = true;
    }
    
    public static DecoratorAppearance checkOut() {
        DecoratorAppearance e;
        try {
            e = (DecoratorAppearance)DecoratorAppearance.m_pool.borrowObject();
            e.m_currentPool = DecoratorAppearance.m_pool;
        }
        catch (Exception ex) {
            DecoratorAppearance.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            e = new DecoratorAppearance();
            e.onCheckOut();
        }
        return e;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
        if (e instanceof Decorator) {
            this.addDecorator((Decorator)e);
        }
        if (e instanceof Switch) {
            this.addSwitch((Switch)e);
        }
        if (e instanceof Trigger) {
            this.addTrigger((Trigger)e);
        }
    }
    
    public void addMeshes() {
        final EntityGroup entity = this.m_widget.getEntity();
        for (int numDecorators = this.m_decorators.size(), i = 0; i < numDecorators; ++i) {
            final Decorator d = this.m_decorators.get(i);
            if (d.isEnabled() && d.getEntity() != null) {
                entity.addChild(d.getEntity());
            }
        }
        this.m_needsToResetMeshes = false;
    }
    
    public void addTrigger(final Trigger t) {
        this.addTrigger(t, true);
    }
    
    public void addTrigger(final Trigger t, final boolean addToList) {
        t.setDecoratorAppearance(this);
        if (addToList) {
            this.m_triggers.add(t);
        }
        if (this.m_widget != null) {
            this.m_widget.addEventListener(t.getTriggerAction(), new EventListener() {
                @Override
                public boolean run(final Event event) {
                    t.run();
                    return false;
                }
            }, false);
        }
        if (this.m_widget != null) {
            this.m_widget.setNeedsToResetMeshes();
        }
    }
    
    protected void addSwitch(final Switch s) {
        s.setDecoratorAppearance(this);
        if (this.m_modulationColor != null && s instanceof ModulationColorClient) {
            ((ModulationColorClient)s).setModulationColor(this.m_modulationColor);
        }
        if (s.getState() == null) {
            s.setState(this.m_currentLoadingState);
        }
        if ((s.getState().equalsIgnoreCase("DEFAULT") || s.getState().equalsIgnoreCase(this.m_currentState)) && this.m_widget != null) {
            s.setEnabled(true);
            if (s.isDecoratorSwitch()) {
                s.setup(this);
            }
            else {
                s.setup(this.m_widget);
            }
        }
        this.m_switches.add(s);
        if (this.m_widget != null) {
            this.m_widget.setNeedsToResetMeshes();
        }
    }
    
    protected void addDecorator(final Decorator decorator) {
        decorator.setDecoratorAppearance(this);
        if (decorator.getState() == null) {
            decorator.setState(this.m_currentLoadingState);
        }
        if (this.m_modulationColor != null && decorator instanceof ModulationColorClient) {
            ((ModulationColorClient)decorator).setModulationColor(this.m_modulationColor);
        }
        if ((decorator.getState().equalsIgnoreCase("DEFAULT") || decorator.getState().equalsIgnoreCase(this.m_currentState)) && this.m_widget != null) {
            decorator.setEnabled(true);
        }
        if (decorator instanceof InsetsBorder) {
            this.setBorder(((InsetsBorder)decorator).getInsets());
        }
        if (this.m_widget != null) {
            decorator.updateDecorator(this.m_widget.m_size, this.m_margin, this.m_border, this.m_padding);
        }
        this.m_decorators.add(decorator);
        if (this.m_widget != null) {
            this.m_widget.setNeedsToResetMeshes();
        }
    }
    
    protected void addBorder(final InsetsBorder insetsBorder) {
        this.addDecorator(insetsBorder);
    }
    
    public void removeDecorator(final Decorator decorator) {
        this.m_decorators.remove(decorator);
        if (decorator instanceof InsetsBorder) {
            this.recomputeBorderInsets();
        }
        ((EventDispatcher)decorator).removeSelfFromParent();
    }
    
    public void removeAllDecorators() {
        for (int i = this.m_decorators.size() - 1; i >= 0; --i) {
            ((EventDispatcher)this.m_decorators.get(i)).removeSelfFromParent();
        }
        this.m_decorators.clear();
        this.recomputeBorderInsets();
    }
    
    public void destroyDecorator(final Decorator decorator) {
        this.m_decorators.remove(decorator);
        ((EventDispatcher)decorator).destroySelfFromParent();
        if (decorator instanceof InsetsBorder) {
            this.recomputeBorderInsets();
        }
    }
    
    public void destroyAllDecorators() {
        for (int i = this.m_decorators.size() - 1; i >= 0; --i) {
            ((EventDispatcher)this.m_decorators.get(i)).destroySelfFromParent();
        }
        this.m_decorators.clear();
        this.recomputeBorderInsets();
        for (int i = this.m_switches.size() - 1; i >= 0; --i) {
            ((EventDispatcher)this.m_switches.get(i)).destroySelfFromParent();
        }
        this.m_switches.clear();
    }
    
    public void destroyAllRemovableDecorators() {
        for (int i = this.m_decorators.size() - 1; i >= 0; --i) {
            final Decorator d = this.m_decorators.get(i);
            if (d.isRemovable()) {
                ((EventDispatcher)d).destroySelfFromParent();
                this.m_decorators.remove(i);
            }
        }
        this.recomputeBorderInsets();
        for (int i = this.m_switches.size() - 1; i >= 0; --i) {
            final Switch s = this.m_switches.get(i);
            if (s.isRemovable()) {
                ((EventDispatcher)s).destroySelfFromParent();
                this.m_switches.remove(i);
            }
        }
    }
    
    public void setState(final String state) {
        this.m_currentLoadingState = state;
    }
    
    public String getState() {
        return this.m_currentLoadingState;
    }
    
    public String getCurrentState() {
        return this.m_currentState;
    }
    
    @Override
    public String getTag() {
        return "Appearance";
    }
    
    public void setEnabled(final String label, final boolean enable) {
        this.m_currentState = label;
        for (int i = 0; i < this.m_decorators.size(); ++i) {
            final Decorator d = this.m_decorators.get(i);
            if (d.getLabel() != null && d.getLabel().equals(label)) {
                d.setEnabled(enable);
            }
        }
        final ArrayList<Class<? extends Switch>> switchTypes = new ArrayList<Class<? extends Switch>>();
        for (int j = this.m_switches.size() - 1; j >= 0; --j) {
            final Switch sw = this.m_switches.get(j);
            if (sw.getLabel() != null && sw.getLabel().equals(label)) {
                sw.setEnabled(enable);
                if (!switchTypes.contains(sw.getClass()) && sw.isEnabled()) {
                    if (sw.isDecoratorSwitch()) {
                        sw.setup(this);
                    }
                    else {
                        sw.setup(this.getWidget());
                    }
                    switchTypes.add(sw.getClass());
                }
            }
        }
    }
    
    @Override
    public void setWidget(final Widget w) {
        super.setWidget(w);
        for (int i = 0, size = this.m_decorators.size(); i < size; ++i) {
            final Decorator d = this.m_decorators.get(i);
            if (d.getState().equalsIgnoreCase(this.m_currentState) && this.m_widget != null) {
                d.setEnabled(true);
            }
        }
        for (int i = 0, size = this.m_switches.size(); i < size; ++i) {
            final Switch sw = this.m_switches.get(i);
            if (sw.getState().equalsIgnoreCase(this.m_currentState) && this.m_widget != null) {
                sw.setEnabled(true);
                if (!sw.isDecoratorSwitch()) {
                    sw.setup(w);
                }
            }
        }
        for (int i = 0, size = this.m_triggers.size(); i < size; ++i) {
            final Trigger t = this.m_triggers.get(i);
            this.addTrigger(t, false);
        }
        if (this.m_modulationColor != null && this.m_widget instanceof ModulationColorClient) {
            ((ModulationColorClient)this.m_widget).setModulationColor(this.m_modulationColor);
        }
        w.setNeedsToResetMeshes();
    }
    
    public void setNeedsToResetMeshes() {
        if (this.m_widget != null) {
            this.m_widget.setNeedsToResetMeshes();
        }
    }
    
    @Override
    public void setColor(final Color c, final String name) {
        if (name == null || name.equalsIgnoreCase("modulation")) {
            this.setModulationColor(c);
        }
    }
    
    @Override
    public void setModulationColor(final Color c) {
        if (this.m_modulationColor == c) {
            return;
        }
        this.m_modulationColor = c;
        if (this.m_widget instanceof ModulationColorClient) {
            ((ModulationColorClient)this.m_widget).setModulationColor(c);
        }
        for (int i = this.m_decorators.size() - 1; i >= 0; --i) {
            final Decorator d = this.m_decorators.get(i);
            if (d instanceof ModulationColorClient) {
                ((ModulationColorClient)d).setModulationColor(c);
            }
        }
        for (int i = this.m_switches.size() - 1; i >= 0; --i) {
            final Switch s = this.m_switches.get(i);
            if (s instanceof ModulationColorClient) {
                ((ModulationColorClient)s).setModulationColor(c);
            }
        }
    }
    
    @Override
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    public void recomputeBorderInsets() {
        Insets insets = null;
        for (int i = this.m_decorators.size() - 1; i >= 0; --i) {
            final Decorator d = this.m_decorators.get(i);
            if (d instanceof InsetsBorder) {
                insets = ((InsetsBorder)d).getInsets();
                break;
            }
        }
        if (insets == null) {
            insets = new Insets(0, 0, 0, 0);
        }
        this.setBorder(insets);
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        final DecoratorAppearance d = (DecoratorAppearance)source;
        if (this.m_modulationColor != null) {
            d.setModulationColor(this.m_modulationColor);
        }
    }
    
    public void disableAllDecorators() {
        for (final Decorator decorator : this.m_decorators) {
            decorator.setEnabled(false);
        }
        for (final Switch sw : this.m_switches) {
            sw.setEnabled(false);
        }
    }
    
    @Override
    public void validate() {
        if (this.m_widget != null) {
            for (int i = this.m_decorators.size() - 1; i >= 0; --i) {
                this.m_decorators.get(i).updateDecorator(this.m_widget.m_size, this.m_margin, this.m_border, this.m_padding);
            }
        }
        super.validate();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_decorators.clear();
        this.m_switches.clear();
        this.m_triggers.clear();
        this.m_modulationColor = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_currentLoadingState = "DEFAULT";
        this.m_currentState = "DEFAULT";
        this.m_needsToResetMeshes = true;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == DecoratorAppearance.MODULATION_COLOR_HASH) {
            this.setModulationColor(cl.convertToColor(value));
        }
        else {
            if (hash != DecoratorAppearance.STATE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setState(cl.convertToString(value, this.m_elementMap));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == DecoratorAppearance.MODULATION_COLOR_HASH) {
            this.setModulationColor((Color)value);
        }
        else {
            if (hash != DecoratorAppearance.STATE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setState(String.valueOf(value));
        }
        return true;
    }
    
    static {
        DecoratorAppearance.m_logger = Logger.getLogger((Class)DecoratorAppearance.class);
        m_pool = new MonitoredPool(new ObjectFactory<DecoratorAppearance>() {
            @Override
            public DecoratorAppearance makeObject() {
                return new DecoratorAppearance();
            }
        });
        MODULATION_COLOR_HASH = "modulationColor".hashCode();
        STATE_HASH = "state".hashCode();
    }
}
