$(document).ready(function(){
	$('#page').css({
		'padding-bottom':50
	});
	
	$('#content-detail .content-item .content-detail-like-count strong').click(function(){
		location.href='news_feed_likeList.html';
	});
});