/*
 * Copyright (C) 2019-2020 University of Dundee & Open Microscopy Environment.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ome.util;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

/**
 * A Hibernate type for string-valued properties where only privileged readers see non-{@code null} values.
 * @author m.t.b.carroll@dundee.ac.uk
 * @since 5.5.5
 */
/* TODO: With Hibernate 4.3 could try AttributeConverter and @Convert. */
@SuppressWarnings("serial")
public abstract class PrivilegedStringType extends AbstractSingleColumnStandardBasicType<String>
    implements DiscriminatorType<String> {

    /** String type for which users read {@code null} except for full administrators. */
    public static class FilteredFullAdmin extends PrivilegedStringType {
        public FilteredFullAdmin() {
            super(VarcharTypeDescriptor.INSTANCE, PrivilegedStringTypeDescriptor.FilteredFullAdmin.INSTANCE);
        }
    };

    /** String type for which users read a dummy value except for full administrators. */
    public static class FilteredFullAdminHidden extends PrivilegedStringType {
        public FilteredFullAdminHidden() {
            super(VarcharTypeDescriptor.INSTANCE, PrivilegedStringTypeDescriptor.FilteredFullAdminHidden.INSTANCE);
        }
    };

    /** String type for which users read a dummy UUID except for full administrators. */
    public static class FilteredFullAdminUUID extends PrivilegedStringType {
        public FilteredFullAdminUUID() {
            super(VarcharTypeDescriptor.INSTANCE, PrivilegedStringTypeDescriptor.FilteredFullAdminUUID.INSTANCE);
        }
    };

    /** String type for which users read {@code null} except for (potentially) related users. */
    public static class FilteredRelatedUser extends PrivilegedStringType {
        public FilteredRelatedUser() {
            super(VarcharTypeDescriptor.INSTANCE, PrivilegedStringTypeDescriptor.FilteredRelatedUser.INSTANCE);
        }
    };

    /** String type for which users read a dummy value except for (potentially) related users. */
    public static class FilteredRelatedUserHidden extends PrivilegedStringType {
        public FilteredRelatedUserHidden() {
            super(VarcharTypeDescriptor.INSTANCE, PrivilegedStringTypeDescriptor.FilteredRelatedUserHidden.INSTANCE);
        }
    };

    /**
     * @param sqlInstance the SQL type of the privileged strings
     * @param javaInstance the Java type of the privileged strings
     */
    public PrivilegedStringType(VarcharTypeDescriptor sqlInstance, PrivilegedStringTypeDescriptor javaInstance) {
        super(sqlInstance, javaInstance);
    }

    /* Adapted from org.hibernate.type.StringType. */

    @Override
    public String getName() {
        return "securestring";
    }

    @Override
    public String objectToSQLString(String value, Dialect dialect) throws Exception {
        return '\'' + value + '\'';
    }

    @Override
    public String stringToObject(String xml) throws Exception {
        return xml;
    }

    @Override
    public String toString(String value) {
        return value;
    }
}
