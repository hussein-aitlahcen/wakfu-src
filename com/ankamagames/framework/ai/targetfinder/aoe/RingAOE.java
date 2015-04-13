package com.ankamagames.framework.ai.targetfinder.aoe;

import java.util.*;
import com.ankamagames.framework.external.*;

public class RingAOE extends AreaOfEffect
{
    public static String RING_AOE;
    private int m_innerRadius;
    private int m_outerRadius;
    private List<int[]> m_patternList;
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    public RingAOE() {
        super();
        this.m_patternList = new ArrayList<int[]>(1);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RingAOE.PARAMETERS_LIST_SET;
    }
    
    @Override
    public List<int[]> getPattern() {
        return this.m_patternList;
    }
    
    @Override
    public String toShortDescription() {
        return RingAOE.RING_AOE + "-" + this.m_innerRadius + "-" + this.m_outerRadius;
    }
    
    @Override
    public int getVisualRange() {
        return this.m_innerRadius + this.m_outerRadius;
    }
    
    @Override
    public void initialize(final int[] params) throws IllegalArgumentException {
        if (params == null) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type ring : 2 param\u00e8tres attendus, 0 trouv\u00e9");
        }
        if (params.length != 2) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type ring : 2 param\u00e8tres attendus, " + params.length + " trouv\u00e9(s)");
        }
        this.m_innerRadius = ((params[0] < params[1]) ? params[0] : params[1]);
        this.m_outerRadius = ((params[0] > params[1]) ? params[0] : params[1]);
        this.m_patternList.clear();
        for (int x = 0; x <= this.m_outerRadius; ++x) {
            for (int y = Math.max(this.m_innerRadius - x, 0); y <= Math.max(this.m_outerRadius - x, 0); ++y) {
                this.m_patternList.add(new int[] { x, y });
                if (y != 0) {
                    this.m_patternList.add(new int[] { x, -y });
                }
                if (x != 0) {
                    this.m_patternList.add(new int[] { -x, y });
                    if (y != 0) {
                        this.m_patternList.add(new int[] { -x, -y });
                    }
                }
            }
        }
    }
    
    @Override
    protected boolean isInvariantByRotation() {
        return true;
    }
    
    @Override
    public AreaOfEffectEnum getType() {
        return AreaOfEffectEnum.RING;
    }
    
    public int getOuterRadius() {
        return this.m_outerRadius;
    }
    
    public int getInnerRadius() {
        return this.m_innerRadius;
    }
    
    @Override
    public ArrayList<AreaOfEffect> getSubAOEs() {
        final ArrayList<AreaOfEffect> areaOfEffects = new ArrayList<AreaOfEffect>();
        areaOfEffects.add(this);
        return areaOfEffects;
    }
    
    @Override
    public AreaOfEffectShape getShape() {
        if (this.m_innerRadius == 0 && this.m_outerRadius == 0) {
            return AreaOfEffectShape.POINT;
        }
        if (this.m_innerRadius == 0) {
            return AreaOfEffectShape.CIRCLE;
        }
        return AreaOfEffectShape.CIRCLERING;
    }
    
    static {
        RingAOE.RING_AOE = "ring";
        PARAMETERS_LIST_SET = new AOEParametersListSet(new ParameterList[] { new AOEParameterList("Anneau", new Parameter[] { new Parameter("rayon int\u00e9rieur"), new Parameter("rayon ext\u00e9rieur") }) });
    }
}
