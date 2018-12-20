package br.com.sudosu.terminal.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import br.com.sudosu.terminal.R

object NotificationUtil{
    fun create(context: Context, id: Int, intent: Intent, contentTitle: String, contentText: String){
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //Intent para dispara broadcast
        val p = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        //cria a notifocação
        val builder = NotificationCompat.Builder(context, "id")
            .setContentIntent(p)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_stat)
            .setAutoCancel(true)
        //Dispara a notificação
        val n = builder.build()
        manager.notify(id, n)
    }//fecha create


}//feha object