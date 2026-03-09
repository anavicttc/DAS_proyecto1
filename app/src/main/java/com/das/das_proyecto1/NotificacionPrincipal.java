package com.das.das_proyecto1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificacionPrincipal extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //leemos el idioma elegido
        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String idiomaGuardado = prefs.getString("idioma", "es");

        //aplicar idioma al contexto
        java.util.Locale locale = new java.util.Locale(idiomaGuardado);
        java.util.Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "menu_channel";

        // Crear canal para versiones Android 8.0+ REVISARRRR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Recordatorios", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        String titulo = intent.getStringExtra("titulo_notificacion");
        String texto = intent.getStringExtra("texto_notificacion");

        //mensaje por defecto por si no se pasa nada
        if (titulo == null){titulo = context.getString(R.string.noti_comer);}
        if (texto == null) {texto = context.getString(R.string.noti_comer_sub);}
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(titulo)
                .setContentText(texto)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        manager.notify(1, builder.build());
    }
}