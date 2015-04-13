package com.ankamagames.wakfu.client.core.game.group.partySearch;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;

public class PartyRequesterView extends ImmutableFieldProvider
{
    public static final String MOOD = "mood";
    public static final String DESCRIPTION = "description";
    public static final String MEMBER0 = "member0";
    public static final String MEMBER1 = "member1";
    public static final String MEMBER2 = "member2";
    public static final String MEMBER3 = "member3";
    public static final String MEMBER4 = "member4";
    public static final String MEMBER5 = "member5";
    public static final String ENABLED = "enabled";
    private final long m_id;
    private final PartyMoodView m_mood;
    private final String m_description;
    private String m_playersText;
    private final String m_leaderName;
    private final List<PartyPlayerDefinitionView> m_players;
    private final boolean m_enabled;
    
    public PartyRequesterView(final PartyRequester partyRequester) {
        super();
        this.m_id = partyRequester.getId();
        this.m_mood = PartyMoodView.getView(partyRequester.getMood());
        this.m_description = partyRequester.getDescription();
        this.m_leaderName = partyRequester.getLeaderName();
        final List<PartyPlayerDefinitionView> players = new ArrayList<PartyPlayerDefinitionView>();
        for (final PartyPlayerDefinition definition : partyRequester.getDefinitions()) {
            players.add(new PartyPlayerDefinitionView(definition));
        }
        this.m_players = new ArrayList<PartyPlayerDefinitionView>(players);
        this.generateDescription();
        final PartyModelInterface party = WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment().getParty();
        final int partySize = (party != null) ? party.getRealPlayerCount() : 1;
        this.m_enabled = (this.m_players.size() + partySize <= 6);
    }
    
    private void generateDescription() {
        final StringBuilder sb = new StringBuilder();
        for (final PartyPlayerDefinitionView view : this.m_players) {
            sb.append(view.getBreed().getName()).append(" - ").append(view.getLevel()).append(" - ").append(view.getRoleView().getName()).append('\n');
        }
        this.m_playersText = sb.toString();
    }
    
    public String getLeaderName() {
        return this.m_leaderName;
    }
    
    @Override
    public String[] getFields() {
        return PartyRequesterView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("mood")) {
            return this.m_mood;
        }
        if (fieldName.equals("description")) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.append(WakfuTranslator.getInstance().getString("description"));
            sb.append(WakfuTranslator.getInstance().getString("colon"));
            sb.b().append(this.m_description)._b();
            return sb.finishAndToString();
        }
        if (fieldName.equals("member0")) {
            return this.m_players.isEmpty() ? null : this.m_players.get(0);
        }
        if (fieldName.equals("member1")) {
            return (this.m_players.size() > 1) ? this.m_players.get(1) : null;
        }
        if (fieldName.equals("member2")) {
            return (this.m_players.size() > 2) ? this.m_players.get(2) : null;
        }
        if (fieldName.equals("member3")) {
            return (this.m_players.size() > 3) ? this.m_players.get(3) : null;
        }
        if (fieldName.equals("member4")) {
            return (this.m_players.size() > 4) ? this.m_players.get(4) : null;
        }
        if (fieldName.equals("member5")) {
            return (this.m_players.size() > 5) ? this.m_players.get(5) : null;
        }
        if (fieldName.equals("enabled")) {
            return this.m_enabled;
        }
        return null;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public PartyMoodView getMoodView() {
        return this.m_mood;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public String getPlayersText() {
        return this.m_playersText;
    }
}
