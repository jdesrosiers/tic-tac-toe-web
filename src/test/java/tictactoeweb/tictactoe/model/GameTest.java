package tictactoeweb.tictactoe.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

import java.io.IOException;

import scala.Symbol;

import com.fasterxml.jackson.core.JsonProcessingException;

import json.Json;

public class GameTest {

    @Test
    public void itShouldCreateANewGameWithPlayerTypes() {
        Game game = new Game("human", "minimax");

        assertThat(game.getPlayerX(), equalTo("human"));
        assertThat(game.getPlayerO(), equalTo("minimax"));
        assertThat(game.getState(), equalTo("inProgress"));
    }

    @Test
    public void itShouldTellIfAPositionCanBePlayed() {
        Game game = new Game("human", "minimax");

        assertThat(game.canPlay("center"), equalTo(true));
        game.play("center");
        assertThat(game.canPlay("center"), equalTo(false));
    }

    @Test
    public void itShouldPlayAPostion() {
        Game game = new Game("human", "minimax");

        game.play("center");

        assertThat(game.getBoard().xs().contains(Symbol.apply("center")), equalTo(true));
    }

    @Test
    public void itShouldUpdateTheGameStateWhenAPositionIsPlayed() {
        Game game = new Game("human", "minimax");

        game.play("topLeft");
        game.play("middleLeft");
        game.play("topMiddle");
        game.play("center");
        game.play("topRight");

        assertThat(game.getState(), equalTo("xWins"));
    }

    @Test
    public void itShouldBeSerializableToJson() throws JsonProcessingException {
        Game game = new Game("human", "minimax");
        String json = "{\"id\":1,\"playerX\":\"human\",\"playerO\":\"minimax\",\"board\":{\"player\":\"X\",\"xs\":[],\"os\":[]},\"state\":\"inProgress\"}";
        assertThat(Json.stringify(game), equalTo(json));
    }

    @Test
    public void itShouldDeserializeJsonToAGame() throws IOException, JsonProcessingException {
        String json = "{\"id\":1,\"playerX\":\"human\",\"playerO\":\"minimax\",\"board\":{\"player\":\"X\",\"xs\":[],\"os\":[]},\"state\":\"inProgress\"}";
        Game game = Json.parseAs(json, Game.class);
        assertThat(game.getId(), equalTo(1));
    }

}
