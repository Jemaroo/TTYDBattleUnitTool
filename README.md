# TTYDBattleUnitTool
A tool to modify ttyd's unit stats

<br/>

## Updates

UPDATED v 1.2.0 (7/6/25): Added support for BattleWeapon, changed the open method to be a root folder system instead of a single file, added a file selector after opening a root folder, and changing the opened file now closes unit edit field until a struct is selected

UPDATED v1.1.1 (6/30/25): Changed BattleUnitDefenseAttr to display selectable options instead of a text field, added a new popup window to display if file export is successful, and fixed a bug where data fields would stay up when swapping between units.

UPDATED v1.1.0 (6/30/25): Added support for StatusVulnerability structs, added a scroll bar to the struct fields editor, and removed unnecessary files to reduce file size.

UPDATED v1.0.0 (6/27/25): Initial Release Version

<br/>

## Additional Mentions

Huge thanks to **Jdaster64** for [documentation](https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt) on TTYD's unit structures

<br/>

## Latest Usage

**Requirements: A recent installation of Java**

1: Download the latest release, it should contain a zip file.

2: Unzip the file, and in the root of the folder should be TTYDBattleUnitTool.exe. Run this executable.

3: With the program now open, you have access to the toolbar.
  - Open: Allows you to open main.dol/start.dol/or a valid .rel file with enemy data
  - Export: After making changes to enemy stats, will allow you to save a new file with the changes made
  - Close: Closes the current file you're viewing (Not the application)
  - About: Information about the tool such as its version

4: After exporting a file with the changes made, replace the dol/rel you modified with the new file in your game's filesystem.

<br/>

## Planned Future Features/Additions
  - Support for BattleUnitKindPart
  - Making the Save Changes Button more accessible
