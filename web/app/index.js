requirejs.config({
  shim: {
    "bootstrap": ["jquery"],
    "jsonary": { exports: "Jsonary" }
  },
  paths: {
    "jquery": "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min",
    "bootstrap": "https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min",
    "jsonary": "../vendor/jsonary/0.0.18/jsonary-core.min",
    "handlebars": "https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.6/handlebars.amd.min",
    "alpaca": "https://code.cloudcms.com/alpaca/1.5.23/bootstrap/alpaca.min",
    "lodash": "https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.4/lodash.min",
    "ldsh": "../vendor/lodash-template-loader/2.0.0/loader"
  }
});

requirejs(["jquery", "jsonary", "new-game-form", "board", "bootstrap"], function ($, Jsonary, newGameForm, board) {
  "use strict";

  var displayGame = function (game) {
    var $board = board(game.value());

    newGameForm(game.getLink("urn:jdesrosiers:tictactoe:create"), displayGame, function ($newGameForm) {
      $board.find("#new-game").click(function () {
        $("#content").html($newGameForm);
      });
    });

    $("#content").html($board);
  }

  Jsonary.getData("/tictactoe/1.json", displayGame);
});
