package robin.scaffold.jet.track;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import robin.scaffold.jet.MainActivity;
import robin.scaffold.jet.R;
import robin.scaffold.track.Utils;

public class TrackActivity extends AppCompatActivity {
    private static final String TAG = "TrackActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        Animal animal = new Animal();
        Log.d(TAG, " onCreate fly start...");
        animal.fly();

//        ViewGroup viewGroup = (ViewGroup)findViewById(ID_ANDROID_CONTENT);
//        viewGroup.getChildCount();
//        TextView textView = findViewById(R.id.text);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: ");
//            }
//        });
//
//        textView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });
        View contentView = LayoutInflater.from(TrackActivity.this).inflate(R.layout.dialog_track, null);
        final PopupWindow popWnd = new PopupWindow(this);
        popWnd.setContentView(contentView);

        EditText editText = findViewById(R.id.edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setAdapter(new RecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: dx--" + dx + ";dy--" + dy);
            }
        });

        final TextView title = findViewById(R.id.title);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(TrackActivity.this);
                        dialog.setContentView(R.layout.dialog_track);
                        dialog.show();
//                        popWnd.showAsDropDown(title);
                    }
                });
            }
        }, 1500);
        Log.e("fjb", Utils.INSTANCE.getAbsolutePath(editText));
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private static final String TAG = "RecyclerViewAdapter";

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(TrackActivity.this).inflate(R.layout.simple_list_item, parent, false);
            Log.d(TAG, "onCreateViewHolder: ");
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: ");
            holder.textView.setText("第" + position + "行数据");
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("fjb", Utils.INSTANCE.getAbsolutePath(holder.textView));
                }
            });
        }

        public void addItem(int position, ViewModel viewModel) {
            notifyItemInserted(position);
        }

        public void removeItem(int position) {
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            private ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.list_title);
            }
        }
    }

    public void onTestClick(View view) {
//        CheckBox checkBox = findViewById(R.id.checkbox);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.d(TAG, "onCheckedChanged: ");
//            }
//        });

        TextView textView = new TextView(this);
        textView.setText("sfdadasdas");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
            }
        });
        LinearLayout layout = findViewById(R.id.container);
        layout.addView(textView, LinearLayout.LayoutParams.WRAP_CONTENT, 200);
    }

}
