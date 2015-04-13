package org.jdom;

import java.util.*;
import org.jdom.output.*;

public class ProcessingInstruction extends Content
{
    private static final String CVS_ID = "@(#) $RCSfile: ProcessingInstruction.java,v $ $Revision: 1.46 $ $Date: 2004/02/27 11:32:57 $ $Name: jdom_1_0 $";
    protected String target;
    protected String rawData;
    protected Map mapData;
    
    protected ProcessingInstruction() {
        super();
    }
    
    public ProcessingInstruction(final String target, final String data) {
        super();
        this.setTarget(target);
        this.setData(data);
    }
    
    public ProcessingInstruction(final String target, final Map data) {
        super();
        this.setTarget(target);
        this.setData(data);
    }
    
    public Object clone() {
        final ProcessingInstruction pi = (ProcessingInstruction)super.clone();
        if (this.mapData != null) {
            pi.mapData = this.parseData(this.rawData);
        }
        return pi;
    }
    
    private static int[] extractQuotedString(final String rawData) {
        boolean inQuotes = false;
        char quoteChar = '\"';
        int start = 0;
        for (int pos = 0; pos < rawData.length(); ++pos) {
            final char currentChar = rawData.charAt(pos);
            if (currentChar == '\"' || currentChar == '\'') {
                if (!inQuotes) {
                    quoteChar = currentChar;
                    inQuotes = true;
                    start = pos + 1;
                }
                else if (quoteChar == currentChar) {
                    inQuotes = false;
                    return new int[] { start, pos };
                }
            }
        }
        return null;
    }
    
    public String getData() {
        return this.rawData;
    }
    
    public List getPseudoAttributeNames() {
        final Set mapDataSet = this.mapData.entrySet();
        final List nameList = new ArrayList();
        final Iterator i = mapDataSet.iterator();
        while (i.hasNext()) {
            final String wholeSet = i.next().toString();
            final String attrName = wholeSet.substring(0, wholeSet.indexOf("="));
            nameList.add(attrName);
        }
        return nameList;
    }
    
    public String getPseudoAttributeValue(final String name) {
        return this.mapData.get(name);
    }
    
    public String getTarget() {
        return this.target;
    }
    
    public String getValue() {
        return this.rawData;
    }
    
    private Map parseData(final String rawData) {
        final Map data = new HashMap();
        String inputData = rawData.trim();
        while (!inputData.trim().equals("")) {
            String name = "";
            String value = "";
            int startName = 0;
            char previousChar = inputData.charAt(startName);
            int pos = 1;
            while (pos < inputData.length()) {
                final char currentChar = inputData.charAt(pos);
                if (currentChar == '=') {
                    name = inputData.substring(startName, pos).trim();
                    final int[] bounds = extractQuotedString(inputData.substring(pos + 1));
                    if (bounds == null) {
                        return new HashMap();
                    }
                    value = inputData.substring(bounds[0] + pos + 1, bounds[1] + pos + 1);
                    pos += bounds[1] + 1;
                    break;
                }
                else {
                    if (Character.isWhitespace(previousChar) && !Character.isWhitespace(currentChar)) {
                        startName = pos;
                    }
                    previousChar = currentChar;
                    ++pos;
                }
            }
            inputData = inputData.substring(pos);
            if (name.length() > 0 && value != null) {
                data.put(name, value);
            }
        }
        return data;
    }
    
    public boolean removePseudoAttribute(final String name) {
        if (this.mapData.remove(name) != null) {
            this.rawData = this.toString(this.mapData);
            return true;
        }
        return false;
    }
    
    public ProcessingInstruction setData(final String data) {
        final String reason = Verifier.checkProcessingInstructionData(data);
        if (reason != null) {
            throw new IllegalDataException(data, reason);
        }
        this.rawData = data;
        this.mapData = this.parseData(data);
        return this;
    }
    
    public ProcessingInstruction setData(final Map data) {
        final String temp = this.toString(data);
        final String reason = Verifier.checkProcessingInstructionData(temp);
        if (reason != null) {
            throw new IllegalDataException(temp, reason);
        }
        this.rawData = temp;
        this.mapData = data;
        return this;
    }
    
    public ProcessingInstruction setPseudoAttribute(final String name, final String value) {
        String reason = Verifier.checkProcessingInstructionData(name);
        if (reason != null) {
            throw new IllegalDataException(name, reason);
        }
        reason = Verifier.checkProcessingInstructionData(value);
        if (reason != null) {
            throw new IllegalDataException(value, reason);
        }
        this.mapData.put(name, value);
        this.rawData = this.toString(this.mapData);
        return this;
    }
    
    public ProcessingInstruction setTarget(final String newTarget) {
        final String reason;
        if ((reason = Verifier.checkProcessingInstructionTarget(newTarget)) != null) {
            throw new IllegalTargetException(newTarget, reason);
        }
        this.target = newTarget;
        return this;
    }
    
    public String toString() {
        return "[ProcessingInstruction: " + new XMLOutputter().outputString(this) + "]";
    }
    
    private String toString(final Map mapData) {
        final StringBuffer rawData = new StringBuffer();
        for (final String name : mapData.keySet()) {
            final String value = mapData.get(name);
            rawData.append(name).append("=\"").append(value).append("\" ");
        }
        if (rawData.length() > 0) {
            rawData.setLength(rawData.length() - 1);
        }
        return rawData.toString();
    }
}
