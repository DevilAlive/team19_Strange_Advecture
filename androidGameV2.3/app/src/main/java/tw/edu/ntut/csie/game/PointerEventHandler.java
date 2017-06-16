package tw.edu.ntut.csie.game;

import java.util.List;

/**
 * 對指標裝置(滑鼠或觸控螢幕)事件有興趣的類別，可以實作<code>PointerEventHandler</code>
 * 介面來接收及處理遊戲引擎轉送的指標裝置事件。
 *
 * @author <a href="http://www.csie.ntut.edu.tw/labsdtl/">Lab SDT</a>
 * @version 2.0
 * @since 2.0
 */
public interface PointerEventHandler {

    /**
     * 處理指標被按下的事件(滑鼠被按下或是手指按在觸控螢幕上)。
     *
     * @param pointers 指標點目前的座標
     * @return true表示該事件已經被處理完畢，不用再繼續傳遞下去。
     */
    public boolean pointerPressed(List<Pointer> pointers);

    /**
     * 處理指標移動的事件(滑鼠移動或是手指按在觸控螢幕移動)。
     *
     * @param pointers 指標點目前的座標
     * @return true表示該事件已經被處理完畢，不用再繼續傳遞下去。
     */
    public boolean pointerMoved(List<Pointer> pointers);

    /**
     * 處理單個指標點放開的事件(滑鼠被放開或是手指離開螢幕)。
     *
     * @param pointers 指標被放開的座標
     * @return true表示該事件已經被處理完畢，不用再繼續傳遞下去。
     */
    public boolean pointerReleased(List<Pointer> pointers);

    /**
     * 處理多個指標點放開的事件(滑鼠被放開或是手指離開螢幕)。
     *
     * @param pointers 指標被放開的座標
     * @return true表示該事件已經被處理完畢，不用再繼續傳遞下去。
     */
    public boolean pointerMultiReleased(List<Pointer> pointers);
}
