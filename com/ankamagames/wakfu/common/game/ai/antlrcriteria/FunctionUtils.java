package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public class FunctionUtils
{
    public static byte checkType(final String classname, final List<ParserType[]> signatures, final List<ParserObject> args) {
        if (args == null) {
            return -1;
        }
        for (byte j = 0; j < signatures.size(); ++j) {
            final ParserType[] signature = signatures.get(j);
            boolean fixedSize = true;
            boolean error = false;
            byte index = 0;
            final int signatureLastIndex = signature.length - 1;
            for (byte i = 0; i < args.size(); ++i) {
                if (index > signatureLastIndex) {
                    error = true;
                    break;
                }
                final ParserType argType = args.get(i).getType();
                if (signature[index] == ParserType.NUMBERLIST && argType != ParserType.NUMBERLIST) {
                    fixedSize = false;
                    boolean endReached = true;
                    while (i < args.size()) {
                        if (argType != ParserType.NUMBER) {
                            endReached = false;
                            break;
                        }
                        ++i;
                    }
                    if (endReached) {
                        if (index != signatureLastIndex) {
                            error = true;
                            break;
                        }
                        return j;
                    }
                    else {
                        --i;
                        ++index;
                    }
                }
                else if (signature[index] == ParserType.STRINGLIST) {
                    fixedSize = false;
                    boolean endReached = true;
                    while (i < args.size()) {
                        if (argType != ParserType.STRING) {
                            endReached = false;
                            break;
                        }
                        ++i;
                    }
                    if (endReached) {
                        if (index != signatureLastIndex) {
                            error = true;
                            break;
                        }
                        return j;
                    }
                    else {
                        --i;
                        ++index;
                    }
                }
                else {
                    if (argType != signature[index]) {
                        error = true;
                        break;
                    }
                    ++index;
                }
            }
            if (!error && (!fixedSize || args.size() == signature.length)) {
                return j;
            }
        }
        String sigs = "La fonction " + classname + " n'est pas utilis\u00e9e avec le bon nombre (ou type) d'arguments. \n";
        sigs += "Les arguments possibles sont :";
        sigs += getSignatureString(signatures);
        throw new ParseException(sigs);
    }
    
    private static String getSignatureString(final Iterable<ParserType[]> signatures) {
        final StringBuilder sb = new StringBuilder();
        for (final ParserType[] sig : signatures) {
            sb.append("\n (");
            appendTypes(sb, sig);
            sb.append(')');
        }
        return sb.toString();
    }
    
    private static void appendTypes(final StringBuilder sb, final ParserType... sig) {
        final int length = sig.length;
        if (length == 0) {
            return;
        }
        sb.append(sig[0].name());
        for (byte i = 1; i < length; ++i) {
            sb.append(", ").append(sig[i].name());
        }
    }
}
