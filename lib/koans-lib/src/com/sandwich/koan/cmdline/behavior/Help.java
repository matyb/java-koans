package com.sandwich.koan.cmdline.behavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.util.ApplicationUtils;


public class Help extends AbstractArgumentBehavior {

	public void run(String...values) {
		StringBuilder sb = new StringBuilder(
				 "Java Koans - HELP").append(KoanConstants.EOL);
		line(sb, "-----------------");
		line(sb, "the following options are available:");
		ArrayList<ArgumentType> argTypes = new ArrayList<ArgumentType>(Arrays.asList(getArgumentTypes()));
		Collections.reverse(argTypes);
		for(ArgumentType argType : argTypes){
			StringBuilder stringArgs = new StringBuilder();
			for(String arg : argType.args()){
				stringArgs.append(arg);
				if(argType.args().indexOf(arg) < (argType.args().size() - 1)){
					stringArgs.append(", ");
				}
			}
			String args = stringArgs.toString();
			String fiveSpaces = spaces(5);
			sb.append(args).append(KoanConstants.EOL);
			sb.append(fiveSpaces).append(argType.name()).append(": ").append(
					makeCharWidth(70, getPositionOnCurrentLine(sb), argType.description()))
					.append(KoanConstants.EOL);
		}
		ApplicationUtils.getPresenter().displayMessage(sb.toString());
	}

	private String makeCharWidth(int width, int positionOnCurrentLine, String text) {
		int upperIndexForLine = width - positionOnCurrentLine;
		if(upperIndexForLine > text.length()){
			upperIndexForLine = text.length();
		}
		StringBuilder sb = new StringBuilder(text.substring(0, upperIndexForLine)).append(
				KoanConstants.EOL);
		for(int i = upperIndexForLine; i < text.length(); i += width){
			upperIndexForLine += width;
			if(upperIndexForLine > text.length()){
				upperIndexForLine = text.length();
			}
			sb.append(text.substring(i,upperIndexForLine))
				.append(KoanConstants.EOL);
		}
		return sb.toString();
	}

	private int getPositionOnCurrentLine(StringBuilder sb) {
		return getTextOnCurrentLine(sb).length();
	}

	private String getTextOnCurrentLine(StringBuilder sb) {
		return sb.substring(sb.lastIndexOf(KoanConstants.EOL), sb.length());
	}

	protected ArgumentType[] getArgumentTypes() {
		return ArgumentType.values();
	}

	private String spaces(int i) {
		StringBuilder spaces = new StringBuilder(i);
		while(spaces.length() < i){
			spaces.append(" ");
		}
		return spaces.toString();
	}

	public void line(StringBuilder builder, String text){
		builder.append(text).append(KoanConstants.EOL);
	}
	
}
