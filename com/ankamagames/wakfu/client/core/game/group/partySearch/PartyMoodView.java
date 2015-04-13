package com.ankamagames.wakfu.client.core.game.group.partySearch;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;

public class PartyMoodView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String STYLE = "style";
    private final PartyMood m_mood;
    private static final Map<PartyMood, PartyMoodView> VIEWS;
    
    public static PartyMoodView getView(final PartyMood mood) {
        return PartyMoodView.VIEWS.get(mood);
    }
    
    private PartyMoodView(final PartyMood mood) {
        super();
        this.m_mood = mood;
    }
    
    @Override
    public String[] getFields() {
        return PartyMoodView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("style")) {
            return "partyMood" + this.m_mood.getId();
        }
        return null;
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString("partySearch.moodName" + this.m_mood.getId());
    }
    
    public PartyMood getMood() {
        return this.m_mood;
    }
    
    static {
        VIEWS = new EnumMap<PartyMood, PartyMoodView>(PartyMood.class);
        for (final PartyMood mood : PartyMood.values()) {
            PartyMoodView.VIEWS.put(mood, new PartyMoodView(mood));
        }
    }
}
