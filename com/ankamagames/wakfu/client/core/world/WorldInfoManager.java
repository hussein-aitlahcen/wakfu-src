package com.ankamagames.wakfu.client.core.world;

import org.apache.log4j.*;
import java.nio.*;
import java.io.*;
import com.ankamagames.baseImpl.client.core.world.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.world.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.ecosystem.*;
import gnu.trove.*;

public class WorldInfoManager
{
    private static final Logger m_logger;
    private final TShortObjectHashMap<WorldInfo> m_infos;
    private static final WorldInfoManager m_instance;
    
    public static WorldInfoManager getInstance() {
        return WorldInfoManager.m_instance;
    }
    
    private WorldInfoManager() {
        super();
        this.m_infos = new TShortObjectHashMap<WorldInfo>();
    }
    
    public void load(final String fileName) throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(ContentFileHelper.readFile(fileName));
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int worldCount = buffer.getShort(), i = 0; i < worldCount; ++i) {
            final WorldInfo info = new WorldInfo(buffer);
            this.m_infos.put(info.m_worldId, info);
        }
    }
    
    public WorldInfo getInfo(final short worldId) {
        return this.m_infos.get(worldId);
    }
    
    public TShortArrayList getInfoWithParentWorld(final short parentWorldId) {
        final TShortArrayList list = new TShortArrayList();
        final TShortObjectIterator<WorldInfo> it = this.m_infos.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value().m_papermapId == parentWorldId) {
                list.add(it.key());
            }
        }
        return list;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorldInfoManager.class);
        m_instance = new WorldInfoManager();
    }
    
    public static class WorldInfo
    {
        public final short m_worldId;
        public ParallaxInfo[] m_parallax;
        public final short m_papermapId;
        public final int m_bannerTypeId;
        public final Color m_bannerColor;
        public final byte m_grouptype;
        public final boolean m_isHavenWorld;
        public final boolean m_isDungeon;
        public final boolean m_isExterior;
        public final boolean m_pveAllowed;
        public final boolean m_duelAllowed;
        public final boolean m_pvpAllowed;
        public final boolean m_wakfuStasis;
        public final boolean m_dimensionalBagAllowed;
        public final SubscribeWorldAccess m_subscriberWorld;
        public final Color m_backGroundColor;
        public final Color m_papermapBgColor;
        public final short m_altitude;
        public final boolean m_isDisplayTerritory;
        public final boolean m_canZoomOutToGlobalMap;
        public final boolean m_blackOutOfFight;
        public final TIntObjectHashMap<Territory> m_territories;
        public final TIntObjectHashMap<AmbienceZone> m_ambienceZone;
        
        public void setParallax(final int backgroundMapId) {
            if (this.m_parallax != null) {
                this.m_parallax[0] = this.m_parallax[0].withWorldId((short)backgroundMapId);
            }
            else {
                this.m_parallax = new ParallaxInfo[] { new ParallaxInfo((short)backgroundMapId, true, 0.5f, 2.0f, 0.5f) };
            }
        }
        
        public WorldInfo(final short worldId, final short papermapId, final int bannerTypeId, final Color papermapBgColor, final Color backgroundColor, final byte grouptype, final boolean havenWorld, final boolean isExterior, final boolean pveAllowed, final boolean duelAllowed, final boolean pvpAllowed, final ParallaxInfo[] parallaxInfos, final boolean wakfuStasis, final boolean dimensionalBagAllowed, final short altitude, final SubscribeWorldAccess subscribe, final Color bannerColor, final boolean isDisplayTerritories, final boolean canZoomOutToGlobalMap, final boolean blackOutOfFight, final boolean isDungeon) {
            super();
            this.m_bannerColor = new Color();
            this.m_backGroundColor = new Color();
            this.m_papermapBgColor = new Color();
            this.m_territories = new TIntObjectHashMap<Territory>();
            this.m_ambienceZone = new TIntObjectHashMap<AmbienceZone>();
            this.m_worldId = worldId;
            this.m_papermapId = papermapId;
            this.m_bannerTypeId = bannerTypeId;
            this.m_isHavenWorld = havenWorld;
            this.m_bannerColor.set(bannerColor.get());
            this.m_papermapBgColor.set(papermapBgColor.get());
            this.m_backGroundColor.set(backgroundColor.get());
            this.m_parallax = parallaxInfos;
            this.m_grouptype = grouptype;
            this.m_isExterior = isExterior;
            this.m_pveAllowed = pveAllowed;
            this.m_duelAllowed = duelAllowed;
            this.m_pvpAllowed = pvpAllowed;
            this.m_wakfuStasis = wakfuStasis;
            this.m_dimensionalBagAllowed = dimensionalBagAllowed;
            this.m_subscriberWorld = subscribe;
            this.m_altitude = altitude;
            this.m_canZoomOutToGlobalMap = canZoomOutToGlobalMap;
            this.m_isDisplayTerritory = isDisplayTerritories;
            this.m_blackOutOfFight = blackOutOfFight;
            this.m_isDungeon = isDungeon;
        }
        
        private WorldInfo(final ByteBuffer buffer) {
            super();
            this.m_bannerColor = new Color();
            this.m_backGroundColor = new Color();
            this.m_papermapBgColor = new Color();
            this.m_territories = new TIntObjectHashMap<Territory>();
            this.m_ambienceZone = new TIntObjectHashMap<AmbienceZone>();
            this.m_worldId = buffer.getShort();
            this.m_papermapId = buffer.getShort();
            this.m_bannerTypeId = buffer.getInt();
            this.m_papermapBgColor.set(buffer.getInt());
            this.m_backGroundColor.set(buffer.getInt());
            final byte parallaxCount = buffer.get();
            if (parallaxCount == 0) {
                this.m_parallax = null;
            }
            else {
                this.m_parallax = new ParallaxInfo[parallaxCount];
                for (int i = 0; i < this.m_parallax.length; ++i) {
                    this.m_parallax[i] = new ParallaxInfo(buffer);
                }
            }
            this.m_grouptype = buffer.get();
            this.m_isHavenWorld = (buffer.get() != 0);
            this.m_isDungeon = (buffer.get() != 0);
            this.m_isExterior = (buffer.get() != 0);
            this.m_blackOutOfFight = (buffer.get() != 0);
            this.m_pveAllowed = (buffer.get() != 0);
            this.m_duelAllowed = (buffer.get() != 0);
            this.m_pvpAllowed = (buffer.get() != 0);
            this.m_wakfuStasis = (buffer.get() != 0);
            this.m_dimensionalBagAllowed = (buffer.get() != 0);
            this.m_subscriberWorld = SubscribeWorldAccess.from(buffer.get());
            this.m_bannerColor.set(buffer.getInt());
            this.m_canZoomOutToGlobalMap = (buffer.get() != 0);
            this.m_isDisplayTerritory = (buffer.get() != 0);
            this.m_altitude = buffer.getShort();
            for (int count = buffer.get() & 0xFF, j = 0; j < count; ++j) {
                final Territory territory = new Territory();
                territory.setInstanceId(this.m_worldId);
                territory.read(buffer);
                if (!TerritoryConstants.IGNORED_TERRITORIES.contains(territory.getId())) {
                    this.m_territories.put(territory.getId(), territory);
                    TerritoryManager.INSTANCE.registerTerritory(territory);
                }
            }
            for (int zoneCount = buffer.get() & 0xFF, k = 0; k < zoneCount; ++k) {
                final AmbienceZone zone = new AmbienceZone(buffer);
                this.m_ambienceZone.put(zone.m_zoneId, zone);
            }
        }
        
        public boolean isHavenWorld() {
            return this.m_isHavenWorld;
        }
        
        public boolean isDungeon() {
            return this.m_isDungeon;
        }
        
        public boolean isFightAllowed() {
            return this.m_pveAllowed || this.m_duelAllowed || this.m_pvpAllowed;
        }
        
        public boolean isPvpAllowed() {
            return this.m_pvpAllowed;
        }
        
        public void addAmbienceZone(final AmbienceZone zone) {
            this.m_ambienceZone.put(zone.m_zoneId, zone);
        }
        
        public void addTerritory(final Territory territory) {
            this.m_territories.put(territory.getId(), territory);
        }
        
        public final GroupType getGrouptype() {
            return GroupType.getFromTypeId(this.m_grouptype);
        }
        
        public void save(final OutputBitStream stream) throws IOException {
            stream.writeShort(this.m_worldId);
            stream.writeShort(this.m_papermapId);
            stream.writeInt(this.m_bannerTypeId);
            stream.writeInt(this.m_papermapBgColor.get());
            stream.writeInt(this.m_backGroundColor.get());
            if (this.m_parallax == null) {
                stream.writeByte((byte)0);
            }
            else {
                stream.writeByte((byte)this.m_parallax.length);
                for (int i = 0; i < this.m_parallax.length; ++i) {
                    this.m_parallax[i].write(stream);
                }
            }
            stream.writeByte(this.m_grouptype);
            stream.writeByte((byte)(this.m_isHavenWorld ? 1 : 0));
            stream.writeByte((byte)(this.m_isDungeon ? 1 : 0));
            stream.writeByte((byte)(this.m_isExterior ? 1 : 0));
            stream.writeByte((byte)(this.m_blackOutOfFight ? 1 : 0));
            stream.writeByte((byte)(this.m_pveAllowed ? 1 : 0));
            stream.writeByte((byte)(this.m_duelAllowed ? 1 : 0));
            stream.writeByte((byte)(this.m_pvpAllowed ? 1 : 0));
            stream.writeByte((byte)(this.m_wakfuStasis ? 1 : 0));
            stream.writeByte((byte)(this.m_dimensionalBagAllowed ? 1 : 0));
            stream.writeByte(this.m_subscriberWorld.getId());
            stream.writeInt(this.m_bannerColor.get());
            stream.writeByte((byte)(this.m_canZoomOutToGlobalMap ? 1 : 0));
            stream.writeByte((byte)(this.m_isDisplayTerritory ? 1 : 0));
            stream.writeShort(this.m_altitude);
            final int territoryCount = this.m_territories.size();
            if (territoryCount >= 255) {
                throw new IllegalArgumentException("trop de territoire dans le monde " + this.m_worldId);
            }
            stream.writeByte((byte)territoryCount);
            final TIntObjectIterator<Territory> iter = this.m_territories.iterator();
            for (int j = 0; j < territoryCount; ++j) {
                iter.advance();
                iter.value().write(stream);
            }
            final int zoneCount = this.m_ambienceZone.size();
            if (zoneCount >= 255) {
                throw new IllegalArgumentException("trop de territoire dans le monde " + this.m_worldId);
            }
            stream.writeByte((byte)zoneCount);
            final TIntObjectIterator<AmbienceZone> iterZone = this.m_ambienceZone.iterator();
            for (int k = 0; k < zoneCount; ++k) {
                iterZone.advance();
                iterZone.value().write(stream);
            }
        }
        
        public Territory getTerritory(final int territoryId) {
            return this.m_territories.get(territoryId);
        }
        
        public AmbienceZone getAmbienceZone(final int ambienceZoneId) {
            return this.m_ambienceZone.get(ambienceZoneId);
        }
        
        public TIntObjectIterator<Territory> getTerritoriesIterator() {
            return this.m_territories.iterator();
        }
        
        public int getParallaxCount() {
            return (this.m_parallax == null) ? 0 : this.m_parallax.length;
        }
        
        public ParallaxInfo getParallaxInfo(final int index) {
            return this.m_parallax[index];
        }
        
        public static class AmbienceZone
        {
            final int m_zoneId;
            final boolean m_isWakfuZone;
            final ResourceBalancing m_resources;
            final MonsterBalancing m_monsters;
            final short[] m_groundTypes;
            final TIntByteHashMap m_authorisedResources;
            final boolean m_isPvpAllowed;
            private static final int INCARNAM_ZONE_ID = 32001;
            
            public AmbienceZone(final ByteBuffer buffer) {
                super();
                this.m_authorisedResources = new TIntByteHashMap();
                this.m_zoneId = buffer.getInt();
                this.m_isWakfuZone = (buffer.get() != 0);
                this.m_resources = new ResourceBalancing(buffer);
                this.m_monsters = new MonsterBalancing(buffer);
                final int groundtypeCount = buffer.get() & 0xFF;
                this.m_groundTypes = new short[groundtypeCount];
                for (int i = 0; i < groundtypeCount; ++i) {
                    this.m_groundTypes[i] = buffer.getShort();
                }
                this.m_isPvpAllowed = (buffer.get() != 0);
                if (this.m_zoneId == 32001) {
                    this.m_authorisedResources.put(resourceType.CULTURE.getId(), (byte)0);
                    this.m_authorisedResources.put(resourceType.PLANT.getId(), (byte)1);
                    this.m_authorisedResources.put(resourceType.TREE.getId(), (byte)0);
                }
                else {
                    this.m_authorisedResources.put(resourceType.CULTURE.getId(), (byte)1);
                    this.m_authorisedResources.put(resourceType.PLANT.getId(), (byte)1);
                    this.m_authorisedResources.put(resourceType.TREE.getId(), (byte)1);
                }
            }
            
            public AmbienceZone(final int zoneId, final boolean isWakfuZone, final ResourceBalancing resources, final MonsterBalancing monsters, final short[] groundTypes, final boolean isPvpAllowed) {
                super();
                this.m_authorisedResources = new TIntByteHashMap();
                this.m_zoneId = zoneId;
                this.m_isWakfuZone = isWakfuZone;
                this.m_resources = resources;
                this.m_monsters = monsters;
                this.m_groundTypes = groundTypes;
                this.m_isPvpAllowed = isPvpAllowed;
            }
            
            public void write(final OutputBitStream stream) throws IOException {
                stream.writeInt(this.m_zoneId);
                stream.writeByte((byte)(this.m_isWakfuZone ? 1 : 0));
                this.m_resources.write(stream);
                this.m_monsters.write(stream);
                stream.writeByte((byte)this.m_groundTypes.length);
                for (int i = 0; i < this.m_groundTypes.length; ++i) {
                    stream.writeShort(this.m_groundTypes[i]);
                }
                stream.writeByte((byte)(this.m_isPvpAllowed ? 1 : 0));
            }
            
            public int getZoneId() {
                return this.m_zoneId;
            }
            
            public ResourceBalancing getResources() {
                return this.m_resources;
            }
            
            public MonsterBalancing getMonsters() {
                return this.m_monsters;
            }
            
            public short[] getGroundTypes() {
                return this.m_groundTypes;
            }
            
            public boolean isResourceAuthorized(final int familyId) {
                return !this.m_authorisedResources.contains(familyId) || this.m_authorisedResources.get(familyId) == 1;
            }
            
            public boolean isPvpAllowed() {
                return this.m_isPvpAllowed;
            }
            
            private enum resourceType
            {
                CULTURE(2), 
                PLANT(10), 
                TREE(1);
                
                private final int m_id;
                
                private resourceType(final int id) {
                    this.m_id = id;
                }
                
                public int getId() {
                    return this.m_id;
                }
            }
        }
    }
}
