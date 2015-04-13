package com.ankamagames.wakfu.client.ui.mru;

import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import java.util.*;

public class MRUActionDirectRunner
{
    private final Class<? extends AbstractMRUAction> m_type;
    private final CursorFactory.CursorType m_normalIcon;
    private final CursorFactory.CursorType m_highlightIcon;
    private boolean m_started;
    
    public MRUActionDirectRunner(final Class<? extends AbstractMRUAction> type, final CursorFactory.CursorType normalIcon, final CursorFactory.CursorType highlightIcon) {
        super();
        this.m_type = type;
        this.m_normalIcon = normalIcon;
        this.m_highlightIcon = highlightIcon;
    }
    
    public void start() {
        this.m_started = true;
        CursorFactory.getInstance().show(this.m_normalIcon, true);
    }
    
    public void stop() {
        this.m_started = false;
        CursorFactory.getInstance().unlock();
    }
    
    public void onMouseMove(final WakfuWorldScene worldScene, final int x, final int y) {
        if (!this.m_started) {
            return;
        }
        final MRUable mrUable = this.getFirstValidMRUAbleAt(worldScene, x, y);
        if (mrUable != null) {
            CursorFactory.getInstance().show(this.m_highlightIcon, true);
        }
        else {
            CursorFactory.getInstance().show(this.m_normalIcon, true);
        }
    }
    
    public void onMouseClick(final WakfuWorldScene scene, final int x, final int y) {
        if (!this.m_started) {
            return;
        }
        final MRUable mrUable = this.getFirstValidMRUAbleAt(scene, x, y);
        if (mrUable != null) {
            final AbstractMRUAction action = this.getFirstValidAction(mrUable);
            if (action != null) {
                action.run();
            }
        }
        CursorFactory.getInstance().unlock();
        this.m_started = false;
    }
    
    private MRUable getFirstValidMRUAbleAt(final WakfuWorldScene scene, final int x, final int y) {
        final ArrayList<AnimatedInteractiveElement> displayedElementsMouseOver = scene.selectAllNearestElement(x, y);
        if (displayedElementsMouseOver.isEmpty()) {
            return null;
        }
        for (int i = 0, size = displayedElementsMouseOver.size(); i < size; ++i) {
            Object candidate;
            final AnimatedInteractiveElement elem = (AnimatedInteractiveElement)(candidate = displayedElementsMouseOver.get(i));
            if (elem instanceof CharacterActor) {
                candidate = ((CharacterActor)elem).getCharacterInfo();
            }
            if (candidate instanceof MRUable) {
                final MRUable mrUable = (MRUable)candidate;
                if (this.isValid(mrUable)) {
                    return mrUable;
                }
            }
        }
        return null;
    }
    
    private boolean isValid(final MRUable elem) {
        return this.getFirstValidAction(elem) != null;
    }
    
    private AbstractMRUAction getFirstValidAction(final MRUable mrUable) {
        final AbstractMRUAction[] arr$;
        final AbstractMRUAction[] mruActions = arr$ = mrUable.getMRUActions();
        for (final AbstractMRUAction action : arr$) {
            if (action.getClass().isAssignableFrom(this.m_type)) {
                action.initFromSource(mrUable);
                if (action.isRunnable() && action.isEnabled()) {
                    return action;
                }
            }
        }
        return null;
    }
}
