package iserver.plugin.segetter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Getter + Setter 生成器
 *
 * @author Alay
 * @date 2022-04-16 14:49
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface ISeGetter {

    /**
     * 添加链式调用
     *
     * @return
     */
    boolean chainSetter() default false;

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
