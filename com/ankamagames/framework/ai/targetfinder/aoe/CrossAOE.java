package com.ankamagames.framework.ai.targetfinder.aoe;

import java.util.*;
import com.ankamagames.framework.external.*;

public class CrossAOE extends AreaOfEffect
{
    public static final String CROSS_AOE = "cross";
    private int m_upSsize;
    private int m_rightSize;
    private int m_downSize;
    private int m_leftSize;
    private List<int[]> m_patternList;
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    public CrossAOE() {
        super();
        this.m_patternList = new ArrayList<int[]>(1);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CrossAOE.PARAMETERS_LIST_SET;
    }
    
    @Override
    public void initialize(final int[] params) throws IllegalArgumentException {
        if (params == null || params.length == 0) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type cross : 1 param\u00e8tre attendu, 0 trouv\u00e9(s)");
        }
        if (params.length != 1 && params.length != 2 && params.length != 4) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type cross : 1 ou 2 ou 4 param\u00e8tres attendus, " + params.length + " trouv\u00e9(s)");
        }
        this.m_upSsize = params[0];
        if (params.length == 2) {
            this.m_leftSize = params[1];
            this.m_downSize = this.m_upSsize;
            this.m_rightSize = this.m_leftSize;
        }
        else if (params.length == 4) {
            this.m_downSize = params[1];
            this.m_leftSize = params[2];
            this.m_rightSize = params[3];
        }
        else {
            this.m_leftSize = this.m_upSsize;
            this.m_downSize = this.m_upSsize;
            this.m_rightSize = this.m_upSsize;
        }
        this.m_patternList.clear();
        this.m_patternList.add(new int[] { 0, 0 });
        for (int y = 1; y <= this.m_leftSize; ++y) {
            this.m_patternList.add(new int[] { 0, -y });
        }
        for (int y = 1; y <= this.m_rightSize; ++y) {
            this.m_patternList.add(new int[] { 0, y });
        }
        for (int x = 1; x <= this.m_upSsize; ++x) {
            this.m_patternList.add(new int[] { x, 0 });
        }
        for (int x = 1; x <= this.m_downSize; ++x) {
            this.m_patternList.add(new int[] { -x, 0 });
        }
    }
    
    @Override
    protected boolean isInvariantByRotation() {
        return true;
    }
    
    @Override
    public List<int[]> getPattern() {
        return this.m_patternList;
    }
    
    @Override
    public String toShortDescription() {
        return "cross-h" + this.m_upSsize + "b" + this.m_downSize + "-g" + this.m_leftSize + "d" + this.m_rightSize;
    }
    
    @Override
    public int getVisualRange() {
        return Math.min(Math.min(this.m_leftSize, this.m_rightSize), Math.min(this.m_upSsize, this.m_downSize));
    }
    
    @Override
    public AreaOfEffectEnum getType() {
        return AreaOfEffectEnum.CROSS;
    }
    
    @Override
    public ArrayList<AreaOfEffect> getSubAOEs() {
        final ArrayList<AreaOfEffect> areaOfEffects = new ArrayList<AreaOfEffect>();
        areaOfEffects.add(this);
        return areaOfEffects;
    }
    
    public int getHorizontalSize() {
        return this.m_leftSize;
    }
    
    @Override
    public AreaOfEffectShape getShape() {
        if (this.m_downSize == 0 && this.m_upSsize == 0 && this.m_leftSize == 0 && this.m_rightSize == 0) {
            return AreaOfEffectShape.POINT;
        }
        if (this.m_downSize == 0 && this.m_upSsize == 0) {
            return AreaOfEffectShape.VLINE;
        }
        if (this.m_leftSize == 0 && this.m_rightSize == 0) {
            return AreaOfEffectShape.HLINE;
        }
        return AreaOfEffectShape.CROSS;
    }
    
    static {
        PARAMETERS_LIST_SET = new AOEParametersListSet(new ParameterList[] { new AOEParameterList("Croix (deux barres de tailles identiques)", new Parameter[] { new Parameter("distance centre -> extr\u00e9mit\u00e9") }), new AOEParameterList("Croix (deux barres de tailles diff\u00e9rentes)", new Parameter[] { new Parameter("distance centre -> extr\u00e9mit\u00e9 face \u00e0 soi"), new Parameter("distance centre -> extr\u00e9mit\u00e9 sur le c\u00f4t\u00e9") }), new AOEParameterList("Croix (4 barres de tailles diff\u00e9rentes)", new Parameter[] { new Parameter("distance centre -> extr\u00e9mit\u00e9 face \u00e0 soi vers le haut"), new Parameter("distance centre -> extr\u00e9mit\u00e9 face \u00e0 soi vers le bas"), new Parameter("distance centre -> extr\u00e9mit\u00e9 sur le c\u00f4t\u00e9 vers la gauche"), new Parameter("distance centre -> extr\u00e9mit\u00e9 sur le c\u00f4t\u00e9 vers la droite") }) });
    }
}
