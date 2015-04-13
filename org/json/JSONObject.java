package org.json;

import java.lang.reflect.*;
import java.util.*;
import java.io.*;

public class JSONObject
{
    private final Map map;
    public static final Object NULL;
    
    public JSONObject() {
        super();
        this.map = new HashMap();
    }
    
    public JSONObject(final JSONObject jsonObject, final String[] array) {
        this();
        for (int i = 0; i < array.length; ++i) {
            try {
                this.putOnce(array[i], jsonObject.opt(array[i]));
            }
            catch (Exception ex) {}
        }
    }
    
    public JSONObject(final JSONTokener jsonTokener) throws JSONException {
        this();
        if (jsonTokener.nextClean() != '{') {
            throw jsonTokener.syntaxError("A JSONObject text must begin with '{'");
        }
        while (true) {
            switch (jsonTokener.nextClean()) {
                case '\0': {
                    throw jsonTokener.syntaxError("A JSONObject text must end with '}'");
                }
                case '}': {}
                default: {
                    jsonTokener.back();
                    final String string = jsonTokener.nextValue().toString();
                    final char nextClean = jsonTokener.nextClean();
                    if (nextClean == '=') {
                        if (jsonTokener.next() != '>') {
                            jsonTokener.back();
                        }
                    }
                    else if (nextClean != ':') {
                        throw jsonTokener.syntaxError("Expected a ':' after a key");
                    }
                    this.putOnce(string, jsonTokener.nextValue());
                    switch (jsonTokener.nextClean()) {
                        case ',':
                        case ';': {
                            if (jsonTokener.nextClean() == '}') {
                                return;
                            }
                            jsonTokener.back();
                            continue;
                        }
                        case '}': {
                            return;
                        }
                        default: {
                            throw jsonTokener.syntaxError("Expected a ',' or '}'");
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public JSONObject(final Map map) {
        super();
        this.map = new HashMap();
        if (map != null) {
            for (final Map.Entry<K, Object> entry : map.entrySet()) {
                final Object value = entry.getValue();
                if (value != null) {
                    this.map.put(entry.getKey(), wrap(value));
                }
            }
        }
    }
    
    public JSONObject(final Object o) {
        this();
        this.populateMap(o);
    }
    
    public JSONObject(final Object obj, final String[] array) {
        this();
        final Class<?> class1 = obj.getClass();
        for (int i = 0; i < array.length; ++i) {
            final String name = array[i];
            try {
                this.putOpt(name, class1.getField(name).get(obj));
            }
            catch (Exception ex) {}
        }
    }
    
    public JSONObject(final String s) throws JSONException {
        this(new JSONTokener(s));
    }
    
    public JSONObject(final String baseName, final Locale locale) throws JSONException {
        this();
        final ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, Thread.currentThread().getContextClassLoader());
        final Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            final String nextElement = keys.nextElement();
            if (nextElement instanceof String) {
                final String[] split = nextElement.split("\\.");
                final int n = split.length - 1;
                JSONObject jsonObject = this;
                for (final String s : split) {
                    JSONObject optJSONObject = jsonObject.optJSONObject(s);
                    if (optJSONObject == null) {
                        optJSONObject = new JSONObject();
                        jsonObject.put(s, optJSONObject);
                    }
                    jsonObject = optJSONObject;
                }
                jsonObject.put(split[n], bundle.getString(nextElement));
            }
        }
    }
    
    public JSONObject accumulate(final String s, final Object o) throws JSONException {
        testValidity(o);
        final Object opt = this.opt(s);
        if (opt == null) {
            this.put(s, (o instanceof JSONArray) ? new JSONArray().put(o) : o);
        }
        else if (opt instanceof JSONArray) {
            ((JSONArray)opt).put(o);
        }
        else {
            this.put(s, new JSONArray().put(opt).put(o));
        }
        return this;
    }
    
    public JSONObject append(final String str, final Object o) throws JSONException {
        testValidity(o);
        final Object opt = this.opt(str);
        if (opt == null) {
            this.put(str, new JSONArray().put(o));
        }
        else {
            if (!(opt instanceof JSONArray)) {
                throw new JSONException("JSONObject[" + str + "] is not a JSONArray.");
            }
            this.put(str, ((JSONArray)opt).put(o));
        }
        return this;
    }
    
    public static String doubleToString(final double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return "null";
        }
        String s = Double.toString(d);
        if (s.indexOf(46) > 0 && s.indexOf(101) < 0 && s.indexOf(69) < 0) {
            while (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }
    
    public Object get(final String s) throws JSONException {
        if (s == null) {
            throw new JSONException("Null key.");
        }
        final Object opt = this.opt(s);
        if (opt == null) {
            throw new JSONException("JSONObject[" + quote(s) + "] not found.");
        }
        return opt;
    }
    
    public boolean getBoolean(final String s) throws JSONException {
        final Object value = this.get(s);
        if (value.equals(Boolean.FALSE) || (value instanceof String && ((String)value).equalsIgnoreCase("false"))) {
            return false;
        }
        if (value.equals(Boolean.TRUE) || (value instanceof String && ((String)value).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new JSONException("JSONObject[" + quote(s) + "] is not a Boolean.");
    }
    
    public double getDouble(final String s) throws JSONException {
        final Object value = this.get(s);
        try {
            return (value instanceof Number) ? ((Number)value).doubleValue() : Double.parseDouble((String)value);
        }
        catch (Exception ex) {
            throw new JSONException("JSONObject[" + quote(s) + "] is not a number.");
        }
    }
    
    public int getInt(final String s) throws JSONException {
        final Object value = this.get(s);
        try {
            return (value instanceof Number) ? ((Number)value).intValue() : Integer.parseInt((String)value);
        }
        catch (Exception ex) {
            throw new JSONException("JSONObject[" + quote(s) + "] is not an int.");
        }
    }
    
    public JSONArray getJSONArray(final String s) throws JSONException {
        final Object value = this.get(s);
        if (value instanceof JSONArray) {
            return (JSONArray)value;
        }
        throw new JSONException("JSONObject[" + quote(s) + "] is not a JSONArray.");
    }
    
    public JSONObject getJSONObject(final String s) throws JSONException {
        final Object value = this.get(s);
        if (value instanceof JSONObject) {
            return (JSONObject)value;
        }
        throw new JSONException("JSONObject[" + quote(s) + "] is not a JSONObject.");
    }
    
    public long getLong(final String s) throws JSONException {
        final Object value = this.get(s);
        try {
            return (value instanceof Number) ? ((Number)value).longValue() : Long.parseLong((String)value);
        }
        catch (Exception ex) {
            throw new JSONException("JSONObject[" + quote(s) + "] is not a long.");
        }
    }
    
    public static String[] getNames(final JSONObject jsonObject) {
        final int length = jsonObject.length();
        if (length == 0) {
            return null;
        }
        final Iterator keys = jsonObject.keys();
        final String[] array = new String[length];
        int n = 0;
        while (keys.hasNext()) {
            array[n] = keys.next();
            ++n;
        }
        return array;
    }
    
    public static String[] getNames(final Object o) {
        if (o == null) {
            return null;
        }
        final Field[] fields = o.getClass().getFields();
        final int length = fields.length;
        if (length == 0) {
            return null;
        }
        final String[] array = new String[length];
        for (int i = 0; i < length; ++i) {
            array[i] = fields[i].getName();
        }
        return array;
    }
    
    public String getString(final String s) throws JSONException {
        final Object value = this.get(s);
        if (value instanceof String) {
            return (String)value;
        }
        throw new JSONException("JSONObject[" + quote(s) + "] not a string.");
    }
    
    public boolean has(final String s) {
        return this.map.containsKey(s);
    }
    
    public JSONObject increment(final String s) throws JSONException {
        final Object opt = this.opt(s);
        if (opt == null) {
            this.put(s, 1);
        }
        else if (opt instanceof Integer) {
            this.put(s, (int)opt + 1);
        }
        else if (opt instanceof Long) {
            this.put(s, (long)opt + 1L);
        }
        else if (opt instanceof Double) {
            this.put(s, (double)opt + 1.0);
        }
        else {
            if (!(opt instanceof Float)) {
                throw new JSONException("Unable to increment [" + quote(s) + "].");
            }
            this.put(s, (float)opt + 1.0f);
        }
        return this;
    }
    
    public boolean isNull(final String s) {
        return JSONObject.NULL.equals(this.opt(s));
    }
    
    public Iterator keys() {
        return this.map.keySet().iterator();
    }
    
    public int length() {
        return this.map.size();
    }
    
    public JSONArray names() {
        final JSONArray jsonArray = new JSONArray();
        final Iterator keys = this.keys();
        while (keys.hasNext()) {
            jsonArray.put(keys.next());
        }
        return (jsonArray.length() == 0) ? null : jsonArray;
    }
    
    public static String numberToString(final Number n) throws JSONException {
        if (n == null) {
            throw new JSONException("Null pointer");
        }
        testValidity(n);
        String s = n.toString();
        if (s.indexOf(46) > 0 && s.indexOf(101) < 0 && s.indexOf(69) < 0) {
            while (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }
    
    public Object opt(final String s) {
        return (s == null) ? null : this.map.get(s);
    }
    
    public boolean optBoolean(final String s) {
        return this.optBoolean(s, false);
    }
    
    public boolean optBoolean(final String s, final boolean b) {
        try {
            return this.getBoolean(s);
        }
        catch (Exception ex) {
            return b;
        }
    }
    
    public double optDouble(final String s) {
        return this.optDouble(s, Double.NaN);
    }
    
    public double optDouble(final String s, final double n) {
        try {
            return this.getDouble(s);
        }
        catch (Exception ex) {
            return n;
        }
    }
    
    public int optInt(final String s) {
        return this.optInt(s, 0);
    }
    
    public int optInt(final String s, final int n) {
        try {
            return this.getInt(s);
        }
        catch (Exception ex) {
            return n;
        }
    }
    
    public JSONArray optJSONArray(final String s) {
        final Object opt = this.opt(s);
        return (opt instanceof JSONArray) ? ((JSONArray)opt) : null;
    }
    
    public JSONObject optJSONObject(final String s) {
        final Object opt = this.opt(s);
        return (opt instanceof JSONObject) ? ((JSONObject)opt) : null;
    }
    
    public long optLong(final String s) {
        return this.optLong(s, 0L);
    }
    
    public long optLong(final String s, final long n) {
        try {
            return this.getLong(s);
        }
        catch (Exception ex) {
            return n;
        }
    }
    
    public String optString(final String s) {
        return this.optString(s, "");
    }
    
    public String optString(final String s, final String s2) {
        final Object opt = this.opt(s);
        return JSONObject.NULL.equals(opt) ? s2 : opt.toString();
    }
    
    private void populateMap(final Object obj) {
        final Class<?> class1 = obj.getClass();
        final Method[] array = (class1.getClassLoader() != null) ? class1.getMethods() : class1.getDeclaredMethods();
        for (int i = 0; i < array.length; ++i) {
            try {
                final Method method = array[i];
                if (Modifier.isPublic(method.getModifiers())) {
                    final String name = method.getName();
                    String s = "";
                    if (name.startsWith("get")) {
                        if ("getClass".equals(name) || "getDeclaringClass".equals(name)) {
                            s = "";
                        }
                        else {
                            s = name.substring(3);
                        }
                    }
                    else if (name.startsWith("is")) {
                        s = name.substring(2);
                    }
                    if (s.length() > 0 && Character.isUpperCase(s.charAt(0)) && method.getParameterTypes().length == 0) {
                        if (s.length() == 1) {
                            s = s.toLowerCase();
                        }
                        else if (!Character.isUpperCase(s.charAt(1))) {
                            s = s.substring(0, 1).toLowerCase() + s.substring(1);
                        }
                        final Object invoke = method.invoke(obj, (Object[])null);
                        if (invoke != null) {
                            this.map.put(s, wrap(invoke));
                        }
                    }
                }
            }
            catch (Exception ex) {}
        }
    }
    
    public JSONObject put(final String s, final boolean b) throws JSONException {
        this.put(s, b ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }
    
    public JSONObject put(final String s, final Collection collection) throws JSONException {
        this.put(s, new JSONArray(collection));
        return this;
    }
    
    public JSONObject put(final String s, final double value) throws JSONException {
        this.put(s, new Double(value));
        return this;
    }
    
    public JSONObject put(final String s, final int value) throws JSONException {
        this.put(s, new Integer(value));
        return this;
    }
    
    public JSONObject put(final String s, final long value) throws JSONException {
        this.put(s, new Long(value));
        return this;
    }
    
    public JSONObject put(final String s, final Map map) throws JSONException {
        this.put(s, new JSONObject(map));
        return this;
    }
    
    public JSONObject put(final String s, final Object o) throws JSONException {
        if (s == null) {
            throw new JSONException("Null key.");
        }
        if (o != null) {
            testValidity(o);
            this.map.put(s, o);
        }
        else {
            this.remove(s);
        }
        return this;
    }
    
    public JSONObject putOnce(final String str, final Object o) throws JSONException {
        if (str != null && o != null) {
            if (this.opt(str) != null) {
                throw new JSONException("Duplicate key \"" + str + "\"");
            }
            this.put(str, o);
        }
        return this;
    }
    
    public JSONObject putOpt(final String s, final Object o) throws JSONException {
        if (s != null && o != null) {
            this.put(s, o);
        }
        return this;
    }
    
    public static String quote(final String s) {
        final StringWriter stringWriter = new StringWriter();
        synchronized (stringWriter.getBuffer()) {
            try {
                return quote(s, stringWriter).toString();
            }
            catch (IOException ex) {
                return "";
            }
        }
    }
    
    public static Writer quote(final String s, final Writer writer) throws IOException {
        if (s == null || s.length() == 0) {
            writer.write("\"\"");
            return writer;
        }
        int char1 = 0;
        final int length = s.length();
        writer.write(34);
        for (int i = 0; i < length; ++i) {
            final int n = char1;
            char1 = s.charAt(i);
            switch (char1) {
                case 34:
                case 92: {
                    writer.write(92);
                    writer.write(char1);
                    break;
                }
                case 47: {
                    if (n == 60) {
                        writer.write(92);
                    }
                    writer.write(char1);
                    break;
                }
                case 8: {
                    writer.write("\\b");
                    break;
                }
                case 9: {
                    writer.write("\\t");
                    break;
                }
                case 10: {
                    writer.write("\\n");
                    break;
                }
                case 12: {
                    writer.write("\\f");
                    break;
                }
                case 13: {
                    writer.write("\\r");
                    break;
                }
                default: {
                    if (char1 < 32 || (char1 >= 128 && char1 < 160) || (char1 >= 8192 && char1 < 8448)) {
                        final String string = "000" + Integer.toHexString(char1);
                        writer.write("\\u" + string.substring(string.length() - 4));
                        break;
                    }
                    writer.write(char1);
                    break;
                }
            }
        }
        writer.write(34);
        return writer;
    }
    
    public Object remove(final String s) {
        return this.map.remove(s);
    }
    
    public static Object stringToValue(final String s) {
        if (s.equals("")) {
            return s;
        }
        if (s.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (s.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (s.equalsIgnoreCase("null")) {
            return JSONObject.NULL;
        }
        final char char1 = s.charAt(0);
        if ((char1 < '0' || char1 > '9') && char1 != '.' && char1 != '-') {
            if (char1 != '+') {
                return s;
            }
        }
        try {
            if (s.indexOf(46) > -1 || s.indexOf(101) > -1 || s.indexOf(69) > -1) {
                final Double value = Double.valueOf(s);
                if (!value.isInfinite() && !value.isNaN()) {
                    return value;
                }
            }
            else {
                final Long n = new Long(s);
                if (n == (int)(Object)n) {
                    return new Integer((int)(Object)n);
                }
                return n;
            }
        }
        catch (Exception ex) {}
        return s;
    }
    
    public static void testValidity(final Object o) throws JSONException {
        if (o != null) {
            if (o instanceof Double) {
                if (((Double)o).isInfinite() || ((Double)o).isNaN()) {
                    throw new JSONException("JSON does not allow non-finite numbers.");
                }
            }
            else if (o instanceof Float && (((Float)o).isInfinite() || ((Float)o).isNaN())) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        }
    }
    
    public JSONArray toJSONArray(final JSONArray jsonArray) throws JSONException {
        if (jsonArray == null || jsonArray.length() == 0) {
            return null;
        }
        final JSONArray jsonArray2 = new JSONArray();
        for (int i = 0; i < jsonArray.length(); ++i) {
            jsonArray2.put(this.opt(jsonArray.getString(i)));
        }
        return jsonArray2;
    }
    
    public String toString() {
        try {
            return this.toString(0);
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
    
    public static String valueToString(final Object o) throws JSONException {
        if (o == null || o.equals(null)) {
            return "null";
        }
        if (o instanceof JSONString) {
            String jsonString;
            try {
                jsonString = ((JSONString)o).toJSONString();
            }
            catch (Exception ex) {
                throw new JSONException(ex);
            }
            if (jsonString instanceof String) {
                return jsonString;
            }
            throw new JSONException("Bad value from toJSONString: " + (Object)jsonString);
        }
        else {
            if (o instanceof Number) {
                return numberToString((Number)o);
            }
            if (o instanceof Boolean || o instanceof JSONObject || o instanceof JSONArray) {
                return o.toString();
            }
            if (o instanceof Map) {
                return new JSONObject((Map)o).toString();
            }
            if (o instanceof Collection) {
                return new JSONArray((Collection)o).toString();
            }
            if (o.getClass().isArray()) {
                return new JSONArray(o).toString();
            }
            return quote(o.toString());
        }
    }
    
    public static Object wrap(final Object obj) {
        try {
            if (obj == null) {
                return JSONObject.NULL;
            }
            if (obj instanceof JSONObject || obj instanceof JSONArray || JSONObject.NULL.equals(obj) || obj instanceof JSONString || obj instanceof Byte || obj instanceof Character || obj instanceof Short || obj instanceof Integer || obj instanceof Long || obj instanceof Boolean || obj instanceof Float || obj instanceof Double || obj instanceof String) {
                return obj;
            }
            if (obj instanceof Collection) {
                return new JSONArray((Collection)obj);
            }
            if (obj.getClass().isArray()) {
                return new JSONArray(obj);
            }
            if (obj instanceof Map) {
                return new JSONObject((Map)obj);
            }
            final Package package1 = obj.getClass().getPackage();
            final String s = (package1 != null) ? package1.getName() : "";
            if (s.startsWith("java.") || s.startsWith("javax.") || obj.getClass().getClassLoader() == null) {
                return obj.toString();
            }
            return new JSONObject(obj);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public Writer write(final Writer writer) throws JSONException {
        return this.write(writer, 0, 0);
    }
    
    static final Writer writeValue(final Writer writer, final Object o, final int n, final int n2) throws JSONException, IOException {
        if (o == null || o.equals(null)) {
            writer.write("null");
        }
        else if (o instanceof JSONObject) {
            ((JSONObject)o).write(writer, n, n2);
        }
        else if (o instanceof JSONArray) {
            ((JSONArray)o).write(writer, n, n2);
        }
        else if (o instanceof Map) {
            new JSONObject((Map)o).write(writer, n, n2);
        }
        else if (o instanceof Collection) {
            new JSONArray((Collection)o).write(writer, n, n2);
        }
        else if (o.getClass().isArray()) {
            new JSONArray(o).write(writer, n, n2);
        }
        else if (o instanceof Number) {
            writer.write(numberToString((Number)o));
        }
        else if (o instanceof Boolean) {
            writer.write(o.toString());
        }
        else if (o instanceof JSONString) {
            String jsonString;
            try {
                jsonString = ((JSONString)o).toJSONString();
            }
            catch (Exception ex) {
                throw new JSONException(ex);
            }
            writer.write((jsonString != null) ? jsonString.toString() : quote(o.toString()));
        }
        else {
            quote(o.toString(), writer);
        }
        return writer;
    }
    
    static final void indent(final Writer writer, final int n) throws IOException {
        for (int i = 0; i < n; ++i) {
            writer.write(32);
        }
    }
    
    Writer write(final Writer writer, final int n, final int n2) throws JSONException {
        try {
            int n3 = 0;
            final int length = this.length();
            final Iterator keys = this.keys();
            writer.write(123);
            if (length == 1) {
                final Object next = keys.next();
                writer.write(quote(next.toString()));
                writer.write(58);
                if (n > 0) {
                    writer.write(32);
                }
                writeValue(writer, this.map.get(next), n, n2);
            }
            else if (length != 0) {
                final int n4 = n2 + n;
                while (keys.hasNext()) {
                    final Object next2 = keys.next();
                    if (n3 != 0) {
                        writer.write(44);
                    }
                    if (n > 0) {
                        writer.write(10);
                    }
                    indent(writer, n4);
                    writer.write(quote(next2.toString()));
                    writer.write(58);
                    if (n > 0) {
                        writer.write(32);
                    }
                    writeValue(writer, this.map.get(next2), n, n4);
                    n3 = 1;
                }
                if (n > 0) {
                    writer.write(10);
                }
                indent(writer, n2);
            }
            writer.write(125);
            return writer;
        }
        catch (IOException ex) {
            throw new JSONException(ex);
        }
    }
    
    static {
        NULL = new Null();
    }
    
    private static final class Null
    {
        protected final Object clone() {
            return this;
        }
        
        public boolean equals(final Object o) {
            return o == null || o == this;
        }
        
        public String toString() {
            return "null";
        }
    }
}
