package com.ankamagames.wakfu.client.core.game.tutorial;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import org.jetbrains.annotations.*;

public class TutorialPageView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    final TutorialPage m_tutorialPage;
    private ArrayList<TutorialEntryView> m_tutorialEntries;
    
    public TutorialPageView(final TutorialPage tutorialPage, final ArrayList<TutorialEntryView> tutorialEntries) {
        super();
        this.m_tutorialEntries = new ArrayList<TutorialEntryView>();
        this.m_tutorialPage = tutorialPage;
        this.m_tutorialEntries = tutorialEntries;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        return null;
    }
    
    public String getName() {
        return this.m_tutorialPage.getName();
    }
    
    public ArrayList<TutorialEntryView> getTutorialEntries() {
        return this.m_tutorialEntries;
    }
}
