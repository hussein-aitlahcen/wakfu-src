package com.ankamagames.wakfu.common.game.world;

import com.ankamagames.framework.external.*;

public enum ResourceSizeCategory implements ExportableEnum
{
    SMALL(0, false, 10, false, 0, 0.0f, "2  [taille <  perso / vent /       / sous   le perso]"), 
    MEDIUM(1, false, 30, true, 3, 1.0471976f, "3  [taille ~= perso / vent / ombre / devant le perso]"), 
    TALL(2, true, 80, true, 5, 1.5707964f, "4  [taille >  perso /      / ombre / devant le perso]"), 
    TINY(3, false, 10, false, 0, 0.0f, "1  [ras du sol      /      /       / sous   le perso]"), 
    SMALL2(4, false, 10, false, 0, 0.0f, "2b [taille < perso  / vent /       / devant le perso]"), 
    MEDIUM2(5, false, 30, true, 3, 1.0471976f, "3b [taille ~= perso /      / ombre / devant le perso]");
    
    private final int m_id;
    private final boolean m_isOccluder;
    private final int m_height;
    private final boolean m_isShadowed;
    private final int m_shadowLength;
    private final float m_shadowWidth;
    private final String m_label;
    
    private ResourceSizeCategory(final int id, final boolean isOccluder, final int height, final boolean isShadowed, final int shadowLength, final float shadowWidth, final String label) {
        this.m_id = id;
        this.m_isOccluder = isOccluder;
        this.m_height = height;
        this.m_isShadowed = isShadowed;
        this.m_shadowLength = shadowLength;
        this.m_shadowWidth = shadowWidth;
        this.m_label = label;
    }
    
    public final boolean isOccluder() {
        return this.m_isOccluder;
    }
    
    public final int getHeight() {
        return this.m_height;
    }
    
    public final boolean isShadowed() {
        return this.m_isShadowed;
    }
    
    public final int getShadowLength() {
        return this.m_shadowLength;
    }
    
    public final float getShadowWidth() {
        return this.m_shadowWidth;
    }
    
    @Override
    public final String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public final String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static boolean underMobile(final ResourceSizeCategory category) {
        return category == ResourceSizeCategory.TINY || category == ResourceSizeCategory.SMALL;
    }
    
    public static boolean applyWind(final ResourceSizeCategory category) {
        return category == ResourceSizeCategory.SMALL || category == ResourceSizeCategory.SMALL2 || category == ResourceSizeCategory.MEDIUM;
    }
}
