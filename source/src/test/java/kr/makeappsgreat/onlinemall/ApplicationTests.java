package kr.makeappsgreat.onlinemall;

import kr.makeappsgreat.onlinemall.model.NamedEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.openjdk.jol.vm.VM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ModelMapper deepModelMapper;

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

    @Test
    @Disabled
    void bar() {
        System.out.println("ModelMapper's address     : " + VM.current().addressOf(modelMapper));
        System.out.println("DeepModelMapper's address : " + VM.current().addressOf(deepModelMapper));

        Map<String, Object> map = new HashMap<>();
        Long id = 1000L;
        map.put("id", id);
        map.put("name", "이름");

        NamedEntity entity = modelMapper.map(map, NamedEntity.class);
        System.out.println(String.format("[%d] %s", entity.getId(), entity.getName()));


        id = 2000L;
        map.put("id", id);
        map.put("name", null);
        modelMapper.map(map, entity);
        System.out.println(String.format("[%d] %s", entity.getId(), entity.getName()));
    }
}
