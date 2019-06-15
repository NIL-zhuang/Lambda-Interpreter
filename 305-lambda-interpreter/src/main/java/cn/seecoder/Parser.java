package cn.seecoder;

import java.util.ArrayList;

public class Parser {
    private Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    AST parse() {
        return term(new ArrayList<>());
    }

    /**
     * Term ::= Application| LAMBDA LCID DOT Term
     */
    private AST term(ArrayList<String> ctx) {
        if (this.lexer.getNextToken(TokenType.LAMBDA)) {
            if (lexer.next(TokenType.LCID)) {
                String param = lexer.token.value;       //check if it matches LAMBDA LCID DOT Term
                lexer.getNextToken(TokenType.LCID);            //nextToken
                if (lexer.getNextToken(TokenType.DOT)) {
                    ctx.add(0, param);
                    AST aTerm = term(ctx);
                    String valOfParam = String.valueOf(ctx.indexOf(param));        //param的层数，也就是当前的德布鲁因值
                    ctx.remove(ctx.indexOf(param));
                    return new Abstraction(new Identifier(param, valOfParam), aTerm);
                }
            }
        } else {
            return application(ctx);        // it is an application
        }
        return null;
    }

    /**
     * application ::= atom application
     */
    private AST application(ArrayList<String> ctx) {
        AST lhs = this.atom(ctx);
        AST rhs;
        while (true) {
            rhs = atom(ctx);
            if (rhs == null) {
                return lhs;
            } else {
                lhs = new Application(lhs, rhs);
            }
        }
    }

    /**
     * atom ::= LPAREN term RPAREN
     * | LCID
     */
    private AST atom(ArrayList<String> ctx) {
        String param;
        if (this.lexer.getNextToken(TokenType.LPAREN)) {
            AST term = term(ctx);                           // it is a term
            if (this.lexer.getNextToken((TokenType.RPAREN))) {
                return term;
            }
        } else if (this.lexer.next(TokenType.LCID)) {
            param = lexer.token.value;                      // it is an LCID
            lexer.getNextToken(TokenType.LCID);
            return new Identifier(param, String.valueOf(ctx.indexOf(param)));
        }
        return null;
    }

    public static void main(String[] args) {
        String source = "(\\x.x)(\\y.y)";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        AST ast = parser.parse();
        System.out.println(ast.toString());
        ast.printTree(ast, 0);
    }

}
