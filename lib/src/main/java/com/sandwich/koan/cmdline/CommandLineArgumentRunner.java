package com.sandwich.koan.cmdline;

import com.sandwich.koan.constant.ArgumentType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandLineArgumentRunner implements Runnable {

    private final Map<ArgumentType, CommandLineArgument> commandLineArguments;

    public CommandLineArgumentRunner() {
        this(new CommandLineArgumentBuilder());
    }

    public CommandLineArgumentRunner(Map<ArgumentType, CommandLineArgument> commandLineArguments) {
        this.commandLineArguments = Collections.unmodifiableMap(commandLineArguments);
    }

    public void run() {
        List<CommandLineArgument> sortedArguments = new ArrayList<CommandLineArgument>(commandLineArguments.values());
        Collections.sort(sortedArguments);
        for (CommandLineArgument argument : sortedArguments) {
            argument.run();
        }
    }

}
