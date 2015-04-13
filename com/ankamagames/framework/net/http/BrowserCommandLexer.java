package com.ankamagames.framework.net.http;

import java.io.*;

public final class BrowserCommandLexer
{
    private static final int YYEOF = -1;
    private static final int ZZ_BUFFERSIZE = 16384;
    private static final int YYINITIAL = 0;
    private static final int[] ZZ_LEXSTATE;
    private static final String ZZ_CMAP_PACKED = "\t\u0000\u0002\u0002\u0001\u0000\u0002\u0002\u0012\u0000\u0001\u0002\u0001\u0000\u0001\u0003\u001e\u0000\u0001\u0000\u001a\u0000\u0001\u0001\uffa3\u0000";
    private static final char[] ZZ_CMAP;
    private static final int[] ZZ_ACTION;
    private static final String ZZ_ACTION_PACKED_0 = "\u0002\u0001\u0002\u0002\u0001\u0001\u0003\u0000\u0001\u0003\u0001\u0001\u0001\u0003\u0001\u0000\u0002\u0003";
    private static final int[] ZZ_ROWMAP;
    private static final String ZZ_ROWMAP_PACKED_0 = "\u0000\u0000\u0000\u0004\u0000\b\u0000\f\u0000\u0010\u0000\b\u0000\u0014\u0000\u0018\u0000\u0004\u0000\u001c\u0000\u0010\u0000 \u0000\f\u0000\u0018";
    private static final int[] ZZ_TRANS;
    private static final String ZZ_TRANS_PACKED_0 = "\u0001\u0002\u0001\u0003\u0001\u0004\u0001\u0005\u0001\u0002\u0001\u0006\u0001\u0000\u0005\u0002\u0004\u0000\u0001\u0005\u0001\u0007\u0001\b\u0001\t\u0001\u0005\u0001\n\u0001\u0005\u0001\u000b\u0001\b\u0001\f\u0001\b\u0001\r\u0001\u0005\u0001\u0007\u0001\b\u0001\u000b\u0001\b\u0001\f\u0001\b\u0001\u000e";
    private static final int ZZ_UNKNOWN_ERROR = 0;
    private static final int ZZ_NO_MATCH = 1;
    private static final int ZZ_PUSHBACK_2BIG = 2;
    private static final String[] ZZ_ERROR_MSG;
    private static final int[] ZZ_ATTRIBUTE;
    private static final String ZZ_ATTRIBUTE_PACKED_0 = "\u0003\u0001\u0001\t\u0001\u0001\u0003\u0000\u0003\u0001\u0001\u0000\u0001\t\u0001\u0001";
    private Reader zzReader;
    private int zzState;
    private int zzLexicalState;
    private char[] zzBuffer;
    private int zzMarkedPos;
    private int zzCurrentPos;
    private int zzStartRead;
    private int zzEndRead;
    private int yyline;
    private int yychar;
    private int yycolumn;
    private boolean zzAtBOL;
    private boolean zzAtEOF;
    
    private static int[] zzUnpackAction() {
        final int[] result = new int[14];
        int offset = 0;
        offset = zzUnpackAction("\u0002\u0001\u0002\u0002\u0001\u0001\u0003\u0000\u0001\u0003\u0001\u0001\u0001\u0003\u0001\u0000\u0002\u0003", offset, result);
        return result;
    }
    
    private static int zzUnpackAction(final String packed, final int offset, final int[] result) {
        int i = 0;
        int j = offset;
        final int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            final int value = packed.charAt(i++);
            do {
                result[j++] = value;
            } while (--count > 0);
        }
        return j;
    }
    
    private static int[] zzUnpackRowMap() {
        final int[] result = new int[14];
        int offset = 0;
        offset = zzUnpackRowMap("\u0000\u0000\u0000\u0004\u0000\b\u0000\f\u0000\u0010\u0000\b\u0000\u0014\u0000\u0018\u0000\u0004\u0000\u001c\u0000\u0010\u0000 \u0000\f\u0000\u0018", offset, result);
        return result;
    }
    
    private static int zzUnpackRowMap(final String packed, final int offset, final int[] result) {
        int i = 0;
        int j = offset;
        int high;
        for (int l = packed.length(); i < l; high = packed.charAt(i++) << 16, result[j++] = (high | packed.charAt(i++))) {}
        return j;
    }
    
    private static int[] zzUnpackTrans() {
        final int[] result = new int[36];
        int offset = 0;
        offset = zzUnpackTrans("\u0001\u0002\u0001\u0003\u0001\u0004\u0001\u0005\u0001\u0002\u0001\u0006\u0001\u0000\u0005\u0002\u0004\u0000\u0001\u0005\u0001\u0007\u0001\b\u0001\t\u0001\u0005\u0001\n\u0001\u0005\u0001\u000b\u0001\b\u0001\f\u0001\b\u0001\r\u0001\u0005\u0001\u0007\u0001\b\u0001\u000b\u0001\b\u0001\f\u0001\b\u0001\u000e", offset, result);
        return result;
    }
    
    private static int zzUnpackTrans(final String packed, final int offset, final int[] result) {
        int i = 0;
        int j = offset;
        final int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            --value;
            do {
                result[j++] = value;
            } while (--count > 0);
        }
        return j;
    }
    
    private static int[] zzUnpackAttribute() {
        final int[] result = new int[14];
        int offset = 0;
        offset = zzUnpackAttribute("\u0003\u0001\u0001\t\u0001\u0001\u0003\u0000\u0003\u0001\u0001\u0000\u0001\t\u0001\u0001", offset, result);
        return result;
    }
    
    private static int zzUnpackAttribute(final String packed, final int offset, final int[] result) {
        int i = 0;
        int j = offset;
        final int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            final int value = packed.charAt(i++);
            do {
                result[j++] = value;
            } while (--count > 0);
        }
        return j;
    }
    
    private static void main(final String[] args) {
        try {
            InputStream in;
            if (args.length > 0) {
                final File f = new File(args[0]);
                if (!f.exists()) {
                    throw new IOException("Could not find " + args[0]);
                }
                if (!f.canRead()) {
                    throw new IOException("Could not open " + args[0]);
                }
                in = new FileInputStream(f);
            }
            else {
                in = System.in;
            }
            final BrowserCommandLexer shredder = new BrowserCommandLexer(in);
            String t;
            while ((t = shredder.getNextToken()) != null) {
                System.out.println(t);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public String getNextToken() throws IOException {
        return this.getToken();
    }
    
    private static String unescape(final String s) {
        final StringBuffer sb = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '\\' && i < s.length()) {
                ++i;
            }
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }
    
    BrowserCommandLexer(final Reader in) {
        super();
        this.zzLexicalState = 0;
        this.zzBuffer = new char[16384];
        this.zzAtBOL = true;
        this.zzReader = in;
    }
    
    BrowserCommandLexer(final InputStream in) {
        this(new InputStreamReader(in));
    }
    
    private static char[] zzUnpackCMap(final String packed) {
        final char[] map = new char[65536];
        int i = 0;
        int j = 0;
        while (i < 26) {
            int count = packed.charAt(i++);
            final char value = packed.charAt(i++);
            do {
                map[j++] = value;
            } while (--count > 0);
        }
        return map;
    }
    
    private boolean zzRefill() throws IOException {
        if (this.zzStartRead > 0) {
            System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
            this.zzEndRead -= this.zzStartRead;
            this.zzCurrentPos -= this.zzStartRead;
            this.zzMarkedPos -= this.zzStartRead;
            this.zzStartRead = 0;
        }
        if (this.zzCurrentPos >= this.zzBuffer.length) {
            final char[] newBuffer = new char[this.zzCurrentPos * 2];
            System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
            this.zzBuffer = newBuffer;
        }
        final int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
        if (numRead > 0) {
            this.zzEndRead += numRead;
            return false;
        }
        if (numRead != 0) {
            return true;
        }
        final int c = this.zzReader.read();
        if (c == -1) {
            return true;
        }
        this.zzBuffer[this.zzEndRead++] = (char)c;
        return false;
    }
    
    private final void yyclose() throws IOException {
        this.zzAtEOF = true;
        this.zzEndRead = this.zzStartRead;
        if (this.zzReader != null) {
            this.zzReader.close();
        }
    }
    
    private final void yyreset(final Reader reader) {
        this.zzReader = reader;
        this.zzAtBOL = true;
        this.zzAtEOF = false;
        final boolean b = false;
        this.zzStartRead = (b ? 1 : 0);
        this.zzEndRead = (b ? 1 : 0);
        final boolean b2 = false;
        this.zzMarkedPos = (b2 ? 1 : 0);
        this.zzCurrentPos = (b2 ? 1 : 0);
        final boolean yyline = false;
        this.yycolumn = (yyline ? 1 : 0);
        this.yychar = (yyline ? 1 : 0);
        this.yyline = (yyline ? 1 : 0);
        this.zzLexicalState = 0;
    }
    
    private final int yystate() {
        return this.zzLexicalState;
    }
    
    private final void yybegin(final int newState) {
        this.zzLexicalState = newState;
    }
    
    private final String yytext() {
        return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
    }
    
    private final char yycharat(final int pos) {
        return this.zzBuffer[this.zzStartRead + pos];
    }
    
    private final int yylength() {
        return this.zzMarkedPos - this.zzStartRead;
    }
    
    private void zzScanError(final int errorCode) {
        String message;
        try {
            message = BrowserCommandLexer.ZZ_ERROR_MSG[errorCode];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            message = BrowserCommandLexer.ZZ_ERROR_MSG[0];
        }
        throw new Error(message);
    }
    
    private void yypushback(final int number) {
        if (number > this.yylength()) {
            this.zzScanError(2);
        }
        this.zzMarkedPos -= number;
    }
    
    private String getToken() throws IOException {
        int zzEndReadL = this.zzEndRead;
        char[] zzBufferL = this.zzBuffer;
        final char[] zzCMapL = BrowserCommandLexer.ZZ_CMAP;
        final int[] zzTransL = BrowserCommandLexer.ZZ_TRANS;
        final int[] zzRowMapL = BrowserCommandLexer.ZZ_ROWMAP;
        final int[] zzAttrL = BrowserCommandLexer.ZZ_ATTRIBUTE;
        while (true) {
            int zzMarkedPosL = this.zzMarkedPos;
            int zzAction = -1;
            final int n = zzMarkedPosL;
            this.zzStartRead = n;
            this.zzCurrentPos = n;
            int zzCurrentPosL = n;
            this.zzState = BrowserCommandLexer.ZZ_LEXSTATE[this.zzLexicalState];
            int zzInput;
            while (true) {
                if (zzCurrentPosL < zzEndReadL) {
                    zzInput = zzBufferL[zzCurrentPosL++];
                }
                else {
                    if (this.zzAtEOF) {
                        zzInput = -1;
                        break;
                    }
                    this.zzCurrentPos = zzCurrentPosL;
                    this.zzMarkedPos = zzMarkedPosL;
                    final boolean eof = this.zzRefill();
                    zzCurrentPosL = this.zzCurrentPos;
                    zzMarkedPosL = this.zzMarkedPos;
                    zzBufferL = this.zzBuffer;
                    zzEndReadL = this.zzEndRead;
                    if (eof) {
                        zzInput = -1;
                        break;
                    }
                    zzInput = zzBufferL[zzCurrentPosL++];
                }
                final int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
                if (zzNext == -1) {
                    break;
                }
                this.zzState = zzNext;
                final int zzAttributes = zzAttrL[this.zzState];
                if ((zzAttributes & 0x1) != 0x1) {
                    continue;
                }
                zzAction = this.zzState;
                zzMarkedPosL = zzCurrentPosL;
                if ((zzAttributes & 0x8) == 0x8) {
                    break;
                }
            }
            this.zzMarkedPos = zzMarkedPosL;
            switch ((zzAction < 0) ? zzAction : BrowserCommandLexer.ZZ_ACTION[zzAction]) {
                case 3: {
                    return unescape(this.yytext().substring(1, this.yytext().length() - 1));
                }
                case 4: {
                    continue;
                }
                case 2:
                case 5: {
                    continue;
                }
                case 1: {
                    return unescape(this.yytext());
                }
                case 6: {
                    continue;
                }
                default: {
                    if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
                        this.zzAtEOF = true;
                        return null;
                    }
                    this.zzScanError(1);
                    continue;
                }
            }
        }
    }
    
    static {
        ZZ_LEXSTATE = new int[] { 0, 0 };
        ZZ_CMAP = zzUnpackCMap("\t\u0000\u0002\u0002\u0001\u0000\u0002\u0002\u0012\u0000\u0001\u0002\u0001\u0000\u0001\u0003\u001e\u0000\u0001\u0000\u001a\u0000\u0001\u0001\uffa3\u0000");
        ZZ_ACTION = zzUnpackAction();
        ZZ_ROWMAP = zzUnpackRowMap();
        ZZ_TRANS = zzUnpackTrans();
        ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
        ZZ_ATTRIBUTE = zzUnpackAttribute();
    }
}
