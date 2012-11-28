(function($) {
	
	$(document).ready(function() {
		// 댓글 달기 이벤트 세팅
		$('.content-comment-one span').bind('click', function(e) {
			switch ($(e.currentTarget).attr('class')) {

			case 'content-comment-like':
				alert('content-comment-like event');
				break;
			case 'content-comment-reply':
				location.href = 'news_feed_detail.html';
				break;
			}
		});

		//공지사항 버튼 클릭 이벤트 세팅
		$('#news .btn_title').bind('click', function() {
			location.href='news_feed_write_select.html';
		});
	});

})(jQuery);
