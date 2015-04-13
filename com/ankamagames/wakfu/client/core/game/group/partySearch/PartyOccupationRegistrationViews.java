package com.ankamagames.wakfu.client.core.game.group.partySearch;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.google.common.collect.*;
import java.util.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;

public class PartyOccupationRegistrationViews extends PartyOccupationViews
{
    private static final Logger m_logger;
    public static final String MEMBER0 = "member0";
    public static final String MEMBER1 = "member1";
    public static final String MEMBER2 = "member2";
    public static final String MEMBER3 = "member3";
    public static final String MEMBER4 = "member4";
    public static final String MEMBER5 = "member5";
    public static final String CAN_REGISTER = "canRegister";
    public static final String REGISTERED = "registered";
    public static final String SELECTED_VIEWS = "selectedViews";
    public static final String SELECTION_ENABLED = "selectionEnabled";
    public static final String IS_LEADER = "isLeader";
    private final List<PvePartyOccupationGroup> m_selectedPveOccupationGroup;
    private final List<PartyOccupationView<? extends PartyOccupation>> m_selectedViews;
    private long m_leaderId;
    private List<PartyPlayerDefinitionView> m_party;
    private boolean m_canRegister;
    private boolean m_registered;
    
    public PartyOccupationRegistrationViews() {
        super(true);
        this.m_selectedPveOccupationGroup = new ArrayList<PvePartyOccupationGroup>();
        this.m_selectedViews = new ArrayList<PartyOccupationView<? extends PartyOccupation>>();
        this.updateParty();
    }
    
    public PartyOccupationRegistrationViews(final PartyRequester currentRequester) {
        super(currentRequester, true);
        this.m_selectedPveOccupationGroup = new ArrayList<PvePartyOccupationGroup>();
        this.m_selectedViews = new ArrayList<PartyOccupationView<? extends PartyOccupation>>();
        this.m_leaderId = currentRequester.getLeaderId();
        this.fillSelectedOccupations();
        this.fillParty(currentRequester);
    }
    
    public void update(final PartyRequester partyRequester) {
        if (partyRequester != null) {
            super.update(partyRequester);
            this.fillSelectedOccupations();
            this.fillParty(partyRequester);
        }
        else {
            this.updateParty();
        }
        PartyOccupationRegistrationViews.m_logger.info((Object)"[PartySearch] Update du requester et fillParty");
    }
    
    public final void updateParty() {
        this.m_party = new ArrayList<PartyPlayerDefinitionView>();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final PartyModelInterface party = localPlayer.getPartyComportment().getParty();
        if (party != null) {
            this.m_leaderId = party.getLeaderId();
            final TLongObjectIterator<PartyMemberInterface> it = party.getMembers().iterator();
            while (it.hasNext()) {
                it.advance();
                final PartyPlayerDefinitionView definition = new PartyPlayerDefinitionView(it.value());
                if (definition.getId() == this.m_leaderId) {
                    this.m_party.add(0, definition);
                }
                else {
                    this.m_party.add(definition);
                }
            }
        }
        else {
            this.m_leaderId = localPlayer.getId();
            final PartyPlayerDefinition localDefinition = new PartyPlayerDefinition(localPlayer.getId(), PartySearchConstants.DEFAULT_PARTY_ROLE, localPlayer.getBreedId(), localPlayer.getLevel());
            localDefinition.setName(localPlayer.getName());
            this.m_party.add(new PartyPlayerDefinitionView(localDefinition));
        }
        this.recomputePartyLevel();
        this.computeUnselection();
        this.filterPvePartyOccupationGroup();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "member0", "member1", "member2", "member3", "member4", "member5");
    }
    
    private void fillSelectedOccupations() {
        this.forEachOccupationGroup(new TObjectProcedure<PvePartyOccupationGroup>() {
            @Override
            public boolean execute(final PvePartyOccupationGroup object) {
                final MonsterPartyOccupationView monster = object.getMonster();
                if (monster != null) {
                    PartyOccupationRegistrationViews.this.selectView(monster, monster.isSelected(), false, true);
                }
                final DungeonPartyOccupationView dungeon = object.getDungeon();
                if (dungeon != null) {
                    PartyOccupationRegistrationViews.this.selectView(dungeon, dungeon.isSelected(), false, true);
                }
                return true;
            }
        });
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedViews");
    }
    
    private void fillParty(final PartyRequester currentRequester) {
        this.m_leaderId = currentRequester.getLeaderId();
        this.m_party = new ArrayList<PartyPlayerDefinitionView>();
        for (final PartyPlayerDefinition definition : currentRequester.getDefinitions()) {
            final PartyPlayerDefinitionView definitionView = new PartyPlayerDefinitionView(definition);
            if (definitionView.getId() == currentRequester.getLeaderId()) {
                this.m_party.add(0, definitionView);
            }
            else {
                this.m_party.add(definitionView);
            }
        }
        this.recomputePartyLevel();
        this.computeUnselection();
        this.filterPvePartyOccupationGroup();
        this.m_registered = true;
        this.m_canRegister = !this.m_selectedViews.isEmpty();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isLeader", "registered", "canRegister", "member0", "member1", "member2", "member3", "member4", "member5");
    }
    
    private void recomputePartyLevel() {
        this.setOccupationMaxLevel(this.getPartyMinLevel());
        this.setOccupationMinLevel();
    }
    
    @Override
    protected short getPartyMinLevel() {
        short level = 32767;
        for (final PartyPlayerDefinitionView definition : this.m_party) {
            if (definition.getDefinition().isCompanion()) {
                continue;
            }
            level = MathHelper.minShort(definition.getLevel(), level);
        }
        return level;
    }
    
    @Override
    protected boolean withNone() {
        return false;
    }
    
    public boolean hasSelections() {
        return !this.m_selectedPveOccupationGroup.isEmpty();
    }
    
    @Override
    public void selectOccupation(final PartyOccupationView<PartyOccupation> view) {
        if (view.isSelected()) {
            return;
        }
        if (this.m_selectedViews.size() >= 20 && !view.isSelected()) {
            PartySearchFeedbackManagement.computeFeedback(PartySearchFeedbackEnum.TOO_MUCH_OCCUPATIONS);
            PropertiesProvider.getInstance().firePropertyValueChanged(view, "selected");
            return;
        }
        final PartyOccupationType occupationType = view.getOccupation().getOccupationType();
        if (occupationType == PartyOccupationType.DUNGEON || occupationType == PartyOccupationType.MONSTER) {
            final int level = view.getOccupation().getLevel();
            if (level > this.getPartyMinLevel()) {
                PartySearchFeedbackManagement.computeFeedback(PartySearchFeedbackEnum.BAD_LEVEL);
                PartyOccupationRegistrationViews.m_logger.error((Object)"[PartySearch] On tente de s\u00e9lectionner une occupation pour laquelle on n'a pas le niveau");
                return;
            }
        }
        this.selectView(view, true, true, false);
        this.m_canRegister = true;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "canRegister");
    }
    
    @Override
    public void unselectOccupation(final PartyOccupationView<PartyOccupation> view) {
        if (!view.isSelected()) {
            return;
        }
        this.selectView(view, false, true, false);
        if (this.m_selectedViews.isEmpty()) {
            this.m_canRegister = false;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "canRegister");
        }
    }
    
    public boolean canRegister() {
        if (this.m_leaderId != WakfuGameEntity.getInstance().getLocalPlayer().getId()) {
            PartySearchFeedbackManagement.computeFeedback(PartySearchFeedbackEnum.YOU_ARE_NOT_PARTY_LEADER);
            PartyOccupationRegistrationViews.m_logger.error((Object)"[PartySearch] On tente de s'enregistrer sans \u00eatre le chef du groupe");
            return false;
        }
        for (final PartyPlayerDefinitionView view : this.m_party) {
            final PartyPlayerDefinition def = view.getDefinition();
            if (def.getRole() == PartyRole.NONE) {
                PartyOccupationRegistrationViews.m_logger.error((Object)"[PartySearch] On tente d'enregistrer une entit\u00e9 qui n'a pas de r\u00f4le : c'est normalement impossible");
                PartySearchFeedbackManagement.computeFeedback(PartySearchFeedbackEnum.INVALID_ROLE);
                return false;
            }
        }
        if (this.m_description.length() > 80) {
            PartyOccupationRegistrationViews.m_logger.error((Object)"[PartySearch] On tente d'enregistrer une description trop longue : c'est normalement impossible");
            PartySearchFeedbackManagement.computeFeedback(PartySearchFeedbackEnum.TOO_LONG_DESC);
            return false;
        }
        return true;
    }
    
    public PartyRequester generatePartyRequester() {
        final ArrayList<PartyPlayerDefinition> definitions = new ArrayList<PartyPlayerDefinition>();
        for (final PartyPlayerDefinitionView view : this.m_party) {
            definitions.add(view.getDefinition());
        }
        final ArrayList<PartyOccupation> occupations = new ArrayList<PartyOccupation>();
        for (final PartyOccupationView<? extends PartyOccupation> occupation : this.m_selectedViews) {
            occupations.add((PartyOccupation)occupation.getOccupation());
        }
        PartyOccupationRegistrationViews.m_logger.info((Object)"[PartySearch] G\u00e9n\u00e9ration du PartyRequester");
        return new PartyRequester(this.m_leaderId, this.m_selectedMood.getMood(), this.m_description, GameDate.NULL_DATE, occupations, definitions);
    }
    
    public void selectRole(final PartyRoleView roleView, final long id) {
        for (final PartyPlayerDefinitionView view : this.m_party) {
            if (view.getId() == id) {
                view.setRole(roleView);
            }
        }
    }
    
    public void setRegistered(final boolean registered) {
        this.m_registered = registered;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "registered");
    }
    
    public void clearView(final boolean withParty, final boolean keepViews) {
        this.setSearchString(this.m_description = "");
        if (!keepViews) {
            final ImmutableList<PartyOccupationView<? extends PartyOccupation>> copy = (ImmutableList<PartyOccupationView<? extends PartyOccupation>>)ImmutableList.copyOf((Collection)this.m_selectedViews);
            for (int i = this.m_selectedViews.size() - 1; i >= 0; --i) {
                this.selectView(this.m_selectedViews.get(i), false, false, true);
            }
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedViews");
            for (final PartyOccupationView<? extends PartyOccupation> view : copy) {
                view.fireSelection();
            }
        }
        if (withParty) {
            this.updateParty();
        }
        else {
            this.recomputePartyLevel();
            this.filterPvePartyOccupationGroup();
        }
    }
    
    private void computeUnselection() {
        for (int i = this.m_selectedViews.size() - 1; i >= 0; --i) {
            final PartyOccupationView<? extends PartyOccupation> view = this.m_selectedViews.get(i);
            final PvePartyOccupation occupation = (PvePartyOccupation)view.getOccupation();
            if (occupation.getLevel() > this.getPartyMinLevel()) {
                this.selectView(view, false, true, true);
            }
        }
        if (this.m_selectedViews.isEmpty()) {
            this.m_canRegister = false;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "canRegister");
        }
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isLeader")) {
            return this.m_leaderId == WakfuGameEntity.getInstance().getLocalPlayer().getId();
        }
        if (fieldName.equals("member0")) {
            return this.m_party.isEmpty() ? null : this.m_party.get(0);
        }
        if (fieldName.equals("member1")) {
            return (this.m_party.size() > 1) ? this.m_party.get(1) : null;
        }
        if (fieldName.equals("member2")) {
            return (this.m_party.size() > 2) ? this.m_party.get(2) : null;
        }
        if (fieldName.equals("member3")) {
            return (this.m_party.size() > 3) ? this.m_party.get(3) : null;
        }
        if (fieldName.equals("member4")) {
            return (this.m_party.size() > 4) ? this.m_party.get(4) : null;
        }
        if (fieldName.equals("member5")) {
            return (this.m_party.size() > 5) ? this.m_party.get(5) : null;
        }
        if (fieldName.equals("registered")) {
            return this.m_registered;
        }
        if (fieldName.equals("canRegister")) {
            return this.m_canRegister;
        }
        if (fieldName.equals("selectedViews")) {
            return this.m_selectedPveOccupationGroup;
        }
        if (fieldName.equals("selectionEnabled")) {
            return this.m_selectedViews.size() < 20;
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public String toString() {
        return "PartyOccupationRegistrationViews{m_selectedViews=" + this.m_selectedViews + '}';
    }
    
    public List<PartyOccupationView<? extends PartyOccupation>> getSelectedViews() {
        return this.m_selectedViews;
    }
    
    protected void selectView(final PartyOccupationView<? extends PartyOccupation> view, final boolean select, final boolean update, final boolean force) {
        if (!force && view.isSelected() == select) {
            return;
        }
        view.setSelected(select, update);
        final PvePartyOccupationGroup group = view.getGroup();
        if (select) {
            if (!this.m_selectedViews.contains(view)) {
                this.m_selectedViews.add(view);
            }
            if (!this.m_selectedPveOccupationGroup.contains(group)) {
                this.m_selectedPveOccupationGroup.add(group);
                if (update) {
                    PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedViews");
                }
            }
            if (update) {
                PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectionEnabled");
            }
        }
        else {
            this.m_selectedViews.remove(view);
            if (!group.hasSelection()) {
                this.m_selectedPveOccupationGroup.remove(group);
                if (update) {
                    PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedViews");
                }
            }
            if (update) {
                PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectionEnabled");
            }
        }
    }
    
    public void setCanRegister(final boolean canRegister) {
        this.m_canRegister = canRegister;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "canRegister");
    }
    
    public void addPartyDefinition(final PartyPlayerDefinition definition) {
        final PartyPlayerDefinitionView view = new PartyPlayerDefinitionView(definition);
        for (final PartyPlayerDefinitionView def : this.m_party) {
            if (def.getId() == definition.getId()) {
                return;
            }
        }
        this.m_party.add(view);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "member0", "member1", "member2", "member3", "member4", "member5");
    }
    
    static {
        m_logger = Logger.getLogger((Class)PartyOccupationRegistrationViews.class);
    }
}
