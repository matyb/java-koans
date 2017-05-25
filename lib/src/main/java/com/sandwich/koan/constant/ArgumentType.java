package com.sandwich.koan.constant;

import com.sandwich.koan.cmdline.behavior.*;
import com.sandwich.koan.runner.RunKoans;
import com.sandwich.koan.util.ApplicationUtils;
import com.sandwich.util.Strings;

import java.util.*;
import java.util.logging.Logger;


public enum ArgumentType implements ArgumentBehavior {

    HELP(Help.class),
    RESET(Reset.class),
    BACKUP(Backup.class),
    DEBUG(Debug.class),
    CLEAR(Clear.class),
    //TEST(		Test.class),
    // important class MUST come before method - due to how Enum implements comparable and order
    // dependent logic later @ see ArgumentTypeTest.testClassPrecedesMethod
    CLASS_ARG(ClassArg.class),
    METHOD_ARG(MethodArg.class),
    RUN_KOANS(RunKoans.class);

    private static final String DESCRIPTION = "description";
    private static final String ARGUMENTS = "args";

    private final List<String> args;
    private final ArgumentBehavior behavior;
    private final String description;

    ArgumentType(Class<? extends ArgumentBehavior> c) {
        try {
            this.behavior = c.newInstance();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        this.args = Arrays.asList(Strings.getMessages(c, ARGUMENTS));
        this.description = Strings.getMessage(c, DESCRIPTION);
    }

    public List<String> args() {
        return args;
    }

    public String description() {
        return description;
    }

    private static final Map<String, ArgumentType> TYPES_BY_STRING;

    static {
        Map<String, ArgumentType> types = new HashMap<String, ArgumentType>();
        for (ArgumentType type : ArgumentType.values()) {
            for (String arg : type.args()) {
                if (types.containsKey(arg)) {
                    throw new IllegalArgumentException(
                            Strings.getMessages(ArgumentType.class, "duplicated_arg_error_part1") + arg +
                                    Strings.getMessages(ArgumentType.class, "duplicated_arg_error_part2")); //$NON-NLS-1$ //$NON-NLS-2$
                }
                types.put(arg, type);
            }
        }
        TYPES_BY_STRING = Collections.unmodifiableMap(types);
    }

    public void run(String... values) {
        try {
            behavior.run(values);
            ApplicationUtils.getPresenter().displayMessage(behavior.getSuccessMessage());
        } catch (Throwable t) {
            if (behavior instanceof RunKoans) {
                if (t instanceof RuntimeException) {
                    throw (RuntimeException) t;
                } else {
                    throw new RuntimeException(t);
                }
            } else {
                Logger.getAnonymousLogger().severe(t.getLocalizedMessage());
                ApplicationUtils.getPresenter().displayError(behavior.getErrorMessage());
            }
        }
    }

    public String getErrorMessage() {
        return behavior.getErrorMessage();
    }

    public String getSuccessMessage() {
        return behavior.getSuccessMessage();
    }

    public static ArgumentType findTypeByString(String stringArg) {
        return TYPES_BY_STRING.get(stringArg);
    }
}


