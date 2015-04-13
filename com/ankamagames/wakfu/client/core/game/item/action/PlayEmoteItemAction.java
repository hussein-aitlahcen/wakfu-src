package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.emote.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class PlayEmoteItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private int m_emoteId;
    private long m_targetId;
    
    public PlayEmoteItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
        this.m_emoteId = Integer.valueOf(params[0]);
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            PlayEmoteItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        final EmoteRunnableHandler runnableHandler = new EmoteRunnableHandler() {
            @Override
            public void runEmote(final Emote emote, final long targetId) {
                PlayEmoteItemAction.this.m_targetId = targetId;
                PlayEmoteItemAction.this.sendRequest(item.getUniqueId());
            }
            
            @Override
            public boolean emoteMustBeLearned() {
                return false;
            }
        };
        if (!character.getEmoteHandler().useEmote(this.m_emoteId, runnableHandler)) {
            PlayEmoteItemAction.m_logger.error((Object)("[ItemAction] Impossible d'utiliser l'Emote " + this.m_emoteId));
        }
        return true;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        super.serialize(buffer);
        final Emote emote = ReferenceEmoteManager.INSTANCE.getEmote(this.m_emoteId);
        buffer.put((byte)(emote.isInfiniteDuration() ? 1 : 0));
        buffer.putLong(this.m_targetId);
        return false;
    }
    
    @Override
    public int serializedSize() {
        return super.serializedSize() + 1 + 8;
    }
    
    @Override
    public void clear() {
        this.m_targetId = -1L;
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.PLAY_EMOTE;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayEmoteItemAction.class);
    }
}
