package console.screen

/**
 * Some text graphics, taken from http://en.wikipedia.org/wiki/Codepage_437
 * but converted to its UTF-8 counterpart.
 * @author martin
 */
object ASCII {
  // single box drawing chars
  val HLINE: Char = 0x2500
  val VLINE: Char = 0x2502
  val ULCORNER: Char = 0x250C
  val URCORNER: Char = 0x2510
  val LLCORNER: Char = 0x2514
  val LRCORNER: Char = 0x2518

  // double box drawing chars
  val DOUBLE_HLINE: Char = 0x2550
  val DOUBLE_VLINE: Char = 0x2551
  val DOUBLE_ULCORNER: Char = 0x2554
  val DOUBLE_URCORNER: Char = 0x2557
  val DOUBLE_LLCORNER: Char = 0x255A
  val DOUBLE_LRCORNER: Char = 0x255D

  val FACE_WHITE: Char = 0x263A
  val FACE_BLACK: Char = 0x263B
  val HEART: Char = 0x2665
  val CLUB: Char = 0x2663
  val DIAMOND: Char = 0x2666
  val SPADES: Char = 0x2660
  val DOT: Char = 0x2022
  val ARROW_UP: Char = 0x2191
  val ARROW_DOWN: Char = 0x2193
  val ARROW_RIGHT: Char = 0x2192
  val ARROW_LEFT: Char = 0x2190
  val BLOCK_SOLID: Char = 0x2588
  val BLOCK_DENSE: Char = 0x2593
  val BLOCK_MIDDLE: Char = 0x2592
  val BLOCK_SPARSE: Char = 0x2591
  val SINGLE_LINE_HORIZONTAL: Char = HLINE
  val DOUBLE_LINE_HORIZONTAL: Char = DOUBLE_HLINE
  val SINGLE_LINE_VERTICAL: Char = VLINE
  val DOUBLE_LINE_VERTICAL: Char = DOUBLE_VLINE
  val SINGLE_LINE_UP_LEFT_CORNER: Char = ULCORNER
  val DOUBLE_LINE_UP_LEFT_CORNER: Char = DOUBLE_ULCORNER
  val SINGLE_LINE_UP_RIGHT_CORNER: Char = URCORNER
  val DOUBLE_LINE_UP_RIGHT_CORNER: Char = DOUBLE_URCORNER
  val SINGLE_LINE_LOW_LEFT_CORNER: Char = LLCORNER
  val DOUBLE_LINE_LOW_LEFT_CORNER: Char = DOUBLE_LLCORNER
  val SINGLE_LINE_LOW_RIGHT_CORNER: Char = LRCORNER
  val DOUBLE_LINE_LOW_RIGHT_CORNER: Char = DOUBLE_LRCORNER
  val SINGLE_LINE_CROSS: Char = 0x253C
  val DOUBLE_LINE_CROSS: Char = 0x256C
  val SINGLE_LINE_T_UP: Char = 0x2534
  val SINGLE_LINE_T_DOWN: Char = 0x252C
  val SINGLE_LINE_T_RIGHT: Char = 0x251c
  val SINGLE_LINE_T_LEFT: Char = 0x2524
  val SINGLE_LINE_T_DOUBLE_UP: Char = 0x2568
  val SINGLE_LINE_T_DOUBLE_DOWN: Char = 0x2565
  val SINGLE_LINE_T_DOUBLE_RIGHT: Char = 0x255E
  val SINGLE_LINE_T_DOUBLE_LEFT: Char = 0x2561
  val DOUBLE_LINE_T_UP: Char = 0x2569
  val DOUBLE_LINE_T_DOWN: Char = 0x2566
  val DOUBLE_LINE_T_RIGHT: Char = 0x2560
  val DOUBLE_LINE_T_LEFT: Char = 0x2563
  val DOUBLE_LINE_T_SINGLE_UP: Char = 0x2567
  val DOUBLE_LINE_T_SINGLE_DOWN: Char = 0x2564
  val DOUBLE_LINE_T_SINGLE_RIGHT: Char = 0x255F
  val DOUBLE_LINE_T_SINGLE_LEFT: Char = 0x2562
}



