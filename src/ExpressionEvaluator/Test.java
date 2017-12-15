package ExpressionEvaluator;

public class Test {

	public static void main(String[] args) {
		Validator v = new Validator("p -> q");
		
		if(v.isValid()){
		Evaluator e = new Evaluator("p -> q ");
		e.createTruthTable();
		}else{
			System.out.println(v.getErrorMsg());
		}
	}

}
