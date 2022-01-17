package ipca.gameproject.a21080

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import java.util.*

class Obstacle {

    var bitmap : Bitmap
    var x : Float
    var y : Float
    var speed : Int = 0
    var radius : Float

    var maxY : Float
    var minY : Float
    var maxX : Float
    var minX : Float

    private var bitmapWidth0 : Int
    private var bitmapHeight0 : Int

    constructor(context: Context, screenWidth: Int, screenHeight: Int){
        maxX = screenWidth.toFloat()
        minX = 0F
        maxY = screenHeight.toFloat()
        minY = 0F

        bitmap = BitmapFactory
            .decodeResource(context.resources,R.drawable.asteroide)

        bitmapWidth0 = bitmap.width
        bitmapHeight0 = bitmap.height

        var generator = Random()

        var d = generator.nextInt(3) + 3
        bitmap = bitmap.scale(bitmapWidth0/d, bitmapHeight0/d)

        radius = bitmap.height * 0.5f

        speed = generator.nextInt(10) + 5
        x = generator.nextInt(maxX.toInt()).toFloat()
        y = 0f - radius
    }

    fun update(gameScore: Int){
        if (y > maxY + radius){
            var generator = Random()

            var d = generator.nextInt(3) + 3
            bitmap = bitmap.scale(bitmapWidth0/d, bitmapHeight0/d)

            radius = bitmap.height * 0.5f

            speed = generator.nextInt(10) + 10
            x = generator.nextInt(maxX.toInt()).toFloat()
            y = 0f - radius
        }

        y += speed + 0.4f * gameScore
    }
}