package com.ankamagames.framework.graphics.engine.opengl.Cg;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.nio.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.sun.opengl.cg.*;
import com.ankamagames.framework.graphics.engine.test.*;
import com.ankamagames.framework.graphics.engine.fx.*;

public class GLEffect extends Effect
{
    private static final boolean SHOW_TECHNIQUE_CRC = true;
    private static final Logger m_logger;
    private final EffectParams.Procedure m_setParameterProcedure;
    private static CGcontext m_context;
    protected CGeffect m_effect;
    private final IntObjectLightWeightMap<Parameter> m_parameters;
    private static String m_functions;
    
    public GLEffect() {
        super();
        this.m_setParameterProcedure = new EffectParams.Procedure() {
            @Override
            public void execute(final Variable var, final int index, final float[] values) {
                final Parameter parameter = GLEffect.this.m_parameters.get(var.m_crc);
                if (parameter == null) {
                    GLEffect.m_logger.error((Object)("param\u00e8tre inconnu " + var.m_name));
                    return;
                }
                switch (var.m_type) {
                    case Float: {
                        if (!parameter.lastValueEquals(values[index])) {
                            CgGL.cgSetParameter1f(parameter.m_parameter, values[index]);
                            parameter.setLastValue(values[index]);
                            break;
                        }
                        break;
                    }
                    case Vector2: {
                        if (!parameter.lastValueEquals(values, index, 2)) {
                            CgGL.cgSetParameter2fv(parameter.m_parameter, values, index);
                            parameter.setLastValue(values, index, 2);
                            break;
                        }
                        break;
                    }
                    case Vector3: {
                        if (!parameter.lastValueEquals(values, index, 3)) {
                            CgGL.cgSetParameter3fv(parameter.m_parameter, values, index);
                            parameter.setLastValue(values, index, 3);
                            break;
                        }
                        break;
                    }
                    case Vector4: {
                        if (!parameter.lastValueEquals(values, index, 4)) {
                            CgGL.cgSetParameter4fv(parameter.m_parameter, values, index);
                            parameter.setLastValue(values, index, 4);
                            break;
                        }
                        break;
                    }
                    case Matrix44: {
                        if (!parameter.lastValueEquals(values, index, 16)) {
                            CgGL.cgSetParameterValuefr(parameter.m_parameter, 16, values, index);
                            parameter.setLastValue(values, index, 16);
                            break;
                        }
                        break;
                    }
                    default: {
                        GLEffect.m_logger.error((Object)("type inconnu " + var.m_type));
                        break;
                    }
                }
            }
        };
        this.m_parameters = new IntObjectLightWeightMap<Parameter>(5);
    }
    
    @Override
    public void load(final String name, final String fileName) {
        super.load(name, fileName);
        if (GLEffect.m_context == null) {
            try {
                GLEffect.m_context = CgGL.cgCreateContext();
            }
            catch (NoClassDefFoundError e2) {
                GLEffect.m_logger.error((Object)"GlEffect not loaded : Cg library not loaded");
                return;
            }
            catch (UnsatisfiedLinkError e) {
                GLEffect.m_logger.error((Object)("GlEffect not loaded : Cg library not found : " + e.getMessage()));
                return;
            }
            CgGL.cgGLRegisterStates(GLEffect.m_context);
        }
        if (GLEffect.m_functions == null) {
            final String functionFileName = FileHelper.getPath(fileName) + "/functions.cgfx";
            byte[] fileBuffer;
            try {
                fileBuffer = ContentFileHelper.readFile(functionFileName);
            }
            catch (Exception e3) {
                GLEffect.m_logger.error((Object)("Unable to read file functions: " + functionFileName));
                return;
            }
            GLEffect.m_functions = new String(fileBuffer);
        }
        byte[] fileBuffer;
        try {
            fileBuffer = ContentFileHelper.readFile(fileName);
        }
        catch (Exception e4) {
            GLEffect.m_logger.error((Object)("Unable to read file " + fileName));
            return;
        }
        final String source = new String(fileBuffer);
        this.m_effect = CgGL.cgCreateEffect(GLEffect.m_context, GLEffect.m_functions + source, (String[])null);
        if (this.m_effect == null) {
            GLEffect.m_logger.error((Object)(CgGL.cgGetLastErrorString((IntBuffer)null) + "\n(prendre en compte le nombre de ligne de functions.cgfx)"));
            GLEffect.m_logger.error((Object)CgGL.cgGetLastListing(GLEffect.m_context));
        }
        this.initialize();
    }
    
    @Override
    public void reload() {
        if (this.m_effect != null) {
            CgGL.cgDestroyEffect(this.m_effect);
        }
        super.reload();
    }
    
    @Override
    public void create(final String name, final String code) {
        super.create(name, code);
        CgGL.cgGLRegisterStates(GLEffect.m_context = CgGL.cgCreateContext());
        this.m_effect = CgGL.cgCreateEffect(GLEffect.m_context, code, (String[])null);
        this.initialize();
    }
    
    @Override
    public void parse() {
        this.m_parameters.clear();
        CGparameter parameter = CgGL.cgGetFirstEffectParameter(this.m_effect);
        do {
            final String s = CgGL.cgGetParameterName(parameter);
            if (s != null && s.length() > 0) {
                this.m_parameters.put(Engine.getTechnic(s), new Parameter(s, parameter));
            }
            parameter = CgGL.cgGetNextParameter(parameter);
        } while (parameter != null);
    }
    
    @Override
    public final void render(final Renderer renderer, final Entity entity, final EffectParams params) {
        super.render(renderer, entity, params);
        assert renderer.getType() == RendererType.OpenGL : "Invalid renderer type";
        if (this.useFixedPipeline()) {
            RenderStateManager.getInstance().setColorScale(params.getFloat("gColorScale"));
            entity.renderWithoutEffect(renderer);
        }
        else {
            if (params != null) {
                this.applyParams(params);
            }
            this.m_selectedTechnique.render(renderer, entity);
        }
    }
    
    @Override
    protected final void applyParams(final EffectParams params) {
        assert params != null;
        params.foreach(this.m_setParameterProcedure);
    }
    
    public static void setCGContext(final CGcontext context) {
        GLEffect.m_context = context;
    }
    
    private int countTechniques() {
        int count = 0;
        CGtechnique technique = CgGL.cgGetFirstTechnique(this.m_effect);
        CgGL.cgGetTechniqueName(technique);
        while (technique != null) {
            ++count;
            technique = CgGL.cgGetNextTechnique(technique);
        }
        return count;
    }
    
    private void initialize() {
        this.m_techniques.clear();
        if (!HardwareFeatureManager.INSTANCE.isShaderSupported()) {
            return;
        }
        this.m_techniques.ensureCapacity(this.countTechniques());
        for (CGtechnique cgTechnique = CgGL.cgGetFirstTechnique(this.m_effect); cgTechnique != null; cgTechnique = CgGL.cgGetNextTechnique(cgTechnique)) {
            final boolean valide = CgGL.cgValidateTechnique(cgTechnique);
            final Technique technique = new GLTechnique(cgTechnique);
            final int techniqueCRC = technique.m_crc;
            final String message = "[TECHNIQUE] " + (valide ? "V" : "X") + ' ' + technique.m_name + " (" + String.format("0x%X", techniqueCRC) + ") ";
            if (valide) {
                GLEffect.m_logger.info((Object)message);
            }
            else {
                GLEffect.m_logger.warn((Object)message);
            }
            if (valide) {
                this.m_techniques.put(techniqueCRC, technique);
            }
        }
        this.parse();
    }
    
    static {
        m_logger = Logger.getLogger((Class)GLEffect.class);
        GLEffect.m_context = null;
        GLEffect.m_functions = null;
    }
    
    private static class Parameter
    {
        public final CGparameter m_parameter;
        public final String m_name;
        private float m_lastValue;
        private float[] m_lastValuev;
        
        Parameter(final String name, final CGparameter parameter) {
            super();
            this.m_name = name;
            this.m_parameter = parameter;
        }
        
        public boolean lastValueEquals(final float value) {
            return this.m_lastValue == value;
        }
        
        public void setLastValue(final float value) {
            this.m_lastValue = value;
        }
        
        public boolean lastValueEquals(final float[] values, final int index, final int count) {
            if (this.m_lastValuev == null) {
                return false;
            }
            for (int i = 0; i < count; ++i) {
                if (this.m_lastValuev[i] != values[index + i]) {
                    return false;
                }
            }
            return true;
        }
        
        public void setLastValue(final float[] values, final int index, final int count) {
            if (this.m_lastValuev == null) {
                this.m_lastValuev = new float[count];
            }
            for (int i = 0; i < count; ++i) {
                this.m_lastValuev[i] = values[index + i];
            }
        }
    }
}
