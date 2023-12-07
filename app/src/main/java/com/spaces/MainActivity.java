package com.spaces;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Objects;

import androidx.navigation.fragment.NavHostFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final double MIN_OPENGL_VERSION = 3.1;
    private ArFragment arFragment;
    private ModelRenderable model1;
    private ModelRenderable model2;
    private ModelRenderable model3;
    private ModelRenderable model4;
    private TextView txtTitle;
    private String selectedModel = "";
    private DrawerLayout drawerLayout;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkDeviceSupport(this)) {
            return;
        }
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView imgLeftIcon=findViewById(R.id.imgLeftIcon);
        txtTitle=findViewById(R.id.txtTitle);
        imgLeftIcon.setOnClickListener(view1 -> openOrCloseDrawer());
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        ModelRenderable.builder()
                .setSource(this, R.raw.lean_ah_cen_hq)
                .build()
                .thenAccept(renderable -> model1 = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });


        ModelRenderable.builder()
                .setSource(this, R.raw.on_again_off_again_)
                .build()
                .thenAccept(renderable -> model2 = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.the_rory)
                .build()
                .thenAccept(renderable -> model3 = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });


        ModelRenderable.builder()
                .setSource(this, R.raw.glens_monitor_riser_2_tier)
                .build()
                .thenAccept(renderable -> model4 = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });


        if (arFragment != null) {
            arFragment.setOnTapArPlaneListener(
                    (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                        plotModel(hitResult);
                    });
        }
    }


    public void openOrCloseDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void plotModel(HitResult hitResult) {
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
        andy.setParent(anchorNode);
        andy.setLocalScale(new Vector3(1f, 1f, 1f));
        switch (selectedModel) {
            case Models.Model_1: {
                if (model1 == null)
                    return;
                andy.setRenderable(model1);
            }
            break;
            case Models.Model_2: {
                if (model2 == null) return;
                andy.setRenderable(model2);
            }
            break;
            case Models.Model_3: {
                if (model3 == null) return;
                andy.setRenderable(model3);
            }
            break;
            case Models.Model_4: {
                if (model4 == null) return;
                andy.setRenderable(model4);
            }
            break;
        }
        andy.select();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_model_1:
                selectedModel = Models.Model_1;
                txtTitle.setText("Shelf Unit");
                break;
            case R.id.menu_model_2:
                selectedModel = Models.Model_2;
                txtTitle.setText("Small Cupboard");
                break;
            case R.id.menu_model_3:
                selectedModel = Models.Model_3;
                txtTitle.setText("Cupboard");
                break;
            case R.id.menu_model_4:
                selectedModel = Models.Model_4;
                txtTitle.setText("Desk");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @SuppressLint("ObsoleteSdkInt")
    public static boolean checkDeviceSupport(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.1 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    @interface Models {
        String Model_1 = "Model_1";
        String Model_2 = "Model_2";
        String Model_3 = "Model_3";
        String Model_4 = "Model_4";
    }
}