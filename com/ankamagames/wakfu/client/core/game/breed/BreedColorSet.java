package com.ankamagames.wakfu.client.core.game.breed;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.byteKey.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.colors.*;

public class BreedColorSet
{
    private final byte m_defaultSkinColorIndex;
    private final byte m_defaultSkinColorFactor;
    private final byte m_defaultHairColorIndex;
    private final byte m_defaultHairColorFactor;
    private final byte m_defaultPupilColorIndex;
    private final ByteObjectLightWeightMap<CharacterColor> m_skinColors;
    private final ByteObjectLightWeightMap<CharacterColor> m_hairColors;
    private final ByteObjectLightWeightMap<CharacterColor> m_pupilColors;
    
    public BreedColorSet(final byte defaultSkinColorIndex, final byte defaultSkinColorFactor, final byte defaultHairColorIndex, final byte defaultHairColorFactor, final byte defaultPupilColorIndex, final ByteObjectLightWeightMap<CharacterColor> skinColors, final ByteObjectLightWeightMap<CharacterColor> hairColors, final ByteObjectLightWeightMap<CharacterColor> pupilColors) {
        super();
        this.m_defaultSkinColorIndex = defaultSkinColorIndex;
        this.m_defaultSkinColorFactor = defaultSkinColorFactor;
        this.m_defaultHairColorIndex = defaultHairColorIndex;
        this.m_defaultHairColorFactor = defaultHairColorFactor;
        this.m_defaultPupilColorIndex = defaultPupilColorIndex;
        this.m_skinColors = skinColors;
        this.m_hairColors = hairColors;
        this.m_pupilColors = pupilColors;
    }
    
    byte getDefaultSkinColorIndex() {
        return this.m_defaultSkinColorIndex;
    }
    
    byte getDefaultSkinColorFactor() {
        return this.m_defaultSkinColorFactor;
    }
    
    byte getDefaultHairColorIndex() {
        return this.m_defaultHairColorIndex;
    }
    
    byte getDefaultHairColorFactor() {
        return this.m_defaultHairColorFactor;
    }
    
    byte getDefaultPupilColorIndex() {
        return this.m_defaultPupilColorIndex;
    }
    
    CharacterColor getSkinColor(final byte index) {
        return this.m_skinColors.get(index);
    }
    
    CharacterColor getHairColor(final byte index) {
        return this.m_hairColors.get(index);
    }
    
    CharacterColor getPupilColor(final byte index) {
        return this.m_pupilColors.get(index);
    }
    
    private static CharacterColor[] getColors(final ByteObjectLightWeightMap<CharacterColor> colorsMap) {
        final int mapSize = colorsMap.size();
        final CharacterColor[] characterColors = new CharacterColor[mapSize];
        for (byte i = 0; i < mapSize; ++i) {
            characterColors[i] = colorsMap.get(i);
        }
        return characterColors;
    }
    
    CharacterColor[] getSkinColors() {
        return getColors(this.m_skinColors);
    }
    
    CharacterColor[] getHairColors() {
        return getColors(this.m_hairColors);
    }
    
    CharacterColor[] getPupilColors() {
        return getColors(this.m_pupilColors);
    }
    
    public boolean hasSkinColor() {
        return this.m_skinColors.size() != 0;
    }
    
    public boolean hasHairColor() {
        return this.m_hairColors.size() != 0;
    }
    
    public boolean hasPupilColor() {
        return this.m_pupilColors.size() != 0;
    }
}
