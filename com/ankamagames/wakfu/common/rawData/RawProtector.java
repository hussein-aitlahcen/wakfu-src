package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawProtector implements VersionableObject
{
    public int protectorId;
    public Nationality nationality;
    public Appearance appearance;
    public Challenges challenges;
    public ReferenceMerchantInventories referenceMerchantInventories;
    public NationMerchantInventories nationMerchantInventories;
    public Wallet wallet;
    public Stake stake;
    public Taxes taxes;
    public WeatherModifiers weatherModifiers;
    public Satisfaction satisfaction;
    public MonsterTargets monsterTargets;
    public ResourceTargets resourceTargets;
    public Ecosystem ecosystem;
    
    public RawProtector() {
        super();
        this.protectorId = 0;
        this.nationality = null;
        this.appearance = null;
        this.challenges = null;
        this.referenceMerchantInventories = null;
        this.nationMerchantInventories = null;
        this.wallet = null;
        this.stake = null;
        this.taxes = null;
        this.weatherModifiers = null;
        this.satisfaction = null;
        this.monsterTargets = null;
        this.resourceTargets = null;
        this.ecosystem = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.protectorId);
        if (this.nationality != null) {
            buffer.put((byte)1);
            final boolean nationality_ok = this.nationality.serialize(buffer);
            if (!nationality_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.appearance != null) {
            buffer.put((byte)1);
            final boolean appearance_ok = this.appearance.serialize(buffer);
            if (!appearance_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.challenges != null) {
            buffer.put((byte)1);
            final boolean challenges_ok = this.challenges.serialize(buffer);
            if (!challenges_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.referenceMerchantInventories != null) {
            buffer.put((byte)1);
            final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.serialize(buffer);
            if (!referenceMerchantInventories_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.nationMerchantInventories != null) {
            buffer.put((byte)1);
            final boolean nationMerchantInventories_ok = this.nationMerchantInventories.serialize(buffer);
            if (!nationMerchantInventories_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.wallet != null) {
            buffer.put((byte)1);
            final boolean wallet_ok = this.wallet.serialize(buffer);
            if (!wallet_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.stake != null) {
            buffer.put((byte)1);
            final boolean stake_ok = this.stake.serialize(buffer);
            if (!stake_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.taxes != null) {
            buffer.put((byte)1);
            final boolean taxes_ok = this.taxes.serialize(buffer);
            if (!taxes_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.weatherModifiers != null) {
            buffer.put((byte)1);
            final boolean weatherModifiers_ok = this.weatherModifiers.serialize(buffer);
            if (!weatherModifiers_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.satisfaction != null) {
            buffer.put((byte)1);
            final boolean satisfaction_ok = this.satisfaction.serialize(buffer);
            if (!satisfaction_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.monsterTargets != null) {
            buffer.put((byte)1);
            final boolean monsterTargets_ok = this.monsterTargets.serialize(buffer);
            if (!monsterTargets_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.resourceTargets != null) {
            buffer.put((byte)1);
            final boolean resourceTargets_ok = this.resourceTargets.serialize(buffer);
            if (!resourceTargets_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.ecosystem != null) {
            buffer.put((byte)1);
            final boolean ecosystem_ok = this.ecosystem.serialize(buffer);
            if (!ecosystem_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.protectorId = buffer.getInt();
        final boolean nationality_present = buffer.get() == 1;
        if (nationality_present) {
            this.nationality = new Nationality();
            final boolean nationality_ok = this.nationality.unserialize(buffer);
            if (!nationality_ok) {
                return false;
            }
        }
        else {
            this.nationality = null;
        }
        final boolean appearance_present = buffer.get() == 1;
        if (appearance_present) {
            this.appearance = new Appearance();
            final boolean appearance_ok = this.appearance.unserialize(buffer);
            if (!appearance_ok) {
                return false;
            }
        }
        else {
            this.appearance = null;
        }
        final boolean challenges_present = buffer.get() == 1;
        if (challenges_present) {
            this.challenges = new Challenges();
            final boolean challenges_ok = this.challenges.unserialize(buffer);
            if (!challenges_ok) {
                return false;
            }
        }
        else {
            this.challenges = null;
        }
        final boolean referenceMerchantInventories_present = buffer.get() == 1;
        if (referenceMerchantInventories_present) {
            this.referenceMerchantInventories = new ReferenceMerchantInventories();
            final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserialize(buffer);
            if (!referenceMerchantInventories_ok) {
                return false;
            }
        }
        else {
            this.referenceMerchantInventories = null;
        }
        final boolean nationMerchantInventories_present = buffer.get() == 1;
        if (nationMerchantInventories_present) {
            this.nationMerchantInventories = new NationMerchantInventories();
            final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserialize(buffer);
            if (!nationMerchantInventories_ok) {
                return false;
            }
        }
        else {
            this.nationMerchantInventories = null;
        }
        final boolean wallet_present = buffer.get() == 1;
        if (wallet_present) {
            this.wallet = new Wallet();
            final boolean wallet_ok = this.wallet.unserialize(buffer);
            if (!wallet_ok) {
                return false;
            }
        }
        else {
            this.wallet = null;
        }
        final boolean stake_present = buffer.get() == 1;
        if (stake_present) {
            this.stake = new Stake();
            final boolean stake_ok = this.stake.unserialize(buffer);
            if (!stake_ok) {
                return false;
            }
        }
        else {
            this.stake = null;
        }
        final boolean taxes_present = buffer.get() == 1;
        if (taxes_present) {
            this.taxes = new Taxes();
            final boolean taxes_ok = this.taxes.unserialize(buffer);
            if (!taxes_ok) {
                return false;
            }
        }
        else {
            this.taxes = null;
        }
        final boolean weatherModifiers_present = buffer.get() == 1;
        if (weatherModifiers_present) {
            this.weatherModifiers = new WeatherModifiers();
            final boolean weatherModifiers_ok = this.weatherModifiers.unserialize(buffer);
            if (!weatherModifiers_ok) {
                return false;
            }
        }
        else {
            this.weatherModifiers = null;
        }
        final boolean satisfaction_present = buffer.get() == 1;
        if (satisfaction_present) {
            this.satisfaction = new Satisfaction();
            final boolean satisfaction_ok = this.satisfaction.unserialize(buffer);
            if (!satisfaction_ok) {
                return false;
            }
        }
        else {
            this.satisfaction = null;
        }
        final boolean monsterTargets_present = buffer.get() == 1;
        if (monsterTargets_present) {
            this.monsterTargets = new MonsterTargets();
            final boolean monsterTargets_ok = this.monsterTargets.unserialize(buffer);
            if (!monsterTargets_ok) {
                return false;
            }
        }
        else {
            this.monsterTargets = null;
        }
        final boolean resourceTargets_present = buffer.get() == 1;
        if (resourceTargets_present) {
            this.resourceTargets = new ResourceTargets();
            final boolean resourceTargets_ok = this.resourceTargets.unserialize(buffer);
            if (!resourceTargets_ok) {
                return false;
            }
        }
        else {
            this.resourceTargets = null;
        }
        final boolean ecosystem_present = buffer.get() == 1;
        if (ecosystem_present) {
            this.ecosystem = new Ecosystem();
            final boolean ecosystem_ok = this.ecosystem.unserialize(buffer);
            if (!ecosystem_ok) {
                return false;
            }
        }
        else {
            this.ecosystem = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.protectorId = 0;
        this.nationality = null;
        this.appearance = null;
        this.challenges = null;
        this.referenceMerchantInventories = null;
        this.nationMerchantInventories = null;
        this.wallet = null;
        this.stake = null;
        this.taxes = null;
        this.weatherModifiers = null;
        this.satisfaction = null;
        this.monsterTargets = null;
        this.resourceTargets = null;
        this.ecosystem = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawProtectorConverter converter = new RawProtectorConverter();
        final boolean ok = converter.unserializeVersion(buffer, version);
        if (ok) {
            converter.pushResult();
            return true;
        }
        return false;
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 4;
        ++size;
        if (this.nationality != null) {
            size += this.nationality.serializedSize();
        }
        ++size;
        if (this.appearance != null) {
            size += this.appearance.serializedSize();
        }
        ++size;
        if (this.challenges != null) {
            size += this.challenges.serializedSize();
        }
        ++size;
        if (this.referenceMerchantInventories != null) {
            size += this.referenceMerchantInventories.serializedSize();
        }
        ++size;
        if (this.nationMerchantInventories != null) {
            size += this.nationMerchantInventories.serializedSize();
        }
        ++size;
        if (this.wallet != null) {
            size += this.wallet.serializedSize();
        }
        ++size;
        if (this.stake != null) {
            size += this.stake.serializedSize();
        }
        ++size;
        if (this.taxes != null) {
            size += this.taxes.serializedSize();
        }
        ++size;
        if (this.weatherModifiers != null) {
            size += this.weatherModifiers.serializedSize();
        }
        ++size;
        if (this.satisfaction != null) {
            size += this.satisfaction.serializedSize();
        }
        ++size;
        if (this.monsterTargets != null) {
            size += this.monsterTargets.serializedSize();
        }
        ++size;
        if (this.resourceTargets != null) {
            size += this.resourceTargets.serializedSize();
        }
        ++size;
        if (this.ecosystem != null) {
            size += this.ecosystem.serializedSize();
        }
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("protectorId=").append(this.protectorId).append('\n');
        repr.append(prefix).append("nationality=");
        if (this.nationality == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.nationality.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("appearance=");
        if (this.appearance == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.appearance.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("challenges=");
        if (this.challenges == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.challenges.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("referenceMerchantInventories=");
        if (this.referenceMerchantInventories == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.referenceMerchantInventories.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("nationMerchantInventories=");
        if (this.nationMerchantInventories == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.nationMerchantInventories.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("wallet=");
        if (this.wallet == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.wallet.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("stake=");
        if (this.stake == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.stake.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("taxes=");
        if (this.taxes == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.taxes.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("weatherModifiers=");
        if (this.weatherModifiers == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.weatherModifiers.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("satisfaction=");
        if (this.satisfaction == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.satisfaction.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("monsterTargets=");
        if (this.monsterTargets == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.monsterTargets.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("resourceTargets=");
        if (this.resourceTargets == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.resourceTargets.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("ecosystem=");
        if (this.ecosystem == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.ecosystem.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class Nationality implements VersionableObject
    {
        public int nativeNationId;
        public int currentNationId;
        public int territoryId;
        public static final int SERIALIZED_SIZE = 12;
        
        public Nationality() {
            super();
            this.nativeNationId = 0;
            this.currentNationId = 0;
            this.territoryId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.nativeNationId);
            buffer.putInt(this.currentNationId);
            buffer.putInt(this.territoryId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.nativeNationId = buffer.getInt();
            this.currentNationId = buffer.getInt();
            this.territoryId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.nativeNationId = 0;
            this.currentNationId = 0;
            this.territoryId = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 12;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("nativeNationId=").append(this.nativeNationId).append('\n');
            repr.append(prefix).append("currentNationId=").append(this.currentNationId).append('\n');
            repr.append(prefix).append("territoryId=").append(this.territoryId).append('\n');
        }
    }
    
    public static class Appearance implements VersionableObject
    {
        public int monsterCrewId;
        public long monterId;
        public static final int SERIALIZED_SIZE = 12;
        
        public Appearance() {
            super();
            this.monsterCrewId = 0;
            this.monterId = 0L;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.monsterCrewId);
            buffer.putLong(this.monterId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.monsterCrewId = buffer.getInt();
            this.monterId = buffer.getLong();
            return true;
        }
        
        @Override
        public void clear() {
            this.monsterCrewId = 0;
            this.monterId = 0L;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 12;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("monsterCrewId=").append(this.monsterCrewId).append('\n');
            repr.append(prefix).append("monterId=").append(this.monterId).append('\n');
        }
    }
    
    public static class Challenges implements VersionableObject
    {
        public int dropTableId;
        public int dropTableIdToBuy;
        public int dropTableIdChaos;
        public static final int SERIALIZED_SIZE = 12;
        
        public Challenges() {
            super();
            this.dropTableId = 0;
            this.dropTableIdToBuy = 0;
            this.dropTableIdChaos = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.dropTableId);
            buffer.putInt(this.dropTableIdToBuy);
            buffer.putInt(this.dropTableIdChaos);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.dropTableId = buffer.getInt();
            this.dropTableIdToBuy = buffer.getInt();
            this.dropTableIdChaos = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.dropTableId = 0;
            this.dropTableIdToBuy = 0;
            this.dropTableIdChaos = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 313) {
                return this.unserialize(buffer);
            }
            final ChallengesConverter converter = new ChallengesConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            return 12;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("dropTableId=").append(this.dropTableId).append('\n');
            repr.append(prefix).append("dropTableIdToBuy=").append(this.dropTableIdToBuy).append('\n');
            repr.append(prefix).append("dropTableIdChaos=").append(this.dropTableIdChaos).append('\n');
        }
        
        private final class ChallengesConverter
        {
            private int dropTableId;
            private int dropTableIdToBuy;
            private int dropTableIdChaos;
            
            private ChallengesConverter() {
                super();
                this.dropTableId = 0;
                this.dropTableIdToBuy = 0;
                this.dropTableIdChaos = 0;
            }
            
            public void pushResult() {
                Challenges.this.dropTableId = this.dropTableId;
                Challenges.this.dropTableIdToBuy = this.dropTableIdToBuy;
                Challenges.this.dropTableIdChaos = this.dropTableIdChaos;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                this.dropTableId = buffer.getInt();
                this.dropTableIdToBuy = buffer.getInt();
                return true;
            }
            
            public void convert_v1_to_v313() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 313) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v313();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class ReferenceMerchantInventories implements VersionableObject
    {
        public final RawProtectorReferenceInventory challengeReferenceInventory;
        public final RawProtectorReferenceInventory climateReferenceInventory;
        public final RawProtectorReferenceInventory buffsReferenceInventory;
        public final RawProtectorReferenceInventory itemsReferenceInventory;
        
        public ReferenceMerchantInventories() {
            super();
            this.challengeReferenceInventory = new RawProtectorReferenceInventory();
            this.climateReferenceInventory = new RawProtectorReferenceInventory();
            this.buffsReferenceInventory = new RawProtectorReferenceInventory();
            this.itemsReferenceInventory = new RawProtectorReferenceInventory();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean challengeReferenceInventory_ok = this.challengeReferenceInventory.serialize(buffer);
            if (!challengeReferenceInventory_ok) {
                return false;
            }
            final boolean climateReferenceInventory_ok = this.climateReferenceInventory.serialize(buffer);
            if (!climateReferenceInventory_ok) {
                return false;
            }
            final boolean buffsReferenceInventory_ok = this.buffsReferenceInventory.serialize(buffer);
            if (!buffsReferenceInventory_ok) {
                return false;
            }
            final boolean itemsReferenceInventory_ok = this.itemsReferenceInventory.serialize(buffer);
            return itemsReferenceInventory_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean challengeReferenceInventory_ok = this.challengeReferenceInventory.unserialize(buffer);
            if (!challengeReferenceInventory_ok) {
                return false;
            }
            final boolean climateReferenceInventory_ok = this.climateReferenceInventory.unserialize(buffer);
            if (!climateReferenceInventory_ok) {
                return false;
            }
            final boolean buffsReferenceInventory_ok = this.buffsReferenceInventory.unserialize(buffer);
            if (!buffsReferenceInventory_ok) {
                return false;
            }
            final boolean itemsReferenceInventory_ok = this.itemsReferenceInventory.unserialize(buffer);
            return itemsReferenceInventory_ok;
        }
        
        @Override
        public void clear() {
            this.challengeReferenceInventory.clear();
            this.climateReferenceInventory.clear();
            this.buffsReferenceInventory.clear();
            this.itemsReferenceInventory.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 1) {
                return this.unserialize(buffer);
            }
            final ReferenceMerchantInventoriesConverter converter = new ReferenceMerchantInventoriesConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.challengeReferenceInventory.serializedSize();
            size += this.climateReferenceInventory.serializedSize();
            size += this.buffsReferenceInventory.serializedSize();
            size += this.itemsReferenceInventory.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("challengeReferenceInventory=...\n");
            this.challengeReferenceInventory.internalToString(repr, prefix + "  ");
            repr.append(prefix).append("climateReferenceInventory=...\n");
            this.climateReferenceInventory.internalToString(repr, prefix + "  ");
            repr.append(prefix).append("buffsReferenceInventory=...\n");
            this.buffsReferenceInventory.internalToString(repr, prefix + "  ");
            repr.append(prefix).append("itemsReferenceInventory=...\n");
            this.itemsReferenceInventory.internalToString(repr, prefix + "  ");
        }
        
        private final class ReferenceMerchantInventoriesConverter
        {
            private final RawProtectorReferenceInventory challengeReferenceInventory;
            private final RawProtectorReferenceInventory climateReferenceInventory;
            private final RawProtectorReferenceInventory buffsReferenceInventory;
            private final RawProtectorReferenceInventory itemsReferenceInventory;
            
            private ReferenceMerchantInventoriesConverter() {
                super();
                this.challengeReferenceInventory = new RawProtectorReferenceInventory();
                this.climateReferenceInventory = new RawProtectorReferenceInventory();
                this.buffsReferenceInventory = new RawProtectorReferenceInventory();
                this.itemsReferenceInventory = new RawProtectorReferenceInventory();
            }
            
            public void pushResult() {
                ReferenceMerchantInventories.this.challengeReferenceInventory.contents.clear();
                ReferenceMerchantInventories.this.challengeReferenceInventory.contents.ensureCapacity(this.challengeReferenceInventory.contents.size());
                ReferenceMerchantInventories.this.challengeReferenceInventory.contents.addAll(this.challengeReferenceInventory.contents);
                ReferenceMerchantInventories.this.challengeReferenceInventory.contentsSelection = this.challengeReferenceInventory.contentsSelection;
                ReferenceMerchantInventories.this.challengeReferenceInventory.buyableContents.clear();
                ReferenceMerchantInventories.this.challengeReferenceInventory.buyableContents.ensureCapacity(this.challengeReferenceInventory.buyableContents.size());
                ReferenceMerchantInventories.this.challengeReferenceInventory.buyableContents.addAll(this.challengeReferenceInventory.buyableContents);
                ReferenceMerchantInventories.this.climateReferenceInventory.contents.clear();
                ReferenceMerchantInventories.this.climateReferenceInventory.contents.ensureCapacity(this.climateReferenceInventory.contents.size());
                ReferenceMerchantInventories.this.climateReferenceInventory.contents.addAll(this.climateReferenceInventory.contents);
                ReferenceMerchantInventories.this.climateReferenceInventory.contentsSelection = this.climateReferenceInventory.contentsSelection;
                ReferenceMerchantInventories.this.climateReferenceInventory.buyableContents.clear();
                ReferenceMerchantInventories.this.climateReferenceInventory.buyableContents.ensureCapacity(this.climateReferenceInventory.buyableContents.size());
                ReferenceMerchantInventories.this.climateReferenceInventory.buyableContents.addAll(this.climateReferenceInventory.buyableContents);
                ReferenceMerchantInventories.this.buffsReferenceInventory.contents.clear();
                ReferenceMerchantInventories.this.buffsReferenceInventory.contents.ensureCapacity(this.buffsReferenceInventory.contents.size());
                ReferenceMerchantInventories.this.buffsReferenceInventory.contents.addAll(this.buffsReferenceInventory.contents);
                ReferenceMerchantInventories.this.buffsReferenceInventory.contentsSelection = this.buffsReferenceInventory.contentsSelection;
                ReferenceMerchantInventories.this.buffsReferenceInventory.buyableContents.clear();
                ReferenceMerchantInventories.this.buffsReferenceInventory.buyableContents.ensureCapacity(this.buffsReferenceInventory.buyableContents.size());
                ReferenceMerchantInventories.this.buffsReferenceInventory.buyableContents.addAll(this.buffsReferenceInventory.buyableContents);
                ReferenceMerchantInventories.this.itemsReferenceInventory.contents.clear();
                ReferenceMerchantInventories.this.itemsReferenceInventory.contents.ensureCapacity(this.itemsReferenceInventory.contents.size());
                ReferenceMerchantInventories.this.itemsReferenceInventory.contents.addAll(this.itemsReferenceInventory.contents);
                ReferenceMerchantInventories.this.itemsReferenceInventory.contentsSelection = this.itemsReferenceInventory.contentsSelection;
                ReferenceMerchantInventories.this.itemsReferenceInventory.buyableContents.clear();
                ReferenceMerchantInventories.this.itemsReferenceInventory.buyableContents.ensureCapacity(this.itemsReferenceInventory.buyableContents.size());
                ReferenceMerchantInventories.this.itemsReferenceInventory.buyableContents.addAll(this.itemsReferenceInventory.buyableContents);
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                final boolean challengeReferenceInventory_ok = this.challengeReferenceInventory.unserializeVersion(buffer, 0);
                if (!challengeReferenceInventory_ok) {
                    return false;
                }
                final boolean climateReferenceInventory_ok = this.climateReferenceInventory.unserializeVersion(buffer, 0);
                if (!climateReferenceInventory_ok) {
                    return false;
                }
                final boolean buffsReferenceInventory_ok = this.buffsReferenceInventory.unserializeVersion(buffer, 0);
                if (!buffsReferenceInventory_ok) {
                    return false;
                }
                final boolean itemsReferenceInventory_ok = this.itemsReferenceInventory.unserializeVersion(buffer, 0);
                return itemsReferenceInventory_ok;
            }
            
            public void convert_v0_to_v1() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 0) {
                    return false;
                }
                if (version >= 1) {
                    return false;
                }
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class NationMerchantInventories implements VersionableObject
    {
        public final RawProtectorMerchantInventory challengeMerchantInventory;
        public final RawProtectorMerchantInventory climateMerchantInventory;
        public final RawProtectorMerchantInventory buffsMerchantInventory;
        public final RawProtectorMerchantInventory itemsMerchantInventory;
        
        public NationMerchantInventories() {
            super();
            this.challengeMerchantInventory = new RawProtectorMerchantInventory();
            this.climateMerchantInventory = new RawProtectorMerchantInventory();
            this.buffsMerchantInventory = new RawProtectorMerchantInventory();
            this.itemsMerchantInventory = new RawProtectorMerchantInventory();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.serialize(buffer);
            if (!challengeMerchantInventory_ok) {
                return false;
            }
            final boolean climateMerchantInventory_ok = this.climateMerchantInventory.serialize(buffer);
            if (!climateMerchantInventory_ok) {
                return false;
            }
            final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.serialize(buffer);
            if (!buffsMerchantInventory_ok) {
                return false;
            }
            final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.serialize(buffer);
            return itemsMerchantInventory_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserialize(buffer);
            if (!challengeMerchantInventory_ok) {
                return false;
            }
            final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserialize(buffer);
            if (!climateMerchantInventory_ok) {
                return false;
            }
            final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserialize(buffer);
            if (!buffsMerchantInventory_ok) {
                return false;
            }
            final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserialize(buffer);
            return itemsMerchantInventory_ok;
        }
        
        @Override
        public void clear() {
            this.challengeMerchantInventory.clear();
            this.climateMerchantInventory.clear();
            this.buffsMerchantInventory.clear();
            this.itemsMerchantInventory.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10037001) {
                return this.unserialize(buffer);
            }
            final NationMerchantInventoriesConverter converter = new NationMerchantInventoriesConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.challengeMerchantInventory.serializedSize();
            size += this.climateMerchantInventory.serializedSize();
            size += this.buffsMerchantInventory.serializedSize();
            size += this.itemsMerchantInventory.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("challengeMerchantInventory=...\n");
            this.challengeMerchantInventory.internalToString(repr, prefix + "  ");
            repr.append(prefix).append("climateMerchantInventory=...\n");
            this.climateMerchantInventory.internalToString(repr, prefix + "  ");
            repr.append(prefix).append("buffsMerchantInventory=...\n");
            this.buffsMerchantInventory.internalToString(repr, prefix + "  ");
            repr.append(prefix).append("itemsMerchantInventory=...\n");
            this.itemsMerchantInventory.internalToString(repr, prefix + "  ");
        }
        
        private final class NationMerchantInventoriesConverter
        {
            private final RawProtectorMerchantInventory challengeMerchantInventory;
            private final RawProtectorMerchantInventory climateMerchantInventory;
            private final RawProtectorMerchantInventory buffsMerchantInventory;
            private final RawProtectorMerchantInventory itemsMerchantInventory;
            
            private NationMerchantInventoriesConverter() {
                super();
                this.challengeMerchantInventory = new RawProtectorMerchantInventory();
                this.climateMerchantInventory = new RawProtectorMerchantInventory();
                this.buffsMerchantInventory = new RawProtectorMerchantInventory();
                this.itemsMerchantInventory = new RawProtectorMerchantInventory();
            }
            
            public void pushResult() {
                NationMerchantInventories.this.challengeMerchantInventory.uid = this.challengeMerchantInventory.uid;
                NationMerchantInventories.this.challengeMerchantInventory.contents.clear();
                NationMerchantInventories.this.challengeMerchantInventory.contents.ensureCapacity(this.challengeMerchantInventory.contents.size());
                NationMerchantInventories.this.challengeMerchantInventory.contents.addAll(this.challengeMerchantInventory.contents);
                NationMerchantInventories.this.climateMerchantInventory.uid = this.climateMerchantInventory.uid;
                NationMerchantInventories.this.climateMerchantInventory.contents.clear();
                NationMerchantInventories.this.climateMerchantInventory.contents.ensureCapacity(this.climateMerchantInventory.contents.size());
                NationMerchantInventories.this.climateMerchantInventory.contents.addAll(this.climateMerchantInventory.contents);
                NationMerchantInventories.this.buffsMerchantInventory.uid = this.buffsMerchantInventory.uid;
                NationMerchantInventories.this.buffsMerchantInventory.contents.clear();
                NationMerchantInventories.this.buffsMerchantInventory.contents.ensureCapacity(this.buffsMerchantInventory.contents.size());
                NationMerchantInventories.this.buffsMerchantInventory.contents.addAll(this.buffsMerchantInventory.contents);
                NationMerchantInventories.this.itemsMerchantInventory.uid = this.itemsMerchantInventory.uid;
                NationMerchantInventories.this.itemsMerchantInventory.contents.clear();
                NationMerchantInventories.this.itemsMerchantInventory.contents.ensureCapacity(this.itemsMerchantInventory.contents.size());
                NationMerchantInventories.this.itemsMerchantInventory.contents.addAll(this.itemsMerchantInventory.contents);
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 0);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 0);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 0);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 0);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 1);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 1);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 1);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 1);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v313(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 313);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 313);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 313);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 313);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v314(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 314);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 314);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 314);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 314);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v315(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 315);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 315);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 315);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 315);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v10003(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 10003);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 10003);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 10003);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 10003);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v10023(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 10023);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 10023);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 10023);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 10023);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v10028000(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 10028000);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 10028000);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 10028000);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 10028000);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v10029000(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 10029000);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 10029000);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 10029000);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 10029000);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v10032003(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 10032003);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 10032003);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 10032003);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 10032003);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v10035004(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 10035004);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 10035004);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 10035004);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 10035004);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v10035007(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 10035007);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 10035007);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 10035007);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 10035007);
                return itemsMerchantInventory_ok;
            }
            
            private boolean unserialize_v10036004(final ByteBuffer buffer) {
                final boolean challengeMerchantInventory_ok = this.challengeMerchantInventory.unserializeVersion(buffer, 10036004);
                if (!challengeMerchantInventory_ok) {
                    return false;
                }
                final boolean climateMerchantInventory_ok = this.climateMerchantInventory.unserializeVersion(buffer, 10036004);
                if (!climateMerchantInventory_ok) {
                    return false;
                }
                final boolean buffsMerchantInventory_ok = this.buffsMerchantInventory.unserializeVersion(buffer, 10036004);
                if (!buffsMerchantInventory_ok) {
                    return false;
                }
                final boolean itemsMerchantInventory_ok = this.itemsMerchantInventory.unserializeVersion(buffer, 10036004);
                return itemsMerchantInventory_ok;
            }
            
            public void convert_v0_to_v1() {
            }
            
            public void convert_v1_to_v313() {
            }
            
            public void convert_v313_to_v314() {
            }
            
            public void convert_v314_to_v315() {
            }
            
            public void convert_v315_to_v10003() {
            }
            
            public void convert_v10003_to_v10023() {
            }
            
            public void convert_v10023_to_v10028000() {
            }
            
            public void convert_v10028000_to_v10029000() {
            }
            
            public void convert_v10029000_to_v10032003() {
            }
            
            public void convert_v10032003_to_v10035004() {
            }
            
            public void convert_v10035004_to_v10035007() {
            }
            
            public void convert_v10035007_to_v10036004() {
            }
            
            public void convert_v10036004_to_v10037001() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 0) {
                    return false;
                }
                if (version < 1) {
                    final boolean ok = this.unserialize_v0(buffer);
                    if (ok) {
                        this.convert_v0_to_v1();
                        this.convert_v1_to_v313();
                        this.convert_v313_to_v314();
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 313) {
                    final boolean ok = this.unserialize_v1(buffer);
                    if (ok) {
                        this.convert_v1_to_v313();
                        this.convert_v313_to_v314();
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 314) {
                    final boolean ok = this.unserialize_v313(buffer);
                    if (ok) {
                        this.convert_v313_to_v314();
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 315) {
                    final boolean ok = this.unserialize_v314(buffer);
                    if (ok) {
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10003) {
                    final boolean ok = this.unserialize_v315(buffer);
                    if (ok) {
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10023) {
                    final boolean ok = this.unserialize_v10003(buffer);
                    if (ok) {
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10028000) {
                    final boolean ok = this.unserialize_v10023(buffer);
                    if (ok) {
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10029000) {
                    final boolean ok = this.unserialize_v10028000(buffer);
                    if (ok) {
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10032003) {
                    final boolean ok = this.unserialize_v10029000(buffer);
                    if (ok) {
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10035004) {
                    final boolean ok = this.unserialize_v10032003(buffer);
                    if (ok) {
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10035007) {
                    final boolean ok = this.unserialize_v10035004(buffer);
                    if (ok) {
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10036004) {
                    final boolean ok = this.unserialize_v10035007(buffer);
                    if (ok) {
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else {
                    if (version >= 10037001) {
                        return false;
                    }
                    final boolean ok = this.unserialize_v10036004(buffer);
                    if (ok) {
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
            }
        }
    }
    
    public static class Wallet implements VersionableObject
    {
        public int cashAmount;
        public final ArrayList<Content> contexts;
        
        public Wallet() {
            super();
            this.cashAmount = 0;
            this.contexts = new ArrayList<Content>(0);
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.cashAmount);
            if (this.contexts.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.contexts.size());
            for (int i = 0; i < this.contexts.size(); ++i) {
                final Content contexts_element = this.contexts.get(i);
                final boolean contexts_element_ok = contexts_element.serialize(buffer);
                if (!contexts_element_ok) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.cashAmount = buffer.getInt();
            final int contexts_size = buffer.getShort() & 0xFFFF;
            this.contexts.clear();
            this.contexts.ensureCapacity(contexts_size);
            for (int i = 0; i < contexts_size; ++i) {
                final Content contexts_element = new Content();
                final boolean contexts_element_ok = contexts_element.unserialize(buffer);
                if (!contexts_element_ok) {
                    return false;
                }
                this.contexts.add(contexts_element);
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.cashAmount = 0;
            this.contexts.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 4;
            size += 2;
            for (int i = 0; i < this.contexts.size(); ++i) {
                final Content contexts_element = this.contexts.get(i);
                size += contexts_element.serializedSize();
            }
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("cashAmount=").append(this.cashAmount).append('\n');
            repr.append(prefix).append("contexts=");
            if (this.contexts.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.contexts.size()).append(" elements)...\n");
                for (int i = 0; i < this.contexts.size(); ++i) {
                    final Content contexts_element = this.contexts.get(i);
                    contexts_element.internalToString(repr, prefix + i + "/ ");
                }
            }
        }
        
        public static class Content implements VersionableObject
        {
            public byte contextId;
            public int cashAmount;
            public static final int SERIALIZED_SIZE = 5;
            
            public Content() {
                super();
                this.contextId = 0;
                this.cashAmount = 0;
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                buffer.put(this.contextId);
                buffer.putInt(this.cashAmount);
                return true;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                this.contextId = buffer.get();
                this.cashAmount = buffer.getInt();
                return true;
            }
            
            @Override
            public void clear() {
                this.contextId = 0;
                this.cashAmount = 0;
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                return this.unserialize(buffer);
            }
            
            @Override
            public int serializedSize() {
                return 5;
            }
            
            @Override
            public final String toString() {
                final StringBuilder repr = new StringBuilder();
                this.internalToString(repr, "");
                return repr.toString();
            }
            
            public final void internalToString(final StringBuilder repr, final String prefix) {
                repr.append(prefix).append("contextId=").append(this.contextId).append('\n');
                repr.append(prefix).append("cashAmount=").append(this.cashAmount).append('\n');
            }
        }
    }
    
    public static class Stake implements VersionableObject
    {
        public int fightStake;
        public static final int SERIALIZED_SIZE = 4;
        
        public Stake() {
            super();
            this.fightStake = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.fightStake);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.fightStake = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.fightStake = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 4;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("fightStake=").append(this.fightStake).append('\n');
        }
    }
    
    public static class Taxes implements VersionableObject
    {
        public final ArrayList<TaxValue> taxValues;
        
        public Taxes() {
            super();
            this.taxValues = new ArrayList<TaxValue>(0);
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.taxValues.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.taxValues.size());
            for (int i = 0; i < this.taxValues.size(); ++i) {
                final TaxValue taxValues_element = this.taxValues.get(i);
                final boolean taxValues_element_ok = taxValues_element.serialize(buffer);
                if (!taxValues_element_ok) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int taxValues_size = buffer.getShort() & 0xFFFF;
            this.taxValues.clear();
            this.taxValues.ensureCapacity(taxValues_size);
            for (int i = 0; i < taxValues_size; ++i) {
                final TaxValue taxValues_element = new TaxValue();
                final boolean taxValues_element_ok = taxValues_element.unserialize(buffer);
                if (!taxValues_element_ok) {
                    return false;
                }
                this.taxValues.add(taxValues_element);
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.taxValues.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 1) {
                return this.unserialize(buffer);
            }
            final TaxesConverter converter = new TaxesConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 2;
            for (int i = 0; i < this.taxValues.size(); ++i) {
                final TaxValue taxValues_element = this.taxValues.get(i);
                size += taxValues_element.serializedSize();
            }
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("taxValues=");
            if (this.taxValues.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.taxValues.size()).append(" elements)...\n");
                for (int i = 0; i < this.taxValues.size(); ++i) {
                    final TaxValue taxValues_element = this.taxValues.get(i);
                    taxValues_element.internalToString(repr, prefix + i + "/ ");
                }
            }
        }
        
        public static class TaxValue implements VersionableObject
        {
            public final RawTax tax;
            
            public TaxValue() {
                super();
                this.tax = new RawTax();
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                final boolean tax_ok = this.tax.serialize(buffer);
                return tax_ok;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                final boolean tax_ok = this.tax.unserialize(buffer);
                return tax_ok;
            }
            
            @Override
            public void clear() {
                this.tax.clear();
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version >= 1) {
                    return this.unserialize(buffer);
                }
                final TaxValueConverter converter = new TaxValueConverter();
                final boolean ok = converter.unserializeVersion(buffer, version);
                if (ok) {
                    converter.pushResult();
                    return true;
                }
                return false;
            }
            
            @Override
            public int serializedSize() {
                int size = 0;
                size += this.tax.serializedSize();
                return size;
            }
            
            @Override
            public final String toString() {
                final StringBuilder repr = new StringBuilder();
                this.internalToString(repr, "");
                return repr.toString();
            }
            
            public final void internalToString(final StringBuilder repr, final String prefix) {
                repr.append(prefix).append("tax=...\n");
                this.tax.internalToString(repr, prefix + "  ");
            }
            
            private final class TaxValueConverter
            {
                private final RawTax tax;
                
                private TaxValueConverter() {
                    super();
                    this.tax = new RawTax();
                }
                
                public void pushResult() {
                    TaxValue.this.tax.taxContext = this.tax.taxContext;
                    TaxValue.this.tax.taxValue = this.tax.taxValue;
                }
                
                private boolean unserialize_v0(final ByteBuffer buffer) {
                    final boolean tax_ok = this.tax.unserializeVersion(buffer, 0);
                    return tax_ok;
                }
                
                public void convert_v0_to_v1() {
                }
                
                public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                    if (version < 0) {
                        return false;
                    }
                    if (version >= 1) {
                        return false;
                    }
                    final boolean ok = this.unserialize_v0(buffer);
                    if (ok) {
                        this.convert_v0_to_v1();
                        return true;
                    }
                    return false;
                }
            }
        }
        
        private final class TaxesConverter
        {
            private final ArrayList<TaxValue> taxValues;
            
            private TaxesConverter() {
                super();
                this.taxValues = new ArrayList<TaxValue>(0);
            }
            
            public void pushResult() {
                Taxes.this.taxValues.clear();
                Taxes.this.taxValues.ensureCapacity(this.taxValues.size());
                Taxes.this.taxValues.addAll(this.taxValues);
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                final int taxValues_size = buffer.getShort() & 0xFFFF;
                this.taxValues.clear();
                this.taxValues.ensureCapacity(taxValues_size);
                for (int i = 0; i < taxValues_size; ++i) {
                    final TaxValue taxValues_element = new TaxValue();
                    final boolean taxValues_element_ok = taxValues_element.unserializeVersion(buffer, 0);
                    if (!taxValues_element_ok) {
                        return false;
                    }
                    this.taxValues.add(taxValues_element);
                }
                return true;
            }
            
            public void convert_v0_to_v1() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 0) {
                    return false;
                }
                if (version >= 1) {
                    return false;
                }
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class WeatherModifiers implements VersionableObject
    {
        public final ArrayList<WeatherModifier> modifiers;
        
        public WeatherModifiers() {
            super();
            this.modifiers = new ArrayList<WeatherModifier>(0);
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.modifiers.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.modifiers.size());
            for (int i = 0; i < this.modifiers.size(); ++i) {
                final WeatherModifier modifiers_element = this.modifiers.get(i);
                final boolean modifiers_element_ok = modifiers_element.serialize(buffer);
                if (!modifiers_element_ok) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int modifiers_size = buffer.getShort() & 0xFFFF;
            this.modifiers.clear();
            this.modifiers.ensureCapacity(modifiers_size);
            for (int i = 0; i < modifiers_size; ++i) {
                final WeatherModifier modifiers_element = new WeatherModifier();
                final boolean modifiers_element_ok = modifiers_element.unserialize(buffer);
                if (!modifiers_element_ok) {
                    return false;
                }
                this.modifiers.add(modifiers_element);
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.modifiers.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 2;
            for (int i = 0; i < this.modifiers.size(); ++i) {
                final WeatherModifier modifiers_element = this.modifiers.get(i);
                size += modifiers_element.serializedSize();
            }
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("modifiers=");
            if (this.modifiers.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.modifiers.size()).append(" elements)...\n");
                for (int i = 0; i < this.modifiers.size(); ++i) {
                    final WeatherModifier modifiers_element = this.modifiers.get(i);
                    modifiers_element.internalToString(repr, prefix + i + "/ ");
                }
            }
        }
        
        public static class WeatherModifier implements VersionableObject
        {
            public long applicationDate;
            public int climateBonusId;
            public static final int SERIALIZED_SIZE = 12;
            
            public WeatherModifier() {
                super();
                this.applicationDate = 0L;
                this.climateBonusId = 0;
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                buffer.putLong(this.applicationDate);
                buffer.putInt(this.climateBonusId);
                return true;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                this.applicationDate = buffer.getLong();
                this.climateBonusId = buffer.getInt();
                return true;
            }
            
            @Override
            public void clear() {
                this.applicationDate = 0L;
                this.climateBonusId = 0;
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                return this.unserialize(buffer);
            }
            
            @Override
            public int serializedSize() {
                return 12;
            }
            
            @Override
            public final String toString() {
                final StringBuilder repr = new StringBuilder();
                this.internalToString(repr, "");
                return repr.toString();
            }
            
            public final void internalToString(final StringBuilder repr, final String prefix) {
                repr.append(prefix).append("applicationDate=").append(this.applicationDate).append('\n');
                repr.append(prefix).append("climateBonusId=").append(this.climateBonusId).append('\n');
            }
        }
    }
    
    public static class Satisfaction implements VersionableObject
    {
        public byte satisfactionLevel;
        public static final int SERIALIZED_SIZE = 1;
        
        public Satisfaction() {
            super();
            this.satisfactionLevel = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.satisfactionLevel);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.satisfactionLevel = buffer.get();
            return true;
        }
        
        @Override
        public void clear() {
            this.satisfactionLevel = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 1;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("satisfactionLevel=").append(this.satisfactionLevel).append('\n');
        }
    }
    
    public static class MonsterTargets implements VersionableObject
    {
        public final ArrayList<MonsterTarget> targets;
        
        public MonsterTargets() {
            super();
            this.targets = new ArrayList<MonsterTarget>(0);
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.targets.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.targets.size());
            for (int i = 0; i < this.targets.size(); ++i) {
                final MonsterTarget targets_element = this.targets.get(i);
                final boolean targets_element_ok = targets_element.serialize(buffer);
                if (!targets_element_ok) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int targets_size = buffer.getShort() & 0xFFFF;
            this.targets.clear();
            this.targets.ensureCapacity(targets_size);
            for (int i = 0; i < targets_size; ++i) {
                final MonsterTarget targets_element = new MonsterTarget();
                final boolean targets_element_ok = targets_element.unserialize(buffer);
                if (!targets_element_ok) {
                    return false;
                }
                this.targets.add(targets_element);
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.targets.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 2;
            for (int i = 0; i < this.targets.size(); ++i) {
                final MonsterTarget targets_element = this.targets.get(i);
                size += targets_element.serializedSize();
            }
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("targets=");
            if (this.targets.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.targets.size()).append(" elements)...\n");
                for (int i = 0; i < this.targets.size(); ++i) {
                    final MonsterTarget targets_element = this.targets.get(i);
                    targets_element.internalToString(repr, prefix + i + "/ ");
                }
            }
        }
        
        public static class MonsterTarget implements VersionableObject
        {
            public final RawEcosystemTarget target;
            
            public MonsterTarget() {
                super();
                this.target = new RawEcosystemTarget();
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                final boolean target_ok = this.target.serialize(buffer);
                return target_ok;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                final boolean target_ok = this.target.unserialize(buffer);
                return target_ok;
            }
            
            @Override
            public void clear() {
                this.target.clear();
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                return this.unserialize(buffer);
            }
            
            @Override
            public int serializedSize() {
                int size = 0;
                size += this.target.serializedSize();
                return size;
            }
            
            @Override
            public final String toString() {
                final StringBuilder repr = new StringBuilder();
                this.internalToString(repr, "");
                return repr.toString();
            }
            
            public final void internalToString(final StringBuilder repr, final String prefix) {
                repr.append(prefix).append("target=...\n");
                this.target.internalToString(repr, prefix + "  ");
            }
        }
    }
    
    public static class ResourceTargets implements VersionableObject
    {
        public final ArrayList<ResourceTarget> targets;
        
        public ResourceTargets() {
            super();
            this.targets = new ArrayList<ResourceTarget>(0);
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.targets.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.targets.size());
            for (int i = 0; i < this.targets.size(); ++i) {
                final ResourceTarget targets_element = this.targets.get(i);
                final boolean targets_element_ok = targets_element.serialize(buffer);
                if (!targets_element_ok) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int targets_size = buffer.getShort() & 0xFFFF;
            this.targets.clear();
            this.targets.ensureCapacity(targets_size);
            for (int i = 0; i < targets_size; ++i) {
                final ResourceTarget targets_element = new ResourceTarget();
                final boolean targets_element_ok = targets_element.unserialize(buffer);
                if (!targets_element_ok) {
                    return false;
                }
                this.targets.add(targets_element);
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.targets.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 2;
            for (int i = 0; i < this.targets.size(); ++i) {
                final ResourceTarget targets_element = this.targets.get(i);
                size += targets_element.serializedSize();
            }
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("targets=");
            if (this.targets.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.targets.size()).append(" elements)...\n");
                for (int i = 0; i < this.targets.size(); ++i) {
                    final ResourceTarget targets_element = this.targets.get(i);
                    targets_element.internalToString(repr, prefix + i + "/ ");
                }
            }
        }
        
        public static class ResourceTarget implements VersionableObject
        {
            public final RawEcosystemTarget target;
            
            public ResourceTarget() {
                super();
                this.target = new RawEcosystemTarget();
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                final boolean target_ok = this.target.serialize(buffer);
                return target_ok;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                final boolean target_ok = this.target.unserialize(buffer);
                return target_ok;
            }
            
            @Override
            public void clear() {
                this.target.clear();
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                return this.unserialize(buffer);
            }
            
            @Override
            public int serializedSize() {
                int size = 0;
                size += this.target.serializedSize();
                return size;
            }
            
            @Override
            public final String toString() {
                final StringBuilder repr = new StringBuilder();
                this.internalToString(repr, "");
                return repr.toString();
            }
            
            public final void internalToString(final StringBuilder repr, final String prefix) {
                repr.append(prefix).append("target=...\n");
                this.target.internalToString(repr, prefix + "  ");
            }
        }
    }
    
    public static class Ecosystem implements VersionableObject
    {
        public final ArrayList<MonsterFamily> protectedMonsters;
        public final ArrayList<ResourceFamily> protectedResources;
        
        public Ecosystem() {
            super();
            this.protectedMonsters = new ArrayList<MonsterFamily>(0);
            this.protectedResources = new ArrayList<ResourceFamily>(0);
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.protectedMonsters.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.protectedMonsters.size());
            for (int i = 0; i < this.protectedMonsters.size(); ++i) {
                final MonsterFamily protectedMonsters_element = this.protectedMonsters.get(i);
                final boolean protectedMonsters_element_ok = protectedMonsters_element.serialize(buffer);
                if (!protectedMonsters_element_ok) {
                    return false;
                }
            }
            if (this.protectedResources.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.protectedResources.size());
            for (int i = 0; i < this.protectedResources.size(); ++i) {
                final ResourceFamily protectedResources_element = this.protectedResources.get(i);
                final boolean protectedResources_element_ok = protectedResources_element.serialize(buffer);
                if (!protectedResources_element_ok) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int protectedMonsters_size = buffer.getShort() & 0xFFFF;
            this.protectedMonsters.clear();
            this.protectedMonsters.ensureCapacity(protectedMonsters_size);
            for (int i = 0; i < protectedMonsters_size; ++i) {
                final MonsterFamily protectedMonsters_element = new MonsterFamily();
                final boolean protectedMonsters_element_ok = protectedMonsters_element.unserialize(buffer);
                if (!protectedMonsters_element_ok) {
                    return false;
                }
                this.protectedMonsters.add(protectedMonsters_element);
            }
            final int protectedResources_size = buffer.getShort() & 0xFFFF;
            this.protectedResources.clear();
            this.protectedResources.ensureCapacity(protectedResources_size);
            for (int j = 0; j < protectedResources_size; ++j) {
                final ResourceFamily protectedResources_element = new ResourceFamily();
                final boolean protectedResources_element_ok = protectedResources_element.unserialize(buffer);
                if (!protectedResources_element_ok) {
                    return false;
                }
                this.protectedResources.add(protectedResources_element);
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.protectedMonsters.clear();
            this.protectedResources.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 2;
            for (int i = 0; i < this.protectedMonsters.size(); ++i) {
                final MonsterFamily protectedMonsters_element = this.protectedMonsters.get(i);
                size += protectedMonsters_element.serializedSize();
            }
            size += 2;
            for (int i = 0; i < this.protectedResources.size(); ++i) {
                final ResourceFamily protectedResources_element = this.protectedResources.get(i);
                size += protectedResources_element.serializedSize();
            }
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("protectedMonsters=");
            if (this.protectedMonsters.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.protectedMonsters.size()).append(" elements)...\n");
                for (int i = 0; i < this.protectedMonsters.size(); ++i) {
                    final MonsterFamily protectedMonsters_element = this.protectedMonsters.get(i);
                    protectedMonsters_element.internalToString(repr, prefix + i + "/ ");
                }
            }
            repr.append(prefix).append("protectedResources=");
            if (this.protectedResources.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.protectedResources.size()).append(" elements)...\n");
                for (int i = 0; i < this.protectedResources.size(); ++i) {
                    final ResourceFamily protectedResources_element = this.protectedResources.get(i);
                    protectedResources_element.internalToString(repr, prefix + i + "/ ");
                }
            }
        }
        
        public static class MonsterFamily implements VersionableObject
        {
            public int familyId;
            public static final int SERIALIZED_SIZE = 4;
            
            public MonsterFamily() {
                super();
                this.familyId = 0;
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                buffer.putInt(this.familyId);
                return true;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                this.familyId = buffer.getInt();
                return true;
            }
            
            @Override
            public void clear() {
                this.familyId = 0;
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                return this.unserialize(buffer);
            }
            
            @Override
            public int serializedSize() {
                return 4;
            }
            
            @Override
            public final String toString() {
                final StringBuilder repr = new StringBuilder();
                this.internalToString(repr, "");
                return repr.toString();
            }
            
            public final void internalToString(final StringBuilder repr, final String prefix) {
                repr.append(prefix).append("familyId=").append(this.familyId).append('\n');
            }
        }
        
        public static class ResourceFamily implements VersionableObject
        {
            public int familyId;
            public static final int SERIALIZED_SIZE = 4;
            
            public ResourceFamily() {
                super();
                this.familyId = 0;
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                buffer.putInt(this.familyId);
                return true;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                this.familyId = buffer.getInt();
                return true;
            }
            
            @Override
            public void clear() {
                this.familyId = 0;
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                return this.unserialize(buffer);
            }
            
            @Override
            public int serializedSize() {
                return 4;
            }
            
            @Override
            public final String toString() {
                final StringBuilder repr = new StringBuilder();
                this.internalToString(repr, "");
                return repr.toString();
            }
            
            public final void internalToString(final StringBuilder repr, final String prefix) {
                repr.append(prefix).append("familyId=").append(this.familyId).append('\n');
            }
        }
    }
    
    private final class RawProtectorConverter
    {
        private int protectorId;
        private Nationality nationality;
        private Appearance appearance;
        private Challenges challenges;
        private ReferenceMerchantInventories referenceMerchantInventories;
        private NationMerchantInventories nationMerchantInventories;
        private Wallet wallet;
        private Stake stake;
        private Taxes taxes;
        private WeatherModifiers weatherModifiers;
        private Satisfaction satisfaction;
        private MonsterTargets monsterTargets;
        private ResourceTargets resourceTargets;
        private Ecosystem ecosystem;
        
        private RawProtectorConverter() {
            super();
            this.protectorId = 0;
            this.nationality = null;
            this.appearance = null;
            this.challenges = null;
            this.referenceMerchantInventories = null;
            this.nationMerchantInventories = null;
            this.wallet = null;
            this.stake = null;
            this.taxes = null;
            this.weatherModifiers = null;
            this.satisfaction = null;
            this.monsterTargets = null;
            this.resourceTargets = null;
            this.ecosystem = null;
        }
        
        public void pushResult() {
            RawProtector.this.protectorId = this.protectorId;
            RawProtector.this.nationality = this.nationality;
            RawProtector.this.appearance = this.appearance;
            RawProtector.this.challenges = this.challenges;
            RawProtector.this.referenceMerchantInventories = this.referenceMerchantInventories;
            RawProtector.this.nationMerchantInventories = this.nationMerchantInventories;
            RawProtector.this.wallet = this.wallet;
            RawProtector.this.stake = this.stake;
            RawProtector.this.taxes = this.taxes;
            RawProtector.this.weatherModifiers = this.weatherModifiers;
            RawProtector.this.satisfaction = this.satisfaction;
            RawProtector.this.monsterTargets = this.monsterTargets;
            RawProtector.this.resourceTargets = this.resourceTargets;
            RawProtector.this.ecosystem = this.ecosystem;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 1);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 1);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 1);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 1);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 1);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 1);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 1);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 1);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 1);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 1);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 1);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 1);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 1);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 313);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 313);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 313);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 313);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 313);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 313);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 313);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 313);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 313);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 313);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 313);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 313);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 313);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 314);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 314);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 314);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 314);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 314);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 314);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 314);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 314);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 314);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 314);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 314);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 314);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 314);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 315);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 315);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 315);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 315);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 315);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 315);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 315);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 315);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 315);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 315);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 315);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 315);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 315);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 10003);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 10003);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 10003);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 10003);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 10003);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10003);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 10003);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 10003);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 10003);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 10003);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 10003);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 10003);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10003);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 10023);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 10023);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 10023);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 10023);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 10023);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10023);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 10023);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 10023);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 10023);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 10023);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 10023);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 10023);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10023);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 10028000);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 10028000);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 10028000);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 10028000);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 10028000);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10028000);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 10028000);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 10028000);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 10028000);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 10028000);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 10028000);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 10028000);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10028000);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 10029000);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 10029000);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 10029000);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 10029000);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 10029000);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10029000);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 10029000);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 10029000);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 10029000);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 10029000);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 10029000);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 10029000);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10029000);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 10032003);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 10032003);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 10032003);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 10032003);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 10032003);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10032003);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 10032003);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 10032003);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 10032003);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 10032003);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 10032003);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 10032003);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10032003);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 10035004);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 10035004);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 10035004);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 10035004);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 10035004);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10035004);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 10035004);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 10035004);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 10035004);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 10035004);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 10035004);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 10035004);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10035004);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 10035007);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 10035007);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 10035007);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 10035007);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 10035007);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10035007);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 10035007);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 10035007);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 10035007);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 10035007);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 10035007);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 10035007);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10035007);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            this.protectorId = buffer.getInt();
            final boolean nationality_present = buffer.get() == 1;
            if (nationality_present) {
                this.nationality = new Nationality();
                final boolean nationality_ok = this.nationality.unserializeVersion(buffer, 10036004);
                if (!nationality_ok) {
                    return false;
                }
            }
            else {
                this.nationality = null;
            }
            final boolean appearance_present = buffer.get() == 1;
            if (appearance_present) {
                this.appearance = new Appearance();
                final boolean appearance_ok = this.appearance.unserializeVersion(buffer, 10036004);
                if (!appearance_ok) {
                    return false;
                }
            }
            else {
                this.appearance = null;
            }
            final boolean challenges_present = buffer.get() == 1;
            if (challenges_present) {
                this.challenges = new Challenges();
                final boolean challenges_ok = this.challenges.unserializeVersion(buffer, 10036004);
                if (!challenges_ok) {
                    return false;
                }
            }
            else {
                this.challenges = null;
            }
            final boolean referenceMerchantInventories_present = buffer.get() == 1;
            if (referenceMerchantInventories_present) {
                this.referenceMerchantInventories = new ReferenceMerchantInventories();
                final boolean referenceMerchantInventories_ok = this.referenceMerchantInventories.unserializeVersion(buffer, 10036004);
                if (!referenceMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.referenceMerchantInventories = null;
            }
            final boolean nationMerchantInventories_present = buffer.get() == 1;
            if (nationMerchantInventories_present) {
                this.nationMerchantInventories = new NationMerchantInventories();
                final boolean nationMerchantInventories_ok = this.nationMerchantInventories.unserializeVersion(buffer, 10036004);
                if (!nationMerchantInventories_ok) {
                    return false;
                }
            }
            else {
                this.nationMerchantInventories = null;
            }
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10036004);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean stake_present = buffer.get() == 1;
            if (stake_present) {
                this.stake = new Stake();
                final boolean stake_ok = this.stake.unserializeVersion(buffer, 10036004);
                if (!stake_ok) {
                    return false;
                }
            }
            else {
                this.stake = null;
            }
            final boolean taxes_present = buffer.get() == 1;
            if (taxes_present) {
                this.taxes = new Taxes();
                final boolean taxes_ok = this.taxes.unserializeVersion(buffer, 10036004);
                if (!taxes_ok) {
                    return false;
                }
            }
            else {
                this.taxes = null;
            }
            final boolean weatherModifiers_present = buffer.get() == 1;
            if (weatherModifiers_present) {
                this.weatherModifiers = new WeatherModifiers();
                final boolean weatherModifiers_ok = this.weatherModifiers.unserializeVersion(buffer, 10036004);
                if (!weatherModifiers_ok) {
                    return false;
                }
            }
            else {
                this.weatherModifiers = null;
            }
            final boolean satisfaction_present = buffer.get() == 1;
            if (satisfaction_present) {
                this.satisfaction = new Satisfaction();
                final boolean satisfaction_ok = this.satisfaction.unserializeVersion(buffer, 10036004);
                if (!satisfaction_ok) {
                    return false;
                }
            }
            else {
                this.satisfaction = null;
            }
            final boolean monsterTargets_present = buffer.get() == 1;
            if (monsterTargets_present) {
                this.monsterTargets = new MonsterTargets();
                final boolean monsterTargets_ok = this.monsterTargets.unserializeVersion(buffer, 10036004);
                if (!monsterTargets_ok) {
                    return false;
                }
            }
            else {
                this.monsterTargets = null;
            }
            final boolean resourceTargets_present = buffer.get() == 1;
            if (resourceTargets_present) {
                this.resourceTargets = new ResourceTargets();
                final boolean resourceTargets_ok = this.resourceTargets.unserializeVersion(buffer, 10036004);
                if (!resourceTargets_ok) {
                    return false;
                }
            }
            else {
                this.resourceTargets = null;
            }
            final boolean ecosystem_present = buffer.get() == 1;
            if (ecosystem_present) {
                this.ecosystem = new Ecosystem();
                final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10036004);
                if (!ecosystem_ok) {
                    return false;
                }
            }
            else {
                this.ecosystem = null;
            }
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v313() {
        }
        
        public void convert_v313_to_v314() {
        }
        
        public void convert_v314_to_v315() {
        }
        
        public void convert_v315_to_v10003() {
        }
        
        public void convert_v10003_to_v10023() {
        }
        
        public void convert_v10023_to_v10028000() {
        }
        
        public void convert_v10028000_to_v10029000() {
        }
        
        public void convert_v10029000_to_v10032003() {
        }
        
        public void convert_v10032003_to_v10035004() {
        }
        
        public void convert_v10035004_to_v10035007() {
        }
        
        public void convert_v10035007_to_v10036004() {
        }
        
        public void convert_v10036004_to_v10037001() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 1) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    this.convert_v1_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 313) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 314) {
                final boolean ok = this.unserialize_v313(buffer);
                if (ok) {
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 315) {
                final boolean ok = this.unserialize_v314(buffer);
                if (ok) {
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10003) {
                final boolean ok = this.unserialize_v315(buffer);
                if (ok) {
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10023) {
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10028000) {
                final boolean ok = this.unserialize_v10023(buffer);
                if (ok) {
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10029000) {
                final boolean ok = this.unserialize_v10028000(buffer);
                if (ok) {
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10032003) {
                final boolean ok = this.unserialize_v10029000(buffer);
                if (ok) {
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10035004) {
                final boolean ok = this.unserialize_v10032003(buffer);
                if (ok) {
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10035007) {
                final boolean ok = this.unserialize_v10035004(buffer);
                if (ok) {
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10036004) {
                final boolean ok = this.unserialize_v10035007(buffer);
                if (ok) {
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10037001) {
                    return false;
                }
                final boolean ok = this.unserialize_v10036004(buffer);
                if (ok) {
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
        }
    }
}
