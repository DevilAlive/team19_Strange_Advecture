package tw.edu.ntut.csie.game.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.Audio;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.engine.GameEngine;
import tw.edu.ntut.csie.game.extend.Animation;
import tw.edu.ntut.csie.game.extend.BitmapButton;
import tw.edu.ntut.csie.game.extend.ButtonEventHandler;
import tw.edu.ntut.csie.game.extend.Integer;

public class StateRunThree extends GameState {
    private MovingBitmap _background;
    private MovingBitmap _android;
    private MovingBitmap _cloud;
    private MovingBitmap _door;
    private MovingBitmap _message;

    private BitmapButton _keyl;
    private BitmapButton _keyr;
    private BitmapButton _keyj;

    private int _ax, _ay;
    private int _cx, _cy;
    private int _position;
    private int _maplevel;
    private int[] _mapfloor;
    private int _goldKey2;

    private boolean _isfalling, _isJump;
    private boolean _grabl, _grabr, _grabj;
    private boolean _goldKey;
    private long _time, _timeA;

    private Audio _music;
    private Audio _jumpSound;
    private Audio _dieSound;

    public StateRunThree(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {
        _background = new MovingBitmap(R.drawable.level1);
        _message = new MovingBitmap(R.drawable.message, 92, 150);

        _android = new MovingBitmap(R.drawable.android_black);
        _ax = 40;
        _ay = 200;
        _android.setLocation(_ax, _ay - _android.getWidth());

        _cloud = new MovingBitmap(R.drawable.cloud);
        _cx = 100;
        _cy = 50;
        _cloud.setLocation(_cx, _cy);

        _door = new MovingBitmap(R.drawable.water_tower);
        _door.setLocation(100 - _door.getWidth(), 227);
        _door.setVisible(false);

        _keyj = new BitmapButton(R.drawable.jump1, R.drawable.jump2, 530, 306);
        _keyl = new BitmapButton(R.drawable.left1, R.drawable.left2, 20, 306);
        _keyl.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _grabl = false;
            }
        });
        _keyr = new BitmapButton(R.drawable.right1, R.drawable.right2, 100, 306);
        _keyr.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _grabr = false;
            }
        });

        _music = new Audio(R.raw.peacherine_rag);
        _music.setRepeating(true);
        _music.play();
        _jumpSound = new Audio(R.raw.mario_jump);
        _dieSound = new Audio(R.raw.mario_die);

        _mapfloor = new int[] { 296, 296, 227, 296, 296, 296, 296, 296, 296, 296, 400, 400, 400, 400, 400, 400,
                                176, 216, 256, 296, 296 ,296, 400, 400, 296, 296, 296, 296, 296, 296, 296, 296,
                                296, 296, 296, 296, 296, 296, 296, 296, 400, 400, 296, 296, 296, 256, 216, 176,
                                400, 400, 400, 400, 400, 400, 296, 296, 296, 296, 296, 296, 296, 227, 296, 296};
        _maplevel = 32;
        _goldKey2 = 0;
        _position = 0;
        _time = 0;
        _timeA = 0;

        _grabl = false;
        _grabr = false;
        _grabj = true;
        _goldKey = false;
        _isfalling = false;
        _isJump = false;
    }

    @Override
    public void move() {
        if (_goldKey2 == 30) {
            _background = new MovingBitmap(R.drawable.level3_2);
            _ax = 320;
            _ay = 296 - _android.getHeight();
            _door.setLocation(100 - _door.getWidth(), 227);
            _door.setVisible(true);
            _maplevel = 0;
            _goldKey2++;
        }
        else if (_goldKey2 == 50) changeState(Game.OVER_STATE);

        //死亡&過關條件
        if (_ax + _android.getWidth() / 2 > _door.getX() && _ax < _door.getX() + _door.getWidth() / 2 &&
                _ay + _android.getHeight() / 2 > _door.getY() && _ay < _door.getY() + _door.getHeight() / 2 && _maplevel == 0) {
            changeState(Game.OVER_STATE);
        } else if (_ax + _android.getWidth() >= _cloud.getX() && _ay <= _cloud.getY() + _cloud.getHeight() && _ax <= _cloud.getX() + _cloud.getWidth() && _ay + _android.getHeight() >= _cloud.getY()) {
            _dieSound.play();
            changeState(Game.END_STATE);
        } else if (_ay + _android.getHeight() / 2 >= 376) {
            _dieSound.play();
            changeState(Game.END_STATE);
        }

        if (_goldKey) {
            //走路
            if (_grabl && !_grabr) _ax -= 4;
            else if (!_grabl && _grabr) _ax += 4;
        }
        else {
            //走路
            if (_grabl && !_grabr) _ax += 4;
            else if (!_grabl && _grabr) _ax -= 4;
        }

        //新障礙物地圖
        if (_maplevel + _position <= 0 && _ax <= 0) {
            _ax = 0;
        } else if (_maplevel + _position >= 63 && _ax >= 640 - _android.getWidth()) {
            _ax = 640 - _android.getWidth();
        } else if (_maplevel + _position != 0 && _maplevel + _position != 63) {
            if (_mapfloor[_maplevel + _position - 1] < _mapfloor[_maplevel + _position]) {
                if (_ax < _position * 40 && _ay + _android.getHeight() > _mapfloor[_maplevel + _position - 1]) {
                    _ax = _android.getX();
                }
            } else if (_mapfloor[_maplevel + _position + 1] < _mapfloor[_maplevel + _position]) {
                if (_ax + _android.getWidth() >= (_position + 1) * 40 && _ay + _android.getHeight() > _mapfloor[_maplevel + _position + 1]) {
                    _ax = _android.getX();
                }
            }
        }

        if (_android.getX() > 640 - _android.getWidth()) _position = _ax / 40 - 1;
        else if (_android.getX() >= 40 ) _position = _ax / 40;
        else if (_android.getX() < 40) _position = 0;

        //地心引力
        for (int i = 0; i < 2; i++) {
            if (_grabj && _ay + _android.getHeight() == _mapfloor[_maplevel + _position + i] && !_isJump) {
                _timeA = System.currentTimeMillis();
                _isJump = true;
                _isfalling = true;
                _jumpSound.play();
                break;
            } else if (_ay + _android.getHeight() < _mapfloor[_maplevel + _position + i] && !_isfalling) {
                _timeA = System.currentTimeMillis();
                _isfalling = true;
                break;
            } else if (_ay + _android.getHeight() > _mapfloor[_maplevel + _position + i]) {
                _ay = _mapfloor[_maplevel + _position + i] - _android.getHeight();
                _isfalling = false;
                _isJump = false;
                _time = 1;
                break;
            }
        }
        if (_isJump) _ay -= (int)(20.0 / _time);
        if (_isfalling) {
            _time = 1 + (int)((System.currentTimeMillis() - _timeA) / 100);
            _ay += (int)(0.2 * _time * _time);
        }

        //第一張圖
        if (_maplevel == 0) {
            // 到第二張圖
            if (_android.getX() + (_android.getWidth() / 2) > 640) {
                _background = new MovingBitmap(R.drawable.level3);
                _ax = - (_android.getWidth() / 2);
                _door.setVisible(false);
                _maplevel = 16;
            }
        } else if(_maplevel == 16) {
            //到第一張圖
            if (_ax + (_android.getWidth() / 2) < 0) {
                _background = new MovingBitmap(R.drawable.level3_2);
                _ax = 640 - (_android.getWidth() / 2);
                _door.setLocation(100 - _door.getWidth(), 227);
                _door.setVisible(true);
                _maplevel = 0;
            }
            //到第三張圖
            else if (_android.getX() + (_android.getWidth() / 2) > 640) {
                _background = new MovingBitmap(R.drawable.level1);
                _ax = - (_android.getWidth() / 2);
                _door.setVisible(false);
                _maplevel = 32;
            }
        } else if(_maplevel == 32) {
            // 到第二張圖
            if (_ax + (_android.getWidth() / 2) < 0) {
                _background = new MovingBitmap(R.drawable.level3);
                _ax = 640 - (_android.getWidth() / 2);
                _door.setVisible(false);
                _maplevel = 16;
            }
            //到第四張圖
            else if (_android.getX() + (_android.getWidth() / 2) > 640) {
                _background = new MovingBitmap(R.drawable.level1_2);
                _ax = - (_android.getWidth() / 2);
                _door.setLocation(540, 227);
                _door.setVisible(true);
                _maplevel = 48;
            }
        } else if (_maplevel == 48) {
            // 到第三張圖
            if (_ax + (_android.getWidth() / 2) < 0) {
                _background = new MovingBitmap(R.drawable.level1);
                _ax = 640 - (_android.getWidth() / 2);
                _door.setVisible(false);
                _maplevel = 32;
            }
        }

        _cloud.setLocation(_cx, _cy);
        _android.setLocation(_ax, _ay);
        if (_goldKey) _grabj = false;
        else _grabj = true;

    }

    @Override
    public void show() {
        // �I�s���Ǭ��K�϶���
        _background.show();
        _message.show();
        _android.show();
        _door.show();
        _cloud.show();
        _keyj.show();
        _keyl.show();
        _keyr.show();
    }

    @Override
    public void release() {
        _background.release();
        _android.release();
        _cloud.release();
        _message.release();
        _music.release();
        _door.release();
        _keyj.release();
        _keyl.release();
        _keyr.release();
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
        if (pitch > 5 && pitch < 90 && _cx > 50) _cx -= 2;
        else if (pitch < -5 && pitch > -90 && _cx + _cloud.getWidth() < 590) _cx += 2;
    }

    @Override
    public void accelerationChanged(float dX, float dY, float dZ) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean pointerPressed(List<Pointer> pointers) {
        _message.setVisible(false);
        for (int i = 0; i < pointers.size(); i++) {
            if (    _cloud.getX() < pointers.get(i).getX() && pointers.get(i).getX() < _cloud.getX() + _cloud.getWidth() &&
                    _cloud.getY() < pointers.get(i).getY() && pointers.get(i).getY() < _cloud.getY() + _cloud.getHeight()) {
                _goldKey2++;
                if (_goldKey) {
                    _goldKey = false;
                }
                else {
                    _goldKey = true;
                }
            }
        }
        if (pointers.size() <= 3) {
            if (_goldKey) _grabj = _keyj.pointerPressed(pointers);
            else _grabj =  !_keyj.pointerPressed(pointers);
            _grabl = _keyl.pointerPressed(pointers);
            _grabr = _keyr.pointerPressed(pointers);
            if (_grabj || _grabl || _grabr) return true;
        }
        return false;
    }

    @Override
    public boolean pointerMoved(List<Pointer> pointers) {
        if (pointers.size() <= 3) {
            if (_goldKey) _grabj = _keyj.pointerPressed(pointers);
            else _grabj =  !_keyj.pointerPressed(pointers);
            _grabl = _keyl.pointerPressed(pointers);
            _grabr = _keyr.pointerPressed(pointers);
            if (!_grabj || _grabl || _grabr) return true;
        }
        return false;
    }

    @Override
    public boolean pointerReleased(List<Pointer> pointers) {
        _keyj.pointerReleased(pointers);
        _keyl.pointerReleased(pointers);
        _keyr.pointerReleased(pointers);
        return false;
    }

    @Override
    public boolean pointerMultiReleased(List<Pointer> pointers) {
        _keyj.pointerMultiReleased(pointers);
        _keyl.pointerMultiReleased(pointers);
        _keyr.pointerMultiReleased(pointers);
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