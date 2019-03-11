package com.symbol.scene;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.symbol.ecs.Mapper;
import com.symbol.ecs.component.HealthComponent;
import com.symbol.game.Config;
import com.symbol.game.Symbol;

public class Hud extends Scene {

    private static final String HP_PROMPT = "HP";
    private static final Vector2 HP_PROMPT_POSITION = new Vector2(5, 108);

    private static final Vector2 HP_BAR_POSITION = new Vector2(16, 108.3f);
    private static final float HP_BAR_WIDTH = 44f;
    private static final float HP_BAR_HEIGHT = 4;

    private static final Vector2 SETTINGS_BUTTON_POSITION = new Vector2(183, 105);

    private static final float HP_BAR_DECAY_RATE = 18.f;

    private Entity player;

    private float hpBarWidth;
    private float decayingHpBarWidth;
    private boolean startHpBarDecay = false;

    private Label fps;

    public Hud(final Symbol game, Entity player) {
        super(game);
        this.player = player;

        createHealthBar();
        createSettingsButton();

        if (Config.DEBUG) {
            fps = new Label("", new Label.LabelStyle(game.getRes().getFont(), Color.BLACK));
            fps.setPosition(5, 5);
            stage.addActor(fps);
        }
    }

    private void createHealthBar() {
        Label.LabelStyle labelStyle = new Label.LabelStyle(game.getRes().getFont(), Color.BLACK);
        Label hpPromptLabel = new Label(HP_PROMPT, labelStyle);
        hpPromptLabel.setPosition(HP_PROMPT_POSITION.x, HP_PROMPT_POSITION.y);

        stage.addActor(hpPromptLabel);
    }

    private void createSettingsButton() {
        ImageButton.ImageButtonStyle style = game.getRes().getButtonStyle("settings");
        ImageButton settingsButton = new ImageButton(style);
        settingsButton.setPosition(SETTINGS_BUTTON_POSITION.x, SETTINGS_BUTTON_POSITION.y);

        stage.addActor(settingsButton);
    }

    @Override
    public void update(float dt) {
        if (Config.DEBUG) fps.setText(Gdx.graphics.getFramesPerSecond() + " FPS");

        HealthComponent health = Mapper.INSTANCE.getHEALTH_MAPPER().get(player);
        hpBarWidth = HP_BAR_WIDTH * ((float) health.getHp() / health.getMaxHp());
        if (health.getHpChange()) {
            decayingHpBarWidth = HP_BAR_WIDTH * ((float) health.getHpDelta() / health.getMaxHp());
            startHpBarDecay = true;
            health.setHpChange(false);
        }
        if (startHpBarDecay) {
            decayingHpBarWidth -= HP_BAR_DECAY_RATE * dt;
            if (decayingHpBarWidth <= 0) {
                decayingHpBarWidth = 0;
                startHpBarDecay = false;
            }
        }
    }

    @Override
    public void render(float dt) {
        game.getBatch().setProjectionMatrix(stage.getCamera().combined);
        game.getBatch().begin();

        game.getBatch().draw(game.getRes().getTexture("black"), HP_BAR_POSITION.x, HP_BAR_POSITION.y,
                HP_BAR_WIDTH + 2, HP_BAR_HEIGHT + 2);
        game.getBatch().draw(game.getRes().getTexture("hp_bar_bg_color"), HP_BAR_POSITION.x + 1, HP_BAR_POSITION.y + 1,
                HP_BAR_WIDTH, HP_BAR_HEIGHT);
        game.getBatch().draw(game.getRes().getTexture("hp_bar_green"), HP_BAR_POSITION.x + 1, HP_BAR_POSITION.y + 1,
                hpBarWidth, HP_BAR_HEIGHT);

        if (startHpBarDecay) {
            game.getBatch().draw(game.getRes().getTexture("hp_bar_color"),
                    HP_BAR_POSITION.x + 1 + hpBarWidth, HP_BAR_POSITION.y + 1,
                    decayingHpBarWidth, HP_BAR_HEIGHT);
        }

        game.getBatch().end();

        stage.act(dt);
        stage.draw();
    }

}