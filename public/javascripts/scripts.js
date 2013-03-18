(function($){

    /**
     * Document Ready Intializations
    */
	$(document).ready(function(){
	
		// inin newsletter form ajax
		initNewsletterAjax();
	});
	

    /**
     * Window onLoad Initializations
    */
    $(window).load(function(){
        $(window).scroll(function(){
            stickyNewsletter();
        });
    });

    /**
     * Initialize Newsletter Ajax Signup Functionality
    */
	function initNewsletterAjax()
	{
		// set vars
		var form = $('#subscribe')
			actionUrl = form.attr('action');

        // set confirmation code
        $("input[name='code']").val(extras);

		// add submit event to form
		form.submit(function(e){
			
			e.preventDefault();
			var email = $('.input-text', form).val();
			
			// if input is empty, display message
			if('' == email){
				showMessage('invalid', 'Пожалуйста, введите адрес электронной почты');
				return;
			}
			
			// show Progress
			showMessage('progress', subscribe_progress);
			
			// submit the form using Ajax
			$.ajax({
	  			url: actionUrl,
	  			data: form.serialize(),
	  			dataType: 'json',
	  			success: function(d){
	  				if('null' == d){
						showMessage('error', 'Что-то пошло не так.');
		  				
	  				}else{
		  				if(d.result){
		  					showMessage('success', d.msg);
		  				}else{
		  					showMessage('invalid', d.msg);
		  				}
	  				}
	  			},
	  			error: function(d){
					showMessage('error', 'Что-то пошло не так.');
	  			}
			});
			
		});
	}
	
	
	/**
	 * Check if given string is an Email
	*/
	function isEmail(email) {
		var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		return regex.test(email);
	}

	
	/**
	 * Display Newsletter Input Message with Given Class and Text String
	*/
	function showMessage(className, msg)
	{	
		// set vars
		var msgCont = $('#subscribe-result');
		
		// if action is to hide message
		if(className == 'hide'){
			msgCont.animate({opacity: 0}, 150, function(){msgCont.html('')});
				
		// otherwise display appropriate message
		}else{
			msgCont.animate({opacity: 0}, 150, function(){
				msgCont.removeClass();
				msgCont.addClass(className).html(msg).animate({opacity: 1}, 150);
			});
			
			// hide message automatically after X seconds
			//setTimeout(function(){showMessage('hide')}, 6000);
		}
	}

    function stickyNewsletter()
    {
        if(isMobile()){
            return;
        }
        // declare vars
        var sbscrCont = $('.subscribe-container'),
            offsetTop = 570
        isFixed = sbscrCont.hasClass('fixed');
        windowTop = $(document).scrollTop();

        // fix the position
        if(windowTop > offsetTop && !isFixed){
            sbscrCont
                .hide(0)
                .addClass('fixed')
                .slideDown(250);

            // make element static
        }else if(windowTop < offsetTop && isFixed){
            sbscrCont
                .slideUp(100, function(){
                    sbscrCont
                        .removeClass('fixed')
                        .fadeIn(250);
                });
        }
    }


    function isMobile(){
        return (
            (navigator.userAgent.toLowerCase().indexOf("ipad") > -1) ||
                (navigator.userAgent.toLowerCase().indexOf("iphone") > -1) ||
                (navigator.userAgent.toLowerCase().indexOf("ipod") > -1)
            );
    }

}(jQuery))