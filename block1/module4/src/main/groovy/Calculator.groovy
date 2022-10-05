import java.util.function.Function

class Calculator {
    static def calc(String input) {
        return evaluate(input)
    }

    static def evaluate(String expression) {
        def tokens = expression.toCharArray()
        def range = ('0'..'9')

        Stack<Integer> values = new Stack<>()
        Stack<Character> ops = new Stack<>()

        for (int i = 0; i < tokens.length; i++) {
            // if (tokens[i] >= '0' && tokens[i] <= '9') {
            if (('0'..'9').contains(tokens[i])) {
                def sbuf = ""

                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    sbuf <<= (tokens[i++])
                }
                values.push(sbuf as Integer)
                i--
            } else if ((tokens[i] =~ '\\(').size() > 0) {

                ops.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()) as Integer);
                ops.pop()
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '^') {

                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()) as Integer)
                }
                ops.push(tokens[i])
            }
        }

        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()) as Integer);
        }
        return values.pop()
    }

    static def hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return !((op1 == '*' || op1 == '/' || op1 == '^') && (op2 == '+' || op2 == '-'))
    }

    static def applyOp(Character op, Integer b, Integer a) {
        Map<Character, Integer> functionMap = new HashMap<>();
        functionMap.put('+', () -> a + b);
        functionMap.put('-', (() -> a - b));
        functionMap.put('*', (() -> a * b));
        functionMap.put('/', (() -> {
            if (b == 0) {
                throw new UnsupportedOperationException("Cannot divide by zero");
            }
            return a / b;
        }))


        return functionMap.get(op);

    }


}
