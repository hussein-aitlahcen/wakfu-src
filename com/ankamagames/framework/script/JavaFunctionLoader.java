package com.ankamagames.framework.script;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import org.keplerproject.luajava.*;
import java.util.*;
import gnu.trove.*;
import java.io.*;
import org.apache.commons.lang3.*;
import org.apache.tools.ant.*;
import java.lang.reflect.*;

public class JavaFunctionLoader
{
    private static final Logger m_logger;
    public static final JavaFunctionLoader INSTANCE;
    private JavaFunctionsLibrary[] m_defaultLibraries;
    private final THashMap<String, CustomJavaFunctionLib[]> m_libs;
    private FunctionSearch m_search;
    
    private JavaFunctionLoader() {
        super();
        this.m_libs = new THashMap<String, CustomJavaFunctionLib[]>();
    }
    
    public void setDefaultLibraries(final JavaFunctionsLibrary[] defaultLibraries) {
        this.m_defaultLibraries = defaultLibraries;
    }
    
    public void addLibrary(final JavaFunctionsLibrary... libraries) {
        if (this.m_search != null) {
            this.m_search.addAvailableFunctionsLibraries(libraries);
        }
        final JavaFunctionsLibrary[] defaultLibraries = this.m_defaultLibraries;
        System.arraycopy(defaultLibraries, 0, this.m_defaultLibraries = new JavaFunctionsLibrary[defaultLibraries.length + libraries.length], 0, defaultLibraries.length);
        System.arraycopy(libraries, 0, this.m_defaultLibraries, defaultLibraries.length, libraries.length);
    }
    
    public void load(final String fileName) throws IOException {
        try {
            final ExtendedDataInputStream s = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(fileName));
            final int libCount = s.readInt();
            final CustomJavaFunctionLib[][] libs = new CustomJavaFunctionLib[libCount][];
            for (int i = 0; i < libCount; ++i) {
                final int count = s.readByte();
                libs[i] = new CustomJavaFunctionLib[count];
                for (int j = 0; j < count; ++j) {
                    final String name = s.readString();
                    final Constructor[] functions = readFunctions(s);
                    if (name.length() == 0) {
                        libs[i][j] = new CustomJavaFunctionLib(name, null, functions);
                    }
                    else {
                        libs[i][j] = new CustomJavaFunctionLib(name, functions, null);
                    }
                }
            }
            for (int count2 = s.readInt(), k = 0; k < count2; ++k) {
                final String scriptName = s.readString();
                final int index = s.readInt();
                this.m_libs.put(scriptName, libs[index]);
            }
        }
        catch (IOException e) {
            JavaFunctionLoader.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    private static Constructor[] readFunctions(final ExtendedDataInputStream s) throws IOException {
        final int count = s.readByte();
        final String[] functions = new String[count];
        for (int i = 0; i < count; ++i) {
            functions[i] = s.readString();
        }
        return createFunction(functions);
    }
    
    private static Constructor[] createFunction(final String[] className) {
        final Constructor[] functions = new Constructor[className.length];
        for (int i = 0; i < className.length; ++i) {
            try {
                final Class<?> aClass = Class.forName(className[i]);
                (functions[i] = aClass.getDeclaredConstructor(LuaState.class)).setAccessible(true);
            }
            catch (NoSuchMethodException e) {
                JavaFunctionLoader.m_logger.error((Object)"", (Throwable)e);
            }
            catch (ClassNotFoundException e2) {
                JavaFunctionLoader.m_logger.error((Object)"", (Throwable)e2);
            }
        }
        return functions;
    }
    
    public CustomJavaFunctionLib[] getLibs(final String scriptName) {
        return this.m_libs.get(scriptName);
    }
    
    public boolean findUsage(final String id, final String fileAbsolutePath) {
        try {
            final InputStream inputStream = ContentFileHelper.openFile(fileAbsolutePath);
            final CustomJavaFunctionLib[] libs = this.findUsage(new InputStreamReader(inputStream));
            if (libs == null) {
                return false;
            }
            this.m_libs.put(id, libs);
            return true;
        }
        catch (IOException e) {
            JavaFunctionLoader.m_logger.error((Object)"", (Throwable)e);
            return false;
        }
    }
    
    public CustomJavaFunctionLib[] findUsage(final String scriptText) {
        return this.findUsage(new StringReader(scriptText));
    }
    
    public CustomJavaFunctionLib[] findUsage(final Reader script) {
        if (this.m_search == null) {
            (this.m_search = new FunctionSearch()).addAvailableFunctionsLibraries(this.m_defaultLibraries);
        }
        final Collection<Function> classes = this.m_search.getFunctionsInScript(script);
        if (classes == null) {
            return null;
        }
        final THashMap<String, ArrayList<Class>> converted = convert(classes);
        final CustomJavaFunctionLib[] libs = new CustomJavaFunctionLib[converted.size()];
        converted.forEachEntry(new TObjectObjectProcedure<String, ArrayList<Class>>() {
            int i = 0;
            
            @Override
            public boolean execute(final String libName, final ArrayList<Class> list) {
                final String[] classNames = new String[list.size()];
                for (int j = 0, listSize = list.size(); j < listSize; ++j) {
                    classNames[j] = list.get(j).getName();
                }
                final Constructor[] functions = createFunction(classNames);
                if (libName.length() == 0) {
                    libs[this.i++] = new CustomJavaFunctionLib(libName, null, functions);
                }
                else {
                    libs[this.i++] = new CustomJavaFunctionLib(libName, functions, null);
                }
                return true;
            }
        });
        return libs;
    }
    
    public static THashMap<String, ArrayList<Class>> convert(final Collection<Function> classes) {
        final THashMap<String, ArrayList<Class>> libs = new THashMap<String, ArrayList<Class>>();
        for (final Function c : classes) {
            final String packageName = c.getPackageName();
            ArrayList<Class> list = libs.get(packageName);
            if (list == null) {
                list = new ArrayList<Class>();
                libs.put(packageName, list);
            }
            list.add(c.m_class);
        }
        return libs;
    }
    
    static {
        m_logger = Logger.getLogger((Class)JavaFunctionLoader.class);
        INSTANCE = new JavaFunctionLoader();
    }
    
    public static class Function
    {
        private final String m_name;
        public final Class m_class;
        private final String m_packageName;
        
        public Function(final String name, final Class aClass) {
            super();
            this.m_name = name;
            this.m_class = aClass;
            final int index = this.m_name.indexOf(46);
            this.m_packageName = ((index == -1) ? "" : this.m_name.substring(0, index));
        }
        
        public String getPackageName() {
            return this.m_packageName;
        }
        
        public boolean isGlobal() {
            return this.m_packageName.length() == 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Function function = (Function)o;
            return this.m_class.equals(function.m_class) && this.m_name.equals(function.m_name);
        }
        
        @Override
        public int hashCode() {
            int result = this.m_name.hashCode();
            result = 31 * result + this.m_class.hashCode();
            return result;
        }
    }
    
    public static class FunctionSearch
    {
        private final THashMap<String, Class> m_availableFunctions;
        
        public FunctionSearch() {
            super();
            this.m_availableFunctions = new THashMap<String, Class>();
        }
        
        public void readFunctionsInScript(final InputStream script, final String scriptName, final String dirName, final THashMap<String, Collection<Function>> functionsInScript) {
            final THashSet<Function> functions = this.getFunctionsInScript(new InputStreamReader(script));
            if (functions == null) {
                return;
            }
            functionsInScript.put(getFileName(dirName, scriptName), functions);
        }
        
        public final THashSet<Function> getFunctionsInScript(final Reader script) {
            final THashSet<Function> functions = new THashSet<Function>();
            try {
                final BufferedReader b = new BufferedReader(script);
                for (String line = b.readLine(); line != null; line = b.readLine()) {
                    this.getFunctionInLine(line, functions);
                }
                b.close();
            }
            catch (IOException e) {
                JavaFunctionLoader.m_logger.error((Object)"", (Throwable)e);
                return null;
            }
            return functions;
        }
        
        public static String getFileName(final String dirName, final String file) {
            return (dirName.length() == 0) ? file : (dirName + "/" + file);
        }
        
        private void getFunctionInLine(final String line, final Collection<Function> functions) {
            this.m_availableFunctions.forEachEntry((TObjectObjectProcedure<String, Class>)new TObjectObjectProcedure<String, Class>() {
                @Override
                public boolean execute(final String funcName, final Class c) {
                    if (StringUtils.contains(line, funcName)) {
                        functions.add(new Function(funcName, c));
                    }
                    return true;
                }
            });
        }
        
        public void addAvailableFunctionsLibraries(final JavaFunctionsLibrary[] libraries) {
            try {
                for (final JavaFunctionsLibrary lib : libraries) {
                    if (isExportable(lib)) {
                        final JavaFunctionEx[] functions = lib.createFunctions(null);
                        if (functions != null) {
                            for (final JavaFunctionEx f : functions) {
                                if (isExportable(f)) {
                                    this.m_availableFunctions.put(lib.getName() + "." + f.getName(), f.getClass());
                                }
                            }
                        }
                        final JavaFunctionEx[] globalFunctions = lib.createGlobalFunctions(null);
                        if (globalFunctions != null) {
                            for (final JavaFunctionEx f2 : globalFunctions) {
                                if (isExportable(f2)) {
                                    this.m_availableFunctions.put(f2.getName(), f2.getClass());
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                throw new BuildException(e);
            }
        }
        
        private static boolean isExportable(final JavaFunctionsLibrary lib) {
            try {
                lib.getClass().getDeclaredConstructor((Class<?>[])new Class[0]);
                return true;
            }
            catch (NoSuchMethodException e) {
                return false;
            }
        }
        
        private static boolean isExportable(final JavaFunctionEx func) {
            try {
                func.getClass().getDeclaredConstructor(LuaState.class);
                return true;
            }
            catch (NoSuchMethodException e) {
                return false;
            }
        }
    }
    
    private static class CustomJavaFunctionLib extends JavaFunctionsLibrary
    {
        private final String m_name;
        private final Constructor[] m_functions;
        private final Constructor[] m_globalFunctions;
        
        CustomJavaFunctionLib(final String name, final Constructor[] functions, final Constructor[] globalFunctions) {
            super();
            this.m_name = name;
            this.m_functions = functions;
            this.m_globalFunctions = globalFunctions;
        }
        
        @Override
        public JavaFunctionEx[] createFunctions(final LuaState luaState) {
            return createFunctions(this.m_functions, luaState);
        }
        
        @Override
        public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
            return createFunctions(this.m_globalFunctions, luaState);
        }
        
        private static JavaFunctionEx[] createFunctions(final Constructor[] constructors, final LuaState luaState) {
            if (constructors == null || constructors.length == 0) {
                return null;
            }
            final JavaFunctionEx[] functions = new JavaFunctionEx[constructors.length];
            for (int i = 0; i < constructors.length; ++i) {
                try {
                    functions[i] = constructors[i].newInstance(luaState);
                }
                catch (InstantiationException e) {
                    CustomJavaFunctionLib.m_logger.error((Object)"", (Throwable)e);
                }
                catch (IllegalAccessException e2) {
                    CustomJavaFunctionLib.m_logger.error((Object)"", (Throwable)e2);
                }
                catch (InvocationTargetException e3) {
                    CustomJavaFunctionLib.m_logger.error((Object)"", (Throwable)e3);
                }
            }
            return functions;
        }
        
        @Override
        public final String getName() {
            return this.m_name;
        }
        
        @Override
        public String getDescription() {
            return "NO Description<br/>Please Dev... implement me!";
        }
    }
}
