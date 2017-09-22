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

/**
 * An Avro record visitor
 *
 * To visit an Avro generic record, simply proceed as follows:
 *
 * <pre>
 *     {@code
 *     IndexedRecord yourRecord = ...;
 *     RecordVisitor yourVisitor = ...;
 *     new VisitableRecord(yourRecord).accept(yourVisitor);
 *     }
 * </pre>
 *
 * Your visitor implementation will be called while traversing the record structure.
 *
 */
public interface RecordVisitor {

    /**
     * visits an integer field
     * 
     * @param field the field to visit
     */
    void visit(VisitableInt field);

    /**
     * visits a long field
     * @param field the field to visit
     */
    void visit(VisitableLong field);

    /**
     * visits a string field
     * @param field the field to visit
     */
    void visit(VisitableString field);

    /**
     * visits a boolean field
     * @param field the field to visit
     */
    void visit(VisitableBoolean field);

    /**
     * visits a float field
     * @param field the field to visit
     */
    void visit(VisitableFloat field);

    /**
     * visits a double field
     * @param field the field to visit
     */
    void visit(VisitableDouble field);

    /**
     * visits a null field
     * @param field the field to visit
     */
    void visit(VisitableNull field);

    /**
     * visits a fixed field
     * @param field the field to visit
     */
    void visit(VisitableFixed field);

    /**
     * visits a bytes field
     * @param field the field to visit
     */
    void visit(VisitableBytes field);

    /**
     * starts visiting a record field. Called before the children fields are actually visited.
     * Children are visited in Schema order after this method is completed.
     * @param field the field to visit
     */
    void startRecord(VisitableRecord field);

    /**
     * stops visiting a record field. Called once all children fields were visited.
     * @param field the field to visit
     */
    void endRecord(VisitableRecord field);

    /**
     * starts visiting an array field. Called before values are actually visited.
     * Values are visited after this method is completed.
     * @param field the field to visit
     */
    void startArray(VisitableArray field);

    /**
     * stops visiting an array field. Called after all values were visited
     * @param field the field to visit
     */
    void endArray(VisitableArray field);

    /**
     * starts visiting a map field. Called before entries are actually visited.
     * Entries are visited after this method is completed.
     * @param field the field to visit
     */
    void startMap(VisitableMap field);

    /**
     * stops visiting a map field. Called once all entries were visited.
     * @param field the field to visit
     */
    void endMap(VisitableMap field);
}
