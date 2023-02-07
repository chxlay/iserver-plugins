package iserver.plugin.segetter.processor.generator;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;

/**
 * Getter 函数生成器
 *
 * @author Alay
 * @date 2022-04-20 22:44
 */
public class GetterGenerator extends AbstractMethodGenerator {
    private static final String BOOLEAN_PREFIX = "is";
    private String prefix = "get";
    private final TreeMaker treeMaker;
    private final Names names;


    public GetterGenerator(Messager messager, TreeMaker treeMaker, Names names) {
        super(messager);
        this.treeMaker = treeMaker;
        this.names = names;
    }

    public GetterGenerator(Messager messager, TreeMaker treeMaker, Names names, String prefix) {
        super(messager);
        this.treeMaker = treeMaker;
        this.names = names;
        this.prefix = prefix;
    }

    public static GetterGenerator build(Messager messager, TreeMaker treeMaker, Names names) {
        return new GetterGenerator(messager, treeMaker, names);
    }

    public static GetterGenerator build(Messager messager, TreeMaker treeMaker, Names names, String prefix) {
        return new GetterGenerator(messager, treeMaker, names, prefix);
    }

    /**
     * 生成生成方法
     *
     * @param jcVariableDecl
     * @return
     */
    @Override
    public JCTree.JCMethodDecl methodGenerator(JCTree.JCVariableDecl jcVariableDecl) {
        // 生成Getter 语句 this.xxx;
        JCTree.JCReturn jcStatement = treeMaker.Return(treeMaker.Select(
                treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName()
                )
        );
        // 语句列表
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<JCTree.JCStatement>().append(jcStatement);
        // 访问修饰符
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        // 返回值,当前对象
        JCTree.JCExpression returnType = jcVariableDecl.vartype;
        // 方法名首字母大写,并加上 get
        Name getterName;
        boolean startIs = jcVariableDecl.name.toString().startsWith(BOOLEAN_PREFIX);
        if (startIs) {
            getterName = super.methodName(names, jcVariableDecl.name, EMPTY);
        } else {
            getterName = super.methodName(names, jcVariableDecl.name, prefix);
        }
        // 生成方法体
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        // 泛型参数列表
        List<JCTree.JCTypeParameter> methodGenericParameters = List.nil();
        // 参数值列表
        List<JCTree.JCVariableDecl> parameters = List.nil();
        // 抛出异常列表
        List<JCTree.JCExpression> throwCauseList = List.nil();
        // ( 修饰符,函数名,返回值类型,泛型参数列表,参数值列表,抛出异常列表,初始值 )
        JCTree.JCMethodDecl methodDef = treeMaker.MethodDef(
                modifiers,
                getterName,
                returnType,
                methodGenericParameters,
                parameters,
                throwCauseList,
                body,
                null);
        return methodDef;
    }

}
