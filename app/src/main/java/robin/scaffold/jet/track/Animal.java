package robin.scaffold.jet.track;

import android.util.Log;

public class Animal {
    private static final String TAG = "Animal";

    public void fly() {
        Log.e(TAG, "animal fly method:" + this.toString() + "#fly");
    }
}
