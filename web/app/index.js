requirejs.config({
  shim: {
    "bootstrap": ["jquery"]
  },
  paths: {
    "jquery": "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min",
    "bootstrap": "https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min",
  }
});

requirejs(["jquery", "bootstrap"], function ($) {
  "use strict";

  console.log("requirejs");
});
