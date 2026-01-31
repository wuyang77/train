package org.wuyang.generator.server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.wuyang.generator.util.FreemarkerUtil;

import java.io.File;
import java.util.HashMap;

public class ServerGenerator {
    static String toPath = "generator/src/main/java/org/wuyang/generator/test/";
    static String pomPath = "generator/pom.xml";
    static String module = "";
    static String servicePath = "[module]/src/main/java/org/wuyang/[module]/service/";

    public static void main(String[] args) throws Exception {

        // 获取mybatis-genertor
        String generatorPath = getGeneratorPath();
        // 比如generator-config-member.xml，得到module = member
        module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: " + module);
        servicePath = servicePath.replace("[module]", module);
        System.out.println("servicePath: " + servicePath);
        new File(toPath).mkdirs();

        //读取table节点
        Document document = new SAXReader().read("generator/" + generatorPath);
        Node table = document.selectSingleNode("//table");
        System.out.println(table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText()+ "/" + domainObjectName.getText());

        // 示例：表名 passenger_test
        // Domain = PassengerTest
        String Domain = domainObjectName.getText();
        // domain = passengerTest
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // do_main = passenger-test
//        String do_main = tableName.getText().replaceAll("_", "-");

        // 组装参数到map
        HashMap<String, Object> param = new HashMap<>();
        param.put("module", module);
        param.put("Domain", Domain);
        param.put("domain", domain);
//        param.put("do_main", do_main);
        System.out.printf("组装参数： ", param);
        FreemarkerUtil.initConfig("service.ftl");
        FreemarkerUtil.generator(servicePath + Domain + "Service.java", param);
    }

    private static String getGeneratorPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        HashMap<String, String> map = new HashMap<>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        return node.getText();
    }
}
