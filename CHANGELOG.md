5.5.5 (March 2020)
------------------

- Display unit test output instead of caching it
  ([#48](https://github.com/ome/omero-model/pull/48))
- Security vulnerability fixes for
  [2019-SV3](https://www.openmicroscopy.org/security/advisories/2019-SV3-user-privacy/) and
  [2019-SV5](https://www.openmicroscopy.org/security/advisories/2019-SV5-bypass-filters/)

5.5.4 (February 2020)
---------------------

- Add `omero.pixeldata.max_projection_bytes` to dependency stack ([#134](https://github.com/ome/omero-model/pull/47))

5.5.3 (December 2019)
---------------------

- Bump to Bio-Formats 6.3.1 ([#44](https://github.com/ome/omero-model/pull/44))
- Fix NPE issue with nulls in ordered collections ([#43](https://github.com/ome/omero-model/pull/43))

5.5.2 (July 2019)
-----------------

- Bump to Bio-Formats 6.1.1 ([#40](https://github.com/ome/omero-model/pull/40))
- Use offline Hibernate DTD ([#41](https://github.com/ome/omero-model/pull/41))

5.5.1 (June 2019)
-----------------

- Adjust combined_fields annotations for search.

5.5.0 (May 2019)
----------------

- Include Bio-Formats 6.1.0.
- Add omero.pixeldata.memoizer.dir to set BioFormatsCache.
- Note current deprecations in model mapping files.
- Add @Deprecation annotations to ome.model.* classes.
- Replace http by https when suitable.
- Reduce the number of declarations for Bio-Formats dependencies.
- Convert Unicode in properties file to non-literal.
- Use Java's CascadeType.DETACH instead of Hibernate's EVICT.
- Partially migrate Properties file from the openmicroscopy repository.
- Deprecate fields.
- Update Hibernate DTD URL to current recommendation.
- Add License file.
- Provide configurable indexer for text fields.
- Fix Enumeration generation.
- Improve SqlAction Exception handling.
- Run units test in Travis.
- Fix Javadoc warnings.
- Use new Gradle build system.
- Extract omero-model from the openmicroscopy repository.
