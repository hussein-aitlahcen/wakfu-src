package com.ankamagames.xulor2.core;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;

public class ModalManager
{
    private static ModalManager m_modalManager;
    public static short BOTTOM_MODAL_LEVEL;
    public static short TOP_MODAL_LEVEL;
    public static short MSG_BOX_MODAL_LEVEL;
    public static short POP_UP_MODAL_LEVEL;
    private ArrayList<EventDispatcher> m_modals;
    private short m_maxModalLevel;
    private static Comparator<EventDispatcher> m_comparator;
    private Container m_background;
    
    private ModalManager() {
        super();
        this.m_modals = new ArrayList<EventDispatcher>();
        this.m_maxModalLevel = 0;
    }
    
    public static ModalManager getInstance() {
        return ModalManager.m_modalManager;
    }
    
    public void addPseudoModalElement(final EventDispatcher element) {
        if (element.getModalLevel() > this.m_maxModalLevel) {
            this.m_maxModalLevel = element.getModalLevel();
            FocusManager.getInstance().checkForModality();
        }
        this.m_modals.add(element);
        Collections.sort(this.m_modals, ModalManager.m_comparator);
    }
    
    public void addModalElement(final EventDispatcher element) {
        if (this.m_maxModalLevel + 1 < ModalManager.MSG_BOX_MODAL_LEVEL) {
            this.m_maxModalLevel = ModalManager.MSG_BOX_MODAL_LEVEL;
        }
        else {
            ++this.m_maxModalLevel;
        }
        element.setModalLevel(this.m_maxModalLevel);
        FocusManager.getInstance().checkForModality();
        this.m_modals.add(element);
        Collections.sort(this.m_modals, ModalManager.m_comparator);
        final MasterRootContainer rootContainer = MasterRootContainer.getInstance();
        final LayeredContainer lc = rootContainer.getLayeredContainer();
        final int positionInLayer = lc.getWidgetPositionInLayer((Widget)element);
        if (this.m_modals.size() == 1) {
            (this.m_background = new Container()).onCheckOut();
            final StaticLayoutData staticLayoutData = new StaticLayoutData();
            staticLayoutData.onCheckOut();
            staticLayoutData.setAlign(Alignment17.CENTER);
            staticLayoutData.setSize(rootContainer.getSize());
            this.m_background.setLayoutData(staticLayoutData);
            final PlainBackground plainBackground = new PlainBackground();
            plainBackground.onCheckOut();
            plainBackground.setColor(Xulor.getInstance().getModalBackgroundColor());
            this.m_background.getAppearance().addBasicElement(plainBackground);
            this.m_background.setSize(rootContainer.getSize());
            lc.addWidgetToLayer(this.m_background, 26000, positionInLayer);
        }
        else {
            lc.setWidgetPositionInLayer(this.m_background, positionInLayer - 1);
        }
    }
    
    public void removeElement(final EventDispatcher element) {
        if (element.getModalLevel() == this.m_maxModalLevel) {
            this.m_maxModalLevel = 0;
            for (final EventDispatcher stackElement : this.m_modals) {
                if (stackElement.getModalLevel() > this.m_maxModalLevel) {
                    this.m_maxModalLevel = stackElement.getModalLevel();
                }
            }
        }
        final boolean removed = this.m_modals.remove(element);
        if (removed) {
            if (this.m_modals.isEmpty()) {
                this.m_background.destroySelfFromParent();
            }
            else {
                final MasterRootContainer rootContainer = MasterRootContainer.getInstance();
                final LayeredContainer lc = rootContainer.getLayeredContainer();
                final EventDispatcher eventDispatcher = this.m_modals.get(this.m_modals.size() - 1);
                final int positionInLayer = lc.getWidgetPositionInLayer((Widget)eventDispatcher);
                final int backgroundPositionInLayer = lc.getWidgetPositionInLayer(this.m_background);
                lc.setWidgetPositionInLayer(this.m_background, positionInLayer - ((backgroundPositionInLayer < positionInLayer) ? 1 : 0));
            }
        }
    }
    
    public void removeAllElements() {
        this.m_modals.clear();
    }
    
    public short getMaxModalLevel() {
        return this.m_maxModalLevel;
    }
    
    public boolean isCoordinateActive(final int x, final int y) {
        if (!this.m_modals.isEmpty()) {
            Widget w;
            for (w = MasterRootContainer.getInstance().getWidget(x, y); w != null && w.getModalLevel() == -1 && w != MasterRootContainer.getInstance(); w = w.getParentOfType(Widget.class)) {}
            if (w != null) {
                return w.getModalLevel() >= this.m_modals.get(0).getModalLevel();
            }
        }
        return true;
    }
    
    public boolean isEmpty() {
        return this.m_modals.isEmpty();
    }
    
    static {
        ModalManager.m_modalManager = new ModalManager();
        ModalManager.BOTTOM_MODAL_LEVEL = 1;
        ModalManager.TOP_MODAL_LEVEL = 10000;
        ModalManager.MSG_BOX_MODAL_LEVEL = 20000;
        ModalManager.POP_UP_MODAL_LEVEL = 30000;
        ModalManager.m_comparator = new Comparator<EventDispatcher>() {
            @Override
            public int compare(final EventDispatcher o1, final EventDispatcher o2) {
                return o2.getModalLevel() - o1.getModalLevel();
            }
        };
    }
}
