define(["jquery", "lodash", "ldsh!board.lodash"], function ($, _, template) {
  "use strict";

  var getToken = function (board, position) {
    if (_.includes(board.x, position)) {
      return "X";
    } else if (_.includes(board.o, position)) {
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

  return function (game) {
    return $(template({ heading: buildHeading(game), board: buildBoard(game) }));
  }
});
