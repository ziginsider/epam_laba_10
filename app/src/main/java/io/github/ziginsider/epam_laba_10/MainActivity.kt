package io.github.ziginsider.epam_laba_10

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

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

        val rnd = Random()
        Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        bottomNavigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.setColor -> {
                    emojiSmile.setColor(Color.rgb(
                            rnd.nextInt(256),
                            rnd.nextInt(256),
                            rnd.nextInt(256)))
                }
                R.id.changeEyes -> {
                    if (emojiSmile.isLeftEyeOpen()) {
                        emojiSmile.setLeftEyeOpen(false)
                        emojiSmile.setRightEyeOpen(false)
                    } else {
                        emojiSmile.setLeftEyeOpen(true)
                        emojiSmile.setRightEyeOpen(true)
                    }
                }
                R.id.changeSmile -> {
                    if (emojiSmile.isSmileHappy()) emojiSmile.setSmileState(false)
                    else emojiSmile.setSmileState(true)
                }
            }
            true
        }
    }
}
