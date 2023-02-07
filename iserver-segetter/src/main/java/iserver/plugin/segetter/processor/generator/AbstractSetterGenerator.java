package iserver.plugin.segetter.processor.generator;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;

/**
 * Getter 函数
 *
 * @author Alay
 * @date 2022-04-24 21:27
 */
public abstract class AbstractSetterGenerator extends AbstractMethodGenerator {
    protected final TreeMaker treeMaker;
    protected final Names names;

    public AbstractSetterGenerator(Messager messager, TreeMaker treeMaker, Names names) {
        super(messager);
        this.treeMaker = treeMaker;
        this.names = names;
    }

    @Override
    public JCTree.JCMethodDecl methodGenerator(JCTree.JCVariableDecl jcVariableDecl) {
        // 赋值语句
        JCTree.JCExpressionStatement statement = treeMaker.Exec(
                treeMaker.Assign(
                        treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.name),
                        treeMaker.Ident(jcVariableDecl.name)
                )
        );
        // 语句列表
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<JCTree.JCStatement>().append(statement);
        this.extraStatement(jcVariableDecl, statements);

        // setter 方法参数,方法形参(修饰符，形参列表,变量名,变量类型)
        JCTree.JCVariableDecl parameter = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER, List.nil()),
                jcVariableDecl.name, jcVariableDecl.vartype, null);

        // 防范访问修饰符
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        // 返回值类型
        JCTree.JCExpression returnType = treeMaker.Type(new Type.JCVoidType());

        // 函数体
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        // 泛型参数列表
        List<JCTree.JCTypeParameter> methodGenericParameters = List.nil();
        // 参数值列表
        List<JCTree.JCVariableDecl> parameters = List.of(parameter);
        // 抛出异常列表
        List<JCTree.JCExpression> throwCauseList = List.nil();
        // 构建最终的函数体
        JCTree.JCMethodDecl methodDef = treeMaker.MethodDef(
                modifiers,
                this.methodName(jcVariableDecl),
                returnType,
                methodGenericParameters,
                parameters,
                throwCauseList,
                body,
                null
        );
        return methodDef;
    }

    /**
     * 函数名
     *
     * @param jcVariableDecl
     * @return
     */
    protected Name methodName(JCTree.JCVariableDecl jcVariableDecl) {
        return super.methodName(names, jcVariableDecl.name, EMPTY);
    }

    /**
     * 返回值类型
     *
     * @return
     */
    protected JCTree.JCExpression returnType() {
        return treeMaker.Type(new Type.JCVoidType());
    }

    protected void extraStatement(JCTree.JCVariableDecl jcVariableDecl, ListBuffer<JCTree.JCStatement> statements) {
    }
}
