package com.ankamagames.wakfu.client.core.dungeon;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.dungeon.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.dungeon.loader.*;

public class DungeonLadderView extends ImmutableFieldProvider
{
    public static final String TITLE_FIELD = "title";
    public static final String LEVEL_DESC_FIELD = "levelDesc";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String TYPE_FIELD = "type";
    public static final String BUFFS_FIELD = "buffs";
    public static final String FIRST_GROUP_FIELD = "firstGroup";
    public static final String SECOND_GROUP_FIELD = "secondGroup";
    public static final String THIRD_GROUP_FIELD = "thirdGroup";
    public static final String[] FIELDS;
    private DungeonLadder m_dungeonLadder;
    private static final int GROUP_MAX_COUNT = 3;
    private final int m_levelMax;
    private AbstractDungeonLadderResultView m_firstDungeonLadderResultView;
    private AbstractDungeonLadderResultView m_secondDungeonLadderResultView;
    private AbstractDungeonLadderResultView m_thirdDungeonLadderResultView;
    
    public DungeonLadderView(final DungeonLadder dungeonLadder) {
        super();
        this.m_firstDungeonLadderResultView = null;
        this.m_secondDungeonLadderResultView = null;
        this.m_thirdDungeonLadderResultView = null;
        this.m_dungeonLadder = dungeonLadder;
        final ArrayList<DungeonLadderResult> ladderResults = this.m_dungeonLadder.getResults();
        this.m_firstDungeonLadderResultView = ((ladderResults.size() > 0) ? new DungeonLadderResultView(ladderResults.get(0)) : new EmptyDungeonLadderResultView());
        this.m_secondDungeonLadderResultView = ((ladderResults.size() > 1) ? new DungeonLadderResultView(ladderResults.get(1)) : new EmptyDungeonLadderResultView());
        this.m_thirdDungeonLadderResultView = ((ladderResults.size() > 2) ? new DungeonLadderResultView(ladderResults.get(2)) : new EmptyDungeonLadderResultView());
        this.m_levelMax = dungeonLadder.getDefinition().getMaxDungeonLevel();
    }
    
    @Override
    public String[] getFields() {
        return DungeonLadderView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("title")) {
            final DungeonDefinition dungeonFromWorld = ArcadeDungeonLoader.INSTANCE.getDungeonFromWorld(this.m_dungeonLadder.getDefinition().getInstanceId());
            return dungeonFromWorld.getName();
        }
        if (fieldName.equals("description")) {
            final DungeonDefinition dungeonFromWorld = ArcadeDungeonLoader.INSTANCE.getDungeonFromWorld(this.m_dungeonLadder.getDefinition().getInstanceId());
            return dungeonFromWorld.getDescription();
        }
        if (fieldName.equals("levelDesc")) {
            return this.m_levelMax;
        }
        if (fieldName.equals("type")) {
            return this.m_dungeonLadder.getDefinition().getLadderType().ordinal();
        }
        if (fieldName.equals("buffs")) {
            return null;
        }
        if (fieldName.equals("firstGroup")) {
            return this.m_firstDungeonLadderResultView;
        }
        if (fieldName.equals("secondGroup")) {
            return this.m_secondDungeonLadderResultView;
        }
        if (fieldName.equals("thirdGroup")) {
            return this.m_thirdDungeonLadderResultView;
        }
        return null;
    }
    
    public DungeonLadder getDungeonLadder() {
        return this.m_dungeonLadder;
    }
    
    static {
        FIELDS = new String[] { "title", "levelDesc", "description", "type", "buffs", "firstGroup", "secondGroup", "thirdGroup" };
    }
}
