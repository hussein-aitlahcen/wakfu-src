package com.ankamagames.framework.ai.targetfinder.aoe;

import java.util.*;
import com.ankamagames.framework.external.*;

public class PointAOE extends AreaOfEffect
{
    public static String POINT_AOE;
    private static final PointAOE m_staticInstance;
    private List<int[]> m_patternList;
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    public PointAOE() {
        super();
        this.m_patternList = new ArrayList<int[]>(1);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return PointAOE.PARAMETERS_LIST_SET;
    }
    
    public static PointAOE getInstance() {
        return PointAOE.m_staticInstance;
    }
    
    @Override
    public List<int[]> getPattern() {
        return this.m_patternList;
    }
    
    @Override
    public String toShortDescription() {
        return PointAOE.POINT_AOE;
    }
    
    @Override
    public int getVisualRange() {
        return 0;
    }
    
    @Override
    public void initialize(final int[] params) throws IllegalArgumentException {
        if (params != null && params.length > 0) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type Point : 0 attendu, " + params.length + " fourni(s)");
        }
        this.m_patternList.clear();
        this.m_patternList.add(new int[] { 0, 0 });
    }
    
    @Override
    protected boolean isInvariantByRotation() {
        return true;
    }
    
    @Override
    public AreaOfEffectEnum getType() {
        return AreaOfEffectEnum.POINT;
    }
    
    @Override
    public ArrayList<AreaOfEffect> getSubAOEs() {
        final ArrayList<AreaOfEffect> areaOfEffects = new ArrayList<AreaOfEffect>();
        areaOfEffects.add(this);
        return areaOfEffects;
    }
    
    @Override
    public AreaOfEffectShape getShape() {
        return AreaOfEffectShape.POINT;
    }
    
    static {
        PointAOE.POINT_AOE = "point";
        m_staticInstance = new PointAOE();
        PARAMETERS_LIST_SET = new AOEParametersListSet(new ParameterList[0]);
    }
}
