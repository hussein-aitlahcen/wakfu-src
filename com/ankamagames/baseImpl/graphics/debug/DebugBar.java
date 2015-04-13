package com.ankamagames.baseImpl.graphics.debug;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class DebugBar extends JPanel
{
    private static Logger m_logger;
    private final String PROPERTIES_FILE = "debugBar.properties";
    private static final int HEIGHT = 38;
    private static final Border EMPTY_BORDER;
    private final AbstractGameClientInstance m_clientInstance;
    private final Vector<DebugComponent> m_components;
    private final JPanel m_componentsPanel;
    private final Vector<Class<? extends DebugComponent>> m_componentClasses;
    
    public DebugBar(final AbstractGameClientInstance clientInstance) {
        super(new BorderLayout(0, 0), true);
        this.m_components = new Vector<DebugComponent>();
        this.m_componentClasses = new Vector<Class<? extends DebugComponent>>();
        this.m_clientInstance = clientInstance;
        this.setIgnoreRepaint(true);
        final Dimension dimension = new Dimension(-1, 38);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        this.setPreferredSize(dimension);
        (this.m_componentsPanel = new JPanel(new FlowLayout(0, 0, 0))).setMaximumSize(dimension);
        this.m_componentsPanel.setMinimumSize(dimension);
        this.m_componentsPanel.setPreferredSize(dimension);
        this.add(this.m_componentsPanel, "Center");
    }
    
    public void addDebugComponent(final Class<? extends DebugComponent> c) {
        for (final DebugComponent usedComponent : this.m_components) {
            if (usedComponent.getClass().getCanonicalName().equals(c.getCanonicalName())) {
                DebugBar.m_logger.debug((Object)(c.toString() + " component already present"));
                return;
            }
        }
        DebugComponent comp;
        try {
            comp = (DebugComponent)c.newInstance();
        }
        catch (Exception e) {
            DebugBar.m_logger.info((Object)("Unable to instanciate a " + c + " DebugComponent"));
            return;
        }
        this.m_components.add(comp);
        comp.registerComponent(this.m_clientInstance);
        if (this.m_componentsPanel.getComponentCount() > 0) {
            this.m_componentsPanel.add(new Separator(8, 38));
        }
        final JComponent jComponent = comp.getAwtComponent();
        jComponent.setBorder(DebugBar.EMPTY_BORDER);
        this.m_componentsPanel.add(jComponent);
        this.m_componentsPanel.validate();
    }
    
    public void registerDebugComponent(final Class<? extends DebugComponent> c) {
        this.m_componentClasses.add(c);
    }
    
    private void removeDebugComponent(final Class<? extends DebugComponent> dc) {
        for (final DebugComponent comp : this.m_components) {
            if (comp.getClass() == dc) {
                this.removeDebugComponent(comp);
                break;
            }
        }
    }
    
    private boolean removeDebugComponent(final DebugComponent dc) {
        final int index = this.m_componentsPanel.getComponentZOrder(dc.getAwtComponent());
        if (index == -1) {
            return false;
        }
        this.m_components.remove(dc);
        this.m_componentsPanel.remove(dc.getAwtComponent());
        if (this.m_componentsPanel.getComponentCount() > 0) {
            if (index > 0) {
                this.m_componentsPanel.remove(index - 1);
            }
            else {
                this.m_componentsPanel.remove(index);
            }
        }
        dc.unregisterComponent(this.m_clientInstance);
        this.m_componentsPanel.validate();
        return true;
    }
    
    @Override
    public void removeAll() {
        this.removeAllComponents();
    }
    
    private void removeAllComponents() {
        while (this.m_components.size() > 0) {
            final DebugComponent dc = this.m_components.get(0);
            if (!this.removeDebugComponent(dc)) {
                DebugBar.m_logger.error((Object)("Error while deleting DebugComponent " + dc));
                this.m_components.remove(0);
            }
        }
        if (this.m_componentsPanel.getComponentCount() != 0) {
            DebugBar.m_logger.error((Object)"Encore des composants de pr\u00e9sent alors qu'on vient de tous les supprimer");
        }
        if (this.m_components.size() != 0) {
            DebugBar.m_logger.error((Object)"Encore des debugcomposants de pr\u00e9sent alors qu'on vient de tous les supprimer");
        }
    }
    
    public void reset() {
        this.removeAllComponents();
    }
    
    static {
        DebugBar.m_logger = Logger.getLogger((Class)DebugBar.class);
        EMPTY_BORDER = new EmptyBorder(0, 0, 0, 0);
    }
}
