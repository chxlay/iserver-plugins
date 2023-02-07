package iserver.plugin.segetter.processor;


import com.google.auto.service.AutoService;
import com.sun.tools.javac.tree.JCTree;
import iserver.plugin.segetter.annotation.IBuilder;
import iserver.plugin.segetter.processor.generator.BuilderMethodGenerator;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * 创建函数
 * $T  代指 TypeName 类
 * $N  代指变量或方法名称
 * $S  代指字符串
 *
 * @author Alay
 * @date 2022-04-15 21:42
 * @see <a href="https://www.cnblogs.com/strongmore/p/13287726.html">参看文档</a>
 */
@AutoService(Processor.class)
public class IBuilderProcessor extends IAbstractProcessor {
    /**
     * 支持的注解类型
     * 和 @SupportedAnnotationTypes(value = {"com.iserver.common.plugin.annotation.注解名"})一样的用法
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(IBuilder.class.getCanonicalName());
    }


    /**
     * @param annotations
     * @param roundEnv
     * @return 返回 true 表示已经处理，其他 Processor 不在处理
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        // 获取注解表示的类集合
        Set<? extends Element> builderAnnotations = roundEnv.getElementsAnnotatedWith(IBuilder.class);
        if (builderAnnotations.isEmpty()) {
            return false;
        }
        // 编写类
        this.doSomething(builderAnnotations, roundEnv);
        return true;
    }


    @Override
    public void doSomething(Set<? extends Element> annotations, RoundEnvironment roundEnv) {
        for (Element element : annotations) {
            IBuilder iBuilder = element.getAnnotation(IBuilder.class);
            if (null == iBuilder) {
                return;
            }
            // 函数名
            String methodName = iBuilder.value();
            // 参数
            String[] args = iBuilder.properties();
            // 获取语法树的根节点
            JCTree tree = javacTrees.getTree(element);
            // 添加访问事件
            tree.accept(BuilderMethodGenerator.build(messager, treeMaker, names, element, methodName, args));
        }
    }
}
