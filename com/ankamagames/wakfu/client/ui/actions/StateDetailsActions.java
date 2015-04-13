package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.java.util.*;

@XulorActionsTag
public class StateDetailsActions
{
    public static final String PACKAGE = "wakfu.stateDetails";
    
    public static void setStateLevel(final Event event) {
        if (event instanceof SliderMovedEvent) {
            final short levelValue = (short)((SliderMovedEvent)event).getValue();
            final ElementMap map = event.getTarget().getElementMap();
            if (map == null) {
                return;
            }
            setStateLevel(levelValue, map);
        }
    }
    
    public static void keyType(final Event event, final TextEditor te) {
        if (te.getText().length() == 0) {
            return;
        }
        final ElementMap map = event.getTarget().getElementMap();
        if (map == null) {
            return;
        }
        final EffectFieldProvider state = (EffectFieldProvider)PropertiesProvider.getInstance().getObjectProperty("describedState", map.getId());
        if (state == null) {
            return;
        }
        short levelValue = PrimitiveConverter.getShort(te.getText());
        final short maxLevel = state.getDisplayedMaxLevel();
        if (levelValue > maxLevel) {
            levelValue = maxLevel;
        }
        setStateLevel(levelValue, map);
    }
    
    public static void restore(final Event event) {
        final ElementMap map = event.getCurrentTarget().getElementMap();
        if (map == null) {
            return;
        }
        final short level = (short)PropertiesProvider.getInstance().getIntProperty("describedStateLevel", map.getId());
        setStateLevel(level, map);
    }
    
    private static void setStateLevel(final short level, final ElementMap map) {
        final EffectFieldProvider state = (EffectFieldProvider)PropertiesProvider.getInstance().getObjectProperty("describedState", map.getId());
        if (state != null) {
            state.forceLevel(level);
        }
    }
}
