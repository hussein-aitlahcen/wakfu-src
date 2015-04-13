package org.apache.tools.ant.util;

import org.apache.tools.ant.types.selectors.*;
import org.apache.tools.ant.*;
import java.util.*;
import org.apache.tools.ant.filters.util.*;
import java.nio.channels.*;
import java.io.*;
import org.apache.tools.ant.types.resources.selectors.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.types.resources.*;

public class ResourceUtils
{
    private static final FileUtils FILE_UTILS;
    public static final String ISO_8859_1 = "ISO-8859-1";
    private static final long MAX_IO_CHUNK_SIZE = 16777216L;
    
    public static Resource[] selectOutOfDateSources(final ProjectComponent logTo, final Resource[] source, final FileNameMapper mapper, final ResourceFactory targets) {
        return selectOutOfDateSources(logTo, source, mapper, targets, ResourceUtils.FILE_UTILS.getFileTimestampGranularity());
    }
    
    public static Resource[] selectOutOfDateSources(final ProjectComponent logTo, final Resource[] source, final FileNameMapper mapper, final ResourceFactory targets, final long granularity) {
        final Union u = new Union();
        u.addAll(Arrays.asList(source));
        final ResourceCollection rc = selectOutOfDateSources(logTo, u, mapper, targets, granularity);
        return (rc.size() == 0) ? new Resource[0] : ((Union)rc).listResources();
    }
    
    public static ResourceCollection selectOutOfDateSources(final ProjectComponent logTo, final ResourceCollection source, final FileNameMapper mapper, final ResourceFactory targets, final long granularity) {
        logFuture(logTo, source, granularity);
        final ResourceSelectorProvider p = new ResourceSelectorProvider() {
            public ResourceSelector getTargetSelectorForSource(final Resource sr) {
                return new ResourceSelector() {
                    public boolean isSelected(final Resource target) {
                        return SelectorUtils.isOutOfDate(sr, target, granularity);
                    }
                };
            }
        };
        return selectSources(logTo, source, mapper, targets, p);
    }
    
    public static ResourceCollection selectSources(final ProjectComponent logTo, ResourceCollection source, final FileNameMapper mapper, final ResourceFactory targets, final ResourceSelectorProvider selector) {
        if (source.size() == 0) {
            logTo.log("No sources found.", 3);
            return Resources.NONE;
        }
        source = Union.getInstance(source);
        final Union result = new Union();
        for (final Resource sr : source) {
            String srName = sr.getName();
            srName = ((srName == null) ? srName : srName.replace('/', File.separatorChar));
            String[] targetnames = null;
            try {
                targetnames = mapper.mapFileName(srName);
            }
            catch (Exception e) {
                logTo.log("Caught " + e + " mapping resource " + sr, 3);
            }
            if (targetnames == null || targetnames.length == 0) {
                logTo.log(sr + " skipped - don't know how to handle it", 3);
            }
            else {
                for (int i = 0; i < targetnames.length; ++i) {
                    if (targetnames[i] == null) {
                        targetnames[i] = "(no name)";
                    }
                }
                final Union targetColl = new Union();
                for (int j = 0; j < targetnames.length; ++j) {
                    targetColl.add(targets.getResource(targetnames[j].replace(File.separatorChar, '/')));
                }
                final Restrict r = new Restrict();
                r.add(selector.getTargetSelectorForSource(sr));
                r.add(targetColl);
                if (r.size() > 0) {
                    result.add(sr);
                    final Resource t = r.iterator().next();
                    logTo.log(sr.getName() + " added as " + t.getName() + (t.isExists() ? " is outdated." : " doesn't exist."), 3);
                }
                else {
                    logTo.log(sr.getName() + " omitted as " + targetColl.toString() + ((targetColl.size() == 1) ? " is" : " are ") + " up to date.", 3);
                }
            }
        }
        return result;
    }
    
    public static void copyResource(final Resource source, final Resource dest) throws IOException {
        copyResource(source, dest, null);
    }
    
    public static void copyResource(final Resource source, final Resource dest, final Project project) throws IOException {
        copyResource(source, dest, null, null, false, false, null, null, project);
    }
    
    public static void copyResource(final Resource source, final Resource dest, final FilterSetCollection filters, final Vector filterChains, final boolean overwrite, final boolean preserveLastModified, final String inputEncoding, final String outputEncoding, final Project project) throws IOException {
        copyResource(source, dest, filters, filterChains, overwrite, preserveLastModified, false, inputEncoding, outputEncoding, project);
    }
    
    public static void copyResource(final Resource source, final Resource dest, final FilterSetCollection filters, final Vector filterChains, final boolean overwrite, final boolean preserveLastModified, final boolean append, final String inputEncoding, final String outputEncoding, final Project project) throws IOException {
        copyResource(source, dest, filters, filterChains, overwrite, preserveLastModified, append, inputEncoding, outputEncoding, project, false);
    }
    
    public static void copyResource(final Resource source, final Resource dest, final FilterSetCollection filters, final Vector filterChains, final boolean overwrite, final boolean preserveLastModified, final boolean append, final String inputEncoding, final String outputEncoding, final Project project, final boolean force) throws IOException {
        if (!overwrite && !SelectorUtils.isOutOfDate(source, dest, FileUtils.getFileUtils().getFileTimestampGranularity())) {
            return;
        }
        final boolean filterSetsAvailable = filters != null && filters.hasFilters();
        final boolean filterChainsAvailable = filterChains != null && filterChains.size() > 0;
        String effectiveInputEncoding = null;
        if (source instanceof StringResource) {
            effectiveInputEncoding = ((StringResource)source).getEncoding();
        }
        else {
            effectiveInputEncoding = inputEncoding;
        }
        File destFile = null;
        if (dest.as(FileProvider.class) != null) {
            destFile = dest.as(FileProvider.class).getFile();
        }
        if (destFile != null && destFile.isFile() && !destFile.canWrite()) {
            if (!force) {
                throw new IOException("can't write to read-only destination file " + destFile);
            }
            if (!ResourceUtils.FILE_UTILS.tryHardToDelete(destFile)) {
                throw new IOException("failed to delete read-only destination file " + destFile);
            }
        }
        if (filterSetsAvailable) {
            BufferedReader in = null;
            BufferedWriter out = null;
            try {
                InputStreamReader isr = null;
                if (effectiveInputEncoding == null) {
                    isr = new InputStreamReader(source.getInputStream());
                }
                else {
                    isr = new InputStreamReader(source.getInputStream(), effectiveInputEncoding);
                }
                in = new BufferedReader(isr);
                final OutputStream os = getOutputStream(dest, append, project);
                OutputStreamWriter osw;
                if (outputEncoding == null) {
                    osw = new OutputStreamWriter(os);
                }
                else {
                    osw = new OutputStreamWriter(os, outputEncoding);
                }
                out = new BufferedWriter(osw);
                if (filterChainsAvailable) {
                    final ChainReaderHelper crh = new ChainReaderHelper();
                    crh.setBufferSize(8192);
                    crh.setPrimaryReader(in);
                    crh.setFilterChains(filterChains);
                    crh.setProject(project);
                    final Reader rdr = crh.getAssembledReader();
                    in = new BufferedReader(rdr);
                }
                final LineTokenizer lineTokenizer = new LineTokenizer();
                lineTokenizer.setIncludeDelims(true);
                String newline = null;
                for (String line = lineTokenizer.getToken(in); line != null; line = lineTokenizer.getToken(in)) {
                    if (line.length() == 0) {
                        out.newLine();
                    }
                    else {
                        newline = filters.replaceTokens(line);
                        out.write(newline);
                    }
                }
            }
            finally {
                FileUtils.close(out);
                FileUtils.close(in);
            }
        }
        else if (filterChainsAvailable || (effectiveInputEncoding != null && !effectiveInputEncoding.equals(outputEncoding)) || (effectiveInputEncoding == null && outputEncoding != null)) {
            BufferedReader in = null;
            BufferedWriter out = null;
            try {
                InputStreamReader isr = null;
                if (effectiveInputEncoding == null) {
                    isr = new InputStreamReader(source.getInputStream());
                }
                else {
                    isr = new InputStreamReader(source.getInputStream(), effectiveInputEncoding);
                }
                in = new BufferedReader(isr);
                final OutputStream os = getOutputStream(dest, append, project);
                OutputStreamWriter osw;
                if (outputEncoding == null) {
                    osw = new OutputStreamWriter(os);
                }
                else {
                    osw = new OutputStreamWriter(os, outputEncoding);
                }
                out = new BufferedWriter(osw);
                if (filterChainsAvailable) {
                    final ChainReaderHelper crh = new ChainReaderHelper();
                    crh.setBufferSize(8192);
                    crh.setPrimaryReader(in);
                    crh.setFilterChains(filterChains);
                    crh.setProject(project);
                    final Reader rdr = crh.getAssembledReader();
                    in = new BufferedReader(rdr);
                }
                final char[] buffer = new char[8192];
                while (true) {
                    final int nRead = in.read(buffer, 0, buffer.length);
                    if (nRead == -1) {
                        break;
                    }
                    out.write(buffer, 0, nRead);
                }
            }
            finally {
                FileUtils.close(out);
                FileUtils.close(in);
            }
        }
        else if (source.as(FileProvider.class) != null && destFile != null) {
            final File sourceFile = source.as(FileProvider.class).getFile();
            final File parent = destFile.getParentFile();
            if (parent != null && !parent.isDirectory() && !destFile.getParentFile().mkdirs()) {
                throw new IOException("failed to create the parent directory for " + destFile);
            }
            FileInputStream in2 = null;
            FileOutputStream out2 = null;
            FileChannel srcChannel = null;
            FileChannel destChannel = null;
            try {
                in2 = new FileInputStream(sourceFile);
                out2 = new FileOutputStream(destFile);
                srcChannel = in2.getChannel();
                destChannel = out2.getChannel();
                long chunk;
                for (long position = 0L, count = srcChannel.size(); position < count; position += destChannel.transferFrom(srcChannel, position, chunk)) {
                    chunk = Math.min(16777216L, count - position);
                }
            }
            finally {
                FileUtils.close(srcChannel);
                FileUtils.close(destChannel);
                FileUtils.close(out2);
                FileUtils.close(in2);
            }
        }
        else {
            InputStream in3 = null;
            OutputStream out3 = null;
            try {
                in3 = source.getInputStream();
                out3 = getOutputStream(dest, append, project);
                final byte[] buffer2 = new byte[8192];
                int count2 = 0;
                do {
                    out3.write(buffer2, 0, count2);
                    count2 = in3.read(buffer2, 0, buffer2.length);
                } while (count2 != -1);
            }
            finally {
                FileUtils.close(out3);
                FileUtils.close(in3);
            }
        }
        if (preserveLastModified) {
            final Touchable t = dest.as(Touchable.class);
            if (t != null) {
                setLastModified(t, source.getLastModified());
            }
        }
    }
    
    public static void setLastModified(final Touchable t, final long time) {
        t.touch((time < 0L) ? System.currentTimeMillis() : time);
    }
    
    public static boolean contentEquals(final Resource r1, final Resource r2, final boolean text) throws IOException {
        if (r1.isExists() != r2.isExists()) {
            return false;
        }
        if (!r1.isExists()) {
            return true;
        }
        if (r1.isDirectory() || r2.isDirectory()) {
            return false;
        }
        if (r1.equals(r2)) {
            return true;
        }
        if (!text) {
            final long s1 = r1.getSize();
            final long s2 = r2.getSize();
            if (s1 != -1L && s2 != -1L && s1 != s2) {
                return false;
            }
        }
        return compareContent(r1, r2, text) == 0;
    }
    
    public static int compareContent(final Resource r1, final Resource r2, final boolean text) throws IOException {
        if (r1.equals(r2)) {
            return 0;
        }
        final boolean e1 = r1.isExists();
        final boolean e2 = r2.isExists();
        if (!e1 && !e2) {
            return 0;
        }
        if (e1 != e2) {
            return e1 ? 1 : -1;
        }
        final boolean d1 = r1.isDirectory();
        final boolean d2 = r2.isDirectory();
        if (d1 && d2) {
            return 0;
        }
        if (d1 || d2) {
            return d1 ? -1 : 1;
        }
        return text ? textCompare(r1, r2) : binaryCompare(r1, r2);
    }
    
    public static FileResource asFileResource(final FileProvider fileProvider) {
        if (fileProvider instanceof FileResource || fileProvider == null) {
            return (FileResource)fileProvider;
        }
        final FileResource result = new FileResource(fileProvider.getFile());
        result.setProject(Project.getProject(fileProvider));
        return result;
    }
    
    private static int binaryCompare(final Resource r1, final Resource r2) throws IOException {
        InputStream in1 = null;
        InputStream in2 = null;
        try {
            in1 = new BufferedInputStream(r1.getInputStream());
            in2 = new BufferedInputStream(r2.getInputStream());
            for (int b1 = in1.read(); b1 != -1; b1 = in1.read()) {
                final int b2 = in2.read();
                if (b1 != b2) {
                    return (b1 > b2) ? 1 : -1;
                }
            }
            return (in2.read() == -1) ? 0 : -1;
        }
        finally {
            FileUtils.close(in1);
            FileUtils.close(in2);
        }
    }
    
    private static int textCompare(final Resource r1, final Resource r2) throws IOException {
        BufferedReader in1 = null;
        BufferedReader in2 = null;
        try {
            in1 = new BufferedReader(new InputStreamReader(r1.getInputStream()));
            in2 = new BufferedReader(new InputStreamReader(r2.getInputStream()));
            String expected = in1.readLine();
            while (expected != null) {
                final String actual = in2.readLine();
                if (!expected.equals(actual)) {
                    if (actual == null) {
                        return 1;
                    }
                    return expected.compareTo(actual);
                }
                else {
                    expected = in1.readLine();
                }
            }
            return (in2.readLine() == null) ? 0 : -1;
        }
        finally {
            FileUtils.close(in1);
            FileUtils.close(in2);
        }
    }
    
    private static void logFuture(final ProjectComponent logTo, final ResourceCollection rc, final long granularity) {
        final long now = System.currentTimeMillis() + granularity;
        final Date sel = new Date();
        sel.setMillis(now);
        sel.setWhen(TimeComparison.AFTER);
        final Restrict future = new Restrict();
        future.add(sel);
        future.add(rc);
        for (final Resource r : future) {
            logTo.log("Warning: " + r.getName() + " modified in the future.", 1);
        }
    }
    
    private static OutputStream getOutputStream(final Resource resource, final boolean append, final Project project) throws IOException {
        if (append) {
            final Appendable a = resource.as(Appendable.class);
            if (a != null) {
                return a.getAppendOutputStream();
            }
            project.log("Appendable OutputStream not available for non-appendable resource " + resource + "; using plain OutputStream", 3);
        }
        return resource.getOutputStream();
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
    }
    
    public interface ResourceSelectorProvider
    {
        ResourceSelector getTargetSelectorForSource(Resource p0);
    }
}
