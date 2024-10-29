

package com.pick.color.color.flag;

import com.canvas.paint.color.flag.FlagView;

/** FlagMode decides the {@link FlagView}'s visibility action. */
public enum FlagMode {
  /** showing always by tapping and dragging. */
  ALWAYS,

  /** showing only when finger released. */
  LAST,

  /** showing only when finger tapping with fadeIn-fadeOut animations. */
  FADE
}
