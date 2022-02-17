package com.genta.smartalarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.number.IntegerWidth
import android.icu.text.CaseMap
import android.media.RingtoneManager
import android.os.Build
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*

class AlarmService : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra(EXTRA_MESSAGE)
        val type = intent?.getIntExtra(EXTRA_TYPE, 0)

        val title = when (type) {
            TYPE_ONE_TIME -> "Hi'Genta it's time to $message Semangat!!"
            TYPE_REPEATING -> "Repeating Alarm"
            else -> "Something wrong here."
        }

        val requestCode = when (type) {
            TYPE_ONE_TIME -> NOTIF_ID_ONE_TIME
            TYPE_REPEATING -> NOTIF_ID_REPEATING
            else -> -1
        }

        if (context != null && message != null) {
            showNotificationAlarm(
                context,
                title,
                message,
                requestCode
            )
        }
    }

    fun cancelAlarm(context: Context,type: Int,) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,AlarmService::class.java)
        val requestCode = when (type) {
            TYPE_ONE_TIME -> NOTIF_ID_ONE_TIME
            TYPE_REPEATING -> NOTIF_ID_REPEATING
            else -> Log.d( "cancelAlarm","Unknown type of Alarm")
        }
        val pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        if (type == TYPE_ONE_TIME) {
            Toast.makeText(context,"One Time Alarm Canceled.", Toast.LENGTH_SHORT ).show()
        }else{
            Toast.makeText(context,"Repeating Alarm Canceled.",Toast.LENGTH_SHORT).show()
        }
    }

    fun setRepeatingAlarm(context: Context, type: Int, time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmService::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR, Integer.parseInt(timeArray[0]))
//        Log.i("HourOfAlarm", "setOneTimeAlarm ${Integer.parseInt(timeArray[0])}")

        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
//        Log.i("MinuteOfAlarm","setOneTimeAlarm ${Integer.parseInt(timeArray[1])}")

        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_REPEATING, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Succes set RepeatingTimeAlarm.", Toast.LENGTH_SHORT).show()
        Log.i("SetNotifAlarm", "setRepeatingTimeAlarm: alarm will rings on ${calendar.time}")
    }

    fun setOneTimeAlarm(context: Context, type: Int, date: String, time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmService::class.java)
        intent.putExtra("message", message)
        intent.putExtra("type", type)


        //
        //data dati dosy
        //convert di menjadi array ->{22


        val dateArray = date.split("-").toTypedArray()
        val timeArray = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[2]))
//        Log.i("YearOfAlarm", "setOneTimeAlarm ${Integer.parseInt(dateArray[2])}")

        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1)
//        Log.i("MonthOfAlarm","setOneTimeAlarm ${Integer.parseInt(dateArray[1])}")

        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[0]))
//        Log.i("DayOfAlarm", "setOneTimeAlarm ${Integer.parseInt(dateArray[0])}")

        calendar.set(Calendar.HOUR, Integer.parseInt(timeArray[0]))
//        Log.i("HourOfAlarm", "setOneTimeAlarm ${Integer.parseInt(timeArray[0])}")

        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
//        Log.i("MinuteOfAlarm","setOneTimeAlarm ${Integer.parseInt(timeArray[1])}")

        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_ONE_TIME, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Succes set OneTimeAlarm.", Toast.LENGTH_SHORT).show()
        Log.i("SetNotifAlarm", "setOneTimeAlarm: alarm will rings on ${calendar.time}")

    }


    private fun showNotificationAlarm(
        context: Context,
        title: String,
        message: String,
        notificationId: Int
    ) {
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val channelId = "smart_alarm"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_one_time)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(ringtone)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "SmartAlarm", NotificationManager.IMPORTANCE_DEFAULT)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId("alarm_1")
            notificationManager.createNotificationChannel(channel)
        }
        val notif = builder.build()
        notificationManager.notify(notificationId, notif)
    }

    companion object{
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        const val NOTIF_ID_ONE_TIME = 101
        const val NOTIF_ID_REPEATING = 102

        const val TYPE_ONE_TIME = 1
        const val TYPE_REPEATING = 0
    }

}