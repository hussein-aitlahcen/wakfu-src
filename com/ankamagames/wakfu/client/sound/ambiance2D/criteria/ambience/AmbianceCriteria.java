package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.*;

public enum AmbianceCriteria
{
    SEASON((byte)0, (ContainerCriterionFactory)new ContainerCriterionFactory() {
        @Override
        public ContainerCriterion createCriterion() {
            final SeasonCriterion seasonCriterion = new SeasonCriterion();
            seasonCriterion.setSeason(Season.SPRING);
            return seasonCriterion;
        }
    }), 
    TEMPERATURE((byte)1, (ContainerCriterionFactory)new ContainerCriterionFactory() {
        @Override
        public ContainerCriterion createCriterion() {
            return new TemperatureCriterion();
        }
    }), 
    TIME_OF_DAY((byte)2, (ContainerCriterionFactory)new ContainerCriterionFactory() {
        @Override
        public ContainerCriterion createCriterion() {
            return new TimeOfDayCriterion();
        }
    }), 
    ZONE_TYPE((byte)3, (ContainerCriterionFactory)new ContainerCriterionFactory() {
        @Override
        public ContainerCriterion createCriterion() {
            final ZoneTypeCriterion criterion = new ZoneTypeCriterion();
            criterion.setZoneType(AmbianceZoneType.PLAIN);
            return criterion;
        }
    }), 
    WAKFU((byte)4, (ContainerCriterionFactory)new ContainerCriterionFactory() {
        @Override
        public ContainerCriterion createCriterion() {
            return new WakfuCriterion();
        }
    }), 
    AND((byte)6, (ContainerCriterionFactory)new ContainerCriterionFactory() {
        @Override
        public ContainerCriterion createCriterion() {
            return new AndCriterion();
        }
    }), 
    OR((byte)5, (ContainerCriterionFactory)new ContainerCriterionFactory() {
        @Override
        public ContainerCriterion createCriterion() {
            return new OrCriterion();
        }
    }), 
    NUM_PLAYERS((byte)7, (ContainerCriterionFactory)new ContainerCriterionFactory() {
        @Override
        public ContainerCriterion createCriterion() {
            return new NumPlayersCriterion();
        }
    }), 
    ALTITUDE((byte)8, (ContainerCriterionFactory)new ContainerCriterionFactory() {
        @Override
        public ContainerCriterion createCriterion() {
            return new AltitudeCriterion();
        }
    });
    
    private byte m_id;
    private ContainerCriterionFactory m_factory;
    
    private AmbianceCriteria(final byte id, final ContainerCriterionFactory factory) {
        this.m_id = id;
        this.m_factory = factory;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public ContainerCriterion createCriterion() {
        return this.m_factory.createCriterion();
    }
    
    public static AmbianceCriteria getById(final byte id) {
        for (final AmbianceCriteria crit : values()) {
            if (crit.m_id == id) {
                return crit;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        switch (this) {
            case SEASON: {
                return "Saison";
            }
            case TEMPERATURE: {
                return "Temp\u00e9rature";
            }
            case TIME_OF_DAY: {
                return "P\u00e9riode de la journ\u00e9e";
            }
            case ZONE_TYPE: {
                return "Type de zone";
            }
            case WAKFU: {
                return "Wakfu";
            }
            case AND: {
                return "Et";
            }
            case OR: {
                return "Ou";
            }
            case NUM_PLAYERS: {
                return "Joueurs alentours";
            }
            case ALTITUDE: {
                return "Altitude";
            }
            default: {
                return super.toString();
            }
        }
    }
    
    public static ContainerCriterion read(final ExtendedDataInputStream is) {
        final ContainerCriterion criterion = getById(is.readByte()).createCriterion();
        criterion.load(is);
        return criterion;
    }
    
    public static void write(final OutputBitStream os, final ContainerCriterion criterion) throws IOException {
        os.writeByte(criterion.getCriterionId());
        criterion.save(os);
    }
}
