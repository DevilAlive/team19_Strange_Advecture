package tw.edu.ntut.csie.game.state;

import java.util.List;
import java.util.Map;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.Audio;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.engine.GameEngine;
import tw.edu.ntut.csie.game.extend.BitmapButton;
import tw.edu.ntut.csie.game.extend.ButtonEventHandler;
import tw.edu.ntut.csie.game.extend.Integer;

public class StateRunTwo extends GameState {
    public static final int DEFAULT_SCORE_DIGITS = 2;
    private MovingBitmap _background;
    private MovingBitmap _goldKey;
    private MovingBitmap _android;
    private MovingBitmap[] _door;

    private Integer _num;
    private BitmapButton _zero;
    private BitmapButton _one;
    private BitmapButton _two;
    private BitmapButton _three;
    private BitmapButton _four;
    private BitmapButton _five;
    private BitmapButton _six;
    private BitmapButton _seven;
    private BitmapButton _eight;
    private BitmapButton _nine;
    private BitmapButton _enter;
    private BitmapButton _keyleft;
    private BitmapButton _keyright;
    private BitmapButton _keyjump;

    private int _ax, _ay;
    private int _position;
    private int _maplevel;
    private int[] _mapfloor;
    private int[] _doorstate;

    private boolean _isChange, _isStart;
    private boolean _isfalling, _isJump;
    private boolean _grabl, _grabr, _grabj;
    private long _time, _timeA;
    private long _goldTime, _goldTimeA;

    private Audio _music;
    private Audio _jumpSound;

    public StateRunTwo(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {
        _background = new MovingBitmap(R.drawable.level2);
        _goldKey = new MovingBitmap(R.drawable.level2_cheat);
        _goldKey.setLocation(0, 0);
        _goldKey.setVisible(false);
        _goldTime = 0;
        _goldTimeA = 0;

        _android = new MovingBitmap(R.drawable.android_black);
        _ax = 40;
        _ay = 200;
        _android.setLocation(_ax, _ay - _android.getWidth());

        _door = new MovingBitmap[6];
        for (int i = 0; i < 6; i++) {
            _door[i] = new MovingBitmap(R.drawable.door_white);
            _door[i].setLocation(50 + 90 * i, 196 - _door[i].getHeight());
            _door[i].setVisible(false);
        }
        _doorstate = new int[] {0, 0, 0, 0, 0, 0};

        _zero = new BitmapButton(R.drawable.zero);
        _one = new BitmapButton(R.drawable.one);
        _two = new BitmapButton(R.drawable.two);
        _three = new BitmapButton(R.drawable.three);
        _four = new BitmapButton(R.drawable.four);
        _five = new BitmapButton(R.drawable.five);
        _six = new BitmapButton(R.drawable.six);
        _seven = new BitmapButton(R.drawable.seven);
        _eight = new BitmapButton(R.drawable.eight);
        _nine = new BitmapButton(R.drawable.nine);
        _enter = new BitmapButton(R.drawable.enter);
        _zero.setLocation(80, 94);
        _one.setLocation(160, 94);
        _two.setLocation(240, 94);
        _three.setLocation(320, 94);
        _four.setLocation(400, 94);
        _five.setLocation(80, 188);
        _six.setLocation(160, 188);
        _seven.setLocation(240, 188);
        _eight.setLocation(320, 188);
        _nine.setLocation(400, 188);
        _enter.setLocation(480, 188);

        _zero.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _num.setValue(_num.getValue() * 10);
            }
        });
        _one.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _num.setValue(_num.getValue() * 10 + 1);
            }
        });
        _two.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _num.setValue(_num.getValue() * 10 + 2);
            }
        });
        _three.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _num.setValue(_num.getValue() * 10 + 3);
            }
        });
        _four.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _num.setValue(_num.getValue() * 10 + 4);
            }
        });
        _five.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _num.setValue(_num.getValue() * 10 + 5);
            }
        });
        _six.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _num.setValue(_num.getValue() * 10 + 6);
            }
        });
        _seven.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _num.setValue(_num.getValue() * 10 + 7);
            }
        });
        _eight.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _num.setValue(_num.getValue() * 10 + 8);
            }
        });
        _nine.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _num.setValue(_num.getValue() * 10 + 9);
            }
        });

        _num = new Integer(DEFAULT_SCORE_DIGITS, 0, 480, 0);

        _keyjump = new BitmapButton(R.drawable.jump1, R.drawable.jump2, 530, 306);
        _keyleft = new BitmapButton(R.drawable.left1, R.drawable.left2, 20, 306);
        _keyright = new BitmapButton(R.drawable.right1, R.drawable.right2, 100, 306);
        _keyjump.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _grabj = false;
            }
        });
        _keyleft.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _grabl = false;
            }
        });
        _keyright.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _grabr = false;
            }
        });
        _keyleft.setVisible(false);
        _keyright.setVisible(false);
        _keyjump.setVisible(false);

        _music = new Audio(R.raw.ntut);
        _music.setRepeating(true);
        _music.play();
        _jumpSound = new Audio(R.raw.mario_jump);

        _mapfloor = new int[] {296, 296, 296, 296, 296, 296, 296, 296, 296, 296, 296, 296, 296, 296, 296, 296};
        _maplevel = 0;
        _time = 0;
        _timeA = 0;

        _grabl = false;
        _grabr = false;
        _grabj = false;
        _isfalling = false;
        _isJump = false;
        _isChange = false;
        _isStart = false;

    }

    @Override
    public void move() {

        if (_isStart){
            _goldTime = System.currentTimeMillis();
            if (_goldTime - _goldTimeA >= 30000) _goldKey.setVisible(true);
        }

        if (_android.getX() > 640 - _android.getWidth()) _position = _ax / 40 - 1;
        else if (_android.getX() >= 40 ) _position = _ax / 40;
        else if (_android.getX() < 40) _position = 0;

        //過關條件
        if (_isStart && _num.getValue() == 32 * _doorstate[0] + 16 * _doorstate[1] + 8 * _doorstate[2] + 4 * _doorstate[3] + 2 * _doorstate[4] + _doorstate[5]) {
            changeState(Game.OVER_STATE);
        }

        //地心引力
        if (_grabj && _ay + _android.getHeight() == _mapfloor[_maplevel + _position] && !_isJump) {
            _timeA = System.currentTimeMillis();
            _isJump = true;
            _isfalling = true;
            _jumpSound.play();
            _isChange = true;
        } else if (_ay + _android.getHeight() < _mapfloor[_maplevel + _position] && !_isfalling) {
            _timeA = System.currentTimeMillis();
            _isfalling = true;
        } else if (_ay + _android.getHeight() > _mapfloor[_maplevel + _position]) {
            _ay = _mapfloor[_maplevel + _position] - _android.getHeight();
            _isfalling = false;
            _isJump = false;
            _time = 1;
        }
        if (_isJump) _ay -= (int)(20.0 / _time);
        if (_isfalling) {
            _time = 1 + (int)((System.currentTimeMillis() - _timeA) / 100);
            _ay += (int)(0.2 * _time * _time);
        }

        //走路
        if (_grabl && !_grabr) _ax -= 4;
        else if (!_grabl && _grabr) _ax += 4;

        //新障礙物地圖
        if (_maplevel + _position == 0 && _ax <= 0) {
            _ax = 0;
        } else if (_maplevel + _position >= 15 && _ax >= 640 - _android.getWidth()) {
            _ax = 640 - _android.getWidth();
        } else if (_maplevel + _position != 0 && _maplevel + _position != 15) {
            if (_mapfloor[_maplevel + _position - 1] < _mapfloor[_maplevel + _position]) {
                if (_ax < _position * 40 && _ay + _android.getHeight() > _mapfloor[_maplevel + _position - 1]) {
                    _ax = _android.getX();
                }
            } else if (_mapfloor[_maplevel + _position + 1] < _mapfloor[_maplevel + _position]) {
                if (_ax + _android.getWidth() > (_position + 1) * 40 && _ay + _android.getHeight() > _mapfloor[_maplevel + _position + 1]) {
                    _ax = _android.getX();
                }
            }
        }

        //門
        if (_ay <= 196 && _isChange) {
            for (int i = 0; i < 6; i++) {
                if ((_door[i].getX() <= _ax && _ax <= _door[i].getX() + _door[i].getWidth()) || (_door[i].getX() <= _ax + _android.getWidth() && _ax + _android.getWidth() <= _door[i].getX() + _door[i].getWidth())) {
                    if (_doorstate[i] == 1) {
                        _door[i].loadBitmap(R.drawable.door_white);
                        _doorstate[i] = 0;
                    } else {
                        _door[i].loadBitmap(R.drawable.door_black);
                        _doorstate[i] = 1;
                    }
                }
            }
            _isChange = false;
        }

        _android.setLocation(_ax, _ay);
    }

    @Override
    public void show() {
        // �I�s���Ǭ��K�϶���
        _background.show();
        for (int i = 0; i < 6; i++) _door[i].show();
        _goldKey.show();
        _android.show();
        _keyleft.show();
        _keyright.show();
        _keyjump.show();
        _zero.show();
        _one.show();
        _two.show();
        _three.show();
        _four.show();
        _five.show();
        _six.show();
        _seven.show();
        _eight.show();
        _nine.show();
        _enter.show();
        _num.show();
    }

    @Override
    public void release() {
        _background.release();
        for (int i = 0; i < 6; i++) _door[i].release();
        _goldKey.release();
        _android.release();
        _music.release();
        _keyleft.release();
        _keyright.release();
        _keyjump.release();
        _zero.release();
        _one.release();
        _two.release();
        _three.release();
        _four.release();
        _five.release();
        _six.release();
        _seven.release();
        _eight.release();
        _nine.release();
        _enter.release();
        _num.release();
    }

    @Override
    public void keyPressed(int keyCode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyReleased(int keyCode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void orientationChanged(float pitch, float azimuth, float roll) {
    }

    @Override
    public void accelerationChanged(float dX, float dY, float dZ) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean pointerPressed(List<Pointer> pointers) {
        if (_enter.pointerPressed(pointers) && _num.getValue() != 0) {
            _zero.setVisible(false);
            _one.setVisible(false);
            _two.setVisible(false);
            _three.setVisible(false);
            _four.setVisible(false);
            _five.setVisible(false);
            _six.setVisible(false);
            _seven.setVisible(false);
            _eight.setVisible(false);
            _nine.setVisible(false);
            _enter.setVisible(false);
            _isStart = true;
            for (int i = 0; i < 6; i++) _door[i].setVisible(true);
            _keyleft.setVisible(true);
            _keyright.setVisible(true);
            _keyjump.setVisible(true);
            _goldTimeA = System.currentTimeMillis();
        } else {
            _zero.pointerPressed(pointers);
            _one.pointerPressed(pointers);
            _two.pointerPressed(pointers);
            _three.pointerPressed(pointers);
            _four.pointerPressed(pointers);
            _five.pointerPressed(pointers);
            _six.pointerPressed(pointers);
            _seven.pointerPressed(pointers);
            _eight.pointerPressed(pointers);
            _nine.pointerPressed(pointers);
        }
        return true;
    }

    @Override
    public boolean pointerMoved(List<Pointer> pointers) {
        if (pointers.size() <= 3) {
            _grabj = _keyjump.pointerPressed(pointers);
            _grabl = _keyleft.pointerPressed(pointers);
            _grabr = _keyright.pointerPressed(pointers);
        }
        return false;
    }

    @Override
    public boolean pointerReleased(List<Pointer> pointers) {
        _zero.pointerReleased(pointers);
        _one.pointerReleased(pointers);
        _two.pointerReleased(pointers);
        _three.pointerReleased(pointers);
        _four.pointerReleased(pointers);
        _five.pointerReleased(pointers);
        _six.pointerReleased(pointers);
        _seven.pointerReleased(pointers);
        _eight.pointerReleased(pointers);
        _nine.pointerReleased(pointers);
        _enter.pointerReleased(pointers);
        _keyjump.pointerReleased(pointers);
        _keyleft.pointerReleased(pointers);
        _keyright.pointerReleased(pointers);
        return false;
    }

    @Override
    public boolean pointerMultiReleased(List<Pointer> pointers) {
        _zero.pointerReleased(pointers);
        _one.pointerReleased(pointers);
        _two.pointerReleased(pointers);
        _three.pointerReleased(pointers);
        _four.pointerReleased(pointers);
        _five.pointerReleased(pointers);
        _six.pointerReleased(pointers);
        _seven.pointerReleased(pointers);
        _eight.pointerReleased(pointers);
        _nine.pointerReleased(pointers);
        _enter.pointerReleased(pointers);
        _keyjump.pointerReleased(pointers);
        _keyleft.pointerReleased(pointers);
        _keyright.pointerReleased(pointers);
        return false;
    }

    @Override
    public void pause() {
        _music.pause();
    }

    @Override
    public void resume() {
        _music.resume();
    }
}
