package org.apache.commons.lang3;

import org.apache.commons.lang3.text.translate.*;
import java.io.*;

public class StringEscapeUtils
{
    public static final CharSequenceTranslator ESCAPE_JAVA;
    public static final CharSequenceTranslator ESCAPE_ECMASCRIPT;
    public static final CharSequenceTranslator ESCAPE_XML;
    public static final CharSequenceTranslator ESCAPE_HTML3;
    public static final CharSequenceTranslator ESCAPE_HTML4;
    public static final CharSequenceTranslator ESCAPE_CSV;
    public static final CharSequenceTranslator UNESCAPE_JAVA;
    public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT;
    public static final CharSequenceTranslator UNESCAPE_HTML3;
    public static final CharSequenceTranslator UNESCAPE_HTML4;
    public static final CharSequenceTranslator UNESCAPE_XML;
    public static final CharSequenceTranslator UNESCAPE_CSV;
    
    public static final String escapeJava(final String input) {
        return StringEscapeUtils.ESCAPE_JAVA.translate(input);
    }
    
    public static final String escapeEcmaScript(final String input) {
        return StringEscapeUtils.ESCAPE_ECMASCRIPT.translate(input);
    }
    
    public static final String unescapeJava(final String input) {
        return StringEscapeUtils.UNESCAPE_JAVA.translate(input);
    }
    
    public static final String unescapeEcmaScript(final String input) {
        return StringEscapeUtils.UNESCAPE_ECMASCRIPT.translate(input);
    }
    
    public static final String escapeHtml4(final String input) {
        return StringEscapeUtils.ESCAPE_HTML4.translate(input);
    }
    
    public static final String escapeHtml3(final String input) {
        return StringEscapeUtils.ESCAPE_HTML3.translate(input);
    }
    
    public static final String unescapeHtml4(final String input) {
        return StringEscapeUtils.UNESCAPE_HTML4.translate(input);
    }
    
    public static final String unescapeHtml3(final String input) {
        return StringEscapeUtils.UNESCAPE_HTML3.translate(input);
    }
    
    public static final String escapeXml(final String input) {
        return StringEscapeUtils.ESCAPE_XML.translate(input);
    }
    
    public static final String unescapeXml(final String input) {
        return StringEscapeUtils.UNESCAPE_XML.translate(input);
    }
    
    public static final String escapeCsv(final String input) {
        return StringEscapeUtils.ESCAPE_CSV.translate(input);
    }
    
    public static final String unescapeCsv(final String input) {
        return StringEscapeUtils.UNESCAPE_CSV.translate(input);
    }
    
    static {
        ESCAPE_JAVA = new LookupTranslator((CharSequence[][])new String[][] { { "\"", "\\\"" }, { "\\", "\\\\" } }).with(new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_ESCAPE())).with(UnicodeEscaper.outsideOf(32, 127));
        ESCAPE_ECMASCRIPT = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])new String[][] { { "'", "\\'" }, { "\"", "\\\"" }, { "\\", "\\\\" }, { "/", "\\/" } }), new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), UnicodeEscaper.outsideOf(32, 127) });
        ESCAPE_XML = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.APOS_ESCAPE()) });
        ESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_ESCAPE()) });
        ESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_ESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.HTML40_EXTENDED_ESCAPE()) });
        ESCAPE_CSV = new CsvEscaper();
        UNESCAPE_JAVA = new AggregateTranslator(new CharSequenceTranslator[] { new OctalUnescaper(), new UnicodeUnescaper(), new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()), new LookupTranslator((CharSequence[][])new String[][] { { "\\\\", "\\" }, { "\\\"", "\"" }, { "\\'", "'" }, { "\\", "" } }) });
        UNESCAPE_ECMASCRIPT = StringEscapeUtils.UNESCAPE_JAVA;
        UNESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_UNESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
        UNESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_UNESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_UNESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.HTML40_EXTENDED_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
        UNESCAPE_XML = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_UNESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.APOS_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
        UNESCAPE_CSV = new CsvUnescaper();
    }
    
    static class CsvEscaper extends CharSequenceTranslator
    {
        private static final char CSV_DELIMITER = ',';
        private static final char CSV_QUOTE = '\"';
        private static final String CSV_QUOTE_STR;
        private static final char[] CSV_SEARCH_CHARS;
        
        public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
            if (index != 0) {
                throw new IllegalStateException("CsvEscaper should never reach the [1] index");
            }
            if (StringUtils.containsNone(input.toString(), CsvEscaper.CSV_SEARCH_CHARS)) {
                out.write(input.toString());
            }
            else {
                out.write(34);
                out.write(StringUtils.replace(input.toString(), CsvEscaper.CSV_QUOTE_STR, CsvEscaper.CSV_QUOTE_STR + CsvEscaper.CSV_QUOTE_STR));
                out.write(34);
            }
            return input.length();
        }
        
        static {
            CSV_QUOTE_STR = String.valueOf('\"');
            CSV_SEARCH_CHARS = new char[] { ',', '\"', '\r', '\n' };
        }
    }
    
    static class CsvUnescaper extends CharSequenceTranslator
    {
        private static final char CSV_DELIMITER = ',';
        private static final char CSV_QUOTE = '\"';
        private static final String CSV_QUOTE_STR;
        private static final char[] CSV_SEARCH_CHARS;
        
        public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
            if (index != 0) {
                throw new IllegalStateException("CsvUnescaper should never reach the [1] index");
            }
            if (input.charAt(0) != '\"' || input.charAt(input.length() - 1) != '\"') {
                out.write(input.toString());
                return input.length();
            }
            final String quoteless = input.subSequence(1, input.length() - 1).toString();
            if (StringUtils.containsAny(quoteless, CsvUnescaper.CSV_SEARCH_CHARS)) {
                out.write(StringUtils.replace(quoteless, CsvUnescaper.CSV_QUOTE_STR + CsvUnescaper.CSV_QUOTE_STR, CsvUnescaper.CSV_QUOTE_STR));
            }
            else {
                out.write(input.toString());
            }
            return input.length();
        }
        
        static {
            CSV_QUOTE_STR = String.valueOf('\"');
            CSV_SEARCH_CHARS = new char[] { ',', '\"', '\r', '\n' };
        }
    }
}
