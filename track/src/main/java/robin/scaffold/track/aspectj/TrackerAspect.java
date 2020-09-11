package robin.scaffold.track.aspectj;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Field;

import robin.scaffold.track.aspectj.ReflectUtils;
import robin.scaffold.track.aspectj.Tracker;

import static robin.scaffold.track.Constants.TAG;

@Aspect
public class TrackerAspect {

    @After("execution(* android.app.Activity+.onCreate(..))")
    public void onActivityResume(JoinPoint joinPoint) {
        Log.d(TAG, "onActivityResume: ");
        Tracker.INSTANCE.startViewTracker((Activity) joinPoint.getThis());
    }

    @After("call(* android.app.Dialog+.show(..))")
    public void onDialogShow(JoinPoint joinPoint) {
        Log.d(TAG, "onDialogShow: " + joinPoint.getSignature().getName());
        Dialog dialog = (Dialog) joinPoint.getTarget();
        Tracker.INSTANCE.startViewTracker(dialog.getWindow().getDecorView());
    }

    @After("call(* android.widget.PopupWindow+.showAsDropDown(..))")
    public void onPopupWindowShow(JoinPoint joinPoint) {
        Log.d(TAG, "onPopupWindowShow: " + joinPoint.getSignature().getName());
        PopupWindow popupWindow = (PopupWindow) joinPoint.getTarget();
        try {
            Field field = ReflectUtils.INSTANCE.getDeclaredField(popupWindow, "mDecorView");
            field.setAccessible(true);
            FrameLayout popupDecorView = (FrameLayout) field.get(popupWindow);
            Tracker.INSTANCE.startViewTracker(popupDecorView);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
