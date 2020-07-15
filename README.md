<p align="center">
    <img src="branding/evolve_logo_512.png" alt="Evolve Logo">
</p>

Evolve is a patch-based, event-oriented modding framework for [Equilinox](https://equilinox.com/) by [ThinMatrix](https://twitter.com/ThinMatrix/). In order to avoid republishing and freely distributing the decompiled source code of Equilinox - a game [purchaseable on Steam](https://store.steampowered.com/app/853550/) - this framework works exclusively based on patch files which may be later applied to a locally owned copy of the game. Therefore, if you do not own a legal copy of Equilinox, you will not be able to use or contribute to Evolve.

# Contributing to Evolve
This project is open source and welcomes contributions from developers interested in writing more API for Evolve mod developers. When contributing to this project, it is advised that you create a new branch for each pull request you wish to create.

## Prerequisites
- A legally purchased copy of Equilinox
- Java 8 or above ([AdoptOpenJDK](adoptopenjdk.net/) is recommended)
- [Git for Windows](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
  * For Windows, this should include Git Bash. MacOS and Linux both have their own terminals which serve a similar purpose
- [Apache Maven](http://maven.apache.org/download.cgi)

## Setting Up the Workspace
1. Locate your installation of Equilinox (on Windows, by default, this is `C:\Program Files (x86)\Steam\steamapps\common\Equilinox\`). It should look similar to the following:

![Equilinox Install Directory](https://i.imgur.com/dzQolLM.png)

You may find your installation directory directly through Steam using the following option:

![Steam](https://i.imgur.com/s2Amope.png)

2. Create a new directory titled `Evolve` and clone this repository using the Git client of your choice or by running `git clone https://github.com/2008Choco/Evolve.git` in the aforementioned directory. It should look similar to the following:

![Evolve Directory](https://i.imgur.com/MjzI6Gk.png)

3. Run the `setupWorkspace.sh` script and let the shell script complete before continuing. This should take no more than 5 minutes after which point a new `decompile` directory will have been created. It will look like so:

![Installed Evolve Directory](https://i.imgur.com/A1tvc9F.png)

4. Import the Evolve directory into your IDE of choice as a Maven project. **NOTE:** This project was written with Eclipse in mind, no other IDEs have been tested therefore it is requested that IntelliJ or NetBeans users contribute to update these steps as necessary

![Importing in Eclipse](https://i.imgur.com/U7laokK.png)
![Project Structure](https://i.imgur.com/Id95wf1.png)

Congratulations, you have successfully setup the Evolve workspace and are free to edit as you please. For instructions on how to edit Equilinox code and generate patches, see [The Patch System](#The-Patch-System) below.

# Setting Up Run Configurations
In order to run a development environment for Equilinox directly from your IDE, a run configuration must be created for the project. At a later time, this run configuration will be automatically generated. This information applies only to Eclipse IDE though other IDEs almost certain have similar setup.

Access your run configurations and create one for the Evolve project. Assign the Main class to `wtf.choco.evolve.userdev.LocalLauncher`

![Basic Run Configuration](https://i.imgur.com/lrRA3gZ.png)

Open the `Environment` tab and add a new environment variable titled `EQUILINOX_DECOMP_DIRECTORY`. The value of this variable should be the absolute path to the clean source of Equilinox (this directory should contain the LWJGL natives for Equilinox as well).

![Environment Variables](https://i.imgur.com/LFMXhY6.png)

Apply, save and you're ready to run Equilinox in the modded Evolve environment.

# The Patch System
Equilinox is a paid game and code copyright is held by ThinMatrix, therefore it cannot be distributed or hosted on a public repository such as Evolve. As such, Equilinox makes use of a patch-based system to list the bare minimum, a set of changes on which patches may be applied to the decompiled vanilla source.

**DISCLAIMER:** While you may own the game and have access to its decompiled code on your local system, it is asked that you **DO NOT** create any pull requests or contributions containing decompiled source from Equilinox. At no point should any copywritten code or binaries be distributed through GitHub or any other means.

## Generating Patches (Modifying Equilinox Code)
There are two possibilities for a change in Equilinox's decompiled source:
1. The file has already been patched and applyPatches has generated the modified source already
2. The file has not yet been patched and is unavailable in the project

In either case, please follow the [Patching Guidelines](#Patching-Guidelines) as stated below.

In the first case, you are welcome to modify the source file as you please and skip to [The Script](#The-Script). Alternatively, if the file has not yet been created, you must copy the file you wish to change from the `decompile/` directory into the appropriate package under `src/main/java` of your project directory. After it is present in your IDE, continue with making necessary changes. If any decompilation errors arise (ones that would cause Maven to fail), please resolve these issues and leave an Evolve comment, `// Evolve - decompile error`.

### The Script
Once you are satisfied with the changes you have made (and have ensured that the [Patching Guidelines](#Patching-Guidelines) are met), you may proceed with generating the patch(es) for the file(s) you have modified. Evolve provides you with a bash script in the root directory titled `createPatches.sh`. Double clicking this script will not suffice as this script requires a parameter pointing to the path of the clean decompiled source (the source that has not been modified).

![Create Patches Command](https://i.imgur.com/2kNT5y3.png)

`sh createPatches.sh "./decompile"`  
The argument may take either an absolute or relative path... so long as the path is that of the clean source.

After the script has terminated, you should see changes in the patch files corresponding to the source files to which you've made changes. These may be staged, committed and pushed to your fork in preparation for a pull request.

### Patching Guidelines
- Maintain a minimal diff where possible. Avoid large changes
- Single line changes should be suffixed with a `// Evolve` comment
  - If the change is not obvious, leave a note. i.e. `// Evolve - added an argument to foo()`
- Multi-line changes should be wrapped in comments such as the following:
```java
// Evolve start
this.buttons.add(this.addLine(7, 13, 2, "Mods", event -> {
    if (event.isLeftClick()) {
        this.gameMenu.setNewSecondaryScreen(new wtf.choco.evolve.gui.mods.ModPanelGui(gameMenu));
    }
}));
this.buttons.add(this.addLine(8, 15, 2, GameText.getText(6), new ClickListener() {
// Evolve end
```
- In-line imports if possible. If readability becomes an issue, you may add imports and surround them in multi-line comments
- For the sake of minimal diff, avoid adding methods to Equilinox source and opt instead for an access level change
- Access level changes should be marked with an ACCESS comment. For instance, `// ACCESS private -> public`
- **DO NOT RENAME ANYTHING!** Equilinox is not obfuscated and therefore does not require any mappings. Changing a name may cause unnecessary conflicts

## Applying Patches
Patch application is a way to ensure that your local source is up to date with the patches pulled from upstream. When pulling patches from upstream, `applyPatches` should be run. Much like with `createPatches`, the clean source must be provided as a script argument.

**WARNING!!** This script will delete any local changes to Equilinox source you may have written. Backup any changes you may have made before running this script.

# Installing Evolve
While Evolve remains in its early stages, installation is still technically possible. However, due to its instability and technical requirements, it is advised otherwise until a later date. Players, stay tuned for an installer!
