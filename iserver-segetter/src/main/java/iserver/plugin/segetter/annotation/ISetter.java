package iserver.plugin.segetter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Setter 源码生成器
 *
 * @author Alay
 * @date 2022-04-16 14:46
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface ISetter {

    /**
     * 是否需要 Setter 函数
     *
     * @return
     */
    boolean setter() default true;

    /**
     * 添加链式调用
     *
     * @return
     */
    boolean chain() default false;

}
