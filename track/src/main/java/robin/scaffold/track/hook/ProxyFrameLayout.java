package robin.scaffold.track.hook;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import robin.scaffold.track.Constants;
import robin.scaffold.track.Utils;

public class ProxyFrameLayout extends FrameLayout {
    private Activity resumedActivity = null;
    private long touchViewHashCode = -1L;

    public ProxyFrameLayout(Activity activity) {
        super(activity);
        this.resumedActivity = activity;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View touchViewDown = findEventSrcView(event, this);
                if(touchViewDown != null) {
                    Log.d(Constants.INSTANCE.TAG, "Activity:" + resumedActivity.getClass().getName()
                            + "- ACTION_DOWN:" + Utils.INSTANCE.getAbsolutePath(touchViewDown));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //TODO
                break;
            case MotionEvent.ACTION_UP:
                View touchViewUp = findEventSrcView(event, this);
                if(touchViewUp != null) {
                    Log.d(Constants.INSTANCE.TAG, "Activity:" + resumedActivity.getClass().getName()
                            + "- ACTION_UP:" + Utils.INSTANCE.getAbsolutePath(touchViewUp));
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    /**
     * 遍历找到点击的view
     * @param event
     * @param srcView
     * @return
     */
    private View findEventSrcView(MotionEvent event, View srcView) {
        if (srcView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)srcView;
            int size = viewGroup.getChildCount();

            for (int i = 0; i < size; i++) {
                View view = viewGroup.getChildAt(i);
                if (!(view instanceof ProxyFrameLayout) && isEventInView(event, view)) {
                    View tmpRetView = findEventSrcView(event, view);
                    if (tmpRetView != null) {
                        return tmpRetView;
                    }
                }
            }
        } else if (isEventInView(event, srcView)) {
            return srcView;
        }
        return null;
    }

    /**
     * 判断是否在view的rect范围内
     * @param event
     * @param srcView
     * @return
     */
    private boolean isEventInView(MotionEvent event, View srcView) {
        Rect currentViewRect = new Rect();
        if (srcView.getGlobalVisibleRect(currentViewRect)) {
            RectF rectF = new RectF(currentViewRect);
            if (rectF.contains(event.getRawX(), event.getRawY())) {
                return true;
            }
        }
        return false;
    }
}
