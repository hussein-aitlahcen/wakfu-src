package com.ankamagames.wakfu.client.core.effect;

import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.*;

public final class SubscriptionEndPopupDisplayer
{
    public static final SubscriptionEndPopupDisplayer INSTANCE;
    
    public void displayPopupIfNecessary(final SubscriptionLevel previousLevel, final SubscriptionLevel newLevel) {
        if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.DISPLAY_SUBSCRIPTION_END_POPUP)) {
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (previousLevel == null || previousLevel == SubscriptionLevel.UNKNOWN) {
            return;
        }
        if (previousLevel == newLevel) {
            return;
        }
        final boolean wasAuthorized = this.instanceWasAuthorized(previousLevel, localPlayer);
        if (!wasAuthorized) {
            return;
        }
        final boolean zoneAuthorized = WakfuAccountPermissionContext.SUBSCRIBER_ZONE_CLOSED.hasPermission(localPlayer);
        if (zoneAuthorized) {
            return;
        }
        this.displayMsgBox();
    }
    
    private void displayMsgBox() {
        final MessageBoxData data = new MessageBoxData(102, 0, WakfuTranslator.getInstance().getString("subscription.end.notification"), 2L);
        final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                Xulor.getInstance().load("subscriptionEndedDialog", Dialogs.getDialogPath("subscriptionEndedDialog"), 8192L, (short)10000);
            }
        });
    }
    
    private boolean instanceWasAuthorized(final SubscriptionLevel previousLevel, final LocalPlayerCharacter localPlayer) {
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE_CLOSED.hasPermission(new WakfuAccountInformationHolder() {
            @Override
            public WakfuAccountInformationHandler getAccountInformationHandler() {
                final WakfuAccountInformationHandler wakfuAccountInformationHandler = new WakfuAccountInformationHandler();
                wakfuAccountInformationHandler.copyFrom(localPlayer.getAccountInformationHandler());
                wakfuAccountInformationHandler.setForcedSubscriptionLevel(previousLevel);
                return wakfuAccountInformationHandler;
            }
            
            @Override
            public short getInstanceId() {
                return localPlayer.getInstanceId();
            }
        });
    }
    
    static {
        INSTANCE = new SubscriptionEndPopupDisplayer();
    }
}
