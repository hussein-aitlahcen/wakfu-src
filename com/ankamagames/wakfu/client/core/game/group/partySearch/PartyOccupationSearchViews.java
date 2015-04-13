package com.ankamagames.wakfu.client.core.game.group.partySearch;

import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;

public class PartyOccupationSearchViews extends PartyOccupationViews
{
    public static final String PARTY_REQUESTERS = "requesters";
    public static final String FILTER_REQUESTER_MIN_LEVEL = "requesterMinLevel";
    public static final String FILTER_REQUESTER_MAX_LEVEL = "requesterMaxLevel";
    public static final String BREEDS = "breeds";
    public static final String SELECTED_BREED = "breed";
    private PartyOccupationView<PartyOccupation> m_selectedView;
    private ArrayList<PartyRequesterView> m_partyRequesters;
    private List<BreedInfo> m_breeds;
    private BreedInfo m_selectedBreed;
    private short m_requesterMinLevel;
    private short m_requesterMaxLevel;
    private final short m_partyMinLevel;
    
    public PartyOccupationSearchViews() {
        super(false);
        this.m_partyRequesters = new ArrayList<PartyRequesterView>();
        this.m_requesterMinLevel = this.m_occupationMinLevel;
        this.m_requesterMaxLevel = 200;
        this.createBreedView();
        this.m_partyMinLevel = this.m_occupationMaxLevel;
    }
    
    private void createBreedView() {
        (this.m_breeds = new ArrayList<BreedInfo>()).add(new BreedInfo(AvatarBreed.NONE));
        for (final AvatarBreed b : AvatarBreed.values()) {
            if (b != AvatarBreed.SOUL) {
                if (AvatarBreedConstants.isBreedEnabled(b)) {
                    this.m_breeds.add(new BreedInfo(b));
                }
            }
        }
        this.m_selectedBreed = this.m_breeds.get(0);
    }
    
    @Override
    protected short getPartyMinLevel() {
        return this.m_partyMinLevel;
    }
    
    @Override
    protected boolean withNone() {
        return true;
    }
    
    public short getRequesterMinLevel() {
        return this.m_requesterMinLevel;
    }
    
    public void setRequesterMinLevel(final short requesterMinLevel) {
        this.m_requesterMinLevel = requesterMinLevel;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "requesterMinLevel");
    }
    
    public short getRequesterMaxLevel() {
        return this.m_requesterMaxLevel;
    }
    
    public void setRequesterMaxLevel(final short requesterMaxLevel) {
        this.m_requesterMaxLevel = requesterMaxLevel;
    }
    
    public BreedInfo getSelectedBreed() {
        return this.m_selectedBreed;
    }
    
    public void setSelectedBreed(final BreedInfo selectedBreed) {
        this.m_selectedBreed = selectedBreed;
    }
    
    public PartyOccupationView<PartyOccupation> getSelectedView() {
        return this.m_selectedView;
    }
    
    @Override
    public void selectOccupation(final PartyOccupationView<PartyOccupation> view) {
        if (view == null) {
            return;
        }
        if (this.m_selectedView != null) {
            this.m_selectedView.setSelected(false);
        }
        (this.m_selectedView = view).setSelected(true);
    }
    
    @Override
    public void unselectOccupation(final PartyOccupationView<PartyOccupation> view) {
        view.setSelected(false);
        if (view == this.m_selectedView) {
            this.m_selectedView = null;
        }
    }
    
    public void setPartyRequesters(final List<PartyRequester> partyRequesters) {
        final ArrayList<PartyRequesterView> result = new ArrayList<PartyRequesterView>();
        for (final PartyRequester requester : partyRequesters) {
            result.add(new PartyRequesterView(requester));
        }
        this.m_partyRequesters = new ArrayList<PartyRequesterView>(result);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "requesters");
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("requesters".equals(fieldName)) {
            return this.m_partyRequesters;
        }
        if ("requesterMinLevel".equals(fieldName)) {
            return this.m_requesterMinLevel;
        }
        if ("requesterMaxLevel".equals(fieldName)) {
            return this.m_requesterMaxLevel;
        }
        if ("breeds".equals(fieldName)) {
            return Collections.unmodifiableList((List<?>)this.m_breeds);
        }
        if ("breed".equals(fieldName)) {
            return this.m_selectedBreed;
        }
        return super.getFieldValue(fieldName);
    }
    
    public SearchParameters generateSearchParameters() {
        if (this.m_selectedView == null) {
            return null;
        }
        final SearchParameters searchParameters = new SearchParameters();
        searchParameters.setPartyOccupation(this.m_selectedView.getOccupation());
        searchParameters.setBreed(this.m_selectedBreed.getBreed());
        searchParameters.setMinLevel(this.m_requesterMinLevel);
        searchParameters.setMaxLevel(this.m_requesterMaxLevel);
        searchParameters.setMood(this.m_selectedMood.getMood());
        searchParameters.setRole(this.m_selectedRole.getRole());
        return searchParameters;
    }
    
    @Override
    public String toString() {
        return "PartyOccupationSearchViews{m_selectedView=" + this.m_selectedView + '}';
    }
}
