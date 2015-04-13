package com.ankamagames.wakfu.common.account;

import com.ankamagames.baseImpl.common.clientAndServer.account.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.account.subscription.instanceRight.*;

public enum WakfuAccountPermissionContext implements AccountPermissionContext<WakfuAccountInformationHolder>
{
    SUBSCRIBER {
        @Override
        public boolean hasPermission(final WakfuAccountInformationHolder... holders) {
            for (int i = 0, size = holders.length; i < size; ++i) {
                final WakfuAccountInformationHolder holder = holders[i];
                final WakfuAccountInformationHandler handler = holder.getAccountInformationHandler();
                if (!handler.isDebuggingHackMode()) {
                    return handler.getActiveSubscriptionLevel() == SubscriptionLevel.EU_SUBSCRIBER || (handler.getActiveSubscriptionLevel() != SubscriptionLevel.EU_FREE && WakfuAccountPermissionContext$1.SUBSCRIBER_ZONE.hasPermission(holder));
                }
            }
            return true;
        }
    }, 
    SUBSCRIBER_ZONE {
        @Override
        public boolean hasPermission(final WakfuAccountInformationHolder... holders) {
            for (int i = 0, size = holders.length; i < size; ++i) {
                final WakfuAccountInformationHolder holder = holders[i];
                final WakfuAccountInformationHandler handler = holder.getAccountInformationHandler();
                if (!handler.isDebuggingHackMode()) {
                    final InstanceInteractionLevel interactionLevel = InstanceInteractionLevelManager.INSTANCE.getInstanceInteractionLevel(holder.getInstanceId(), handler.getActiveSubscriptionLevel());
                    if (interactionLevel != InstanceInteractionLevel.FULL_ACCESS) {
                        return false;
                    }
                }
            }
            return true;
        }
    }, 
    SUBSCRIBER_ZONE_CLOSED {
        @Override
        public boolean hasPermission(final WakfuAccountInformationHolder... holders) {
            for (int i = 0, size = holders.length; i < size; ++i) {
                final WakfuAccountInformationHolder holder = holders[i];
                final WakfuAccountInformationHandler handler = holder.getAccountInformationHandler();
                if (!handler.isDebuggingHackMode()) {
                    final InstanceInteractionLevel interactionLevel = InstanceInteractionLevelManager.INSTANCE.getInstanceInteractionLevel(holder.getInstanceId(), handler.getActiveSubscriptionLevel());
                    if (interactionLevel == InstanceInteractionLevel.FORBIDDEN_ACCESS) {
                        return false;
                    }
                }
            }
            return true;
        }
    };
    
    @Override
    public abstract boolean hasPermission(final WakfuAccountInformationHolder... p0);
}
