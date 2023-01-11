package kr.makeappsgreat.onlinemall;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.vm.VM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() { }

    @Test
    @Disabled
    void foo() throws SQLException {
        System.out.println("ApplicationContext's address : " + VM.current().addressOf(applicationContext));
        System.out.println("========================================");

        Arrays.stream(applicationContext.getBeanDefinitionNames()).sorted()
                .forEach(name -> {
                    String clazz = applicationContext.getBean(name).getClass().toString();

                    if (clazz.endsWith(name)) System.out.println(">> " + name);
                    else System.out.println(String.format(">> %s (%s)", name, clazz));
                });
        System.out.println("========================================");

        DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
        System.out.println("* Datasource");
        System.out.println("URL : " + metaData.getURL());
        System.out.println("========================================");
    }
}
