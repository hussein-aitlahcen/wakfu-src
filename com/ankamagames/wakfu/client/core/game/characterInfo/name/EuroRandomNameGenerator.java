package com.ankamagames.wakfu.client.core.game.characterInfo.name;

import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;

public class EuroRandomNameGenerator implements RandomNameGenerator
{
    private final Random m_rand;
    private final char[] m_voyelles;
    private final char[] m_consonnes;
    private final HashMap<Character, char[]> m_bigrammes;
    private static final int DEFAULT_MIDDLE_PART_MAX_SIZE = 1;
    private static final int DEFAULT_FIRST_PART_IS_VOYELLE_PROBABILITY = 20;
    private static final int DEFAULT_ADD_SYLLABE_IN_MIDDLE_PART_PROBABILITY = 45;
    private static final int DEFAULT_USE_TERMINAL_LETTER_PROBABILITY = 30;
    private static final int DEFAULT_COMPOSED_NAMES_PROBABILITY = 8;
    
    EuroRandomNameGenerator() {
        super();
        this.m_rand = new MersenneTwister();
        this.m_bigrammes = new HashMap<Character, char[]>();
        final char[] m_voyelles_primaires = { 'a', 'e', 'i', 'o', 'u', 'y' };
        final char[] m_consonnes_primaires = { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z' };
        final short[] m_voyelles_probabilities = { 9, 15, 8, 6, 6, 1 };
        final short[] m_consonnes_probabilities = { 2, 2, 3, 2, 2, 2, 1, 1, 5, 3, 6, 2, 1, 6, 6, 6, 2, 1, 1, 1 };
        this.ajoutBigramme('c', new char[] { '_', 'h', 'l', 'r' }, new short[] { 8, 3, 2, 2 });
        this.ajoutBigramme('g', new char[] { '_', 'l', 'n', 'r' }, new short[] { 10, 2, 1, 2 });
        this.ajoutBigramme('l', new char[] { '_', 'l' }, new short[] { 8, 1 });
        this.ajoutBigramme('m', new char[] { '_', 'm' }, new short[] { 8, 1 });
        this.ajoutBigramme('n', new char[] { '_', 'n' }, new short[] { 6, 1 });
        this.ajoutBigramme('p', new char[] { '_', 'h', 'l', 'r', 'p' }, new short[] { 8, 2, 1, 3, 1 });
        this.ajoutBigramme('q', new char[] { '_', 'u' }, new short[] { 0, 1 });
        this.ajoutBigramme('s', new char[] { '_', 'h', 'k', 'l', 'n', 'p', 'q', 'r', 's', 't' }, new short[] { 15, 1, 1, 1, 2, 5, 1, 2, 10, 5 });
        this.ajoutBigramme('t', new char[] { '_', 'h', 'r', 't' }, new short[] { 8, 3, 5, 1 });
        this.ajoutBigramme('a', new char[] { 't' }, new short[] { 1 });
        this.ajoutBigramme('e', new char[] { 't', 'd' }, new short[] { 2, 1 });
        this.ajoutBigramme('i', new char[0], new short[0]);
        this.ajoutBigramme('o', new char[] { 't' }, new short[] { 1 });
        this.ajoutBigramme('u', new char[] { 's', 't' }, new short[] { 2, 1 });
        this.ajoutBigramme('y', new char[0], new short[0]);
        int nb = 0;
        TShortArrayList index = new TShortArrayList();
        for (int i = 0; i < m_voyelles_primaires.length; ++i) {
            nb += m_voyelles_probabilities[i];
        }
        for (short j = 0; j < nb; ++j) {
            index.add(j);
        }
        this.m_voyelles = new char[nb];
        for (int i = 0; i < m_voyelles_primaires.length; ++i) {
            for (int k = 0; k < m_voyelles_probabilities[i]; ++k) {
                final int l = this.m_rand.nextInt(index.size());
                final int randomIndex = index.get(l);
                this.m_voyelles[randomIndex] = m_voyelles_primaires[i];
                index.remove(l);
            }
        }
        nb = 0;
        index = new TShortArrayList();
        for (int i = 0; i < m_consonnes_primaires.length; ++i) {
            nb += m_consonnes_probabilities[i];
        }
        for (short j = 0; j < nb; ++j) {
            index.add(j);
        }
        this.m_consonnes = new char[nb];
        for (int i = 0; i < m_consonnes_primaires.length; ++i) {
            for (int k = 0; k < m_consonnes_probabilities[i]; ++k) {
                final int l = this.m_rand.nextInt(index.size());
                final int randomIndex = index.get(l);
                this.m_consonnes[randomIndex] = m_consonnes_primaires[i];
                index.remove(l);
            }
        }
    }
    
    private void ajoutBigramme(final char c, final char[] possibles, final short[] probabilities) {
        int nb = 0;
        for (int i = 0; i < possibles.length; ++i) {
            nb += probabilities[i];
        }
        final TShortArrayList index = new TShortArrayList();
        for (short j = 0; j < nb; ++j) {
            index.add(j);
        }
        final char[] result = new char[nb];
        for (int k = 0; k < possibles.length; ++k) {
            for (int l = 0; l < probabilities[k]; ++l) {
                final int m = this.m_rand.nextInt(index.size());
                final int randomIndex = index.get(m);
                result[randomIndex] = possibles[k];
                index.remove(m);
            }
        }
        this.m_bigrammes.put(c, result);
    }
    
    private boolean checkRandom(final int probabilite) {
        return this.m_rand.nextInt(100) < probabilite;
    }
    
    private char getRandomVoyelle() {
        return this.m_voyelles[this.m_rand.nextInt(this.m_voyelles.length)];
    }
    
    private char getRandomConsonne() {
        return this.m_consonnes[this.m_rand.nextInt(this.m_consonnes.length)];
    }
    
    private String generateSyllabe() {
        String result = "";
        final char c = this.getRandomConsonne();
        result += c;
        if (this.m_bigrammes.containsKey(c)) {
            final char[] tab = this.m_bigrammes.get(c);
            if (tab.length > 0) {
                result += tab[this.m_rand.nextInt(tab.length)];
            }
        }
        result += this.getRandomVoyelle();
        result = result.replace("_", "");
        return result;
    }
    
    private String generateFirstPart() {
        if (this.checkRandom(20)) {
            return (this.getRandomVoyelle() + "").toUpperCase();
        }
        String result = this.generateSyllabe();
        if (result.charAt(0) == result.charAt(1)) {
            result = result.substring(1);
        }
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }
    
    private String generateMiddlePart() {
        String result = "";
        for (int n = 0; n < 1 && this.checkRandom(45); ++n) {
            result += this.generateSyllabe();
        }
        return result;
    }
    
    private String generateLastPart() {
        String result = this.generateSyllabe();
        final char[] syl = result.toCharArray();
        final char last = syl[syl.length - 1];
        if (this.m_bigrammes.containsKey(last) && this.checkRandom(30)) {
            final char[] tab = this.m_bigrammes.get(last);
            if (tab.length > 1) {
                result += tab[this.m_rand.nextInt(tab.length)];
            }
        }
        return result;
    }
    
    private String generateSimpleName() {
        return this.generateFirstPart() + this.generateMiddlePart() + this.generateLastPart();
    }
    
    private String generateName() {
        if (this.checkRandom(8)) {
            return this.generateSimpleName() + "-" + this.generateSimpleName();
        }
        return this.generateSimpleName();
    }
    
    @Override
    public String getRandomName() {
        final String name = this.generateName();
        if (!WordsModerator.getInstance().validateName(name)) {
            return this.getRandomName();
        }
        return name;
    }
    
    @Override
    public void clean() {
    }
}
