define(["jquery", "alpaca"], function ($, alpaca) {
  "use strict";

  var newGameForm = function (schema, onSubmit) {
    return $('<div class="create-game-form">').alpaca({
      schema: schema,
      options: {
        form: {
          buttons: {
            submit: {
              title: "Play!",
              id: "play-button",
              click: function () {
                onSubmit(this.getValue());
              }
            }
          }
        }
      }
    });
  };

  return function (createLink, click, callback) {
    createLink.submissionSchemas.getFull(function (fullSchema) {
      var $newGameForm = newGameForm(fullSchema[0].data.value(), function (value) {
        createLink.follow(value, false).getData(click);
      });

      callback($newGameForm);
    });
  };

});
