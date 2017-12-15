package ExpressionEvaluator;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class TruthTableConstructor extends JFrame {

	private JPanel contentPane;
	private JTextField expression;
	JLabel lblErrors = new JLabel("errors");
	private JButton btnNewButton;
	private JLabel lblisTautollogy;
	private JButton btnIsContradicyion;
	private JButton btnNewButton_2;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TruthTableConstructor frame = new TruthTableConstructor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TruthTableConstructor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 746, 433);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		expression = new JTextField();
		expression.setBounds(33, 23, 286, 45);
		contentPane.add(expression);
		expression.setColumns(10);
		
		JTextArea result = new JTextArea();
		result.setBounds(10, 113, 422, 270);
		contentPane.add(result);
		
		JButton btnDrawTruthTable = new JButton("Draw Truth Table");
		btnDrawTruthTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				result.setText("");
				Validator v = new Validator(expression.getText());
				if(v.isValid()){
					Evaluator e = new Evaluator(expression.getText());
					for (int i = 0; i < e.getPropositionNames().size(); i++) {
						result.append("\t");
						result.append(e.getPropositionNames().get(i)+"");
					}
					result.append("\t " + expression.getText() + "\n");
					
					for (int i = 0; i < e.getResult().length; i++) {
						for (int j = 0; j < e.getPropositions().size(); j++) {
							result.append("\t");

							result.append(e.booleanToString(e.getPropositions().get(j)[i]));

						}
						result.append("\t");

						result.append(e.booleanToString(e.getResult()[i]));
						result.append("\n");
					}
				}else {
					lblErrors.setText(v.getErrorMsg());
				}
			}
		});
		btnDrawTruthTable.setBounds(33, 79, 168, 23);
		contentPane.add(btnDrawTruthTable);
		
		lblErrors.setBounds(329, 23, 391, 45);
		contentPane.add(lblErrors);
		
		lblisTautollogy = new JLabel("");
		lblisTautollogy.setBounds(442, 113, 278, 44);
		contentPane.add(lblisTautollogy);
		
		btnNewButton = new JButton("Is Tautology ??");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Validator v = new Validator(expression.getText());
				if(v.isValid()){
					Evaluator e = new Evaluator(expression.getText());
					if(e.isTautology()){
						lblisTautollogy.setText("yes , the expression is a tautology ");
					}else{
						lblisTautollogy.setText("no , the expression is not a tautology ");
					}
				}else {
					lblErrors.setText(v.getErrorMsg());
				}
			}
		});
		btnNewButton.setBounds(525, 79, 135, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblisContradiction = new JLabel("");
		lblisContradiction.setBounds(442, 202, 278, 44);
		contentPane.add(lblisContradiction);
		
		btnIsContradicyion = new JButton("Is Contradiction ??");
		btnIsContradicyion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Validator v = new Validator(expression.getText());
				if(v.isValid()){
					Evaluator e = new Evaluator(expression.getText());
					if(e.isContradiction()){
						lblisContradiction.setText("yes , the expression is a contadiction ");
					}else{
						lblisContradiction.setText("no , the expression is not a contradiction ");
					}
				}else {
					lblErrors.setText(v.getErrorMsg());
				}
			}
		});
		btnIsContradicyion.setBounds(525, 168, 135, 23);
		contentPane.add(btnIsContradicyion);
		
		btnNewButton_2 = new JButton("save truth tabele");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Validator v = new Validator(expression.getText());
				if(v.isValid()){
					Evaluator e = new Evaluator(expression.getText());
					e.save();
				}else {
					lblErrors.setText(v.getErrorMsg());
				}
			}
		});
		btnNewButton_2.setBounds(525, 257, 135, 23);
		contentPane.add(btnNewButton_2);
		
		
	}
}
