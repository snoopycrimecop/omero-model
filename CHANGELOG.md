5.5.0-m8 (May 2019)
-------------------

- Include Bio-Formats 6.1.0-m1.
- Add omero.pixeldata.memoizer.dir to set BioFormatsCache.
- Note current deprecations in model mapping files.
- Add @Deprecation annotations to ome.model.* classes.
- Replace http by https when suitable.

5.5.0-m7 (April 2019)
---------------------

- Reduce the number of declarations for Bio-Formats dependencies.
- Convert Unicode in properties file to non-literal.

5.5.0-m6 (April 2019)
---------------------

- Use Java's CascadeType.DETACH instead of Hibernate's EVICT.
- Partially migrate Properties file from the openmicroscopy repository.

5.5.0-m5 (April 2019)
---------------------

- Deprecate fields.
- Update Hibernate DTD URL to current recommendation.
- Add License file.
- Provide configurable indexer for text fields.
- Fix Enumeration generation.
- Improve SqlAction Exception handling.
- Run units test in Travis.
- Fix Javadoc warnings.

5.5.0-m4 (March 2019)
---------------------

- Use new Gradle build system.

5.5.0-m3 (February 2019)
------------------------

- Extract omero-model from the openmicroscopy repository.
