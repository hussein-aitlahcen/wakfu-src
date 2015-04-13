package com.ankamagames.xulor2.core;

import org.apache.log4j.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;

public class FocusManager
{
    private static Logger m_logger;
    private static FocusManager m_instance;
    private Widget m_focused;
    private final HashMap<Container, ArrayList<Widget>> m_lists;
    private boolean m_enabled;
    private Widget m_lastFocused;
    
    private FocusManager() {
        super();
        this.m_lists = new HashMap<Container, ArrayList<Widget>>();
        this.m_enabled = false;
        this.m_lastFocused = null;
    }
    
    public void addWidgetToFocusList(final Widget w) {
        final Container root = w.getRootFocusParent();
        if (root != null) {
            ArrayList<Widget> list = this.m_lists.get(root);
            if (list == null) {
                list = new ArrayList<Widget>();
                this.m_lists.put(root, list);
            }
            if (!list.contains(w)) {
                list.add(w);
            }
        }
    }
    
    public void removeWidgetFromFocusList(final Widget w) {
        if (this.m_focused == w) {
            this.focusNext();
        }
        if (this.m_focused == w) {
            this.m_focused = null;
        }
        if (this.m_lastFocused == w) {
            this.m_lastFocused = null;
        }
        final Container root = w.getRootFocusParent();
        if (root != null) {
            final ArrayList<Widget> list = this.m_lists.get(root);
            this.removeFromList(w, root, list);
        }
        else {
            for (final ArrayList<Widget> list2 : this.m_lists.values()) {
                this.removeFromList(w, root, list2);
            }
        }
    }
    
    private void removeFromList(final Widget w, final Container root, final Collection<Widget> list) {
        if (list != null) {
            list.remove(w);
            if (list.isEmpty()) {
                this.m_lists.remove(root);
            }
        }
    }
    
    public void removeRootContainer(final Container container) {
        this.m_lists.remove(container);
    }
    
    public static FocusManager getInstance() {
        return FocusManager.m_instance;
    }
    
    public void setEnabled(final boolean enabled) {
        if (this.m_enabled == enabled) {
            return;
        }
        if (!enabled) {
            this.m_lastFocused = this.m_focused;
            this.setFocused(null, true);
        }
        else {
            this.setFocused(this.m_lastFocused, true);
            this.m_lastFocused = null;
        }
        this.m_enabled = enabled;
    }
    
    public void setFocused(@Nullable final Widget focused) {
        this.setFocused(focused, false);
    }
    
    private void setFocused(@Nullable final Widget focused, final boolean force) {
        final boolean enabled = this.m_enabled || force;
        final Widget newFocused = (focused != null && focused.getFocusable()) ? focused : null;
        if ((enabled && newFocused == this.m_focused) || (!enabled && newFocused == this.m_lastFocused)) {
            return;
        }
        final Widget oldFocused = this.m_focused;
        if (enabled) {
            this.m_focused = newFocused;
        }
        else {
            this.m_lastFocused = newFocused;
            this.m_focused = null;
        }
        if (oldFocused != null && oldFocused.getFocusable()) {
            final Event fce = new FocusChangedEvent(oldFocused, false);
            oldFocused.dispatchEvent(fce);
        }
        if (this.m_focused != null && this.m_focused.getFocusable()) {
            final Event fce = new FocusChangedEvent(this.m_focused, true);
            this.m_focused.dispatchEvent(fce);
        }
    }
    
    @Nullable
    public Widget getFocused() {
        return this.m_focused;
    }
    
    public void checkForModality() {
        if (this.m_focused != null) {
            Widget w;
            for (w = this.m_focused; w != null && w != MasterRootContainer.getInstance() && w.getModalLevel() == -1; w = w.getContainer()) {}
            if (w != null && w.getModalLevel() < ModalManager.getInstance().getMaxModalLevel()) {
                this.setFocused(null);
            }
        }
    }
    
    public void focusPrevious() {
        this.moveFocus(false);
    }
    
    public void focusNext() {
        this.moveFocus(true);
    }
    
    private void moveFocus(final boolean forward) {
        Widget newFocused = null;
        if (this.m_focused == null) {
            for (final ArrayList<Widget> list : this.m_lists.values()) {
                if (!list.isEmpty()) {
                    newFocused = list.get(0);
                    break;
                }
            }
        }
        else {
            final Container root = this.m_focused.getRootFocusParent();
            if (root != null) {
                final ArrayList<Widget> list = this.m_lists.get(root);
                if (list != null && !list.isEmpty()) {
                    final int index = list.indexOf(this.m_focused);
                    final int indexMax = list.size() - 1;
                    if (forward && index == indexMax) {
                        newFocused = list.get(0);
                    }
                    else if (!forward && index == 0) {
                        newFocused = list.get(indexMax);
                    }
                    else if (index >= 0 && index <= indexMax) {
                        newFocused = list.get(index + (forward ? 1 : -1));
                    }
                    else {
                        FocusManager.m_logger.error((Object)("m_focused (" + this.m_focused + ") n'est pas enregistr\u00e9 dans les listes de widgets Focusables"));
                    }
                }
            }
        }
        this.setFocused(newFocused);
    }
    
    static {
        FocusManager.m_logger = Logger.getLogger((Class)FocusManager.class);
        FocusManager.m_instance = new FocusManager();
    }
}
