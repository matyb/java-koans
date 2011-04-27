package com.sandwich.koan.cmdline;

import com.sandwich.koan.constant.ArgumentType;

public class CommandLineArgument implements Runnable, Comparable<CommandLineArgument> {
	private ArgumentType argType;
	private String value;
	boolean isPlantedByApp = false;
	public CommandLineArgument(ArgumentType argType, String value) {
		this(argType, value, false);
	}

	public CommandLineArgument(ArgumentType argType, String value, boolean isPlantedByApp) {
		this.argType = argType;
		this.value = value;
		this.isPlantedByApp = isPlantedByApp;
	}

	public String getValue() {
		return value;
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
		argType.run(value);
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
				.append(", value=").append(value).append("]").toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((argType == null) ? 0 : argType.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
