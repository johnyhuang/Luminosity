package com.github.johnyhuang.mobprogproject

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import kotlin.math.abs

class GameActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private var mSensors: Sensor? = null
    private var lxValue: Float = 0f
    private lateinit var lightBar: ProgressBar
    private var currentBoard: Int = 0
    private var currentStage: Int = 1
    private val boardOneLight: MutableList<MutableList<Char>> = mutableListOf(
        mutableListOf('G', 'B', 'G', 'G', 'G'),
        mutableListOf('B', 'B', 'B', 'B', 'G'),
        mutableListOf('B', 'B', 'B', 'B', 'B'),
        mutableListOf('B', 'B', 'B', 'B', 'G'),
        mutableListOf('B', 'B', 'B', 'B', 'R')
    )

    private val boardOneDark: MutableList<MutableList<Char>> = mutableListOf(
        mutableListOf('G', 'G', 'G', 'B', 'G'),
        mutableListOf('B', 'B', 'B', 'B', 'G'),
        mutableListOf('B', 'B', 'B', 'B', 'G'),
        mutableListOf('B', 'B', 'B', 'B', 'G'),
        mutableListOf('B', 'B', 'B', 'B', 'R')
    )
    private val boardTwoLight: MutableList<MutableList<Char>> = mutableListOf(
        mutableListOf('G', 'B', 'G', 'G', 'G'),
        mutableListOf('B', 'B', 'B', 'B', 'G'),
        mutableListOf('G', 'G', 'G', 'G', 'B'),
        mutableListOf('G', 'B', 'B', 'B', 'B'),
        mutableListOf('B', 'B', 'G', 'G', 'R')
    )
    private val boardTwoDark: MutableList<MutableList<Char>> = mutableListOf(
        mutableListOf('G', 'G', 'G', 'B', 'G'),
        mutableListOf('B', 'B', 'B', 'B', 'G'),
        mutableListOf('G', 'B', 'G', 'G', 'G'),
        mutableListOf('G', 'B', 'B', 'B', 'B'),
        mutableListOf('G', 'G', 'G', 'B', 'R')
    )
    private val boardThreeLight: MutableList<MutableList<Char>> = mutableListOf(
        mutableListOf('G', 'B', 'G', 'B', 'B'),
        mutableListOf('B', 'B', 'G', 'B', 'G'),
        mutableListOf('B', 'B', 'G', 'G', 'G'),
        mutableListOf('B', 'B', 'G', 'B', 'B'),
        mutableListOf('B', 'G', 'G', 'B', 'R')
    )
    private val boardThreeDark: MutableList<MutableList<Char>> = mutableListOf(
        mutableListOf('G', 'G', 'G', 'B', 'B'),
        mutableListOf('B', 'B', 'B', 'B', 'G'),
        mutableListOf('G', 'G', 'G', 'B', 'G'),
        mutableListOf('G', 'B', 'B', 'B', 'G'),
        mutableListOf('G', 'G', 'B', 'B', 'R')
    )


    private val boardList = mutableListOf(boardOneLight, boardOneDark, boardTwoLight, boardTwoDark, boardThreeLight, boardThreeDark)

    private var board: MutableList<MutableList<GameTile>> = ArrayList()

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    //Light sensor function for when light changes
    override fun onSensorChanged(p0: SensorEvent?) {
        val millibarsOfPressure = p0!!.values[0]
        lxValue = millibarsOfPressure

        val lxField = findViewById<TextView>(R.id.lxField)
        lxField.setText(lxValue.toString() + " lx")
        var currentBoardNum = currentBoard
        if(lxValue > 15){
            board = changeBoardState(board, boardList[currentBoardNum])
        }else if(lxValue < 5){
            board = changeBoardState(board, boardList[++currentBoardNum])
        }
        Log.i("light", currentBoard.toString())
        lightBar.progress = (lxValue.toInt() - 5) * 10
    }

    //On create activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Initialize sensor manager
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensors = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        //Initialize UI components and Intent
        lightBar = findViewById<ProgressBar>(R.id.lightBar)
        val intent = Intent(this@GameActivity,MainActivity::class.java)
        val stageText = findViewById<TextView>(R.id.stageText)
        val quitBtn = findViewById<Button>(R.id.quitButton)
        val row1 = findViewById<TableRow>(R.id.row1)
        val row2 = findViewById<TableRow>(R.id.row2)
        val row3 = findViewById<TableRow>(R.id.row3)
        val row4 = findViewById<TableRow>(R.id.row4)
        val row5 = findViewById<TableRow>(R.id.row5)

        val rows = mutableListOf<TableRow>(row1, row2, row3, row4, row5)

        val params = TableRow.LayoutParams(
            200,
            200
        )
        var currentPos = intArrayOf(0,0)

        //Initialize Board
        for(i in 0..4){
            val boardRow: MutableList<GameTile> = ArrayList()
            for(j in 0..4){
                var tile = GameTile(this, i, j, "white")
                when (boardOneDark[i][j]){
                    'G' -> {
                        tile = GameTile(this, i, j, "green")
                        tile.setBackgroundColor(resources.getColor(R.color.green))
                    }
                    'B' -> {
                        tile = GameTile(this, i, j, "black")
                        tile.setBackgroundColor(resources.getColor(R.color.black))
                    }
                    'R' -> {
                        tile = GameTile(this, i, j, "red")
                        tile.setBackgroundColor(resources.getColor(R.color.red))
                    }
                }

                tile.layoutParams = params

                //Set on click listener for each tile on board
                tile.setOnClickListener{
                    if(checkValidMove(tile, currentPos)){
                        board[currentPos[0]][currentPos[1]].setImageResource(android.R.color.transparent)
                        currentPos[0] = tile.xPos
                        currentPos[1] = tile.yPos
                        board[currentPos[0]][currentPos[1]].setImageResource(R.drawable.player_sprite)
                    }
                    //End of Stage reached
                    if(board[currentPos[0]][currentPos[1]].color == "red"){
                        currentBoard+=2
                        currentStage++
                        stageText.setText("Stage " + currentStage)
                        //Game End Reached
                        if(currentStage>3) {
                            Toast.makeText(this, "You win", Toast.LENGTH_LONG).show()
                        } else {
                            board[currentPos[0]][currentPos[1]].setImageResource(android.R.color.transparent)
                            currentPos = intArrayOf(0,0)
                            Toast.makeText(this, "Next Stage", Toast.LENGTH_SHORT).show()
                            board[currentPos[0]][currentPos[1]].setImageResource(R.drawable.player_sprite)
                            board = changeBoardState(board, boardList[currentBoard])
                        }
                    }
                }
                //Update view
                rows[i].addView(tile)
                boardRow.add(tile)
            }
            board.add(boardRow)
        }
        //Quit btn on click listener
        quitBtn.setOnClickListener{
            startActivity(intent)
        }
        //Initialize Player Sprite om board
        board[currentPos[0]][currentPos[1]].setImageResource(R.drawable.player_sprite)
    }

    //Function to check if move is valid
    private fun checkValidMove(tile: GameTile, currentPos: IntArray):Boolean{
        if((abs(tile.xPos - currentPos[0]) == 1 && abs(tile.yPos - currentPos[1]) == 0) ||
            (abs(tile.xPos - currentPos[0]) == 0 && abs(tile.yPos - currentPos[1]) == 1)){
            if(tile.color != "black")
                return true
        }
        return false
    }

    //Function to change board state
    private fun changeBoardState(board: MutableList<MutableList<GameTile>>, boardDesign: MutableList<MutableList<Char>>): MutableList<MutableList<GameTile>>{
        for (i in 0..4){
            for(j in 0..4){
                when (boardDesign[i][j]){
                    'G' -> {
                        board[i][j].color = "green"
                        board[i][j].setBackgroundColor(resources.getColor(R.color.green))
                    }
                    'B' -> {
                        board[i][j].color = "black"
                        board[i][j].setBackgroundColor(resources.getColor(R.color.black))
                    }
                    'R' -> {
                        board[i][j].color = "red"
                        board[i][j].setBackgroundColor(resources.getColor(R.color.red))
                    }
                }
            }
        }

        return board
    }

    //On resume activity to resume sensor manager
    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mSensors, SensorManager.SENSOR_DELAY_NORMAL)
    }


    //On resume activity to resume sensor manager
    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }
}
