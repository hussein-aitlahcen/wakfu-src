package com.ankamagames.wakfu.client.core.utils;

import com.ankamagames.framework.kernel.core.common.collections.*;
import org.apache.commons.lang3.*;
import java.util.*;
import java.util.regex.*;
import com.ankamagames.framework.kernel.core.translator.*;

public final class SmsAndFloodController
{
    private static final SmsAndFloodController m_instance;
    private boolean m_checking;
    private boolean m_smsChecking;
    private boolean m_floodChecking;
    private static final int SMS_WORDS_COUNT_LIMIT = 2;
    private static final int SMS_LIGHT_LEVEL = 3;
    private static final int SMS_HARDCORE_LEVEL = 8;
    private static final long SMS_HISTORIC_PASTTIME = 900000L;
    private static final int SIMILARY_SENTENCES_LIMIT = 4;
    private static final int SIMILARY_SENTENCES_DETECTION = 75;
    private static final long SIMILARY_SENTENCES_PASTTIME = 120000L;
    private static final int FLOOD_SENTENCES_LIMIT = 6;
    private static final long FLOOD_SENTENCES_PASTTIME = 5000L;
    private static final int HISTORIC_LENGHT = 10;
    private final LinkedList<ObjectPair<String, Long>> m_historic;
    private final LinkedList<ObjectPair<Action, Long>> m_actionHistoric;
    public static final int FIGHT_ACTION = 1;
    public static final int GROUP_ACTION = 2;
    public static final int EXCHANGE_ACTION = 3;
    public static final int GUILD_ACTION = 3;
    private int m_smsLightCount;
    private int m_smsHardcoreCount;
    private long m_smsLastDetection;
    private static final String[] SMS_WORDS_LIST_FR;
    private static final Pattern m_smsPattern_fr;
    private static Pattern m_smsPattern;
    
    public static SmsAndFloodController getInstance() {
        return SmsAndFloodController.m_instance;
    }
    
    private SmsAndFloodController() {
        super();
        this.m_checking = true;
        this.m_smsChecking = true;
        this.m_floodChecking = true;
        this.m_historic = new LinkedList<ObjectPair<String, Long>>();
        this.m_actionHistoric = new LinkedList<ObjectPair<Action, Long>>();
        if (!this.m_checking) {
            return;
        }
        for (int i = 0; i < 10; ++i) {
            this.m_historic.add(new ObjectPair<String, Long>("vide-" + i, 0L));
        }
        for (int i = 0; i < 10; ++i) {
            this.m_actionHistoric.add(new ObjectPair<Action, Long>(new Action(null, -1), 0L));
        }
    }
    
    public ControllerAction pushAction(final Action action) {
        return this.pushAction(action, System.currentTimeMillis());
    }
    
    public ControllerAction pushAction(final Action action, final long timestamp) {
        if (!this.m_checking) {
            return ControllerAction.NONE;
        }
        if (action == null) {
            return ControllerAction.ERROR;
        }
        if (this.m_floodChecking) {
            final ControllerAction flood = this.checkForActionFlood(action, timestamp);
            if (flood == ControllerAction.BLOCK_FLOOD) {
                this.updateActionHistoric(action, timestamp);
                return flood;
            }
        }
        this.updateActionHistoric(action, timestamp);
        return ControllerAction.NONE;
    }
    
    public ControllerAction pushMessage(final String message) {
        return this.pushMessage(message, System.currentTimeMillis());
    }
    
    public ControllerAction pushMessage(final String message, final long timestamp) {
        if (!this.m_checking) {
            return ControllerAction.NONE;
        }
        if (message == null) {
            return ControllerAction.ERROR;
        }
        final String entry = cleanSentence(message);
        if (this.m_floodChecking) {
            final ControllerAction flood = this.checkForFlood(entry, timestamp);
            if (flood == ControllerAction.BLOCK_FLOOD) {
                this.updateHistoric(entry, timestamp);
                return flood;
            }
        }
        if (this.m_smsChecking) {
            final ControllerAction sms = this.checkForSms(entry, timestamp);
            if (sms == ControllerAction.BLOCK_SMS_LIGHT || sms == ControllerAction.BLOCK_SMS_HARDCORE) {
                this.updateHistoric(entry, timestamp);
                return sms;
            }
        }
        this.updateHistoric(entry, timestamp);
        return ControllerAction.NONE;
    }
    
    private void updateHistoric(final String message, final long timestamp) {
        final ObjectPair<String, Long> entry = this.m_historic.removeLast();
        entry.setFirst(message);
        entry.setSecond(timestamp);
        this.m_historic.addFirst(entry);
    }
    
    private void updateActionHistoric(final Action action, final long timestamp) {
        final ObjectPair<Action, Long> entry = this.m_actionHistoric.removeLast();
        entry.setFirst(action);
        entry.setSecond(timestamp);
        this.m_actionHistoric.addFirst(entry);
    }
    
    private ControllerAction checkForActionFlood(final Action action, final long timestamp) {
        if (this.m_actionHistoric.get(5).getSecond() + 5000L > timestamp) {
            return ControllerAction.BLOCK_FLOOD;
        }
        int count = 0;
        for (final ObjectPair<Action, Long> entry : this.m_actionHistoric) {
            if (entry.getSecond() + 120000L < timestamp) {
                break;
            }
            if (!action.equals(entry.getFirst())) {
                continue;
            }
            ++count;
        }
        if (count + 1 >= 4) {
            return ControllerAction.BLOCK_FLOOD;
        }
        return ControllerAction.NONE;
    }
    
    private ControllerAction checkForFlood(final String message, final long timestamp) {
        if (this.m_historic.get(5).getSecond() + 5000L > timestamp) {
            return ControllerAction.BLOCK_FLOOD;
        }
        int count = 0;
        for (final ObjectPair<String, Long> entry : this.m_historic) {
            if (entry.getSecond() + 120000L < timestamp) {
                break;
            }
            if (compareSentence(message, entry.getFirst()) < 75) {
                continue;
            }
            ++count;
        }
        if (count + 1 >= 4) {
            return ControllerAction.BLOCK_FLOOD;
        }
        return ControllerAction.NONE;
    }
    
    private ControllerAction checkForSms(final String message, final long timestamp) {
        if (timestamp >= this.m_smsLastDetection + 900000L) {
            this.m_smsLightCount = 0;
            this.m_smsHardcoreCount = 0;
        }
        if (this.countSMSLevel(message) >= 2) {
            ++this.m_smsLightCount;
            ++this.m_smsHardcoreCount;
            this.m_smsLastDetection = timestamp;
            if (this.m_smsHardcoreCount >= 8) {
                return ControllerAction.BLOCK_SMS_HARDCORE;
            }
            if (this.m_smsLightCount >= 3) {
                this.m_smsLightCount = 0;
                return ControllerAction.BLOCK_SMS_LIGHT;
            }
        }
        return ControllerAction.NONE;
    }
    
    private static int compareSentence(final String ref, final String toCompare) {
        if (ref == null || toCompare == null) {
            return 0;
        }
        final int levenshteinDistance = StringUtils.getLevenshteinDistance(ref, toCompare);
        return 100 - (int)(levenshteinDistance / Math.max(ref.length(), toCompare.length()) * 100.0f);
    }
    
    public static String cleanSentence(final String message) {
        String result = message.toLowerCase();
        result = result.replaceAll("(\\?|\\!|\\,|\\;|\\.|\\/|\\*|\\@|\\#|\\\")", "");
        result = result.replaceAll("(\u00e1|\u00e0|\u00e2|\u00e4|\u00e3)", "a");
        result = result.replaceAll("(\u00e8|\u00ea|\u00eb)", "e");
        result = result.replaceAll("(\u00ed|\u00ec|\u00ef|\u00ee)", "i");
        result = result.replaceAll("(\u00f3|\u00f2|\u00f6|\u00f4)", "o");
        result = result.replaceAll("(\u00fa|\u00f9|\u00fc|\u00fb)", "u");
        result = result.replaceAll("(\u00f1)", "n");
        result = result.replaceAll("(\u00e7)", "c");
        result = result.replaceAll("(-)", " ");
        result = result.replaceAll("(')", "' ");
        result = result.replaceAll("([ ]+)", " ");
        result = result.trim();
        return result;
    }
    
    private int countSMSLevel(final String sentence) {
        if (SmsAndFloodController.m_smsPattern == null) {
            return -1;
        }
        final ArrayList<String> smsWords = new ArrayList<String>();
        final String[] arr$;
        final String[] worlds = arr$ = com.ankamagames.framework.kernel.utils.StringUtils.split(sentence.trim(), ' ');
        for (final String w : arr$) {
            final Matcher matcher = SmsAndFloodController.m_smsPattern.matcher(w.trim());
            if (matcher.find() && !smsWords.contains(matcher.group().trim())) {
                smsWords.add(matcher.group().trim());
            }
        }
        return smsWords.size();
    }
    
    public static void setLanguage(final Language lang) {
        switch (lang) {
            case FR: {
                SmsAndFloodController.m_smsPattern = SmsAndFloodController.m_smsPattern_fr;
                break;
            }
            default: {
                SmsAndFloodController.m_smsPattern = null;
                break;
            }
        }
    }
    
    private static void checkValidityList(final String label, final String[] words) {
        boolean isValid = true;
        for (final String s : words) {
            int count = 0;
            for (final String w : words) {
                if (w.equalsIgnoreCase(s) && ++count > 1) {
                    isValid = false;
                    System.out.println("\t! [" + label + "] " + "Multiples correspondances : " + s + ", " + w);
                }
            }
            if (s.matches("((.|\n)*(\u00e8|\u00ea|\u00eb|\u00e0|\u00e2|\u00e4|\u00ec|\u00ef|\u00ee|\u00f2|\u00f6|\u00f4|\u00f9|\u00fc|\u00fb|\\?|\\!|\\,|\\;|\\.|\\/|\\*|\\@|\\#|\\\")(.|\n)*)")) {
                isValid = false;
                System.out.println("\t! [" + label + "] " + "Contient des caract\u00e8res ignor\u00e9s : " + s);
            }
        }
        System.out.println("[" + label + "] " + "Liste de mots valide : " + (isValid ? "OK." : "ERROR."));
    }
    
    public static void main(final String[] args) {
        setLanguage(Language.FR);
        checkValidityList("FR", SmsAndFloodController.SMS_WORDS_LIST_FR);
        final String text1 = "!! Hello les gens comment      tu vas ?! Bien ? Ici, c'est super cool *top m\u00e9ga g\u00e8ni\u00e0l*! On se croirait a \"Saint-Laurent-du-var\"!!";
        System.out.println("-----");
        System.out.println("Phrase       : !! Hello les gens comment      tu vas ?! Bien ? Ici, c'est super cool *top m\u00e9ga g\u00e8ni\u00e0l*! On se croirait a \"Saint-Laurent-du-var\"!!");
        System.out.println("Phrase clean : " + cleanSentence("!! Hello les gens comment      tu vas ?! Bien ? Ici, c'est super cool *top m\u00e9ga g\u00e8ni\u00e0l*! On se croirait a \"Saint-Laurent-du-var\"!!"));
        final String text2 = cleanSentence("Ceci est une phrase test d'une longueur   de 10 mots");
        final String text3 = cleanSentence("Ceci est une phrase bidon d'une taille de 8 mots");
        System.out.println("-----");
        System.out.println("Phrase 1 : " + text2);
        System.out.println("Phrase 2 : " + text3);
        System.out.println("Compare  : " + compareSentence(text2, text3) + "%");
        final String text4 = cleanSentence("\u00e9 si tu  \u00e9 t changes un peu ta phrase, au bout sde 10 il se rend compte que c'est presque la m\u00eame et t'as pas le droit non plus");
        System.out.println("-----");
        System.out.println("Phrase test SMS : " + cleanSentence(text4));
        System.out.println("SMS d\u00e9tect\u00e9 : " + (SmsAndFloodController.m_instance.countSMSLevel(text4) >= 2));
        System.out.println("-----");
        final String tmp = "19500";
        final Pattern p = Pattern.compile("^([^1][0-9]*)$");
        final Matcher matcher = p.matcher("19500");
        System.out.println("Match " + matcher.matches());
    }
    
    static {
        m_instance = new SmsAndFloodController();
        SMS_WORDS_LIST_FR = new String[] { "nn", "t1", "tien", "mang", "vnd", "t\u00e9", "fet", "drol", "r\u00e9pon", "kool", "tro", "parl", "toa", "conpran", "mo", "gl", "ak", "chui", "m\u00e9", "j\u00e9", "tkt", "tou", "comen", "kom", "ogmente", "ji", "esque", "tt", "kelk1", "bi1", "esk", "eske", "doi", "pl1", "nouvo", "veu", "dir", "cose", "gt", "jer", "pr", "serer", "kkn", "tve", "vremen", "vreman", "psk", "osi", "qq1", "orai", "coser", "pe", "tap", "cuncun", "taleur", "p1n", "v\u00e9", "kel", "kelle", "kan", "kelkin", "coman", "com", "pt1", "f\u00e9", "f\u00e9re", "coner", "fo", "conner", "tr\u00e9", "kk1", "ls", "ven", "pren", "sava", "b1", "sui", "gen", "dan", "jv\u00e9", "ke", "jen", "mar", "vr\u00e9", "fon", "pk", "ka", "jpe", "kik", "kkun", "ki", "con\u00e9", "conn\u00e9", "r\u00e9pond\u00e9", "r1", "p1", "devi1", "koman", "komen", "tlm", "kess", "koi", "tm", "di", "ss", "mnt", "cmb", "2m1", "ac", "issi", "pq", "pour\u00e9", "d1", "langaj", "ds", "\u00e9", "c", "selman", "paske", "an\u00e9", "ser", "dvu", "dla", "pt", "l\u00e9", "propoz", "ms", "t", "g", "s\u00e9", "slt", "vs", "ns", "slmt", "alor", "jt\u00e9", "kon", "dsl", "bjr", "mm", "fair", "f\u00e9r", "pa", "gg", "jte", "d", "qq", "kelke", "repren", "cetai", "mai", "dacor", "plai", "mci", "esqui", "quan", "desoler", "jecri", "kestion", "ve", "qqn", "vien", "cmt", "cb", "bo", "hom", "fam", "kelkun", "konba", "ske", "franc\u00e9", "cop1", "copin", "pourkoi", "peti", "stille", "dotre", "kil", "jam\u00e9", "grav", "pev", "kompendr", "trouv", "peuv", "att", "achet", "ano", "coif", "coiff", "deu", "pl\u00e9", "plin", "tjs", "dr1", "jve", "coi", "lon", "fai", "ekipement", "jofre", "kelkechose", "kkchose", "nivo", "cintur", "cinture", "equipemen", "commen", "tar", "pcq", "fallai", "ptin", "fau", "srx", "nou", "vou", "\u00e9kip", "vo", "tor\u00e9", "rep", "qi", "sal", "voi", "kelchose", "jven", "dj", "chu", "qe", "choi", "plu", "m\u00e9d\u00e9", "mark\u00e9", "jait\u00e9", "cons\u00e9", "bocoup", "connaiss\u00e9", "deg", "d\u00e9g", "maren", "maran", "vai" };
        String fr = "\\A(";
        for (int i = 0; i < SmsAndFloodController.SMS_WORDS_LIST_FR.length; ++i) {
            if (i > 0) {
                fr += "|";
            }
            fr += SmsAndFloodController.SMS_WORDS_LIST_FR[i];
        }
        fr += ")\\z";
        m_smsPattern_fr = Pattern.compile(fr, 2);
    }
    
    public enum ControllerAction
    {
        ERROR, 
        NONE, 
        BLOCK_FLOOD, 
        BLOCK_SMS_LIGHT, 
        BLOCK_SMS_HARDCORE;
    }
}
