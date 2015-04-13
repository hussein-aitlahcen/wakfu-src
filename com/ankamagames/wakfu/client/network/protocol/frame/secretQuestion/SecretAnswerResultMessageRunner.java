package com.ankamagames.wakfu.client.network.protocol.frame.secretQuestion;

import com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class SecretAnswerResultMessageRunner implements MessageRunner<SecretAnswerResultMessage>
{
    @Override
    public boolean run(final SecretAnswerResultMessage msg) {
        if (msg.isAnswerIsRight()) {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("right.answer.character.deleted"), 102, 0);
        }
        else {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("bad.answer"), 102, 0);
        }
        NetSecretQuestionFrame.INSTANCE.setCharacterId(0L);
        WakfuGameEntity.getInstance().removeFrame(NetSecretQuestionFrame.INSTANCE);
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 2076;
    }
}
