package com.ankamagames.wakfu.client.core.game.pvp;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.pvp.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class PvpLadderEntryView extends ImmutableFieldProvider
{
    public static final String ANIMATED_ELEMENT = "animatedElement";
    public static final String ANIM_NAME = "animName";
    public static final String NAME = "name";
    public static final String SCORE = "score";
    public static final String RANKING = "ranking";
    public static final String RANK = "rank";
    public static final String SMILEY = "smiley";
    public static final String GUILD_NAME = "guildName";
    public static final String LEVEL = "level";
    public static final String CURRENT_STREAK_DESCRIPTION = "currentStreakDescription";
    public static final String MAX_STREAK_DESCRIPTION = "maxStreakDescription";
    public static final String TOTAL = "totalMatches";
    public static final String MATCH_1V1 = "1v1Matches";
    public static final String MATCH_2V2 = "2v2Matches";
    public static final String MATCH_3V3 = "3v3Matches";
    public static final String MATCH_4V4 = "4v4Matches";
    public static final String MATCH_5V5 = "5v5Matches";
    public static final String MATCH_6V6 = "6v6Matches";
    public static final String ALL_MATCHES = "allMatches";
    public static final String PVP_RANK_ICON_URL = "pvpRankIconUrl";
    public static final String PVP_RANK_PASSPORT_ICON_URL = "pvpRankPassportIconUrl";
    public static final String NATION_ID = "nationId";
    private static final String[] FIELDS;
    private PvpLadderEntry m_entry;
    private AvatarSmileyView m_smiley;
    private ArrayList<PvpMatchView> m_matches;
    private CharacterActor m_actor;
    private static final TLongObjectHashMap<PvpLadderEntryView> KEPT_VIEWS;
    private static final LRUCache<Long, PvpLadderEntryView> VIEWS;
    
    public static void clear() {
        PvpLadderEntryView.VIEWS.clear();
        PvpLadderEntryView.KEPT_VIEWS.clear();
    }
    
    public static PvpLadderEntryView getOrCreate(final Citizen character, final boolean forceUpdate) {
        final PvpLadderEntry entry = new PvpLadderEntry(character.getId(), character.getCitizenComportment().getNationId());
        entry.setName(character.getName());
        entry.setBreedId(character.getBreedId());
        entry.setSex(character.getSex());
        final boolean keep = WakfuGameEntity.getInstance().getLocalPlayer().getId() == entry.getId();
        final PvpLadderEntryView view = get(entry);
        if (view == null) {
            final PvpLadderEntryView createdView = keep ? createKeptView(entry) : createLRUView(entry);
            createdView.requestPvpLadderEntryUpdate();
            return createdView;
        }
        if (forceUpdate) {
            view.requestPvpLadderEntryUpdate();
        }
        return view;
    }
    
    public static PvpLadderEntryView getOrCreate(final PvpLadderEntry entry, final boolean refresh) {
        final boolean keep = WakfuGameEntity.getInstance().getLocalPlayer().getId() == entry.getId();
        final PvpLadderEntryView view = get(entry);
        if (view == null) {
            return keep ? createKeptView(entry) : createLRUView(entry);
        }
        if (refresh) {
            view.setEntryWithoutFire(entry);
            return view;
        }
        view.setEntry(entry);
        return view;
    }
    
    private static PvpLadderEntryView get(final PvpLadderEntry entry) {
        final PvpLadderEntryView view = PvpLadderEntryView.KEPT_VIEWS.get(entry.getId());
        return (view == null) ? PvpLadderEntryView.VIEWS.get(entry.getId()) : view;
    }
    
    private static PvpLadderEntryView createLRUView(final PvpLadderEntry entry) {
        final PvpLadderEntryView view;
        PvpLadderEntryView.VIEWS.put(entry.getId(), view = new PvpLadderEntryView(entry));
        return view;
    }
    
    private static PvpLadderEntryView createKeptView(final PvpLadderEntry entry) {
        final PvpLadderEntryView view;
        PvpLadderEntryView.KEPT_VIEWS.put(entry.getId(), view = new PvpLadderEntryView(entry));
        return view;
    }
    
    private PvpLadderEntryView(final PvpLadderEntry entry) {
        super();
        this.m_matches = new ArrayList<PvpMatchView>();
        this.m_entry = entry;
        this.m_smiley = AvatarSmileyView.getView(entry.getBreedId(), entry.getSex(), SmileyEnum.HAPPY_SMILEYS.getId());
        if (this.m_entry.getAppearance() != null) {
            this.m_actor = ActorUtils.getActorFromCharacterData(this.m_entry);
        }
        for (final MatchType type : MatchType.values()) {
            this.m_matches.add(new PvpMatchView(type, entry));
        }
    }
    
    private void setEntry(final PvpLadderEntry entry) {
        this.m_entry = entry;
        this.m_smiley = AvatarSmileyView.getView(entry.getBreedId(), entry.getSex(), SmileyEnum.HAPPY_SMILEYS.getId());
        if (this.m_entry.getAppearance() != null) {
            this.m_actor = ActorUtils.getActorFromCharacterData(this.m_entry);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, PvpLadderEntryView.FIELDS);
        for (final MatchType type : MatchType.values()) {
            this.m_matches.get(type.getId()).setEntry(entry);
        }
    }
    
    private void setEntryWithoutFire(final PvpLadderEntry entry) {
        this.m_entry = entry;
        this.m_smiley = AvatarSmileyView.getView(entry.getBreedId(), entry.getSex(), SmileyEnum.HAPPY_SMILEYS.getId());
        if (this.m_entry.getAppearance() != null) {
            this.m_actor = ActorUtils.getActorFromCharacterData(this.m_entry);
        }
        for (final MatchType type : MatchType.values()) {
            this.m_matches.get(type.getId()).setEntry(entry);
        }
    }
    
    public PvpLadderEntry getEntry() {
        return this.m_entry;
    }
    
    @Override
    public String[] getFields() {
        return PvpLadderEntryView.FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("pvpRankIconUrl")) {
            final int nationId = this.m_entry.getNationId();
            final NationPvpRanks rank = this.m_entry.getRank();
            return WakfuConfiguration.getInstance().getIconUrl("pvpRankIconsPath", "defaultIconPath", nationId, rank.getId());
        }
        if (fieldName.equals("pvpRankPassportIconUrl")) {
            final int nationId = this.m_entry.getNationId();
            final NationPvpRanks rank = this.m_entry.getRank();
            return WakfuConfiguration.getInstance().getIconUrl("pvpRankPassportIconsPath", "defaultIconPath", nationId, rank.getId());
        }
        if (fieldName.equals("nationId")) {
            return this.m_entry.getNationId();
        }
        if (fieldName.equals("animatedElement")) {
            return this.m_actor;
        }
        if (fieldName.equals("animName")) {
            return "AnimEmote-Victoire";
        }
        if (fieldName.equals("name")) {
            return this.m_entry.getName();
        }
        if (fieldName.equals("score")) {
            return this.m_entry.getCachedStrength();
        }
        if (fieldName.equals("ranking")) {
            final int ranking = this.m_entry.getCachedRanking();
            return (ranking == -1) ? "-" : (ranking + 1);
        }
        if (fieldName.equals("rank")) {
            return WakfuTranslator.getInstance().getString("nation.pvpRank." + this.m_entry.getCachedRank());
        }
        if (fieldName.equals("smiley")) {
            return this.m_smiley;
        }
        if (fieldName.equals("level")) {
            return this.m_entry.getLevel();
        }
        if (fieldName.equals("totalMatches")) {
            return this.m_matches.get(MatchType.TOTAL.getId());
        }
        if (fieldName.equals("1v1Matches")) {
            return this.m_matches.get(MatchType.ONE_VS_ONE.getId());
        }
        if (fieldName.equals("2v2Matches")) {
            return this.m_matches.get(MatchType.TWO_VS_TWO.getId());
        }
        if (fieldName.equals("3v3Matches")) {
            return this.m_matches.get(MatchType.THREE_VS_THREE.getId());
        }
        if (fieldName.equals("4v4Matches")) {
            return this.m_matches.get(MatchType.FOUR_VS_FOUR.getId());
        }
        if (fieldName.equals("5v5Matches")) {
            return this.m_matches.get(MatchType.FIVE_VS_FIVE.getId());
        }
        if (fieldName.equals("6v6Matches")) {
            return this.m_matches.get(MatchType.SIX_VS_SIX.getId());
        }
        if (fieldName.equals("allMatches")) {
            return this.m_matches;
        }
        if (fieldName.equals("currentStreakDescription")) {
            return "x" + this.m_entry.getCurrentStreak();
        }
        if (fieldName.equals("maxStreakDescription")) {
            return "x" + this.m_entry.getMaxStreak();
        }
        return null;
    }
    
    public void requestPvpLadderEntryUpdate() {
        final Message msg = new NationPvpLadderEntryByIdRequest(this.m_entry.getId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    @Override
    public String toString() {
        return "PvpLadderEntryView{m_entry=" + this.m_entry + ", m_smiley=" + this.m_smiley + '}';
    }
    
    static {
        FIELDS = new String[] { "animatedElement", "name", "score", "ranking", "rank", "smiley", "guildName", "level", "currentStreakDescription", "maxStreakDescription", "totalMatches", "1v1Matches", "2v2Matches", "3v3Matches", "4v4Matches", "5v5Matches", "6v6Matches", "allMatches", "nationId", "pvpRankIconUrl", "pvpRankPassportIconUrl" };
        KEPT_VIEWS = new TLongObjectHashMap<PvpLadderEntryView>();
        VIEWS = new LRUCache<Long, PvpLadderEntryView>(50);
    }
}
