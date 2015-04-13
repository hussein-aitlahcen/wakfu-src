package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import java.util.*;

public class GuildRankView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String POSITION_FIELD = "position";
    public static final String AUTHORISATIONS_FIELD = "authorisations";
    public static final String CAN_BE_EDITED_FIELD = "canBeEdited";
    public static final String CAN_BE_DELETED_FIELD = "canBeDeleted";
    public static final String ID_FIELD = "id";
    public static final String MODULATION_COLOR_FIELD = "modulationColor";
    public static final String[] FIELDS;
    private final GuildAuthorisationView[] m_authorisations;
    private final GuildRank m_rank;
    
    public GuildRankView(final GuildRank rank) {
        super();
        this.m_rank = rank;
        final GuildRankAuthorisation[] values = GuildRankAuthorisation.values();
        this.m_authorisations = new GuildAuthorisationView[values.length];
        for (int i = 0; i < this.m_authorisations.length; ++i) {
            this.m_authorisations[i] = new GuildAuthorisationView(values[i]);
        }
        for (final GuildAuthorisationView guildAuthorisationView : this.m_authorisations) {
            final boolean checked = this.m_rank.hasAuthorisation(guildAuthorisationView.getRankAuthorisation());
            guildAuthorisationView.setChecked(checked);
        }
    }
    
    @Override
    public String[] getFields() {
        return GuildRankView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("position")) {
            return RomanUtils.IntToRoman(this.m_rank.getPosition() + 1);
        }
        if (fieldName.equals("canBeEdited")) {
            return this.canBeEditedByLocalPlayer();
        }
        if (fieldName.equals("canBeDeleted")) {
            return this.canBeDeletedByLocalPlayer();
        }
        if (fieldName.equals("id")) {
            return this.m_rank.getId();
        }
        if (fieldName.equals("authorisations")) {
            return this.m_authorisations;
        }
        if (fieldName.equals("modulationColor")) {
            return this.getModulationColor();
        }
        return null;
    }
    
    public Color getModulationColor() {
        final GuildRankView overGuildRankView = (GuildRankView)PropertiesProvider.getInstance().getObjectProperty("overGuildRank");
        if (overGuildRankView != null && overGuildRankView.getId() == this.getId()) {
            return Color.WHITE_SEMI_ALPHA;
        }
        final GuildRankView draggedGuildRankView = (GuildRankView)PropertiesProvider.getInstance().getObjectProperty("draggedGuildRank");
        if (draggedGuildRankView != null && draggedGuildRankView.getId() == this.getId()) {
            return Color.WHITE_QUARTER_ALPHA;
        }
        final GuildRankView selectedGuildRankView = (GuildRankView)PropertiesProvider.getInstance().getObjectProperty("selectedGuildRank");
        if (selectedGuildRankView != null && selectedGuildRankView.getId() == this.getId()) {
            return Color.WHITE;
        }
        return new Color(1.0f, 1.0f, 1.0f, 0.7f);
    }
    
    public boolean canBeDeletedByLocalPlayer() {
        if (!this.canBeEditedByLocalPlayer()) {
            return false;
        }
        final long bestRank = WakfuGuildView.getInstance().getBestRank();
        final long worstRank = WakfuGuildView.getInstance().getWorstRank();
        return this.m_rank.getId() != bestRank && this.m_rank.getId() != worstRank;
    }
    
    public boolean canBeEditedByLocalPlayer() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
        final GuildRank sourceRank = guildHandler.getRank(guildHandler.getMember(localPlayer.getId()).getRank());
        return sourceRank.hasAuthorisation(GuildRankAuthorisation.EDIT_RANK, this.getRank().getPosition());
    }
    
    public String getName() {
        return this.m_rank.getName();
    }
    
    public long getModifiedAuthorisationLong() {
        final ArrayList<GuildRankAuthorisation> result = new ArrayList<GuildRankAuthorisation>();
        for (final GuildAuthorisationView guildAuthorisationView : this.m_authorisations) {
            if (guildAuthorisationView.isChecked()) {
                result.add(guildAuthorisationView.getRankAuthorisation());
            }
        }
        return GuildRankAuthorisation.longValueOf(result);
    }
    
    public long getAuthorisationsLong() {
        return this.m_rank.getAuthorisations();
    }
    
    public long getId() {
        return this.m_rank.getId();
    }
    
    public GuildRank getRank() {
        return this.m_rank;
    }
    
    public ModifiedGuildRankView getCopy() {
        return new ModifiedGuildRankView(this.m_rank);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof GuildRankView)) {
            return false;
        }
        final GuildRankView rankView = (GuildRankView)obj;
        return rankView.getModifiedAuthorisationLong() == this.getModifiedAuthorisationLong() && rankView.getId() == this.getId() && rankView.getName().equals(this.getName());
    }
    
    static {
        FIELDS = new String[] { "name", "position", "authorisations", "canBeEdited", "canBeDeleted", "id", "modulationColor" };
    }
}
