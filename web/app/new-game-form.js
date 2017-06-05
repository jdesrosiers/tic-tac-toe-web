define(["jquery", "alpaca"], function ($, alpaca) {
  "use strict";

  return function (schema, onSubmit) {
    return $("<div>").alpaca({
      schema: schema,
      options: {
        form: {
          buttons: {
            submit: {
              title: "Play!",
              click: function () {
                onSubmit(this.getValue());
              }
            }
          }
        }
      }
    });
  };
});
