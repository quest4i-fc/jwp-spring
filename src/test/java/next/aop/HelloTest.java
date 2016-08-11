package next.aop;

import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

public class HelloTest {

    @Test
    public void hello() throws Exception {
        Hello hello = new HelloTarget();
        HelloUppercase proxy = new HelloUppercase(hello);
        System.out.println(proxy.sayHello("jae sung"));
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());
        Hello proxiedHello = (Hello) pfBean.getObject();
        System.out.println(proxiedHello.sayHello("Toby"));
//        assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
//        assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
//        assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
    }
}