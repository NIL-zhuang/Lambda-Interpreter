package cn.seecoder;

import java.util.ArrayList;

public class Parser {
    Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public AST parse() {
        AST ast = term(new ArrayList<>());
        System.out.println(lexer.match(TokenType.EOF));
        return ast;
    }

    /**
     * Term ::= Application| LAMBDA LCID DOT Term
     */
    public AST term(ArrayList<String> ctx) {
        //check if it matches LAMBDA LCID DOT Term
        if (this.lexer.skip(TokenType.LAMBDA)) {
            if (lexer.match(TokenType.LCID)) {
                String param = lexer.token.value;
                lexer.nextToken();
                if (lexer.skip(TokenType.DOT)) {
                    ctx.add(0, param);
                    AST aTerm = term(ctx);
                    return new Abstraction(param, aTerm);
                }
            }
        } else {
            // it is an application
            return application(ctx);
        }

        return null;
    }

    /**
     * application ::= atom application
     */
    public AST application(ArrayList<String> ctx) {
        AST lhs = this.atom(ctx);
        while (true) {
            AST rhs = this.atom(ctx);
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
    public AST atom(ArrayList<String> ctx) {
        if (this.lexer.skip(TokenType.LPAREN)) {
            // it is a term
            AST term = this.term(ctx);
            if (this.lexer.match((TokenType.RPAREN))) {
                return term(ctx);
            }
        } else if (this.lexer.next(TokenType.LCID)) {
            // it is an LCID
            Token id = this.lexer.token(TokenType.LCID);
            return new Identifier(id.value);
        }
        return null;
    }


}
