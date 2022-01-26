package za.co.mahlaza.research.grammarengine.base.models.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.METHOD;

@Retention(RetentionPolicy.RUNTIME)
@Target(value=METHOD)
@Inherited
public @interface RelationSetter {
    String RdfName() default "";
    String RangeName() default "";
}
