package iserver.plugin.segetter.processor.generator;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

/**
 * Builder 注解函数生成器
 *
 * @author Alay
 * @date 2022-04-21 23:58
 */
public class BuilderMethodGenerator extends AbstractMethodGenerator {
    private final TreeMaker treeMaker;
    private final Names names;
    private final Element element;
    private final String methodName;
    private final String[] properties;


    public BuilderMethodGenerator(Messager messager, TreeMaker treeMaker, Names names, Element element, String methodName, String[] properties) {
        super(messager);
        this.treeMaker = treeMaker;
        this.names = names;
        this.element = element;
        this.methodName = methodName;
        this.properties = properties;
    }

    public static BuilderMethodGenerator build(Messager messager, TreeMaker treeMaker, Names names, Element element, String methodName, String[] properties) {
        return new BuilderMethodGenerator(messager, treeMaker, names, element, methodName, properties);
    }

    @Override
    public JCTree.JCMethodDecl methodGenerator(JCTree.JCVariableDecl jcVariableDecl) {
        if (properties.length > 0) {
            // 生成链式调用属性函数
            return this.chainPropertyMethods(jcVariableDecl);
        }
        return null;
    }

    /**
     * 生成链式调用属性函数
     *
     * @param jcVariableDecl
     */
    private JCTree.JCMethodDecl chainPropertyMethods(JCTree.JCVariableDecl jcVariableDecl) {
        // 当前字段名
        Name name = jcVariableDecl.name;
        for (int i = 0; i < properties.length; i++) {
            String property = properties[i];
            if (!property.equals(name.toString())) {
                continue;
            }
            SetterChainGenerator.build(messager, treeMaker, names, element);
        }
        return null;
    }
}
