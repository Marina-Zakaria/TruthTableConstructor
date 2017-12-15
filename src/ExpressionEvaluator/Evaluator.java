package ExpressionEvaluator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Stack;

public class Evaluator {
	private ArrayList<boolean[]> propositions = new ArrayList<>();
	private ArrayList<boolean[]> intermediate = new ArrayList<>();

	private ArrayList<Character> propositionNames = new ArrayList<>();
	private ArrayList<Character> intermediateNames = new ArrayList<>();

	private int rows;
	private boolean[] result;
	private String expression;

	public Evaluator(String expression) {
		this.expression = expression.toLowerCase().replaceAll("->", ">").replaceAll("<->", "<").replaceAll(" ", "");
		evaluatExpression();
	}

	private void findPropositions() {
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) >= 'a' && expression.charAt(i) <= 'z'
					&& !propositionNames.contains(expression.charAt(i))) {
				propositionNames.add(expression.charAt(i));
			}
		}
		rows = (int) Math.pow(2, propositionNames.size());
		for (int i = 0; i < propositionNames.size(); i++) {
			boolean[] values = new boolean[rows];
			int j = 0;
			while (j < rows) {
				for (int j2 = 0; j2 < Math.pow(2, i); j2++) {
					values[j++] = false;
				}
				for (int j2 = 0; j2 < Math.pow(2, i); j2++) {
					values[j++] = true;
				}
			}
			propositions.add(values);
		}

	}

	private String infixTPostfix(String expression) {
		Stack<Character> operands = new Stack<>();
		String infix = expression;
		String postfix = "";
		for (int i = 0; i < infix.length(); i++) {
			Character current = infix.charAt(i);
			if (current == '(') {
				operands.push(current);
			} else if (current >= 'a' && current <= 'z') {
				postfix = postfix + current;
			} else if (current == '~') {
				operands.push(current);
			} else if (current == '^') {
				if (!operands.isEmpty() && operands.get(operands.size() - 1) == '~') {
					postfix = postfix + operands.pop();
				}
				operands.push(current);
			} else if (current == '|') {
				while (!operands.isEmpty()
						&& (operands.get(operands.size() - 1) == '^' || operands.get(operands.size() - 1) == '~')) {
					postfix = postfix + operands.pop();
				}
				operands.push(current);
			} else if (current == '>') {
				while (!operands.isEmpty() && ((operands.get(operands.size() - 1) == '^'
						|| operands.get(operands.size() - 1) == '~' || operands.get(operands.size() - 1) == '|'))) {
					postfix = postfix + operands.pop();
				}
				operands.push(current);
			} else if (current == '<') {
				while (!operands.isEmpty() && ((operands.get(operands.size() - 1) == '^'
						|| operands.get(operands.size() - 1) == '~' || operands.get(operands.size() - 1) == '|')
						|| operands.get(operands.size() - 1) == '>')) {
					postfix = postfix + operands.pop();
				}
				operands.push(current);
			} else if (current == ')') {
				while (!operands.isEmpty() && (operands.get(operands.size() - 1) != '(')) {
					postfix = postfix + operands.pop();
				}
				operands.pop();
			}
		}
		while (!operands.isEmpty()) {
			postfix = postfix + operands.pop();
		}
		return postfix;
	}

	private void EvaluatePostfix(String postfix) {
		Stack<Character> helper = new Stack<>();
		char current;
		String simpleExpression = "";
		for (int i = 0; i < postfix.length(); i++) {
			simpleExpression = "";
			current = postfix.charAt(i);
			if (current >= 'a' && current <= 'z' || current >= 'A' && current <= 'Z') {
				helper.push(current);
			} else if (current == '~') {
				simpleExpression = simpleExpression + current + (char) helper.pop();
				evaluateSimpleExpression(simpleExpression);
				helper.push(intermediateNames.get(intermediateNames.size() - 1));
			} else {
				simpleExpression = simpleExpression + (char) helper.pop() + current + (char) helper.pop();
				evaluateSimpleExpression(simpleExpression);
				helper.push(intermediateNames.get(intermediateNames.size() - 1));
			}
		}
		this.result = intermediate.get(intermediateNames.size() - 1);

	}

	private void evaluateSimpleExpression(String expression) {

		if (expression.contains("~")) {
			Character operand;
			boolean[] operandArray;
			operand = expression.toCharArray()[1];
			if (propositionNames.contains(operand)) {
				operandArray = propositions.get(propositionNames.indexOf(operand));
			} else {
				operandArray = intermediate.get(intermediateNames.indexOf(operand));
			}
			boolean[] result = new boolean[operandArray.length];

			for (int i = 0; i < operandArray.length; i++) {
				result[i] = !operandArray[i];
			}
			intermediate.add(result);
		} else {
			Character operand1 = expression.toCharArray()[0];
			boolean[] operandArray1;
			if (propositionNames.contains(operand1)) {
				operandArray1 = propositions.get(propositionNames.indexOf(operand1));
			} else {
				operandArray1 = intermediate.get(intermediateNames.indexOf(operand1));
			}

			Character operand2 = expression.toCharArray()[2];
			boolean[] operandArray2;
			if (propositionNames.contains(operand2)) {
				operandArray2 = propositions.get(propositionNames.indexOf(operand2));
			} else {
				operandArray2 = intermediate.get(intermediateNames.indexOf(operand2));
			}
			boolean[] result = new boolean[operandArray1.length];

			Character operator = expression.toCharArray()[1];
			if (operator == '^') {
				for (int i = 0; i < result.length; i++) {
					result[i] = operandArray1[i] && operandArray2[i];
				}
			} else if (operator == '|') {
				for (int i = 0; i < result.length; i++) {
					result[i] = operandArray1[i] || operandArray2[i];
				}
			} else if (operator == '>') {
				for (int i = 0; i < result.length; i++) {
					result[i] = !operandArray2[i] || operandArray1[i];
				}
			} else if (operator == '<') {
				for (int i = 0; i < result.length; i++) {
					result[i] = (!operandArray2[i] || operandArray1[i]) ^ (!operandArray1[i] || operandArray2[i]);
				}
			}
			intermediate.add(result);

		}
		intermediateNames.add((char) ('A' + (int) intermediateNames.size()));
	}

	/**
	 * @return the result
	 */
	public boolean[] getResult() {

		return result;
	}

	private void evaluatExpression() {
		this.findPropositions();
		this.EvaluatePostfix(this.infixTPostfix(expression));
	}

	public boolean isTautology() {
		for (int i = 0; i < this.getResult().length; i++) {
			if (result[i] == false) {
				return false;
			}
		}
		return true;
	}

	public boolean isContradiction() {
		for (int i = 0; i < this.getResult().length; i++) {
			if (result[i] == true) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the propositions
	 */
	public ArrayList<boolean[]> getPropositions() {
		return propositions;
	}

	/**
	 * @return the propositionNames
	 */
	public ArrayList<Character> getPropositionNames() {
		return propositionNames;
	}

	public void createTruthTable() {

		for (int i = 0; i < propositionNames.size(); i++) {
			System.out.print("\t");
			System.out.print(propositionNames.get(i));
		}
		System.out.print("\t " + expression + "\n");
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < propositions.size(); j++) {
				System.out.print("     ");

				System.out.print(propositions.get(j)[i]);

			}
			System.out.print("     ");

			System.out.print(result[i]);
			System.out.println("\n");
		}
	}

	public String booleanToString(boolean value) {
		if (value) {
			return "true";
		}
		return "false";
	}

	public void save() {
File file = new File("expression.txt");
		try {
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
			for (int i = 0; i < propositionNames.size(); i++) {
				writer.write("\t  ");
				writer.write(propositionNames.get(i));
			}
			writer.write("\t\t  " + expression );
			writer.newLine();
			for (int i = 0; i < result.length; i++) {
				for (int j = 0; j < propositions.size(); j++) {
					writer.write("\t");

					writer.write(booleanToString(propositions.get(j)[i]));

				}
				writer.write("\t\t");

				writer.write(booleanToString(result[i]));
				writer.newLine();
			}
			writer.close();

		} catch (IOException ex) {
			System.out.println(ex);
		} finally {
			try {
			} catch (Exception ex) {
				/* ignore */}
		}
	}

}
