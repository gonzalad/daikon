package org.talend.tql.model;

import org.talend.tql.visitor.IASTVisitor;

/*
 * Literal value within a Tql expression.
 */

/**
 * Created by gmzoughi on 24/06/16.
 */
public class LiteralValue implements TqlElement {

    private final Enum literal;

    private final String value;

    public LiteralValue(Enum literal, String value) {
        this.literal = literal;
        this.value = value;
    }

    public Enum getLiteral() {
        return literal;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "LiteralValue{" + "literal=" + literal + ", value='" + value + '\'' + '}';
    }

    @Override
    public Object accept(IASTVisitor visitor) {
        return visitor.visit(this);
    }

    public enum Enum {
        QUOTED_VALUE,
        INT,
        DECIMAL,
        BOOLEAN
    }
}
