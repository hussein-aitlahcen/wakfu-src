package com.ankamagames.wakfu.common.game.guild.change;

import com.ankamagames.framework.kernel.core.common.*;
import org.jetbrains.annotations.*;

public enum GuildChangeType implements SimpleObjectFactory<GuildChange>
{
    ADD_MEMBER {
        @Override
        public GuildChange createNew() {
            return new AddMemberChange();
        }
    }, 
    REMOVE_MEMBER {
        @Override
        public GuildChange createNew() {
            return new RemoveMemberChange();
        }
    }, 
    MODIFY_MEMBER {
        @Override
        public GuildChange createNew() {
            return new ModifyMemberChange();
        }
    }, 
    ADD_BONUS {
        @Override
        public GuildChange createNew() {
            return new AddBonusChange();
        }
    }, 
    REMOVE_BONUS {
        @Override
        public GuildChange createNew() {
            return new RemoveBonusChange();
        }
    }, 
    MODIFY_BONUS {
        @Override
        public GuildChange createNew() {
            return new ModifyBonusChange();
        }
    }, 
    ADD_RANK {
        @Override
        public GuildChange createNew() {
            return new AddRankChange();
        }
    }, 
    REMOVE_RANK {
        @Override
        public GuildChange createNew() {
            return new RemoveRankChange();
        }
    }, 
    MODIFY_RANK {
        @Override
        public GuildChange createNew() {
            return new ModifyRankChange();
        }
    }, 
    MOVE_RANK {
        @Override
        public GuildChange createNew() {
            return new MoveRankChange();
        }
    }, 
    GUILD_POINTS {
        @Override
        public GuildChange createNew() {
            return new GuildPointsChange();
        }
    }, 
    GUILD_LEVEL {
        @Override
        public GuildChange createNew() {
            return new GuildLevelChange();
        }
    }, 
    DESCRIPTION {
        @Override
        public GuildChange createNew() {
            return new DescriptionChange();
        }
    }, 
    MESSAGE {
        @Override
        public GuildChange createNew() {
            return new MessageChange();
        }
    }, 
    NATION {
        @Override
        public GuildChange createNew() {
            return new NationChange();
        }
    };
    
    public final byte idx;
    
    private GuildChangeType(final int ordinal) {
        this.idx = (byte)this.ordinal();
    }
    
    @Nullable
    public static GuildChangeType fromId(final byte id) {
        final GuildChangeType[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final GuildChangeType type = values[i];
            if (type.idx == id) {
                return type;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "PetChangeType{idx=" + this.idx + '}';
    }
}
