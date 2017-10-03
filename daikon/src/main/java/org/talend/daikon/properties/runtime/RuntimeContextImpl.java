// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.daikon.properties.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link RuntimeContext} default implementation based on {@link HashMap}
 */
public class RuntimeContextImpl implements RuntimeContext {

    private Map<String, Object> map = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getData(String key) {
        return map.get(key);
    }

    /**
     * Sets some data
     */
    public void setData(String key, Object value) {
        map.put(key, value);
    }

}
