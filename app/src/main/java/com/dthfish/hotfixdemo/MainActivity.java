package com.dthfish.hotfixdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTv = findViewById(R.id.tv);
        findViewById(R.id.btn_set_text).setOnClickListener(this);
        findViewById(R.id.btn_hotfix).setOnClickListener(this);
        findViewById(R.id.btn_delete_hotfix).setOnClickListener(this);
        findViewById(R.id.btn_kill_self).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_text:
                mTv.setText(new Util().getTitle());
                break;
            case R.id.btn_hotfix:
//                printClassLoader();
                loadHotfixDex();
                break;
            case R.id.btn_delete_hotfix:
                deleteHotfixDex();
                break;
            case R.id.btn_kill_self:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }

    }

    private void deleteHotfixDex() {
        File dex = new File(getCacheDir(), "hotfix.dex");
        if (dex.exists()) {
            boolean delete = dex.delete();
            if (delete)
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();

        }

    }

    private void printClassLoader() {
        ClassLoader classLoader = getClassLoader();
        while (classLoader != null) {
            Log.d("HotfixDemo", "ClassLoader: " + classLoader.getClass().getName());
            classLoader = classLoader.getParent();
        }
    }

    /**
     * 实际应用上这里应该从网络获取的
     */
    private void loadHotfixDex() {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = getAssets().open("hotfix.dex");
            File file = new File(getCacheDir(), "hotfix.dex");
            os = new FileOutputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            os.write(buffer);
            Toast.makeText(this, "加载成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
