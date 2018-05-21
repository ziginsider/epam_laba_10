package io.github.ziginsider.epam_laba_10

/**
 * An interface for a view implementing an emoji smiley
 *
 * @since 2018-04-10
 * @author Alex Kisel
 */
interface EmojiSmiley {
    /**
     * Set color for emoji head
     */
    fun setColor(color: Int)

    /**
     * Set left eye open state
     * - **true** - open
     * - **false** - close
     */
    fun setEyesOpenState(state: Boolean)

    /**
     * @return **true** if left eye open, **false** if left eye close
     */
    fun areEyesOpen(): Boolean

    /**
     * Set smiley state
     * - **true** - happy
     * - **false** - sad
     */
    fun setSmileState(state: Boolean)

    /**
     * @return **true** if the smiley is happy, **false** if the smiley is sad
     */
    fun isSmileHappy(): Boolean
}
