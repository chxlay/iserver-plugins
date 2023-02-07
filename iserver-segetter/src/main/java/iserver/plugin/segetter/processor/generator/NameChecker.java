package iserver.plugin.segetter.processor.generator;

import javax.annotation.processing.Messager;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner9;
import javax.tools.Diagnostic;
import java.util.EnumSet;

/**
 * 名称检查
 *
 * @author Alay
 * @date 2022-04-24 23:34
 */
public class NameChecker {

    private final Messager messager;
    private final NameCheckScanner nameCheckScanner = new NameCheckScanner();


    public NameChecker(Messager messager) {
        this.messager = messager;
    }

    public static NameChecker build(Messager messager) {
        return new NameChecker(messager);
    }

    /**
     * 对 Java 命名进行检测
     *
     * @param element
     */
    public void checkNames(Element element) {
        nameCheckScanner.scan(element);
    }

    /**
     * 名称检查实现类,将会以 Visitor 模式方位抽象语法树中的元素
     */
    private class NameCheckScanner extends ElementScanner9<Void, Void> {
        /**
         * 此函数用来检查 java 类
         *
         * @param element
         * @param aVoid
         * @return
         */
        @Override
        public Void visitType(TypeElement element, Void aVoid) {
            super.scan(element.getTypeParameters(), aVoid);
            this.checkCamelCase(element, true);
            super.visitType(element, aVoid);
            return null;
        }

        /**
         * 检查方法命名是否合法
         *
         * @param element
         * @param aVoid
         * @return
         */
        @Override
        public Void visitExecutable(ExecutableElement element, Void aVoid) {
            if (element.getKind() == ElementKind.METHOD) {
                Name name = element.getSimpleName();
                if (name.contentEquals(element.getEnclosingElement().getSimpleName())) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "一个普通函数" + name + "不应该与类名重复,避免与构造函数产生混淆", element);
                }
                this.checkCamelCase(element, false);
            }
            super.visitExecutable(element, aVoid);
            return null;
        }

        /**
         * 检查变量命名是否合法
         *
         * @param element
         * @param aVoid
         * @return
         */
        @Override
        public Void visitVariable(VariableElement element, Void aVoid) {
            // 如果这个 variable 是枚举常量,则按大写命名检查,否则按驼峰命名检查
            boolean isConstant = this.heuristicallyConstant(element);
            if (element.getKind() == ElementKind.ENUM_CONSTANT || element.getConstantValue() != null || isConstant) {
                this.checkAllCaps(element);
            } else {
                this.checkCamelCase(element, false);
            }
            return null;
        }

        /**
         * 判断是否式常量
         *
         * @param element
         * @return
         */
        private boolean heuristicallyConstant(VariableElement element) {
            if (element.getEnclosingElement().getKind() == ElementKind.INTERFACE) {
                // 市场量
                return true;
            } else if (element.getKind() == ElementKind.FIELD
                    && element.getModifiers().contains(EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL))) {
                // 是常量
                return true;
            }
            return false;
        }

        /**
         * 传入的 Element 是否符合驼式命名法,如果不符合则输出警告
         *
         * @param element
         * @param initialCaps
         */
        private void checkCamelCase(Element element, boolean initialCaps) {
            String name = element.getSimpleName().toString();
            boolean previousUpper = false;
            boolean conventional = true;
            int firstCodePoint = name.codePointAt(0);

            if (Character.isUpperCase(firstCodePoint)) {
                previousUpper = true;
                if (!initialCaps) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "名称: " + name + " 应该以小写字母开头", element);
                    return;
                }
            } else if (Character.isLowerCase(firstCodePoint)) {
                if (initialCaps) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "名称: " + name + " 应该以大写字母开头", element);
                    return;
                }
            } else {
                conventional = false;
            }

            if (conventional) {
                int cp = firstCodePoint;
                for (int i = Character.charCount(cp); i < name.length(); i += Character.charCount(cp)) {
                    cp = name.codePointAt(i);
                    if (Character.isUpperCase(cp)) {
                        if (previousUpper) {
                            conventional = false;
                            continue;
                        }
                        previousUpper = true;
                    } else {
                        previousUpper = false;
                    }
                }
            }
            if (!conventional) {
                messager.printMessage(Diagnostic.Kind.WARNING, "名称: " + name
                        + "应当符合驼式命名法(Camel Case Names)", element);
            }
        }

        /**
         * 大写命名检测,要求第一个字母必须是大写的英文字母,其余部分可以是下划线或者大写字母
         *
         * @param element
         */
        private void checkAllCaps(Element element) {
            String name = element.getSimpleName().toString();
            boolean conventional = true;
            int firstCodePoint = name.codePointAt(0);

            if (!Character.isUpperCase(firstCodePoint)) {
                conventional = false;
            } else {
                boolean previousUnderscore = false;
                int cp = firstCodePoint;
                for (int i = Character.charCount(cp); i < name.length(); i += Character.charCount(cp)) {
                    cp = name.codePointAt(i);
                    if (cp == (int) '_') {
                        if (previousUnderscore) {
                            conventional = false;
                            continue;
                        }
                        previousUnderscore = true;
                    } else {
                        previousUnderscore = false;
                        if (!Character.isUpperCase(cp) && !Character.isDigit(cp)) {
                            conventional = false;
                            break;
                        }
                    }
                }
            }
            if (!conventional) {
                messager.printMessage(Diagnostic.Kind.WARNING, "常量: " + name
                        + "应当全部以大写字母或下划线命名,并以字母开头", element);
            }
        }
    }

}
