import java.util.ArrayList;
import java.util.Map;

class Node {
    public enum Operation {
        ADD, SUB, MULT, DIV, EXP, SIN, COS, TAN, FACT, OPEN_PAREN, CLOSE_PAREN, VAR, CONST, NEG
    }

    private Operation operation;

    private int precedence;

    private double value;

    private char variableName;

    private Node parentNode;
    private Node leftNode;
    private Node rightNode;

    Node() {
        precedence = 0;
    }

    Node(double num) {
        operation = Operation.CONST;
        precedence = 7;
        value = num;
    }

    Node(char operator) {
        switch (operator) {
            case '(':
                operation = Operation.OPEN_PAREN;
                precedence = 1;
                break;
            case ')':
                operation = Operation.CLOSE_PAREN;
                precedence = 1;
                break;
            case '+':
                operation = Operation.ADD;
                precedence = 2;
                break;
            case '-':
                operation = Operation.SUB;
                precedence = 2;
                break;
            case '*':
                operation = Operation.MULT;
                precedence = 4;
                break;
            case '/':
                operation = Operation.DIV;
                precedence = 4;
                break;
            case '^':
                operation = Operation.EXP;
                precedence = 5;
                break;
            case '!':
                operation = Operation.FACT;
                precedence = 6;
                break;
        }

        if (96 < operator && operator < 123) {
            variableName = operator;
            operation = Operation.VAR;
            precedence = 7;
        }
    }

    Node(String operator) {
        switch (operator) {
            case "-ve":
                operation = Operation.NEG;
                precedence = 3;
                break;
            case "sin":
                operation = Operation.SIN;
                precedence = 7;
                break;
            case "cos":
                operation = Operation.COS;
                precedence = 7;
                break;
            case "tan":
                operation = Operation.TAN;
                precedence = 7;
                break;
        }
    }

    ArrayList<Character> getVariables() {
        if (operation == Operation.VAR) {
            return new ArrayList<>() {{
                add(variableName);
            }};
        }

        ArrayList<Character> variables = new ArrayList<>();
        if (rightNode != null) {
            variables.addAll(rightNode.getVariables());
        }
        if (leftNode != null) {
            variables.addAll(leftNode.getVariables());
        }

        return variables;
    }

    double getValue() {
        switch (operation) {
            case CONST:
                return value;
            case NEG:
                return -value;
            case SIN:
                return Math.sin(rightNode.getValue());
            case COS:
                return Math.cos(rightNode.getValue());
            case TAN:
                return Math.tan(rightNode.getValue());
            case ADD:
                return leftNode.getValue() + rightNode.getValue();
            case SUB:
                return leftNode.getValue() - rightNode.getValue();
            case MULT:
                return leftNode.getValue() * rightNode.getValue();
            case DIV:
                return leftNode.getValue() / rightNode.getValue();
            case EXP:
                return Math.pow(leftNode.getValue(), rightNode.getValue());
            case FACT:
                return factorial(leftNode.getValue());
            default:
                System.out.println("ERROR: No operator found while solving.");
                return 0;
        }
    }

    double getValue(Map<Character, Double> variables) {
        switch (operation) {
            case CONST:
                return value;
            case NEG:
                return -rightNode.getValue(variables);
            case VAR:
                return variables.get(variableName);
            case SIN:
                return Math.sin(rightNode.getValue(variables));
            case COS:
                return Math.cos(rightNode.getValue(variables));
            case TAN:
                return Math.tan(rightNode.getValue(variables));
            case ADD:
                return leftNode.getValue(variables) + rightNode.getValue(variables);
            case SUB:
                return leftNode.getValue(variables) - rightNode.getValue(variables);
            case MULT:
                return leftNode.getValue(variables) * rightNode.getValue(variables);
            case DIV:
                return leftNode.getValue(variables) / rightNode.getValue(variables);
            case EXP:
                return Math.pow(leftNode.getValue(variables), rightNode.getValue(variables));
            case FACT:
                return factorial(leftNode.getValue(variables));
            default:
                System.out.println("ERROR: No operator found while solving.");
                return 0;
        }
    }

    private double factorial(double num) {
        if (num <= 2) {
            return num;
        }
        return num * factorial(num-1);
    }

    int getPrecedence() {
        return precedence;
    }

    Node getParentNode() {
        return parentNode;
    }

    void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    Node getLeftNode() {
        return leftNode;
    }

    void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    Node getRightNode() {
        return rightNode;
    }

    void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    Operation getOperation() {
        return operation;
    }
}
