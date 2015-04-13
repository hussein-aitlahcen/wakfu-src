package com.ankamagames.wakfu.client.network.protocol.frame.secretQuestion;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;

public final class NetSecretQuestionFrame extends MessageRunnerFrame
{
    public static final NetSecretQuestionFrame INSTANCE;
    private long m_characterId;
    
    private NetSecretQuestionFrame() {
        super(new MessageRunner[] { new SecretQuestionMessageRunner(), new SecretAnswerResultMessageRunner() });
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        this.m_characterId = 0L;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        INSTANCE = new NetSecretQuestionFrame();
    }
}
