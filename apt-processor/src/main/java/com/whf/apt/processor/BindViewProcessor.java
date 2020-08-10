package com.whf.apt.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.whf.apt.annotation.BindView;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


@AutoService(javax.annotation.processing.Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    //日志辅助类
    private Messager mMessager;
    private Elements mElements;
    private Types mTypes;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mElements = processingEnv.getElementUtils();
        mTypes = processingEnv.getTypeUtils();

        mMessager.printMessage(Diagnostic.Kind.NOTE, "BindViewProcessor init...");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "BindViewProcessor process...");

        //获得被该注解声明的所有元素
        Set<? extends Element> bindViewElements = roundEnvironment.getElementsAnnotatedWith(BindView.class);

        for (Element element : bindViewElements) {
            // 检查被注解为@BindView的元素是否是一个成员变量
            if (element.getKind() == ElementKind.FIELD) {
                //转换为实际类型
                VariableElement variableElement = (VariableElement) element;
                //此处是成员变量注解，getSimpleName为成员变量名
                mMessager.printMessage(Diagnostic.Kind.NOTE,
                        "BindViewProcessor VariableElement getSimpleName " + variableElement.getSimpleName());

                //获取该属性所存在的类
                TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
                mMessager.printMessage(Diagnostic.Kind.NOTE,
                        "BindViewProcessor TypeElement getSimpleName " + typeElement.getSimpleName());
                mMessager.printMessage(Diagnostic.Kind.NOTE,
                        "BindViewProcessor TypeElement getQualifiedName " + typeElement.getQualifiedName());

                //获取注解
                BindView bindAnnotation = variableElement.getAnnotation(BindView.class);


                //获取 com.whf.testapt.TestApt 类的Class
                ClassName testAptClass = ClassName.get("com.whf.testapt","TestApt");

                //获取 java.lang.Override 类的Class
                ClassName overrideClass = ClassName.get("java.lang","Override");

                //获取 android.app.Activity 类的class
                ClassName activityClass = ClassName.get("android.app","Activity");

                FieldSpec activity = FieldSpec.builder(activityClass, "mActivity")
                        .addModifiers(Modifier.PUBLIC)
                        .initializer("new $T()", activityClass)
                        .build();

                //生成getApt方法
                MethodSpec getApt = MethodSpec.methodBuilder("getApt")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addAnnotation(overrideClass)
                        .addStatement("String test = new String()")
                        .addStatement("return \"hello apt\" ")
                        .build();

                //生成Main方法
                MethodSpec main = MethodSpec.methodBuilder("main")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(void.class)
                        .addParameter(String[].class, "args")
                        .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                        .build();

                //生成HelloWord类
                TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addField(activity)
                        .addMethod(main)
                        .addMethod(getApt)
                        .superclass(testAptClass)
                        .build();

                //生成Java文件
                JavaFile javaFile = JavaFile.builder("com.whf.testapt", helloWorld)
                        .build();

                try {
                    mMessager.printMessage(Diagnostic.Kind.NOTE, "BindViewProcessor write " + javaFile.toString());
                    javaFile.writeTo(processingEnv.getFiler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "BindViewProcessor getSupportedAnnotationTypes...");
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(BindView.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "BindViewProcessor getSupportedSourceVersion...");
        return SourceVersion.latestSupported();
    }


}
