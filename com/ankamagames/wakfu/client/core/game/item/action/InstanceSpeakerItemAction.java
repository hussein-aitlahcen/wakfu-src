package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.xulor2.util.*;

public final class InstanceSpeakerItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private static final TextEditorParameters TEXT_EDITOR_PARAMETERS;
    private String m_message;
    private byte[] m_serializedMessage;
    
    InstanceSpeakerItemAction(final int id) {
        super(id);
        this.m_message = "";
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            InstanceSpeakerItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        final MessageBoxData data = new MessageBoxData(102, 0, WakfuTranslator.getInstance().getString("question.whatIsYourMessage"), 65542L);
        data.setTextEditorParameters(InstanceSpeakerItemAction.TEXT_EDITOR_PARAMETERS);
        final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type != 2) {
                    return;
                }
                InstanceSpeakerItemAction.this.m_message = userEntry;
                InstanceSpeakerItemAction.this.m_message = ChatHelper.controlSmsAndFlood(InstanceSpeakerItemAction.this.m_message);
                if (InstanceSpeakerItemAction.this.m_message == null) {
                    return;
                }
                InstanceSpeakerItemAction.this.m_message = WakfuWordsModerator.makeValidSentence(InstanceSpeakerItemAction.this.m_message);
                if (InstanceSpeakerItemAction.this.m_message.isEmpty()) {
                    ChatHelper.pushErrorMessage("error.chat.operationNotPermited", new Object[0]);
                    return;
                }
                InstanceSpeakerItemAction.this.m_serializedMessage = StringUtils.toUTF8(InstanceSpeakerItemAction.this.m_message);
                InstanceSpeakerItemAction.this.sendRequest(item.getUniqueId());
            }
        });
        return true;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        super.serialize(buffer);
        buffer.put(this.m_serializedMessage);
        return true;
    }
    
    @Override
    public int serializedSize() {
        return super.serializedSize() + this.m_serializedMessage.length;
    }
    
    @Override
    public void clear() {
        this.m_message = "";
    }
    
    @Override
    public String toString() {
        return "InstanceSpeakerItemAction{m_message='" + this.m_message + '\'' + '}';
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.INSTANCE_SPEAKER;
    }
    
    static {
        m_logger = Logger.getLogger((Class)InstanceSpeakerItemAction.class);
        (TEXT_EDITOR_PARAMETERS = new TextEditorParameters()).setMaxCharacters(100);
        InstanceSpeakerItemAction.TEXT_EDITOR_PARAMETERS.setPrefSize(new Dimension(370, 35));
        InstanceSpeakerItemAction.TEXT_EDITOR_PARAMETERS.setMaxWidth(200);
        InstanceSpeakerItemAction.TEXT_EDITOR_PARAMETERS.setMultiline(true);
    }
}
