package com.ankamagames.baseImpl.graphics.alea.animatedElement;

import java.io.*;

public class AnimationConstants
{
    public static final String ANIMATION_PREFIX = "Anim";
    public static final String SUFFIX_ANIM_START = "-Debut";
    public static final String SUFFIX_ANIM_END = "-Fin";
    public static final String ANIM_STATIC = "AnimStatique";
    public static final String ANIM_JUMP = "AnimSaut";
    public static final String ANIM_JUMP_SLOW = "AnimSaut-Marche";
    public static final String ANIM_MOVE = "AnimCourse";
    public static final String ANIM_MOVE_END = "AnimCourse-Fin";
    public static final String ANIM_WALK = "AnimMarche";
    public static final String ANIM_HIT = "AnimHit";
    public static final String HAMECON_ANIM_FILENAME = "AnimHamecon";
    public static final String ANIM_HAMECON_LINK = "Accessoire";
    public static final String ANIM_GUILD_LOGO = "blason";
    public static final String ANIM_GUILD_LOGO_BG = "blason_bg";
    public static final String ANIM_RELIC_PREFIX = "AnimRelique";
    public static final String ANIM_RELIC_JUMP = "AnimRelique-Saut";
    
    public static String setAnimationEndSuffix(final String animationName, final boolean append) {
        final StringBuilder animation = new StringBuilder(animationName);
        final int index = animation.lastIndexOf("-");
        if (index == -1) {
            if (append) {
                animation.append("-Fin");
            }
        }
        else {
            animation.replace(index, animation.length(), "-Fin");
        }
        return animation.toString();
    }
}
