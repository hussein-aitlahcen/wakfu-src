package org.json;

import java.lang.reflect.*;
import java.util.*;
import java.io.*;

public class JSONArray
{
    private final ArrayList myArrayList;
    
    public JSONArray() {
        super();
        this.myArrayList = new ArrayList();
    }
    
    public JSONArray(final JSONTokener jsonTokener) throws JSONException {
        this();
        if (jsonTokener.nextClean() != '[') {
            throw jsonTokener.syntaxError("A JSONArray text must start with '['");
        }
        if (jsonTokener.nextClean() == ']') {
            return;
        }
        jsonTokener.back();
        while (true) {
            if (jsonTokener.nextClean() == ',') {
                jsonTokener.back();
                this.myArrayList.add(JSONObject.NULL);
            }
            else {
                jsonTokener.back();
                this.myArrayList.add(jsonTokener.nextValue());
            }
            switch (jsonTokener.nextClean()) {
                case ',':
                case ';': {
                    if (jsonTokener.nextClean() == ']') {
                        return;
                    }
                    jsonTokener.back();
                    continue;
                }
                case ']': {}
                default: {
                    throw jsonTokener.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }
    
    public JSONArray(final String s) throws JSONException {
        this(new JSONTokener(s));
    }
    
    public JSONArray(final Collection collection) {
        super();
        this.myArrayList = new ArrayList();
        if (collection != null) {
            final Iterator<Object> iterator = collection.iterator();
            while (iterator.hasNext()) {
                this.myArrayList.add(JSONObject.wrap(iterator.next()));
            }
        }
    }
    
    public JSONArray(final Object o) throws JSONException {
        this();
        if (o.getClass().isArray()) {
            for (int length = Array.getLength(o), i = 0; i < length; ++i) {
                this.put(JSONObject.wrap(Array.get(o, i)));
            }
            return;
        }
        throw new JSONException("JSONArray initial value should be a string or collection or array.");
    }
    
    public Object get(final int i) throws JSONException {
        final Object opt = this.opt(i);
        if (opt == null) {
            throw new JSONException("JSONArray[" + i + "] not found.");
        }
        return opt;
    }
    
    public boolean getBoolean(final int i) throws JSONException {
        final Object value = this.get(i);
        if (value.equals(Boolean.FALSE) || (value instanceof String && ((String)value).equalsIgnoreCase("false"))) {
            return false;
        }
        if (value.equals(Boolean.TRUE) || (value instanceof String && ((String)value).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new JSONException("JSONArray[" + i + "] is not a boolean.");
    }
    
    public double getDouble(final int i) throws JSONException {
        final Object value = this.get(i);
        try {
            return (value instanceof Number) ? ((Number)value).doubleValue() : Double.parseDouble((String)value);
        }
        catch (Exception ex) {
            throw new JSONException("JSONArray[" + i + "] is not a number.");
        }
    }
    
    public int getInt(final int i) throws JSONException {
        final Object value = this.get(i);
        try {
            return (value instanceof Number) ? ((Number)value).intValue() : Integer.parseInt((String)value);
        }
        catch (Exception ex) {
            throw new JSONException("JSONArray[" + i + "] is not a number.");
        }
    }
    
    public JSONArray getJSONArray(final int i) throws JSONException {
        final Object value = this.get(i);
        if (value instanceof JSONArray) {
            return (JSONArray)value;
        }
        throw new JSONException("JSONArray[" + i + "] is not a JSONArray.");
    }
    
    public JSONObject getJSONObject(final int i) throws JSONException {
        final Object value = this.get(i);
        if (value instanceof JSONObject) {
            return (JSONObject)value;
        }
        throw new JSONException("JSONArray[" + i + "] is not a JSONObject.");
    }
    
    public long getLong(final int i) throws JSONException {
        final Object value = this.get(i);
        try {
            return (value instanceof Number) ? ((Number)value).longValue() : Long.parseLong((String)value);
        }
        catch (Exception ex) {
            throw new JSONException("JSONArray[" + i + "] is not a number.");
        }
    }
    
    public String getString(final int i) throws JSONException {
        final Object value = this.get(i);
        if (value instanceof String) {
            return (String)value;
        }
        throw new JSONException("JSONArray[" + i + "] not a string.");
    }
    
    public boolean isNull(final int n) {
        return JSONObject.NULL.equals(this.opt(n));
    }
    
    public String join(final String str) throws JSONException {
        final int length = this.length();
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            if (i > 0) {
                sb.append(str);
            }
            sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
        }
        return sb.toString();
    }
    
    public int length() {
        return this.myArrayList.size();
    }
    
    public Object opt(final int index) {
        return (index < 0 || index >= this.length()) ? null : this.myArrayList.get(index);
    }
    
    public boolean optBoolean(final int n) {
        return this.optBoolean(n, false);
    }
    
    public boolean optBoolean(final int n, final boolean b) {
        try {
            return this.getBoolean(n);
        }
        catch (Exception ex) {
            return b;
        }
    }
    
    public double optDouble(final int n) {
        return this.optDouble(n, Double.NaN);
    }
    
    public double optDouble(final int n, final double n2) {
        try {
            return this.getDouble(n);
        }
        catch (Exception ex) {
            return n2;
        }
    }
    
    public int optInt(final int n) {
        return this.optInt(n, 0);
    }
    
    public int optInt(final int n, final int n2) {
        try {
            return this.getInt(n);
        }
        catch (Exception ex) {
            return n2;
        }
    }
    
    public JSONArray optJSONArray(final int n) {
        final Object opt = this.opt(n);
        return (opt instanceof JSONArray) ? ((JSONArray)opt) : null;
    }
    
    public JSONObject optJSONObject(final int n) {
        final Object opt = this.opt(n);
        return (opt instanceof JSONObject) ? ((JSONObject)opt) : null;
    }
    
    public long optLong(final int n) {
        return this.optLong(n, 0L);
    }
    
    public long optLong(final int n, final long n2) {
        try {
            return this.getLong(n);
        }
        catch (Exception ex) {
            return n2;
        }
    }
    
    public String optString(final int n) {
        return this.optString(n, "");
    }
    
    public String optString(final int n, final String s) {
        final Object opt = this.opt(n);
        return JSONObject.NULL.equals(opt) ? s : opt.toString();
    }
    
    public JSONArray put(final boolean b) {
        this.put(b ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }
    
    public JSONArray put(final Collection collection) {
        this.put(new JSONArray(collection));
        return this;
    }
    
    public JSONArray put(final double value) throws JSONException {
        final Double n = new Double(value);
        JSONObject.testValidity(n);
        this.put(n);
        return this;
    }
    
    public JSONArray put(final int value) {
        this.put(new Integer(value));
        return this;
    }
    
    public JSONArray put(final long value) {
        this.put(new Long(value));
        return this;
    }
    
    public JSONArray put(final Map map) {
        this.put(new JSONObject(map));
        return this;
    }
    
    public JSONArray put(final Object e) {
        this.myArrayList.add(e);
        return this;
    }
    
    public JSONArray put(final int n, final boolean b) throws JSONException {
        this.put(n, b ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }
    
    public JSONArray put(final int n, final Collection collection) throws JSONException {
        this.put(n, new JSONArray(collection));
        return this;
    }
    
    public JSONArray put(final int n, final double value) throws JSONException {
        this.put(n, new Double(value));
        return this;
    }
    
    public JSONArray put(final int n, final int value) throws JSONException {
        this.put(n, new Integer(value));
        return this;
    }
    
    public JSONArray put(final int n, final long value) throws JSONException {
        this.put(n, new Long(value));
        return this;
    }
    
    public JSONArray put(final int n, final Map map) throws JSONException {
        this.put(n, new JSONObject(map));
        return this;
    }
    
    public JSONArray put(final int i, final Object element) throws JSONException {
        JSONObject.testValidity(element);
        if (i < 0) {
            throw new JSONException("JSONArray[" + i + "] not found.");
        }
        if (i < this.length()) {
            this.myArrayList.set(i, element);
        }
        else {
            while (i != this.length()) {
                this.put(JSONObject.NULL);
            }
            this.put(element);
        }
        return this;
    }
    
    public Object remove(final int index) {
        final Object opt = this.opt(index);
        this.myArrayList.remove(index);
        return opt;
    }
    
    public JSONObject toJSONObject(final JSONArray jsonArray) throws JSONException {
        if (jsonArray == null || jsonArray.length() == 0 || this.length() == 0) {
            return null;
        }
        final JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < jsonArray.length(); ++i) {
            jsonObject.put(jsonArray.getString(i), this.opt(i));
        }
        return jsonObject;
    }
    
    public String toString() {
        try {
            return '[' + this.join(",") + ']';
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public String toString(final int n) throws JSONException {
        final StringWriter stringWriter = new StringWriter();
        synchronized (stringWriter.getBuffer()) {
            return this.write(stringWriter, n, 0).toString();
        }
    }
    
    public Writer write(final Writer writer) throws JSONException {
        return this.write(writer, 0, 0);
    }
    
    Writer write(final Writer writer, final int n, final int n2) throws JSONException {
        try {
            int n3 = 0;
            final int length = this.length();
            writer.write(91);
            if (length == 1) {
                JSONObject.writeValue(writer, this.myArrayList.get(0), n, n2);
            }
            else if (length != 0) {
                final int n4 = n2 + n;
                for (int i = 0; i < length; ++i) {
                    if (n3 != 0) {
                        writer.write(44);
                    }
                    if (n > 0) {
                        writer.write(10);
                    }
                    JSONObject.indent(writer, n4);
                    JSONObject.writeValue(writer, this.myArrayList.get(i), n, n4);
                    n3 = 1;
                }
                if (n > 0) {
                    writer.write(10);
                }
                JSONObject.indent(writer, n2);
            }
            writer.write(93);
            return writer;
        }
        catch (IOException ex) {
            throw new JSONException(ex);
        }
    }
}
