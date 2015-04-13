package com.ankamagames.xulor2.component.map;

import com.ankamagames.framework.graphics.engine.fx.postProcess.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.test.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import gnu.trove.*;

public class MapShader extends PostProcess
{
    private static final Color COLOR;
    private static final String SELECTED_ZONE = "selectedZone";
    private static final String TEX0_DIM = "tex0Dim";
    private Texture m_maskTexture;
    private TIntByteHashMap m_zoneIndexById;
    private TIntObjectHashMap<Color> m_colorsById;
    
    private static EffectParams createParams() {
        return new EffectParams(new Variable[] { new Variable("selectedZone", Variable.VariableType.Float), new Variable("tex0Dim", Variable.VariableType.Float) });
    }
    
    public MapShader() {
        super(Xulor.getInstance().m_shaderPath + "mappemonde.cgfx", "nation", createParams());
    }
    
    public void setMaskTexture(final Texture maskTexture) {
        this.m_maskTexture = maskTexture;
        this.writeColorsToTexture();
    }
    
    public void setZonesIndexById(final TIntByteHashMap zonesIndexById) {
        this.m_zoneIndexById = zonesIndexById;
        this.writeColorsToTexture();
    }
    
    public void setColorsById(final TIntObjectHashMap<Color> colorsById) {
        this.m_colorsById = colorsById;
        this.writeColorsToTexture();
    }
    
    public void setSelectedZone(final int zoneId) {
        this.m_params.setFloat("selectedZone", zoneId);
    }
    
    public void setMainTextureDimension(final int dim) {
        this.m_params.setFloat("tex0Dim", dim);
    }
    
    private void writeColorsToTexture() {
        if (this.m_maskTexture == null || this.m_zoneIndexById == null || this.m_colorsById == null) {
            return;
        }
        final Layer layer = this.m_maskTexture.getLayer(0);
        final TIntObjectIterator<Color> it = this.m_colorsById.iterator();
        while (it.hasNext()) {
            it.advance();
            it.key();
            final byte index = this.m_zoneIndexById.get(it.key());
            final Color color = it.value();
            if (color == null) {
                MapShader.COLOR.setFromFloat(1.0f, 1.0f, 1.0f, 1.0f);
            }
            else {
                MapShader.COLOR.setFromFloat(1.0f, color.getRed(), color.getGreen(), color.getBlue());
            }
            layer.setPixel(index, 0, MapShader.COLOR);
        }
        this.m_maskTexture.toUpdate();
    }
    
    @Override
    public void prepare(final EntitySprite entity) {
        entity.setEffect(this.getEffect(), this.getTechniqueCRC(), this.getParams());
        entity.setTexture2(this.m_maskTexture);
    }
    
    public EffectParams getParams() {
        if (!HardwareFeatureManager.INSTANCE.isShaderSupported()) {
            this.m_params.setFloat("gColorScale", FxConstants.COLOR_SCALE_FOR_UI_PARAMS.getFloat("gColorScale"));
        }
        return this.m_params;
    }
    
    @Override
    public String toString() {
        return "MapShader{m_maskTexture=" + this.m_maskTexture.getFileName() + '}';
    }
    
    static {
        COLOR = new Color();
    }
}
