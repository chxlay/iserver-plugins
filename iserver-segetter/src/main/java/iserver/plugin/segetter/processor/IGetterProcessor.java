package iserver.plugin.segetter.processor;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.tree.JCTree;
import iserver.plugin.segetter.annotation.IGetter;
import iserver.plugin.segetter.processor.generator.GetterFluentGenerator;
import iserver.plugin.segetter.processor.generator.GetterGenerator;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Getter 注解处理
 *
 * @author Alay
 * @date 2022-04-24 22:58
 */
@AutoService(Processor.class)
public class IGetterProcessor extends IAbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(IGetter.class.getCanonicalName());
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        // 获取注解表示的类集合
        Set<? extends Element> builderAnnotations = roundEnv.getElementsAnnotatedWith(IGetter.class);
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
            IGetter iGetter = element.getAnnotation(IGetter.class);
            // 添加访问事件 Getter 函数
            if (iGetter.getter()) {
                tree.accept(GetterGenerator.build(messager, treeMaker, names));
            }
            if (iGetter.fluent()) {
                // 生成流畅的 Getter函
                tree.accept(GetterFluentGenerator.build(messager, treeMaker, names, iGetter.prefix()));
            }
        }
    }

}
