package org.talend.daikon.container;

import java.util.Date;

public class DateConverter extends Converter<Date> {

    @Override
    public Date convert(Object value) {
        throw new RuntimeException("We do not support date pattern yet.");
    }
}
