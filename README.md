# TTYDBattleUnitTool
A tool to modify ttyd's unit stats

<br/>

## Updates

UPDATED v2.4.0 (2/14/26): Added tooltips when hovering over fields for further information on what each field does, added a ton of new fields that were previously missing, when opening the tool, it now remembers your last opened root and starts there, when saving files, the start directory is now the opened root folder, global.txt is now backed up as well when updating tattles, those who are wanting to use custom-made rel files can now add them to the UnitData.json file and have them pop up when the tool is searching a directory, fixed a bug where CannotTargetGrounded would not load correctly, tons of various backend code restructuring/changes.

UPDATED v2.3.1 (1/10/26): Fixed a bug with exporting tattles correctly.

UPDATED v2.3.0 (1/9/26): Added Fields for Stat Upgrades for Mario and Party, added a Update Tattles button - Clicking it will update all the tattles of the opened file's enemies using the format specified in TattleFormat.json and the text from Tattle Data.xlsx, moved Status Vulnerability Structs to the Defenses List to remove clutter, added icons to all the different structs for visual representation, fixed a bug where Yoshi Swallow Attributes weren't always saving properly, and fixed some typos in the input json.

UPDATED v2.2.0 (9/19/25): Added a few new fields in BattleUnitKind, added Enemy Icons to the Unit Selector, modified the File selector to show Location Names when selecting a file, fixed a bug where the Action Command Difficulty Level Selector would grow larger than intended, and fixed a bug where Target Weighting flags in BattleWeapon weren't preloading correctly.

UPDATED v2.1.0 (8/2/25): Added a popup on startup if a new update is available, modified some buttons on the GUI, added Action Command Difficulty to BattleWeaponm, exporting a file will now save a backup of the old file in the backups folder before saving a new file, and presets are here! Save files and structs as .bup files and load them in for quicker edits.

UPDATED v2.0.2 (7/15/25): Added a few more weapons that were previously missing.

UPDATED v2.0.1 (7/12/25): Fixed a bug where Single Target Flags in BattleWeapon weren't loading correctly.

UPDATED v2.0.0 (7/10/25): Implemented Support for BattleUnitKindPart, added a ton of icons to various areas for ease of searching, moved the Save Changes Button to the left for easier access, combined similar struct types to save space on GUI, renamed struct lists to be easier to understand, all 0's are now red in text fields to help distinguish from filled-in values, set the default export name to the currently opened file, set the default export file extensions to match the file opened, made some minor UI sizing tweaks, fixed a bug where some unused or rarely used flags in BattleWeapon wouldn't save properly, and fixed a few bugs with unitData.json

UPDATED v1.2.1 (7/8/25): Fixed a bug where some structs wouldn't save properly when hitting the save changes button.

UPDATED v1.2.0 (7/6/25): Added support for BattleWeapon, changed the open method to be a root folder system instead of a single file, added a file selector after opening a root folder, and changing the opened file now closes unit edit field until a struct is selected.

UPDATED v1.1.1 (6/30/25): Changed BattleUnitDefenseAttr to display selectable options instead of a text field, added a new popup window to display if file export is successful, and fixed a bug where data fields would stay up when swapping between units.

UPDATED v1.1.0 (6/30/25): Added support for StatusVulnerability structs, added a scroll bar to the struct fields editor, and removed unnecessary files to reduce file size.

UPDATED v1.0.0 (6/27/25): Initial Release Version

<br/>

## Additional Mentions

Huge thanks to **Jdaster64** for [documentation](https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt) on TTYD's unit structures and their [sheet](https://docs.google.com/spreadsheets/d/15hTm80MaefXxEuWorJOSBD3e6lvw2CCAQTtKUVRhbf4/edit?gid=0#gid=0) on the Switch remake's registry values which helped obtain some of the flag names

Huge thanks to **Silver** for their work on writing the tooltips and research on [various flag and value scenarios](https://www.youtube.com/@SilverGames136/videos)

<br/>

## Latest Usage

**Requirements: A recent installation of Java**

1: Download the latest release, it should contain a zip file.

2: Unzip the file, and in the root of the folder should be TTYDBattleUnitTool.exe. Run this executable.

3: With the program now open, you have access to the toolbar.
  - Open: Allows you to open a root folder, and select a valid file with unit data
  - Export: After making changes to unit stats, will allow you to save a new file with the changes made
  - Close: Closes the current file you're viewing (Not the application)
  - About: Information about the tool such as its version

4: After exporting a file with the changes made, replace the dol/rel you modified with the new file in your game's filesystem.

<br/>

## Planned Future Features/Additions
  - Implement enemy formations?
  - Implement Random Option
  - More documentation on Unknown Flags
  - Combine Preset Buttons into one
  - Add options button/menu
