package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

final class ModerationCommandUtils
{
    protected static GameDate getGameDate(final String startDate) {
        final String[] strings = startDate.split(" ");
        if (strings.length < 2) {
            return null;
        }
        final String[] date = strings[0].split("/");
        if (date.length < 3) {
            return null;
        }
        final String[] hour = strings[1].split(":");
        if (hour.length < 2) {
            return null;
        }
        return new GameDate(0, Integer.parseInt(hour[1]), Integer.parseInt(hour[0]), Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
    }
}
