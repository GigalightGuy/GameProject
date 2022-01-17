package ipca.gameproject.a21080

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.PointF
import androidx.core.graphics.scale
import kotlin.math.abs

class Player {

    var bitmap : Bitmap
    var x : Float
    var y : Float
    var target : PointF
    var speed : Int = 0
    var radius : Float

    constructor(context: Context, screenWidth: Int, screenHeight : Int){
        speed = 100
        bitmap = BitmapFactory
            .decodeResource(context.resources,R.drawable.straight_face)

        bitmap = bitmap.scale(bitmap.width/10, bitmap.height/10)

        radius = bitmap.height * 0.4f
        x = screenWidth / 2f
        y = screenHeight - screenHeight / 5f
        target = PointF(x, y)
    }

    fun update(){
        var dist : Float = target.x - x
        if(abs(dist) <= speed) x += dist
        else{
            if(dist < 0) x -= speed
            else x += speed
        }

        dist = target.y - y
        if(abs(dist) <= speed) y += dist
        else{
            if(dist < 0) y -= speed
            else y += speed
        }
    }

    fun move(target: PointF){
        this.target = target
    }

    fun stopMoving(){
        target = PointF(x, y)
    }
}
