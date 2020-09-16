package robin.scaffold.track;

import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class OnClickListenerProxy implements InvocationHandler {

    static final String TAG="OnClickListenerProxy";

    //这个其实就是我们相关的activity
    Object delegate;

    //这个是activity中我们标记了OnClick的方法，最终的操作就是把对OnClickListener中OnClick方法的调用替换成对这个event方法的调用
    Method event;


    public Object bind(Object delegate){
        this.delegate=delegate;
        //生成代理类，这个没什么好说的了
        return Proxy.newProxyInstance(this.delegate.getClass().getClassLoader(),
                new Class[]{View.OnClickListener.class},this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Log.e(TAG,"invoke");
        Log.e(TAG,"method.name:"+method.getName()+"  args:"+args);

        //判断调用的是onClick方法的话，替换成对我们event方法的调用。
        if ("onClick".equals(method.getName())){
            for (Object arg : args) {
                Log.e(TAG,"arg:"+arg);
            }
            View view= (View) args[0];
            return event.invoke(delegate,view);
        }
        return method.invoke(delegate,args);
    }
    public void bindEvent(Method declaredMethod) {
        event=declaredMethod;
    }
}