package kr.makeappsgreat.onlinemall;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.vm.VM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        System.out.println("ApplicationContext's address : " + VM.current().addressOf(applicationContext));

        Arrays.stream(applicationContext.getBeanDefinitionNames()).sorted()
                .forEach(name -> {
                    String clazz = applicationContext.getBean(name).getClass().toString();

                    if (clazz.endsWith(name)) System.out.println(">> " + name);
                    else System.out.println(String.format(">> %s (%s)", name, clazz));
                });
    }
}
