package com.ankamagames.baseImpl.common.clientAndServer.utils;

public enum SmileyEnum
{
    HAPPY_SMILEYS(0, new String[] { ":)", ":]", ":}", "=)", "=]", ":-)", ":o)" }, "AnimSourire"), 
    SAD_SMILEYS(1, new String[] { ":(", ":[", ":{", "=(", "=/", ":-(", "._.", "v_v", "\u00e9_\u00e8" }, "AnimTriste"), 
    WINK_SMILEYS(2, new String[] { ";)", ";]", ";-)", ",-)", "*-)" }, "AnimClinOeil"), 
    VERY_HAPPY_SMILEYS(3, new String[] { ":D", "=D", "8D", "|D", ":-D" }, "AnimGrosSourire"), 
    SUFFERING_SMILEYS(4, new String[] { ":/", "X/", "X'(", ":*(", "X_+", "+_X", ":s", ":S", ":x", ":-s", ":-S", "'x'", "(°~°)" }, "AnimMalade"), 
    YIPPEE_SMILEYS(5, new String[] { "xD", "8D", "X3", "XD" }, "AnimMoqueur"), 
    INLOVE_SMILEYS(6, new String[] { ";D", "*o*", "*-*", "*3*", "°3°", "^3^", "8-)", "8)" }, "AnimAmoureux"), 
    BLAZED_SMILEYS(7, new String[] { "-_-", "-.-", ":-|", ":|", "'-_-", "'_'", "-.-'", "--'" }, "AnimBlaser"), 
    ANGRY_SMILEYS(8, new String[] { ":@", "\u00e8_\u00e9", ">(", ">:(" }, "AnimColere"), 
    MOCKER_SMILEYS(9, new String[] { ":p", ":b", ":P", ":6", ":9", ":-p", ":-b", ":-P" }, "AnimTireLangue"), 
    FEAR_SMILEYS(10, new String[] { ":O", "D:", ":-O", "O.O", "O_O" }, "AnimPeur"), 
    CRY_SMILEYS(11, new String[] { ":'(", ":'-(", ";_;", "T-T", "T_T", "u_u", "U_U", "q_q", "QQ" }, "AnimPleure"), 
    ENTHUSIASTIC_SMILEYS(12, new String[] { ":$", "^^", "^.^", "^_^", "n_n", "(^^)", "(^.^)", "(^_^)", "^^'" }, "AnimRougit"), 
    SURPRISED_SMILEYS(13, new String[] { ":o", ":0", "^^'", "-_-'", "'_'", "'A'", "'o'", "o_o" }, "AnimSueur"), 
    ASTONISHED_SMILEYS(14, new String[] { ":-E", "\u00e8_\u00e9", "X(", "x(", "x[", "@_@", "OwO", "¤_¤" }, "AnimEnrager"), 
    QUESTION_SMILEYS(15, new String[] { "??", ":?" }, "AnimInterogation"), 
    IDEA_SMILEYS(16, new String[] { "oo*", "oO*", "OO*", "Oo*" }, "AnimIdee"), 
    HEART_SMILEYS(17, new String[] { "{3", ":heart:", "K3|", "K3I" }, "AnimCoeur");
    
    private final int m_id;
    private final String[] m_smileys;
    private final String m_commandText;
    private final String m_animation;
    
    private SmileyEnum(final int id, final String[] smileys, final String animation) {
        this.m_id = id;
        this.m_smileys = smileys;
        this.m_commandText = createCommandText(smileys);
        this.m_animation = animation;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getCommandText() {
        return this.m_commandText;
    }
    
    public String getDefaultSmiley() {
        return this.m_smileys[0];
    }
    
    public String getAnimation() {
        return this.m_animation;
    }
    
    public static int formatEmoteIconId(final int emoteId, final int breedFamilyId) {
        return breedFamilyId * 100 + emoteId;
    }
    
    public static SmileyEnum getSmileyFromId(final int id) {
        for (final SmileyEnum smileyEnum : values()) {
            if (id == smileyEnum.m_id) {
                return smileyEnum;
            }
        }
        return null;
    }
    
    public boolean equalsText(final String text) {
        for (final String smiley : this.m_smileys) {
            if (smiley.equals(text)) {
                return true;
            }
        }
        return false;
    }
    
    private static String createCommandText(final String[] smileys) {
        String commandText = "";
        for (int j = 0; j < smileys.length; ++j) {
            final String smiley = smileys[j];
            commandText += normalize(smiley);
            if (j < smileys.length - 1) {
                commandText += " ";
            }
        }
        return commandText;
    }
    
    private static String normalize(final String smiley) {
        return smiley;
    }
}
