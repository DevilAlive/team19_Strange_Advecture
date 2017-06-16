package tw.edu.ntut.csie.game.state;

import java.util.Map;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.Audio;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.engine.GameEngine;
import tw.edu.ntut.csie.game.extend.BitmapButton;
import tw.edu.ntut.csie.game.extend.ButtonEventHandler;

public class StateReady extends AbstractGameState {

    private MovingBitmap _levelInfo;
    private MovingBitmap _helpInfo;
    private MovingBitmap _aboutInfo;
    private MovingBitmap _background;
    private MovingBitmap _cheatInfo;

    private BitmapButton _cheatButton;
    private BitmapButton _exitButton;
    private BitmapButton _helpButton;
    private BitmapButton _menuButton;
    private BitmapButton _aboutButton;
    private BitmapButton _startButton;
    private BitmapButton _oneButton;
    private BitmapButton _twoButton;
    private BitmapButton _threeButton;

    private boolean _showLevel;
    private boolean _showHelp;
    private boolean _showAbout;
    private boolean _showCheat;

    private Audio _music;

    public StateReady(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {
        addGameObject(_cheatInfo = new MovingBitmap(R.drawable.cheat_picture));
        addGameObject(_levelInfo = new MovingBitmap(R.drawable.select_level));
        addGameObject(_helpInfo = new MovingBitmap(R.drawable.help_info));
        addGameObject(_background = new MovingBitmap(R.drawable.state_ready));
        addGameObject(_aboutInfo = new MovingBitmap(R.drawable.about_info));
        initializeCheatButton();
        initializeStartButton();
        initializeExitButton();
        initializeMenuButton();
        initializeHelpButton();
        initializeAboutButton();
        setVisibility(false, false, false, false);
        _music = new Audio(R.raw.ntut);
        _music.setRepeating(true);
        _music.play();
    }

    /**
     * ��l�ơyAbout�z�����s�C
     */
    // �}�o²��
    private void initializeAboutButton() {
        addGameObject(_aboutButton = new BitmapButton(R.drawable.about, R.drawable.button_pressed, 465, 270));
        _aboutButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                setVisibility(false, false, true, false);
            }
        });
        addPointerEventHandler(_aboutButton);
    }

    /**
     * ��l�ơyHelp�z�����s�C
     */
    // �C������
    private void initializeHelpButton() {
        addGameObject(_helpButton = new BitmapButton(R.drawable.help, R.drawable.button_pressed, 465, 220));
        _helpButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                setVisibility(false, true, false, false);
            }
        });
        addPointerEventHandler(_helpButton);
    }

    /**
     * ��l�ơyMenu�z�����s�C
     */
    private void initializeMenuButton() {
        addGameObject(_menuButton = new BitmapButton(R.drawable.menu, R.drawable.button_pressed, 465, 17));
        _menuButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                setVisibility(false, false, false, false);
            }
        });
        addPointerEventHandler(_menuButton);
    }

    /**
     * ��l�ơyExit�z�����s�C
     */
    private void initializeExitButton() {
        addGameObject(_exitButton = new BitmapButton(R.drawable.exit, R.drawable.button_pressed, 465, 320));
        _exitButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _music.release();
                _engine.exit();
            }
        });
        addPointerEventHandler(_exitButton);
    }

    /**
     * ��l�ơyStart�z�����s�C
     */
    private void initializeStartButton() {
        addGameObject(_startButton = new BitmapButton(R.drawable.start, R.drawable.button_pressed, 465, 170));
        addGameObject(_oneButton = new BitmapButton(R.drawable.select_level_1, 50, 80));
        addGameObject(_twoButton = new BitmapButton(R.drawable.select_level_2, 250, 180));
        addGameObject(_threeButton = new BitmapButton(R.drawable.select_level_3, 450, 100));
        _startButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                setVisibility(true, false, false, false);
            }
        });
        _oneButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _music.release();
                changeState(Game.RUNNINGONE_STATE);
            }
        });
        _twoButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _music.release();
                changeState(Game.RUNNINGTWO_STATE);
            }
        });
        _threeButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _music.release();
                changeState(Game.RUNNINGTHREE_STATE);
            }
        });
        addPointerEventHandler(_startButton);
        addPointerEventHandler(_oneButton);
        addPointerEventHandler(_twoButton);
        addPointerEventHandler(_threeButton);
    }

    /**
     * ��l�ơyCheat�z�����s�C
     */
    // �}�o²��
    private void initializeCheatButton() {
        addGameObject(_cheatButton = new BitmapButton(R.drawable.cheat, 480, 25));
        _cheatButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                setVisibility(false, false, false, true);
            }
        });
        addPointerEventHandler(_cheatButton);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    /**
     * �]�w�e���W���ǹϤ�����ܡA���ǹϤ������áC
     *
     * @param showHelp  ���Help�e��
     * @param showAbout ���About�e��
     */
    private void setVisibility(boolean showLevel, boolean showHelp, boolean showAbout, boolean showCheat) {
        _showLevel = showLevel;
        _showHelp = showHelp;
        _showAbout = showAbout;
        _showCheat = showCheat;
        boolean showMenu = !_showLevel && !_showAbout && !_showHelp && !_showCheat;
        _levelInfo.setVisible(_showLevel);
        _helpInfo.setVisible(_showHelp);
        _aboutInfo.setVisible(_showAbout);
        _cheatInfo.setVisible(_showCheat);
        _background.setVisible(showMenu);

        _oneButton.setVisible(_showLevel);
        _twoButton.setVisible(_showLevel);
        _threeButton.setVisible(_showLevel);
        _cheatButton.setVisible(showMenu);
        _exitButton.setVisible(showMenu);
        _helpButton.setVisible(showMenu);
        _aboutButton.setVisible(showMenu);
        _startButton.setVisible(showMenu);
        _menuButton.setVisible(!showMenu);
    }
}

