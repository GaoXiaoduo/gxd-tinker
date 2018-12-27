package com.gxd.tinker;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.beta.Beta;

/**
 * 不需要在运行时添加读写权限
 **/
public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final int REQUEST_CODE_SDCARD_READ = 1;

    private TextView tvCurrentVersion;

    private Button btnShowToast;

    private Button btnLoadPatch;

    private Button btnKillSelf;

    private Button btnLoadLibrary;

    private Button btnDownloadPatch;

    private Button btnUserPatch;

    private Button btnCheckUpgrade;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView ()
    {

        tvCurrentVersion = (TextView) findViewById(R.id.tvVersion);
        btnShowToast = (Button) findViewById(R.id.btnShowToast);
        btnShowToast.setOnClickListener(this);
        btnKillSelf = (Button) findViewById(R.id.btnKillSelf);
        btnKillSelf.setOnClickListener(this);
        btnLoadPatch = (Button) findViewById(R.id.btnLoadPatch);
        btnLoadPatch.setOnClickListener(this);
        //        btnLoadLibrary = (Button) findViewById(R.id.btnLoadLibrary);
        //        btnLoadLibrary.setOnClickListener(this);
        //        btnDownloadPatch = (Button) findViewById(R.id.btnDownloadPatch);
        //        btnDownloadPatch.setOnClickListener(this);
        //        btnUserPatch = (Button) findViewById(R.id.btnPatchDownloaded);
        //        btnUserPatch.setOnClickListener(this);
        //        btnCheckUpgrade = (Button) findViewById(R.id.btnCheckUpgrade);
        //        btnCheckUpgrade.setOnClickListener(this);

        tvCurrentVersion.setText("包括我吗？ 当前版本：" + getCurrentVersion(this));
    }

    @Override
    public void onClick (View v)
    {

        switch (v.getId())
        {
            // 测试热更新功能
            case R.id.btnShowToast:
                testToast();
                break;
            // 杀死进程
            case R.id.btnKillSelf:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            // 本地加载补丁测试
            case R.id.btnLoadPatch:
                Toast.makeText(this, "button load on click", Toast.LENGTH_LONG).show();
                if (isGrantSDCardReadPermission())
                {
                    getPatchFile();
                }
                else
                {
                    requestPermission();
                }

                break;
            //            case R.id.btnLoadLibrary: // 本地加载so库测试
            //                TestJNI testJNI = new TestJNI();
            //                testJNI.createANativeCrash();
            //                break;
            //            case R.id.btnDownloadPatch:
            //                Beta.downloadPatch();
            //                break;
            //            case R.id.btnPatchDownloaded:
            //                Beta.applyDownloadedPatch();
            //                break;
            //            case R.id.btnCheckUpgrade:
            //                Beta.checkUpgrade();
            //                break;
            default:
                break;
        }
    }

    /**
     * 根据应用patch包前后来测试是否应用patch包成功.
     * <p>
     * 应用patch包前，提示"This is a bug class"
     * 应用patch包之后，提示"The bug has fixed"
     */
    public void testToast ()
    {

        Toast.makeText(this, LoadBugClass.getBugString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取当前版本.
     *
     * @param context 上下文对象
     *
     * @return 返回当前版本
     */
    public String getCurrentVersion (Context context)
    {

        try
        {
            PackageInfo packageInfo =
                    context.getPackageManager().getPackageInfo(this.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            return versionName + "." + versionCode;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return "";

    }

    public void getPatchFile ()
    {

        Beta.applyTinkerPatch(getApplicationContext(), Environment
                .getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
    }

    @Override
    public void onBackPressed ()
    {

        super.onBackPressed();
        Log.e("MainActivity", "onBackPressed");

        Beta.unInit();
    }

    private boolean isGrantSDCardReadPermission ()
    {

        return PermissionUtils.isGrantSDCardReadPermission(this);
    }

    private void requestPermission ()
    {

        PermissionUtils.requestSDCardReadPermission(this, REQUEST_CODE_SDCARD_READ);
    }

    @Override
    public void onRequestPermissionsResult (
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {

        switch (requestCode)
        {
            case REQUEST_CODE_SDCARD_READ:
                handlePermissionResult();
                break;

            default:
                break;
        }
    }

    private void handlePermissionResult ()
    {

        if (isGrantSDCardReadPermission())
        {
            getPatchFile();
        }
        else
        {
            Toast.makeText(this, "failure because without sd card read permission", Toast
                    .LENGTH_SHORT).show();
        }
    }
}
