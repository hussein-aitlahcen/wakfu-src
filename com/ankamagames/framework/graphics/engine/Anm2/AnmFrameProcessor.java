package com.ankamagames.framework.graphics.engine.Anm2;

import org.apache.log4j.*;

enum AnmFrameProcessor
{
    EMPTY {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.noColor(parentTransform, result);
            AnmFrameProcessor.noRotation(parentTransform, result);
            AnmFrameProcessor.noTranslation(parentTransform, result);
        }
    }, 
    A {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorAdd(parentTransform, result, table.m_colors, frame.read());
            AnmFrameProcessor.noRotation(parentTransform, result);
            AnmFrameProcessor.noTranslation(parentTransform, result);
        }
    }, 
    AM {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorMultAdd(parentTransform, result, table.m_colors, frame.read(), frame.read());
            AnmFrameProcessor.noRotation(parentTransform, result);
            AnmFrameProcessor.noTranslation(parentTransform, result);
        }
    }, 
    M {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorMult(parentTransform, result, table.m_colors, frame.read());
            AnmFrameProcessor.noRotation(parentTransform, result);
            AnmFrameProcessor.noTranslation(parentTransform, result);
        }
    }, 
    R {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.noColor(parentTransform, result);
            AnmFrameProcessor.processRotation(parentTransform, result, table.m_rotations, frame.read());
            AnmFrameProcessor.noTranslation(parentTransform, result);
        }
    }, 
    RA {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorAdd(parentTransform, result, table.m_colors, frame.read());
            AnmFrameProcessor.processRotation(parentTransform, result, table.m_rotations, frame.read());
            AnmFrameProcessor.noTranslation(parentTransform, result);
        }
    }, 
    RAM {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorMultAdd(parentTransform, result, table.m_colors, frame.read(), frame.read());
            AnmFrameProcessor.processRotation(parentTransform, result, table.m_rotations, frame.read());
            AnmFrameProcessor.noTranslation(parentTransform, result);
        }
    }, 
    RM {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorMult(parentTransform, result, table.m_colors, frame.read());
            AnmFrameProcessor.processRotation(parentTransform, result, table.m_rotations, frame.read());
            AnmFrameProcessor.noTranslation(parentTransform, result);
        }
    }, 
    RT {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.noColor(parentTransform, result);
            AnmFrameProcessor.processRotationTranslation(parentTransform, result, table.m_rotations, frame.read(), table.m_translations, frame.read());
        }
    }, 
    RTA {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorAdd(parentTransform, result, table.m_colors, frame.read());
            AnmFrameProcessor.processRotationTranslation(parentTransform, result, table.m_rotations, frame.read(), table.m_translations, frame.read());
        }
    }, 
    RTAM {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorMultAdd(parentTransform, result, table.m_colors, frame.read(), frame.read());
            AnmFrameProcessor.processRotationTranslation(parentTransform, result, table.m_rotations, frame.read(), table.m_translations, frame.read());
        }
    }, 
    RTM {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorMult(parentTransform, result, table.m_colors, frame.read());
            AnmFrameProcessor.processRotationTranslation(parentTransform, result, table.m_rotations, frame.read(), table.m_translations, frame.read());
        }
    }, 
    T {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.noColor(parentTransform, result);
            AnmFrameProcessor.noRotation(parentTransform, result);
            AnmFrameProcessor.processTranslation(parentTransform, result, table.m_translations, frame.read());
        }
    }, 
    TA {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorAdd(parentTransform, result, table.m_colors, frame.read());
            AnmFrameProcessor.noRotation(parentTransform, result);
            AnmFrameProcessor.processTranslation(parentTransform, result, table.m_translations, frame.read());
        }
    }, 
    TAM {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorMultAdd(parentTransform, result, table.m_colors, frame.read(), frame.read());
            AnmFrameProcessor.noRotation(parentTransform, result);
            AnmFrameProcessor.processTranslation(parentTransform, result, table.m_translations, frame.read());
        }
    }, 
    TM {
        @Override
        protected final void readAndProcess(final AnmFrameData frame, final AnmTransformDataTable table, final AnmTransform parentTransform, final AnmTransform result) {
            AnmFrameProcessor.processColorMult(parentTransform, result, table.m_colors, frame.read());
            AnmFrameProcessor.noRotation(parentTransform, result);
            AnmFrameProcessor.processTranslation(parentTransform, result, table.m_translations, frame.read());
        }
    };
    
    private static final Logger m_logger;
    static final int ROTATION_MASK = 1;
    static final int TRANSLATION_MASK = 2;
    static final int MULT_COLOR_MASK = 4;
    static final int ADD_COLOR_MASK = 8;
    
    static AnmFrameProcessor getFromType(final int type) {
        switch (type) {
            case 3: {
                return AnmFrameProcessor.RT;
            }
            case 2: {
                return AnmFrameProcessor.T;
            }
            case 0: {
                return AnmFrameProcessor.EMPTY;
            }
            case 8: {
                return AnmFrameProcessor.A;
            }
            case 12: {
                return AnmFrameProcessor.AM;
            }
            case 4: {
                return AnmFrameProcessor.M;
            }
            case 1: {
                return AnmFrameProcessor.R;
            }
            case 9: {
                return AnmFrameProcessor.RA;
            }
            case 13: {
                return AnmFrameProcessor.RAM;
            }
            case 5: {
                return AnmFrameProcessor.RM;
            }
            case 11: {
                return AnmFrameProcessor.RTA;
            }
            case 15: {
                return AnmFrameProcessor.RTAM;
            }
            case 7: {
                return AnmFrameProcessor.RTM;
            }
            case 10: {
                return AnmFrameProcessor.TA;
            }
            case 14: {
                return AnmFrameProcessor.TAM;
            }
            case 6: {
                return AnmFrameProcessor.TM;
            }
            default: {
                AnmFrameProcessor.m_logger.error((Object)("type inconnu " + type));
                return null;
            }
        }
    }
    
    protected abstract void readAndProcess(final AnmFrameData p0, final AnmTransformDataTable p1, final AnmTransform p2, final AnmTransform p3);
    
    static void noColor(final AnmTransform parentTransform, final AnmTransform result) {
        result.m_red = parentTransform.m_red;
        result.m_green = parentTransform.m_green;
        result.m_blue = parentTransform.m_blue;
        result.m_alpha = parentTransform.m_alpha;
    }
    
    static void noTranslation(final AnmTransform parentTransform, final AnmTransform result) {
        result.m_translationIsIdentity = parentTransform.m_translationIsIdentity;
        result.m_translationX = parentTransform.m_translationX;
        result.m_translationY = parentTransform.m_translationY;
    }
    
    static void noRotation(final AnmTransform parentTransform, final AnmTransform result) {
        result.m_rotationIsIdentity = parentTransform.m_rotationIsIdentity;
        result.m_rotationSkewX0 = parentTransform.m_rotationSkewX0;
        result.m_rotationSkewX1 = parentTransform.m_rotationSkewX1;
        result.m_rotationSkewY0 = parentTransform.m_rotationSkewY0;
        result.m_rotationSkewY1 = parentTransform.m_rotationSkewY1;
    }
    
    static void processColorAdd(final AnmTransform parentTransform, final AnmTransform result, final float[] colors, final int offset) {
        result.m_red = parentTransform.m_red + colors[offset];
        result.m_green = parentTransform.m_green + colors[offset + 1];
        result.m_blue = parentTransform.m_blue + colors[offset + 2];
        result.m_alpha = parentTransform.m_alpha + colors[offset + 3];
    }
    
    static void processColorMult(final AnmTransform parentTransform, final AnmTransform result, final float[] colors, final int offset) {
        result.m_red = parentTransform.m_red * colors[offset];
        result.m_green = parentTransform.m_green * colors[offset + 1];
        result.m_blue = parentTransform.m_blue * colors[offset + 2];
        result.m_alpha = parentTransform.m_alpha * colors[offset + 3];
    }
    
    static void processColorMultAdd(final AnmTransform parentTransform, final AnmTransform result, final float[] colors, final int offsetA, final int offsetM) {
        result.m_red = parentTransform.m_red * colors[offsetM] + colors[offsetA];
        result.m_green = parentTransform.m_green * colors[offsetM + 1] + colors[offsetA + 1];
        result.m_blue = parentTransform.m_blue * colors[offsetM + 2] + colors[offsetA + 2];
        result.m_alpha = parentTransform.m_alpha * colors[offsetM + 3] + colors[offsetA + 3];
    }
    
    static void processRotation(final AnmTransform parentTransform, final AnmTransform result, final float[] rotations, final int offset) {
        result.m_rotationIsIdentity = false;
        final float rx0 = rotations[offset];
        final float ry0 = rotations[offset + 1];
        final float rx = rotations[offset + 2];
        final float ry = rotations[offset + 3];
        if (parentTransform.m_rotationIsIdentity) {
            result.m_rotationSkewX0 = rx0;
            result.m_rotationSkewY0 = ry0;
            result.m_rotationSkewX1 = rx;
            result.m_rotationSkewY1 = ry;
        }
        else {
            result.m_rotationSkewX0 = rx0 * parentTransform.m_rotationSkewX0 + ry0 * parentTransform.m_rotationSkewX1;
            result.m_rotationSkewY0 = rx0 * parentTransform.m_rotationSkewY0 + ry0 * parentTransform.m_rotationSkewY1;
            result.m_rotationSkewX1 = rx * parentTransform.m_rotationSkewX0 + ry * parentTransform.m_rotationSkewX1;
            result.m_rotationSkewY1 = rx * parentTransform.m_rotationSkewY0 + ry * parentTransform.m_rotationSkewY1;
        }
    }
    
    static void processTranslation(final AnmTransform parentTransform, final AnmTransform result, final float[] translations, final int offset) {
        result.m_translationIsIdentity = false;
        final float tx = translations[offset];
        final float ty = translations[offset + 1];
        if (parentTransform.m_rotationIsIdentity) {
            result.m_translationX = tx + parentTransform.m_translationX;
            result.m_translationY = ty + parentTransform.m_translationY;
        }
        else {
            result.m_translationX = tx * parentTransform.m_rotationSkewX0 + ty * parentTransform.m_rotationSkewX1 + parentTransform.m_translationX;
            result.m_translationY = tx * parentTransform.m_rotationSkewY0 + ty * parentTransform.m_rotationSkewY1 + parentTransform.m_translationY;
        }
    }
    
    static void processRotationTranslation(final AnmTransform parentTransform, final AnmTransform result, final float[] rotations, final int offsetR, final float[] translations, final int offsetT) {
        result.m_rotationIsIdentity = false;
        result.m_translationIsIdentity = false;
        final float rx0 = rotations[offsetR];
        final float ry0 = rotations[offsetR + 1];
        final float rx = rotations[offsetR + 2];
        final float ry = rotations[offsetR + 3];
        final float tx = translations[offsetT];
        final float ty = translations[offsetT + 1];
        if (parentTransform.m_rotationIsIdentity) {
            result.m_rotationSkewX0 = rx0;
            result.m_rotationSkewY0 = ry0;
            result.m_rotationSkewX1 = rx;
            result.m_rotationSkewY1 = ry;
            result.m_translationX = tx + parentTransform.m_translationX;
            result.m_translationY = ty + parentTransform.m_translationY;
        }
        else {
            result.m_rotationSkewX0 = rx0 * parentTransform.m_rotationSkewX0 + ry0 * parentTransform.m_rotationSkewX1;
            result.m_rotationSkewY0 = rx0 * parentTransform.m_rotationSkewY0 + ry0 * parentTransform.m_rotationSkewY1;
            result.m_rotationSkewX1 = rx * parentTransform.m_rotationSkewX0 + ry * parentTransform.m_rotationSkewX1;
            result.m_rotationSkewY1 = rx * parentTransform.m_rotationSkewY0 + ry * parentTransform.m_rotationSkewY1;
            result.m_translationX = tx * parentTransform.m_rotationSkewX0 + ty * parentTransform.m_rotationSkewX1 + parentTransform.m_translationX;
            result.m_translationY = tx * parentTransform.m_rotationSkewY0 + ty * parentTransform.m_rotationSkewY1 + parentTransform.m_translationY;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmFrameProcessor.class);
    }
}
