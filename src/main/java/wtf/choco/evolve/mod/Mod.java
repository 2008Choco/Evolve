package wtf.choco.evolve.mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an Evolve mod. All Evolve mods should be annotated with the Mod annotation in
 * order for the Evolve mod loader to recognize the binary in the mods directory.
 * <p>
 * Only one class may be annotated with the Mod annotation in any given project, else an
 * error will be thrown by the mod loader.
 *
 * @author Parker Hawke
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mod {

    /**
     * The unique identification string for this mod.
     *
     * @return the mod id
     */
    public String value();

}
