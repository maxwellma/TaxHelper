package com.maxwell.taxhelper.busevent

/**
 * Created by maxwellma on 20/11/2017.
 */
class BuchonggjjEvent(value: Int) {

    var params: Int = 0
        private set(value) {field = value}

    init {
        params = value
    }
}