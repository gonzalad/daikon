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
import org.apache.avro.generic.IndexedRecord;
import org.talend.daikon.avro.path.TraversalPath;

/**
 * Main entry point of the Avro visitable API.
 *
 * To visit an IndexedRecord:
 *
 * Calling the accept method will call {@link RecordVisitor#startRecord(VisitableRecord)} then
 * create a wrapper object for each field contained in the record and call the corresponding accept
 * method on this wrapper to trigger its visit and finally call {@link RecordVisitor#endRecord(VisitableRecord)}
 */
public class VisitableRecord extends AbstractVisitableStructure<IndexedRecord> {

    /**
     * Create a new visitable record from an existing Avro {@link IndexedRecord}
     * 
     * @param record the record to visit
     */
    public VisitableRecord(IndexedRecord record) {
        this(record, TraversalPath.create(record.getSchema()));
    }

    VisitableRecord(IndexedRecord value, TraversalPath path) {
        super(value, path);
    }

    @Override
    public void accept(RecordVisitor visitor) {
        visitor.startRecord(this);
        final Schema schema = this.getValue().getSchema();
        for (Schema.Field field : schema.getFields()) {
            final TraversalPath fieldPath = this.getPath().append(field.name(), field.pos(), field.schema());
            final VisitableStructure node = VisitableStructureFactory.createVisitableField(field, getValue(), fieldPath);
            node.accept(visitor);
        }
        visitor.endRecord(this);
    }
}
