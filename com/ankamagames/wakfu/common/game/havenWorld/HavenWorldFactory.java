package com.ankamagames.wakfu.common.game.havenWorld;

import com.ankamagames.wakfu.common.game.havenWorld.definition.*;

public class HavenWorldFactory
{
    public static HavenWorld create(final HavenWorldDefinition definition) {
        return create(definition, null);
    }
    
    public static HavenWorld create(final HavenWorldDefinition definition, final GuildInfo guildInfo) {
        return create(definition, guildInfo, 0);
    }
    
    public static HavenWorld create(final HavenWorldDefinition definition, final GuildInfo guildInfo, final int resources) {
        final HavenWorldModel world = new HavenWorldModel(definition);
        world.setGuildInfo(guildInfo);
        world.setResources(resources);
        return world;
    }
}
