package com.ankamagames.baseImpl.common.clientAndServer.game.parameter;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public class ParametersChecker
{
    public static boolean checkType(final Parameterized parameterized, final ArrayList<ParserObject> args) {
        if (args == null || args.size() == 0) {
            final Iterator<ParameterList> it = parameterized.getParametersListSet().iterator();
            while (it.hasNext()) {
                if (it.next().getParametersCount() == 0) {
                    return true;
                }
            }
            return false;
        }
        for (final ParameterList list : parameterized.getParametersListSet()) {
            boolean fixedSize = true;
            boolean valid = true;
            int i1 = 0;
            for (byte j = 0; j < args.size(); ++j) {
                if (i1 > list.getParametersCount() - 1) {
                    valid = false;
                    break;
                }
                if (((ParserObjectParameter)list.getParameter(i1)).getType() == ParserType.NUMBERLIST && args.get(j).getType() != ParserType.NUMBERLIST) {
                    fixedSize = false;
                    boolean endReached = true;
                    while (j < args.size()) {
                        if (args.get(j) == null || args.get(j).getType() != ParserType.NUMBER) {
                            endReached = false;
                            break;
                        }
                        ++j;
                    }
                    if (endReached) {
                        if (i1 != list.getParametersCount() - 1) {
                            valid = false;
                            break;
                        }
                        return true;
                    }
                    else {
                        --j;
                        ++i1;
                    }
                }
                else if (((ParserObjectParameter)list.getParameter(i1)).getType() == ParserType.STRINGLIST) {
                    fixedSize = false;
                    boolean endReached = true;
                    for (int size = args.size(); j < size; ++j) {
                        if (args.get(j).getType() != ParserType.STRING) {
                            endReached = false;
                            break;
                        }
                    }
                    if (endReached) {
                        if (i1 != list.getParametersCount() - 1) {
                            valid = false;
                            break;
                        }
                        return true;
                    }
                    else {
                        --j;
                        ++i1;
                    }
                }
                else if (((ParserObjectParameter)list.getParameter(i1)).getType() == ParserType.POSITIONLIST) {
                    fixedSize = false;
                    boolean endReached = true;
                    while (j < args.size()) {
                        if (args.get(j).getType() != ParserType.POSITION) {
                            endReached = false;
                            break;
                        }
                        ++j;
                    }
                    if (endReached) {
                        if (i1 != list.getParametersCount() - 1) {
                            valid = false;
                            break;
                        }
                        return true;
                    }
                    else {
                        --j;
                        ++i1;
                    }
                }
                else {
                    if (args.get(j) == null) {
                        valid = false;
                        break;
                    }
                    if (args.get(j).getType() != ((ParserObjectParameter)list.getParameter(i1)).getType()) {
                        valid = false;
                        break;
                    }
                    ++i1;
                }
            }
            if (valid) {
                if (fixedSize && args.size() != list.getParametersCount()) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
}
