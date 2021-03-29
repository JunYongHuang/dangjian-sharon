package cc.ryanc.halo;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * <pre>
 *     Halo run!
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
@Slf4j
@SpringBootApplication
@EnableCaching
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

//    //下面是2.0的配置，1.x请搜索对应的设置
//    @Bean
//    public ServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//        tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
//        return tomcat;
//    }
//
//    private Connector createHTTPConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        //同时启用http（8080）、https（8443）两个端口
//        connector.setScheme("http");
//        connector.setSecure(false);
//        connector.setPort(8888);
////        connector.setRedirectPort(9090);
//        return connector;
//    }


}
