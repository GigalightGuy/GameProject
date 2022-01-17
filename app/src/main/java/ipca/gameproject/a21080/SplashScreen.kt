package ipca.gameproject.a21080

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if(FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}