package com.sariaydinalparslan.tictactoe

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_online_multi_player.*
import kotlin.system.exitProcess

var isMyMove = isCodeMaker
var playerTurn = true
class OnlineMultiPlayerActivity : AppCompatActivity() {
    var player1count = 0
    var player2count = 0
    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var emptyCells = ArrayList<Int>()
    var activeUser = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_multi_player)

        resetbtn.setOnClickListener {
            reset()
        }
        FirebaseDatabase.getInstance().reference.child("data").child(code)
            .addChildEventListener(object :ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    var data = snapshot.value
                    if (isMyMove == true){
                        isMyMove = false
                        moveOnline(data.toString(), isMyMove)
                    }else{
                        isMyMove = true
                        moveOnline(data.toString(), isMyMove)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    reset()
                    Toast.makeText(this@OnlineMultiPlayerActivity, "Gamereset", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    fun moveOnline(data : String,move : Boolean){
    if (move){
        var buttonSelected : Button ?
        buttonSelected = when(data.toInt()){
            1-> button
            2-> button2
            3-> button3
            4-> button4
            5-> button5
            6-> button6
            7-> button7
            8-> button8
            9-> button9
            else-> { button }
        }
        buttonSelected.text = "0"
        idTurn.text = "Turn : Player 1"
        buttonSelected.setTextColor(Color.parseColor("#0222B884"))
        player2.add(data.toInt())
        emptyCells.add(data.toInt())
       buttonSelected.isEnabled = false
        checkWinner()

    }
}

    private fun checkWinner() : Int {
        if (player1.contains(1) && player1.contains(2) && player1.contains(3) ||
            player1.contains(1) && player1.contains(4) &&player1.contains(7)  ||
            player1.contains(3) && player1.contains(6) &&player1.contains(9)  ||
            player1.contains(7) && player1.contains(8) &&player1.contains(9)  ||
            player1.contains(4) && player1.contains(5) &&player1.contains(6)  ||
            player1.contains(3) && player1.contains(5) &&player1.contains(7)  ||
            player1.contains(2) && player1.contains(5) &&player1.contains(8)){
            player1count+= 1
            buttonDisable()
            disableReset()
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 1 Wins " +"\n\n"+"do u want again")
            build.setNegativeButton("Ok"){
                dialog,which ->
                reset()
            }
            build.setNegativeButton("exit"){dialog,which->
               removeCode()
               exitProcess(1)
            }
            Handler().postDelayed(Runnable { build.show() },2000)
            return  1
        }else if (
            player2.contains(1) && player2.contains(2) && player2.contains(3) ||
            player2.contains(1) && player2.contains(4) &&player2.contains(7)  ||
            player2.contains(3) && player2.contains(6) &&player2.contains(9)  ||
            player2.contains(7) && player2.contains(8) &&player2.contains(9)  ||
            player2.contains(4) && player2.contains(5) &&player2.contains(6)  ||
            player2.contains(3) && player2.contains(5) &&player2.contains(7)  ||
            player2.contains(2) && player2.contains(5) &&player2.contains(8)
        ){
            player2count+=1
            buttonDisable()
            disableReset()
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 2 Wins " +"\n\n"+"do u want again")
            build.setNegativeButton("Ok"){
                    dialog,which ->
                reset()
            }
            build.setNegativeButton("exit"){dialog,which->
                removeCode()
                exitProcess(1)
            }
            Handler().postDelayed(Runnable { build.show() },2000)
            return 1
        }else if(emptyCells.contains(1) && emptyCells.contains(2) && emptyCells.contains(3)
            && emptyCells.contains(4)&& emptyCells.contains(5)&& emptyCells.contains(6)
            && emptyCells.contains(7)&& emptyCells.contains(8)&& emptyCells.contains(9)){
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Draw")
            build.setMessage("Game Draw "+"\n\n"+ " Do you want to play again")
            build.setPositiveButton("Ok "){dialog,which ->
                reset()

            }
            build.setNegativeButton("exit"){dialog,which->
                exitProcess(1)
                removeCode()
            }
            build.show()
            return 1
        }
        return 0
    }

    fun playNow(buttonSelected : Button,currCell : Int){
        buttonSelected.text = "x"
        emptyCells.remove(currCell)
        idTurn.text = "Turn : Player 2"
        buttonSelected.setTextColor(Color.parseColor("#EC0C0C"))
        player1.add(currCell)
        emptyCells.add(currCell)
        buttonSelected.isEnabled = false
        checkWinner()

    }

    fun reset() {
        player1.clear()
        player2.clear()
        emptyCells.clear()
        activeUser = 1
        for (i in 1..9){
            var buttonSelected : Button?
            buttonSelected= when(i){
                1->button
                2->button2
                3->button3
                4->button4
                5->button5
                6->button6
                7->button7
                8->button8
                9->button9
                else -> {button}
            }
            buttonSelected.isEnabled = true
            buttonSelected.text =""
            textView.text ="Player 1: $player1count"
            textView2.text ="Player 2 : $player2count"
            isMyMove = isCodeMaker
            if (isCodeMaker){
                FirebaseDatabase.getInstance().reference.child("data")
                    .child(code).removeValue()
            }

        }
    }

    fun buttonDisable(){
        for (i in 1..9){
            val buttonSelected = when(i){
                1 ->button
                2 ->button2
                3 ->button3
                4 ->button4
                5 ->button5
                6 ->button6
                7 ->button7
                8 ->button8
                9 ->button9
                else -> { button }
            }
            if (buttonSelected.isEnabled == true)
                buttonSelected.isEnabled = false
        }
    }

    fun removeCode(){
        if (isCodeMaker){
            FirebaseDatabase.getInstance().reference.child("codes")
                .child(keyValue).removeValue()
        }
    }

    fun disableReset(){
        resetbtn.isEnabled = false
        Handler().postDelayed(Runnable {
            resetbtn.isEnabled = true
        },2000)
    }

    fun updateDatabase(cellId : Int){
        FirebaseDatabase.getInstance().reference.child("data").child(code)
            .push().setValue(cellId)
    }

    override fun onBackPressed() {
        removeCode()
        if (isCodeMaker){
            FirebaseDatabase.getInstance().reference.child("data")
                .child(code).removeValue()
        }
        exitProcess(0)
    }

    fun clickfun(view: View) {

        if (isMyMove){
            val but = view as Button
            var cellOnline = 0
            when (but.id){
                R.id.button -> cellOnline =1
                R.id.button2 -> cellOnline =2
                R.id.button3 -> cellOnline =3
                R.id.button4 -> cellOnline =4
                R.id.button5 -> cellOnline =5
                R.id.button6 -> cellOnline =6
                R.id.button7 -> cellOnline =7
                R.id.button8 -> cellOnline =8
                R.id.button9 -> cellOnline =9
                else -> {cellOnline = 0}
            }
            playerTurn = false
            Handler().postDelayed(Runnable { playerTurn = true },600)
            playNow(but,cellOnline)
            updateDatabase(cellOnline)
        }
    }
}