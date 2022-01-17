package ipca.gameproject.a21080

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tv_login.setOnClickListener {
            onBackPressed()
        }

        btn_register.setOnClickListener {
            when {
                TextUtils.isEmpty(et_register_username.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please enter username",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(et_register_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please enter email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(et_register_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please enter password",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                !TextUtils.regionMatches(
                    "@gmail.com",
                    0,
                    et_register_email.text.toString(),
                    et_register_email.text.toString().length - 10,
                    10) -> {
                    Toast.makeText(
                        this,
                        "Please enter valid email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                et_register_password.text.toString().trim { it <= ' ' }.length < 6 -> {
                    Toast.makeText(
                        this,
                        "Please enter valid password",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                    val username: String = et_register_username.text.toString().trim { it <= ' ' }
                    val email: String = et_register_email.text.toString().trim { it <= ' ' }
                    val password: String = et_register_password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                val db = Firebase.firestore

                                val user = hashMapOf(
                                    "username" to username,
                                    "highScore" to 0,
                                )
                                db.collection("users")
                                    .document(FirebaseAuth.getInstance().uid.toString())
                                    .set(user)

                                Toast.makeText(
                                    this,
                                    "Registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
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