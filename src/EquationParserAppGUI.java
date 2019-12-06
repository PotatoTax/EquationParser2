import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class EquationParserAppGUI {
    private JTable variablesTable;
    private JPanel rootPanel;
    private JTextField equationInputField;
    private JButton evaluateButton;
    private JLabel resultLabel;
    private JButton inputButton;

    private Map<Character, Integer> variableMap;

    private EquationParserAppGUI() {
        variableMap = new HashMap<>();

        ActionListener actionListener = e -> {
            if (e.getSource() == inputButton) {
                ingestEquation();
            } else if (e.getSource() == evaluateButton) {
                evaluateEquation();
            }
        };

        inputButton.addActionListener(actionListener);
        evaluateButton.addActionListener(actionListener);
    }

    private void ingestEquation() {
        Parser parser = new Parser(equationInputField.getText());

        String[] vars = parser.getVariables();
        String[] values = new String[vars.length];
        System.out.println(vars);

        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("Variable Name", vars);
        tableModel.addColumn("Value", values);

        variablesTable.setModel(tableModel);
    }

    private void evaluateEquation() {

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bank");
        frame.setPreferredSize(new Dimension(600, 400));
        frame.setContentPane(new EquationParserAppGUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
