package ipca.gameproject.a21080

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tv_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btn_login.setOnClickListener {
            when {
                TextUtils.isEmpty(et_login_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please enter email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(et_login_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please enter password",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                !TextUtils.regionMatches(
                    "@gmail.com",
                    0,
                    et_login_email.text.toString(),
                    et_login_email.text.toString().length - 10,
                    10) -> {
                    Toast.makeText(
                        this,
                        "Please enter valid email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                et_login_password.text.toString().trim { it <= ' ' }.length < 6 -> {
                    Toast.makeText(
                        this,
                        "Please enter valid password",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                    val email: String = et_login_email.text.toString().trim { it <= ' ' }
                    val password: String = et_login_password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                Toast.makeText(
                                    this,
                                    "Logged in successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            }
                            else {
                                Toast.makeText(
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}