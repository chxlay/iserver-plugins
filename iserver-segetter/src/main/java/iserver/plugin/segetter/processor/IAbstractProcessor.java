package iserver.plugin.segetter.processor;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 抽取公共的
 *
 * @author Alay
 * @date 2022-04-16 15:01
 */
//@SupportedOptions(value = {"parameter"})
//@SupportedSourceVersion(value = SourceVersion.RELEASE_11)
//@SupportedAnnotationTypes(value = {"com.iserver.common.plugin.annotation.注解名"})
public abstract class IAbstractProcessor extends AbstractProcessor {
    /**
     * 元素操作工具类
     */
    protected Elements elements;
    /**
     * 类信息工具类
     */
    protected Types types;

    protected Locale locale;
    /**
     * 打印日志
     */
    protected Messager messager;
    /**
     * 写 Java代码
     */
    protected Filer filer;
    /**
     * 编译时期参数
     */
    protected Map<String, String> options;

    /**
     * 环境上下文
     */
    protected Context context;
    /**
     * 编译器语法树
     */
    protected JavacTrees javacTrees;
    /**
     * 树编译器
     */
    protected TreeMaker treeMaker;
    protected Names names;
    protected Symtab symtab;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.elements = processingEnv.getElementUtils();
        this.types = processingEnv.getTypeUtils();
        this.locale = processingEnv.getLocale();
        this.options = processingEnv.getOptions();
        this.context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.javacTrees = JavacTrees.instance(processingEnv);
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        this.symtab = Symtab.instance(context);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_11;
    }

    /**
     * 写入 method
     *
     * @param annotations
     * @param roundEnv
     */
    public abstract void doSomething(Set<? extends Element> annotations, RoundEnvironment roundEnv);

}
