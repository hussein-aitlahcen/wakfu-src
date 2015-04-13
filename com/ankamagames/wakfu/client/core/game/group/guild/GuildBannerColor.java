package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.framework.graphics.image.*;
import java.util.*;

public class GuildBannerColor
{
    public static final int[] COLORS;
    private static final GuildBannerColor m_instance;
    private final ArrayList<Color> m_colors;
    
    private GuildBannerColor() {
        super();
        this.m_colors = new ArrayList<Color>();
        for (int numColor = GuildBannerColor.COLORS.length / 3, i = 0; i < numColor; ++i) {
            final Color c1 = new Color(GuildBannerColor.COLORS[i * 3], GuildBannerColor.COLORS[i * 3 + 1], GuildBannerColor.COLORS[i * 3 + 2], 255);
            this.m_colors.add(c1);
            for (float j = 0.8f; j >= 0.4f; j -= 0.2f) {
                final Color c2 = new Color(c1);
                c2.setIntensity(c2.getMaxRGBComponent() * j);
                this.m_colors.add(c2);
            }
        }
    }
    
    public static GuildBannerColor getInstance() {
        return GuildBannerColor.m_instance;
    }
    
    public int getNumColors() {
        return this.m_colors.size();
    }
    
    public short indexOf(final Color c) {
        return (short)this.m_colors.indexOf(c);
    }
    
    public Color getColor(final int index) {
        return this.m_colors.get(index);
    }
    
    public ArrayList<Color> getColors() {
        return this.m_colors;
    }
    
    static {
        COLORS = new int[] { 255, 255, 255, 106, 106, 106, 255, 128, 128, 255, 0, 0, 255, 128, 192, 255, 0, 128, 255, 128, 255, 255, 0, 255, 128, 0, 255, 192, 128, 255, 128, 128, 255, 0, 0, 255, 0, 128, 255, 128, 192, 255, 128, 255, 255, 0, 255, 255, 0, 255, 128, 128, 255, 192, 128, 255, 128, 0, 255, 0, 128, 255, 0, 192, 255, 128, 255, 255, 128, 255, 255, 0, 255, 128, 0, 255, 192, 128 };
        m_instance = new GuildBannerColor();
    }
    
    private static class ColorIntensityComparator implements Comparator<Color>
    {
        private static ColorIntensityComparator INSTANCE;
        
        @Override
        public int compare(final Color o1, final Color o2) {
            int max1;
            int value1;
            if (o1.getRed() >= o1.getBlue() && o1.getRed() >= o1.getGreen()) {
                max1 = 0;
                value1 = o1.getGreenInt() - o1.getBlueInt();
            }
            else if (o1.getGreen() >= o1.getBlue()) {
                max1 = 1;
                value1 = o1.getBlueInt() - o1.getRedInt();
            }
            else {
                max1 = 2;
                value1 = o1.getRedInt() - o1.getBlueInt();
            }
            int max2;
            int value2;
            if (o2.getRed() >= o2.getBlue() && o2.getRed() >= o2.getGreen()) {
                max2 = 0;
                value2 = o2.getGreenInt() - o2.getBlueInt();
            }
            else if (o2.getGreen() >= o2.getBlue()) {
                max2 = 1;
                value2 = o2.getBlueInt() - o2.getRedInt();
            }
            else {
                max2 = 2;
                value2 = o2.getRedInt() - o2.getBlueInt();
            }
            if (max1 != max2) {
                return max1 - max2;
            }
            return value1 - value2;
        }
        
        static {
            ColorIntensityComparator.INSTANCE = new ColorIntensityComparator();
        }
    }
}
