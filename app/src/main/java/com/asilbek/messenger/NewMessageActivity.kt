package com.asilbek.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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


supportActionBar?.title="Select User"

//        val adapter = GroupAdapter<GroupieViewHolder>()
//
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//
//        recyclerView_newMessage.adapter=adapter

        fetchUsers()


    }
    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach{
                    Log.d("NewMessage",it.toString())
                    val user =it.getValue(User::class.java)
                    if (user!=null){
                        adapter.add(UserItem(user))

                    }
                }
                recyclerView_newMessage.adapter =adapter
            }
            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
}

class UserItem(val user:User):Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

viewHolder.itemView.userName_textView.text=user.username

        // This shit downloads Avatar for users from FirebaseDatabase
        Picasso.get().load(user.profileImage).into(viewHolder.itemView.imageView_userName)
    }

    override fun getLayout(): Int {
return R.layout.user_row_new_message
    }
}

