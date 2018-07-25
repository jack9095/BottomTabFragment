package com.example.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.schedule.utils.ScheduleUtil;
import com.fly.permissions.OnPermissionListener;
import com.fly.permissions.Permission;
import com.fly.permissions.CallPermissions;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int CAMERA = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        requestPermission(Manifest.permission.WRITE_CALENDAR, CAMERA);

        CallPermissions.with(this)
                .permission(Permission.READ_CALENDAR, Permission.WRITE_CALENDAR)
                .request(new OnPermissionListener() {

                    @Override
                    public void onPermissionResult(List<String> granted, boolean isAll) {
                        if (isAll) {
                            Toast.makeText(MainActivity.this, "获取权限成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "获取权限成功，部分权限未正常授予", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if(quick) {
                            Toast.makeText(MainActivity.this, "被永久拒绝授权，请手动授予权限", Toast.LENGTH_SHORT).show();
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            CallPermissions.gotoPermissionSettings(MainActivity.this);
                        }else {
                            Toast.makeText(MainActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void click(View view) {
        ScheduleUtil.addCalendarEvent(this,"你有一场直播","今天是一个风和日丽的日子",
                System.currentTimeMillis() + 60000);
        Log.e("MainActivity","点击事件");
    }

//    /**
//     * 2
//     * 是否申请了该使用权限
//     * @param permission 被检查的权限
//     * @return  true  已经有权限了，false 没有权限，需要申请
//     */
//    private boolean isGranted(String permission) {
//        int checkSelfPermission = ActivityCompat.checkSelfPermission(this, permission);
//        return checkSelfPermission == PackageManager.PERMISSION_GRANTED;
//    }
//
//    /**
//     * 3
//     * 申请使用权限
//     * @param permission  需要申请的权限
//     * @param requestCode 请求码  对应下一步的 回调码，两者需一致
//     */
//    private void requestPermission(String permission, int requestCode) {
//        if (!isGranted(permission)) {
//            // shouldShowRequestPermissionRationale主要用于给用户一个申请权限的解释，
//            // 该方法只有在用户在上一次已经拒绝过你的这个权限申请。
//            // 也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode); // 申请权限
//            }
//        } else {
//            // 有权限，直接执行相应操作了
//            if(ScheduleUtil.checkCalendarAccount(this) == -1){
//                ScheduleUtil.addCalendarAccount(this);
//            }
//        }
//    }
//
//    /**
//     * 4
//     * 处理授权回调
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == CAMERA) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                String jpgPath = getCacheDir() + "test.jpg";
//                // 成功获取到权限，可以执行相关操作
//                if(ScheduleUtil.checkCalendarAccount(this) == -1){
//                    ScheduleUtil.addCalendarAccount(this);
//                }
//            } else {
//                // Permission Denied
//                Toast.makeText(MainActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
//            }
//            return;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    public void delete(View view) {
        ScheduleUtil.deleteCalendarEvent(this,"你有一场直播");
    }
}
