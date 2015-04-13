package com.ankamagames.framework.ai.targetfinder.aoe;

import java.util.*;
import com.ankamagames.framework.external.*;

public class CircleAOE extends AreaOfEffect
{
    public static final String CIRCLE_AOE = "circle";
    private int m_radius;
    private List<int[]> m_patternList;
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    public CircleAOE() {
        super();
        this.m_patternList = new ArrayList<int[]>(1);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CircleAOE.PARAMETERS_LIST_SET;
    }
    
    @Override
    public List<int[]> getPattern() {
        return this.m_patternList;
    }
    
    @Override
    public String toShortDescription() {
        return "circle-" + this.m_radius;
    }
    
    @Override
    public int getVisualRange() {
        return this.m_radius;
    }
    
    @Override
    public void initialize(final int[] params) throws IllegalArgumentException {
        if (params != null && params.length == 1) {
            this.m_radius = params[0];
            this.m_patternList.clear();
            for (int x = -this.m_radius; x <= this.m_radius; ++x) {
                for (int yBounds = this.m_radius - Math.abs(x), y = -yBounds; y <= yBounds; ++y) {
                    this.m_patternList.add(new int[] { x, y });
                }
            }
            return;
        }
        if (params == null || params.length == 0) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type cercle : 1 param\u00e8tre attendu, 0 trouv\u00e9(s)");
        }
        throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type cercle : 1 param\u00e8tre attendu, " + params.length + " trouv\u00e9(s)");
    }
    
    @Override
    protected boolean isInvariantByRotation() {
        return true;
    }
    
    @Override
    public AreaOfEffectEnum getType() {
        return AreaOfEffectEnum.CIRCLE;
    }
    
    public int getRadius() {
        return this.m_radius;
    }
    
    @Override
    public ArrayList<AreaOfEffect> getSubAOEs() {
        final ArrayList<AreaOfEffect> areaOfEffects = new ArrayList<AreaOfEffect>();
        areaOfEffects.add(this);
        return areaOfEffects;
    }
    
    @Override
    public AreaOfEffectShape getShape() {
        if (this.m_radius == 0) {
            return AreaOfEffectShape.POINT;
        }
        return AreaOfEffectShape.CIRCLE;
    }
    
    static {
        PARAMETERS_LIST_SET = new AOEParametersListSet(new ParameterList[] { new AOEParameterList("Cercle", new Parameter[] { new Parameter("rayon") }) });
    }
}
