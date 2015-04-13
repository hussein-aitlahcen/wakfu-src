package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.nation.data.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.buff.*;
import java.text.*;
import com.ankamagames.wakfu.common.game.nation.survey.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.common.game.nation.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;
import gnu.trove.*;

public class NationDisplayer implements FieldProvider
{
    private static final NationDisplayer m_instance;
    private static final Logger m_logger;
    public static final String NAME_FIELD = "name";
    public static final String CANDIDATES_FIELD = "candidates";
    public static final String GOVERNMENT_FIELD = "government";
    public static final String GOVERNOR_FIELD = "governor";
    public static final String MARSHAL_FIELD = "marshal";
    public static final String GENERAL_FIELD = "general";
    public static final String GOVERNOR_HISTORY_FIELD = "governorHistory";
    public static final String IS_VOTE_RUNNING_FIELD = "isVoteRunning";
    public static final String CANDIDATE_REGISTRATION_FEE_FIELD = "candidateRegistrationFee";
    public static final String NATION_COLOR = "nationColor";
    public static final String FLAG_ICON_URL_FIELD = "flagIconUrl";
    public static final String ELECTION_CLOSURE_DESCRIPTION = "electionClosureDescription";
    public static final String ELECTION_TOTAL_VOTES = "electionTotalVotes";
    public static final String ELECTION_TOTAL_CANDIDATES = "electionTotalCandidates";
    public static final String GOVERNOR_IS_LOCAL_PLAYER = "governorIsLocalPlayer";
    public static final String MARSHAL_IS_LOCAL_PLAYER = "marshalIsLocalPlayer";
    public static final String GENERAL_IS_LOCAL_PLAYER = "generalIsLocalPlayer";
    public static final String GOVERNOR_TITLES = "governorTitles";
    public static final String TIME_BEFORE_MANDATE_END = "timeBeforeMandateEnd";
    public static final String TIME_BEFORE_MANDATE_END_PERC = "timeBeforeMandateEndPerc";
    public static final String MANDATE_START_DATE = "mandateStartDate";
    public static final String MANDATE_END_DATE = "mandateEndDate";
    public static final String LOCAL_PLAYER_HAS_RANK_TO_CHANGE_TAXES = "localPlayerHasRankToChangeTaxes";
    public static final String LOCAL_PLAYER_HAS_RANK_TO_BUY_CHALLENGES = "localPlayerHasRankToBuyChallenges";
    public static final String LOCAL_PLAYER_HAS_RANK_TO_BUY_CLIMATE_BONUSES = "localPlayerHasRankToBuyClimateBonuses";
    public static final String LOCAL_PLAYER_HAS_RANK_TO_BUY_ZOOLOGY = "localPlayerHasRankToBuyZoology";
    public static final String LOCAL_PLAYER_HAS_RANK_TO_CHANGE_LAWS = "localPlayerHasRankToChangeLaws";
    public static final String LOCAL_PLAYER_HAS_RANK_TO_CHANGE_DIPLOMACY = "localPlayerHasRankToChangeDiplomacy";
    public static final String PROTECTOR_LIST = "protectorList";
    public static final String PROTECTOR_TOTAL_WILLS_SIZE = "protectorTotalWillsSize";
    public static final String PROTECTOR_SATISFIED_WILLS_SIZE = "protectorSatisfiedWillsSize";
    public static final String ECONOMY = "economy";
    public static final String LAWS = "laws";
    public static final String OTHER_NATIONS_LAWS = "otherNationsLaws";
    public static final String NATION_DIPLOMACY_LIST = "nationDiplomacyList";
    public static final String SORTED_NATION_TERRITORIES_LIST = "sortedNationTerritoriesList";
    public static final String CURRENT_GOVERNOR_OPINION_ID = "currentGovernorOpinionId";
    public static final String CURRENT_GOVERNOR_OPINION_TEXT = "currentGovernorOpinionText";
    public static final String CURRENT_GOVERNOR_OPINION_VALUE = "currentGovernorOpinionValue";
    public static final String CURRENT_GOVERNOR_OPINION_BAD_CONSEQUENCY = "opinionBadConsequency";
    public static final String CURRENT_GOVERNOR_OPINION_GOOD_CONSEQUENCY = "opinionGoodConsequency";
    public static final String CURRENT_GOVERNOR_POPULARITY_SCORE_TEXT = "currentGovernorPopularityScoreText";
    public static final String OPINION_DIRTY = "opinionDirty";
    public static final String CANT_GIVE_OPINION_REASON = "cantGiveOpinionReason";
    public static final String PROTECTOR_BUFFS_REGULAR_DESC = "protectorBuffsRegularDesc";
    public static final String PROTECTOR_BUFFS_REGULAR = "protectorBuffsRegular";
    public static final String PROTECTOR_BUFFS_SHUKRUTE = "protectorBuffsShukrute";
    public static final String PROTECTOR_NUM_BUFFS_REGULAR = "protectorNumBuffsRegular";
    public static final String PROTECTOR_NUM_BUFFS_SHUKRUTE = "protectorNumBuffsShukrute";
    public static final String[] FIELDS;
    public static final String ELECTION_INFOS_COLOR = "fca33a";
    private Nation m_nation;
    private final RatingProcedure m_ratingProcedure;
    private TIntObjectHashMap<ArrayList<CandidateInfoFieldProvider>> m_candidateInfosByOffset;
    private GovernorInfoFieldProvider m_governor;
    private GovernmentMemberInfoFieldProvider m_general;
    private GovernmentMemberInfoFieldProvider m_marshal;
    private CandidateInfoFieldProvider m_governorHistory;
    private TShortObjectHashMap<GovernorHonorificTitleView> m_governorTitles;
    private ArrayList<GovernorHonorificTitleView> m_sortedGovernorTitles;
    private TLongObjectHashMap<NationRankDisplayer> m_ranks;
    private ArrayList<NationRankDisplayer> m_ranksArrayList;
    private String m_temporarySpeech;
    private ArrayList<ProtectorInListView> m_protectors;
    private EconomyView m_economy;
    private LawsView m_lawsView;
    private ArrayList<NationDiplomacyView> m_nationDiplomacyList;
    private ArrayList<NationTerritoriesInfoView> m_nationTerritoriesList;
    private TIntObjectHashMap<String> m_nationColors;
    private TIntIntHashMap m_nationProtectorsCount;
    private ArrayList<LawsView> m_otherNationLaws;
    private byte m_currentOpinionId;
    final NationPartListener m_governorPartListener;
    final NationPartListener m_governmentFullPartListener;
    final NationPartListener m_economyPartListener;
    final NationPartListener m_protectorsPartListener;
    final NationPartListener m_lawsPartListener;
    final NationPartListener m_otherNationLawsPartListener;
    final NationPartListener m_governorOpinionPartListener;
    private TIntByteHashMap m_alignments;
    private TIntByteHashMap m_alignmentsWithRanks;
    final NationPartListener m_diplomacyPartListener;
    final NationPartListener m_diplomacyChangesPartListener;
    
    public static NationDisplayer getInstance() {
        return NationDisplayer.m_instance;
    }
    
    private NationDisplayer() {
        super();
        this.m_ratingProcedure = new RatingProcedure();
        this.m_candidateInfosByOffset = new TIntObjectHashMap<ArrayList<CandidateInfoFieldProvider>>();
        this.m_governorTitles = new TShortObjectHashMap<GovernorHonorificTitleView>();
        this.m_ranks = new TLongObjectHashMap<NationRankDisplayer>();
        this.m_nationColors = new TIntObjectHashMap<String>();
        this.m_nationProtectorsCount = new TIntIntHashMap();
        this.m_governorPartListener = new NationPartListener() {
            @Override
            public void onDataChanged() {
                NationDisplayer.this.clearGovernor();
                PropertiesProvider.getInstance().firePropertyValueChanged(NationDisplayer.this, "governor");
            }
        };
        this.m_governmentFullPartListener = new NationPartListener() {
            @Override
            public void onDataChanged() {
                NationDisplayer.this.fullUpdate();
            }
        };
        this.m_economyPartListener = new NationPartListener() {
            @Override
            public void onDataChanged() {
                NationDisplayer.this.m_economy = null;
                PropertiesProvider.getInstance().firePropertyValueChanged(NationDisplayer.this, "economy");
                UINationFrame.getInstance().setNetEnable(true);
            }
        };
        this.m_protectorsPartListener = new NationPartListener() {
            @Override
            public void onDataChanged() {
                NationDisplayer.this.m_protectors = null;
                PropertiesProvider.getInstance().firePropertyValueChanged(NationDisplayer.this, "protectorList");
                UINationFrame.getInstance().setNetEnable(true);
            }
        };
        this.m_lawsPartListener = new NationPartListener() {
            @Override
            public void onDataChanged() {
                NationDisplayer.this.clearLaws();
                PropertiesProvider.getInstance().firePropertyValueChanged(NationDisplayer.this, "laws");
                UINationFrame.getInstance().setNetEnable(true);
                PropertiesProvider.getInstance().setPropertyValue("lawsDirty", false);
                UINationFrame.getInstance().cleanLawChanges();
            }
        };
        this.m_otherNationLawsPartListener = new NationPartListener() {
            @Override
            public void onDataChanged() {
                NationDisplayer.this.clearOtherNationsLaws();
                PropertiesProvider.getInstance().firePropertyValueChanged(NationDisplayer.this, "otherNationsLaws");
                UINationFrame.getInstance().setNetEnable(true);
            }
        };
        this.m_governorOpinionPartListener = new NationPartListener() {
            @Override
            public void onDataChanged() {
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                NationDisplayer.this.setCurrentOpinionId(localPlayer.getCitizenComportment().getGovernmentOpinion().idx);
                UINationFrame.getInstance().setNetEnable(true);
                if (Xulor.getInstance().isLoaded("nationDialog")) {
                    final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("nationDialog");
                    final Container container = (Container)map.getElement("crownCursorContainer");
                    container.invalidate();
                }
            }
        };
        this.m_diplomacyPartListener = new NationPartListener() {
            @Override
            public void onDataChanged() {
                NationDisplayer.this.clearDiplomacy();
                PropertiesProvider.getInstance().firePropertyValueChanged(NationDisplayer.this, "nationDiplomacyList", "sortedNationTerritoriesList");
                UINationFrame.getInstance().setNetEnable(true);
                NationDisplayer.this.setWaitingForDiplomacyInformation(false);
            }
        };
        this.m_diplomacyChangesPartListener = new NationPartListener() {
            @Override
            public void onDataChanged() {
                NationDisplayer.this.onDiplomacyChange();
            }
        };
        for (final NationRank rank : NationRank.values()) {
            if (rank != NationRank.GOVERNOR && rank != NationRank.SPY) {
                if (rank != NationRank.ASSASSIN) {
                    this.m_ranks.put(rank.getId(), new NationRankDisplayer(rank));
                }
            }
        }
        for (final GovernorHonorificTitle honorificTitle : GovernorHonorificTitle.values()) {
            this.m_governorTitles.put(honorificTitle.getId(), new GovernorHonorificTitleView(honorificTitle));
        }
        this.m_governorTitles.put((short)(-1), new GovernorHonorificTitleView(null));
    }
    
    private void onDiplomacyChange() {
        if (this.m_nation == Nation.VOID_NATION) {
            return;
        }
        this.clearDiplomacy();
        boolean newDiplomacyRequest = false;
        final boolean rightToChangeDiplomacy = this.localPlayerHasRankToChangeDiplomacy();
        final int currentPageIndex = PropertiesProvider.getInstance().getIntProperty("nationCurrentPageIndex");
        final boolean internationalPolicyPage = currentPageIndex == 3;
        final boolean dialogOpenned = Xulor.getInstance().isLoaded("nationDialog") && internationalPolicyPage;
        if (rightToChangeDiplomacy && !internationalPolicyPage) {
            final TIntByteHashMap alignmentsWithRank = this.getDiplomacyAlignments(rightToChangeDiplomacy);
            final TIntByteIterator it = alignmentsWithRank.iterator();
            while (it.hasNext()) {
                it.advance();
                newDiplomacyRequest = true;
                final NationAlignement alignment = NationAlignement.getFromId(it.value());
                String chatTranslationKey = null;
                String notifTranslatorKey = null;
                switch (alignment) {
                    case ALLIED: {
                        chatTranslationKey = "nation.suggestAllianceChat";
                        notifTranslatorKey = "notification.diplomacySuggestAllianceText";
                        break;
                    }
                }
                if (chatTranslationKey != null) {
                    final int nationId = NationManager.INSTANCE.getNationById(it.key()).getNationId();
                    final String nationName = WakfuTranslator.getInstance().getString(39, nationId, new Object[0]);
                    final String message = WakfuTranslator.getInstance().getString(chatTranslationKey, nationName);
                    final ChatMessage chatMessage = new ChatMessage(message);
                    chatMessage.setPipeDestination(8);
                    ChatManager.getInstance().pushMessage(chatMessage);
                }
                if (notifTranslatorKey != null) {
                    final int nationId = NationManager.INSTANCE.getNationById(it.key()).getNationId();
                    final String nationName = WakfuTranslator.getInstance().getString(39, nationId, new Object[0]);
                    final String title = WakfuTranslator.getInstance().getString("notification.diplomacyTitle");
                    final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString(notifTranslatorKey, nationName), NotificationMessageType.NATION, "3");
                    final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.NATION, 600132);
                    Worker.getInstance().pushMessage(uiNotificationMessage);
                }
            }
        }
        final TIntByteHashMap alignments = this.getDiplomacyAlignments(false);
        if (this.m_alignments != null) {
            final TIntByteIterator it = alignments.iterator();
            while (it.hasNext()) {
                it.advance();
                if (it.value() != this.m_alignments.get(it.key())) {
                    newDiplomacyRequest = true;
                    final NationAlignement alignment = NationAlignement.getFromId(it.value());
                    String chatTranslationKey = null;
                    String notifTranslatorKey = null;
                    switch (alignment) {
                        case ALLIED: {
                            chatTranslationKey = "nation.allianceChat";
                            notifTranslatorKey = "notification.diplomacyAllianceText";
                            break;
                        }
                        case ENEMY: {
                            chatTranslationKey = "nation.declareWarChat";
                            notifTranslatorKey = "notification.diplomacyWarText";
                            break;
                        }
                    }
                    if (chatTranslationKey != null) {
                        final int nationId = NationManager.INSTANCE.getNationById(it.key()).getNationId();
                        final String nationName = WakfuTranslator.getInstance().getString(39, nationId, new Object[0]);
                        final String message = WakfuTranslator.getInstance().getString(chatTranslationKey, nationName);
                        final ChatMessage chatMessage = new ChatMessage(message);
                        chatMessage.setPipeDestination(8);
                        ChatManager.getInstance().pushMessage(chatMessage);
                    }
                    if (notifTranslatorKey != null) {
                        final int nationId = NationManager.INSTANCE.getNationById(it.key()).getNationId();
                        final String nationName = WakfuTranslator.getInstance().getString(39, nationId, new Object[0]);
                        final String title = WakfuTranslator.getInstance().getString("notification.diplomacyTitle");
                        final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString(notifTranslatorKey, nationName), NotificationMessageType.NATION, "3");
                        final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.NATION, 600132);
                        Worker.getInstance().pushMessage(uiNotificationMessage);
                        break;
                    }
                    break;
                }
            }
        }
        this.m_alignments = alignments;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "nationDiplomacyList", "sortedNationTerritoriesList");
        if (!dialogOpenned && newDiplomacyRequest) {
            UIControlCenterContainerFrame.getInstance().highLightCommunityButton();
        }
        UINationFrame.getInstance().setNetEnable(true);
    }
    
    private TIntByteHashMap getDiplomacyAlignments(final boolean hasRightToChangeDiplomacy) {
        final TIntByteHashMap alignments = new TIntByteHashMap();
        for (final NationDiplomacyView nationDiplomacyView : this.getNationDiplomacyList()) {
            if (nationDiplomacyView.isLocalNation()) {
                continue;
            }
            final int nationId = nationDiplomacyView.getNationId();
            if (hasRightToChangeDiplomacy) {
                if (!nationDiplomacyView.isWaitingForAllianceAnswer()) {
                    continue;
                }
                alignments.put(nationId, NationAlignement.ALLIED.getId());
            }
            else {
                alignments.put(nationId, nationDiplomacyView.getLocalAlignment().getId());
            }
        }
        return alignments;
    }
    
    public void setNation(final Nation nation) {
        if (this.m_nation != null) {
            this.m_nation.getPart(NationSerializationType.Part.GOVERNOR_BOOK).removeDataChangedListener(this.m_governorPartListener);
            this.m_nation.getPart(NationSerializationType.Part.GOVERNMENT_FULL).removeDataChangedListener(this.m_governmentFullPartListener);
            this.m_nation.getPart(NationSerializationType.Part.ECONOMY).removeDataChangedListener(this.m_economyPartListener);
            this.m_nation.getPart(NationSerializationType.Part.PROTECTORS).removeDataChangedListener(this.m_protectorsPartListener);
            this.m_nation.getPart(NationSerializationType.Part.LAWS).removeDataChangedListener(this.m_lawsPartListener);
            this.m_nation.getPart(NationSerializationType.Part.SURVEY).removeDataChangedListener(this.m_governorOpinionPartListener);
            final TIntObjectIterator<Nation> it = NationManager.INSTANCE.nationsIterator();
            while (it.hasNext()) {
                it.advance();
                final Nation n = it.value();
                n.getPart(NationSerializationType.Part.DIPLOMACY).removeDataChangedListener(this.m_diplomacyPartListener);
                n.getPart(NationSerializationType.Part.DIPLOMACY).removeDataChangedListener(this.m_diplomacyChangesPartListener);
                n.getPart(NationSerializationType.Part.LAWS).removeDataChangedListener(this.m_otherNationLawsPartListener);
            }
        }
        this.m_nation = nation;
        PropertiesProvider.getInstance().setPropertyValue("nation", this);
        this.m_nation.getPart(NationSerializationType.Part.GOVERNOR_BOOK).addDataChangedListener(this.m_governorPartListener);
        this.m_nation.getPart(NationSerializationType.Part.GOVERNMENT_FULL).addDataChangedListener(this.m_governmentFullPartListener);
        this.m_nation.getPart(NationSerializationType.Part.ECONOMY).addDataChangedListener(this.m_economyPartListener);
        this.m_nation.getPart(NationSerializationType.Part.PROTECTORS).addDataChangedListener(this.m_protectorsPartListener);
        this.m_nation.getPart(NationSerializationType.Part.LAWS).addDataChangedListener(this.m_lawsPartListener);
        this.m_nation.getPart(NationSerializationType.Part.SURVEY).addDataChangedListener(this.m_governorOpinionPartListener);
        final TIntObjectIterator<Nation> it = NationManager.INSTANCE.nationsIterator();
        while (it.hasNext()) {
            it.advance();
            final Nation n = it.value();
            n.getPart(NationSerializationType.Part.DIPLOMACY).addDataChangedListener(this.m_diplomacyChangesPartListener);
            n.getPart(NationSerializationType.Part.LAWS).addDataChangedListener(this.m_otherNationLawsPartListener);
        }
        this.updateUI();
    }
    
    public String getName() {
        if (this.m_nation != null) {
            return WakfuTranslator.getInstance().getString(39, this.m_nation.getNationId(), new Object[0]);
        }
        return null;
    }
    
    public NationRankDisplayer getRank(final long rankId) {
        return this.m_ranks.get(rankId);
    }
    
    @Override
    public String[] getFields() {
        return NationDisplayer.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("candidates")) {
            final int offset = UIVoteInformationFrame.getInstance().getCurrentCandidatesOffset();
            if (this.m_candidateInfosByOffset.isEmpty() || !this.m_candidateInfosByOffset.containsKey(offset)) {
                return null;
            }
            return this.m_candidateInfosByOffset.get(offset);
        }
        else {
            if (fieldName.equals("isVoteRunning")) {
                return this.m_nation.isVoteRunning();
            }
            if (fieldName.equals("candidateRegistrationFee")) {
                return this.getCandidateRegistrationFee();
            }
            if (fieldName.equals("government")) {
                if (this.m_ranksArrayList == null) {
                    this.m_ranksArrayList = new ArrayList<NationRankDisplayer>();
                    final TLongObjectIterator<NationRankDisplayer> it = this.m_ranks.iterator();
                    while (it.hasNext()) {
                        it.advance();
                        this.m_ranksArrayList.add(it.value());
                    }
                    Collections.sort(this.m_ranksArrayList, new Comparator<Object>() {
                        @Override
                        public int compare(final Object o1, final Object o2) {
                            return (int)(((NationRankDisplayer)o1).getRankId() - ((NationRankDisplayer)o2).getRankId());
                        }
                    });
                }
                return this.m_ranksArrayList;
            }
            if (fieldName.equals("governorHistory")) {
                return this.m_governorHistory;
            }
            if (fieldName.equals("governor")) {
                return this.getOrCreateGovernor();
            }
            if (fieldName.equals("marshal")) {
                return this.getOrCreateMarshal();
            }
            if (fieldName.equals("general")) {
                return this.getOrCreateGeneral();
            }
            if (fieldName.equals("flagIconUrl")) {
                final int nationId = this.m_nation.getNationId();
                return WakfuConfiguration.getInstance().getFlagIconUrl((nationId == 0) ? -1 : nationId);
            }
            if (fieldName.equals("electionClosureDescription")) {
                return WakfuTranslator.getInstance().getString("election.closureDescription", "fca33a", this.getElectionRemainingTime());
            }
            if (fieldName.equals("electionTotalCandidates")) {
                final int nbCandidates = this.m_nation.isVoteRunning() ? this.m_nation.getNbCandidates() : this.m_nation.getElectionHistory().getNbCandidates();
                return WakfuTranslator.getInstance().getString("election.totalCandidates", "fca33a", nbCandidates);
            }
            if (fieldName.equals("electionTotalVotes")) {
                final int candidateBallots = this.m_nation.isVoteRunning() ? this.m_nation.getNbCandidateBallots() : this.m_nation.getElectionHistory().getNbCandidateBallots();
                return WakfuTranslator.getInstance().getString("election.totalVotes", "fca33a", candidateBallots);
            }
            if (fieldName.equals("governorIsLocalPlayer")) {
                if (this.m_governor == null) {
                    return false;
                }
                return this.m_governor.getCandidateInfo().getId() == WakfuGameEntity.getInstance().getLocalPlayer().getId();
            }
            else if (fieldName.equals("marshalIsLocalPlayer")) {
                if (this.m_marshal == null) {
                    return false;
                }
                return this.m_marshal.getCandidateInfo().getId() == WakfuGameEntity.getInstance().getLocalPlayer().getId();
            }
            else if (fieldName.equals("generalIsLocalPlayer")) {
                if (this.m_general == null) {
                    return false;
                }
                return this.m_general.getCandidateInfo().getId() == WakfuGameEntity.getInstance().getLocalPlayer().getId();
            }
            else {
                if (fieldName.equals("governorTitles")) {
                    if (this.m_sortedGovernorTitles == null) {
                        this.m_sortedGovernorTitles = new ArrayList<GovernorHonorificTitleView>();
                        final TShortObjectIterator<GovernorHonorificTitleView> it2 = this.m_governorTitles.iterator();
                        while (it2.hasNext()) {
                            it2.advance();
                            this.m_sortedGovernorTitles.add(it2.value());
                        }
                        Collections.sort(this.m_sortedGovernorTitles, new Comparator<GovernorHonorificTitleView>() {
                            @Override
                            public int compare(final GovernorHonorificTitleView o1, final GovernorHonorificTitleView o2) {
                                if (o1.getTitle() == null) {
                                    return -1;
                                }
                                if (o2.getTitle() == null) {
                                    return 1;
                                }
                                if (o1.getTitle().getId() < o2.getTitle().getId()) {
                                    return -1;
                                }
                                if (o2.getTitle().getId() < o1.getTitle().getId()) {
                                    return -1;
                                }
                                return 0;
                            }
                        });
                    }
                    return this.m_sortedGovernorTitles;
                }
                if (fieldName.equals("nationColor")) {
                    return this.getColor();
                }
                if (fieldName.equals("mandateStartDate")) {
                    final GameDate startDate = this.getMandateStartDate();
                    final int mins = startDate.getMinutes();
                    return WakfuTranslator.getInstance().getString("nation.shortElectionDate", startDate.getDay(), startDate.getMonth(), startDate.getYear(), startDate.getHours(), (mins < 10) ? ("0" + mins) : mins);
                }
                if (fieldName.equals("mandateEndDate")) {
                    final GameDate endDate = this.getMandateEndDate();
                    final int mins = endDate.getMinutes();
                    return WakfuTranslator.getInstance().getString("nation.shortElectionDate", endDate.getDay(), endDate.getMonth(), endDate.getYear(), endDate.getHours(), (mins < 10) ? ("0" + mins) : mins);
                }
                if (fieldName.equals("timeBeforeMandateEndPerc")) {
                    return new Percentage(this.getTimeBeforeMandatePerc() * 100.0f);
                }
                if (fieldName.equals("localPlayerHasRankToChangeTaxes")) {
                    final NationRank rank = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getRank();
                    return rank != null && NationRankRightChecker.hasRankToManageTax(rank);
                }
                if (fieldName.equals("localPlayerHasRankToBuyClimateBonuses")) {
                    final NationRank rank = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getRank();
                    return rank != null && NationRankRightChecker.hasRankToManageWeather(rank);
                }
                if (fieldName.equals("localPlayerHasRankToBuyZoology")) {
                    final NationRank rank = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getRank();
                    return rank != null && NationRankRightChecker.hasRankToManageEcosystem(rank);
                }
                if (fieldName.equals("localPlayerHasRankToChangeLaws")) {
                    return this.localPlayerHasRankToChangeLaws();
                }
                if (fieldName.equals("localPlayerHasRankToBuyChallenges")) {
                    final NationRank rank = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getRank();
                    return rank != null && NationRankRightChecker.hasRankToManageChallenge(rank);
                }
                if (fieldName.equals("localPlayerHasRankToChangeDiplomacy")) {
                    return this.localPlayerHasRankToChangeDiplomacy();
                }
                if (fieldName.equals("protectorTotalWillsSize")) {
                    int count = 0;
                    final ArrayList<ProtectorInListView> protectors = this.getProtectors();
                    for (final ProtectorInListView protectorInListView : protectors) {
                        count += protectorInListView.getTotalSatisfaction();
                    }
                    return count;
                }
                if (fieldName.equals("protectorSatisfiedWillsSize")) {
                    int count = 0;
                    for (final ProtectorInListView protectorInListView2 : this.getProtectors()) {
                        count += protectorInListView2.getCurrentSatisfaction();
                    }
                    return count;
                }
                if (fieldName.equals("protectorList")) {
                    return this.getProtectors();
                }
                if (fieldName.equals("timeBeforeMandateEnd")) {
                    return this.getTimeBeforeMandatePerc();
                }
                if (fieldName.equals("otherNationsLaws")) {
                    if (this.m_otherNationLaws == null) {
                        this.m_otherNationLaws = new ArrayList<LawsView>();
                        final TIntObjectIterator<Nation> it3 = NationManager.INSTANCE.realNationIterator(this.m_nation.getNationId());
                        while (it3.hasNext()) {
                            it3.advance();
                            final Nation nation = it3.value();
                            this.m_otherNationLaws.add(new LawsView(nation));
                        }
                    }
                    return this.m_otherNationLaws;
                }
                if (fieldName.equals("laws")) {
                    return this.getLaws();
                }
                if (fieldName.equals("economy")) {
                    if (this.m_economy == null) {
                        this.m_economy = new EconomyView(this.m_nation.getProtectorInfoManager().getEconomyHandler());
                    }
                    return this.m_economy;
                }
                if (fieldName.equals("nationDiplomacyList")) {
                    return this.getNationDiplomacyList();
                }
                if (fieldName.equals("sortedNationTerritoriesList")) {
                    return this.getSortedNationDiplomacyList();
                }
                if (fieldName.equals("opinionBadConsequency")) {
                    final NumberFormat formatter = new DecimalFormat("#0.##");
                    final String percentage = formatter.format(-50.0) + '%';
                    return WakfuTranslator.getInstance().getString("nation.opinionBadConsequency", percentage);
                }
                if (fieldName.equals("opinionGoodConsequency")) {
                    final NumberFormat formatter = new DecimalFormat("#0.##");
                    final String percentage = formatter.format(50.0) + '%';
                    return WakfuTranslator.getInstance().getString("nation.opinionGoodConsequency", percentage);
                }
                if (fieldName.equals("currentGovernorPopularityScoreText")) {
                    final NationSurveyHandler surveyHandler = this.m_nation.getSurveyHandler();
                    final NumberFormat formatter2 = new DecimalFormat("#0.##");
                    final TextWidgetFormater twf = new TextWidgetFormater();
                    twf.b().append(WakfuTranslator.getInstance().getString("nation.opinionScore")).append(" : ");
                    twf.append(formatter2.format(surveyHandler.getPopularityRate() * 100.0f)).append('%');
                    return twf.finishAndToString();
                }
                if (fieldName.equals("currentGovernorOpinionText")) {
                    final NationSurveyHandler surveyHandler = this.m_nation.getSurveyHandler();
                    final int likeValue = surveyHandler.get(GovernmentOpinion.LIKE);
                    final int unknownValue = surveyHandler.get(GovernmentOpinion.UNKNOWN);
                    final int unlikeValue = surveyHandler.get(GovernmentOpinion.UNLIKE);
                    final NumberFormat formatter3 = new DecimalFormat("#0.##");
                    final TextWidgetFormater twf2 = new TextWidgetFormater();
                    twf2.b().append(WakfuTranslator.getInstance().getString("nation.opinionScore")).append(" : ");
                    twf2.append(formatter3.format(surveyHandler.getPopularityRate() * 100.0f)).append("%");
                    twf2._b().newLine();
                    twf2.openText().addColor(Color.GREEN.getRGBtoHex());
                    twf2.append(WakfuTranslator.getInstance().getString("nation.opinionGood")).append(" : ");
                    twf2.append(likeValue);
                    twf2.closeText().newLine();
                    twf2.openText().addColor(Color.ORANGE.getRGBtoHex());
                    twf2.append(WakfuTranslator.getInstance().getString("nation.opinionNeutral")).append(" : ");
                    twf2.append(unknownValue);
                    twf2.closeText().newLine();
                    twf2.openText().addColor(Color.RED.getRGBtoHex());
                    twf2.append(WakfuTranslator.getInstance().getString("nation.opinionBad")).append(" : ");
                    twf2.append(unlikeValue);
                    twf2.closeText();
                    return twf2.finishAndToString();
                }
                if (fieldName.equals("currentGovernorOpinionValue")) {
                    final NationSurveyHandler surveyHandler = this.m_nation.getSurveyHandler();
                    return new Percentage(((surveyHandler.getTotalOpinions() > 0) ? (surveyHandler.getPopularityRate() + 1.0f) : 1.0f) / 2.0f * 100.0f);
                }
                if (fieldName.equals("opinionDirty")) {
                    return this.m_currentOpinionId != WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getGovernmentOpinion().idx;
                }
                if (fieldName.equals("cantGiveOpinionReason")) {
                    final NationSurveyHandler surveyHandler = this.m_nation.getSurveyHandler();
                    final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                    if (!localPlayer.hasSubscriptionRight(SubscriptionRight.POLITIC_INTERACTION)) {
                        return WakfuTranslator.getInstance().getString("error.playerNotSubscriptionRight");
                    }
                    if (!WakfuAccountPermissionContext.SUBSCRIBER.hasPermission(localPlayer)) {
                        return WakfuTranslator.getInstance().getString("error.playerNotSubscribed");
                    }
                    final int reasonId = surveyHandler.canGiveOpinion(localPlayer);
                    if (reasonId == 0) {
                        return null;
                    }
                    switch (reasonId) {
                        case 2: {
                            return WakfuTranslator.getInstance().getString("opinionReason.didntVote");
                        }
                        case 1: {
                            return WakfuTranslator.getInstance().getString("opinionReason.voteRunning");
                        }
                        case 3: {
                            return WakfuTranslator.getInstance().getString("opinionReason.noGovernor");
                        }
                        default: {
                            return null;
                        }
                    }
                }
                else {
                    if (fieldName.equals("currentGovernorOpinionId")) {
                        return this.m_currentOpinionId;
                    }
                    if (fieldName.equals("protectorBuffsRegularDesc")) {
                        return this.getProtectorBuffsDescription(BuffOrigin.MDC);
                    }
                    if (fieldName.equals("protectorBuffsRegular")) {
                        return this.getProtectorBuffs(BuffOrigin.MDC);
                    }
                    if (fieldName.equals("protectorBuffsShukrute")) {
                        return this.getProtectorBuffs(BuffOrigin.SHUKRUTE);
                    }
                    if (fieldName.equals("protectorNumBuffsRegular")) {
                        return this.getProtectorNumBuffs(BuffOrigin.MDC);
                    }
                    if (fieldName.equals("protectorNumBuffsShukrute")) {
                        return this.getProtectorNumBuffs(BuffOrigin.SHUKRUTE);
                    }
                    return null;
                }
            }
        }
    }
    
    private Object getOrCreateGovernor() {
        if (this.m_governor == null) {
            final GovernmentInfo governor = this.m_nation.getGovernment().getGovernor();
            if (governor != null) {
                this.m_governor = new GovernorInfoFieldProvider(governor);
            }
        }
        return this.m_governor;
    }
    
    private Object getOrCreateMarshal() {
        if (this.m_marshal == null) {
            final GovernmentInfo marshal = this.m_nation.getGovernment().getMember(NationRank.MARSHAL);
            if (marshal != null) {
                this.m_marshal = new GovernmentMemberInfoFieldProvider(marshal);
            }
        }
        return this.m_marshal;
    }
    
    private Object getOrCreateGeneral() {
        if (this.m_general == null) {
            final GovernmentInfo general = this.m_nation.getGovernment().getMember(NationRank.GENERAL);
            if (general != null) {
                this.m_general = new GovernmentMemberInfoFieldProvider(general);
            }
        }
        return this.m_general;
    }
    
    private ArrayList<ProtectorBuffView> getProtectorBuffs(final BuffOrigin origin) {
        final IntArray buffs = this.m_nation.getAllProtectorsBuffs();
        if (buffs == null) {
            return null;
        }
        final ArrayList<ProtectorBuffView> views = new ArrayList<ProtectorBuffView>();
        for (int i = 0, size = buffs.size(); i < size; ++i) {
            final ProtectorBuff buff = ProtectorBuffManager.INSTANCE.getBuff(buffs.get(i));
            if (buff.getOrigin() == origin) {
                views.add(new ProtectorBuffView(buff, null, false));
            }
        }
        return views;
    }
    
    private String getProtectorBuffsDescription(final BuffOrigin origin) {
        final IntArray buffs = this.m_nation.getAllProtectorsBuffs();
        if (buffs == null) {
            return null;
        }
        final List<String> effects = new ArrayList<String>();
        final TObjectIntHashMap<String> effectCount = new TObjectIntHashMap<String>();
        for (int i = 0, size = buffs.size(); i < size; ++i) {
            final ProtectorBuff buff = ProtectorBuffManager.INSTANCE.getBuff(buffs.get(i));
            if (buff.getOrigin() == origin) {
                final ContainerWriter writer = new DefaultContainerWriter<Object>(buff, buff.getId(), buff.getLevel());
                final ArrayList<String> buffEffects = writer.writeContainer();
                for (final String buffEffect : buffEffects) {
                    if (!effects.contains(buffEffect)) {
                        effects.add(buffEffect);
                    }
                    effectCount.adjustOrPutValue(buffEffect, 1, 1);
                }
            }
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        for (int j = 0, size2 = effects.size(); j < size2; ++j) {
            if (j != 0) {
                sb.newLine();
            }
            final String effectDescription = effects.get(j);
            final int count = effectCount.get(effectDescription);
            sb.append(effectDescription);
            if (count != 1) {
                sb.append(" (x").append(count).append(')');
            }
        }
        return sb.finishAndToString();
    }
    
    private int getProtectorNumBuffs(final BuffOrigin origin) {
        final IntArray buffs = this.m_nation.getAllProtectorsBuffs();
        if (buffs == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0, size = buffs.size(); i < size; ++i) {
            final ProtectorBuff buff = ProtectorBuffManager.INSTANCE.getBuff(buffs.get(i));
            if (buff.getOrigin() == origin) {
                ++count;
            }
        }
        return count;
    }
    
    private boolean localPlayerIsInGovernment() {
        return this.m_nation.getGovernment().getMember(WakfuGameEntity.getInstance().getLocalPlayer().getId()) != null;
    }
    
    private boolean localPlayerHasRankToChangeDiplomacy() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return false;
        }
        final NationRank rank = localPlayer.getCitizenComportment().getRank();
        return rank != null && NationRankRightChecker.hasRankToChangeDiplomacy(rank);
    }
    
    public void putNationColor(final int nationId, final String color) {
        this.m_nationColors.put(nationId, color);
    }
    
    public String getNationColor(final int nationId) {
        return this.m_nationColors.get(nationId);
    }
    
    private ArrayList<NationDiplomacyView> getNationDiplomacyList() {
        if (this.m_nationDiplomacyList == null) {
            this.m_nationDiplomacyList = new ArrayList<NationDiplomacyView>();
            final TIntObjectIterator<Nation> it = NationManager.INSTANCE.realNationIterator(new Integer[0]);
            while (it.hasNext()) {
                it.advance();
                final int nationId = it.key();
                this.m_nationDiplomacyList.add(new NationDiplomacyView(nationId));
            }
        }
        return this.m_nationDiplomacyList;
    }
    
    private ArrayList<NationTerritoriesInfoView> getSortedNationDiplomacyList() {
        if (this.m_nationTerritoriesList == null) {
            this.m_nationTerritoriesList = new ArrayList<NationTerritoriesInfoView>();
            final TIntObjectIterator<Nation> it = NationManager.INSTANCE.nationsIterator();
            while (it.hasNext()) {
                it.advance();
                final int nationId = it.key();
                if (this.getNationProtectorCount(nationId) > 0) {
                    this.m_nationTerritoriesList.add(new NationTerritoriesInfoView(nationId));
                }
            }
            Collections.sort(this.m_nationTerritoriesList, new Comparator<NationTerritoriesInfoView>() {
                @Override
                public int compare(final NationTerritoriesInfoView o1, final NationTerritoriesInfoView o2) {
                    return o2.getProtectorsSize() - o1.getProtectorsSize();
                }
            });
            int currentCount = 0;
            for (final NationTerritoriesInfoView nationTerritoriesInfoView : this.m_nationTerritoriesList) {
                currentCount += nationTerritoriesInfoView.getProtectorsSize();
                nationTerritoriesInfoView.setCumulatedProtectorsSize(currentCount);
            }
            Collections.sort(this.m_nationTerritoriesList, new Comparator<NationTerritoriesInfoView>() {
                @Override
                public int compare(final NationTerritoriesInfoView o1, final NationTerritoriesInfoView o2) {
                    if (o2.getCumulatedProtectorsSize() == o1.getCumulatedProtectorsSize()) {
                        return o1.getProtectorsSize() - o2.getProtectorsSize();
                    }
                    return o2.getCumulatedProtectorsSize() - o1.getCumulatedProtectorsSize();
                }
            });
        }
        return this.m_nationTerritoriesList;
    }
    
    private boolean localPlayerHasRankToChangeLaws() {
        final NationRank rank = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getRank();
        return rank != null && NationRankRightChecker.hasRankToChangeLaws(rank);
    }
    
    private LawsView getLaws() {
        if (this.m_lawsView == null) {
            this.m_lawsView = new LawsView(this.m_nation);
        }
        return this.m_lawsView;
    }
    
    public int getCurrentLawPoints() {
        return this.m_lawsView.getCurrentLawPoints();
    }
    
    private ArrayList<ProtectorInListView> getProtectors() {
        if (this.m_protectors == null) {
            this.m_protectors = new ArrayList<ProtectorInListView>();
            final TIntObjectIterator<NationProtectorInfo> it = this.m_nation.getProtectorInfoManager().getIterator();
            while (it.hasNext()) {
                it.advance();
                this.m_protectors.add(new ProtectorInListView(it.value()));
            }
        }
        return this.m_protectors;
    }
    
    public void updateLawsViewPoints() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_lawsView, "currentLawPoints");
    }
    
    public int getTotalProtectorsSize() {
        int total = 0;
        final TIntObjectIterator<Nation> it = NationManager.INSTANCE.nationsIterator();
        while (it.hasNext()) {
            it.advance();
            total += this.getNationProtectorCount(it.key());
        }
        return total;
    }
    
    public int getNationProtectorCount(final int nationId) {
        return this.m_nationProtectorsCount.get(nationId);
    }
    
    public void putNationProtector(final int nationId, final int numProtector) {
        this.m_nationProtectorsCount.put(nationId, numProtector);
    }
    
    public void onOtherNationLawsOpenned(final Nation nation) {
        nation.getPart(NationSerializationType.Part.LAWS).addDataChangedListener(this.m_otherNationLawsPartListener);
    }
    
    public void onOtherNationLawsClosed(final Nation nation) {
        nation.getPart(NationSerializationType.Part.LAWS).removeDataChangedListener(this.m_otherNationLawsPartListener);
    }
    
    public void setCurrentOpinionId(final byte currentOpinionId) {
        this.m_currentOpinionId = currentOpinionId;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentGovernorOpinionId", "currentGovernorOpinionValue", "opinionDirty");
    }
    
    public byte getCurrentOpinionId() {
        return this.m_currentOpinionId;
    }
    
    public void updateMarshal() {
        if (this.m_marshal == null) {
            return;
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_marshal, this.m_marshal.getFields());
    }
    
    public void updateGeneral() {
        if (this.m_general == null) {
            return;
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_general, this.m_general.getFields());
    }
    
    public void updateMember(final GovernmentMemberInfoFieldProvider member) {
        PropertiesProvider.getInstance().firePropertyValueChanged(member, member.getFields());
    }
    
    public void clean() {
        this.clearAllNation();
        this.clearDiplomacyCache();
    }
    
    public void clearAllNation() {
        this.clearDiplomacy();
        this.cleanAllLaws();
        this.clearGovernor();
        this.clearMarshal();
        this.clearGeneral();
        this.clearOtherNationsLaws();
        this.setWaitingForDiplomacyInformation(false);
        this.setTemporarySpeech(null);
    }
    
    public void setWaitingForDiplomacyInformation(final boolean waitingForDiplomacyInformation) {
        if (waitingForDiplomacyInformation) {
            final TIntObjectIterator<Nation> it = NationManager.INSTANCE.nationsIterator();
            while (it.hasNext()) {
                it.advance();
                final Nation n = it.value();
                n.getPart(NationSerializationType.Part.DIPLOMACY).removeDataChangedListener(this.m_diplomacyChangesPartListener);
                n.getPart(NationSerializationType.Part.DIPLOMACY).addDataChangedListener(this.m_diplomacyPartListener);
            }
        }
        else {
            final TIntObjectIterator<Nation> it = NationManager.INSTANCE.nationsIterator();
            while (it.hasNext()) {
                it.advance();
                final Nation n = it.value();
                n.getPart(NationSerializationType.Part.DIPLOMACY).removeDataChangedListener(this.m_diplomacyPartListener);
                n.getPart(NationSerializationType.Part.DIPLOMACY).addDataChangedListener(this.m_diplomacyChangesPartListener);
            }
        }
    }
    
    public void fullUpdate() {
        this.clean();
        this.getOrCreateGeneral();
        this.getOrCreateGovernor();
        this.getOrCreateMarshal();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, NationDisplayer.FIELDS);
    }
    
    public void updateProtectorList() {
        this.m_protectors = null;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "protectorList");
    }
    
    private float getTimeBeforeMandatePerc() {
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        final GameDate mandateDate = this.getMandateStartDate();
        final GameInterval gameInterval = mandateDate.timeTo(now);
        final GameInterval totalInterval = new GameInterval(this.m_nation.getVoteFrequency());
        totalInterval.add(this.m_nation.getVoteDuration());
        return gameInterval.toSeconds() / totalInterval.toSeconds();
    }
    
    private GameDate getMandateEndDate() {
        final GameDate endDate = new GameDate(this.m_nation.getVoteStartDate());
        endDate.add(this.m_nation.getVoteDuration());
        return endDate;
    }
    
    private GameDate getMandateStartDate() {
        final GameDate mandateDate = new GameDate(this.m_nation.getVoteStartDate());
        mandateDate.add(this.m_nation.getVoteDuration());
        mandateDate.sub(this.m_nation.getVoteFrequency());
        return mandateDate;
    }
    
    private String getElectionRemainingTime() {
        final GameDate now = GameDate.fromLong(System.currentTimeMillis());
        final GameDateConst startDate = this.m_nation.getVoteStartDate();
        final GameIntervalConst remaining = new GameInterval(this.m_nation.getVoteDuration().toSeconds() - startDate.timeTo(now).toSeconds());
        if (!remaining.isPositive()) {
            return "-";
        }
        return WakfuTranslator.getInstance().getString("remainingDuration", 0, 0, remaining.getDays(), remaining.getHours(), remaining.getMinutes(), remaining.getSeconds());
    }
    
    public int getCandidateRegistrationFee() {
        return this.m_nation.getCandidateRegistrationFee();
    }
    
    private Color getColor() {
        return WakfuClientConstants.PASSPORT_TEXT_COLOR;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    private void updateVIPList() {
        if (this.m_nation == null) {
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final NationGovernment government = this.m_nation.getGovernment();
        final GovernmentInfo localMember = government.getMember(localPlayer.getId());
        final TLongObjectIterator<NationRankDisplayer> it = this.m_ranks.iterator();
        while (it.hasNext()) {
            it.advance();
            final NationRankDisplayer displayer = it.value();
            final NationRank rank = displayer.getRank();
            final GovernmentInfo member = government.getMember(rank);
            displayer.setOwner((member == null) ? null : member.getName());
        }
    }
    
    private void updateCandidateInfoUI() {
        if (this.m_nation == null) {
            return;
        }
        int candidatesOffset = this.m_nation.isVoteRunning() ? UIVoteInformationFrame.getInstance().getCurrentCandidatesOffset() : 0;
        while (candidatesOffset != -1) {
            final ArrayList<CandidateInfoFieldProvider> candidatesView = this.m_candidateInfosByOffset.get(candidatesOffset);
            if (candidatesView != null && !this.m_nation.isVoteRunning()) {
                candidatesOffset = this.getCurrentFreeOffset(candidatesOffset + 1);
            }
            else {
                final ArrayList<CandidateInfoFieldProvider> arrayList = new ArrayList<CandidateInfoFieldProvider>();
                final ArrayList<CandidateInfo> candidates = new ArrayList<CandidateInfo>();
                if (this.m_nation.isVoteRunning()) {
                    this.m_nation.getCandidates(0, 3, candidates);
                }
                else if (arrayList.isEmpty()) {
                    this.m_nation.getElectionHistory().getCandidates(candidatesOffset * 3, 3, candidates);
                }
                if (candidates.isEmpty()) {
                    candidatesOffset = -1;
                }
                else {
                    boolean first = true;
                    for (final CandidateInfo candidateInfo : candidates) {
                        if (candidatesOffset == 0 && first && !this.m_nation.isVoteRunning() && !candidateInfo.isWithDraw()) {
                            arrayList.add(new GovernorHistoryFieldProvider(candidateInfo));
                        }
                        else {
                            arrayList.add(new CandidateInfoFieldProvider(candidateInfo));
                        }
                        first = false;
                    }
                    this.m_candidateInfosByOffset.put(candidatesOffset, arrayList);
                    candidatesOffset = (this.m_nation.isVoteRunning() ? -1 : this.getCurrentFreeOffset(candidatesOffset + 1));
                }
            }
        }
    }
    
    private int getCurrentFreeOffset(final int offSet) {
        for (int i = offSet; i < this.m_candidateInfosByOffset.size(); ++i) {
            final ArrayList<CandidateInfoFieldProvider> candidates = this.m_candidateInfosByOffset.get(i);
            if (candidates == null) {
                return -1;
            }
            if (candidates.size() < 3) {
                return i;
            }
        }
        return offSet;
    }
    
    public void clearElections() {
        final TIntObjectIterator<ArrayList<CandidateInfoFieldProvider>> it = this.m_candidateInfosByOffset.iterator();
        while (it.hasNext()) {
            it.advance();
            final ArrayList<CandidateInfoFieldProvider> list = it.value();
            for (int i = 0, size = list.size(); i < size; ++i) {
                list.get(i).clear();
            }
        }
        this.m_candidateInfosByOffset.clear();
        this.m_nation.clearCandidates();
    }
    
    public void clearLaws() {
        this.m_lawsView = null;
    }
    
    public void clearOtherNationsLaws() {
        this.m_otherNationLaws = null;
    }
    
    public void clearGovernor() {
        this.m_governor = null;
    }
    
    public void clearMarshal() {
        this.m_marshal = null;
    }
    
    public void clearGeneral() {
        this.m_general = null;
    }
    
    public void clearDiplomacyCache() {
        this.m_alignments = null;
    }
    
    public void clearDiplomacy() {
        this.m_nationDiplomacyList = null;
        this.m_nationTerritoriesList = null;
    }
    
    public void updateUI() {
        this.updateCandidateInfoUI();
        this.updateVIPList();
        this.updateGovernor();
        this.updateGeneral();
        this.updateMarshal();
        this.updateLawsViewPoints();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, NationDisplayer.FIELDS);
    }
    
    public CandidateInfoFieldProvider getCandidateInfoFieldProvider(final long citizenId) {
        final TIntObjectIterator<ArrayList<CandidateInfoFieldProvider>> it = this.m_candidateInfosByOffset.iterator();
        while (it.hasNext()) {
            it.advance();
            for (final CandidateInfoFieldProvider cifp : it.value()) {
                if (cifp.getCandidateInfo().getId() == citizenId) {
                    return cifp;
                }
            }
        }
        return null;
    }
    
    public Nation getNation() {
        return this.m_nation;
    }
    
    public GovernorHonorificTitleView getHonorificTitleView(final short id) {
        return this.m_governorTitles.get(id);
    }
    
    public void setTemporarySpeech(final String text) {
        this.m_temporarySpeech = text;
    }
    
    public String getTemporarySpeech() {
        return this.m_temporarySpeech;
    }
    
    public GovernorInfoFieldProvider getGovernor() {
        return this.m_governor;
    }
    
    public GovernmentMemberInfoFieldProvider getMarshal() {
        return this.m_marshal;
    }
    
    public GovernmentMemberInfoFieldProvider getGeneral() {
        return this.m_general;
    }
    
    public boolean isPreventiveElection() {
        final GameDate now = GameDate.fromLong(System.currentTimeMillis());
        final GameDate endDate = new GameDate(this.m_nation.getVoteStartDate());
        return now.before(endDate);
    }
    
    public void updateGovernor() {
        if (this.m_governor == null) {
            return;
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_governor, this.m_governor.getFields());
    }
    
    public TLongHashSet getActivatedLaws() {
        final TLongHashSet set = new TLongHashSet();
        if (this.m_lawsView == null) {
            return set;
        }
        for (final LawView lawView : this.m_lawsView.m_laws) {
            if (lawView.isGovernorActivated()) {
                set.add(lawView.getLaw().getId());
            }
        }
        for (final LawView lawView : this.m_lawsView.m_rights) {
            if (lawView.isGovernorActivated()) {
                set.add(lawView.getLaw().getId());
            }
        }
        return set;
    }
    
    public void cleanAllLaws() {
        if (this.m_lawsView == null) {
            return;
        }
        for (final LawView lawView : this.m_lawsView.m_laws) {
            lawView.clean();
        }
    }
    
    static {
        m_instance = new NationDisplayer();
        m_logger = Logger.getLogger((Class)NationDisplayer.class);
        FIELDS = new String[] { "name", "candidates", "government", "governor", "marshal", "general", "governorHistory", "isVoteRunning", "candidateRegistrationFee", "nationColor", "flagIconUrl", "electionClosureDescription", "electionTotalVotes", "electionTotalCandidates", "governorIsLocalPlayer", "marshalIsLocalPlayer", "generalIsLocalPlayer", "governorTitles", "timeBeforeMandateEnd", "timeBeforeMandateEndPerc", "mandateStartDate", "mandateEndDate", "localPlayerHasRankToChangeTaxes", "localPlayerHasRankToBuyChallenges", "localPlayerHasRankToBuyClimateBonuses", "localPlayerHasRankToBuyZoology", "localPlayerHasRankToChangeLaws", "localPlayerHasRankToChangeDiplomacy", "protectorList", "protectorTotalWillsSize", "protectorSatisfiedWillsSize", "economy", "laws", "nationDiplomacyList", "sortedNationTerritoriesList", "otherNationsLaws", "currentGovernorOpinionId", "currentGovernorOpinionText", "currentGovernorOpinionValue", "opinionDirty", "cantGiveOpinionReason" };
    }
    
    public class LawsView extends ImmutableFieldProvider
    {
        public static final String LAWS = "laws";
        public static final String RIGHTS = "rights";
        public static final String CURRENT_LAW_POINTS = "currentLawPoints";
        public static final String NATION = "nation";
        public final String[] FIELDS;
        private ArrayList<LawView> m_laws;
        private ArrayList<LawView> m_rights;
        private Nation m_localNation;
        
        public LawsView(final Nation nation) {
            super();
            this.FIELDS = new String[] { "laws", "rights", "currentLawPoints", "nation" };
            this.m_localNation = nation;
        }
        
        private ArrayList<LawView> getLaws() {
            if (this.m_laws == null) {
                this.m_laws = new ArrayList<LawView>();
                final NationLawsManager manager = this.m_localNation.getLawManager();
                final TLongObjectIterator<NationLaw> it = manager.lawsIterator();
                while (it.hasNext()) {
                    it.advance();
                    final NationLaw law = it.value();
                    final boolean isActive = manager.isActive(law.getId());
                    if (law.getBasePointsModification() < 0 && (it.value().isLocked() || isActive || (NationDisplayer.this.localPlayerHasRankToChangeLaws() && this.m_localNation == NationDisplayer.getInstance().getNation()))) {
                        this.m_laws.add(new LawView(law));
                    }
                }
                Collections.sort(this.m_laws);
            }
            return this.m_laws;
        }
        
        private ArrayList<LawView> getRights() {
            if (this.m_rights == null) {
                this.m_rights = new ArrayList<LawView>();
                final NationLawsManager manager = this.m_localNation.getLawManager();
                final TLongObjectIterator<NationLaw> it = manager.lawsIterator();
                while (it.hasNext()) {
                    it.advance();
                    final boolean isActive = manager.isActive(it.value().getId());
                    if (it.value().getBasePointsModification() > 0 && (it.value().isLocked() || isActive || (NationDisplayer.this.localPlayerHasRankToChangeLaws() && this.m_localNation == NationDisplayer.getInstance().getNation()))) {
                        this.m_rights.add(new LawView(it.value()));
                    }
                }
                Collections.sort(this.m_rights);
                Collections.reverse(this.m_rights);
            }
            return this.m_rights;
        }
        
        @Override
        public String[] getFields() {
            return this.FIELDS;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("laws")) {
                return this.getLaws();
            }
            if (fieldName.equals("rights")) {
                return this.getRights();
            }
            if (fieldName.equals("currentLawPoints")) {
                return 50 - this.getCurrentLawPoints();
            }
            if (fieldName.equals("nation")) {
                return new NationFieldProvider(this.m_localNation.getNationId());
            }
            return null;
        }
        
        private int getCurrentLawPoints() {
            int total = 0;
            if (NationDisplayer.this.m_lawsView == null) {
                return total;
            }
            for (final LawView lawView : NationDisplayer.this.m_lawsView.m_laws) {
                if (lawView.isGovernorActivated()) {
                    total += lawView.getLaw().getLawPointCost();
                }
            }
            for (final LawView lawView : NationDisplayer.this.m_lawsView.m_rights) {
                if (lawView.isGovernorActivated()) {
                    total += lawView.getLaw().getLawPointCost();
                }
            }
            return total;
        }
        
        public Nation getLocalNation() {
            return this.m_localNation;
        }
    }
    
    private static class EconomyView extends ImmutableFieldProvider
    {
        public static final String NATIONAL_FUNDS = "nationalFunds";
        public static final String COLLECTED_TAXES = "collectedTaxes";
        public static final String TOTAL_COST = "totalCost";
        public static final String CHALLENGE_COST = "challengeCost";
        public static final String CLIMATE_COST = "climateCost";
        public static final String ANIMAL_COST = "animalCost";
        private NationEconomyHandler m_economyHandler;
        
        public EconomyView(final NationEconomyHandler economyHandler) {
            super();
            this.m_economyHandler = economyHandler;
        }
        
        @Override
        public String[] getFields() {
            return new String[0];
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("nationalFunds")) {
                return NumberFormat.getIntegerInstance().format(this.m_economyHandler.getTotalCash()) + " §";
            }
            if (fieldName.equals("collectedTaxes")) {
                int value = 0;
                final TByteIntIterator it = this.m_economyHandler.getAccumulatedTaxes().iterator();
                while (it.hasNext()) {
                    it.advance();
                    value += it.value();
                }
                return NumberFormat.getIntegerInstance().format(value) + " §";
            }
            if (fieldName.equals("totalCost")) {
                int value = 0;
                final TByteIntIterator it = this.m_economyHandler.getSpentCash().iterator();
                while (it.hasNext()) {
                    it.advance();
                    value += it.value();
                }
                return NumberFormat.getIntegerInstance().format(value) + " §";
            }
            if (fieldName.equals("challengeCost")) {
                final int value = this.m_economyHandler.getSpentCash().get(ProtectorMerchantItemType.CHALLENGE.getTypeId());
                return NumberFormat.getIntegerInstance().format(value) + " §";
            }
            if (fieldName.equals("climateCost")) {
                final int value = this.m_economyHandler.getSpentCash().get(ProtectorMerchantItemType.CLIMATE_BUFF.getTypeId());
                return NumberFormat.getIntegerInstance().format(value) + " §";
            }
            if (fieldName.equals("animalCost")) {
                final int value = this.m_economyHandler.getSpentCash().get(ProtectorMerchantItemType.BUFF.getTypeId());
                return NumberFormat.getIntegerInstance().format(value) + " §";
            }
            return null;
        }
    }
}
