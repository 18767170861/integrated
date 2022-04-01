package org.wmc.integrated.japidocs;


import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

public class DocumentGenerator {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        DocsConfig config = new DocsConfig();
        config.setProjectPath("C:\\Users\\EDZ\\IdeaProjects\\acprights"); // 项目根目录
        config.setProjectName("acprights"); // 项目名称
        config.setApiVersion("V1.0"); // 声明该API的版本
        config.setDocsPath("C:\\test1"); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.TRUE); // 配置自动生成
        Docs.buildHtmlDocs(config); // 执行生成文档

    }
}
