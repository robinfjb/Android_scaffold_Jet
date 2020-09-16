package robin.scaffold.track;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ViewInject {

    public static void inject(Activity activity) {

        Class<? extends Activity> activityKlazz = activity.getClass();

        try {

            injectView(activity,activityKlazz);
            proxyClick(activity,activityKlazz);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void proxyClick(Activity activity,Class<? extends Activity> activityKlazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method[] declaredMethods = activityKlazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            //获取标记了OnClick注解的方法
            if (declaredMethod.isAnnotationPresent(OnClick.class)){
                OnClick annotation = declaredMethod.getAnnotation(OnClick.class);
                int[] value = annotation.value();
                //创建处理器类并且生成代理类，同时将activity中我们标记了OnClick的方法和处理器类绑定起来
                OnClickListenerProxy proxy=new OnClickListenerProxy();
                Object listener=proxy.bind(activity);
                proxy.bindEvent(declaredMethod);


                for (int viewId : value) {
                    Method findViewByIdMethod =
                            activityKlazz.getMethod("findViewById", int.class);

                    findViewByIdMethod.setAccessible(true);
                    View view = (View) findViewByIdMethod.invoke(activity, viewId);

                    //通过反射把我们的代理类注入到相应view的onClickListener中
                    Method setOnClickListener = view.getClass().getMethod("setOnClickListener", View.OnClickListener.class);

                    setOnClickListener.setAccessible(true);
                    setOnClickListener.invoke(view,listener);
                }
            }
        }

    }


    private static void injectView(Activity activity,Class<? extends Activity> activityKlazz)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        /**
         * 注入view其实很简单，通过反射拿到activity中标记了InjectView注解的field。
         * 然后通过反射获取到findViewById方法，并且执行这个方法拿到view的实例
         * 接着将实例赋值给activity里的field上
         */
        for (Field field : activityKlazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectView.class)) {
                InjectView annotation = field.getAnnotation(InjectView.class);
                int viewId = annotation.value();
                Method findViewByIdMethod = activityKlazz.getMethod("findViewById", int.class);
                findViewByIdMethod.setAccessible(true);
                View view = (View) findViewByIdMethod.invoke(activity, viewId);
                field.setAccessible(true);
                field.set(activity, view);
            }
        }
    }
}