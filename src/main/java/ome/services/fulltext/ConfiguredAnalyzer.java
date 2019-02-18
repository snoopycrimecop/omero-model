/*
 * Copyright (C) 2019 University of Dundee & Open Microscopy Environment.
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

package ome.services.fulltext;

import java.io.Reader;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A full-text search analyzer usable by the OMERO model. Wraps an underlying analyzer.
 * The wrapped analyzer class is configured according to server settings or otherwise.
 * @author m.t.b.carroll@dundee.ac.uk
 * @since 5.5.0
 */
public final class ConfiguredAnalyzer extends Analyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfiguredAnalyzer.class);

    private static AtomicReference<Class<? extends Analyzer>> configuredAnalyzerClass = new AtomicReference<>();

    private final Analyzer wrappedAnalyzer;

    /**
     * Construct an analyzer wrapping the same class as previously passed to {@link #ConfiguredAnalyzer(Class)}.
     */
    public ConfiguredAnalyzer() {
        final Class<? extends Analyzer> analyzerClass = configuredAnalyzerClass.get();
        if (analyzerClass == null) {
            throw new IllegalStateException("must configure with analyzer class before using default constructor");
        }
        wrappedAnalyzer = instantiateWrappedAnalyzer(analyzerClass);
    }

    /**
     * Construct an analyzer wrapping the given class.
     * @param analyzerClass a non-abstract class of Lucene analyzer, never {@code null}
     */
    public ConfiguredAnalyzer(Class<? extends Analyzer> analyzerClass) {
        if (analyzerClass == null) {
            throw new IllegalArgumentException("must give analyzer class or use default constructor");
        }
        wrappedAnalyzer = instantiateWrappedAnalyzer(analyzerClass);
        /* set only after verifying it can be instantiated */
        if (analyzerClass != configuredAnalyzerClass.getAndSet(analyzerClass)) {
            LOGGER.debug("set analyzer class to {}", analyzerClass);
        }
    }

    /**
     * Provide an instance of the analyzer to be wrapped.
     * @param analyzerClass a class of Lucene analyzer
     * @return an instance of that class
     */
    private Analyzer instantiateWrappedAnalyzer(Class<? extends Analyzer> analyzerClass) {
        try {
            return analyzerClass.newInstance();
        } catch (ReflectiveOperationException roe) {
            throw new IllegalArgumentException("cannot instantiate analyzer class " + analyzerClass);
        }
    }

    @Override
    public final TokenStream tokenStream(String fieldName, Reader reader) {
        return wrappedAnalyzer.tokenStream(fieldName, reader);
    }

    @Override
    public void close() {
        wrappedAnalyzer.close();
    }
}
