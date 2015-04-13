package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class NationSelectionPanelDialogActions
{
    protected static final Logger m_logger;
    public static final String PACKAGE = "wakfu.nationSelectionPanel";
    
    public static void chooseNation(final Event event, final NationSelectionView nationSelectionView) {
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setIntValue(nationSelectionView.getNationId());
        uiMessage.setId(19007);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void onMouseOverElement(final Event event, final NationSelectionView selectionView) {
        if (!(event instanceof MouseEvent)) {
            return;
        }
        if (WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getNation() != Nation.VOID_NATION) {
            return;
        }
        final MouseEvent me = (MouseEvent)event;
        final EventDispatcher target = event.getTarget();
        final List l = (List)target.getElementMap().getElement("list");
        final NationSelectionView currentNationSelectionView = (NationSelectionView)PropertiesProvider.getInstance().getObjectProperty("selectedNation");
        if (currentNationSelectionView != null) {
            final RenderableContainer renderableByOffset = l.getRenderableByOffset(l.getSelectedOffsetByValue(currentNationSelectionView));
            if (renderableByOffset.getScreenX() > me.getScreenX() || renderableByOffset.getScreenX() + renderableByOffset.getWidth() < me.getScreenX() || renderableByOffset.getScreenY() > me.getScreenY() || renderableByOffset.getScreenY() + renderableByOffset.getHeight() < me.getScreenY()) {
                return;
            }
        }
        PropertiesProvider.getInstance().setPropertyValue("selectedNation", selectionView);
    }
    
    public static void onMouseOutElement(final Event event) {
        if (!(event instanceof MouseEvent)) {
            return;
        }
        if (WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getNation() != Nation.VOID_NATION) {
            return;
        }
        final MouseEvent me = (MouseEvent)event;
        final EventDispatcher target = event.getTarget();
        final List l = (List)target.getElementMap().getElement("list");
        final NationSelectionView currentNationSelectionView = (NationSelectionView)PropertiesProvider.getInstance().getObjectProperty("selectedNation");
        if (currentNationSelectionView != null) {
            final RenderableContainer renderableByOffset = l.getRenderableByOffset(l.getSelectedOffsetByValue(currentNationSelectionView));
            if (renderableByOffset.getScreenX() <= me.getScreenX() && renderableByOffset.getScreenX() + renderableByOffset.getWidth() >= me.getScreenX() && renderableByOffset.getScreenY() <= me.getScreenY() && renderableByOffset.getScreenY() + renderableByOffset.getHeight() >= me.getScreenY()) {
                return;
            }
        }
        PropertiesProvider.getInstance().setPropertyValue("selectedNation", null);
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationSelectionPanelDialogActions.class);
    }
}
