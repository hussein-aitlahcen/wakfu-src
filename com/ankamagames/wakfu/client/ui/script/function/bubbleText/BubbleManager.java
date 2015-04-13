package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import gnu.trove.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import java.util.*;

final class BubbleManager
{
    private static final BubbleManager m_instance;
    private final TIntObjectHashMap<BubbleClosedListener> m_adviserEventsObservers;
    private final TIntObjectHashMap<BubbleClosedListener> m_endFunctionRunners;
    private final TIntObjectHashMap<InteractiveBubble> m_interactiveBubbles;
    private final WeakHashMap<Integer, WakfuBubbleWidget> m_wakfuBubbles;
    
    private BubbleManager() {
        super();
        this.m_adviserEventsObservers = new TIntObjectHashMap<BubbleClosedListener>();
        this.m_endFunctionRunners = new TIntObjectHashMap<BubbleClosedListener>();
        this.m_interactiveBubbles = new TIntObjectHashMap<InteractiveBubble>();
        this.m_wakfuBubbles = new WeakHashMap<Integer, WakfuBubbleWidget>();
    }
    
    public static BubbleManager getInstance() {
        return BubbleManager.m_instance;
    }
    
    public BubbleClosedListener getEndFunctionRunner(final int bubbleId) {
        return this.m_endFunctionRunners.get(bubbleId);
    }
    
    public void putInEndFunctionRunners(final int dialogId, final BubbleClosedListener bubbleClosedListener) {
        this.m_endFunctionRunners.put(dialogId, bubbleClosedListener);
    }
    
    public void removeFromEndFunctionRunners(final int bubbleId) {
        this.m_endFunctionRunners.remove(bubbleId);
    }
    
    public void putInAdviserEventObservers(final int observerId, final BubbleClosedListener bcl) {
        this.m_adviserEventsObservers.put(observerId, bcl);
    }
    
    public void removeFromAdviserEventObservers(final int bubbleId) {
        this.m_adviserEventsObservers.remove(bubbleId);
    }
    
    public BubbleClosedListener getAdviserEventObserver(final int bubbleId) {
        return this.m_adviserEventsObservers.get(bubbleId);
    }
    
    public void putInInteractiveBubbles(final int bubbleUid, final InteractiveBubble bubble) {
        this.m_interactiveBubbles.put(bubbleUid, bubble);
    }
    
    public InteractiveBubble getInteractiveBubble(final int bubbleId) {
        return this.m_interactiveBubbles.get(bubbleId);
    }
    
    public InteractiveBubble removeFromInteractiveBubbles(final int bubbleId) {
        return this.m_interactiveBubbles.remove(bubbleId);
    }
    
    public void clear() {
        this.m_interactiveBubbles.clear();
        this.m_adviserEventsObservers.clear();
        this.m_endFunctionRunners.clear();
    }
    
    public void putWakfuBubble(final WakfuBubbleWidget bubble) {
        this.m_wakfuBubbles.put(bubble.getAdviserId(), bubble);
    }
    
    public WakfuBubbleWidget getWakfuBubble(final int bubbleId) {
        return this.m_wakfuBubbles.get(bubbleId);
    }
    
    public void removeWakfuBubble(final int bubbleId) {
        this.m_wakfuBubbles.remove(bubbleId);
    }
    
    public Collection<WakfuBubbleWidget> getBubblesAttachedTo(final AnimatedElementWithDirection mobile) {
        final Collection<WakfuBubbleWidget> list = new ArrayList<WakfuBubbleWidget>();
        for (final WakfuBubbleWidget b : this.m_wakfuBubbles.values()) {
            if (b.getTarget() == mobile) {
                list.add(b);
            }
        }
        return list;
    }
    
    static {
        m_instance = new BubbleManager();
    }
}
