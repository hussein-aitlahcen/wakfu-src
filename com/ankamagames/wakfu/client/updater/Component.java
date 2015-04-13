package com.ankamagames.wakfu.client.updater;

import java.util.*;

public enum Component
{
    BASE(new Component[0]), 
    TUTORIAL(new Component[] { Component.BASE }), 
    FULL(new Component[] { Component.BASE, Component.TUTORIAL });
    
    private final List<Component> m_neededComponents;
    
    private Component(final Component[] neededComponents) {
        this.m_neededComponents = Arrays.asList(neededComponents);
    }
    
    public List<Component> getNeededComponents() {
        return this.m_neededComponents;
    }
    
    public static Component byName(final String name) {
        final String upper = name.toUpperCase();
        for (final Component c : values()) {
            if (c.name().equals(upper)) {
                return c;
            }
        }
        return null;
    }
}
