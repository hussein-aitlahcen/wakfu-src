package com.ankamagames.baseImpl.graphics.game.DynamicElement;

import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import java.io.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.utils.*;

public class DynamicElement extends AnimatedElementWithDirection
{
    private static final Logger m_logger;
    public static final String BASE_ANIM_NAME = "AnimStatique";
    private final DynamicElementTypeProvider m_typeProvider;
    private String m_baseAnimation;
    private String m_params;
    
    private DynamicElement(final long id, final float worldX, final float worldY, final float altitude, final byte direction, final DynamicElementType type) {
        super(id, worldX, worldY, altitude);
        this.m_baseAnimation = "AnimStatique";
        this.setDirection(Direction8.getDirectionFromIndex(direction));
        this.m_typeProvider = type.createProvider();
    }
    
    public void initialize() {
        this.m_typeProvider.initialize(this);
    }
    
    public void clear() {
        this.m_typeProvider.clear(this);
    }
    
    public static DynamicElement fromDefinition(final ClientEnvironmentMap map, final DynamicElementDef d) throws IOException {
        final DynamicElementType type = DynamicElementTypeProviderFactory.getInstance().getFromId(d.m_type);
        final int cellX = map.convertToWorldX(d.m_x);
        final int cellY = map.convertToWorldY(d.m_y);
        final DynamicElement build = build(d.m_id, d.m_gfxId, cellX, cellY, d.m_z, d.m_occluder, d.m_height, d.m_direction, type, d.m_baseAnimation);
        build.setParams(d.m_params);
        return build;
    }
    
    public static DynamicElement build(final long id, final int gfxId, final float worldX, final float worldY, final float altitude, final boolean occluder, final byte height, final byte direction, final DynamicElementType type, final String animation) throws IOException {
        final DynamicElement elt = new DynamicElement(id, worldX, worldY, altitude, direction, type);
        final String fileName = String.format(DynamicElementTypeProviderFactory.getInstance().getAnmPath(), gfxId);
        elt.setGfxId(fileName);
        elt.load(fileName, true);
        elt.setDeltaZ(LayerOrder.DYNAMIC_ELEMENT.getDeltaZ());
        elt.setBaseAnimation(animation);
        elt.canBeOccluder(occluder);
        elt.setVisualHeight(height);
        return elt;
    }
    
    private void setBaseAnimation(final String animation) {
        if (StringUtils.isEmptyOrNull(animation)) {
            this.m_baseAnimation = "AnimStatique";
        }
        else {
            this.m_baseAnimation = animation;
        }
    }
    
    public String getBaseAnimation() {
        return this.m_baseAnimation;
    }
    
    public String getParams() {
        return this.m_params;
    }
    
    public void setParams(final String params) {
        this.m_params = params;
    }
    
    public DynamicElementTypeProvider getTypeProvider() {
        return this.m_typeProvider;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DynamicElement.class);
    }
}
