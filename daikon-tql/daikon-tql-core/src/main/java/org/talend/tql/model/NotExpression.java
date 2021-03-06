package org.talend.tql.model;

import org.talend.tql.visitor.IASTVisitor;

/*
 * Logical complement (negation) of the given Tql expression.
 */

/**
 * Created by bguillon on 23/06/16.
 */
public class NotExpression implements Atom {

    private final Expression expression;

    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "NotExpression{" + "expression=" + expression + '}';
    }

    @Override
    public Object accept(IASTVisitor visitor) {
        return visitor.visit(this);
    }
}
