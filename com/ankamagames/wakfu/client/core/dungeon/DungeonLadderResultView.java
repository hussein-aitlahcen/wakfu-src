package com.ankamagames.wakfu.client.core.dungeon;

import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.dungeon.*;

public class DungeonLadderResultView extends AbstractDungeonLadderResultView
{
    public static final String RESULT_TEXT_FIELD = "resultText";
    public static final String DATE_FIELD = "date";
    public static final String NATION_ICON_URL_FIELD = "nationIconUrl";
    public static final String NATION_NAME_FIELD = "nationName";
    public static final String PARTY_MEMBERS_FIELD = "partyMembers";
    public static final String[] FIELDS;
    private DungeonLadderResult m_dungeonLadderResult;
    private final DungeonLadderResultCharacterView[] m_dungeonLadderResultCharacterViews;
    
    public DungeonLadderResultView(final DungeonLadderResult dungeonLadderResult) {
        super();
        this.m_dungeonLadderResultCharacterViews = new DungeonLadderResultCharacterView[6];
        this.m_dungeonLadderResult = dungeonLadderResult;
        final ArrayList<DungeonLadderResultCharacter> characters = this.m_dungeonLadderResult.getCharacters();
        for (int i = 0; i < this.m_dungeonLadderResultCharacterViews.length; ++i) {
            final DungeonLadderResultCharacter character = (i < characters.size()) ? characters.get(i) : null;
            this.m_dungeonLadderResultCharacterViews[i] = ((character == null) ? null : new DungeonLadderResultCharacterView(character));
        }
    }
    
    @Override
    public String[] getFields() {
        return DungeonLadderResultView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("resultText")) {
            final DungeonLadderDefinition definition = UIDungeonLadderFrame.getInstance().getDungeonLadderView().getDungeonLadder().getDefinition();
            switch (definition.getLadderType()) {
                case SURVIVAL: {
                    return this.m_dungeonLadderResult.getScore();
                }
                case TIME_ATTACK: {
                    return UIDungeonLadderFrame.getChronoString((int)(this.m_dungeonLadderResult.getScore() / 1000L));
                }
            }
        }
        else {
            if (fieldName.equals("date")) {
                final GameDateConst date = this.m_dungeonLadderResult.getDate();
                return WakfuTranslator.getInstance().formatDateShort(date);
            }
            if (fieldName.equals("nationIconUrl")) {
                final DungeonLadderResultCharacterView characterView = this.m_dungeonLadderResultCharacterViews[0];
                final int nationId = (characterView == null) ? 0 : characterView.getDungeonLadderResultCharacter().getNationId();
                return WakfuConfiguration.getInstance().getFlagIconUrl((nationId != 0) ? nationId : -1);
            }
            if (fieldName.equals("nationName")) {
                final DungeonLadderResultCharacterView characterView = this.m_dungeonLadderResultCharacterViews[0];
                final int nationId = (characterView == null) ? 0 : characterView.getDungeonLadderResultCharacter().getNationId();
                return WakfuTranslator.getInstance().getString(39, nationId, new Object[0]);
            }
            if (fieldName.equals("partyMembers")) {
                return this.m_dungeonLadderResultCharacterViews;
            }
        }
        return null;
    }
    
    static {
        FIELDS = new String[] { "resultText", "date", "nationIconUrl", "nationName", "partyMembers" };
    }
    
    private static class DungeonLadderResultCharacterView extends ImmutableFieldProvider
    {
        public static final String NAME_FIELD = "name";
        public static final String ANIM_FIELD = "anim";
        public static final String GUILD_NAME_FIELD = "guildName";
        public static final String LEVEL_FIELD = "level";
        public final String[] FIELDS;
        private DungeonLadderResultCharacter m_dungeonLadderResultCharacter;
        private CharacterActor m_characterActor;
        
        public DungeonLadderResultCharacterView(final DungeonLadderResultCharacter dungeonLadderResultCharacter) {
            super();
            this.FIELDS = new String[] { "name", "anim", "guildName", "level" };
            this.m_dungeonLadderResultCharacter = dungeonLadderResultCharacter;
        }
        
        @Override
        public String[] getFields() {
            return this.FIELDS;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("name")) {
                return this.m_dungeonLadderResultCharacter.getName();
            }
            if (fieldName.equals("anim")) {
                if (this.m_characterActor == null) {
                    this.m_characterActor = ActorUtils.getActorFromCharacterData(this.m_dungeonLadderResultCharacter);
                }
                return this.m_characterActor;
            }
            if (fieldName.equals("guildName")) {
                return this.m_dungeonLadderResultCharacter.getGuildName();
            }
            if (fieldName.equals("level")) {
                return "(" + WakfuTranslator.getInstance().getString("levelShort.custom", this.m_dungeonLadderResultCharacter.getLevel()) + ")";
            }
            return null;
        }
        
        public DungeonLadderResultCharacter getDungeonLadderResultCharacter() {
            return this.m_dungeonLadderResultCharacter;
        }
    }
}
