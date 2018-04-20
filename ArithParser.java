import java.util.ArrayList;

class ArithParser {
    // S ::= E
    // E ::= T X | T
    // X ::= O E
    // T ::= number
    // O ::= + | - | * | /
    private class Expression {
        public Expression()
        {
            this.t = null;
            this.x = null;
        }
        public Expression(String t, Suffix x)
        {
            this.t = t;
            this.x = x;
        }
        public String t;
        public Suffix x;
    };

    private class Suffix {
        public Suffix()
        {
            this.o = null;
            this.e = null;
        }
        public Suffix(String o, Expression e)
        {
            this.o = o;
            this.e = e;
        }
        public String o;
        public Expression e;
    };

    private class Token {
        public String type;
        public String value;
    };

    public static void main(String args[])
    {
        if (args.length != 1) {
            System.out.println("Usage: ArithParser [quoted expression]");
            return;
        }
        ArithParser parser = new ArithParser();
        try {
            ArrayList<Token> tokens = parser.tokenize(args[0]);
            Expression sentence = parser.parseTokens(tokens);
            System.out.println(parser.evaluate(sentence));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private ArrayList<Token> tokenize(String input) throws Exception
    {
        StringBuffer buf = new StringBuffer(input);
        ArrayList<Token> tokens = new ArrayList<Token>();

        while (buf.length() > 0) {
            if (buf.charAt(0) == ' ') {
                buf.deleteCharAt(0);
            }
            else if (Character.isDigit(buf.charAt(0))) {
                tokens.add(getNumberToken(buf));
            }
            else if ("+-/*".contains(Character.toString(buf.charAt(0)))) {
                tokens.add(getOperatorToken(buf));
            }
            else {
                throw new Exception("Illegal symbol: " + buf.charAt(0));
            }
        }
        return tokens;
    }

    private Token getNumberToken(StringBuffer buf)
    {
        StringBuffer result = new StringBuffer();
        while (buf.length() > 0 && Character.isDigit(buf.charAt(0))) {
            result.append(buf.charAt(0));
            buf.deleteCharAt(0);
        }
        Token output = new Token();
        output.type = "TOKEN_NUMBER";
        output.value = result.toString();
        return output;
    }

    private Token getOperatorToken(StringBuffer buf)
    {
        Token output = new Token();
        output.type = "TOKEN_OPERATOR";
        output.value = Character.toString(buf.charAt(0));
        buf.deleteCharAt(0);
        return output;
    }

    private Expression parseTokens(ArrayList<Token> tokens) throws Exception
    {
        return parseExpression(tokens);
    }

    private Expression parseExpression(ArrayList<Token> tokens) throws Exception
    {
        if (tokens.size() == 0) {
            return null;
        }
        Expression result = new Expression();
        if (tokens.get(0).type != "TOKEN_NUMBER") {
            throw new Exception("Expected number, got: " + tokens.get(0).value);
        }
        result.t = tokens.get(0).value;
        result.x = parseSuffix(new ArrayList<Token>(tokens.subList(1, tokens.size())));
        return result;
    }

    private Suffix parseSuffix(ArrayList<Token> tokens) throws Exception
    {
        if (tokens.size() == 0) {
            return null;
        }
        Suffix result = new Suffix();
        if (tokens.get(0).type != "TOKEN_OPERATOR") {
            throw new Exception("Expected operator, got: " + tokens.get(0).value);
        }
        result.o = tokens.get(0).value;
        result.e = parseExpression(new ArrayList<Token>(tokens.subList(1, tokens.size())));
        return result;
    }

    private int evaluate(Expression expr)
    {
        Expression pointer = expr;
        int result = Integer.parseInt(pointer.t, 10);
        while (true) {
            if (pointer.x != null) {
                if (pointer.x.o.equals("+")) {
                    result += Integer.parseInt(pointer.x.e.t, 10);
                }
                else if (pointer.x.o.equals("-")) {
                    result -= Integer.parseInt(pointer.x.e.t, 10);
                }
                else if (pointer.x.o.equals("*")) {
                    result *= Integer.parseInt(pointer.x.e.t, 10);
                }
                else if (pointer.x.o.equals("/")) {
                    result /= Integer.parseInt(pointer.x.e.t, 10);
                }
                pointer = pointer.x.e;
            }
            else {
                break;
            }
        }
        return result;
    }
};
