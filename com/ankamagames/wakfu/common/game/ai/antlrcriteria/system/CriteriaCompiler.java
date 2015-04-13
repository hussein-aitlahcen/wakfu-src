package com.ankamagames.wakfu.common.game.ai.antlrcriteria.system;

import org.apache.log4j.*;
import org.antlr.runtime.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public final class CriteriaCompiler
{
    private static final Logger m_logger;
    
    public static ArrayList<ParserObject> compileList(final String string) throws Exception {
        if (string == null || string.trim().length() == 0) {
            return null;
        }
        final ANTLRStringStream input = new ANTLRStringStream(string);
        final CritereLexer lex = new CritereLexer((CharStream)input);
        final CommonTokenStream tokens = new CommonTokenStream((TokenSource)lex);
        final CritereParser g = new CritereParser((TokenStream)tokens);
        ArrayList<ParserObject> criterion;
        try {
            criterion = g.critere();
        }
        catch (RecognitionException e) {
            throw new RuntimeException("Erreur de syntaxe dans le crit\u00e8re, \u00e0 la ligne " + e.line + " caract\u00e8re " + e.charPositionInLine + " sur le Token " + e.token.toString() + ".", (Throwable)e);
        }
        if (g.getNumberOfSyntaxErrors() != 0) {
            throw new RuntimeException("Erreur de syntaxe dans le crit\u00e8re", new Exception());
        }
        return criterion;
    }
    
    public static PositionValue compilePosition(final String string) throws Exception {
        final ArrayList<ParserObject> list = compileList(string);
        if (list == null || list.isEmpty()) {
            return null;
        }
        final ParserObject object = list.get(list.size() - 1);
        if (object.getType() != ParserType.POSITION) {
            CriteriaCompiler.m_logger.error((Object)("On a un crit\u00e8re de type " + object.getType().name() + " au lieu d'un bool\u00e9en"));
            return null;
        }
        return (PositionValue)object;
    }
    
    public static SimpleCriterion compileBoolean(final String string) throws Exception {
        final ArrayList<ParserObject> list = compileList(string);
        if (list == null || list.isEmpty()) {
            return null;
        }
        SimpleCriterion value = null;
        for (final ParserObject object : list) {
            if (object != null && object.getType() == ParserType.BOOLEAN) {
                if (value == null) {
                    value = (SimpleCriterion)object;
                }
                else {
                    value = AndCriterion.generate(value, object);
                }
            }
        }
        return value;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CriteriaCompiler.class);
    }
}
