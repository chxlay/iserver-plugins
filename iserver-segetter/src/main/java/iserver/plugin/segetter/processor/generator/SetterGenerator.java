package iserver.plugin.segetter.processor.generator;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;

/**
 * Setter 函数生成器
 *
 * @author Alay
 * @date 2022-04-20 22:44
 */
public class SetterGenerator extends AbstractSetterGenerator {

    public SetterGenerator(Messager messager, TreeMaker treeMaker, Names names) {
        super(messager, treeMaker, names);
    }

    public static SetterGenerator build(Messager messager, TreeMaker treeMaker, Names names) {
        return new SetterGenerator(messager, treeMaker, names);
    }

    /**
     * 生成setter 方法
     *
     * @param jcVariableDecl
     * @return
     */
    @Override
    public JCTree.JCMethodDecl methodGenerator(JCTree.JCVariableDecl jcVariableDecl) {
        Name methodName = super.methodName(names, jcVariableDecl.name, METHOD_PREFIX);
        return super.methodGenerator(jcVariableDecl);
    }


    @Override
    protected Name methodName(JCTree.JCVariableDecl jcVariableDecl) {
        return super.methodName(names, jcVariableDecl.name, METHOD_PREFIX);
    }
}
