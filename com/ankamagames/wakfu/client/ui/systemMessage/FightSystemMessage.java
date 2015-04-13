package com.ankamagames.wakfu.client.ui.systemMessage;

import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;

public class FightSystemMessage extends AbstractSystemMessage
{
    public static final String CONTAINER1_ID = "container1";
    public static final String CONTAINER2_ID = "container2";
    public static final String IMAGE1_ID = "image1";
    public static final String IMAGE2_ID = "image2";
    
    public FightSystemMessage() {
        super(WakfuSystemMessageManager.SystemMessageType.FIGHT_INFO, false, "messageContainerFight", (short)10000);
    }
    
    @Override
    protected void applyTweens(final boolean in, final String id) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(id);
        if (map == null) {
            return;
        }
        final int oldDuration = this.m_fadeDuration;
        this.m_fadeDuration = (in ? 150 : this.m_fadeDuration);
        this.applyTween((Widget)map.getElement("container1"), in);
        this.applyTween((Widget)map.getElement("image1"), in);
        this.applyTween((Widget)map.getElement("image2"), in);
        this.applyTween((Widget)map.getElement("image3"), in);
        this.applyTween((Widget)map.getElement("text"), in);
        if (in) {
            this.m_fadeDuration = 10;
        }
        this.applyTween((Widget)map.getElement("animatedElementLeft"), in);
        this.applyTween((Widget)map.getElement("animatedElementRight"), in);
        this.m_fadeDuration = oldDuration;
    }
    
    @Override
    public void showMessage(final SystemMessageData data) {
        super.showMessage(data);
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("messageContainerFight");
        if (map == null) {
            return;
        }
        AbstractSystemMessage.reloadAnm((AnimatedElementViewer)map.getElement("animatedElementLeft"));
        AbstractSystemMessage.reloadAnm((AnimatedElementViewer)map.getElement("animatedElementRight"));
    }
    
    @Override
    protected void clean(final String id) {
    }
}
