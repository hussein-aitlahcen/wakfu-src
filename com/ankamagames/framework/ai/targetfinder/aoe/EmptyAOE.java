package com.ankamagames.framework.ai.targetfinder.aoe;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import com.ankamagames.framework.external.*;

public class EmptyAOE extends AreaOfEffect
{
    public static String EMPTY_AOE;
    private int m_radius;
    private double m_squaredRadius;
    private static final List<int[]> m_emptyList;
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return EmptyAOE.PARAMETERS_LIST_SET;
    }
    
    @Override
    public void initialize(final int[] params) throws IllegalArgumentException {
    }
    
    @Override
    public AreaOfEffectEnum getType() {
        return AreaOfEffectEnum.EMPTY;
    }
    
    @Override
    public List<int[]> getPattern() {
        return EmptyAOE.m_emptyList;
    }
    
    @Override
    public String toShortDescription() {
        return EmptyAOE.EMPTY_AOE + "-" + this.m_radius;
    }
    
    @Override
    public int getVisualRange() {
        return 0;
    }
    
    public <T extends Target> Iterable<T> getTargets(final Point3 effectSourcePosition, final Point3 areaCenter, final Iterator<T> possibleTargets) {
        return new EmptyIterable<T>();
    }
    
    @Override
    protected boolean isInvariantByRotation() {
        return true;
    }
    
    @Override
    public ArrayList<AreaOfEffect> getSubAOEs() {
        final ArrayList<AreaOfEffect> areaOfEffects = new ArrayList<AreaOfEffect>();
        areaOfEffects.add(this);
        return areaOfEffects;
    }
    
    @Override
    public AreaOfEffectShape getShape() {
        return AreaOfEffectShape.SPECIAL;
    }
    
    static {
        EmptyAOE.EMPTY_AOE = "empty";
        m_emptyList = new ArrayList<int[]>(0);
        PARAMETERS_LIST_SET = new AOEParametersListSet(new ParameterList[0]);
    }
}
