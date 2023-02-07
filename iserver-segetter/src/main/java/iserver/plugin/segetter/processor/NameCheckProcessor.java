package iserver.plugin.segetter.processor;

import com.google.auto.service.AutoService;
import iserver.plugin.segetter.processor.generator.NameChecker;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * 命名规范检查
 *
 * @author Alay
 * @date 2022-04-24 23:30
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class NameCheckProcessor extends AbstractProcessor {

    private NameChecker nameChecker;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            for (TypeElement element : annotations) {
                nameChecker.checkNames(element);
            }
        }
        return false;
    }
}
