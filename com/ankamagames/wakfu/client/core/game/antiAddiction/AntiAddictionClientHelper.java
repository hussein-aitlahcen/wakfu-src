package com.ankamagames.wakfu.client.core.game.antiAddiction;

import com.ankamagames.wakfu.common.game.antiAddiction.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public final class AntiAddictionClientHelper
{
    public static final int NOTIFICATION_DELAY_MIN = 15;
    public static final long NOTIFICATION_DELAY_MS = 900000L;
    private static final Runnable NOTIFIER_RUNNABLE;
    
    public static void sendNotification(final AntiAddictionLevel level) {
        final String title = WakfuTranslator.getInstance().getString("antiAddictionLevel.name." + level.getId());
        final String text = WakfuTranslator.getInstance().getString("antiAddictionLevel.desc." + level.getId());
        if (StringUtils.isEmptyOrNull(title) && StringUtils.isEmptyOrNull(text)) {
            return;
        }
        final Message uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.CHARACTER, 600011);
        Worker.getInstance().pushMessage(uiNotificationMessage);
        ChatManager.getInstance().pushMessage(text, 4);
        if (level == AntiAddictionLevel.LEVEL_7) {
            RemoveRunnable();
            ProcessScheduler.getInstance().schedule(AntiAddictionClientHelper.NOTIFIER_RUNNABLE, 900000L, 1);
        }
    }
    
    public static void RemoveRunnable() {
        ProcessScheduler.getInstance().remove(AntiAddictionClientHelper.NOTIFIER_RUNNABLE);
    }
    
    static {
        NOTIFIER_RUNNABLE = new NotifierRunnable();
    }
    
    private static class NotifierRunnable implements Runnable
    {
        @Override
        public void run() {
            AntiAddictionClientHelper.sendNotification(AntiAddictionLevel.LEVEL_7);
        }
    }
}
