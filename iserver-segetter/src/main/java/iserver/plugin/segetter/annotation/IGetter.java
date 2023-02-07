package iserver.plugin.segetter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Getter 函数生成器
 *
 * @author Alay
 * @date 2022-04-16 14:45
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface IGetter {

    /**
     * 是否需要Getter 函数
     *
     * @return
     */
    boolean getter() default true;

    /**
     * 是否生成流畅的Getter 函数
     *
     * @return
     */
    boolean fluent() default false;

    /**
     * get方法的前缀
     *
     * @return
     */
    String prefix() default "";
}
