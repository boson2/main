//상단 메뉴 버튼 이벤트 세팅
(function($) {
	$(document).ready(function() {
		$(window).resize(function(){
			$('#page').css('min-height',$(window).height());
		}).trigger('resize');
		
		$('.content-user-photo,.content-reg-user').click(function(){
			location.href='my_gallery.html';
		});
		
		var globalVal = {
			menuShowFlag : false,
			setting_w:$('#page > header #btn_smart_setting > li').length * 50
		};
		
		$('#page > header #btn_smart_setting').css('width',globalVal.setting_w);
		
		$('#menu_news_feed').height($(document).height());
		
		$('#btn_menu').bind('click', function() {
			if (globalVal.menuShowFlag) {
				$('#page').animate({
					'left' : 0
				}, 200);
				
				$('#menu_news_feed').animate({
					'left' : -240
				}, 200);
				
				globalVal.menuShowFlag = false;
			} else {
				$('#page').animate({
					'left' : 240
				}, 200);
				
				$('#menu_news_feed').animate({
					'left' : 0
				}, 200);
				
				globalVal.menuShowFlag = true;
			}
		});

		$('#btn_smart_setting li').bind('click', function(e) {
			switch (e.currentTarget.id) {
			case 'btn_setting':
				alert('setting event');
				break;
			case 'btn_back':
				history.back();
				break;
			}
		});
		
		$('#btn_smart').toggle(function() {
			$('#notify').show();
		},function(){
			$('#notify').hide();
		});
		
		$('#btn_back').click(function(){
			history.back();
		});
	});
})(jQuery);
