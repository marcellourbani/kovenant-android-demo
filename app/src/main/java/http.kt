package nl.mplatvoet.komponents.kovenant.android.demo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import fuel.Fuel
import fuel.core.*
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import nl.komponents.kovenant.then
import java.io.ByteArrayInputStream

public class FuelHttpService {
    public fun textUrl(url: String, parameters: Map<String, Any?>? = null): Promise<String, Exception> {
        return Fuel.get(url, parameters).promise() then {
            val (response, array) = it
            //should get encoding from the response.
            String(array, "UTF-8")
        }
    }

    public fun bitmapUrl(url: String): Promise<Bitmap, Exception> {
        return Fuel.get(url).promise() then {
            BitmapFactory.decodeStream(ByteArrayInputStream(it.second))
        }
    }
}


public fun Request.promise(): Promise<Pair<Response, ByteArray>, Exception> {
    val deferred = deferred<Pair<Response, ByteArray>, Exception>()
    response { request, response, either ->
        when (either) {
            is Left<*, *> -> deferred.reject(either.get())
            is Right<*, *> -> deferred.resolve(Pair(response, either.get()))
        }
    }
    return deferred.promise
}


