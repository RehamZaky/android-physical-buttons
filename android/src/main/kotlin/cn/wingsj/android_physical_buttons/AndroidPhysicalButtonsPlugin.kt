package cn.wingsj.android_physical_buttons

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.KeyEvent
import android.widget.Toast
import androidx.annotation.NonNull
import io.flutter.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel

class AndroidPhysicalButtonsPlugin : FlutterPlugin, EventChannel.StreamHandler, BroadcastReceiver() {
    private var actionVolume = "android.media.VOLUME_CHANGED_ACTION"
    private lateinit var channel: EventChannel
    private var sink: EventChannel.EventSink? = null

    override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel = EventChannel(binding.binaryMessenger, "android_physical_buttons")
        channel.setStreamHandler(this)

        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_MEDIA_BUTTON)
      //  filter.addAction(actionVolume)
        binding.applicationContext.registerReceiver(this, filter)
    }

    override fun onListen(arguments: Any?, sink: EventChannel.EventSink?) {
        this.sink = sink
    }

    override fun onCancel(arguments: Any?) {}

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setStreamHandler(null)
    }

    override fun onReceive(context: Context, intent: Intent) {
       var action = intent.getAction()
        Toast.makeText(context, " media button test", Toast.LENGTH_LONG).show()

        if (intent.action.equals(Intent.ACTION_SCREEN_ON) || intent.action.equals(Intent.ACTION_SCREEN_OFF)) {
            sink?.success("power")
        }
        else if(intent.action.equals(Intent.ACTION_MEDIA_BUTTON))
        {
            val event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT) as KeyEvent? ?: return
            if ( event.action == KeyEvent.KEYCODE_VOLUME_DOWN || event.action == KeyEvent.KEYCODE_VOLUME_UP) {
                sink?.success("volume")
                Toast.makeText(context, "BUTTON PRESSED!", Toast.LENGTH_SHORT).show();
            }
            if (event.action == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                // do something
                Toast.makeText(context, "Play Pause pressed!", Toast.LENGTH_SHORT).show();
            }
            
            val key = intent.getIntExtra("android.intent.extra.KEY_EVENT", 0)
            if(key == 24)
            {
                sink?.success("volume")
            }
            if(key == 25)
            {
                sink?.success("volume")
            }
            
        }
        else{
            sink?.success("any_key")
        }
    }
}
