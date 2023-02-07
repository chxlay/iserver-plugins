package iserver.plugin.segetter.processor;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.tree.JCTree;
import iserver.plugin.segetter.annotation.ISetter;
import iserver.plugin.segetter.processor.generator.SetterChainGenerator;
import iserver.plugin.segetter.processor.generator.SetterGenerator;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Setter 注解处理
 *
 * @author Alay
 * @date 2022-04-24 22:58
 */
@AutoService(Processor.class)
public class ISetterProcessor extends IAbstractProcessor {

    /**
     * 支持的注解类型
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ISetter.class.getCanonicalName());
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        // 获取注解表示的类集合
        Set<? extends Element> builderAnnotations = roundEnv.getElementsAnnotatedWith(ISetter.class);
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
            // 注解控制扩展
            ISetter iSetter = element.getAnnotation(ISetter.class);
            if (iSetter.setter()) {
                // 添加访问事件 Setter 函数
                tree.accept(SetterGenerator.build(messager, treeMaker, names));
            }
            if (iSetter.chain()) {
                // 链式调用 Setter函
                tree.accept(SetterChainGenerator.build(messager, treeMaker, names, element));
            }
        }
    }

}
