package com.ankamagames.wakfu.client.ui.protocol.frame.runners.guild;

import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.messagebox.*;

public class GuildChangeNationMessageRunner implements MessageRunner<AbstractUIMessage>
{
    @Override
    public boolean run(final AbstractUIMessage msg) {
        final int nationId = msg.getIntValue();
        if (nationId == 0) {
            final String errorMsg = WakfuTranslator.getInstance().getString("guild.changeNation.errorWrongNation");
            final MessageBoxData data = new MessageBoxData(102, 1, errorMsg, 2L);
            Xulor.getInstance().msgBox(data);
        }
        else if (nationId == WakfuGameEntity.getInstance().getLocalPlayer().getGuildHandler().getNationId()) {
            final String errorMsg = WakfuTranslator.getInstance().getString("guild.changeNation.errorSameNation");
            final MessageBoxData data = new MessageBoxData(102, 1, errorMsg, 2L);
            Xulor.getInstance().msgBox(data);
        }
        else {
            final String errorMsg = WakfuTranslator.getInstance().getString("guild.changeNation.confirm");
            final MessageBoxData data = new MessageBoxData(102, 1, errorMsg, 24L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new GuildChangeNationRequestMessage(nationId));
                    }
                }
            });
        }
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 17014;
    }
}
