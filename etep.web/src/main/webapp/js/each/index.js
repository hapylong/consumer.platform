$package('IQB.index');
IQB.index = function() {
	var _this = {
		login : function() {
			_this.hidden_error_msg();
			if(_this.form_check()){
				var option = {
						'userCode':$('#userCode').val(),
						'userPassword':MD5($('#userPassword').val())
				};
				IQB.get(urls['sysmanegeUrl'] + '/sysLogin/login', option, _this.login_call_back)
			}
		},
		show_error_msg : function(val){
			$("#warning").html("<i class='warnImg'></i>"+val);
			$(".warning").css("display","block");
		},
		hidden_error_msg : function(){
			$(".warning").css("display","none");
		},
		login_call_back : function(data){
			if ($.isSucc(data)) {
				window.location.href = 'view/main/main.html';
			} else {
				_this.show_error_msg($.getRetUserInfo(data));
			}
		},
		form_check : function(){
			if(_this.check_is_null($('#userCode').val())){
				_this.show_error_msg("请输入用户名");
				return false;
			}
			if(_this.check_is_null($('#userPassword').val())){
				_this.show_error_msg("请输入密码");
				return false;
			}
			return true;
		},
		check_is_null : function(val){
			if(!val || val == ''){
				return true;
			}
			return false;
		}
	}
	return _this;
}();
$(function() {
	particlesJS('particles-js', {
		  particles: {
		    color: '#fff',
		    shape: 'circle', // "circle", "edge" or "triangle"
		    opacity: 1,
		    size: 4,
		    size_random: true,
		    nb: 400,
		    line_linked: {
		      enable_auto: true,
		      distance: 100,
		      color: '#3A5FCD',
		      opacity: 1,
		      width: 2,
		      condensed_mode: {
		        enable: false,
		        rotateX: 600,
		        rotateY: 600
		      }
		    },
		    anim: {
		      enable: true,
		      speed: 2
		    }
		  },
		  interactivity: {
		    enable: true,
		    mouse: {
		      distance: 250
		    },
		    detect_on: 'canvas', // "canvas" or "window"
		    mode: 'grab',
		    line_linked: {
		      opacity: .5
		    },
		    events: {
		      onclick: {
		        enable: true,
		        mode: 'push', // "push" or "remove" (particles)
		        nb: 4
		      }
		    }
		  },
		  /* Retina Display Support */
		  retina_detect: true
		});
	
	document.onkeydown = function(event) {
		//e = event ? event : (window.event ? window.event : null);
		if (event.keyCode == 13) {
			IQB.index.login();
		}
	}
	/*login*/
	// 输入框获取焦点 改变样式
	$('.logIpt').focus(function(){
		if ($(this).parent().hasClass('log-unsel')) {
			$(this).parent().addClass('log-sel').removeClass('log-unsel');
		}
	});
	$('.logItem').mouseenter(function(){
		if ($(this).hasClass('log-unsel')) {
			$(this).addClass('log-sel').removeClass('log-unsel');
			$(this).siblings('.log-sel').addClass('log-unsel').removeClass('log-sel');
		}
	});
	$('.logItem').mouseleave(function(){
		if ($(this).hasClass('log-sel')) {
			$(this).addClass('log-unsel').removeClass('log-sel');
		}
	})
	
	// 输入框失去焦点 改变样式
	$('.logIpt').blur(function(){
		$(this).parent().addClass('log-unsel').removeClass('log-sel');
	});
	
	//输入框显示清除按钮
	$('.logIpt').keydown(function(){
		if ($(this).val!="") {
			$(this).siblings('.close').show();
			$('.close').click(function(){
				$(this).siblings(".logIpt").val('');
				$(this).hide();
			})
		}
	});
	
	// 清空按钮点击
	$('.logBtnclear').click(function(){
		$('.logItem').each(function(){
			if ($(this).children('.logIpt').val()!='') {
				$(this).children('.logIpt').val('');
				$(this).children('.close').hide();
			}
		})
	});
});


