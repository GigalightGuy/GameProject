package ipca.gameproject.a21080

import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_leaderboard.*

class LeaderboardActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    var players = arrayListOf<UserData>()

    private lateinit var adapter : ScoresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        adapter = ScoresAdapter()
        lv_leaderboard.adapter = adapter

        db.collection("users")
            .orderBy("highScore", Query.Direction.DESCENDING)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                players.clear()
                for (doc in value!!.documents) {
                    if(doc.data != null){
                        val userData = doc.toObject<UserData>()
                        players.add(userData!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    inner class ScoresAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return players.size
        }

        override fun getItem(position: Int): Any {
            return players[position]
        }

        override fun getItemId(p0: Int): Long {
            return 0L
        }

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
            var rootView = layoutInflater.inflate(R.layout.player_score,viewGroup,false)
            val username = rootView.findViewById<TextView>(R.id.tv_username)
            val score = rootView.findViewById<TextView>(R.id.tv_score)

            username.text = players[position].username
            score.text = players[position].highScore.toString()


            return rootView
        }

    }
}