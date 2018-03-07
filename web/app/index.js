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

  var API_HOME = "/tictactoe";

  var displayGame = function (game) {
    var play = game.getLink("urn:jdesrosiers:tictactoe:play");

    var $board = board({
      game: game.value(),
      onPlay: function () {
        var message = this ? { position: this.id } : {};
        play.follow(message, false).getData(displayGame);
      },
      onNewGame: function () {
        location.reload();
      }
    });

    $("#content").html($board);
  };

  Jsonary.getData(API_HOME, function (index) {
    var create = index.getLink("urn:jdesrosiers:tictactoe:create");

    newGameForm(create, displayGame, function ($newGameForm) {
      $("#content").html($newGameForm);
    });
  });
});
