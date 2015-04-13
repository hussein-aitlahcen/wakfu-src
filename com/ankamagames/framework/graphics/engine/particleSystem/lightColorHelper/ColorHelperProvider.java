package com.ankamagames.framework.graphics.engine.particleSystem.lightColorHelper;

import java.util.*;
import com.ankamagames.framework.graphics.engine.*;

public class ColorHelperProvider implements ColorHelper
{
    private final ColorHelper m_colorHelper;
    private static final ArrayList<ColorHelperValidator> m_validators;
    
    public ColorHelperProvider(final ColorHelper colorHelper) {
        super();
        this.m_colorHelper = colorHelper;
    }
    
    public static boolean addValidator(final ColorHelperValidator validator) {
        return !ColorHelperProvider.m_validators.contains(validator) && ColorHelperProvider.m_validators.add(validator);
    }
    
    @Override
    public void applyImmediate(final VertexBufferPCT vb, final float[] particleGeomColor, final int length) {
        for (int i = 0; i < ColorHelperProvider.m_validators.size(); ++i) {
            final ColorHelperValidator validator = ColorHelperProvider.m_validators.get(i);
            if (!validator.canApplyColor()) {
                ImmediateColor.getInstance().applyImmediate(vb, particleGeomColor, length);
                return;
            }
        }
        this.m_colorHelper.applyImmediate(vb, particleGeomColor, length);
    }
    
    @Override
    public void applyDelayed(final float[] colors) {
        for (int i = 0; i < ColorHelperProvider.m_validators.size(); ++i) {
            final ColorHelperValidator validator = ColorHelperProvider.m_validators.get(i);
            if (!validator.canApplyColor()) {
                ImmediateColor.getInstance().applyDelayed(colors);
                return;
            }
        }
        this.m_colorHelper.applyDelayed(colors);
    }
    
    static {
        m_validators = new ArrayList<ColorHelperValidator>();
    }
}
