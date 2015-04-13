package org.apache.tools.ant.input;

import org.apache.tools.ant.*;
import java.util.*;
import java.io.*;
import org.apache.tools.ant.util.*;

public class DefaultInputHandler implements InputHandler
{
    public void handleInput(final InputRequest request) throws BuildException {
        final String prompt = this.getPrompt(request);
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(this.getInputStream()));
            do {
                System.err.println(prompt);
                System.err.flush();
                try {
                    final String input = r.readLine();
                    request.setInput(input);
                }
                catch (IOException e) {
                    throw new BuildException("Failed to read input from Console.", e);
                }
            } while (!request.isInputValid());
        }
        finally {
            if (r != null) {
                try {
                    r.close();
                }
                catch (IOException e2) {
                    throw new BuildException("Failed to close input.", e2);
                }
            }
        }
    }
    
    protected String getPrompt(final InputRequest request) {
        final String prompt = request.getPrompt();
        final String def = request.getDefaultValue();
        if (request instanceof MultipleChoiceInputRequest) {
            final StringBuilder sb = new StringBuilder(prompt).append(" (");
            boolean first = true;
            for (final String next : ((MultipleChoiceInputRequest)request).getChoices()) {
                if (!first) {
                    sb.append(", ");
                }
                if (next.equals(def)) {
                    sb.append('[');
                }
                sb.append(next);
                if (next.equals(def)) {
                    sb.append(']');
                }
                first = false;
            }
            sb.append(")");
            return sb.toString();
        }
        if (def != null) {
            return prompt + " [" + def + "]";
        }
        return prompt;
    }
    
    protected InputStream getInputStream() {
        return KeepAliveInputStream.wrapSystemIn();
    }
}
