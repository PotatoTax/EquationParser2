import java.lang.reflect.Array;
import java.util.*;

public class Parser {

    private Node parentNode;
    private Node currentNode;

    public Parser(String equation) {
        parentNode = new Node();
        parse(equation);
    }

    private void parse(String equation) {
        List<Object> firstNode = getNextItem(equation);
        currentNode = (Node) firstNode.get(0);
        currentNode.setParentNode(parentNode);
        parentNode.setRightNode(currentNode);
        equation = equation.substring((int) firstNode.get(1));

        List<Object> newNode = getNextItem(equation);
        while (newNode.get(0) != null) {
            if (((Node) newNode.get(0)).getOperation() == Node.Operation.CLOSE_PAREN) {
                closeParentheses();
            } else {
                addNode((Node) newNode.get(0));
            }
            equation = equation.substring((int) newNode.get(1));
            if (equation.length() == 0) {
                break;
            }
            newNode = getNextItem(equation);
        }
    }

    private void closeParentheses() {
        while (currentNode.getOperation() != Node.Operation.OPEN_PAREN) {
            currentNode = currentNode.getParentNode();
        }

        currentNode.getParentNode().setRightNode(currentNode.getRightNode());
        currentNode = currentNode.getParentNode();
    }

    private void addNode(Node newNode) {

        if (newNode.getOperation() == Node.Operation.OPEN_PAREN ||
            newNode.getOperation() == Node.Operation.NEG) {
            while (currentNode.getPrecedence() > 7) {
                currentNode = currentNode.getParentNode();
            }
        } else {
            while (currentNode.getPrecedence() > newNode.getPrecedence()) {
                currentNode = currentNode.getParentNode();
            }
        }

        if (currentNode.getRightNode() != null) {
            newNode.setLeftNode(currentNode.getRightNode());
            newNode.getLeftNode().setParentNode(newNode);
        }

        newNode.setParentNode(currentNode);
        currentNode.setRightNode(newNode);

        currentNode = newNode;
    }

    private List<Object> getNextItem(String equation) {
        if (equation.startsWith("sin")) {
            return Arrays.asList(new Node("sin"), 3);
        } if (equation.startsWith("cos")) {
            return Arrays.asList(new Node("cos"), 3);
        } if (equation.startsWith("tan")) {
            return Arrays.asList(new Node("tan"), 3);
        }

        switch (equation.charAt(0)) {
            case '(':
                return Arrays.asList(new Node('('), 1);
            case ')':
                return Arrays.asList(new Node(')'), 1);
            case '+':
                return Arrays.asList(new Node('+'), 1);
            case '-':
                if (currentNode.getOperation() != Node.Operation.CONST &&
                    currentNode.getOperation() != Node.Operation.VAR) {
                    return Arrays.asList(new Node("-ve"), 1);
                }
                return Arrays.asList(new Node('-'), 1);
            case '*':
                return Arrays.asList(new Node('*'), 1);
            case '/':
                return Arrays.asList(new Node('/'), 1);
            case '^':
                return Arrays.asList(new Node('^'), 1);
            case '!':
                return Arrays.asList(new Node('!'), 1);
        }

        if (equation.charAt(0) > 96 && equation.charAt(0) < 123) {
            return Arrays.asList(new Node(equation.charAt(0)), 1);
        }

        StringBuilder number = new StringBuilder();

        if (equation.substring(0, 1).matches("[0-9|.]")) {
            while (equation.substring(0, 1).matches("[0-9|.]")) {
                number.append(equation.charAt(0));
                equation = equation.substring(1);
                if (equation.length() == 0) {
                    break;
                }
            }
            return Arrays.asList(new Node(Double.parseDouble(number.toString())), number.length());
        }

        return null;
    }

    public String[] getVariables() {
        ArrayList<Character> varsRep = parentNode.getVariables();
        ArrayList<Character> vars = new ArrayList<>();
        for (char c : varsRep) {
            if (!vars.contains(c)) {
                vars.add(c);
            }
        }

        String[] out = new String[vars.size()];
        for (int i = 0; i < vars.size(); i++) {
            out[i] = String.valueOf(vars.get(i));
        }

        return out;
    }

    public double evaluate() {
        return parentNode.getRightNode().getValue();
    }

    public double evaluate(Map<Character, Double> variables) {
        return parentNode.getRightNode().getValue(variables);
    }

    public static void main(String[] args) {
        Map<Character, Double> variables = new HashMap<>() {{
            put('x', (double) 2);
        }};

        Parser p = new Parser("(2^-1)^(3/(3^3)/9)!");
        System.out.println(p.evaluate(variables));
    }
}
