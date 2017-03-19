package com.aippcoder.bottomdialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.aippcoder.bottomdialog.model.BottomModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<BottomModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data.add(new BottomModel(R.drawable.ic_alert, "警告"));
        data.add(new BottomModel(R.drawable.ic_circle, "朋友圈"));
        data.add(new BottomModel(R.drawable.ic_delete, "删除"));
        data.add(new BottomModel(R.drawable.ic_edit, "编辑"));
        data.add(new BottomModel(R.drawable.ic_send, "发送"));
        data.add(new BottomModel(R.drawable.ic_star, "收藏"));

    }

    public void click(View v) {
        if(v.getId() == R.id.btn_default) {
            BottomDialog dialog = new BottomDialog(MainActivity.this,data);
            dialog.show();
        } else if(v.getId() == R.id.btn_list) {
            BottomDialog dialog = new BottomDialog(MainActivity.this, R.style.BottomDialog, BottomDialog.LIST, BottomDialog.NORMAL, data);
            dialog.setOnBottomItemClickListener(new BottomDialog.OnBottomItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(parent.getContext(), "Position:" + position, Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setOnBottomItemLongClickListener(new BottomDialog.OnBottomItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(parent.getContext(), "Long Click Position:" + position, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            dialog.show();
        } else if(v.getId() == R.id.btn_grid) {
            BottomDialog dialog = new BottomDialog(MainActivity.this, R.style.BottomDialog, BottomDialog.GRID, BottomDialog.CIRCLE, data);
            dialog.setOnBottomItemClickListener(new BottomDialog.OnBottomItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(parent.getContext(), "Position:" + position, Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setOnBottomItemLongClickListener(new BottomDialog.OnBottomItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(parent.getContext(), "Long Click Position:" + position, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            dialog.show();
        }else if (v.getId() == R.id.btn_custom) {
            View view = View.inflate(MainActivity.this, R.layout.activity_main, null);
            BottomDialog dialog = new BottomDialog(MainActivity.this, R.style.BottomDialog, BottomDialog.CIRCLE, view);
            dialog.show();
        }
    }
}
