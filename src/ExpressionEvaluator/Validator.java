package ExpressionEvaluator;

import java.util.Stack;

public class Validator {
	private String expression;
	private boolean isValid = true;
	private String errorMsg = "";

	public Validator(String expression) {
		super();
		this.expression = expression.toLowerCase().replaceAll(" ", "").replaceAll("->", ">").replaceAll("<->", "<");
		checkPropositions();
		checkParenthesis();
	}

	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * @return the isValid
	 */
	public boolean isValid() {
		return isValid;
	}

	private void checkPropositions() {
		char current;
		for (int i = 0; i < expression.length(); i++) {
			current = expression.charAt(i);
			if (i != expression.length() - 1) {

				if (current >= 'a' && current <= 'z') {
					if (expression.charAt(i + 1) >= 'a' && expression.charAt(i + 1) <= 'z') {
						isValid = false;
						errorMsg = "error at char " + current + " , must be followed by an operator";
						break;
					} else if (expression.charAt(i + 1) == '(') {
						isValid = false;
						errorMsg = "error at char " + current + " , must be followed by an operator";
						break;
					}
				} else if (current == '^' || current == '|' || current == '>' || current == '<') {
					if (i == 0) {
						isValid = false;
						errorMsg = "error at chat " + current + " , can't start with an operator other then (not)";
						break;
					} else if (expression.charAt(i + 1) == ')') {
						isValid = false;
						errorMsg = "error at char " + current + " , must be followed by an operand";
						break;

					}
				} else if (current == '~') {
					if (expression.charAt(i + 1) == '^' || expression.charAt(i + 1) == '|'
							|| expression.charAt(i + 1) == '>' || expression.charAt(i + 1) == '<') {
						isValid = false;
						errorMsg = "error at char " + current + " , must be followed by an operand";
						break;

					} else if (expression.charAt(i + 1) == ')') {
						isValid = false;
						errorMsg = "error at char " + current + " , must be followed by an operand";
						break;

					}
				} else if (current != '(' && current != ')') {
					isValid = false;
					errorMsg = "error at char " + current + " , this char is not valid";
					break;

				}
			}

		}

	}

	private void checkParenthesis() {
		Stack<Character> helper = new Stack<>();
		char current;
		for (int i = 0; i < expression.length(); i++) {
			current = expression.charAt(i);
			if (current == '(') {
				if (expression.charAt(i + 1) == '^' || expression.charAt(i + 1) == '|'
						|| expression.charAt(i + 1) == '>' || expression.charAt(i + 1) == '<') {
					isValid = false;
					errorMsg = "error at char " + current + " , must be followed by an operand";
					break;
				} else {
					helper.push(current);
				}
			} else if (current == ')' && helper.isEmpty()) {

				isValid = false;
				errorMsg = errorMsg + " , error at char " + current + " ,no opened parenthesis";
				break;
			} else if (current == ')') {
				if (!(expression.charAt(i + 1) == '^' || expression.charAt(i + 1) == '|'
						|| expression.charAt(i + 1) == '>' || expression.charAt(i + 1) == '<')) {
					isValid = false;
					errorMsg = "error at char " + current + " , must be followed by an operator other than (not)";
					break;
				} else {
					helper.pop();
				}
			}
		}
		if (!helper.isEmpty()) {
			isValid = false;
			errorMsg = errorMsg + " \nunclosed bracket";

		}
	}
}
