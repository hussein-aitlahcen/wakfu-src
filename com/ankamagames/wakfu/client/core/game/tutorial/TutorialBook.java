package com.ankamagames.wakfu.client.core.game.tutorial;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.events.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import gnu.trove.*;

public class TutorialBook extends ImmutableFieldProvider
{
    public static final String CURRENT_PAGE_FIELD = "currentPage";
    public static final String TUTORIALS_FIELD = "tutorials";
    public static final String HAS_SEARCH_FIELD = "hasSearch";
    public static final String HAS_PREVIOUS_PAGE_FIELD = "hasPreviousPage";
    public static final String HAS_NEXT_PAGE_FIELD = "hasNextPage";
    public static final String NUM_PAGES_FIELD = "numPages";
    public static final String SEARCH_RESULT_FIELD = "searchResult";
    final TIntObjectHashMap<TutorialPageView> m_tutorialEntriesByPage;
    private ArrayList<TutorialEntryView> m_tutorialEntries;
    private String m_search;
    private int m_currentPage;
    
    public TutorialBook() {
        super();
        this.m_tutorialEntriesByPage = new TIntObjectHashMap<TutorialPageView>();
        this.m_tutorialEntries = new ArrayList<TutorialEntryView>();
        this.m_currentPage = 0;
        TutorialManager.INSTANCE.forEachPage(new TIntObjectProcedure<TutorialPage>() {
            @Override
            public boolean execute(final int id, final TutorialPage tutorialPage) {
                final ArrayList<TutorialEntryView> tutorialEntries = new ArrayList<TutorialEntryView>();
                tutorialPage.forEachEvent(new TIntProcedure() {
                    @Override
                    public boolean execute(final int value) {
                        final ClientGameEventListener listener = ClientGameEventManager.INSTANCE.getListener(value);
                        if (listener == null) {
                            return true;
                        }
                        final TIntObjectIterator<ClientEventActionList> it = listener.getActionsDropTable().getDrops().iterator();
                        while (it.hasNext()) {
                            it.advance();
                            for (final ClientEventAction cea : it.value().getActions()) {
                                if (!(cea instanceof ClientEventActionLaunchTutorial)) {
                                    continue;
                                }
                                final TutorialEntryView tutorialEntryView = new TutorialEntryView((ClientEventActionLaunchTutorial)cea);
                                if (tutorialEntryView.isLaunched()) {
                                    UITutorialFrame.getInstance().add(tutorialEntryView);
                                }
                                tutorialEntries.add(tutorialEntryView);
                            }
                        }
                        return true;
                    }
                });
                final TutorialPageView tutorialPageView = new TutorialPageView(tutorialPage, tutorialEntries);
                TutorialBook.this.m_tutorialEntriesByPage.put(tutorialPage.getOrder(), tutorialPageView);
                return true;
            }
        });
        this.reflowTutorialEntries();
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("currentPage")) {
            return this.m_tutorialEntriesByPage.get(this.m_currentPage);
        }
        if (fieldName.equals("tutorials")) {
            if (this.m_tutorialEntries.isEmpty()) {
                return null;
            }
            return this.m_tutorialEntries;
        }
        else {
            if (fieldName.equals("hasSearch")) {
                return this.hasSearch();
            }
            if (fieldName.equals("hasPreviousPage")) {
                return this.m_currentPage > 0;
            }
            if (fieldName.equals("hasNextPage")) {
                return this.m_currentPage < this.m_tutorialEntriesByPage.size() - 1;
            }
            if (fieldName.equals("searchResult")) {
                return (this.m_tutorialEntries.size() > 0) ? WakfuTranslator.getInstance().getString("resultNumber", this.m_tutorialEntries.size()) : WakfuTranslator.getInstance().getString("marketBoard.noResults");
            }
            if (!fieldName.equals("numPages")) {
                return null;
            }
            if (this.hasSearch()) {
                return null;
            }
            return this.m_currentPage + 1 + "/" + this.m_tutorialEntriesByPage.size();
        }
    }
    
    public void setPreviousPage() {
        if (this.m_currentPage == 0) {
            return;
        }
        --this.m_currentPage;
        this.reflowTutorialEntries();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasNextPage", "hasPreviousPage", "numPages");
    }
    
    public void setNextPage() {
        if (this.m_currentPage == this.m_tutorialEntriesByPage.size() - 1) {
            return;
        }
        ++this.m_currentPage;
        this.reflowTutorialEntries();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasNextPage", "hasPreviousPage", "numPages");
    }
    
    public void setSearch(final String search) {
        this.m_search = ((search != null && search.length() > 0) ? search : null);
        this.reflowTutorialEntries();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasSearch", "hasNextPage", "hasPreviousPage", "numPages", "searchResult");
    }
    
    private void reflowTutorialEntries() {
        this.m_tutorialEntries.clear();
        if (this.hasSearch()) {
            final TIntObjectIterator<TutorialPageView> it = this.m_tutorialEntriesByPage.iterator();
            while (it.hasNext()) {
                it.advance();
                for (final TutorialEntryView entry : it.value().getTutorialEntries()) {
                    if (entry.getName().toLowerCase().contains(this.m_search.toLowerCase())) {
                        this.m_tutorialEntries.add(entry);
                    }
                }
            }
        }
        else {
            for (final TutorialEntryView entry2 : this.m_tutorialEntriesByPage.get(this.m_currentPage).getTutorialEntries()) {
                this.m_tutorialEntries.add(entry2);
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "tutorials");
    }
    
    private boolean hasSearch() {
        return this.m_search != null;
    }
}
