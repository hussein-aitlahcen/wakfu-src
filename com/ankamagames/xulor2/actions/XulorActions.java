package com.ankamagames.xulor2.actions;

import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@XulorActionsTag
public class XulorActions
{
    public static final String PACKAGE = "xulor";
    private static PopupMessageHandler m_popupMessageHandler;
    
    public static boolean unloadDialog(final Event event) {
        final EventDispatcher element = event.getTarget();
        if (element != null) {
            final String id = element.getElementMap().getId();
            Xulor.getInstance().unload(id, false);
        }
        return false;
    }
    
    public static void switchDrawer(final Event e, final Drawer combo) {
        combo.switchPopup();
    }
    
    private static boolean popup(final Widget target, final PopupElement popup, final float duration, final TextWidget textWidget) {
        if (textWidget != null && !textWidget.isTextReduced()) {
            return false;
        }
        if (target != null && popup != null && !MasterRootContainer.getInstance().isDragging()) {
            MessageScheduler.getInstance().removeAllClocks(XulorActions.m_popupMessageHandler);
            XulorActions.m_popupMessageHandler.setPopup(popup);
            XulorActions.m_popupMessageHandler.setWidget(target);
            if (duration == 0.0f) {
                popup.show(target);
            }
            else {
                MessageScheduler.getInstance().addClock(XulorActions.m_popupMessageHandler, (long)(1000.0f * duration), -1, 1);
            }
        }
        return false;
    }
    
    public static boolean popup(final Event event, final PopupElement popup, final String duration) {
        return popup(event, popup, Float.valueOf(duration));
    }
    
    private static ObjectPair<PopupElement, Widget> getPopupAndTarget(final Event event, final PopupElement popup) {
        final EventDispatcher e = event.getTarget();
        if (!(e instanceof Widget)) {
            return null;
        }
        final Widget widget = (Widget)e;
        final ObjectPair<PopupElement, Widget> res = new ObjectPair<PopupElement, Widget>();
        if (widget.getPopup() != null) {
            res.setFirst(widget.getPopup());
            res.setSecond(widget);
        }
        else if (popup != null) {
            res.setFirst(popup);
            res.setSecond(popup.getParentOfType(Widget.class));
        }
        return res;
    }
    
    private static boolean popup(final Event event, final PopupElement popup, final float duration) {
        final ObjectPair<PopupElement, Widget> popupAndTarget = getPopupAndTarget(event, popup);
        if (popupAndTarget == null) {
            return false;
        }
        popup(popupAndTarget.getFirst(), popupAndTarget.getSecond(), duration);
        return false;
    }
    
    public static boolean popup(final Event event) {
        final EventDispatcher e = event.getTarget();
        if (!(e instanceof Widget)) {
            return false;
        }
        final PopupElement popupElement = ((Widget)e).getPopup();
        if (popupElement != null) {
            popup(event, popupElement);
        }
        return false;
    }
    
    public static boolean popup(final Event event, final PopupElement popup) {
        return popup(event, popup, 0.0f);
    }
    
    public static boolean popup(final PopupElement popup, final Widget target, final float duration) {
        return popup(target, popup, duration, null);
    }
    
    public static boolean popup(final PopupElement popup, final Widget target) {
        return popup(popup, target, 0.0f);
    }
    
    public static boolean popup(final Event event, final PopupElement popup, final Widget target) {
        return popup(popup, target);
    }
    
    public static boolean popupIfReduced(final Event event, final PopupElement popup, final Widget target, final TextWidget textWidget) {
        return popup(target, popup, 0.0f, textWidget);
    }
    
    public static boolean popupIfReduced(final Event event, final PopupElement popup, final TextWidget textWidget) {
        final ObjectPair<PopupElement, Widget> popupAndTarget = getPopupAndTarget(event, popup);
        return popupAndTarget != null && popup(popupAndTarget.getSecond(), popupAndTarget.getFirst(), 0.0f, textWidget);
    }
    
    public static boolean popupIfReduced(final Event event, final PopupElement popup, final TextWidget textWidget, final String delay) {
        final ObjectPair<PopupElement, Widget> popupAndTarget = getPopupAndTarget(event, popup);
        return popupAndTarget != null && popup(popupAndTarget.getSecond(), popupAndTarget.getFirst(), PrimitiveConverter.getFloat(delay, 0.0f), textWidget);
    }
    
    public static boolean popupIfReduced(final Event event, final PopupElement popup, final Widget target, final TextWidget textWidget, final String delay) {
        return popup(target, popup, PrimitiveConverter.getFloat(delay, 0.0f), textWidget);
    }
    
    public static boolean popup(final Event event, final PopupElement popup, final Widget target, final String delay) {
        return popup(target, popup, PrimitiveConverter.getFloat(delay, 0.0f), null);
    }
    
    public static boolean closePopup(final Event event) {
        MessageScheduler.getInstance().removeAllClocks(XulorActions.m_popupMessageHandler);
        MasterRootContainer.getInstance().getPopupContainer().hide();
        return false;
    }
    
    public static boolean closePopup(final Event event, final PopupElement popup) {
        MessageScheduler.getInstance().removeAllClocks(XulorActions.m_popupMessageHandler);
        MasterRootContainer.getInstance().getPopupContainer().hide();
        return false;
    }
    
    public static boolean openClosePopup(final Event event, final PopupElement popup) {
        if (MasterRootContainer.getInstance().isDragging()) {
            return false;
        }
        final EventDispatcher e = event.getTarget();
        if (!(e instanceof Widget)) {
            return false;
        }
        PopupElement popupElement = ((Widget)e).getPopup();
        if (popupElement == null) {
            popupElement = popup;
        }
        popupElement.toggle((PopupClient)e);
        return false;
    }
    
    public static void playSound(final Event e, final String soundIDString) {
        final int soundId = PrimitiveConverter.getInteger(soundIDString, -1);
        if (soundId != -1) {
            XulorSoundManager.getInstance().playSound(soundId);
        }
    }
    
    public static boolean toggleVisible(final Event e, final Widget w) {
        return toggleVisible(w);
    }
    
    public static boolean toggleVisible(final Widget w) {
        if (w != null) {
            w.setVisible(!w.getVisible());
        }
        return false;
    }
    
    public static boolean setupLook(final Event event, final Window w, final String name) {
        return false;
    }
    
    public static boolean foldUnfold(final Event event) {
        final FoldingContainer f = event.getTarget().getParentOfType(FoldingContainer.class);
        if (f != null) {
            if (f.isFolded()) {
                f.unfold();
            }
            else {
                f.fold();
            }
        }
        return false;
    }
    
    public static boolean unfold(final Event event) {
        final FoldingContainer f = event.getTarget().getParentOfType(FoldingContainer.class);
        if (f != null && f.isFolded()) {
            f.unfold();
        }
        return false;
    }
    
    public static boolean fold(final Event event) {
        final FoldingContainer f = event.getTarget().getParentOfType(FoldingContainer.class);
        if (f != null && !f.isFolded()) {
            f.fold();
        }
        return false;
    }
    
    static {
        XulorActions.m_popupMessageHandler = new PopupMessageHandler();
    }
    
    private static class PopupMessageHandler implements MessageHandler
    {
        private PopupElement m_popup;
        private Widget m_widget;
        
        @Override
        public boolean onMessage(final Message message) {
            if (this.m_popup != null && this.m_widget != null) {
                this.m_popup.show(this.m_widget);
            }
            return false;
        }
        
        @Override
        public long getId() {
            return 1L;
        }
        
        @Override
        public void setId(final long id) {
        }
        
        public PopupElement getPopup() {
            return this.m_popup;
        }
        
        public void setPopup(final PopupElement popup) {
            this.m_popup = popup;
        }
        
        public Widget getWidget() {
            return this.m_widget;
        }
        
        public void setWidget(final Widget widget) {
            this.m_widget = widget;
        }
    }
}
