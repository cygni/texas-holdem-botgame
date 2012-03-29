package se.cygni.texasholdem.profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

@Profile("production")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProductionProfile {

}
