package io.github.ziginsider.epam_laba_10

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import io.github.ziginsider.epam_laba_10.utils.randomColor
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Activity that displays custom view emoji smiley and implements navigation bottom view
 * to change custom view's attributes
 *
 * @since 2018-04-10
 * @author Alex Kisel
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.setColor -> {
                    emojiSmile.colorHead = randomColor()
                }
                R.id.changeEyes -> {
                    emojiSmile.openEyesState = !emojiSmile.openEyesState
                }
                R.id.changeSmile -> {
                    emojiSmile.reverseSmile()
                }
            }
            true
        }
    }
}
