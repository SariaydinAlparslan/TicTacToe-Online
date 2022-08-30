package com.sariaydinalparslan.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_online_code_genarator.*

var isCodeMaker = true
var code = "null"
var codeFound = false
var checkTemp = true
var keyValue : String = "null"
class OnlineCodeGenaratorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_code_genarator)

        onlinebutton1.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = editText1.text.toString()
            onlinebutton1.visibility = View.GONE
            onlinebutton2.visibility = View.GONE
            editText1.visibility = View.GONE
            pbLoading.visibility = View.VISIBLE
            if (code != "null" && code!=""){
                isCodeMaker = true
                FirebaseDatabase.getInstance().reference.child("codes")
                    .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var check = isValueAvaliable(snapshot, code)
                        Handler().postDelayed({
                            if (check == true){
                                onlinebutton1.visibility = View.VISIBLE
                                onlinebutton2.visibility = View.VISIBLE
                                editText1.visibility = View.VISIBLE
                                pbLoading.visibility = View.GONE
                            }else{
                                FirebaseDatabase.getInstance().reference.child("codes").push()
                                    .setValue(code)
                                isValueAvaliable(snapshot, code)
                                checkTemp = false
                                Handler().postDelayed({
                                    accepted()
                                    Toast.makeText(
                                        this@OnlineCodeGenaratorActivity,
                                        "Go back",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },300)
                            }
                        },2000)
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            }else{
                onlinebutton1.visibility = View.VISIBLE
                onlinebutton2.visibility = View.VISIBLE
                editText1.visibility = View.VISIBLE
                pbLoading.visibility = View.GONE
                Toast.makeText(this, "Enter a Code", Toast.LENGTH_SHORT).show()
            }
        }
        onlinebutton2.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = editText1.text.toString()
            if (code!= "null" && code!=""){
                onlinebutton1.visibility = View.GONE
                onlinebutton2.visibility = View.GONE
                editText1.visibility = View.GONE
                pbLoading.visibility = View.VISIBLE
                isCodeMaker = false
                FirebaseDatabase.getInstance().reference.child("codes")
                    .addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var data : Boolean = isValueAvaliable(snapshot, code)
                            Handler().postDelayed({
                                if (data ==true){
                                    codeFound = true
                                    accepted()
                                    onlinebutton1.visibility = View.VISIBLE
                                    onlinebutton2.visibility = View.VISIBLE
                                    editText1.visibility = View.VISIBLE
                                    pbLoading.visibility = View.GONE
                                }else{
                                    onlinebutton1.visibility = View.VISIBLE
                                    onlinebutton2.visibility = View.VISIBLE
                                    editText1.visibility = View.VISIBLE
                                    pbLoading.visibility = View.GONE
                                    Toast.makeText(
                                        this@OnlineCodeGenaratorActivity,
                                        "Invalid COde",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },2000)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
            }else{
                Toast.makeText(this, "ENter A Code", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun accepted (){
        startActivity(Intent(this,OnlineMultiPlayerActivity::class.java))
        onlinebutton1.visibility = View.VISIBLE
        onlinebutton2.visibility = View.VISIBLE
        editText1.visibility = View.VISIBLE
        pbLoading.visibility = View.GONE
    }
    fun isValueAvaliable(snapshot : DataSnapshot,code:String):Boolean{
        var data = snapshot.children
        data.forEach{
            var value = it.getValue().toString()
        if (value == code){
            keyValue = it.key.toString()
            return true
             }
        }
        return false
    }
}