package com.das.das_proyecto1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //menú lateral (navigation drawer)
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //botón de hamburguesa
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment fragmentSeleccionado = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_menu_semanal) {//si tocamos menú semanal que nos lleve ahí
                fragmentSeleccionado = new MenuSemanalFragment();
                toolbar.setTitle("Menú Semanal");
            } else if (itemId == R.id.nav_lista_compra) {
                fragmentSeleccionado = new ListaCompraFragment();
                toolbar.setTitle("Lista de la Compra");
            } else if (itemId == R.id.nav_ideas_comer) {
                fragmentSeleccionado = new IdeasComerFragment();//ideas para comer
                toolbar.setTitle("Ideas Para Comer");
            }

            //cambio de pantalla
            if (fragmentSeleccionado != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragmentSeleccionado)
                        .commit();
            }
            //cerrar caja
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        //cargamos menú semanal nada más abrir la app
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MenuSemanalFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_menu_semanal);
            toolbar.setTitle("Menú Semanal");
        }

        // necesario para que cuando demos para atrás con el menú lateral abierto lo cerremos
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // menú lateral abierto -> cerrarlo
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    //menú lateral cerrado -> salir normal
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        //comprobamos permisos y lanzamos noti
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            } else {
                lanzarNotificacionPrueba();
            }
        } else {
            lanzarNotificacionPrueba();
        }
    }
    //lanzar notificación a los 5s de abrir la app (después de haber aceptado las notis y cerrarla)
    private void lanzarNotificacionPrueba() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, Notificaciones.class);
            sendBroadcast(intent); //receiver
        }, 5000);//5s
    }
}