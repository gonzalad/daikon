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
import org.apache.avro.generic.GenericData;
import org.talend.daikon.avro.path.TraversalPath;

/**
 * Wrapper for arrays.
 *
 * Calling the accept method will call {@link RecordVisitor#startArray(VisitableArray)} then
 * create a wrapper object for each value contained and call the corresponding accept method
 * on this wrapper to trigger its visit and finally call {@link RecordVisitor#endArray(VisitableArray)}
 */
public class VisitableArray extends AbstractVisitableStructure<GenericData.Array> {

    public VisitableArray(GenericData.Array value, TraversalPath path) {
        super(value, path);
    }

    @Override
    public void accept(RecordVisitor visitor) {
        visitor.startArray(this);
        GenericData.Array array = this.getValue();
        Schema elementSchema = array.getSchema().getElementType();
        for (int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            TraversalPath path = this.getPath().appendArrayIndex(i);
            VisitableStructure element = VisitableStructureFactory.createVisitableStructure(elementSchema, value, path);
            element.accept(visitor);
        }
        visitor.endArray(this);
    }
}
