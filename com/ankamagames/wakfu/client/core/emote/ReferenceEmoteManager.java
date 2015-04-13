package com.ankamagames.wakfu.client.core.emote;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import gnu.trove.*;

public class ReferenceEmoteManager
{
    public static final ReferenceEmoteManager INSTANCE;
    private final TIntObjectHashMap<Emote> m_emotes;
    private final TIntObjectHashMap<Emote> m_emotesWithTarget;
    private final TIntObjectHashMap<Smiley> m_smileys;
    private final TIntArrayList m_emotesIds;
    private final TIntArrayList m_emotesWithTargetIds;
    
    private ReferenceEmoteManager() {
        super();
        this.m_emotes = new TIntObjectHashMap<Emote>();
        this.m_emotesWithTarget = new TIntObjectHashMap<Emote>();
        this.m_smileys = new TIntObjectHashMap<Smiley>();
        this.m_emotesIds = new TIntArrayList();
        this.m_emotesWithTargetIds = new TIntArrayList();
        for (final SmileyEnum smiley : SmileyEnum.values()) {
            final Smiley s = new Smiley(smiley.getId(), smiley.getCommandText());
            this.m_smileys.put(s.getId(), s);
        }
    }
    
    public void addEmote(final EmoteBinaryData s) {
        this.m_emotes.put(s.getId(), createEmote(s));
        this.m_emotesIds.add(s.getId());
    }
    
    public void addEmoteWithTarget(final EmoteBinaryData s) {
        this.m_emotesWithTarget.put(s.getId(), createEmote(s));
        this.m_emotesWithTargetIds.add(s.getId());
    }
    
    private static Emote createEmote(final EmoteBinaryData s) {
        return new Emote(s.getId(), s.isInfiniteDuration(), s.isNeedTarget(), s.isMoveToTarget(), s.isMusical(), s.getCmd(), s.getScriptParams());
    }
    
    public EmoteSmileyFieldProvider getEmoteOrSmiley(final int id) {
        final Emote emote = this.getEmote(id);
        return (emote != null) ? emote : this.getSmiley(id);
    }
    
    public Smiley getSmiley(final int id) {
        return this.m_smileys.get(id);
    }
    
    public Emote getEmote(final int id) {
        if (this.m_emotesWithTarget.contains(id)) {
            return this.m_emotesWithTarget.get(id);
        }
        if (this.m_emotes.contains(id)) {
            return this.m_emotes.get(id);
        }
        return null;
    }
    
    public Emote getEmote(final CommandPattern pattern) {
        TIntObjectIterator<Emote> it = this.m_emotesWithTarget.iterator();
        while (it.hasNext()) {
            it.advance();
            if (pattern.getCmdPattern().matcher(it.value().m_commandText).matches()) {
                return it.value();
            }
        }
        it = this.m_emotes.iterator();
        while (it.hasNext()) {
            it.advance();
            if (pattern.getCmdPattern().matcher(it.value().m_commandText).matches()) {
                return it.value();
            }
        }
        return null;
    }
    
    public ArrayList<Emote> getEmotes() {
        final ArrayList<Emote> emotes = new ArrayList<Emote>();
        this.m_emotesIds.forEach(new TIntProcedure() {
            @Override
            public boolean execute(final int value) {
                emotes.add(ReferenceEmoteManager.this.m_emotes.get(value));
                return true;
            }
        });
        return emotes;
    }
    
    public ArrayList<Emote> getEmotesWithTarget() {
        final ArrayList<Emote> emotes = new ArrayList<Emote>();
        this.m_emotesWithTargetIds.forEach(new TIntProcedure() {
            @Override
            public boolean execute(final int value) {
                emotes.add(ReferenceEmoteManager.this.m_emotesWithTarget.get(value));
                return true;
            }
        });
        return emotes;
    }
    
    static {
        INSTANCE = new ReferenceEmoteManager();
    }
}
