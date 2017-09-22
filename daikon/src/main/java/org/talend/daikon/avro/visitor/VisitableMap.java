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
package org.talend.daikon.avro.visitor;

import org.apache.avro.Schema;
import org.talend.daikon.avro.path.TraversalPath;

import java.util.Map;

/**
 * Wrapper for maps.
 */
public class VisitableMap extends AbstractVisitableStructure<Map<String, Object>> {

    public VisitableMap(Map<String, Object> value, TraversalPath path) {
        super(value, path);
    }

    @Override
    public void accept(RecordVisitor visitor) {
        visitor.startMap(this);
        Schema schema = getPath().last().getSchema();
        Schema elementSchema = schema.getElementType();
        Map<String, Object> map = this.getValue();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            TraversalPath path = this.getPath().appendMapEntry(key);
        }

        visitor.endMap(this);
    }
}
