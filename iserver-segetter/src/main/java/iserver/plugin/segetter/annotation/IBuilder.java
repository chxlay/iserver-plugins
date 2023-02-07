package iserver.plugin.segetter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义Builder 源码生成注解
 *
 * @author Alay
 * @date 2022-04-15 21:42
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface IBuilder {

    /**
     * 方法名
     *
     * @return
     */
    String value() default "build";

    /**
     * 参数属性字段
     *
     * @return
     */
    String[] properties() default {};
}
