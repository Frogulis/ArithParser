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
            System.out.println(tokens.size() + " tokens:");
            for (Token t : tokens) {
                System.out.println(t.type + ": " + t.value);
            }
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

    private Expression parseTokens(ArrayList<Token> tokens)
    {
        //
        return null;
    }
};
