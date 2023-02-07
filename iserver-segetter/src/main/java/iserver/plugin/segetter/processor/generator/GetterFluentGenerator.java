package iserver.plugin.segetter.processor.generator;

import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;

/**
 * Getter 函数链式调用生成器
 *
 * @author Alay
 * @date 2022-04-20 23:39
 */
public class GetterFluentGenerator extends GetterGenerator {

    public GetterFluentGenerator(Messager messager, TreeMaker treeMaker, Names names) {
        super(messager, treeMaker, names);
    }



}
