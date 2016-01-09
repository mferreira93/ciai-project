$(window).load(function() {
	$('.header').click(function(){
		if($(this).hasClass("collapsed")){
		    $(this).nextUntil('tr.header')
		    .find('td')
		    .parent()
		    .find('td > div')
		    .slideDown("fast", function(){
		        var $set = $(this);
		        $set.replaceWith($set.contents());
		    });
		    $(this).removeClass("collapsed");
		} else {
		    $(this).nextUntil('tr.header')
		    .find('td')
		    .wrapInner('<div style="display: block;" />')
		    .parent()
		    .find('td > div')
		    .slideUp("fast");
		    $(this).addClass("collapsed");
		}
	});
});