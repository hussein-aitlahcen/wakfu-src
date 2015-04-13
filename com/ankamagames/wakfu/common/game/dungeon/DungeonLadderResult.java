package com.ankamagames.wakfu.common.game.dungeon;

import java.util.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import org.jetbrains.annotations.*;

public class DungeonLadderResult
{
    private long m_uid;
    private final int m_dungeonId;
    private final GameDateConst m_date;
    private final GameInterval m_duration;
    private final int m_monsterCount;
    private final int m_challengeCount;
    private final int m_eventCount;
    private final long m_score;
    private final ArrayList<DungeonLadderResultCharacter> m_characters;
    
    public DungeonLadderResult(final long uid, final int dungeonId, final GameDateConst date, final GameInterval duration, final int monsterCount, final int challengeCount, final int eventCount, final long score) {
        super();
        this.m_characters = new ArrayList<DungeonLadderResultCharacter>();
        this.m_uid = uid;
        this.m_dungeonId = dungeonId;
        this.m_date = date;
        this.m_duration = duration;
        this.m_monsterCount = monsterCount;
        this.m_challengeCount = challengeCount;
        this.m_eventCount = eventCount;
        this.m_score = score;
    }
    
    public int getDungeonId() {
        return this.m_dungeonId;
    }
    
    public GameDateConst getDate() {
        return this.m_date;
    }
    
    public GameInterval getDuration() {
        return this.m_duration;
    }
    
    public int getMonsterCount() {
        return this.m_monsterCount;
    }
    
    public int getChallengeCount() {
        return this.m_challengeCount;
    }
    
    public int getEventCount() {
        return this.m_eventCount;
    }
    
    public long getScore() {
        return this.m_score;
    }
    
    public void addCharacter(final DungeonLadderResultCharacter character) {
        this.m_characters.add(character);
    }
    
    public ArrayList<DungeonLadderResultCharacter> getCharacters() {
        return this.m_characters;
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_uid);
        buffer.putInt(this.m_dungeonId);
        buffer.putLong(this.m_date.toLong());
        buffer.putLong(this.m_duration.toSeconds());
        buffer.putInt(this.m_monsterCount);
        buffer.putInt(this.m_challengeCount);
        buffer.putInt(this.m_eventCount);
        buffer.putLong(this.m_score);
        buffer.putShort((short)this.m_characters.size());
        for (int i = 0; i < this.m_characters.size(); ++i) {
            this.m_characters.get(i).serialize(buffer);
        }
    }
    
    public static DungeonLadderResult fromBuild(final ByteBuffer buffer) {
        final long uid = buffer.getLong();
        final int dungeonId = buffer.getInt();
        final long date = buffer.getLong();
        final long duration = buffer.getLong();
        final int monsterCount = buffer.getInt();
        final int challengeCount = buffer.getInt();
        final int eventCount = buffer.getInt();
        final long score = buffer.getLong();
        final DungeonLadderResult result = new DungeonLadderResult(uid, dungeonId, GameDate.fromLong(date), GameInterval.fromSeconds(duration), monsterCount, challengeCount, eventCount, score);
        final short charactersCount = buffer.getShort();
        for (int i = 0; i < charactersCount; ++i) {
            result.addCharacter(DungeonLadderResultCharacter.fromBuild(buffer));
        }
        return result;
    }
    
    public int serializedSize() {
        int characterSize = 0;
        for (int i = 0; i < this.m_characters.size(); ++i) {
            characterSize += this.m_characters.get(i).serializedSize();
        }
        return 50 + characterSize;
    }
    
    @Nullable
    public DungeonLadderResultCharacter getCharacter(final int index) {
        if (index >= this.m_characters.size()) {
            return null;
        }
        return this.m_characters.get(index);
    }
    
    public long getUid() {
        return this.m_uid;
    }
    
    public void setUid(final long uid) {
        this.m_uid = uid;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("date=").append(this.m_date).append(", score=").append(this.m_score).append(", charactersCount=").append(this.m_characters.size());
        return sb.toString();
    }
}
