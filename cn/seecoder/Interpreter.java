package cn.seecoder;

public class Interpreter {

    public static boolean isValue(AST ast) {
        return ast instanceof Abstraction;
    }

    public static AST eval(AST ast) {
        while (true) {
            if (ast instanceof Application) {
                if (isValue(((Application) ast).lhs) && isValue(((Application) ast).rhs)) {
                    /*
                     * if both sides of the application are values
                     * we can proceed and substitute the rhs with the abstraction's parameter in the body
                     */
                    ast = substitute(((Application) ast).rhs, ((Application) ast).body);
                } else if (isValue(((Application) ast).lhs)) {
                    /*
                     * in that the left part is already value
                     * we should only evaluate rhs
                     */
                    ((Application) ast).rhs = eval(((Application) ast).rhs);
                } else {
                    /*
                     * left part isn't a value
                     * keep reducing it
                     */
                    ((Application) ast).lhs = eval(((Application) ast).lhs);
                }
            } else if (isValue(ast)) {
                /*
                 * congratulations! you've reached an end
                 */
                return ast;
            }
        }
    }

    public static AST traverse(AST ast) {

        return null;
    }

    public static AST substitute(AST value, AST node) {
        return null;
    }

    public static AST subst(AST node, AST value, int depth) {
        return null;
    }

    public static AST shift(int by, AST node, int from) {
        if (node instanceof Application) {
            return new Application(shift(by, ((Application) node).lhs, from), shift(by, ((Application) node).rhs, from));
        } else if (node instanceof Abstraction) {
            return new Abstraction(((Abstraction) node).param, shift(by, ((Abstraction) ((Abstraction) node).body), from + 1));
        } else if (node instanceof Identifier) {
            int temp;
        }
        return null;
    }

    public static void main(String[] args) {
        // write your code here
        String source = "(\\x.\\y.x)(\\x.x)(\\y.y)";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        AST ast = parser.parse();
        AST result = Interpreter.eval(ast);
        System.out.println(result.toString());
    }
}
