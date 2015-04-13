package com.ankamagames.wakfu.client.ui.protocol.frame.helpers;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.event.*;
import java.util.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.core.*;

public class HighlightUIHelpers
{
    public static void highlightWidgetUntilClick(final Widget widget) {
        final Color c = new Color(WakfuClientConstants.HIGHLIGHT_COLOR.get());
        final Color c2 = new Color(Color.WHITE.get());
        final DecoratorAppearance appearance = widget.getAppearance();
        final AbstractTween t = new ModulationColorTween(c, c2, appearance, 0, 500, -1, TweenFunction.PROGRESSIVE);
        appearance.addTween(t);
        final EventListener el = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (event.getType() == Events.MOUSE_CLICKED) {
                    appearance.removeTweensOfType(ModulationColorTween.class);
                }
                widget.removeEventListener(Events.MOUSE_CLICKED, this, false);
                return false;
            }
        };
        widget.addEventListener(Events.MOUSE_CLICKED, el, false);
    }
    
    public static void highlightAllWidgetsInMap(final ElementMap map) {
        Widget w = null;
        final ArrayList<ModulationColorClient> widgets = new ArrayList<ModulationColorClient>();
        final ElementMapIterator it = new ElementMapIterator(map);
        while (it.hasNext()) {
            final EventDispatcher next = it.next();
            if (!(next instanceof Widget)) {
                continue;
            }
            final Widget widget = (Widget)next;
            if (w == null) {
                w = widget;
            }
            widgets.add(widget.getAppearance());
        }
        if (w == null) {
            return;
        }
        final Color c = new Color(Color.WHITE.get());
        final Color c2 = new Color(Color.WHITE_ALPHA.get());
        final ModulationColorListTween tween = new ModulationColorListTween(c, c2, widgets, 0, 250, 6, TweenFunction.PROGRESSIVE);
        w.addTween(tween);
    }
}
