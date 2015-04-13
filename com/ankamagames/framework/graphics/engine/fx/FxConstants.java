package com.ankamagames.framework.graphics.engine.fx;

import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.framework.graphics.engine.*;

public final class FxConstants
{
    public static final String TRANSFORM_EFFECTS = "transform";
    public static final String UI_EFFECTS = "gui";
    public static final String POSTPROCESS_EFFECTS = "postprocess";
    public static final int TRANSFORM_TECHNIQUE;
    public static final int ALPHAMASK_TECHNIQUE;
    public static final int ANM_ALPHAMASK_TECHNIQUE;
    public static final int HEATHAZE_TECHNIQUE;
    public static final int BLURX_TECHNIQUE;
    public static final int BLURY_TECHNIQUE;
    public static final int DISTORD_TECHNIQUE;
    public static final int BLOOM_SELECT_TECHNIQUE;
    public static final int BLOOM_TECHNIQUE;
    public static final int HEAT_TECHNIQUE;
    public static final int FIXED_TECHNIQUE;
    public static final int LUMINANCE_TECHNIQUE;
    public static final int INVERT_TECHNIQUE;
    public static final int COLOR_SELECT_TECHNIQUE;
    public static final int BINARIZE_TECHNIQUE;
    public static final int MULTIPLY_TECHNIQUE;
    public static final int NIGHT_TECHNIQUE;
    public static final int BLUR_RADIAL_TECHNIQUE;
    public static final int PIXELIZE_TECHNIQUE;
    public static final int COLOR_MODIFIER_TECHNIQUE;
    public static final int RAW_TECHNIQUE;
    public static final int POST_PROCESS_HEAT_AND_HAZE;
    public static final int POST_PROCESS_BLOOM;
    public static final int POST_PROCESS_HEAT;
    public static final int POST_PROCESS_RADIAL_BLUR;
    public static final int POST_PROCESS_PIXELIZE;
    public static final int POST_PROCESS_DESATURATE;
    public static final int ALPHA_MASK = 1;
    public static final int OCCLUDER = 2;
    public static final Variable[] ALPHAMASK_VARS;
    public static final EffectParams COLOR_SCALE_FOR_WORLD_PARAMS;
    public static final EffectParams COLOR_SCALE_FOR_UI_PARAMS;
    
    static {
        TRANSFORM_TECHNIQUE = Engine.getTechnic("Transform");
        ALPHAMASK_TECHNIQUE = Engine.getTechnic("AlphaMask");
        ANM_ALPHAMASK_TECHNIQUE = Engine.getTechnic("AnmAlphaMask");
        HEATHAZE_TECHNIQUE = Engine.getTechnic("HeatHaze");
        BLURX_TECHNIQUE = Engine.getTechnic("BlurX");
        BLURY_TECHNIQUE = Engine.getTechnic("BlurY");
        DISTORD_TECHNIQUE = Engine.getTechnic("Distord");
        BLOOM_SELECT_TECHNIQUE = Engine.getTechnic("BloomSelect");
        BLOOM_TECHNIQUE = Engine.getTechnic("Bloom");
        HEAT_TECHNIQUE = Engine.getTechnic("Heat");
        FIXED_TECHNIQUE = Engine.getTechnic("FIXED");
        LUMINANCE_TECHNIQUE = Engine.getTechnic("Luminance");
        INVERT_TECHNIQUE = Engine.getTechnic("Invert");
        COLOR_SELECT_TECHNIQUE = Engine.getTechnic("ColorSelect");
        BINARIZE_TECHNIQUE = Engine.getTechnic("Binarize");
        MULTIPLY_TECHNIQUE = Engine.getTechnic("Multiply");
        NIGHT_TECHNIQUE = Engine.getTechnic("Night");
        BLUR_RADIAL_TECHNIQUE = Engine.getTechnic("BlurRadial");
        PIXELIZE_TECHNIQUE = Engine.getTechnic("Pixelize");
        COLOR_MODIFIER_TECHNIQUE = Engine.getTechnic("ColorModifier");
        RAW_TECHNIQUE = Engine.getTechnic("Raw");
        POST_PROCESS_HEAT_AND_HAZE = Engine.getTechnic("HeatAndHaze");
        POST_PROCESS_BLOOM = Engine.getTechnic("Bloom");
        POST_PROCESS_HEAT = Engine.getTechnic("Heat");
        POST_PROCESS_RADIAL_BLUR = Engine.getTechnic("RadialBlur");
        POST_PROCESS_PIXELIZE = Engine.getTechnic("Pixelize");
        POST_PROCESS_DESATURATE = Engine.getTechnic("Desaturate");
        ALPHAMASK_VARS = new Variable[] { new Variable("gAlphaMaskRadius", Variable.VariableType.Vector2), new Variable("gAlphaMaskPos", Variable.VariableType.Vector2) };
        COLOR_SCALE_FOR_WORLD_PARAMS = new EffectParams(new Variable[0]);
        (COLOR_SCALE_FOR_UI_PARAMS = new EffectParams(new Variable[0])).setFloat("gColorScale", 1.0f);
    }
}
