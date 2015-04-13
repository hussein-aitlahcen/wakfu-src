package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.weather.*;

@XulorActionsTag
public class WeatherInformationDialogActions
{
    public static final String PACKAGE = "wakfu.weatherInformation";
    
    public static void closeDialog(final Event e) {
        WakfuGameEntity.getInstance().removeFrame(UIWeatherInfoFrame.getInstance());
    }
    
    public static void onCulturesSelectionChange(final SelectionChangedEvent e) {
        WeatherInfoManager.getInstance().setDisplayCultures(e.isSelected());
    }
    
    public static void onPlantsSelectionChange(final SelectionChangedEvent e) {
        WeatherInfoManager.getInstance().setDisplayPlants(e.isSelected());
    }
    
    public static void onTreesSelectionChange(final SelectionChangedEvent e) {
        WeatherInfoManager.getInstance().setDisplayTrees(e.isSelected());
    }
}
