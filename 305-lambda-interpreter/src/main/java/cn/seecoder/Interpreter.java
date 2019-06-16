package cn.seecoder;

public class Interpreter {
    private AST astAfterParser;

    public Interpreter(Parser parser) {
        astAfterParser = parser.parse();
        //System.out.println("After parser:"+astAfterParser.toString());
    }

    AST eval() {
        return evalAST(astAfterParser);
    }

    /**
     * Identifier是值
     * 如果是Application：若lhs是Identifier，则判断rhs；若lhs是Abstraction，那Application必然可以规约，不是值
     * 如果是Abstraction，则根据body来判断
     *
     * @param ast 需要判断的AST节点
     * @return ast是否为值
     */
    private boolean isValue(AST ast) {
        if (ast instanceof Identifier) {
            return true;
        } else if (ast instanceof Abstraction) {
            return isValue(((Abstraction) ast).body);
        } else if (ast instanceof Application) {
            if (((Application) ast).lhs instanceof Abstraction) {
                return false;
            } else {
                return isValue(((Application) ast).lhs) && isValue(((Application) ast).rhs);
            }
        }
        return false;
    }

    /**
     * 首先检测其是否为 Application，如果是，则对其求值：
     * 1. 若 Application 的两侧都是值，只要将所有出现的 x 用给出的值替换掉；
     * 2. 否则，若左侧为值，给右侧求值；
     * 3. 如果上面都不行，只对左侧求值；
     * 现在，如果下一个节点是 Identifier，我们只需将它替换为它所表示的变量绑定的值。
     * 最后，如果没有规则适用于AST，这意味着它已经是一个 value，我们将它返回。
     *
     * @param ast 一个根节点
     * @return ast
     */
    private AST evalAST(AST ast) {
        while (!isValue(ast)) {
//            ast.printTree(ast, ast.depth);
//            System.out.println();
            if (ast instanceof Application) {
                if (isValue(((Application) ast).lhs) && isValue(((Application) ast).rhs)) {
                    ast = substitute(((Abstraction) ((Application) ast).lhs).body, ((Application) ast).rhs);
                } else if (isValue(((Application) ast).lhs)) {
                    ((Application) ast).rhs = evalAST(((Application) ast).rhs);
                } else {
                    ((Application) ast).lhs = evalAST(((Application) ast).lhs);
                }
            } else if (ast instanceof Abstraction) {
                ((Abstraction) ast).body = evalAST(((Abstraction) ast).body);           //是abstraction后，如果body部分是value则返回，不是value则计算结果
                return ast;
            }
        }
        return ast;
    }


    private AST substitute(AST node, AST value) {
        return shift(-1, subst(node, shift(1, value, 0), 0), 0);
    }

    /**
     * value替换node节点中的变量：
     * 如果节点是Applation，分别对左右树替换；
     * 如果node节点是abstraction，替入node.body时深度得+1；
     * 如果node是identifier，则替换De Bruijn index值等于depth的identifier（替换之后value的值加深depth）
     *
     * @param value 替换成为的value
     * @param node  被替换的整个节点
     * @param depth 外围的深度       
     * @return AST
     */
    private AST subst(AST node, AST value, int depth) {
        System.out.println();
        if (node instanceof Application) {
            //左右两枝都替换
            return new Application(
                    subst(((Application) node).lhs, value, depth),
                    subst(((Application) node).rhs, value, depth)
            );
        } else if (node instanceof Abstraction) {
            //保留abs的参数，替入node.body时深度+1
            return new Abstraction(
                    ((Abstraction) node).param,
                    subst(((Abstraction) node).body, value, depth + 1)
            );
        } else if (node instanceof Identifier) {
            //深度相等则替换，不等不替换
            return Integer.toString(depth).equals(((Identifier) node).value) ? shift(depth, value, 0) : node;
        }
        return null;
    }

    /**
     * De Bruijn index值位移
     * 如果节点是Applation，分别对左右树位移；
     * 如果node节点是abstraction，新的body等于旧node.body位移by（from得+1）；
     * 如果node是identifier，则新的identifier的De Brujjn index值如果大于等于from则加by，否则加0（超出内层的范围的外层变量才要shift by位）.
     *
     * @param by   位移的距离
     * @param node 位移的节点
     * @param from 内层的深度，也就是要替换的节点所对应的De Burjin index值
     * @return AST
     */
    private static AST shift(int by, AST node, int from) {
        if (node instanceof Application) {
            //分别左右树位移，by和from的值都是原来的值
            return new Application(
                    shift(by, ((Application) node).lhs, from),
                    shift(by, ((Application) node).rhs, from)
            );
        }// 新的body等于旧node.body位移by（from得+1）
        else if (node instanceof Abstraction) {
            return new Abstraction(
                    ((Abstraction) node).param,
                    shift(by, ((Abstraction) node).body, from + 1)
            );
        } //新的identifier的De Brujjn index值如果大于等于from则加by，否则加0（超出内层的范围的外层变量才要shift by位）.
        else if (node instanceof Identifier) {
            int val = Integer.valueOf(((Identifier) node).value);
            return new Identifier(((Identifier) node).name, String.valueOf(val + ((val < from) ? 0 : by)));
        }
        return null;
    }

    private static String ZERO = "(\\f.\\x.x)";
    private static String SUCC = "(\\n.\\f.\\x.f (n f x))";
    private static String ONE = app(SUCC, ZERO);
    private static String TWO = app(SUCC, ONE);
    private static String THREE = app(SUCC, TWO);
    private static String FOUR = app(SUCC, THREE);
    private static String FIVE = app(SUCC, FOUR);
    private static String PLUS = "(\\m.\\n.((m " + SUCC + ") n))";
    private static String POW = "(\\b.\\e.e b)";       // POW not ready
    private static String PRED = "(\\n.\\f.\\x.n(\\g.\\h.h(g f))(\\u.x)(\\u.u))";
    private static String SUB = "(\\m.\\n.n" + PRED + "m)";
    private static String TRUE = "(\\x.\\y.x)";
    private static String FALSE = "(\\x.\\y.y)";
    private static String AND = "(\\p.\\q.p q p)";
    private static String OR = "(\\p.\\q.p p q)";
    private static String NOT = "(\\p.\\a.\\b.p b a)";
    private static String IF = "(\\p.\\a.\\b.p a b)";
    private static String ISZERO = "(\\n.n(\\x." + FALSE + ")" + TRUE + ")";
    private static String LEQ = "(\\m.\\n." + ISZERO + "(" + SUB + "m n))";
    private static String EQ = "(\\m.\\n." + AND + "(" + LEQ + "m n)(" + LEQ + "n m))";
    private static String MAX = "(\\m.\\n." + IF + "(" + LEQ + " m n)n m)";
    private static String MIN = "(\\m.\\n." + IF + "(" + LEQ + " m n)m n)";

    private static String app(String func, String x) {
        return "(" + func + x + ")";
    }

    private static String app(String func, String x, String y) {
        return "(" + "(" + func + x + ")" + y + ")";
    }

    private static String app(String func, String cond, String x, String y) {
        return "(" + func + cond + x + y + ")";
    }

    public static void main(String[] args) {
        // write your code here
        String[] sources = {
                ZERO,//0
                ONE,//1
                TWO,//2
                THREE,//3
                app(PLUS, ZERO, ONE),//4
                app(PLUS, TWO, THREE),//5
                app(POW, TWO, TWO),//6
                app(PRED, ONE),//7
                app(PRED, TWO),//8
                app(SUB, FOUR, TWO),//9
                app(AND, TRUE, TRUE),//10
                app(AND, TRUE, FALSE),//11
                app(AND, FALSE, FALSE),//12
                app(OR, TRUE, TRUE),//13
                app(OR, TRUE, FALSE),//14
                app(OR, FALSE, FALSE),//15
                app(NOT, TRUE),//16
                app(NOT, FALSE),//17
                app(IF, TRUE, TRUE, FALSE),//18
                app(IF, FALSE, TRUE, FALSE),//19
                app(IF, app(OR, TRUE, FALSE), ONE, ZERO),//20
                app(IF, app(AND, TRUE, FALSE), FOUR, THREE),//21
                app(ISZERO, ZERO),//22
                app(ISZERO, ONE),//23
                app(LEQ, THREE, TWO),//24
                app(LEQ, TWO, THREE),//25
                app(EQ, TWO, FOUR),//26
                app(EQ, FIVE, FIVE),//27
                app(MAX, ONE, TWO),//28
                app(MAX, FOUR, TWO),//29
                app(MIN, ONE, TWO),//30
                app(MIN, FOUR, TWO),//31
        };

//        for (int i = 0; i < sources.length; i++) {
//            String source = sources[i];
//            System.out.println(i + ":" + source);
//            Lexer lexer = new Lexer(source);
//            Parser parser = new Parser(lexer);
//            Interpreter interpreter = new Interpreter(parser);
//            AST result = interpreter.eval();
//            System.out.println(i + ":" + result.toString());
//        }
//
        String source = SUCC + ZERO;
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Interpreter interpreter = new Interpreter(parser);
        AST result = interpreter.eval();
        System.out.println(result.toString());
    }
}
