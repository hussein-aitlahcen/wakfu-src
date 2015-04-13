package com.ankamagames.wakfu.client.core.game.dungeon;

import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.lock.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import org.jetbrains.annotations.*;

public class LockHelper
{
    @NotNull
    public static String getLockText(final LockContextProvider provider, final int lockId) {
        final LockContext context = provider.getLockContext();
        final TextWidgetFormater sb = new TextWidgetFormater();
        if (context.isLockedGlobally(lockId)) {
            final LockInstance lock = context.getLock(lockId);
            sb.append(WakfuTranslator.getInstance().getString("dungeon.unlockDate")).append(WakfuTranslator.getInstance().formatDateShort(lock.getUnlockDate()));
        }
        else if (context.isLockedPersonnaly(lockId)) {
            final GameDateConst nextStartTime = context.getNextStartTime(lockId);
            sb.append(WakfuTranslator.getInstance().getString("dungeon.lockedUntil")).append(WakfuTranslator.getInstance().formatDateShort(nextStartTime));
        }
        else if (context.getActualCurrentLockValue(lockId) != 0) {
            final LockInstance lock = context.getLock(lockId);
            final int remainingTries = lock.getLockValue() - context.getActualCurrentLockValue(lockId);
            final GameDateConst nextStartTime2 = context.getNextStartTime(lockId);
            final String dateText = WakfuTranslator.getInstance().formatDateShort(nextStartTime2);
            sb.append(WakfuTranslator.getInstance().getString("dungeon.remainingTriesBefore", remainingTries, dateText));
        }
        if (context.containsLock(-1) && !LockContext.isDailyLockBypassed(lockId)) {
            appendGeneralLockString(context, sb);
        }
        return sb.finishAndToString();
    }
    
    private static void appendGeneralLockString(final LockContext context, final TextWidgetFormater sb) {
        final int lockId = -1;
        if (context.isLockedPersonnaly(-1)) {
            if (sb.length() != 0) {
                sb.newLine();
            }
            sb.append(WakfuTranslator.getInstance().getString("dungeon.daily.locked"));
        }
        else if (context.getActualCurrentLockValue(-1) != 0) {
            final LockInstance lock = context.getLock(-1);
            final int remainingTries = lock.getLockValue() - context.getActualCurrentLockValue(-1);
            if (sb.length() != 0) {
                sb.newLine();
            }
            sb.append(WakfuTranslator.getInstance().getString("dungeon.daily.remainingTries", remainingTries));
        }
        else {
            final int remainingTries2 = 3;
            if (sb.length() != 0) {
                sb.newLine();
            }
            sb.append(WakfuTranslator.getInstance().getString("dungeon.daily.remainingTries", 3));
        }
    }
}
