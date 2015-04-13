package com.ankamagames.wakfu.client.network.protocol.frame.secretQuestion;

import com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.messagebox.*;

final class SecretQuestionMessageRunner implements MessageRunner<SecretQuestionMessage>
{
    @Override
    public boolean run(final SecretQuestionMessage msg) {
        final long characterId = NetSecretQuestionFrame.INSTANCE.getCharacterId();
        if (characterId != msg.getCharacterId()) {
            return false;
        }
        final MessageBoxData data = new MessageBoxData(102, 0, msg.getQuestion(), 65542L);
        final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type != 2) {
                    NetSecretQuestionFrame.INSTANCE.setCharacterId(0L);
                    WakfuGameEntity.getInstance().removeFrame(NetSecretQuestionFrame.INSTANCE);
                    return;
                }
                final Message netMessage = new SecretAnswerMessage(characterId, userEntry);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
            }
        });
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 2074;
    }
}
