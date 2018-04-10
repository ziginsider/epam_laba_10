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
     * <p><tt>true</tt> - open
     * <p><tt>false</tt> - close
     */
    fun setLeftEyeOpen(state: Boolean)

    /**
     * @return <tt>true</tt> if left eye open, <tt>false</tt> if left eye close
     */
    fun isLeftEyeOpen(): Boolean

    /**
     * Set right eye open state
     * <p><tt>true</tt> - open
     * <p><tt>false</tt> - close
     */
    fun setRightEyeOpen(state: Boolean)

    /**
     * @return <tt>true</tt> if right eye open, <tt>false</tt> if right eye close
     */
    fun isRightEyeOpen(): Boolean

    /**
     * Set smiley state
     * <p><tt>true</tt> - happy
     * <p><tt>false</tt> - sad
     */
    fun setSmileState(state: Boolean)

    /**
     * @return <tt>true</tt> if the smiley is happy, <tt>false</tt> if the smiley is sad
     */
    fun isSmileHappy():Boolean
}
