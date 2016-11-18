package org.talend.daikon.container;

import java.nio.ByteBuffer;

public class StringConverter extends Converter<String> {

    @Override
    public String convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof ByteBuffer) {
            return new String(((ByteBuffer) value).array());
        } else {
            return value.toString();
        }
    }
}
