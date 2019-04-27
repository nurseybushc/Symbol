package com.symbol.game.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.symbol.game.Config;
import com.symbol.game.Symbol;
import com.symbol.game.scene.Scene;

public class AndroidInput extends Scene {

    private static final float DIRECTIONAL_BUTTON_SIZE = 30f;
    private static final float ACTION_BUTTON_SIZE = 30f;

    private static final Vector2 LEFT_BUTTON_POSITION = new Vector2(5, 10);
    private static final Vector2 RIGHT_BUTTON_POSITION = new Vector2(35, 10);
    private static final Vector2 JUMP_BUTTON_POSITION = new Vector2(134, 3);
    private static final Vector2 SHOOT_BUTTON_POSITION = new Vector2(164, 25);

    private KeyInputHandler keyInputHandler;

    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageButton jumpButton;
    private ImageButton shootButton;

    public AndroidInput(final Symbol game, KeyInputHandler keyInputHandler, Stage stage, Viewport viewport) {
        super(game, stage, viewport);
        this.keyInputHandler = keyInputHandler;

        if (Config.INSTANCE.onAndroid()) {
            createDirectionalButtons();
            createActionButtons();
        }
    }

    private void createDirectionalButtons() {
        final ImageButton.ImageButtonStyle leftUp = new ImageButton.ImageButtonStyle();
        leftUp.imageUp = new TextureRegionDrawable(game.getRes().getTexture("button_left_up"));
        final ImageButton.ImageButtonStyle leftDown = new ImageButton.ImageButtonStyle();
        leftDown.imageUp = new TextureRegionDrawable(game.getRes().getTexture("button_left_down"));

        leftButton = new ImageButton(game.getRes().getImageButtonStyle("left"));
        leftButton.setPosition(LEFT_BUTTON_POSITION.x, LEFT_BUTTON_POSITION.y);
        leftButton.setSize(DIRECTIONAL_BUTTON_SIZE, DIRECTIONAL_BUTTON_SIZE);

        leftButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                keyInputHandler.move(false);
                leftButton.setStyle(leftDown);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                keyInputHandler.stop(false);
                leftButton.setStyle(leftUp);
            }
        });

        final ImageButton.ImageButtonStyle rightUp = new ImageButton.ImageButtonStyle();
        rightUp.imageUp = new TextureRegionDrawable(game.getRes().getTexture("button_right_up"));
        final ImageButton.ImageButtonStyle rightDown = new ImageButton.ImageButtonStyle();
        rightDown.imageUp = new TextureRegionDrawable(game.getRes().getTexture("button_right_down"));

        rightButton = new ImageButton(game.getRes().getImageButtonStyle("right"));
        rightButton.setPosition(RIGHT_BUTTON_POSITION.x, RIGHT_BUTTON_POSITION.y);
        rightButton.setSize(DIRECTIONAL_BUTTON_SIZE, DIRECTIONAL_BUTTON_SIZE);

        rightButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                keyInputHandler.move(true);
                rightButton.setStyle(rightDown);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                keyInputHandler.stop(true);
                rightButton.setStyle(rightUp);
            }
        });

        stage.addActor(leftButton);
        stage.addActor(rightButton);
    }

    private void createActionButtons() {
        jumpButton = new ImageButton(game.getRes().getImageButtonStyle("jump"));
        jumpButton.setPosition(JUMP_BUTTON_POSITION.x, JUMP_BUTTON_POSITION.y);
        jumpButton.setSize(ACTION_BUTTON_SIZE, ACTION_BUTTON_SIZE);

        jumpButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                keyInputHandler.jump();
                return true;
            }
        });

        shootButton = new ImageButton(game.getRes().getImageButtonStyle("shoot"));
        shootButton.setPosition(SHOOT_BUTTON_POSITION.x, SHOOT_BUTTON_POSITION.y);
        shootButton.setSize(ACTION_BUTTON_SIZE, ACTION_BUTTON_SIZE);

        shootButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                keyInputHandler.startCharge();
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                keyInputHandler.endCharge();
            }
        });

        stage.addActor(jumpButton);
        stage.addActor(shootButton);
    }

    public void toggle(boolean toggle) {
        if (Config.INSTANCE.onAndroid()) {
            leftButton.setVisible(toggle);
            rightButton.setVisible(toggle);
            jumpButton.setVisible(toggle);
            shootButton.setVisible(toggle);
        }
    }

    @Override
    public void update(float dt) {}

    @Override
    public void render(float dt) {}

    @Override
    public void dispose() {}

}
