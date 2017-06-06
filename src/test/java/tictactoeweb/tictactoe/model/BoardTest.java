package tictactoeweb.tictactoe.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

import java.io.IOException;

import scala.Symbol;

import com.fasterxml.jackson.core.JsonProcessingException;

import json.Json;

public class BoardTest {

    @Test
    public void itShouldBeSerializableToJson() throws JsonProcessingException {
        Board board = new Board();
        String json = "{\"player\":\"X\",\"xs\":[],\"os\":[]}";
        assertThat(Json.stringify(board), equalTo(json));
    }

    @Test
    public void itShouldDeserializeJsonToABoard() throws IOException, JsonProcessingException {
        String json = "{\"player\":\"X\",\"xs\":[],\"os\":[]}";
        Board board = Json.parseAs(json, Board.class);
        assertThat(board.player(), equalTo(Symbol.apply("X")));
    }

}

