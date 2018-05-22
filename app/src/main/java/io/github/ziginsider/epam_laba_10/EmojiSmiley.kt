package io.github.ziginsider.epam_laba_10

/**
 * An interface for a view implementing an emoji smiley
 *
 * @since 2018-04-10
 * @author Alex Kisel
 */
interface EmojiSmiley {
    /**
     * sets sad if the smiley is happy, sets happy if the smiley is sad
     */
    fun reverseSmile()

    /**
     * Head Color of Emoji
     */
    var colorHead: Int

    /**
     * Keeps eyes state
     */
    var openEyesState: Boolean

    /**
     * Keeps smile state
     */
    var smile: Int

}
