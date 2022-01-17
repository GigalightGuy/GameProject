package ipca.gameproject.a21080

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.minus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.math.pow
import kotlin.math.sqrt

class GameView : SurfaceView, Runnable {

    val db = Firebase.firestore

    var playing = false
    lateinit var gameThread : Thread
    var score : Int = 0
    var highScore : Int = 0
    var screenWidth : Int = 0
    var screenHeight : Int = 0


    lateinit var surfaceHolder: SurfaceHolder
    var canvas : Canvas? =  null
    lateinit var paint : Paint

    lateinit var player : Player
    var obstacles = arrayListOf<Obstacle>()

    constructor(context: Context?,
                screenWidth : Int,
                screenHeight:Int) : super(context){
        init(context,
            screenWidth,
            screenHeight)

        this.screenWidth = screenWidth
        this.screenHeight = screenHeight
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context)
    }

    private fun init(context: Context?,
                     screenWidth : Int = 0,
                     screenHeight: Int = 0){

        surfaceHolder = holder
        player = Player(context!!, screenWidth, screenHeight)
        paint = Paint()

        score = 0

        db.collection("users")
            .document(FirebaseAuth.getInstance().uid.toString())
            .get().addOnSuccessListener { documentSnapshot ->
                val userData = documentSnapshot.toObject<UserData>()

                highScore = userData?.highScore!!
            }

        for(index in 0..2){
            obstacles.add(Obstacle(context,screenWidth,screenHeight))
        }
    }


    override fun run() {
        while(playing){
            update()
            draw()
            control()
        }
    }

    fun resume(){
        playing = true
        gameThread = Thread(this)
        gameThread.start()
    }

    fun pause(){
        playing = false
        gameThread.join()
    }

    private fun gameOver(){
        playing = false
        if(score > highScore) highScore = score


        val user = hashMapOf(
            "highScore" to highScore,
        )

        db.collection("users")
            .document(FirebaseAuth.getInstance().uid.toString())
            .set(user, SetOptions.merge())
    }

    private fun update(){
        player.update()

        for(e in obstacles){
            e.update(score)
            if (e.y > e.maxY + e.radius) score++

            if(detectCollision(PointF(e.x, e.y), e.radius, PointF(player.x, player.y), player.radius)){
                player.x = -150f
                gameOver()
            }

        }
    }

    private fun draw(){
        if (surfaceHolder.surface.isValid){
            canvas = surfaceHolder.lockCanvas()
            canvas?.drawColor(Color.BLUE)

            for(e in obstacles){
                canvas?.drawBitmap(e.bitmap, e.x - e.radius, e.y - e.radius, paint)
            }
            canvas?.drawBitmap(player.bitmap, player.x - player.radius, player.y - player.radius, paint)

            paint.color = Color.BLACK
            paint.style = Paint.Style.FILL
            paint.typeface = Typeface.MONOSPACE
            paint.textSize = 200f

            canvas?.drawText(score.toString(), 100f, 200f, paint)

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun control(){
        Thread.sleep(17)
    }

    private fun detectCollision(pos1: PointF, rad1: Float, pos2: PointF, rad2: Float) : Boolean {
        val dist : Float
        val v : PointF = pos1.minus(pos2)
        dist = sqrt(v.x.pow(2) + v.y.pow(2))
        if(dist < (rad1 + rad2)) return true
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let{
            var target = PointF(it.x, it.y)
            when (it.action.and(MotionEvent.ACTION_MASK)){
                MotionEvent.ACTION_DOWN ->{
                    player.move(target)
                }
                MotionEvent.ACTION_MOVE ->{
                    player.move(target)
                }
                MotionEvent.ACTION_UP ->{
                    player.stopMoving()
                }
            }
        }
        return true
    }
}
