package wtf.choco.evolve.mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mod {

    public String id();

    // TODO: Externalize mod information to a file

    public String version();

    public String description() default "";

    public String author() default "None";

}
