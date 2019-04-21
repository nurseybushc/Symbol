package com.symbol.game.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.symbol.game.data.EntityDetails;
import com.symbol.game.util.Resources;

public class HelpPage extends Table implements Page {

    private static final float ENTITY_DETAILS_IMAGE_SIZE = 32f;
    private static final float SCROLL_PANE_WIDTH = 106f;
    private static final float SCROLL_PANE_HEIGHT = 64;

    private Resources res;

    private Table container;
    private ScrollPane scrollPane;

    private HelpPage(Resources res) {
        this.res = res;
        setBackground(new TextureRegionDrawable(res.getTexture("help_page_bg")));

        container = new Table();
        container.pack();
        container.setTransform(false);

        scrollPane = new ScrollPane(container, res.getSkin());
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.layout();
        scrollPane.setFlingTime(0f);
        scrollPane.setOverscroll(false, false);

        add(scrollPane).size(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT).fill();
    }

    public HelpPage(Resources res, EntityDetails details) {
        this(res);
        createEntityDetailsLayout(details);
    }

    @Override
    public void reset() {
        scrollPane.setScrollY(0f);
    }

    private void createEntityDetailsLayout(EntityDetails details) {
        Label entityTypeLabel = new Label(details.getName(),
                res.getLabelStyle(getColorForEntityType(details.getEntityType())));
        container.add(entityTypeLabel).left().row();

        container.add(new Image(res.getTexture("page_divider")))
                .spaceTop(3f)
                .spaceBottom(3f)
                .left()
                .row();

        Table middleTable = new Table();

        Image entityImage = new Image(details.getImage());
        entityImage.setScaling(Scaling.fit);
        middleTable.add(entityImage)
                .size(ENTITY_DETAILS_IMAGE_SIZE, ENTITY_DETAILS_IMAGE_SIZE)
                .fill()
                .padRight(4f)
                .top();

        Label descriptionLabel = new Label(details.getDescription(),
                res.getLabelStyle(Color.BLACK));
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.topLeft);
        middleTable.add(descriptionLabel)
                .width(64f)
                .fill()
                .padLeft(2f);

        container.add(middleTable).expand().top().row();

        if (!details.getAdditionalInfo().equals("")) {
            container.add(new Image(res.getTexture("page_divider")))
                    .spaceTop(4f)
                    .spaceBottom(3f)
                    .left()
                    .row();
            Label tipLabel = new Label("tip: " + details.getAdditionalInfo(),
                    res.getLabelStyle(new Color(80 / 255.f, 127 / 255.f, 175 / 255.f, 1)));
            tipLabel.setWrap(true);
            tipLabel.setAlignment(Align.topLeft);
            container.add(tipLabel).width(96f).fill().left();
        }
    }

    private Color getColorForEntityType(String entityType) {
        switch (entityType) {
            case "enemy":
                return new Color(220 / 255.f, 0f, 0f, 1f);
            case "terrain":
                return new Color(132 / 255.f, 196 / 255.f, 125 / 255.f, 1);
            default:
                return new Color(114 / 255.f, 184 / 255.f, 1, 1);
        }
    }

}