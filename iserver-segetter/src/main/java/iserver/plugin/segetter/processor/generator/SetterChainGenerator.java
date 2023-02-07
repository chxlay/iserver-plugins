package iserver.plugin.segetter.processor.generator;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Setter 函数链式调用生成器
 *
 * @author Alay
 * @date 2022-04-20 23:39
 */
public class SetterChainGenerator extends AbstractSetterGenerator {
    private final Element element;

    public SetterChainGenerator(Messager messager, TreeMaker treeMaker, Names names, Element element) {
        super(messager, treeMaker, names);
        this.element = element;
    }

    public static SetterChainGenerator build(Messager messager, TreeMaker treeMaker, Names names, Element element) {
        return new SetterChainGenerator(messager, treeMaker, names, element);
    }

    @Override
    public JCTree.JCMethodDecl methodGenerator(JCTree.JCVariableDecl jcVariableDecl) {
        return super.methodGenerator(jcVariableDecl);
    }


    @Override
    protected void extraStatement(JCTree.JCVariableDecl jcVariableDecl, ListBuffer<JCTree.JCStatement> statements) {
        messager.printMessage(Diagnostic.Kind.WARNING, element.getSimpleName().toString());
        // 构建 return this;
        JCTree.JCReturn returnStatement = treeMaker.Return(treeMaker.Select(treeMaker
                .Ident(names.fromString("this")), jcVariableDecl.getName()));
        statements.append(returnStatement);
    }

    @Override
    protected JCTree.JCExpression returnType() {
        return super.returnType();
    }
}
