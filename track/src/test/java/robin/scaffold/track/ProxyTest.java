package robin.scaffold.track;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {
    @Test
    public void test() {
        handlerOrder(new MaxOrderHandler(),"012345678999");
    }

    public void handlerOrder(OrderHandler orderHandler,String orderId){
        //创建处理器对象
        OrderHandlerProxy proxy=new OrderHandlerProxy();
        //为传入的实现了OrderHandler接口的对象创建代理类并实例化对象
        OrderHandler handler = (OrderHandler) proxy.bind(orderHandler);

        handler.handler(orderId);
        System.out.println(handler.toString());
    }

    public interface OrderHandler {
        void handler(String orderId);
    }

    public class NormalOrderHandler implements OrderHandler {
        @Override
        public void handler(String orderId) {
            System.out.println("NormalOrderHandler.handler():orderId:"+orderId);
        }
    }

    public class MaxOrderHandler implements OrderHandler {
        @Override
        public void handler(String orderId) {
            System.out.println("MaxOrderHandler.handler():orderId:"+orderId);
        }
    }

    //创建一个处理器类实现InvocationHandler接口并实现invoke方法
    public class OrderHandlerProxy implements InvocationHandler {

        //委托类 在这里就相当于实现了OrderHandler的类的对象
        Object target;

        public Object bind(Object target){
            this.target=target;

            //重点之一，通过Proxy的静态方法创建代理类 第一个参数为委托类的类加载器，
            //第二个参数为委托类实现的接口集，第三个参数则是处理器类本身
            return Proxy.newProxyInstance(this.target.getClass().getClassLoader(),
                    this.target.getClass().getInterfaces(),this);

        }

        //重点之二
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            //判断执行的方法是否是我们需要代理的handler方法
            if (method.getName().equalsIgnoreCase("handler")){
                System.out.println("OrderHandlerProxy.invoke.before");
                String orderId= (String) args[0];

                //对orderId的长度做限制
                if (orderId.length()>=10){
                    orderId=orderId.substring(0,10);
                }

                //重点之三，这个地方通过反射调用委托类的方法
                Object invoke = method.invoke(target, orderId);

                System.out.println("OrderHandlerProxy.invoke.after");
                return invoke;
            }else {
                //当前执行的方法不是我们需要代理的方法时不做操作直接执行委托的相应方法
                System.out.println("Method.name:"+method.getName());
                return method.invoke(target,args);
            }
        }
    }
}
