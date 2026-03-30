# Release

> This version doesn't have every recipe findable in the Recipe Book before crafting them. This will be added in the
> future. In the meantime I suggest using a recipe search mod such as [REI](https://modrinth.com/mod/rei).

> I didn't have enough time to add everything I wanted to add for release, however this version is in a
> survival-playable state, and the main gameplay is complete.

**Warning: The changelog will contain spoilers for the mod.**

## Features

- Added two new dimensions.
	- **Somnium Reale**: A new world where things aren't quite as you remember, but in
	  some ways feel more real than ever.
	- **The Terrorlands**: An eerie place where nightmares become real.
- Added Relics and Relic Bundles
	- Having a relic inside an active relic bundle in your inventory, will grant you select effects.
- Added two supported wood sets
	- These new blocks break when they aren't supported.
- Added Extractor
	- Burns relics into dust.
- Added Fleecifer Boss Fight
	- You will need to prepare for this...

## Technical Features

- Added two new sizes of stars, and updated the star shader for the new dimensions.
- Updated the terrain shader for the new dimensions to adjust lighting/shadows.
- Updated cloud shader for new dimensions.
- Replaced menu blur shader with a gaussian blur.
- Added a data-driven Relic system
	- There is currently only support for status effects, however it's quite expandable by other mods.
	- I haven't written any documentation, as *it is an april fools mod*.
		- If anyone wants to write some, feel free to submit a pull request
		  on [github](https://github.com/legotaylor/dtaf2026).
- Updated the title screen to include somnium reale's update logo, and moved the position of the splash text.
- Added a new recipe type `dtaf2026:extracting`, for use in the Extractor block.
- Updated Creaking Hearts and Creakings to have variants.
- Added data-driven Junglefowl and Boar variants.
- Added Zoom (from [Perspective](https://modrinth.com/mod/mclegoman-perspective), but in a limited capacity as
  Perspective hasn't been updated yet)
- Added Hold Perspective (from [Perspective](https://modrinth.com/mod/mclegoman-perspective), as Perspective hasn't been
  updated yet)
- Added some iris shader compatibility, though I haven't personally tested this - some vanilla shaders break (mainly
  celestial textures) in the new dimensions (not all though, and none show our stars).
