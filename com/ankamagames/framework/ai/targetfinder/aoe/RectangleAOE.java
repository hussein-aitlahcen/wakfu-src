package com.ankamagames.framework.ai.targetfinder.aoe;

import java.util.*;
import com.ankamagames.framework.external.*;

public class RectangleAOE extends AreaOfEffect
{
    public static String RECT_AOE;
    private int m_width;
    private int m_height;
    private List<int[]> m_patternList;
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    public RectangleAOE() {
        super();
        this.m_patternList = new ArrayList<int[]>(1);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RectangleAOE.PARAMETERS_LIST_SET;
    }
    
    @Override
    public List<int[]> getPattern() {
        return this.m_patternList;
    }
    
    @Override
    public String toShortDescription() {
        return RectangleAOE.RECT_AOE + "-" + this.m_width + "x" + this.m_height;
    }
    
    @Override
    public int getVisualRange() {
        return Math.min(this.m_width, this.m_height);
    }
    
    @Override
    public void initialize(final int[] params) throws IllegalArgumentException {
        if (params == null) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type square : 1 \u00e0 2 param\u00e8tre attendu, 0 trouv\u00e9(s)");
        }
        if (params.length > 2) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type carr\u00e9 : 1 \u00e0 2 param\u00e8tre attendu, " + params.length + " trouv\u00e9(s)");
        }
        this.m_width = params[0];
        if (params.length == 1) {
            this.m_height = this.m_width;
        }
        else {
            this.m_height = params[1];
        }
        if (this.m_width != 0 && this.m_width % 2 == 0) {
            ++this.m_width;
        }
        if (this.m_height != 0 && this.m_height % 2 == 0) {
            ++this.m_height;
        }
        final int midwidth = (this.m_width - 1) / 2;
        final int midheight = (this.m_height - 1) / 2;
        for (int x = 0; x < this.m_width; ++x) {
            for (int y = 0; y < this.m_height; ++y) {
                this.m_patternList.add(new int[] { x - midwidth, y - midheight });
            }
        }
    }
    
    @Override
    protected boolean isInvariantByRotation() {
        return this.m_width == this.m_height;
    }
    
    @Override
    public AreaOfEffectEnum getType() {
        return AreaOfEffectEnum.RECTANGLE;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    @Override
    public ArrayList<AreaOfEffect> getSubAOEs() {
        final ArrayList<AreaOfEffect> areaOfEffects = new ArrayList<AreaOfEffect>();
        areaOfEffects.add(this);
        return areaOfEffects;
    }
    
    @Override
    public AreaOfEffectShape getShape() {
        if (this.m_height == 0 && this.m_width == 0) {
            return AreaOfEffectShape.POINT;
        }
        if (this.m_height == this.m_width) {
            return AreaOfEffectShape.SQUARE;
        }
        return AreaOfEffectShape.RECTANGLE;
    }
    
    static {
        RectangleAOE.RECT_AOE = "rectangle or square";
        PARAMETERS_LIST_SET = new AOEParametersListSet(new ParameterList[] { new AOEParameterList("Carr\u00e9 plein", new Parameter[] { new Parameter("Taille d'un c\u00f4t\u00e9 (doit \u00eatre impaire)") }), new AOEParameterList("Rectangle plein", new Parameter[] { new Parameter("Largeur (doit \u00eatre impaire)"), new Parameter("Hauteur (doit \u00eatre impaire)") }) });
    }
}
