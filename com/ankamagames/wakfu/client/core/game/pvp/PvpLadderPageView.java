package com.ankamagames.wakfu.client.core.game.pvp;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.pvp.filter.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.pvp.*;
import com.ankamagames.wakfu.client.core.*;

public class PvpLadderPageView extends ImmutableFieldProvider
{
    public static final int NUM_ENTRIES_PER_PAGE = 10;
    public static final PvpLadderPageView INSTANCE;
    public static final String ENTRIES = "entries";
    public static final String PAGE_DESCRIPTION = "pageDescription";
    public static final String CAN_GO_BACK = "canGoBack";
    public static final String CAN_GO_FORWARD = "canGoForward";
    public static final String CURRENT_NATION = "currentNation";
    public static final String NATIONS = "nations";
    public static final String CURRENT_BREED = "currentBreed";
    public static final String BREEDS = "breeds";
    public static final String CURRENT_FILTER = "currentFilter";
    public static final String FILTERS = "filters";
    private final ArrayList<PvpLadderEntryView> m_views;
    private int m_currentPage;
    private int m_totalPages;
    private final ArrayList<NationFieldProvider> m_nations;
    private final ArrayList<BreedInfo> m_breeds;
    private final ArrayList<FilterType> m_filters;
    private NationFieldProvider m_currentNation;
    private BreedInfo m_currentBreed;
    private FilterType m_currentFilter;
    private NationPvpLadderFilterParam m_param;
    
    private PvpLadderPageView() {
        super();
        this.m_views = new ArrayList<PvpLadderEntryView>();
        this.m_nations = new ArrayList<NationFieldProvider>();
        this.m_breeds = new ArrayList<BreedInfo>();
        this.m_filters = new ArrayList<FilterType>();
        this.m_nations.add(new NationFieldProvider(30));
        this.m_nations.add(new NationFieldProvider(31));
        this.m_nations.add(new NationFieldProvider(32));
        this.m_nations.add(new NationFieldProvider(33));
        this.m_nations.add(new NationFieldProvider(34));
        this.m_breeds.add(new BreedInfo(AvatarBreed.NONE));
        for (final AvatarBreed b : AvatarBreed.values()) {
            if (b != AvatarBreed.SOUL) {
                if (AvatarBreedConstants.isBreedEnabled(b)) {
                    this.m_breeds.add(new BreedInfo(b));
                }
            }
        }
        this.m_filters.add(new FilterType(true));
        this.m_filters.add(new FilterType(false));
    }
    
    @Override
    public String[] getFields() {
        return PvpLadderPageView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("entries")) {
            return this.m_views;
        }
        if (fieldName.equals("pageDescription")) {
            return this.getPageDescription();
        }
        if (fieldName.equals("canGoBack")) {
            return this.canGoBack();
        }
        if (fieldName.equals("canGoForward")) {
            return this.canGoForward();
        }
        if (fieldName.equals("breeds")) {
            return this.m_breeds;
        }
        if (fieldName.equals("currentBreed")) {
            return this.m_currentBreed;
        }
        if (fieldName.equals("nations")) {
            return this.m_nations;
        }
        if (fieldName.equals("currentNation")) {
            return this.m_currentNation;
        }
        if (fieldName.equals("filters")) {
            return this.m_filters;
        }
        if (fieldName.equals("currentFilter")) {
            return this.m_currentFilter;
        }
        return null;
    }
    
    private String getPageDescription() {
        final int page = Math.max(0, this.m_currentPage);
        return page + 1 + "/" + this.m_totalPages;
    }
    
    public void reset() {
        this.reset(true);
    }
    
    public void reset(final boolean full) {
        this.resetToNation(WakfuGameEntity.getInstance().getLocalPlayer().getNationId(), full);
    }
    
    private void resetToNation(final int nationId, final boolean full) {
        for (int i = 0, size = this.m_nations.size(); i < size; ++i) {
            final NationFieldProvider nation = this.m_nations.get(i);
            if (nation.getNationId() == nationId) {
                this.m_currentNation = nation;
                break;
            }
        }
        this.m_currentFilter = this.m_filters.get(0);
        this.m_currentPage = -1;
        this.m_totalPages = 1;
        if (full) {
            this.m_currentBreed = this.m_breeds.get(0);
            this.m_param = new AllEntries(this.m_currentPage, 10);
        }
        else if (this.m_param == null || this.m_param.getType() != FilterParamType.BREED) {
            this.m_param = new AllEntries(this.m_currentPage, 10);
        }
        this.m_views.clear();
        this.sendRequest();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "entries", "canGoBack", "canGoForward", "pageDescription", "currentFilter", "currentBreed", "currentNation");
    }
    
    public void setEntries(final int pageNum, final int totalPages, final List<PvpLadderEntry> entries) {
        this.m_currentPage = MathHelper.clamp(pageNum, 0, totalPages - 1);
        this.m_totalPages = totalPages;
        this.m_views.clear();
        for (int i = 0, size = entries.size(); i < size; ++i) {
            this.m_views.add(PvpLadderEntryView.getOrCreate(entries.get(i), false));
        }
        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("nationPvpLadderLock", true);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "entries", "canGoBack", "canGoForward", "pageDescription");
    }
    
    private boolean canGoBack() {
        return this.m_currentPage > 0;
    }
    
    private boolean canGoForward() {
        return this.m_currentPage + 1 < this.m_totalPages;
    }
    
    public void searchByName(final String name) {
        final Message msg = new NationPvpLadderEntryByNameRequest(name);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    public void setCurrentNation(final NationFieldProvider nation) {
        if (this.m_currentNation == nation) {
            return;
        }
        this.resetToNation(nation.getNationId(), false);
    }
    
    public void setCurrentBreed(final BreedInfo info) {
        if (this.m_currentBreed == info) {
            return;
        }
        this.m_currentBreed = info;
        this.m_currentPage = 0;
        if (info == this.m_breeds.get(0)) {
            this.allFilter();
        }
        else {
            this.breedFilter(info.getBreed().getBreedId());
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentBreed");
    }
    
    public void setCurrentFilter(final FilterType type) {
        if (this.m_currentFilter == type) {
            return;
        }
        this.m_currentFilter = type;
        if (type.m_all) {
            this.allFilter();
        }
        else {
            this.guildFilter(WakfuGameEntity.getInstance().getLocalPlayer().getGuildId());
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentFilter");
    }
    
    public void allFilter() {
        if (this.m_param.getType() == FilterParamType.ALL) {
            return;
        }
        this.m_currentPage = -1;
        this.m_param = new AllEntries(this.m_currentPage, 10);
        this.sendRequest();
    }
    
    public void guildFilter(final long guildId) {
        if (this.m_param.getType() == FilterParamType.GUILD) {
            final EntriesByGuild param = (EntriesByGuild)this.m_param;
            if (param.getGuildId() == guildId) {
                return;
            }
        }
        this.m_currentPage = -1;
        this.m_param = new EntriesByGuild(this.m_currentPage, 10, guildId);
        this.sendRequest();
    }
    
    public void breedFilter(final short breedId) {
        if (this.m_param.getType() == FilterParamType.BREED) {
            final EntriesByBreed param = (EntriesByBreed)this.m_param;
            if (param.getBreedId() == breedId) {
                return;
            }
        }
        this.m_currentPage = -1;
        this.m_param = new EntriesByBreed(this.m_currentPage, 10, breedId);
        this.sendRequest();
    }
    
    public void first() {
        this.m_currentPage = 0;
        this.sendRequest();
    }
    
    public void previous() {
        if (!this.canGoBack()) {
            return;
        }
        --this.m_currentPage;
        this.sendRequest();
    }
    
    public void next() {
        if (!this.canGoForward()) {
            return;
        }
        ++this.m_currentPage;
        this.sendRequest();
    }
    
    public void last() {
        this.m_currentPage = this.m_totalPages - 1;
        this.sendRequest();
    }
    
    private void sendRequest() {
        this.m_param.setPageNum(this.m_currentPage);
        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("nationPvpLadderLock", false);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new NationPvpLadderPageRequest(this.m_currentNation.getNationId(), this.m_param));
    }
    
    static {
        INSTANCE = new PvpLadderPageView();
    }
    
    public static class FilterType extends ImmutableFieldProvider
    {
        public static final String NAME = "name";
        public static final String ENABLED = "enabled";
        public static final String VALUE = "value";
        public static final String STYLE = "style";
        private final boolean m_all;
        
        public FilterType(final boolean all) {
            super();
            this.m_all = all;
        }
        
        @Override
        public String[] getFields() {
            return FilterType.NO_FIELDS;
        }
        
        public boolean isAll() {
            return this.m_all;
        }
        
        @Nullable
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("name")) {
                return this.m_all ? WakfuTranslator.getInstance().getString("pvp.ladder.filter.all") : WakfuTranslator.getInstance().getString("pvp.ladder.filter.myGuild");
            }
            if (fieldName.equals("enabled")) {
                return this.m_all || WakfuGameEntity.getInstance().getLocalPlayer().getGuildId() > 0L;
            }
            if (fieldName.equals("value")) {
                return this.m_all ? 0 : 1;
            }
            if (fieldName.equals("style")) {
                return this.m_all ? "singleCharacterIcon" : "guild";
            }
            return null;
        }
    }
    
    public static class BreedInfo extends ImmutableFieldProvider
    {
        public static final String NAME = "name";
        private final AvatarBreed m_breed;
        
        protected BreedInfo(final AvatarBreed breed) {
            super();
            this.m_breed = breed;
        }
        
        @Override
        public String[] getFields() {
            return BreedInfo.NO_FIELDS;
        }
        
        public AvatarBreed getBreed() {
            return this.m_breed;
        }
        
        @Nullable
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("name")) {
                return AvatarBreedConstants.isBreedEnabled(this.m_breed) ? WakfuTranslator.getInstance().getString("breed." + this.m_breed.getBreedId()) : WakfuTranslator.getInstance().getString("allBreeds");
            }
            return null;
        }
    }
}
