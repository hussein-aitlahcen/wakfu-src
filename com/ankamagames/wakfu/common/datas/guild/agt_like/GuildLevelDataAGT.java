package com.ankamagames.wakfu.common.datas.guild.agt_like;

import com.ankamagames.wakfu.common.datas.guild.level.*;

public enum GuildLevelDataAGT
{
    LEVEL_0(new GuildLevel(0, new int[] { 74 })), 
    LEVEL_1(new GuildLevel(1, new int[] { 75, 1 })), 
    LEVEL_2(new GuildLevel(2, new int[] { 76, 2, 3, 4 })), 
    LEVEL_3(new GuildLevel(3, new int[] { 77, 5, 6, 7, 8, 9, 10 })), 
    LEVEL_4(new GuildLevel(4, new int[] { 78, 11, 12, 13, 14, 15, 16, 70 })), 
    LEVEL_5(new GuildLevel(5, new int[] { 79, 17, 18, 19, 20, 21, 22, 23, 24, 25, 84 })), 
    LEVEL_6(new GuildLevel(6, new int[] { 80, 26, 27, 28, 29, 30, 31, 32, 33, 34 })), 
    LEVEL_7(new GuildLevel(7, new int[] { 81, 35, 36, 37, 38, 39, 40, 41, 42, 71 })), 
    LEVEL_8(new GuildLevel(8, new int[] { 82, 43, 44, 45, 46, 47, 48, 49, 50, 51 })), 
    LEVEL_9(new GuildLevel(9, new int[] { 83, 52, 53, 54, 55, 56, 57, 58, 59 })), 
    LEVEL_10(new GuildLevel(10, new int[] { 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 72, 73 }));
    
    private final GuildLevel m_data;
    
    private GuildLevelDataAGT(final GuildLevel data) {
        this.m_data = data;
    }
    
    public GuildLevel get() {
        return this.m_data;
    }
}
