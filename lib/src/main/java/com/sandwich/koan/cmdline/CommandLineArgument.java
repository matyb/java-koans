package com.sandwich.koan.cmdline;

import java.util.Arrays;

import com.sandwich.koan.constant.ArgumentType;

public class CommandLineArgument implements Runnable, Comparable<CommandLineArgument> {
	private ArgumentType argType;
	private String[] values;
	boolean isPlantedByApp = false;
	public CommandLineArgument(ArgumentType argType, String... values) {
		this(argType, false, values);
	}

	public CommandLineArgument(ArgumentType argType, boolean isPlantedByApp, String... values) {
		this.argType = argType;
		this.values = values;
		this.isPlantedByApp = isPlantedByApp;
	}

	public String[] getValues() {
		return values;
	}

	public ArgumentType getArgumentType() {
		return argType;
	}

	public void setPlantedByApp(boolean isPlantedByApp){
		this.isPlantedByApp = isPlantedByApp;
	}
	
	public boolean isPlantedByApp() {
		return isPlantedByApp;
	}
	
	public void run(){
		argType.run(values);
	}
	
	public int compareTo(CommandLineArgument o) {
		if(o == null){
			return -1;
		}
		if(argType == null){
			return 1;
		}
		return argType.compareTo(o.getArgumentType());
	}

	@Override
	public String toString() {
		return new StringBuilder("CommandLineArgument [argType=").append(argType)
				.append(", value=").append(Arrays.toString(values)).append("]").toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((argType == null) ? 0 : argType.hashCode());
		result = prime * result + ((values == null) ? 0 : Arrays.hashCode(values));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandLineArgument other = (CommandLineArgument) obj;
		if (argType != other.argType)
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!Arrays.equals(values, other.values))
			return false;
		return true;
	}
	
}
