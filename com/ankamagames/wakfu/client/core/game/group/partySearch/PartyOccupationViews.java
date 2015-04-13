package com.ankamagames.wakfu.client.core.game.group.partySearch;

import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.google.common.base.*;
import com.google.common.collect.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import java.util.*;
import gnu.trove.*;

public abstract class PartyOccupationViews extends ImmutableFieldProvider
{
    public static final String PVE_OCCUPATIONS = "pveOccupations";
    public static final String MOODS = "moods";
    public static final String ROLES = "roles";
    public static final String SELECTED_MOOD = "selectedMood";
    public static final String SELECTED_ROLE = "selectedRole";
    public static final String SEARCH_STRING = "searchString";
    public static final String OCCUPATION_MIN_LEVEL = "occupationMinLevel";
    public static final String OCCUPATION_MAX_LEVEL = "occupationMaxLevel";
    public static final String DESCRIPTION = "description";
    private final TIntObjectHashMap<PvePartyOccupationGroup> m_pveOccupationGroup;
    private List<PvePartyOccupationGroup> m_sortedPveOccupationGroup;
    private List<PvePartyOccupationGroup> m_filteredPveOccupationGroup;
    private final List<PartyMoodView> m_moods;
    private final List<PartyRoleView> m_roles;
    protected PartyMoodView m_selectedMood;
    protected PartyRoleView m_selectedRole;
    protected short m_occupationMinLevel;
    protected short m_occupationMaxLevel;
    protected String m_description;
    private String m_searchString;
    
    protected PartyOccupationViews(final boolean registration) {
        this(null, registration);
    }
    
    protected PartyOccupationViews(@Nullable final PartyRequester partyRequester, final boolean registration) {
        super();
        this.m_pveOccupationGroup = new TIntObjectHashMap<PvePartyOccupationGroup>();
        this.m_moods = new ArrayList<PartyMoodView>();
        this.m_roles = new ArrayList<PartyRoleView>();
        this.createGroupViews(partyRequester, registration);
        this.createMoodViews(partyRequester);
        this.createRoleViews();
        this.setOccupationMaxLevel(WakfuGameEntity.getInstance().getLocalPlayer().getLevel());
        this.setOccupationMinLevel();
        this.m_description = ((partyRequester == null) ? null : partyRequester.getDescription());
    }
    
    public final void filterPvePartyOccupationGroup() {
        if (this.m_occupationMaxLevel <= 0) {
            this.setOccupationMaxLevel(XpConstants.getPlayerCharacterLevelCap());
            this.setOccupationMinLevel();
        }
        this.m_filteredPveOccupationGroup = new ArrayList<PvePartyOccupationGroup>();
        for (final PvePartyOccupationGroup occupation : this.m_sortedPveOccupationGroup) {
            if (!occupation.isLevelValid(this.m_occupationMinLevel, this.m_occupationMaxLevel)) {
                continue;
            }
            if (!occupation.containsName(this.m_searchString)) {
                continue;
            }
            occupation.computeActivationByLevel(this.getPartyMinLevel());
            this.m_filteredPveOccupationGroup.add(occupation);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "pveOccupations");
    }
    
    protected void update(final PartyRequester partyRequester) {
        final TIntObjectIterator<PvePartyOccupationGroup> it = this.m_pveOccupationGroup.iterator();
        while (it.hasNext()) {
            it.advance();
            final PvePartyOccupationGroup group = it.value();
            if (group.getMonster() != null) {
                group.getMonster().setSelected(partyRequester.hasOccupation(((PartyOccupationView<PartyOccupation>)group.getMonster()).getOccupation()));
            }
            if (group.getDungeon() != null) {
                group.getDungeon().setSelected(partyRequester.hasOccupation(((PartyOccupationView<PartyOccupation>)group.getDungeon()).getOccupation()));
            }
        }
        this.m_description = partyRequester.getDescription();
        this.m_selectedMood = PartyMoodView.getView(partyRequester.getMood());
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "description", "selectedMood");
    }
    
    private void createGroupViews(final PartyRequester partyRequester, final boolean registration) {
        final short level = WakfuGameEntity.getInstance().getLocalPlayer().getLevel();
        final Multimap<Integer, PvePartyOccupation> pveOccupations = (Multimap<Integer, PvePartyOccupation>)PvePartyOccupationListBuildProcedure.getMap();
        for (final Integer familyId : pveOccupations.keySet()) {
            final Collection<PvePartyOccupation> occupations = (Collection<PvePartyOccupation>)pveOccupations.get((Object)familyId);
            this.m_pveOccupationGroup.put(familyId, new PvePartyOccupationGroup(familyId, occupations, level, partyRequester, registration));
        }
        final List<PvePartyOccupationGroup> groups = new ArrayList<PvePartyOccupationGroup>();
        this.m_pveOccupationGroup.forEachValue(new TObjectProcedure<PvePartyOccupationGroup>() {
            @Override
            public boolean execute(final PvePartyOccupationGroup object) {
                groups.add(object);
                return true;
            }
        });
        final Ordering<PvePartyOccupationGroup> ordering = (Ordering<PvePartyOccupationGroup>)Ordering.natural().onResultOf((Function)new Function<PvePartyOccupationGroup, Short>() {
            @javax.annotation.Nullable
            public Short apply(@javax.annotation.Nullable final PvePartyOccupationGroup input) {
                return (short)((input == null) ? 0 : input.getLevel());
            }
        }).reverse();
        this.m_sortedPveOccupationGroup = (List<PvePartyOccupationGroup>)ordering.immutableSortedCopy((Iterable)groups);
    }
    
    private void createRoleViews() {
        for (final PartyRole role : PartyRole.values()) {
            if (role != PartyRole.NONE || this.withNone()) {
                this.m_roles.add(PartyRoleView.getView(role));
            }
        }
        this.m_selectedRole = this.m_roles.get(0);
    }
    
    protected abstract short getPartyMinLevel();
    
    public void selectRole(final PartyRoleView roleView) {
        this.m_selectedRole = roleView;
    }
    
    private void createMoodViews(final AbstractPartyRequester partyRequester) {
        this.m_selectedMood = null;
        for (final PartyMood mood : PartyMood.values()) {
            if (mood != PartyMood.NONE || this.withNone()) {
                final PartyMoodView view = PartyMoodView.getView(mood);
                this.m_moods.add(view);
                if (partyRequester != null && partyRequester.getMood() == mood) {
                    this.m_selectedMood = view;
                }
            }
        }
        if (this.m_selectedMood == null) {
            this.m_selectedMood = this.m_moods.get(0);
        }
    }
    
    public void selectMood(final PartyMoodView moodView) {
        this.m_selectedMood = moodView;
    }
    
    public PartyMoodView getSelectedMood() {
        return this.m_selectedMood;
    }
    
    public PartyRoleView getSelectedRole() {
        return this.m_selectedRole;
    }
    
    protected abstract boolean withNone();
    
    public abstract void selectOccupation(final PartyOccupationView<PartyOccupation> p0);
    
    public abstract void unselectOccupation(final PartyOccupationView<PartyOccupation> p0);
    
    public String getDescription() {
        return this.m_description;
    }
    
    public void setDescription(final String description) {
        this.m_description = description;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "description");
    }
    
    public void setSearchString(final String searchString) {
        this.m_searchString = searchString;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "searchString");
    }
    
    private short getValidMinOccupationLevel(final short level) {
        if (level > this.m_occupationMaxLevel) {
            return (short)((this.m_occupationMaxLevel >= 20) ? (this.m_occupationMaxLevel - 20) : 0);
        }
        return (short)((level >= 0) ? level : 0);
    }
    
    public final void setOccupationMinLevel() {
        this.setOccupationMinLevel((short)(this.m_occupationMaxLevel - 20));
    }
    
    public final void setOccupationMinLevel(final short occupationMinLevel) {
        this.m_occupationMinLevel = this.getValidMinOccupationLevel(occupationMinLevel);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "occupationMinLevel");
    }
    
    public final void setOccupationMaxLevel(final short occupationMaxLevel) {
        this.m_occupationMaxLevel = occupationMaxLevel;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "occupationMaxLevel");
    }
    
    protected final boolean forEachOccupationGroup(final TObjectProcedure<PvePartyOccupationGroup> procedure) {
        return this.m_pveOccupationGroup.forEachValue(procedure);
    }
    
    @Override
    public String[] getFields() {
        return PartyOccupationViews.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("pveOccupations")) {
            return (this.m_filteredPveOccupationGroup == null) ? null : Collections.unmodifiableList((List<?>)this.m_filteredPveOccupationGroup);
        }
        if (fieldName.equals("moods")) {
            return Collections.unmodifiableList((List<?>)this.m_moods);
        }
        if (fieldName.equals("roles")) {
            return Collections.unmodifiableList((List<?>)this.m_roles);
        }
        if (fieldName.equals("selectedMood")) {
            return this.m_selectedMood;
        }
        if (fieldName.equals("selectedRole")) {
            return this.m_selectedRole;
        }
        if (fieldName.equals("occupationMinLevel")) {
            return this.m_occupationMinLevel;
        }
        if (fieldName.equals("occupationMaxLevel")) {
            return this.m_occupationMaxLevel;
        }
        if (fieldName.equals("description")) {
            return this.m_description;
        }
        if (fieldName.equals("searchString")) {
            return this.m_searchString;
        }
        return null;
    }
}
