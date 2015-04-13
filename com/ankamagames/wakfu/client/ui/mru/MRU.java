package com.ankamagames.wakfu.client.ui.mru;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.shortKey.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import java.awt.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.event.*;

public class MRU implements EventListener
{
    private static final Logger m_logger;
    private final ShortObjectLightWeightMap<AnimatedInteractiveElement> m_displaybleObject;
    private final ShortObjectLightWeightMap<MRUable> m_sources;
    private final ShortObjectLightWeightMap<AbstractMRUAction[]> m_actions;
    private short m_groupId;
    private int m_currentMRUGroup;
    private UniversalRadialMenu m_mruWidget;
    
    public MRU() {
        super();
        this.m_displaybleObject = new ShortObjectLightWeightMap<AnimatedInteractiveElement>();
        this.m_sources = new ShortObjectLightWeightMap<MRUable>();
        this.m_actions = new ShortObjectLightWeightMap<AbstractMRUAction[]>();
        this.m_groupId = 0;
        this.m_currentMRUGroup = 0;
        this.m_mruWidget = null;
    }
    
    public void display() {
        (this.m_mruWidget = Xulor.getInstance().mru()).addEventListener(Events.UNIVERSAL_RADIAL_MENU_GROUP_CHANGED, this, true);
        this.m_mruWidget.addEventListener(Events.WIDGET_REMOVAL_REQUESTED, this, false);
        if (this.m_sources != null && this.m_sources.size() > 0) {
            this.m_mruWidget.setRadius(this.m_sources.getQuickValue(this.m_sources.size() - 1).getMRUHeight());
        }
        else {
            this.m_mruWidget.setRadius(0);
        }
        AnimatedInteractiveElement displayedObject = null;
        Point p = null;
        for (short i = 0; i < this.m_displaybleObject.size(); ++i) {
            boolean b_newGroupCreated = false;
            final MRUable source = this.m_sources.get(i);
            if (source != null) {
                final AbstractMRUAction[] actions = source.getMRUActions();
                if (actions != null) {
                    if (displayedObject == null) {
                        displayedObject = this.m_displaybleObject.get(i);
                    }
                    for (int j = 0; j < actions.length; ++j) {
                        final AbstractMRUAction action = actions[j];
                        action.initFromSource(source);
                        boolean usable = false;
                        try {
                            usable = action.isUsable();
                        }
                        catch (Exception e) {
                            MRU.m_logger.error((Object)("Exception lev\u00e9e en d\u00e9terminant si l'action " + action.getClass().getSimpleName() + " est usable"), (Throwable)e);
                        }
                        boolean runnable = false;
                        try {
                            runnable = action.isRunnable();
                        }
                        catch (Exception e2) {
                            MRU.m_logger.error((Object)("Exception lev\u00e9e en d\u00e9terminant si l'action " + action.getClass().getSimpleName() + " est runnable"), (Throwable)e2);
                        }
                        if (usable) {
                            if (runnable) {
                                if (!b_newGroupCreated) {
                                    this.m_mruWidget.newGroup();
                                    b_newGroupCreated = true;
                                }
                                this.m_mruWidget.addButton(null, action.getTooltip(), action.getComplementaryTooltip(), null, action.getStyle(), action.getParticleDecorators(), new MouseClickedListener() {
                                    @Override
                                    public boolean run(final Event event) {
                                        action.run();
                                        return false;
                                    }
                                }, action.isEnabled());
                            }
                        }
                    }
                    if (source.isMRUPositionable()) {
                        p = source.getMRUScreenPosition();
                    }
                }
            }
            else {
                final AbstractMRUAction[] abstractMRUActions = this.m_actions.get(i);
                if (abstractMRUActions != null) {
                    final AnimatedInteractiveElement elem = this.m_displaybleObject.get(i);
                    for (final AbstractMRUAction action2 : abstractMRUActions) {
                        if (action2.isUsable()) {
                            if (action2.isRunnable()) {
                                if (displayedObject == null) {
                                    displayedObject = elem;
                                }
                                if (!b_newGroupCreated) {
                                    this.m_mruWidget.newGroup();
                                    b_newGroupCreated = true;
                                }
                                final AleaWorldScene scene = WakfuClientInstance.getInstance().getWorldScene();
                                final Point2 pt = IsoCameraFunc.getScreenPositionFromBottomLeft(scene, elem.getWorldX(), elem.getWorldY(), elem.getAltitude() + elem.getVisualHeight());
                                p = new Point((int)pt.m_x, (int)pt.m_y);
                                this.m_mruWidget.addButton(null, action2.getTooltip(), action2.getComplementaryTooltip(), null, action2.getStyle(), action2.getParticleDecorators(), new MouseClickedListener() {
                                    @Override
                                    public boolean run(final Event event) {
                                        action2.run();
                                        return false;
                                    }
                                }, action2.isEnabled());
                            }
                        }
                    }
                }
            }
        }
        if (this.m_mruWidget.getGroupSize() > 0) {
            WakfuSoundManager.getInstance().playGUISound(600072L);
            p = null;
            if (p != null) {
                Xulor.getInstance().showMRU(this.m_mruWidget, p.x, p.y);
            }
            else {
                Xulor.getInstance().showMRU(this.m_mruWidget);
            }
        }
        if (displayedObject != null && displayedObject.isHighlightable()) {
            MobileColorizeHelper.onSelected(displayedObject);
        }
    }
    
    public void add(final MRUable o, final AnimatedInteractiveElement displayableObject) {
        final AbstractMRUAction[] actions = o.getMRUActions();
        if (actions == null || actions.length == 0) {
            return;
        }
        this.m_displaybleObject.put(this.m_groupId, displayableObject);
        this.m_sources.put(this.m_groupId, o);
        ++this.m_groupId;
    }
    
    public void add(final AbstractMRUAction[] o, final AnimatedInteractiveElement displayableObject) {
        this.m_displaybleObject.put(this.m_groupId, displayableObject);
        this.m_actions.put(this.m_groupId, o);
        ++this.m_groupId;
    }
    
    public final int getSourcesCount() {
        return this.m_sources.size();
    }
    
    public final MRUable getSource(final int index) {
        return this.m_sources.getQuickValue(index);
    }
    
    public final boolean isDisplayable() {
        return this.m_sources.size() > 0 || this.m_actions.size() > 0;
    }
    
    public void closeMRUWidget() {
        if (this.m_mruWidget != null) {
            this.m_mruWidget.destroySelfFromParent();
            this.m_mruWidget = null;
            this.cleanUp();
        }
    }
    
    private void cleanUp() {
        UIMRUFrame.getInstance().setCurrentMRU(null);
        for (short i = 0; i < this.m_displaybleObject.size(); ++i) {
            final AnimatedInteractiveElement AIE = this.m_displaybleObject.get(i);
            if (AIE != null) {
                AIE.resetColor();
            }
        }
    }
    
    @Override
    public boolean run(final Event event) {
        if (event.getType() == Events.UNIVERSAL_RADIAL_MENU_GROUP_CHANGED) {
            final UniversalRadialMenuGroupChanged URMGC = (UniversalRadialMenuGroupChanged)event;
            if (URMGC.getGroup() < this.m_displaybleObject.size()) {
                final AnimatedInteractiveElement AIE = this.m_displaybleObject.get((short)URMGC.getGroup());
                if (AIE != null && AIE.isHighlightable()) {
                    MobileColorizeHelper.onSelected(AIE);
                }
            }
            final AnimatedInteractiveElement current = this.m_displaybleObject.get((short)this.m_currentMRUGroup);
            if (current != null) {
                current.resetColor();
            }
            this.m_currentMRUGroup = URMGC.getGroup();
        }
        if (event.getType() == Events.WIDGET_REMOVAL_REQUESTED) {
            this.cleanUp();
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MRU.class);
    }
}
