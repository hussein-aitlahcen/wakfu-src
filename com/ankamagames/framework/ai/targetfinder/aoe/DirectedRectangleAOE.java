package com.ankamagames.framework.ai.targetfinder.aoe;

import java.util.*;
import com.ankamagames.framework.external.*;

public class DirectedRectangleAOE extends AreaOfEffect
{
    public static String DIRECTED_RECTANGLE_AOE;
    static final AOEParametersListSet PARAMETERS_LIST_SET;
    private ArrayList<AreaOfEffect> m_areaOfEffects;
    private int m_halfWidth;
    private int m_length;
    private List<int[]> m_patternList;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return DirectedRectangleAOE.PARAMETERS_LIST_SET;
    }
    
    @Override
    public void initialize(final int[] params) throws IllegalArgumentException {
        if (params == null || params.length != 2) {
            final int numParams = (params == null) ? 0 : params.length;
            throw new IllegalArgumentException("Deux param\u00e8tres attendus pour une aire d'effet rectangle directionnel, " + numParams + " trouv\u00e9s");
        }
        if (params[0] < 0) {
            throw new IllegalArgumentException("Rectangle directionnel : " + DirectedRectangleAOE.PARAMETERS_LIST_SET.getParameterList(2).getParameter(0).getName() + " doit \u00eatre au moins 0.");
        }
        if (params[1] < 1) {
            throw new IllegalArgumentException("Rectangle directionnel : " + DirectedRectangleAOE.PARAMETERS_LIST_SET.getParameterList(2).getParameter(0).getName() + " doit \u00eatre au moins 1.");
        }
        this.m_halfWidth = params[0];
        this.m_length = params[1];
        this.computePatternList();
    }
    
    private void computePatternList() {
        this.m_patternList = new ArrayList<int[]>();
        for (int y = -this.m_halfWidth; y <= this.m_halfWidth; ++y) {
            for (int x = 0; x < this.m_length; ++x) {
                this.m_patternList.add(new int[] { x, y });
            }
        }
    }
    
    @Override
    protected boolean isInvariantByRotation() {
        return false;
    }
    
    @Override
    public AreaOfEffectEnum getType() {
        return AreaOfEffectEnum.DIRECTED_RECTANGLE;
    }
    
    @Override
    public List<int[]> getPattern() {
        return this.m_patternList;
    }
    
    @Override
    public String toShortDescription() {
        return DirectedRectangleAOE.DIRECTED_RECTANGLE_AOE + "-" + (2 * this.m_halfWidth + 1) + "x" + this.m_length;
    }
    
    @Override
    public int getVisualRange() {
        return this.m_length;
    }
    
    @Override
    public ArrayList<AreaOfEffect> getSubAOEs() {
        if (this.m_areaOfEffects == null) {
            (this.m_areaOfEffects = new ArrayList<AreaOfEffect>(1)).add(this);
        }
        return this.m_areaOfEffects;
    }
    
    @Override
    public AreaOfEffectShape getShape() {
        if (this.m_length == 1) {
            return (this.m_halfWidth == 0) ? AreaOfEffectShape.POINT : AreaOfEffectShape.HLINE;
        }
        return (this.m_halfWidth == 0) ? AreaOfEffectShape.VLINE : AreaOfEffectShape.RECTANGLE;
    }
    
    static {
        DirectedRectangleAOE.DIRECTED_RECTANGLE_AOE = "Directed rectangle";
        PARAMETERS_LIST_SET = new AOEParametersListSet(new ParameterList[] { new AOEParameterList("Rectangle directionnel", new Parameter[] { new Parameter("Distance maximale \u00e0 la ligne centrale"), new Parameter("Longueur") }) });
    }
}
