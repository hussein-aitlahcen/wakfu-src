package com.ankamagames.wakfu.common.game.emote;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;

public class EmoteHandler
{
    protected static final Logger m_logger;
    public static final int SIT_EMOTE_ID = 20015;
    protected final TIntHashSet m_knownEmotes;
    
    public EmoteHandler() {
        super();
        this.m_knownEmotes = new TIntHashSet();
    }
    
    public boolean learnEmote(final int emoteId) {
        return this.m_knownEmotes.add(emoteId);
    }
    
    public final boolean knowEmote(final int emoteId) {
        return this.m_knownEmotes.contains(emoteId);
    }
    
    public void clear() {
        this.m_knownEmotes.clear();
    }
    
    public void toRaw(final CharacterSerializedEmoteInventory part) {
        final TIntIterator it = this.m_knownEmotes.iterator();
        while (it.hasNext()) {
            final CharacterSerializedEmoteInventory.Emote emote = new CharacterSerializedEmoteInventory.Emote();
            emote.emoteId = it.next();
            part.emotes.add(emote);
        }
    }
    
    public final void fromRaw(final CharacterSerializedEmoteInventory part) {
        for (int i = 0; i < part.emotes.size(); ++i) {
            this.learnEmote(part.emotes.get(i).emoteId);
        }
    }
    
    @Override
    public String toString() {
        return "EmoteHandler{m_knownEmotes=" + this.m_knownEmotes + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)EmoteHandler.class);
    }
}
