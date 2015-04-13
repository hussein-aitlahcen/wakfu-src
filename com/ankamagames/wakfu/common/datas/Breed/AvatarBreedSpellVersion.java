package com.ankamagames.wakfu.common.datas.Breed;

public enum AvatarBreedSpellVersion
{
    COMMON(AvatarBreed.COMMON, 0), 
    NONE(AvatarBreed.NONE, 0), 
    FECA(AvatarBreed.FECA, 17), 
    OSAMODAS(AvatarBreed.OSAMODAS, 16), 
    ENUTROF(AvatarBreed.ENUTROF, 15), 
    SRAM(AvatarBreed.SRAM, 13), 
    XELOR(AvatarBreed.XELOR, 14), 
    ECAFLIP(AvatarBreed.ECAFLIP, 16), 
    ENIRIPSA(AvatarBreed.ENIRIPSA, 18), 
    IOP(AvatarBreed.IOP, 13), 
    CRA(AvatarBreed.CRA, 13), 
    SADIDA(AvatarBreed.SADIDA, 16), 
    SACRIER(AvatarBreed.SACRIER, 13), 
    PANDAWA(AvatarBreed.PANDAWA, 18), 
    ROUBLARD(AvatarBreed.ROUBLARD, 14), 
    ZOBAL(AvatarBreed.ZOBAL, 17), 
    OUGINAK(AvatarBreed.OUGINAK, 13), 
    STEAMER(AvatarBreed.STEAMER, 16), 
    ELIATROPE(AvatarBreed.ELIOTROPE, 0), 
    SOUL(AvatarBreed.SOUL, 0);
    
    public static final short APTITUDES_VERSION = 23;
    private final AvatarBreed m_breed;
    private final short m_spellVersion;
    
    private AvatarBreedSpellVersion(final AvatarBreed breed, final int spellVersion) {
        this.m_breed = breed;
        this.m_spellVersion = (short)spellVersion;
    }
    
    public static short getSpellVersion(final int breedId) {
        final AvatarBreedSpellVersion[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final AvatarBreedSpellVersion breed = values[i];
            if (breed.m_breed.getBreedId() == breedId) {
                return breed.m_spellVersion;
            }
        }
        return 0;
    }
}
