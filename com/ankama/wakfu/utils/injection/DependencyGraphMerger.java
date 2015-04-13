package com.ankama.wakfu.utils.injection;

import com.google.inject.*;
import com.google.common.base.*;
import com.google.inject.util.*;
import java.util.*;

class DependencyGraphMerger
{
    public Module merge(final ModuleDependencyGraph graph) {
        final List<Set<Module>> moduleSets = new ModuleSetsProvider(graph).getModuleSets();
        Preconditions.checkArgument(!moduleSets.isEmpty());
        Module module = Modules.combine((Iterable)moduleSets.get(0));
        for (int i = 1; i < moduleSets.size(); ++i) {
            module = Modules.override(new Module[] { module }).with((Iterable)moduleSets.get(i));
        }
        return module;
    }
}
