package com.symbol.game.scene;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.symbol.game.ecs.Mapper;
import com.symbol.game.ecs.component.HealthComponent;
import com.symbol.game.Config;
import com.symbol.game.Symbol;
import com.symbol.game.ecs.component.PlayerComponent;
import com.symbol.game.ecs.entity.EntityColor;

public class Hud extends Scene {

    private static final Vector2 HP_ICON_POSITION = new Vector2(5, 107.3f);
    private static final Vector2 HP_BAR_POSITION = new Vector2(16, 108.3f);
    private static final float HP_BAR_WIDTH = 44f;
    private static final float HP_BAR_HEIGHT = 4;

    private static final Vector2 CHARGE_ICON_POSITION = new Vector2(5.5f, 98);
    private static final Vector2 CHARGE_BAR_POSITION = new Vector2(16, 99.3f);
    private static final float CHARGE_BAR_WIDTH = HP_BAR_WIDTH;
    private static final float CHARGE_BAR_HEIGHT = 2;

    private static final Vector2 SETTINGS_BUTTON_POSITION = new Vector2(183, 105);

    private static final float HP_BAR_DECAY_RATE = 18.f;
    private static final float CHARGE_BAR_ACTIVATION_TIME = 0.3f;

    private Entity player;

    private float hpBarWidth;
    private float decayingHpBarWidth;
    private boolean startHpBarDecay = false;

    private float chargeBarWidth;
    private Image chargeBarIcon;
    private TextureRegionDrawable chargeBarTiers[] = new TextureRegionDrawable[4];

    private Label fps;

    public Hud(final Symbol game, Entity player) {
        super(game);
        this.player = player;

        createHealthBar();
        createSettingsButton();
        createChargeBar();

        if (Config.DEBUG) {
            fps = new Label("", new Label.LabelStyle(game.getRes().getFont(), Color.BLACK));
            fps.setPosition(5, 5);
            stage.addActor(fps);
        }
    }

    private void createHealthBar() {
        Image hpBarIcon = new Image(game.getRes().getTexture("player_hp_icon"));
        hpBarIcon.setPosition(HP_ICON_POSITION.x, HP_ICON_POSITION.y);

        stage.addActor(hpBarIcon);
    }

    private void createSettingsButton() {
        ImageButton.ImageButtonStyle style = game.getRes().getButtonStyle("settings");
        ImageButton settingsButton = new ImageButton(style);
        settingsButton.setPosition(SETTINGS_BUTTON_POSITION.x, SETTINGS_BUTTON_POSITION.y);

        stage.addActor(settingsButton);
    }

    private void createChargeBar() {
        chargeBarTiers[0] = new TextureRegionDrawable(game.getRes().getTexture("charge_bar_icon"));
        for (int i = 2; i <= 4; i++) {
            chargeBarTiers[i - 1] = new TextureRegionDrawable(game.getRes().getTexture("charge_bar_icon" + i));
        }

        chargeBarIcon = new Image(chargeBarTiers[0]);
        chargeBarIcon.setPosition(CHARGE_ICON_POSITION.x, CHARGE_ICON_POSITION.y);
        chargeBarIcon.setVisible(false);

        stage.addActor(chargeBarIcon);
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

        PlayerComponent playerComp = Mapper.INSTANCE.getPLAYER_MAPPER().get(player);
        if (playerComp.getChargeTime() >= CHARGE_BAR_ACTIVATION_TIME) {
            if (!chargeBarIcon.isVisible()) chargeBarIcon.setVisible(true);
            chargeBarWidth = CHARGE_BAR_WIDTH * (playerComp.getChargeTime() / 2.4f);
            if (chargeBarWidth > CHARGE_BAR_WIDTH) chargeBarWidth = CHARGE_BAR_WIDTH;
            chargeBarIcon.setDrawable(chargeBarTiers[playerComp.getDamage() - 1]);
        }
        else {
            if (chargeBarIcon.isVisible()) chargeBarIcon.setVisible(false);
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

        PlayerComponent playerComp = Mapper.INSTANCE.getPLAYER_MAPPER().get(player);
        if (playerComp.getChargeTime() >= CHARGE_BAR_ACTIVATION_TIME) {
            game.getBatch().draw(game.getRes().getTexture("black"), CHARGE_BAR_POSITION.x, CHARGE_BAR_POSITION.y,
                    CHARGE_BAR_WIDTH + 2, CHARGE_BAR_HEIGHT + 2);
            game.getBatch().draw(game.getRes().getTexture("hp_bar_bg_color"), CHARGE_BAR_POSITION.x + 1, CHARGE_BAR_POSITION.y + 1,
                    CHARGE_BAR_WIDTH, CHARGE_BAR_HEIGHT);
            String hex = playerComp.getDamage() == 1 ? EntityColor.INSTANCE.getProjectileColor("p_dot")
                    : EntityColor.INSTANCE.getProjectileColor("p_dot" + playerComp.getDamage());
            game.getBatch().draw(game.getRes().getTexture(hex), CHARGE_BAR_POSITION.x + 1, CHARGE_BAR_POSITION.y + 1,
                    chargeBarWidth, CHARGE_BAR_HEIGHT);
        }

        game.getBatch().end();

        stage.act(dt);
        stage.draw();
    }

}
