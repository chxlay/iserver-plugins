package iserver.plugin.segetter.processor.generator;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;

/**
 * 函数生成器抽象类
 *
 * @author Alay
 * @date 2022-04-20 22:45
 */
public abstract class AbstractMethodGenerator extends TreeTranslator {
    protected static final String EMPTY = "";
    protected static final String METHOD_PREFIX = "set";
    protected static final String BOOL_PREFIX = "is";

    public AbstractMethodGenerator(Messager messager) {
        this.messager = messager;
    }

    /**
     * 打印日志
     */
    protected final Messager messager;

    /**
     * 生成 class 文件的方法
     *
     * @param jcClassDecl
     */
    @Override
    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
        jcClassDecl.defs.stream()
                // 开始过滤,只对变量进行处理
                .filter(item -> item.getKind().equals(Tree.Kind.VARIABLE))
                // 强制类型转换
                .map(item -> (JCTree.JCVariableDecl) item)
                // 处理生成 Getter Setter 的方法
                .forEach(item -> {
                    jcClassDecl.defs = jcClassDecl.defs.append(this.methodGenerator(item));
                });
        super.visitClassDef(jcClassDecl);
    }

    /**
     * 函数生成器
     *
     * @param jcVariableDecl
     * @return
     */
    public abstract JCTree.JCMethodDecl methodGenerator(JCTree.JCVariableDecl jcVariableDecl);

    /**
     * 字段名称转换方法名称 getXxx() setXxx()
     *
     * @param names
     * @param name
     * @param prefix 方法前缀
     * @return
     */
    public Name methodName(Names names, Name name, String prefix) {
        String filedName = name.toString();
        StringBuilder methodNameSb = new StringBuilder();
        if (!EMPTY.equals(prefix)) {
            methodNameSb.append(prefix);
            // 函数名有前缀,字段名首字母大写
            filedName = filedName.substring(0, 1).toUpperCase() + filedName.substring(1, name.length());
        }
        methodNameSb.append(filedName);
        return names.fromString(methodNameSb.toString());
    }

}
