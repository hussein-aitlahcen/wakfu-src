package com.ankama.wakfu.utils.injection;

import com.google.inject.*;

final class ModuleEquality
{
    private static final int MODULE_CLASS_OFFSET = 1183458017;
    private static final int MODULE_OFFSET = 511224688;
    
    public static boolean areEqual(final Module left, final Module right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        final Class<? extends Module> moduleClass = left.getClass();
        return moduleClass == right.getClass() && (!moduleClass.isAnonymousClass() || left.equals(right));
    }
    
    public static int getHashCode(final Module module) {
        if (module == null) {
            return 0;
        }
        final Class<? extends Module> moduleClass = module.getClass();
        if (moduleClass.isAnonymousClass()) {
            return 511224688 + module.hashCode();
        }
        return 1183458017 + moduleClass.hashCode();
    }
}
