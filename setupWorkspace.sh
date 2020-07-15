# NOTE: This script is SERIOUSLY dependent on Windows and has the version hardcoded
# This script should be revisted in the future to be platform and version-independent

echo "Decompiling with Fernflower..."
mkdir -p decompile/classes
java -jar fernflower.jar -dgs=1 -hdc=0 -rbr=0 -asc=1 -udv=0 "../EquilinoxWindows.jar" decompile/
echo "Done!"

echo "Extracing decompiled source..."
cd "decompile/"
jar xfv "EquilinoxWindows.jar"
echo "Done!"

echo "Extracting compiled source..."
cd classes
jar xfv "../../../EquilinoxWindows.jar"
cd ../../
echo "Done!"

sh applyPatches.sh "decompile/"
cd ../

echo "Installing Equilinox to local Maven repository"
mvn install:install-file -DgroupId="com.equilinox" -DartifactId="equilinox" -Dversion="1.7.0b" -Dpackaging=jar -Dfile="EquilinoxWindows.jar" -Dsources="Evolve/decompile/EquilinoxWindows.jar"
echo "Done"