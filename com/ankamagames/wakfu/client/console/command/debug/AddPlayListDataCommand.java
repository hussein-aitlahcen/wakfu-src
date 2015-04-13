package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;

public class AddPlayListDataCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final long musicId = PrimitiveConverter.getLong(args.get(2));
        final long alternativeMusicId = PrimitiveConverter.getLong(args.get(3));
        final short playListId = PrimitiveConverter.getShort(args.get(4));
        final PlayListData data = new PlayListData();
        data.addMusicData(1, musicId, alternativeMusicId, (byte)100, (short)0, (byte)1, 3);
        data.addMusicData(2, musicId, alternativeMusicId, (byte)100, (short)0, (byte)1, 3);
        data.setPlayListId(playListId);
        PlayListManager.getInstance().add(data);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
