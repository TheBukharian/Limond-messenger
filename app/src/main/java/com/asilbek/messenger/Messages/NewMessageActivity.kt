package com.asilbek.messenger.Messages

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.asilbek.messenger.R
import com.asilbek.messenger.RegisterLogin.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        val your_Layout2 = findViewById<RecyclerView>(R.id.recyclerView_newMessage)
        val animationDrawable = your_Layout2.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(4000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()


supportActionBar?.title="Select User"



        fetchUsers()


    }
    companion object{
        val USER_KEY="USER_KEY"
    }
    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<GroupieViewHolder>()
                val bi = findViewById<View>(R.id.Network)



                p0.children.forEach{
                    Log.d("NewMessage",it.toString())
                    val user =it.getValue(User::class.java)
                    if (user!=null){
                        adapter.add(UserItem(user))

                        bi.visibility = View.GONE
                        imageView6.visibility=View.GONE
                    }
                    else {
                        imageView6.visibility=View.VISIBLE
                        bi.visibility = View.VISIBLE
                    }
                }


                adapter.setOnItemClickListener{item,view->
                    val userItem = item as UserItem
                    val intent =Intent(view.context,ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY,userItem.user)
                    startActivity(intent)

                    finish()

                }
                recyclerView_newMessage.adapter =adapter
            }
            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

}

class UserItem(val user: User):Item<GroupieViewHolder>(){


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {



        viewHolder.itemView.userName_textView.text=user.username



        // This  downloads Avatar for users from FirebaseDatabase



        Picasso.get().load(user.profileImage).into(viewHolder.itemView.imageView_userName)


    }


    override fun getLayout(): Int {
return R.layout.user_row_new_message
    }
}

