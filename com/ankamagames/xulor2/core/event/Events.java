package com.ankamagames.xulor2.core.event;

public enum Events
{
    ACTIVATION_CHANGED, 
    COLOR_CHANGED, 
    DISPLAY_CONTAINER_CHANGED, 
    DRAG, 
    DRAG_OUT, 
    DRAG_OVER, 
    DROP, 
    DROP_OUT, 
    FOCUS_CHANGED, 
    ITEM_CLICK, 
    ITEM_DOUBLE_CLICK, 
    ITEM_OUT, 
    ITEM_OVER, 
    KEY_PRESSED, 
    KEY_RELEASED, 
    KEY_TYPED, 
    LIST_SELECTION_CHANGED, 
    MAP_CLICK, 
    MAP_DOUBLE_CLICK, 
    MAP_MOVE, 
    MOUSE_MOVED, 
    MOUSE_DRAGGED_IN, 
    MOUSE_DRAGGED, 
    MOUSE_DRAGGED_OUT, 
    MOUSE_ENTERED, 
    MOUSE_EXITED, 
    MOUSE_PRESSED, 
    MOUSE_RELEASED, 
    MOUSE_CLICKED, 
    MOUSE_DOUBLE_CLICKED, 
    MOUSE_WHEELED, 
    POPUP_DISPLAY, 
    POPUP_HIDE, 
    SELECTION_CHANGED, 
    SLIDER_MOVED, 
    SPACING_CHANGED, 
    UNIVERSAL_RADIAL_MENU_GROUP_CHANGED, 
    VALUE_CHANGED, 
    WIDGET_ADD_REQUESTED, 
    WIDGET_REMOVAL_REQUESTED, 
    WINDOW_STICK, 
    RESIZED, 
    REPOSITIONED;
    
    private static Events[] m_values;
    
    public static Events value(final String value) {
        final Events[] arr$;
        final Events[] values = arr$ = values();
        for (final Events a : arr$) {
            if (a.name().equals(value.toUpperCase())) {
                return a;
            }
        }
        return values[0];
    }
    
    public static Events fromOrdinal(final int ordinal) {
        return Events.m_values[ordinal];
    }
    
    static {
        Events.m_values = values();
    }
}
