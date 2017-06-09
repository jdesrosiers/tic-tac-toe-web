define(["jquery", "lodash", "ldsh!board.lodash"], function ($, _, template) {
  "use strict";

  var getToken = function (board, position) {
    if (_.includes(board.xs, position)) {
      return "X";
    } else if (_.includes(board.os, position)) {
      return "O";
    } else {
      return "unplayed";
    }
  }

  var buildHeading = function (game) {
    if (game.state === "inProgress") {
      return game.board.player + "'s Turn";
    } else if (game.state === "xWins") {
      return "X Wins!";
    } else if (game.state === "oWins") {
      return "O Wins!";
    } else if (game.state === "draw") {
      return "It's a Draw";
    } else {
      return "???";
    }
  }

  var positions = [
    "topLeft", "topMiddle", "topRight",
    "middleLeft", "center", "middleRight",
    "bottomLeft", "bottomMiddle", "bottomRight"
  ];

  var buildBoard = function (game) {
    return _.reduce(positions, function (board, position) {
      board[position] = getToken(game.board, position);
      return board;
    }, {});
  }

  return function (game, play) {
    var $board = $(template({
      heading: buildHeading(game),
      state: game.state,
      player: game.player,
      board: buildBoard(game)
    }));

    if (game.state == "inProgress") {
      if (game.player == "human") {
        $board.find(".unplayed").click(play);
      } else {
        play();
      }
    }

    return $board;
  }
});
