$('.star-rating').raty({
    path: '/images/',
    readOnly: true,
    score: function() {
          return $(this).attr("data-score");
  }
});