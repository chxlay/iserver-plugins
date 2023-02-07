package iserver.plugin.segetter.processor;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.tree.JCTree;
import iserver.plugin.segetter.annotation.ISeGetter;
import iserver.plugin.segetter.processor.generator.GetterFluentGenerator;
import iserver.plugin.segetter.processor.generator.GetterGenerator;
import iserver.plugin.segetter.processor.generator.SetterChainGenerator;
import iserver.plugin.segetter.processor.generator.SetterGenerator;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Getter / Setter 注解处理
 *
 * @author Alay
 * @date 2022-04-16 15:00
 */
@AutoService(Processor.class)
public class ISeGetterProcessor extends IAbstractProcessor {


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ISeGetter.class.getCanonicalName());
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        // 获取注解表示的类集合
        Set<? extends Element> builderAnnotations = roundEnv.getElementsAnnotatedWith(ISeGetter.class);
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
            // 获取语法树的根节点
            JCTree tree = javacTrees.getTree(element);
            // 添加访问事件 Getter 函数
            tree.accept(GetterGenerator.build(messager, treeMaker, names));
            // 添加访问事件 Setter 函数
            tree.accept(SetterGenerator.build(messager, treeMaker, names));
            // 注解控制扩展
            ISeGetter iSeGetter = element.getAnnotation(ISeGetter.class);
            if (iSeGetter.fluent()) {
                // 生成流畅的 Getter函数（无前缀的时候）
                String prefix = iSeGetter.prefix();
                tree.accept(GetterFluentGenerator.build(messager, treeMaker, names, prefix));
            }
            if (iSeGetter.chainSetter()) {
                tree.accept(SetterChainGenerator.build(messager, treeMaker, names, element));
            }
        }
    }
}
