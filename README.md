# TTYDBattleUnitTool
A tool to modify ttyd's unit stats

<br/>

## Updates

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
  - Support for BattleWeapon
  - Root system implementation instead of selecting a single file
