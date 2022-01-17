package ipca.gameproject.a21080

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btn_play.setOnClickListener {

            startActivity(Intent(this, GameActivity::class.java))
        }

        btn_leaderboard.setOnClickListener {

            startActivity(Intent(this, LeaderboardActivity::class.java))
        }

        db.collection("users")
            .document(FirebaseAuth.getInstance().uid.toString())
            .get().addOnSuccessListener { documentSnapshot ->
                val userData = documentSnapshot.toObject<UserData>()

                tv_hello_user.text = "Hello " + userData?.username
                tv_high_score.text = "Highscore: " + userData?.highScore
            }
    }

    override fun onResume() {
        super.onResume()
        db.collection("users")
            .document(FirebaseAuth.getInstance().uid.toString())
            .get().addOnSuccessListener { documentSnapshot ->
                val userData = documentSnapshot.toObject<UserData>()

                tv_high_score.text = "Highscore: " + userData?.highScore
            }
    }
}