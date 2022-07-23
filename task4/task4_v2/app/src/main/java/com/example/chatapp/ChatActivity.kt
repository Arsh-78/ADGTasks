package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.ActivityChatBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class ChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatBinding
     var databaseReference : DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initFirebase()
        setupSendButton()
        createFirebaseListener()


    }

    private fun setupSendButton() {
        binding.sendButton.setOnClickListener {
            if (!binding.enterMessage.text.toString().isEmpty()){
                sendData()
            }else{
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(applicationContext)

        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG)

        //get reference to our db
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    private fun sendData() {
        databaseReference?.
        child("messages")?.
        child(java.lang.String.valueOf(System.currentTimeMillis()))?.
        setValue(Message(binding.enterMessage.text.toString()))

        //clear the text
        binding.enterMessage.setText("")
    }

    private fun createFirebaseListener(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

               val  toReturn : ArrayList<Message> = ArrayList();

                for(data in dataSnapshot.children){
                    val messageData = data.getValue<Message>(Message::class.java)

                    //unwrap
                    val message = messageData?.let { it } ?: continue

                    toReturn.add(message)
                }

                //sort so newest at bottom
                toReturn.sortBy { message ->
                    message.timestamp
                }

                setupAdapter(toReturn)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //log error
            }
        }
        databaseReference?.child("messages")?.addValueEventListener(postListener)
    }

    private fun setupAdapter(data: ArrayList<Message>) {

        val linearLayoutManager = LinearLayoutManager(this)
        binding.messageView.layoutManager = linearLayoutManager
        binding.messageView.adapter = MessageAdapter(data) {
            Toast.makeText(this, "${it.text} clicked", Toast.LENGTH_SHORT).show()
        }

        //scroll to bottom
        binding.messageView.scrollToPosition(data.size - 1)
    }
}