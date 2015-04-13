package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.exception.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class HavenWorldGuildMessage extends InputOnlyProxyMessage
{
    private HavenWorld m_world;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final HavenWorldDefinition def = HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(bb.getShort());
        final GuildInfo guildInfo = GuildInfo.decode(bb);
        this.m_world = HavenWorldFactory.create(def, guildInfo);
        final UnSerializer controller = new UnSerializer(this.m_world);
        for (int i = 0, size = bb.getInt(); i < size; ++i) {
            controller.unSerializePartition(bb);
        }
        for (int i = 0, size = bb.getInt(); i < size; ++i) {
            controller.unSerializeBuilding(bb);
        }
        return true;
    }
    
    public HavenWorld getWorld() {
        return this.m_world;
    }
    
    @Override
    public int getId() {
        return 5518;
    }
    
    private static class UnSerializer extends HavenWorldController
    {
        private static final Logger m_logger;
        
        UnSerializer(final HavenWorld world) {
            super(world);
        }
        
        public void unSerializePartition(final ByteBuffer bb) {
            final short x = bb.getShort();
            final short y = bb.getShort();
            try {
                this.addPartition(x, y);
            }
            catch (HavenWorldException e) {
                UnSerializer.m_logger.error((Object)"[HAVEN_WORLD] Impossible d'ajouter une partition", (Throwable)e);
            }
        }
        
        public void unSerializeBuilding(final ByteBuffer bb) {
            final short buildingRefId = bb.getShort();
            final long buildingUid = bb.getLong();
            final int buildingItemId = bb.getInt();
            final long creationDate = bb.getLong();
            final short x = bb.getShort();
            final short y = bb.getShort();
            try {
                this.addBuilding(new BuildingStruct(buildingUid, buildingRefId, buildingItemId, x, y), creationDate);
            }
            catch (HavenWorldException e) {
                UnSerializer.m_logger.error((Object)("[HAVEN_WORLD] Impossible d'ajouter le b\u00e2timent " + buildingUid), (Throwable)e);
            }
        }
        
        static {
            m_logger = Logger.getLogger((Class)UnSerializer.class);
        }
    }
}
