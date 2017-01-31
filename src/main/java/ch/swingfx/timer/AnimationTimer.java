/*
 * This library is dual-licensed: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation. For the terms of this
 * license, see licenses/gpl_v3.txt or <http://www.gnu.org/licenses/>.
 *
 * You are free to use this library under the terms of the GNU General
 * Public License, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * Alternatively, you can license this library under a commercial
 * license, as set out in licenses/commercial.txt.
 */

package ch.swingfx.timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Class for simple animation. Example:<br />
 * <pre>
 * AnimationTimer at = new AnimationTimer(FrameRate.FPS_25);
 * at.setDuration(2500); // 2500 milliseconds
 * at.setAnimationTarget(new AnimationTarget() {
 * 		public void event(AnimationTimer timer, float fraction) {
 *			// between begin and end.
 *			// fraction represents the time passed (0f = 0%, 1f = 100%)
 *		}
 *		public void begin(AnimationTimer timer) {
 *			// start of the animation			
 *		}
 *		public void end(AnimationTimer timer) {
 *			// end of the animation
 *		}
 * });
 * at.start();
 * </pre>
 * @author Heinrich Spreiter
 */
public class AnimationTimer {
	/**
	 * Frame rates for the {@link AnimationTimer}<br />
	 * Note: These frame rates are approximations.
	 * @author Heinrich Spreiter
	 */
	public enum FrameRate {
		FPS_15(66),
		FPS_20(50),
		FPS_25(40),
		FPS_30(33),
		FPS_40(25),
		FPS_50(20)
		;
		
		/** The delay for the timer */
		private final int fDelay;
		FrameRate(int delay) {
			fDelay = delay;
		}
		
		public int delay() {
			return fDelay;
		}
	}
	
	/** The timer for the animation */
	private final Timer fTimer;
	/** Animation events get sent to this target */
	private AnimationTarget fAnimationTarget;
	/** Stores the start time of the animation so we can calculate the fraction */
	private long fAnimationStartTime;
	/** Duration of the animation in milliseconds */
	private long fDuration;

	/**
	 * Creates an {@link AnimationTimer} with a default frame rate of 25
	 */
	public AnimationTimer() {
		this(FrameRate.FPS_25);
	}
	
	/**
	 * Creates an {@link AnimationTimer} with an specified frame rate
	 * @param fps {@link FrameRate} we want to use for this {@link AnimationTimer}
	 */
	public AnimationTimer(FrameRate fps) {
        fTimer = new Timer(fps.delay(), new TimerActionListener());
	}
	
	/**
	 * Set the duration of the animation
	 * @param duration the duration of the animation
	 */
	public void setDuration(long duration) {
		fDuration = duration;
	}
	
	/**
	 * Start the animation
	 */
	public void start() {
		if(!fTimer.isRunning()) {
			if(fAnimationTarget == null) {
				throw new IllegalStateException("Animation target is not set!");
			}
			fAnimationStartTime = System.nanoTime() / 1000000;
			final Runnable r = new Runnable() {
				public void run() {
					fAnimationTarget.begin(AnimationTimer.this);
				}
			};
			if(SwingUtilities.isEventDispatchThread()) {
				r.run();
			} else {
				SwingUtilities.invokeLater(r);
			}
			fTimer.start();
		}
	}
	
	/**
	 * End the animation before duration has passed
	 */
	public void stop() {
		if(fTimer.isRunning()) {
			fTimer.stop();
			final Runnable r = new Runnable() {
				public void run() {
					fAnimationTarget.end(AnimationTimer.this);
				}
			};
			if(SwingUtilities.isEventDispatchThread()) {
				r.run();
			} else {
				SwingUtilities.invokeLater(r);
			}
		}
	}
	
	/**
	 * Set the animation target
	 * @param target the animation target
	 */
	public void setAnimationTarget(AnimationTarget target) {
		this.fAnimationTarget = target;
	}
	
	/**
	 * Handles the calculation of the time that has elapsed since the start
	 * and sends it to the {@link AnimationTarget} 
	 * @author Heinrich Spreiter
	 *
	 */
	private class TimerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			final long currentAnimationTime = System.nanoTime() / 1000000;
			final long totalTime = currentAnimationTime - fAnimationStartTime;
			if(totalTime > fDuration) {
				fAnimationStartTime = totalTime;
			}
			float fraction = (float) totalTime / fDuration;
			fraction = Math.min(1f, fraction);
			if(fraction == 1f) {
				stop();
			} else {
				fAnimationTarget.event(AnimationTimer.this, fraction);
			}
		}
	}
	
	/**
	 * Used with {@link AnimationTimer}.setAnimationTarget().<br />
	 * Implement this interface to receive events from the {@link AnimationTimer}
	 * @author Heinrich Spreiter
	 *
	 */
	public interface AnimationTarget {
		/**
		 * Fired only once when the animation starts
		 * @param timer the timer that fired the event
		 */
		public void begin(AnimationTimer timer);
		/**
		 * Fired when the animation is running
		 * @param timer the timer that fired the event
		 * @param fraction value between 0f and 1f that represents the time that has ellapsed
		 */
		public void event(AnimationTimer timer, float fraction);
		/**
		 * Fired only once when the animation stopped
		 * @param timer the timer that fired the event
		 */
		public void end(AnimationTimer timer);
	}
}
