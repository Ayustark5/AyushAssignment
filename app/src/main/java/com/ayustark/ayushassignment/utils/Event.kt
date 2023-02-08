package com.ayustark.ayushassignment.utils

class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set //we can change the value from within this class and not outside

    /**
     * hasBeenHandled is private set, can't be changed from outside the scope of this class
     * This method checks for that, and toggles
     * @return returns the content exactly once
     * */
    fun getContentIfNotHandled() = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    fun peekContent() = content

}