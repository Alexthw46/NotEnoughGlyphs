# Not Enough Glyphs 1.21.1 - 4.6.4
* Fix propagate Self unintended behaviors when used on turrets
* Makes redirect direction a propagator type glyph so it doesn't break glyph ordering/categorization

# Not Enough Glyphs 1.21.1 - 4.6.2
* Hotfix for filter always blocking resolve on runes after API update
* Propagate Underfoot can now target the vehicle of the entity it's resolving on
* Updated Sauce to 0.0.50+

# Not Enough Glyphs 1.21.1 - 4.6.0

* Updated Sauce to 0.0.42 for Ars Nouveau 5.12+
* Migration of namespaces of book covers from ars nouveau namespace to not enough glyphs namespace, migration should be
  lossless but a backup is always advised.

# Not Enough Glyphs 1.21.1 - 4.5.1

* Adds config for stuffed-crush damage boost and foodXplosion
* Adds an entity blacklist for ride
* Moved the "Random" filter to Ars Controle namespace and fully replicated its functionalities

# Not Enough Glyphs 1.21.1 - 4.5.0

* Band-Aid to prevent world breaking from upgrading the spellbinder (which is unintended to be possible)
* Added Ride and Feed glyphs
* "Random" Filter

# Not Enough Glyphs 1.21.1 - 4.4.0

* Renamed Book Threads into Book Covers.
* Spellbinders now open the Glyph Unlock Screen instead of being placed on the Scribing Table on interaction.
* Fixed missing lang for empty spellbinder.
* Renamed Reverse Direction into Redirect Placement to better reflect the functionality.
* Redirect Placement is now more consistent with Rotate, supporting Sensitive and Amplify to allow for more precise
  control over the redirection.
