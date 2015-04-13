package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.graphics.image.*;

@XulorActionsTag
public class HavenWorldPanelDialogActions
{
    protected static final Logger m_logger;
    public static final String PACKAGE = "wakfu.havenWorldPanel";
    
    public static void toggleOpenBuildingList(final Event event) {
        final HavenWorldView havenWorldView = (HavenWorldView)PropertiesProvider.getInstance().getObjectProperty("havenWorld");
        havenWorldView.setBuildingsOpenned(!havenWorldView.isBuildingsOpenned());
    }
    
    public static void toggleOpenEvolutingBuildingList(final Event event) {
        final HavenWorldView havenWorldView = (HavenWorldView)PropertiesProvider.getInstance().getObjectProperty("havenWorld");
        havenWorldView.setEvolutingBuildingsOpenned(!havenWorldView.isEvolutingBuildingsOpenned());
    }
    
    public static void onMouseOverElement(final Event event, final HavenWorldElementView buildingDefinitionView, final PopupElement popupElement) {
        XulorActions.popup(event, popupElement);
        final Widget w = event.getTarget();
        w.getAppearance().setModulationColor(WakfuClientConstants.WAKFU_COLOR);
        PropertiesProvider.getInstance().setPropertyValue("havenWorldOverCatalogEntry", buildingDefinitionView.getCatalogEntryView());
    }
    
    public static void onMouseOutElement(final Event event, final HavenWorldElementView buildingDefinitionView, final PopupElement popupElement) {
        XulorActions.closePopup(event, popupElement);
        final Widget w = event.getTarget();
        if (buildingDefinitionView.equals(PropertiesProvider.getInstance().getObjectProperty("havenWorldSelectedCatalogEntry"))) {
            w.getAppearance().setModulationColor(WakfuClientConstants.WAKFU_COLOR);
        }
        else {
            w.getAppearance().setModulationColor(Color.WHITE);
        }
        PropertiesProvider.getInstance().setPropertyValue("havenWorldOverCatalogEntry", null);
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldPanelDialogActions.class);
    }
}
