package com.github.johnyhuang.mobprogproject

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

//Game Tile class
class GameTile: ImageView {

    var xPos: Int = 0
    var yPos: Int = 0
    var color: String = ""
    constructor(context: Context, _xPos: Int, _yPos: Int, _color: String): super(context){
        xPos = _xPos
        yPos = _yPos
        color = _color
    }


}