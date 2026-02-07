package org.wuyang.generator.server;

import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.wuyang.generator.util.DbUtil;
import org.wuyang.generator.util.Field;
import org.wuyang.generator.util.FreemarkerUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ServerGenerator {
    static String pomPath = "generator/pom.xml";
    static String module = "";
    static String servicePath = "[module]/src/main/java/org/wuyang/[module]/";

    public static void main(String[] args) throws Exception {

        // 获取mybatis-genertor
        String generatorPath = getGeneratorPath();
        System.out.println("generatorPath = " + generatorPath);
        // 比如generator-config-member.xml，得到module = member
        module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: " + module);
        servicePath = servicePath.replace("[module]", module);
        System.out.println("servicePath: " + servicePath);


        //读取table节点
        Document document = new SAXReader().read("generator/" + generatorPath);
        Node table = document.selectSingleNode("//table");
        Node tableName = table.selectSingleNode("@tableName");
        System.out.println("表名 = " + tableName.getText());
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println("实体类名 = " + domainObjectName.getText());

        // 示例：表名 passenger_test
        // Domain = PassengerTest
        String Domain = domainObjectName.getText();
        // domain = passengerTest
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // do_main = passenger-test
        String do_main = tableName.getText().replaceAll("_", "-");

        // 为DbUtil配置数据源
        Node connectionURL = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        System.out.println("connectionURL = " + connectionURL.getText());
        System.out.printf("userId = ", userId);
        System.out.printf("password = ", password);
        DbUtil.url = connectionURL.getText();
        DbUtil.user = userId.getText();
        DbUtil.password = password.getText();

        // 获取中文名
        String tableNameCn = DbUtil.getTableComment(tableName.getText());
        // 获取所有列的字段信息
        List<Field> fieldList = DbUtil.getColumnByTableName(tableName.getText());

        // 组装参数到map
        HashMap<String, Object> param = new HashMap<>();
        param.put("module", module);
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        System.out.println("组装信息 " + param.entrySet());
        gen(Domain, param, "service");
        gen(Domain, param, "controller");
    }

    private static void gen(String Domain, HashMap<String, Object> param, String target) throws IOException, TemplateException {
        FreemarkerUtil.initConfig(target + ".ftl");
        String toPath = servicePath + target + "/";
        System.out.println("文件所在路径： " + toPath);
        new File(toPath).mkdirs();
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);
        String filePathName = toPath + Domain + Target + ".java";
        System.out.println("生成文件的完整路径名： " + filePathName);
        FreemarkerUtil.generator(filePathName, param);
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
