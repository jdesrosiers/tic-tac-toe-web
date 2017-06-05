requirejs.config({
  shim: {
    "bootstrap": ["jquery"],
    "jsonary": { exports: "Jsonary" }
  },
  paths: {
    "jquery": "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min",
    "bootstrap": "https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min",
    "jsonary": "../vendor/jsonary/0.0.18/jsonary-core.min",
    "alpaca": "https://code.cloudcms.com/alpaca/1.5.23/bootstrap/alpaca.min",
    "handlebars": "https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.6/handlebars.amd.min"
  }
});

requirejs(["jquery", "jsonary", "new-game-button", "new-game-form", "bootstrap"], function ($, Jsonary, newGameButton, newGameForm) {
  "use strict";

  Jsonary.getData("/tictactoe", function (index) {
    var createLink = index.getLink("urn:jdesrosiers:tictactoe:create");

    createLink.submissionSchemas.getFull(function (fullSchema) {
      var schema = fullSchema[0].data.value();

      var $newGameForm = newGameForm(schema, function (value) {
        createLink.follow(value, false).getData(function (game) {
          console.log(game.value());
        });
      })

      var $newGameButton = newGameButton().click(function () {
        $("#content").html($newGameForm);
      });

      $("#content").html($newGameButton);
    });
  });
});
