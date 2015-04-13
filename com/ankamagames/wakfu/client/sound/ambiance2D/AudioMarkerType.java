package com.ankamagames.wakfu.client.sound.ambiance2D;

import com.ankamagames.framework.external.*;

public enum AudioMarkerType implements ExportableEnum
{
    NONE(0, "Aucun"), 
    FLYS(7, "Insects flys"), 
    FROGS(8, "Frogs"), 
    WEIRD_VOICES_PRAYER(54, "Prayers weird"), 
    SLECK_NEST(55, "Sleck nest"), 
    SLECK_CANVAS(56, "Sleck canvas"), 
    TROOL_ALERT_MESS(85, "Message alert Trool"), 
    BARBECUE(9, "barbecue"), 
    FIRECAMP_BOILING_SMALL(10, "firecamp boiling water small"), 
    FIRECAMP_BOILING_MED(11, "firecamp boiling water med"), 
    FIRECAMP_SMALL(12, "firecamp small"), 
    SMOKESTACK_SMALL(13, "smokestack small"), 
    SMOKESTACK_MED(14, "smokestack med"), 
    VOLCANO_FALL_SMALL(15, "volcano fall small"), 
    VOLCANO_FALL_MED(17, "volcano fall med"), 
    VOLCANO_FALL_MED_DISTANT(18, "volcano fall med distant"), 
    VOLCANO_FALL_BIG(19, "volcano fall big"), 
    VOLCANO_FALL_BIG_DISTANT(20, "volcano fall big distant"), 
    VOLCANO_LAVA_SMALL(21, "volcano lava small"), 
    VOLCANO_LAVA_MED(23, "volcano lava med"), 
    VOLCANO_LAVA_MED_DISTANT(24, "volcano lava med distant"), 
    VOLCANO_LAVA_BIG(25, "volcano lava big"), 
    VOLCANO_LAVA_BIG_DISTANT(26, "volcano lava big distant"), 
    DARK_HOLE(5, "Dark hole"), 
    HIBOSS_BOARD(52, "Hiboss board"), 
    PLASTIC_DUCK(58, "Plastic duck"), 
    GRAVEYARD(59, "Graveyard"), 
    CASINO_MACHINES(60, "Machines casino"), 
    TRAIN_ENGINE(61, "Train engine"), 
    ELIATROPE_TECHNOLOGY(57, "Eliatrope technology"), 
    HUM_MACHINES(63, "Hum machine"), 
    MECHANICAL_SMALL(64, "Mechanical small"), 
    MACHINE_BUBBLE(66, "Machine bubble"), 
    GEAR_ENGINE(73, "Old engine"), 
    MACHINE_OCCLUSION(76, "machine hidden"), 
    MACHINE_VENTILATION(77, "machine ventilation"), 
    ROTOR_BLADE(78, "rotor blade"), 
    COINS(74, "Coins"), 
    CONSTR_DISTANT(84, "Distant construction"), 
    PORTAL_SHUKRUTE(86, "Portal Shukrute"), 
    CHURCH_BELL(65, "Church bells"), 
    METAL_MECHANISM_SMALL(46, "metal maechanism small"), 
    METAL_MECHANISM_MED(47, "Metal maechanism med"), 
    METAL_LOW_SQUEAK(48, "Metal low squeak"), 
    METAL_CREAK_SMALL(49, "Metal creak small"), 
    METAL_CREAK_MED(50, "Metal creak med"), 
    METAL_COINFALL(87, "Metal coinfall"), 
    MUDBUBBLES(2, "Mud bubbles"), 
    MUSIC_CHUCHOTEUR_STELE_AIR(69, "Music chuchoteur stele air"), 
    MUSIC_CHUCHOTEUR_STELE_FIRE(70, "Music chuchoteur stele fire"), 
    MUSIC_CHUCHOTEUR_STELE_EARTH(71, "Music chuchoteur stele earth"), 
    MUSIC_CHUCHOTEUR_STELE_WATER(72, "Music chuchoteur stele water"), 
    MUSIC_BILBIZA_ECRAN_TV(79, "Music Bilyza Ecran tv"), 
    MUSIC_BILBIZA_SHOW_GELAX(80, "Music Bilyza Show Gelax"), 
    MUSIC_BILBIZA_RADEAU(81, "Music Bilyza Radeau de la tentation"), 
    MUSIC_BILBIZA_FULL_GELAX_PARTY(82, "Music Bilbyza Full Gelax party"), 
    MUSIC_BILBIZA_DISCOTHEQUE(83, "Music Bilyza Discotheque"), 
    ROCKFALL(1, "Rocks fall"), 
    ROCKWHISTLE(68, "Rocks whistle"), 
    SFX(4, "FX horror"), 
    SFX_WAKFU(39, "FX wakfu"), 
    SFX_STASIS(40, "FX Stasis"), 
    WIND_TREE(6, "Wind tree"), 
    WIND_INT(41, "Wind int"), 
    WIND_LARGE(44, "Wind large"), 
    STEAM_SMALL(42, "Steam small"), 
    STEAM_MED(43, "Steam med"), 
    TORNADO_OGREST(53, "Tornade Ogrest"), 
    WATER_BOILING_SMALL(27, "Water boiling small"), 
    WATER_BOILING_MED(28, "Water boiling med"), 
    WATER_BOILING_BIG(29, "Water boiling big"), 
    WATER_POOL(38, "Water pool"), 
    WATER_DRIPPING_SMALL(30, "Water dripping small"), 
    WATER_DRIPPING_MED(31, "Water dripping med"), 
    WATER_DRIPPING_BIG(32, "Water dripping big"), 
    WATER_WAVES(33, "Water waves lapping"), 
    WATERFALL_LITTLE(51, "Waterfall little"), 
    WATERFALL_SMALL(34, "Waterfall small"), 
    WATERFALL_MED(35, "Waterfall med"), 
    WATERFALL_MED_DISTANT(36, "Waterfall med distant"), 
    WATERFALL_BIG(37, "Waterfall big"), 
    FALLS(3, "Waterfall big distant"), 
    DROPS(67, "Drops"), 
    WOOD_CREAK_LARGE(45, "Wood creak large"), 
    WOOD_THIN_CREAK(62, "Wood thin creak"), 
    WOOD_OLD_CREAK(75, "Wood old creak");
    
    private final int m_markerType;
    private final String m_description;
    
    private AudioMarkerType(final int markerType, final String desc) {
        this.m_markerType = markerType;
        this.m_description = desc;
    }
    
    public static AudioMarkerType getFromId(final int typeId) {
        for (final AudioMarkerType type : values()) {
            if (type.m_markerType == typeId) {
                return type;
            }
        }
        return null;
    }
    
    public int getId() {
        return this.m_markerType;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_markerType);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_description;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    @Override
    public String toString() {
        return this.getEnumLabel();
    }
}
