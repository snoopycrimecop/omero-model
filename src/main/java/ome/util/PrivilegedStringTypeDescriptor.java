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

import java.util.EnumMap;
import java.util.function.Function;

import org.hibernate.HibernateException;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.StringTypeDescriptor;

/**
* The type descriptors for the privileged string types of {@link PrivilegedStringType}.
 * @author m.t.b.carroll@dundee.ac.uk
 * @since 5.5.5
  */
@SuppressWarnings("serial")
public abstract class PrivilegedStringTypeDescriptor extends StringTypeDescriptor {

    /**
     * The privileged string filters that may be applied.
     */
    public static enum Filter {
        /* Only full administrators pass the filter. */
        FULL_ADMIN,
        /* Only full administrators pass the filter to read the not-nullable value. */
        FULL_ADMIN_HIDDEN,
        /* Only full administrators pass the filter to read the UUID. */
        FULL_ADMIN_UUID,
        /* Only related users pass the filter. */
        RELATED_USER,
        /* Only related users pass the filter to read the not-nullable value. */
        RELATED_USER_HIDDEN;
    }

    /**
     * Type descriptor corresponding to {@link Filter#FULL_ADMIN}
     * for {@link PrivilegedStringType.FilteredFullAdmin}.
     */
    public static class FilteredFullAdmin extends PrivilegedStringTypeDescriptor {
        static final PrivilegedStringTypeDescriptor INSTANCE = new FilteredFullAdmin();

        @Override
        protected Function<Long, Boolean> getFilter() {
            return FILTERS.get(Filter.FULL_ADMIN);
        }
    };

    /**
     * Type descriptor corresponding to {@link Filter#FULL_ADMIN_HIDDEN}
     * for {@link PrivilegedStringType.FilteredFullAdminHidden}.
     */
    public static class FilteredFullAdminHidden extends FilteredFullAdmin {
        static final PrivilegedStringTypeDescriptor INSTANCE = new FilteredFullAdminHidden();

        @Override
        protected String getFilterFailure() {
            return "<hidden>";
        }
    };

    /**
     * Type descriptor corresponding to {@link Filter#FULL_ADMIN_UUID}
     * for {@link PrivilegedStringType.FilteredFullAdminUUID}.
     */
    public static class FilteredFullAdminUUID extends FilteredFullAdmin {
        static final PrivilegedStringTypeDescriptor INSTANCE = new FilteredFullAdminUUID();

        @Override
        protected String getFilterFailure() {
            return "********-****-****-****-*************";
        }
    };

    /**
     * Type descriptor corresponding to {@link Filter#RELATED_USER}
     * for {@link PrivilegedStringType.FilteredRelatedUser}.
     */
    public static class FilteredRelatedUser extends PrivilegedStringTypeDescriptor {
        static final PrivilegedStringTypeDescriptor INSTANCE = new FilteredRelatedUser();

        @Override
        protected Function<Long, Boolean> getFilter() {
            return FILTERS.get(Filter.RELATED_USER);
        }
    };

    /**
     * Type descriptor corresponding to {@link Filter#RELATED_USER_HIDDEN}
     * for {@link PrivilegedStringType.FilteredRelatedUserHidden}.
     */
    public static class FilteredRelatedUserHidden extends FilteredRelatedUser {
        static final PrivilegedStringTypeDescriptor INSTANCE = new FilteredRelatedUserHidden();

        @Override
        protected String getFilterFailure() {
            return "<hidden>";
        }
    };

    /**
     * The filter implementation for each of the {@link Filter} values.
     */
    private static EnumMap<Filter, Function<Long, Boolean>> FILTERS = new EnumMap<>(Filter.class);

    /**
     * Set the filter implementation for a value of {@link Filter}.
     * Note that only those for {@link Filter#FULL_ADMIN} and {@link Filter#RELATED_USER} are consulted.
     * @param filterType a type of privileged string filter
     * @param filter the implementation for that filter
     */
    public static void setFilter(Filter filterType, Function<Long, Boolean> filter) {
        FILTERS.put(filterType, filter);
    }

    /**
     * @return the filter for this descriptor
     */
    protected abstract Function<Long, Boolean> getFilter();

    /**
     * @return the value that this descriptor returns when the filter blocks the underlying value
     */
    protected String getFilterFailure() {
        return null;
    }

    @Override
    public <X> String wrap(X stringLike, WrapperOptions options) {
        /* Get the filter to apply ... */
        final Function<Long, Boolean> filter = getFilter();
        if (filter == null) {
            return getFilterFailure();
        }
        /* ... and the value to which to apply it. */
        final String string = super.wrap(stringLike, options);
        if (string == null) {
            return null;
        }
        /* Split the object owner ID off the transformed value. (See @ColumnTransformer annotations.) */
        final int separatorAt;
        final long userId;
        try {
            separatorAt = string.indexOf(';');  // Don't use a colon: bug HHH-9371 fixed in Hibernate 5.0.7.
            userId = Long.parseLong(string.substring(0, separatorAt));
        } catch (Throwable t) {
            throw new HibernateException("column transformer failed for secure string", t);
        }
        /* Apply filter. */
        return filter.apply(userId) ? string.substring(separatorAt + 1) : getFilterFailure();
    }
}
