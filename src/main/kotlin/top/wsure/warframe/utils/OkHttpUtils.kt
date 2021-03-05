package top.wsure.warframe.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import top.wsure.warframe.data.RemoteCommand
import top.wsure.warframe.utils.OkHttpUtils.Companion.doGetObject
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * FileName: OkHttpUtils
 * Author:   wsure
 * Date:     2020/9/24 4:44 下午
 * Description:
 */
class OkHttpUtils {
    companion object {
        private val client = OkHttpClient().newBuilder().readTimeout(60,TimeUnit.SECONDS).build()

        fun doGet(url:String):String?{
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            val call = client.newCall(request)
            val response = call.execute()
            return response.body?.string()
        }

        fun getImage(url:String): InputStream {
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            val call = client.newCall(request)
            val response = call.execute()
            return response.body?.byteStream()!!
        }

        inline fun <reified T> doGetObject(url: String):T{
            return doGet(url)?.let { Json{ ignoreUnknownKeys = true }.decodeFromString<T>(it) }!!
        }


    }
}
fun main(){
    val list:List<RemoteCommand> = doGetObject("http://localhost:3000/robot/commands")
    list.forEach { println(it.toString()) }
}