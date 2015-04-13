package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characteristics.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class CharacterSheetDialogActions extends CompanionsEmbeddedActions
{
    public static final String PACKAGE = "wakfu.characterSheet";
    
    public static void switchToCharacteristicsMode(final Event e) {
        final boolean value = PropertiesProvider.getInstance().getBooleanProperty("characterSheetSecondMode");
        PropertiesProvider.getInstance().setPropertyValue("characterSheetSecondMode", !value);
        WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.APTITUDE_DISPLAY_MODE_KEY, !value);
    }
    
    public static void openCloseAptitudes(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UIAptitudeBonusFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().removeFrame(UIAptitudeBonusFrame.INSTANCE);
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIAptitudeBonusFrame.INSTANCE);
        }
    }
    
    public static void highlightCharacteristic(final SelectionChangedEvent e, final BaseCharacteristicView view) {
        final CharacterView characterView = (CharacterView)PropertiesProvider.getInstance().getObjectProperty("characterSheet", e.getTarget().getElementMap());
        final CharacteristicViewProvider provider = characterView.getCharacterInfo().getCharacteristicViewProvider();
        provider.setHighlighted(view.getType(), e.isSelected());
    }
    
    public static void displayAllCharacteristics(final SelectionChangedEvent e) {
        UICharacterSheetFrame.getInstance().displayAllCharacteristics(e.isSelected());
    }
    
    public static void dropView(final DropOutEvent e) {
        final ShortCharacterView shortCharacterView = (ShortCharacterView)e.getSourceValue();
        if (shortCharacterView.isPlayer()) {
            return;
        }
        final int screenX = e.getScreenX();
        final int screenY = e.getScreenY();
        UICharacterSheetFrame.getInstance().loadSecondaryDialog(UICompanionsEmbeddedFrame.getCharacterSheetView(shortCharacterView.getBreedId()), "characterSheetWindow", screenX, screenY);
    }
}
